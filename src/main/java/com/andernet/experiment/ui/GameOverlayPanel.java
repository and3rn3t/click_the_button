package com.andernet.experiment.ui;

import javax.swing.*;
import java.awt.*;

public class GameOverlayPanel extends JPanel {
    private final JLabel overlayLabel;
    private final JButton overlayButton;
    private final JButton settingsButton;
    private final JLabel instructions;
    public GameOverlayPanel(int width, int height) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBounds(0, 0, width, height);
        overlayLabel = new JLabel("Click the Button Game", SwingConstants.CENTER);
        overlayLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        overlayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        overlayLabel.setForeground(new Color(33, 33, 33, 220));
        add(Box.createVerticalGlue());
        add(overlayLabel);
        add(Box.createRigidArea(new Dimension(0, 24)));
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
        add(overlayButton);
        add(Box.createRigidArea(new Dimension(0, 24)));
        // Add settings button below overlayButton
        settingsButton = new JButton("Settings");
        settingsButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        settingsButton.setFocusPainted(false);
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsButton.setBackground(new Color(120, 144, 156));
        settingsButton.setForeground(Color.WHITE);
        settingsButton.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        settingsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        settingsButton.setContentAreaFilled(true);
        settingsButton.setOpaque(true);
        settingsButton.setBorderPainted(false);
        settingsButton.setFocusable(false);
        settingsButton.setMaximumSize(new Dimension(160, 36));
        settingsButton.setPreferredSize(new Dimension(140, 32));
        add(settingsButton);
        add(Box.createRigidArea(new Dimension(0, 24)));
        instructions = new JLabel("<html><div style='text-align:center;'>Click the blue button as many times as you can in 30 seconds!<br>Avoid the red fake buttons.</div></html>", SwingConstants.CENTER);
        instructions.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructions.setForeground(new Color(60, 60, 60, 200));
        add(instructions);
        add(Box.createVerticalGlue());
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(255,255,255,200));
        g2.fillRoundRect(40, 60, getWidth()-80, getHeight()-120, 40, 40);
        g2.dispose();
        super.paintComponent(g);
    }
    public JLabel getOverlayLabel() { return overlayLabel; }
    public JButton getOverlayButton() { return overlayButton; }
    public JButton getSettingsButton() { return settingsButton; }
}
