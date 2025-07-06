package com.andernet.experiment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class ClickTheButtonGame extends JFrame {
    // Custom button class for alpha animation
    private class AnimatedButton extends JButton {
        private float alpha = 1.0f;
        public AnimatedButton(String text) { super(text); }
        public void setAlpha(float a) { alpha = a; repaint(); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            // Drop shadow
            g2.setColor(new Color(0,0,0,60));
            g2.fillRoundRect(4, 4, getWidth()-8, getHeight()-8, 30, 30);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth()-8, getHeight()-8, 30, 30);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    private AnimatedButton button;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private JLabel highScoreLabel;
    private int score = 0;
    private int timeLeft = 30;
    private int highScore = 0;
    private Random random = new Random();
    private final int WINDOW_WIDTH = 400;
    private final int WINDOW_HEIGHT = 400;
    private Timer gameTimer;
    private Timer moveTimer;
    private JButton[] fakeButtons;
    private final int NUM_FAKE_BUTTONS = 2;
    private JPanel overlayPanel;
    private JLabel overlayLabel;
    private JButton overlayButton;

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

        Font mainFont = new Font("Segoe UI", Font.BOLD, 20);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);

        scoreLabel = createLabel("Score: 0", 10, 10, 120, 35, labelFont);
        add(scoreLabel);
        timerLabel = createLabel("Time: 30", 140, 10, 120, 35, labelFont);
        add(timerLabel);
        highScoreLabel = createLabel("High Score: 0", 270, 10, 150, 35, labelFont);
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
        button.setBounds((WINDOW_WIDTH-100)/2, (WINDOW_HEIGHT-50)/2, 100, 50);
        button.addActionListener(e -> {
            score++;
            if (score > highScore) {
                highScore = score;
                highScoreLabel.setText("High Score: " + highScore);
            }
            scoreLabel.setText("Score: " + score);
            playClickSound();
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

        fakeButtons = new JButton[NUM_FAKE_BUTTONS];
        for (int i = 0; i < NUM_FAKE_BUTTONS; i++) {
            fakeButtons[i] = createFakeButton(mainFont);
            fakeButtons[i].addActionListener(e -> {
                score = Math.max(0, score - 2);
                scoreLabel.setText("Score: " + score);
                playFakeSound();
                moveAllButtons();
                randomizeColors();
            });
            add(fakeButtons[i]);
        }

        // Overlay panel for start/game over screens
        overlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255,255,255,200)); // semi-transparent white
                g2.fillRoundRect(40, 60, WINDOW_WIDTH-80, WINDOW_HEIGHT-120, 40, 40);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        overlayPanel.setLayout(new BoxLayout(overlayPanel, BoxLayout.Y_AXIS));
        overlayPanel.setOpaque(false);
        overlayPanel.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        overlayLabel = new JLabel("Click the Button Game", SwingConstants.CENTER);
        overlayLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        overlayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        overlayLabel.setForeground(new Color(33, 33, 33, 220));
        overlayPanel.add(Box.createVerticalGlue());
        overlayPanel.add(overlayLabel);
        overlayPanel.add(Box.createRigidArea(new Dimension(0, 24)));

        overlayButton = new JButton("Start Game");
        overlayButton.setFont(new Font("Segoe UI", Font.BOLD, 22));
        overlayButton.setFocusPainted(false);
        overlayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        overlayButton.setBackground(new Color(33, 150, 243));
        overlayButton.setForeground(Color.WHITE);
        overlayButton.setBorder(BorderFactory.createEmptyBorder(16, 40, 16, 40));
        overlayButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        overlayButton.setContentAreaFilled(true);
        overlayButton.setOpaque(true);
        overlayButton.setBorderPainted(false);
        overlayButton.setFocusable(false);
        overlayButton.setMaximumSize(new Dimension(220, 56));
        overlayButton.setMinimumSize(new Dimension(180, 48));
        overlayButton.setPreferredSize(new Dimension(200, 52));
        overlayButton.addActionListener(e -> {
            hideOverlay();
            startGame();
        });
        overlayPanel.add(overlayButton);
        overlayPanel.add(Box.createRigidArea(new Dimension(0, 24)));

        JLabel instructions = new JLabel("<html><div style='text-align:center;'>Click the blue button as many times as you can in 30 seconds!<br>Avoid the red fake buttons.</div></html>", SwingConstants.CENTER);
        instructions.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructions.setForeground(new Color(60, 60, 60, 200));
        overlayPanel.add(instructions);
        overlayPanel.add(Box.createVerticalGlue());

        overlayPanel.setVisible(true);
        getLayeredPane().add(overlayPanel, JLayeredPane.POPUP_LAYER);
        showOverlay("Click the Button Game", "Start Game", false);

        // Start the button move timer (moves every 1 second)
        moveTimer = new Timer(1000, e -> moveAllButtons());
        moveTimer.setInitialDelay(1000);
        moveTimer.start();
    }

    // Helper to set up a label
    private JLabel createLabel(String text, int x, int y, int width, int height, Font font) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(font);
        label.setOpaque(true);
        label.setBackground(new Color(255,255,255,210));
        label.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        return label;
    }

    // Helper to set up a fake button
    private JButton createFakeButton(Font font) {
        JButton fake = new JButton("Fake!") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0,0,0,40));
                g2.fillRoundRect(4, 4, getWidth()-8, getHeight()-8, 30, 30);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth()-8, getHeight()-8, 30, 30);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        fake.setFont(font.deriveFont(Font.PLAIN, 18f));
        fake.setFocusPainted(false);
        fake.setContentAreaFilled(false);
        fake.setOpaque(false);
        fake.setBackground(new Color(244, 67, 54));
        fake.setForeground(Color.WHITE);
        fake.setBounds(0, 0, 80, 40);
        fake.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return fake;
    }

    // Helper to move and randomize all buttons
    private void moveAllButtons() {
        fadeMoveButton();
        moveFakeButtons();
    }

    private void showOverlay(String message, String buttonText, boolean showScore) {
        overlayLabel.setText("<html><div style='text-align:center;'>" + message + "</div></html>");
        overlayButton.setText(buttonText);
        overlayButton.setVisible(true);
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
        for (JButton fake : fakeButtons) fake.setVisible(visible);
    }

    private void startGame() {
        score = 0;
        timeLeft = 30;
        scoreLabel.setText("Score: 0");
        timerLabel.setText("Time: 30");
        button.setEnabled(true);
        for (JButton fake : fakeButtons) fake.setEnabled(true);
        setGameUIVisible(true);
        overlayPanel.setVisible(false);
        gameTimer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft);
            if (timeLeft <= 0) {
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
        playEndSound();
        showOverlay("Game Over!<br>Your score: " + score, "Play Again", true);
    }

    private void nextLevel() {
        Dimension minSize = button.getPreferredSize();
        int minWidth = (int) minSize.getWidth();
        int minHeight = (int) minSize.getHeight();
        int newWidth = Math.max(minWidth, 100 - score * 2);
        int newHeight = Math.max(minHeight, 50 - score);
        button.setSize(newWidth, newHeight);
    }

    private void moveButton() {
        int x = random.nextInt(WINDOW_WIDTH - button.getWidth());
        int y = random.nextInt(WINDOW_HEIGHT - button.getHeight() - 60) + 40;
        button.setLocation(x, y);
    }

    private void moveFakeButtons() {
        for (JButton fake : fakeButtons) {
            int x = random.nextInt(WINDOW_WIDTH - fake.getWidth());
            int y = random.nextInt(WINDOW_HEIGHT - fake.getHeight() - 60) + 40;
            fake.setLocation(x, y);
        }
    }

    private void randomizeColors() {
        Color[] pastelColors = {
            new Color(197, 225, 165), // green
            new Color(255, 224, 178), // orange
            new Color(178, 235, 242), // cyan
            new Color(255, 205, 210), // pink
            new Color(248, 187, 208), // magenta
            new Color(255, 245, 157), // yellow
            new Color(206, 147, 216), // purple
            new Color(144, 202, 249)  // blue
        };
        Color bg = pastelColors[random.nextInt(pastelColors.length)];
        Color btn = pastelColors[random.nextInt(pastelColors.length)].darker();
        getContentPane().setBackground(bg);
        button.setBackground(btn);
        for (JButton fake : fakeButtons) {
            fake.setBackground(pastelColors[random.nextInt(pastelColors.length)].darker());
        }
    }

    private void playClickSound() {
        Toolkit.getDefaultToolkit().beep();
    }

    private void playFakeSound() {
        // Lower beep for fake button
        new Thread(() -> {
            try {
                java.awt.Toolkit.getDefaultToolkit().beep();
                Thread.sleep(50);
                java.awt.Toolkit.getDefaultToolkit().beep();
            } catch (InterruptedException ignored) {}
        }).start();
    }

    private void playEndSound() {
        // Triple beep for end
        new Thread(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    Thread.sleep(100);
                }
            } catch (InterruptedException ignored) {}
        }).start();
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
