package com.andernet.experiment;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import com.andernet.experiment.ui.AnimatedButton;
import com.andernet.experiment.ui.FakeButton;
import com.andernet.experiment.ui.GameOverlayPanel;
import com.andernet.experiment.ui.UIUtils;
import com.andernet.experiment.logic.GameConstants;
import com.andernet.experiment.logic.GameState;
import com.andernet.experiment.util.ResourceManager;
import com.andernet.experiment.settings.Settings;
import com.andernet.experiment.settings.SettingsDialog;

/**
 * ClickTheButtonGame is a modern, graphical Java Swing game where the player
 * must click the moving main button as many times as possible before time runs out.
 * Features include:
 * <ul>
 *   <li>Animated button movement and fade effects</li>
 *   <li>Timer and scoring system with high score tracking</li>
 *   <li>Randomized pastel color themes</li>
 *   <li>Fake (obstacle) buttons that penalize the score</li>
 *   <li>Sound effects for actions and game over</li>
 *   <li>Overlay screens for start and game over</li>
 *   <li>Level progression (button shrinks as score increases)</li>
 * </ul>
 *
 * The code is modularized for maintainability, with UI and logic components
 * separated into sub-packages.
 */
public class ClickTheButtonGame extends JFrame {
    // Main animated button the player must click
    private AnimatedButton button;
    // Displays current score
    private JLabel scoreLabel;
    // Displays remaining time
    private JLabel timerLabel;
    // Displays high score
    private JLabel highScoreLabel;
    // Tracks game state (score, time, high score)
    private GameState gameState;
    private Random random = new Random();
    private final int WINDOW_WIDTH = 400;
    private final int WINDOW_HEIGHT = 400;
    // Timer for game countdown
    private Timer gameTimer;
    // Timer for moving buttons automatically
    private Timer moveTimer;
    // Array of fake (obstacle) buttons
    private FakeButton[] fakeButtons;
    // Overlay panel for start/game over screens
    private GameOverlayPanel overlayPanel;
    private Settings settings = new Settings();

