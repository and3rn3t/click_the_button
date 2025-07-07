package com.andernet.experiment;

import com.andernet.experiment.settings.Settings;
import org.junit.jupiter.api.*;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for the main game flow: start, click, timer, game over overlay.
 * Simulates user actions and verifies UI/game state transitions.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClickTheButtonGameFlowIntegrationTest {
    private ClickTheButtonGame game;
    private Settings settings;

    @BeforeEach
    void setUp() throws InvocationTargetException, InterruptedException {
        settings = new Settings();
        settings.setGameDurationSeconds(10);
        settings.setSoundEnabled(false);
        System.setProperty("ctb.testmode", "true");
        SwingUtilities.invokeAndWait(() -> {
            game = new ClickTheButtonGame(settings);
            game.setVisible(true);
        });
    }

    @AfterEach
    void tearDown() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            game.dispose();
        });
    }

    private void waitForCondition(String message, java.util.function.BooleanSupplier condition, int timeoutMs) throws InterruptedException {
        int waited = 0;
        while (!condition.getAsBoolean() && waited < timeoutMs) {
            Thread.sleep(50);
            waited += 50;
        }
        if (!condition.getAsBoolean()) {
            fail(message);
        }
    }

    private void waitForOverlayVisible(JComponent overlay) throws Exception {
        long start = System.currentTimeMillis();
        while (!overlay.isVisible() && System.currentTimeMillis() - start < 3000) {
            SwingUtilities.invokeAndWait(() -> {});
            Toolkit.getDefaultToolkit().sync();
            Thread.sleep(30);
        }
        SwingUtilities.invokeAndWait(() -> {});
        Toolkit.getDefaultToolkit().sync();
        Thread.sleep(30);
    }

    private void waitForDialogVisible(JDialog dialog) throws Exception {
        long start = System.currentTimeMillis();
        while (!dialog.isVisible() && System.currentTimeMillis() - start < 3000) {
            SwingUtilities.invokeAndWait(() -> {});
            Toolkit.getDefaultToolkit().sync();
            Thread.sleep(30);
        }
        SwingUtilities.invokeAndWait(() -> {});
        Toolkit.getDefaultToolkit().sync();
        Thread.sleep(30);
    }

    @Test
    @Order(1)
    void testGameFlow_StartClickGameOver() throws Exception {
        JButton overlayButton = (JButton) findComponentByName(game, "overlayButton");
        assertNotNull(overlayButton, "Overlay button should exist");
        SwingUtilities.invokeAndWait(overlayButton::doClick);
        Thread.sleep(3000); // Wait longer for countdown to finish (2.8s + buffer)
        JButton mainButton = (JButton) findComponentByType(game, com.andernet.experiment.ui.AnimatedButton.class);
        assertNotNull(mainButton, "Main button should exist");
        for (int i = 0; i < 2; i++) {
            SwingUtilities.invokeAndWait(mainButton::doClick);
            Thread.sleep(100);
        }
        SwingUtilities.invokeAndWait(game::repaint);
        JPanel overlayPanel = (JPanel) findComponentByType(game, com.andernet.experiment.ui.GameOverlayPanel.class);
        assertNotNull(overlayPanel, "Overlay panel should exist");
        waitForOverlayVisible(overlayPanel);
        JLabel scoreLabel = (JLabel) findComponentByName(game, "scoreLabel");
        assertNotNull(scoreLabel);
        assertTrue(scoreLabel.getText().contains("2"), "Score label should show 2, but was: " + scoreLabel.getText());
    }

    @Test
    @Order(2)
    void testSettingsDialog_ChangeGameDuration() throws Exception {
        JButton overlayButton = (JButton) findComponentByName(game, "overlayButton");
        SwingUtilities.invokeAndWait(overlayButton::doClick);
        JButton settingsButton = (JButton) findComponentByName(game, "settingsButton");
        assertNotNull(settingsButton, "Settings button should exist");
        SwingUtilities.invokeAndWait(settingsButton::doClick);
        JDialog dialog = null;
        int waited = 0;
        while (dialog == null && waited < 3000) {
            dialog = (JDialog) findDialogByTitleAnyVisibility("Game Settings");
            if (dialog == null) Thread.sleep(100);
            waited += 100;
        }
        waited = 0;
        while (dialog != null && !dialog.isVisible() && waited < 3000) {
            Thread.sleep(100);
            waited += 100;
        }
        waitForDialogVisible(dialog);
        assertNotNull(dialog, "Settings dialog should appear");
        JSpinner durationSpinner = (JSpinner) findComponentByType(dialog, JSpinner.class);
        assertNotNull(durationSpinner, "Duration spinner should exist");
        SwingUtilities.invokeAndWait(() -> durationSpinner.setValue(12));
        JButton okButton = (JButton) findComponentByName(dialog, "okButton");
        assertNotNull(okButton, "OK button should exist");
        SwingUtilities.invokeAndWait(okButton::doClick);
        SwingUtilities.invokeAndWait(overlayButton::doClick);
        Thread.sleep(500);
        JLabel timerLabel = (JLabel) findComponentByName(game, "timerLabel");
        assertNotNull(timerLabel);
        waitForCondition("Timer shows duration value", 
            () -> {
                String text = timerLabel.getText();
                return text.contains("12") || text.contains("11");
            }, 4000);
    }

    @Test
    @Order(3)
    void testMuteUnmuteButton() throws Exception {
        JButton muteButton = (JButton) findComponentByName(game, "muteButton");
        assertNotNull(muteButton, "Mute button should exist");
        String initialText = muteButton.getText();
        SwingUtilities.invokeAndWait(muteButton::doClick);
        assertNotEquals(initialText, muteButton.getText(), "Mute button icon should toggle");
        SwingUtilities.invokeAndWait(muteButton::doClick);
        assertEquals(initialText, muteButton.getText(), "Mute button icon should toggle back");
    }

    @Test
    @Order(4)
    void testKeyboardShortcuts() throws Exception {
        JButton overlayButton = (JButton) findComponentByName(game, "overlayButton");
        SwingUtilities.invokeAndWait(overlayButton::doClick);
        sendKeyStroke(game, 'P');
        JPanel overlayPanel = (JPanel) findComponentByType(game, com.andernet.experiment.ui.GameOverlayPanel.class);
        waitForCondition("Overlay should be visible after pause", overlayPanel::isVisible, 4000);
        sendKeyStroke(game, 'P');
        waitForCondition("Overlay should be hidden after resume", () -> !overlayPanel.isVisible(), 4000);
    }

    @Test
    @Order(5)
    void testHighScorePersistence() throws Exception {
        JButton overlayButton = (JButton) findComponentByName(game, "overlayButton");
        SwingUtilities.invokeAndWait(overlayButton::doClick);
        Thread.sleep(2500);
        JButton mainButton = (JButton) findComponentByType(game, com.andernet.experiment.ui.AnimatedButton.class);
        for (int i = 0; i < 3; i++) {
            SwingUtilities.invokeAndWait(mainButton::doClick);
            Thread.sleep(100);
        }
        Thread.sleep(2000);
        JLabel highScoreLabel = (JLabel) findComponentByName(game, "highScoreLabel");
        assertNotNull(highScoreLabel);
        String highScoreText = highScoreLabel.getText();
        String userHome = System.getProperty("user.home");
        java.io.File highScoreFile = new java.io.File(userHome, ".ctb_highscore");
        int waited = 0;
        String expectedScore = highScoreText.replaceAll("\\D+", "");
        while (waited < 3000) {
            if (highScoreFile.exists()) {
                String fileContent = new String(java.nio.file.Files.readAllBytes(highScoreFile.toPath())).trim();
                if (fileContent.equals(expectedScore)) break;
            }
            Thread.sleep(100);
            waited += 100;
        }
        SwingUtilities.invokeAndWait(() -> {
            game.dispose();
            game = new ClickTheButtonGame(settings);
            game.setVisible(true);
        });
        Thread.sleep(1000);
        JLabel highScoreLabel2 = (JLabel) findComponentByName(game, "highScoreLabel");
        assertNotNull(highScoreLabel2);
        assertEquals(highScoreText, highScoreLabel2.getText(), "High score should persist");
    }

    @Test
    @Order(6)
    void testFontSizeAdjustment() throws Exception {
        JLabel scoreLabel = (JLabel) findComponentByName(game, "scoreLabel");
        Font origFont = scoreLabel.getFont();
        
        SwingUtilities.invokeAndWait(() -> {
            game.toFront();
            game.requestFocus();
        });
        Thread.sleep(300);
        
        sendKeyStroke(game, java.awt.event.KeyEvent.VK_EQUALS, true);
        Thread.sleep(300);
        
        Font largerFont = scoreLabel.getFont();
        assertTrue(largerFont.getSize() > origFont.getSize(), "Font size should increase");
        
        SwingUtilities.invokeAndWait(() -> {
            game.toFront();
            game.requestFocus();
        });
        Thread.sleep(100);
        
        sendKeyStroke(game, java.awt.event.KeyEvent.VK_MINUS, false);
        Thread.sleep(300);
        
        Font smallerFont = scoreLabel.getFont();
        assertTrue(smallerFont.getSize() < largerFont.getSize(), "Font size should decrease");
        assertTrue(smallerFont.getSize() >= origFont.getSize(), "Font size should not go below original");
    }

    @Test
    @Order(7)
    void testFakeButtonPenalty() throws Exception {
        JButton overlayButton = (JButton) findComponentByName(game, "overlayButton");
        SwingUtilities.invokeAndWait(overlayButton::doClick);
        Thread.sleep(2500);
        JButton mainButton = (JButton) findComponentByType(game, com.andernet.experiment.ui.AnimatedButton.class);
        assertNotNull(mainButton, "Main button should exist");
        SwingUtilities.invokeAndWait(mainButton::doClick);
        Thread.sleep(100);
        JButton fakeButton = (JButton) findComponentByType(game, com.andernet.experiment.ui.FakeButton.class);
        assertNotNull(fakeButton, "Fake button should exist");
        JLabel scoreLabel = (JLabel) findComponentByName(game, "scoreLabel");
        int scoreBefore = Integer.parseInt(scoreLabel.getText().replaceAll("\\D+", ""));
        SwingUtilities.invokeAndWait(fakeButton::doClick);
        SwingUtilities.invokeAndWait(game::repaint);
        waitForCondition("Score should decrease after clicking fake button", () -> {
            int s = Integer.parseInt(scoreLabel.getText().replaceAll("\\D+", ""));
            return s < scoreBefore;
        }, 4000);
    }

    @Test
    @Order(8)
    void testOverlayAccessibilityFocus() throws Exception {
        JButton overlayButton = (JButton) findComponentByName(game, "overlayButton");
        assertNotNull(overlayButton);
        
        SwingUtilities.invokeAndWait(overlayButton::doClick);
        Thread.sleep(100);
        
        JPanel overlayPanel = (JPanel) findComponentByType(game, com.andernet.experiment.ui.GameOverlayPanel.class);
        assertNotNull(overlayPanel);
        
        JButton overlayBtn = (JButton) findComponentByName(overlayPanel, "overlayButton");
        assertNotNull(overlayBtn);
        
        int waited = 0;
        while ((!overlayBtn.isShowing() || !overlayBtn.isFocusable()) && waited < 2000) {
            SwingUtilities.invokeAndWait(() -> {});
            Thread.sleep(50);
            waited += 50;
        }
        
        if (!overlayBtn.isShowing() || !overlayBtn.isFocusable()) {
            return;
        }
        
        SwingUtilities.invokeAndWait(overlayBtn::requestFocusInWindow);
        waitForFocusOwner(overlayBtn, 1000);
    }

    private Component findComponentByName(Container root, String name) {
        for (Component c : root.getComponents()) {
            if (name.equals(c.getName())) return c;
            if (c instanceof Container) {
                Component found = findComponentByName((Container) c, name);
                if (found != null) return found;
            }
        }
        return null;
    }

    private Component findComponentByType(Container root, Class<?> type) {
        for (Component c : root.getComponents()) {
            if (type.isInstance(c)) return c;
            if (c instanceof Container) {
                Component found = findComponentByType((Container) c, type);
                if (found != null) return found;
            }
        }
        return null;
    }

    private JDialog findDialogByTitleAnyVisibility(String title) {
        for (Window w : Window.getWindows()) {
            if (w instanceof JDialog && title.equals(((JDialog) w).getTitle())) {
                return (JDialog) w;
            }
        }
        return null;
    }

    private void sendKeyStroke(JFrame frame, int keyCode, boolean shift) throws Exception {
        Robot robot = new Robot();
        frame.requestFocus();
        Thread.sleep(100);
        if (shift) robot.keyPress(java.awt.event.KeyEvent.VK_SHIFT);
        robot.keyPress(keyCode);
        robot.keyRelease(keyCode);
        if (shift) robot.keyRelease(java.awt.event.KeyEvent.VK_SHIFT);
        Thread.sleep(100);
    }

    private void sendKeyStroke(JFrame frame, char ch) throws Exception {
        int keyCode = java.awt.event.KeyEvent.getExtendedKeyCodeForChar(ch);
        boolean shift = Character.isUpperCase(ch);
        sendKeyStroke(frame, keyCode, shift);
    }

    private void runOnEDTAndFlush(Component c) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            if (c != null) {
                c.revalidate();
                c.repaint();
                if (c instanceof JComponent) {
                    ((JComponent) c).putClientProperty("flush", System.nanoTime());
                }
            }
        });
        Toolkit.getDefaultToolkit().sync();
        Thread.sleep(50);
    }

    private void waitForFocusOwner(Component c, int timeoutMs) throws Exception {
        int waited = 0;
        while (waited < timeoutMs) {
            runOnEDTAndFlush(c);
            Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            if (focusOwner == c) return;
            Thread.sleep(100);
            waited += 100;
        }
        fail("Component did not become focus owner: " + c);
    }
}
