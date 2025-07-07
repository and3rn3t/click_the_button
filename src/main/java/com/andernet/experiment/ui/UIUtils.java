package com.andernet.experiment.ui;

import java.awt.*;
import javax.swing.*;
import java.util.Random;

public class UIUtils {
    public static final Color[] PASTEL_COLORS = {
        new Color(197, 225, 165), // green
        new Color(255, 224, 178), // orange
        new Color(178, 235, 242), // cyan
        new Color(255, 205, 210), // pink
        new Color(248, 187, 208), // magenta
        new Color(255, 245, 157), // yellow
        new Color(206, 147, 216), // purple
        new Color(144, 202, 249)  // blue
    };

    public static Color getRandomPastelColor(Random random) {
        return PASTEL_COLORS[random.nextInt(PASTEL_COLORS.length)];
    }

    public static JLabel createLabel(String text, int x, int y, int width, int height, Font font) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(font);
        label.setOpaque(true);
        label.setBackground(new Color(255,255,255,210));
        label.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        return label;
    }
    
    /**
     * Creates a styled button with common properties
     */
    public static JButton createStyledButton(String text, Font font, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }
    
    /**
     * Creates a game overlay button with consistent styling
     */
    public static JButton createOverlayButton(String text) {
        JButton button = createStyledButton(text, Theme.OVERLAY_BUTTON_FONT, 
            new Color(33, 150, 243), Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(16, 40, 16, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(220, 56));
        button.setMinimumSize(new Dimension(180, 48));
        button.setPreferredSize(new Dimension(200, 52));
        return button;
    }
    
    /**
     * Creates a settings-style button
     */
    public static JButton createSettingsButton(String text) {
        JButton button = createStyledButton(text, new Font("Segoe UI", Font.PLAIN, 16),
            new Color(120, 144, 156), Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(160, 36));
        button.setPreferredSize(new Dimension(140, 32));
        return button;
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