    /**
     * Constructs the game window and initializes all UI components and game state.
     */
    public ClickTheButtonGame(Settings settings) {
        this.settings = settings;
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        setTitle("Click the Button Game");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        setBackground(new Color(0,0,0,0));
        setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(240, 248, 255), 0, getHeight(), new Color(197, 225, 165));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        });
        getContentPane().setLayout(null);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);

        gameState = new GameState(settings.getGameDurationSeconds());
        // Score label at top left
        scoreLabel = UIUtils.createLabel("Score: 0", 10, 10, 120, 35, labelFont);
        add(scoreLabel);
        // Timer label at top center
        timerLabel = UIUtils.createLabel("Time: 30", 140, 10, 120, 35, labelFont);
        add(timerLabel);
        // High score label at top right
        highScoreLabel = UIUtils.createLabel("High Score: 0", 270, 10, 150, 35, labelFont);
        add(highScoreLabel);

        // Main animated button setup
        button = new AnimatedButton("Click me!");
        button.setFont(new Font("Segoe UI", Font.BOLD, 22));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBackground(new Color(33, 150, 243));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBounds((GameConstants.WINDOW_WIDTH-GameConstants.MAIN_BUTTON_START_WIDTH)/2, (GameConstants.WINDOW_HEIGHT-GameConstants.MAIN_BUTTON_START_HEIGHT)/2, GameConstants.MAIN_BUTTON_START_WIDTH, GameConstants.MAIN_BUTTON_START_HEIGHT);
        button.addActionListener(e -> {
            // Main button click: increment score, update UI, play sound, advance level
            gameState.incrementScore();
            highScoreLabel.setText("High Score: " + gameState.getHighScore());
            scoreLabel.setText("Score: " + gameState.getScore());
            ResourceManager.playBeep();
            nextLevel();
            moveAllButtons();
            randomizeColors();
        });
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(33, 150, 243).darker());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(33, 150, 243));
            }
        });
        add(button);
        // Create and add fake (obstacle) buttons
        fakeButtons = new FakeButton[settings.getNumFakeButtons()];
        for (int i = 0; i < settings.getNumFakeButtons(); i++) {
            fakeButtons[i] = new FakeButton("Fake!");
            fakeButtons[i].addActionListener(e -> {
                // Fake button click: penalize score, play sound, move buttons
                gameState.decrementScore(2);
                scoreLabel.setText("Score: " + gameState.getScore());
                ResourceManager.playFakeBeep();
                moveAllButtons();
                randomizeColors();
            });
            add(fakeButtons[i]);
        }
        // Overlay panel for start/game over screens
        overlayPanel = new GameOverlayPanel(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        overlayPanel.getOverlayButton().addActionListener(e -> {
            hideOverlay();
            startGame();
        });
        overlayPanel.setVisible(true);
        getLayeredPane().add(overlayPanel, JLayeredPane.POPUP_LAYER);
        // Ensure game UI is hidden until game starts
        setGameUIVisible(false);
        showOverlay("Click the Button Game", "Start Game", false);

        // Start the button move timer (moves every 1 second)
        moveTimer = new Timer(settings.getMoveIntervalMs(), e -> moveAllButtons());
        moveTimer.setInitialDelay(settings.getMoveIntervalMs());
        moveTimer.start();
    }

    /**
     * Moves the main and fake buttons with fade animation.
     */
    // Helper to move and randomize all buttons
    private void moveAllButtons() {
        fadeMoveButton();
        moveFakeButtons();
    }

    /**
     * Shows the overlay panel with a message and button.
     * @param message The message to display
     * @param buttonText The text for the overlay button
     * @param showScore Whether to show the score (unused, for future use)
     */
    private void showOverlay(String message, String buttonText, boolean showScore) {
        overlayPanel.getOverlayLabel().setText("<html><div style='text-align:center;'>" + message + "</div></html>");
        overlayPanel.getOverlayButton().setText(buttonText);
        overlayPanel.getOverlayButton().setVisible(true);
        overlayPanel.setVisible(true);
        setGameUIVisible(false);
    }

    /**
     * Hides the overlay panel and shows the main game UI.
     */
    private void hideOverlay() {
        overlayPanel.setVisible(false);
        setGameUIVisible(true);
    }

    /**
     * Sets the visibility of all main game UI components.
     * @param visible true to show, false to hide
     */
    private void setGameUIVisible(boolean visible) {
        scoreLabel.setVisible(visible);
        timerLabel.setVisible(visible);
        highScoreLabel.setVisible(visible);
        button.setVisible(visible);
        for (FakeButton fake : fakeButtons) fake.setVisible(visible);
    }

    /**
     * Starts or restarts the game, resetting state and timers.
     */
    private void startGame() {
        gameState.reset(settings.getGameDurationSeconds());
        scoreLabel.setText("Score: 0");
        timerLabel.setText("Time: " + settings.getGameDurationSeconds());
        button.setEnabled(true);
        for (FakeButton fake : fakeButtons) fake.setEnabled(true);
        setGameUIVisible(true);
        overlayPanel.setVisible(false);
        gameTimer = new Timer(1000, e -> {
            gameState.decrementTime();
            timerLabel.setText("Time: " + gameState.getTimeLeft());
            if (gameState.getTimeLeft() <= 0) {
                endGame();
            }
        });
        gameTimer.start();
        moveTimer.setDelay(settings.getMoveIntervalMs());
        moveTimer.restart();
    }

    /**
     * Ends the game, disables input, and shows the game over overlay.
     */
    private void endGame() {
        gameTimer.stop();
        moveTimer.stop();
        button.setEnabled(false);
        for (JButton fake : fakeButtons) fake.setEnabled(false);
        ResourceManager.playEndBeep();
        showOverlay("Game Over!<br>Your score: " + gameState.getScore(), "Play Again", true);
    }

    /**
     * Advances the level by shrinking the main button, but not below its text size.
     */
    private void nextLevel() {
        Dimension minSize = button.getPreferredSize();
        int minWidth = (int) minSize.getWidth();
        int minHeight = (int) minSize.getHeight();
        int newWidth = Math.max(minWidth, GameConstants.MAIN_BUTTON_START_WIDTH - gameState.getScore() * 2);
        int newHeight = Math.max(minHeight, GameConstants.MAIN_BUTTON_START_HEIGHT - gameState.getScore());
        button.setSize(newWidth, newHeight);
    }

    /**
     * Moves the main button to a random location within the window.
     */
    private void moveButton() {
        int x = random.nextInt(GameConstants.WINDOW_WIDTH - button.getWidth());
        int y = random.nextInt(GameConstants.WINDOW_HEIGHT - button.getHeight() - 60) + 40;
        button.setLocation(x, y);
    }

    /**
     * Moves all fake buttons to random locations within the window.
     */
    private void moveFakeButtons() {
        for (FakeButton fake : fakeButtons) {
            int x = random.nextInt(GameConstants.WINDOW_WIDTH - fake.getWidth());
            int y = random.nextInt(GameConstants.WINDOW_HEIGHT - fake.getHeight() - 60) + 40;
            fake.setLocation(x, y);
        }
    }

    /**
     * Randomizes the background and button colors using pastel shades.
     */
    private void randomizeColors() {
        Color bg = UIUtils.getRandomPastelColor(random);
        Color btn = UIUtils.getRandomPastelColor(random).darker();
        getContentPane().setBackground(bg);
        button.setBackground(btn);
        for (FakeButton fake : fakeButtons) {
            fake.setBackground(UIUtils.getRandomPastelColor(random).darker());
        }
    }

    /**
     * Animates the main button fading out, moving, then fading in.
     */
    private void fadeMoveButton() {
        // Fade out, move, fade in
        new Thread(() -> {
            try {
                for (float a = 1.0f; a >= 0.1f; a -= 0.1f) {
                    final float alpha = a;
                    SwingUtilities.invokeLater(() -> button.setAlpha(alpha));
                    Thread.sleep(10);
                }
                SwingUtilities.invokeLater(this::moveButton);
                for (float a = 0.1f; a <= 1.0f; a += 0.1f) {
                    final float alpha = a;
                    SwingUtilities.invokeLater(() -> button.setAlpha(alpha));
                    Thread.sleep(10);
                }
            } catch (InterruptedException ignored) {}
        }).start();
    }

    /**
     * Main entry point. Launches the game window.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Show settings dialog before creating the game frame
            Settings settings = new Settings();
            JFrame dummy = new JFrame(); // Temporary invisible frame for dialog parenting
            SettingsDialog dialog = new SettingsDialog(dummy, settings);
            dialog.setVisible(true);
            dummy.dispose();
            if (!dialog.isConfirmed()) {
                System.exit(0);
            }
            ClickTheButtonGame game = new ClickTheButtonGame(settings);
            game.setVisible(true);
        });
    }
}
