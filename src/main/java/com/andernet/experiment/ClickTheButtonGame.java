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

public class ClickTheButtonGame extends JFrame {
    private AnimatedButton button;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private JLabel highScoreLabel;
    private GameState gameState;
    private Random random = new Random();
    private final int WINDOW_WIDTH = 400;
    private final int WINDOW_HEIGHT = 400;
    private Timer gameTimer;
    private Timer moveTimer;
    private FakeButton[] fakeButtons;
    private GameOverlayPanel overlayPanel;

    public ClickTheButtonGame() {
        setTitle("Click the Button Game");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
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

        gameState = new GameState(GameConstants.GAME_TIME_SECONDS);
        scoreLabel = UIUtils.createLabel("Score: 0", 10, 10, 120, 35, labelFont);
        add(scoreLabel);
        timerLabel = UIUtils.createLabel("Time: 30", 140, 10, 120, 35, labelFont);
        add(timerLabel);
        highScoreLabel = UIUtils.createLabel("High Score: 0", 270, 10, 150, 35, labelFont);
        add(highScoreLabel);

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
        fakeButtons = new FakeButton[GameConstants.NUM_FAKE_BUTTONS];
        for (int i = 0; i < GameConstants.NUM_FAKE_BUTTONS; i++) {
            fakeButtons[i] = new FakeButton("Fake!");
            fakeButtons[i].addActionListener(e -> {
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
        showOverlay("Click the Button Game", "Start Game", false);

        // Start the button move timer (moves every 1 second)
        moveTimer = new Timer(GameConstants.MOVE_INTERVAL_MS, e -> moveAllButtons());
        moveTimer.setInitialDelay(GameConstants.MOVE_INTERVAL_MS);
        moveTimer.start();
    }

    // Helper to move and randomize all buttons
    private void moveAllButtons() {
        fadeMoveButton();
        moveFakeButtons();
    }

    private void showOverlay(String message, String buttonText, boolean showScore) {
        overlayPanel.getOverlayLabel().setText("<html><div style='text-align:center;'>" + message + "</div></html>");
        overlayPanel.getOverlayButton().setText(buttonText);
        overlayPanel.getOverlayButton().setVisible(true);
        overlayPanel.setVisible(true);
        setGameUIVisible(false);
    }

    private void hideOverlay() {
        overlayPanel.setVisible(false);
        setGameUIVisible(true);
    }

    private void setGameUIVisible(boolean visible) {
        scoreLabel.setVisible(visible);
        timerLabel.setVisible(visible);
        highScoreLabel.setVisible(visible);
        button.setVisible(visible);
        for (FakeButton fake : fakeButtons) fake.setVisible(visible);
    }

    private void startGame() {
        gameState.reset(GameConstants.GAME_TIME_SECONDS);
        scoreLabel.setText("Score: 0");
        timerLabel.setText("Time: 30");
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
        moveTimer.restart();
    }

    private void endGame() {
        gameTimer.stop();
        moveTimer.stop();
        button.setEnabled(false);
        for (JButton fake : fakeButtons) fake.setEnabled(false);
        ResourceManager.playEndBeep();
        showOverlay("Game Over!<br>Your score: " + gameState.getScore(), "Play Again", true);
    }

    private void nextLevel() {
        Dimension minSize = button.getPreferredSize();
        int minWidth = (int) minSize.getWidth();
        int minHeight = (int) minSize.getHeight();
        int newWidth = Math.max(minWidth, GameConstants.MAIN_BUTTON_START_WIDTH - gameState.getScore() * 2);
        int newHeight = Math.max(minHeight, GameConstants.MAIN_BUTTON_START_HEIGHT - gameState.getScore());
        button.setSize(newWidth, newHeight);
    }

    private void moveButton() {
        int x = random.nextInt(GameConstants.WINDOW_WIDTH - button.getWidth());
        int y = random.nextInt(GameConstants.WINDOW_HEIGHT - button.getHeight() - 60) + 40;
        button.setLocation(x, y);
    }

    private void moveFakeButtons() {
        for (FakeButton fake : fakeButtons) {
            int x = random.nextInt(GameConstants.WINDOW_WIDTH - fake.getWidth());
            int y = random.nextInt(GameConstants.WINDOW_HEIGHT - fake.getHeight() - 60) + 40;
            fake.setLocation(x, y);
        }
    }

    private void randomizeColors() {
        Color bg = UIUtils.getRandomPastelColor(random);
        Color btn = UIUtils.getRandomPastelColor(random).darker();
        getContentPane().setBackground(bg);
        button.setBackground(btn);
        for (FakeButton fake : fakeButtons) {
            fake.setBackground(UIUtils.getRandomPastelColor(random).darker());
        }
    }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClickTheButtonGame game = new ClickTheButtonGame();
            game.setVisible(true);
        });
    }
}
