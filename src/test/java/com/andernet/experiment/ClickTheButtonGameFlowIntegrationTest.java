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
        settings.setGameDurationSeconds(3); // Shorten for test
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

    @Test
    @Order(1)
    void testGameFlow_StartClickGameOver() throws Exception {
        // Start the game (simulate overlay button click)
        JButton overlayButton = (JButton) findComponentByName(game, "overlayButton");
        assertNotNull(overlayButton, "Overlay button should exist");
        SwingUtilities.invokeAndWait(overlayButton::doClick);
        // Wait for countdown to finish
        Thread.sleep(2500);
        // Click the main button a few times
        JButton mainButton = (JButton) findComponentByType(game, com.andernet.experiment.ui.AnimatedButton.class);
        assertNotNull(mainButton, "Main button should exist");
        for (int i = 0; i < 2; i++) {
            SwingUtilities.invokeAndWait(mainButton::doClick);
            Thread.sleep(100);
        }
        // Wait for timer to expire
        Thread.sleep(2000);
        // Overlay should be visible (game over)
        JPanel overlayPanel = (JPanel) findComponentByType(game, com.andernet.experiment.ui.GameOverlayPanel.class);
        assertNotNull(overlayPanel, "Overlay panel should exist");
        assertTrue(overlayPanel.isVisible(), "Overlay should be visible after game over");
        // Score label should reflect clicks
        JLabel scoreLabel = (JLabel) findComponentByName(game, "scoreLabel");
        assertNotNull(scoreLabel);
        assertTrue(scoreLabel.getText().contains("2"), "Score label should show 2");
    }

    @Test
    @Order(2)
    void testSettingsDialog_ChangeGameDuration() throws Exception {
        // Open settings dialog via overlay
        JButton overlayButton = (JButton) findComponentByName(game, "overlayButton");
        SwingUtilities.invokeAndWait(overlayButton::doClick);
        JButton settingsButton = (JButton) findComponentByName(game, "settingsButton");
        assertNotNull(settingsButton, "Settings button should exist");
        SwingUtilities.invokeAndWait(settingsButton::doClick);
        // Find the dialog and change duration (simulate user input)
        JDialog dialog = (JDialog) findDialogByTitle("Settings");
        assertNotNull(dialog, "Settings dialog should appear");
        JTextField durationField = (JTextField) findComponentByType(dialog, JTextField.class);
        assertNotNull(durationField, "Duration field should exist");
        SwingUtilities.invokeAndWait(() -> durationField.setText("5"));
        JButton okButton = (JButton) findComponentByName(dialog, "okButton");
        assertNotNull(okButton, "OK button should exist");
        SwingUtilities.invokeAndWait(okButton::doClick);
        // Start game and check timer label
        SwingUtilities.invokeAndWait(overlayButton::doClick);
        Thread.sleep(2500);
        JLabel timerLabel = (JLabel) findComponentByName(game, "timerLabel");
        assertNotNull(timerLabel);
        assertTrue(timerLabel.getText().contains("5"), "Timer label should reflect new duration");
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
        assertTrue(overlayPanel.isVisible(), "Overlay should be visible after pause");
        // Simulate resume (P)
        sendKeyStroke(game, 'P');
        assertFalse(overlayPanel.isVisible(), "Overlay should be hidden after resume");
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
        sendKeyStroke(game, '+');
        Thread.sleep(100);
        Font largerFont = scoreLabel.getFont();
        assertTrue(largerFont.getSize() > origFont.getSize(), "Font size should increase");
        sendKeyStroke(game, '-');
        Thread.sleep(100);
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
        Thread.sleep(100);
        int scoreAfter = Integer.parseInt(scoreLabel.getText().replaceAll("\\D+", ""));
        assertTrue(scoreAfter < scoreBefore, "Score should decrease after clicking fake button");
    }

    @Test
    @Order(8)
    void testOverlayAccessibilityFocus() throws Exception {
        JButton overlayButton = (JButton) findComponentByName(game, "overlayButton");
        SwingUtilities.invokeAndWait(overlayButton::doClick);
        JPanel overlayPanel = (JPanel) findComponentByType(game, com.andernet.experiment.ui.GameOverlayPanel.class);
        JButton overlayBtn = (JButton) findComponentByName(overlayPanel, "overlayButton");
        assertNotNull(overlayBtn);
        assertTrue(overlayBtn.isFocusOwner(), "Overlay button should be focused for accessibility");
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
    private void sendKeyStroke(JFrame frame, char keyChar) throws Exception {
        Robot robot = new Robot();
        frame.requestFocus();
        Thread.sleep(100);
        robot.keyPress(Character.toUpperCase(keyChar));
        robot.keyRelease(Character.toUpperCase(keyChar));
        Thread.sleep(100);
    }
}
