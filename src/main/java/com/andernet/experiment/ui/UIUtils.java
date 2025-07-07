package com.andernet.experiment.ui;

import java.awt.*;
import javax.swing.*;
import java.util.Random;

/**
 * Modern UI utility methods for creating consistent, beautiful components.
 */
public class UIUtils {
    
    // Modern color palette (updated to work with new theme)
    public static final Color[] MODERN_COLORS = {
        new Color(99, 102, 241),   // Indigo
        new Color(139, 69, 234),   // Purple
        new Color(236, 72, 153),   // Pink
        new Color(239, 68, 68),    // Red
        new Color(245, 101, 101),  // Red-orange
        new Color(251, 146, 60),   // Orange
        new Color(251, 191, 36),   // Amber
        new Color(34, 197, 94),    // Green
        new Color(6, 182, 212),    // Cyan
        new Color(59, 130, 246),   // Blue
    };

    public static Color getRandomModernColor(Random random) {
        return MODERN_COLORS[random.nextInt(MODERN_COLORS.length)];
    }
    
    // Legacy method for backward compatibility
    public static Color getRandomPastelColor(Random random) {
        return getRandomModernColor(random);
    }

    /**
     * Creates a modern label with enhanced styling
     */
    public static JLabel createModernLabel(String text, int x, int y, int width, int height, Font font) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(font);
        label.setOpaque(false);
        label.setForeground(Theme.TEXT_PRIMARY);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
    
    /**
     * Creates a modern info panel for scores, timers, etc.
     */
    public static ModernPanel createInfoPanel(String text, int x, int y, int width, int height, Font font) {
        ModernPanel panel = new ModernPanel();
        panel.setBounds(x, y, width, height);
        panel.setBackgroundColor(Theme.SURFACE_ELEVATED);
        panel.setBorderRadius(Theme.BORDER_RADIUS_MD);
        panel.setShadow(true);
        
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        label.setForeground(Theme.TEXT_PRIMARY);
        
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Legacy method for backward compatibility
    public static JLabel createLabel(String text, int x, int y, int width, int height, Font font) {
        return createModernLabel(text, x, y, width, height, font);
    }
    
    /**
     * Creates a modern styled button with enhanced visual effects
     */
    public static ModernButton createModernButton(String text, Color bgColor) {
        ModernButton button = new ModernButton(text, bgColor);
        button.setFont(Theme.BUTTON_FONT);
        button.setFocusPainted(false);
        return button;
    }
    
    /**
     * Creates a styled button with common properties (legacy method)
     */
    public static JButton createStyledButton(String text, Font font, Color bgColor, Color fgColor) {
        ModernButton button = new ModernButton(text, bgColor);
        button.setFont(font);
        button.setForeground(fgColor);
        return button;
    }
    
    /**
     * Creates a game overlay button with modern styling
     */
    public static ModernButton createOverlayButton(String text) {
        ModernButton button = new ModernButton(text, Theme.PRIMARY_BLUE);
        button.setFont(Theme.OVERLAY_BUTTON_FONT);
        button.setBorderRadius(Theme.BORDER_RADIUS_LG);
        button.setPreferredSize(new Dimension(200, 60));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
    
    /**
     * Creates a settings button with secondary styling
     */
    public static ModernButton createSettingsButton(String text) {
        ModernButton button = new ModernButton(text, Theme.NEUTRAL_600);
        button.setFont(Theme.BODY_LARGE_FONT);
        button.setBorderRadius(Theme.BORDER_RADIUS_MD);
        button.setPreferredSize(new Dimension(160, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
    
    /**
     * Creates a modern background panel with gradient
     */
    public static ModernPanel createBackgroundPanel(int width, int height) {
        ModernPanel panel = new ModernPanel();
        panel.setSize(width, height);
        panel.setGradient(Theme.BACKGROUND_GRADIENT_TOP, Theme.BACKGROUND_GRADIENT_BOTTOM);
        panel.setBorderRadius(0); // No border radius for full background
        panel.setShadow(false);
        return panel;
    }
    
    /**
     * Creates a modern card-like panel for UI sections
     */
    public static ModernPanel createCard() {
        ModernPanel panel = new ModernPanel();
        panel.setBackgroundColor(Theme.SURFACE_ELEVATED);
        panel.setBorderRadius(Theme.BORDER_RADIUS_LG);
        panel.setShadow(true);
        return panel;
    }
    
    /**
     * Applies modern styling to existing components
     */
    public static void applyModernStyling(JComponent component) {
        component.setFont(Theme.BODY_MEDIUM_FONT);
        component.setForeground(Theme.TEXT_PRIMARY);
        
        if (component instanceof JButton) {
            JButton button = (JButton) component;
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setOpaque(true);
            button.setBackground(Theme.PRIMARY_BLUE);
            button.setForeground(Theme.TEXT_ON_ACCENT);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }
    
    /**
     * Creates a subtle separator line
     */
    public static JSeparator createModernSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(Theme.NEUTRAL_200);
        separator.setBackground(Theme.NEUTRAL_200);
        return separator;
    }
    
    /**
     * Utility method to create consistent spacing
     */
    public static Component createVerticalSpace(int size) {
        return Box.createRigidArea(new Dimension(0, size));
    }
    
    /**
     * Utility method to create consistent spacing
     */
    public static Component createHorizontalSpace(int size) {
        return Box.createRigidArea(new Dimension(size, 0));
    }
    
    /**
     * Applies responsive positioning based on window size
     */
    public static void positionResponsively(JComponent component, double xRatio, double yRatio, int width, int height) {
        int x = (int) (width * xRatio);
        int y = (int) (height * yRatio);
        component.setLocation(x, y);
    }
}
