package com.andernet.experiment.ui;

import javax.swing.*;
import java.awt.*;
import com.andernet.experiment.util.Constants;
import com.andernet.experiment.logic.GameConstants;

public class GameOverlayPanel extends JPanel {
    private final JLabel overlayLabel;
    private final JButton overlayButton;
    private final JButton settingsButton;
    private final JLabel instructions;
    
    public GameOverlayPanel(int width, int height) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBounds(0, 0, width, height);
        
        // Title label
        overlayLabel = new JLabel(Constants.APP_TITLE, SwingConstants.CENTER);
        overlayLabel.setFont(Theme.OVERLAY_TITLE_FONT);
        overlayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        overlayLabel.setForeground(new Color(33, 33, 33, 220));
        
        // Create buttons using utility methods
        overlayButton = UIUtils.createOverlayButton(Constants.START_GAME);
        overlayButton.setFocusable(false);
        
        settingsButton = UIUtils.createSettingsButton(Constants.SETTINGS);
        settingsButton.setFocusable(false);
        
        // Instructions
        instructions = new JLabel(Constants.INSTRUCTIONS, SwingConstants.CENTER);
        instructions.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructions.setForeground(new Color(60, 60, 60, 200));
        
        // Layout components
        add(Box.createVerticalGlue());
        add(overlayLabel);
        add(Box.createRigidArea(new Dimension(0, 24)));
        add(overlayButton);
        add(Box.createRigidArea(new Dimension(0, 24)));
        add(settingsButton);
        add(Box.createRigidArea(new Dimension(0, 24)));
        add(instructions);
        add(Box.createVerticalGlue());
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(255,255,255,200));
        g2.fillRoundRect(40, 60, getWidth()-80, getHeight()-120, GameConstants.OVERLAY_RADIUS, GameConstants.OVERLAY_RADIUS);
        g2.dispose();
        super.paintComponent(g);
    }
    
    public JLabel getOverlayLabel() { return overlayLabel; }
    public JButton getOverlayButton() { return overlayButton; }
    public JButton getSettingsButton() { return settingsButton; }
}
