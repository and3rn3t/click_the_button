package com.andernet.experiment.ui;

import javax.swing.*;
import java.awt.*;
import com.andernet.experiment.util.Constants;

/**
 * Modern game overlay panel with enhanced visual styling
 */
public class GameOverlayPanel extends JPanel {
    private final JLabel overlayLabel;
    private final ModernButton overlayButton;
    private final ModernButton settingsButton;
    private final JLabel instructions;
    private ModernPanel backgroundPanel;
    
    public GameOverlayPanel(int width, int height) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBounds(0, 0, width, height);
        
        // Create semi-transparent background
        backgroundPanel = new ModernPanel();
        backgroundPanel.setGradient(
            new Color(0, 0, 0, 120), 
            new Color(0, 0, 0, 80)
        );
        backgroundPanel.setBorderRadius(0);
        backgroundPanel.setShadow(false);
        backgroundPanel.setSize(width, height);
        
        // Create main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Title label with modern styling
        overlayLabel = new JLabel(Constants.APP_TITLE, SwingConstants.CENTER);
        overlayLabel.setFont(Theme.TITLE_LARGE_FONT);
        overlayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        overlayLabel.setForeground(Color.WHITE);
        
        // Create modern buttons
        overlayButton = UIUtils.createOverlayButton(Constants.START_GAME);
        overlayButton.setFocusable(false);
        
        settingsButton = UIUtils.createSettingsButton(Constants.SETTINGS);
        settingsButton.setFocusable(false);
        
        // Instructions with modern styling
        instructions = new JLabel(Constants.INSTRUCTIONS, SwingConstants.CENTER);
        instructions.setFont(Theme.BODY_LARGE_FONT);
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructions.setForeground(new Color(255, 255, 255, 180));
        
        // Layout components with modern spacing
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(overlayLabel);
        contentPanel.add(UIUtils.createVerticalSpace(Theme.SPACING_2XL));
        contentPanel.add(overlayButton);
        contentPanel.add(UIUtils.createVerticalSpace(Theme.SPACING_LG));
        contentPanel.add(settingsButton);
        contentPanel.add(UIUtils.createVerticalSpace(Theme.SPACING_2XL));
        contentPanel.add(instructions);
        contentPanel.add(Box.createVerticalGlue());
        
        // Add everything to the panel
        add(backgroundPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    public ModernButton getOverlayButton() {
        return overlayButton;
    }
    
    public ModernButton getSettingsButton() {
        return settingsButton;
    }
    
    public JLabel getOverlayLabel() {
        return overlayLabel;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Add subtle pattern overlay for visual interest
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Create a subtle dot pattern
        g2.setColor(new Color(255, 255, 255, 20));
        for (int x = 0; x < getWidth(); x += 40) {
            for (int y = 0; y < getHeight(); y += 40) {
                g2.fillOval(x, y, 2, 2);
            }
        }
        
        g2.dispose();
    }
}
