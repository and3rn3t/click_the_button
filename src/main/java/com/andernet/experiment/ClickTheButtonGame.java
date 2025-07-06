package com.andernet.experiment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class ClickTheButtonGame extends JFrame {
    private JButton button;
    private JLabel scoreLabel;
    private int score = 0;
    private Random random = new Random();
    private final int WINDOW_WIDTH = 400;
    private final int WINDOW_HEIGHT = 400;

    public ClickTheButtonGame() {
        setTitle("Click the Button Game");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setBounds(10, 10, 100, 30);
        add(scoreLabel);

        button = new JButton("Click me!");
        button.setBounds(150, 150, 100, 50);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                score++;
                scoreLabel.setText("Score: " + score);
                moveButton();
            }
        });
        add(button);
    }

    private void moveButton() {
        int x = random.nextInt(WINDOW_WIDTH - button.getWidth());
        int y = random.nextInt(WINDOW_HEIGHT - button.getHeight() - 40) + 40; // leave space for label
        button.setLocation(x, y);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClickTheButtonGame game = new ClickTheButtonGame();
            game.setVisible(true);
        });
    }
}
