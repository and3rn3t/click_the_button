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
    private JButton[] fakeButtons;
    private final int NUM_FAKE_BUTTONS = 2;
    private Color[] funColors = {Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.PINK, Color.YELLOW};

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

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setBounds(10, 10, 120, 35);
        scoreLabel.setFont(labelFont);
        scoreLabel.setOpaque(true);
        scoreLabel.setBackground(new Color(255,255,255,180));
        scoreLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
        add(scoreLabel);

        timerLabel = new JLabel("Time: 30");
        timerLabel.setBounds(140, 10, 120, 35);
        timerLabel.setFont(labelFont);
        timerLabel.setOpaque(true);
        timerLabel.setBackground(new Color(255,255,255,180));
        timerLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
        add(timerLabel);

        highScoreLabel = new JLabel("High Score: 0");
        highScoreLabel.setBounds(270, 10, 150, 35);
        highScoreLabel.setFont(labelFont);
        highScoreLabel.setOpaque(true);
        highScoreLabel.setBackground(new Color(255,255,255,180));
        highScoreLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
        add(highScoreLabel);

        button = new AnimatedButton("Click me!");
        button.setFont(mainFont);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBackground(new Color(100, 181, 246));
        button.setForeground(Color.WHITE);
        button.setBounds((WINDOW_WIDTH-100)/2, (WINDOW_HEIGHT-50)/2, 100, 50);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                score++;
                if (score > highScore) {
                    highScore = score;
                    highScoreLabel.setText("High Score: " + highScore);
                }
                scoreLabel.setText("Score: " + score);
                playClickSound();
                nextLevel();
                fadeMoveButton();
                moveFakeButtons();
                randomizeColors();
            }
        });
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(33, 150, 243));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 181, 246));
            }
        });
        add(button);

        // Add fake buttons
        fakeButtons = new JButton[NUM_FAKE_BUTTONS];
        for (int i = 0; i < NUM_FAKE_BUTTONS; i++) {
            fakeButtons[i] = new JButton("Fake!") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    // Drop shadow
                    g2.setColor(new Color(0,0,0,40));
                    g2.fillRoundRect(4, 4, getWidth()-8, getHeight()-8, 30, 30);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth()-8, getHeight()-8, 30, 30);
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            fakeButtons[i].setFont(mainFont.deriveFont(Font.PLAIN, 18f));
            fakeButtons[i].setFocusPainted(false);
            fakeButtons[i].setContentAreaFilled(false);
            fakeButtons[i].setOpaque(false);
            fakeButtons[i].setBackground(new Color(244, 67, 54));
            fakeButtons[i].setForeground(Color.WHITE);
            fakeButtons[i].setBounds(0, 0, 80, 40);
            fakeButtons[i].addActionListener(e -> {
                score = Math.max(0, score - 2);
                scoreLabel.setText("Score: " + score);
                playFakeSound();
                moveFakeButtons();
                randomizeColors();
            });
            add(fakeButtons[i]);
        }

        startGame();
    }

    private void startGame() {
        score = 0;
        timeLeft = 30;
        scoreLabel.setText("Score: 0");
        timerLabel.setText("Time: 30");
        button.setEnabled(true);
        for (JButton fake : fakeButtons) fake.setEnabled(true);
        gameTimer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft);
            if (timeLeft <= 0) {
                endGame();
            }
        });
        gameTimer.start();
    }

    private void endGame() {
        gameTimer.stop();
        button.setEnabled(false);
        for (JButton fake : fakeButtons) fake.setEnabled(false);
        playEndSound();
        JOptionPane.showMessageDialog(this, "Time's up! Your score: " + score);
        startGame();
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
                    button.setAlpha(a);
                    Thread.sleep(10);
                }
                moveButton();
                for (float a = 0.1f; a <= 1.0f; a += 0.1f) {
                    button.setAlpha(a);
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
