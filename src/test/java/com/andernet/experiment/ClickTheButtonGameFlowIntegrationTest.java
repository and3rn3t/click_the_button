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
        settings.setGameDurationSeconds(10); // Use minimum allowed for dialog
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

    // --- Wait utility for polling UI state ---
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

    @Test
    @Order(1)
    void testGameFlow_StartClickGameOver() throws Exception {
        JButton overlayButton = (JButton) findComponentByName(game, "overlayButton");
        assertNotNull(overlayButton, "Overlay button should exist");
        SwingUtilities.invokeAndWait(overlayButton::doClick);
        Thread.sleep(2500);
        JButton mainButton = (JButton) findComponentByType(game, com.andernet.experiment.ui.AnimatedButton.class);
        assertNotNull(mainButton, "Main button should exist");
        for (int i = 0; i < 2; i++) {
            SwingUtilities.invokeAndWait(mainButton::doClick);
            Thread.sleep(100);
        }
        // Wait for overlay to become visible (game over)
        JPanel overlayPanel = (JPanel) findComponentByType(game, com.andernet.experiment.ui.GameOverlayPanel.class);
        assertNotNull(overlayPanel, "Overlay panel should exist");
        waitForCondition("Overlay should be visible after game over", overlayPanel::isVisible, 5000);
        JLabel scoreLabel = (JLabel) findComponentByName(game, "scoreLabel");
        assertNotNull(scoreLabel);
        assertTrue(scoreLabel.getText().contains("2"), "Score label should show 2");
    }

    @Test
    @Order(2)
    void testSettingsDialog_ChangeGameDuration() throws Exception {
        JButton overlayButton = (JButton) findComponentByName(game, "overlayButton");
        SwingUtilities.invokeAndWait(overlayButton::doClick);
        JButton settingsButton = (JButton) findComponentByName(game, "settingsButton");
        assertNotNull(settingsButton, "Settings button should exist");
        SwingUtilities.invokeAndWait(settingsButton::doClick);
        // Wait for dialog to appear
        waitForCondition("Settings dialog should appear", () -> findDialogByTitle("Settings") != null, 5000);
        JDialog dialog = (JDialog) findDialogByTitle("Settings");
        assertNotNull(dialog, "Settings dialog should appear");
        JTextField durationField = (JTextField) findComponentByType(dialog, JTextField.class);
        assertNotNull(durationField, "Duration field should exist");
        SwingUtilities.invokeAndWait(() -> durationField.setText("12"));
        JButton okButton = (JButton) findComponentByName(dialog, "okButton");
        assertNotNull(okButton, "OK button should exist");
        SwingUtilities.invokeAndWait(okButton::doClick);
        SwingUtilities.invokeAndWait(overlayButton::doClick);
        Thread.sleep(2500);
        JLabel timerLabel = (JLabel) findComponentByName(game, "timerLabel");
        assertNotNull(timerLabel);
        assertTrue(timerLabel.getText().contains("12"), "Timer label should reflect new duration");
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
        // Simulate pause (P)
        sendKeyStroke(game, 'P');
        JPanel overlayPanel = (JPanel) findComponentByType(game, com.andernet.experiment.ui.GameOverlayPanel.class);
        waitForCondition("Overlay should be visible after pause", overlayPanel::isVisible, 2000);
        // Simulate resume (P)
        sendKeyStroke(game, 'P');
        waitForCondition("Overlay should be hidden after resume", () -> !overlayPanel.isVisible(), 2000);
        // Simulate Esc (quit) - should show confirm dialog, skip actual exit
    }

    @Test
    @Order(5)
    void testHighScorePersistence() throws Exception {
        // Start and play a game to set a high score
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
        // Restart game and check high score is retained
        SwingUtilities.invokeAndWait(() -> {
            game.dispose();
            game = new ClickTheButtonGame(settings);
            game.setVisible(true);
        });
        JLabel highScoreLabel2 = (JLabel) findComponentByName(game, "highScoreLabel");
        assertNotNull(highScoreLabel2);
        assertEquals(highScoreText, highScoreLabel2.getText(), "High score should persist");
    }

    @Test
    @Order(6)
    void testFontSizeAdjustment() throws Exception {
        JLabel scoreLabel = (JLabel) findComponentByName(game, "scoreLabel");
        Font origFont = scoreLabel.getFont();
        sendKeyStroke(game, java.awt.event.KeyEvent.VK_EQUALS, true); // Shift+Equals for +
        Thread.sleep(200);
        Font largerFont = scoreLabel.getFont();
        assertTrue(largerFont.getSize() > origFont.getSize(), "Font size should increase");
        sendKeyStroke(game, java.awt.event.KeyEvent.VK_MINUS, false); // -
        Thread.sleep(200);
        Font smallerFont = scoreLabel.getFont();
        assertTrue(smallerFont.getSize() < largerFont.getSize(), "Font size should decrease");
    }

    @Test
    @Order(7)
    void testFakeButtonPenalty() throws Exception {
        JButton overlayButton = (JButton) findComponentByName(game, "overlayButton");
        SwingUtilities.invokeAndWait(overlayButton::doClick);
        Thread.sleep(2500);
        JButton fakeButton = (JButton) findComponentByType(game, com.andernet.experiment.ui.FakeButton.class);
        assertNotNull(fakeButton, "Fake button should exist");
        JLabel scoreLabel = (JLabel) findComponentByName(game, "scoreLabel");
        int scoreBefore = Integer.parseInt(scoreLabel.getText().replaceAll("\\D+", ""));
        SwingUtilities.invokeAndWait(fakeButton::doClick);
        waitForCondition("Score should decrease after clicking fake button", () -> {
            int scoreAfter = Integer.parseInt(scoreLabel.getText().replaceAll("\\D+", ""));
            return scoreAfter < scoreBefore;
        }, 2000);
    }

    @Test
    @Order(8)
    void testOverlayAccessibilityFocus() throws Exception {
        JButton overlayButton = (JButton) findComponentByName(game, "overlayButton");
        SwingUtilities.invokeAndWait(overlayButton::doClick);
        JPanel overlayPanel = (JPanel) findComponentByType(game, com.andernet.experiment.ui.GameOverlayPanel.class);
        JButton overlayBtn = (JButton) findComponentByName(overlayPanel, "overlayButton");
        assertNotNull(overlayBtn);
        waitForCondition("Overlay button should be focused for accessibility", overlayBtn::isFocusOwner, 2000);
    }

    // --- Utility methods for finding components by name or type ---
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
    // --- Additional utility methods ---
    private JDialog findDialogByTitle(String title) {
        for (Window w : Window.getWindows()) {
            if (w instanceof JDialog && w.isVisible() && title.equals(((JDialog) w).getTitle())) {
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
    // Overload for character keys (platform-independent)
    private void sendKeyStroke(JFrame frame, char ch) throws Exception {
        int keyCode = java.awt.event.KeyEvent.getExtendedKeyCodeForChar(ch);
        boolean shift = Character.isUpperCase(ch);
        sendKeyStroke(frame, keyCode, shift);
    }
}
