package com.andernet.experiment.ui;

import javax.swing.*;
import java.awt.*;
import com.andernet.experiment.util.Constants;
import com.andernet.experiment.util.ColorCache;
import com.andernet.experiment.logic.GameConstants;

/**
 * Factory class for creating common UI components with consistent styling
 */
public class ComponentFactory {
    
    /**
     * Creates a styled game button with common properties
     */
    public static JButton createGameButton(String text, String name, String tooltip) {
        JButton button = new JButton(text);
        button.setName(name);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);
        return button;
    }
    
    /**
     * Creates a control button (small, icon-like buttons)
     */
    public static JButton createControlButton(String text, String name, String tooltip, 
                                            int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setName(name);
        button.setBounds(x, y, width, height);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setToolTipText(tooltip);
        return button;
    }
    
    /**
     * Creates a mute/unmute button
     */
    public static JButton createMuteButton(boolean soundEnabled) {
        JButton muteButton = createControlButton(
            soundEnabled ? Constants.SOUND_ON : Constants.SOUND_OFF,
            "muteButton",
            Constants.MUTE_BUTTON_TOOLTIP,
            GameConstants.WINDOW_WIDTH - 40, 5, 32, 32
        );
        muteButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));
        return muteButton;
    }
    
    /**
     * Creates a help button
     */
    public static JButton createHelpButton() {
        JButton helpButton = createGameButton(Constants.HELP, "helpButton", Constants.HELP_BUTTON_TOOLTIP);
        helpButton.setBackground(new Color(197, 225, 165));
        helpButton.setForeground(new Color(33, 33, 33));
        helpButton.setBounds(10, 10, 40, 36);
        return helpButton;
    }
    
    /**
     * Creates the main animated button
     */
    public static AnimatedButton createMainButton(String text, int width, int height) {
        AnimatedButton button = new AnimatedButton(text);
        button.setName("mainButton");
        button.setFont(Theme.BUTTON_FONT);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBackground(Theme.MAIN_BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBounds((GameConstants.WINDOW_WIDTH - width) / 2,
                (GameConstants.WINDOW_HEIGHT - height) / 2,
                width, height);
        button.setToolTipText(Constants.MAIN_BUTTON_TOOLTIP);
        return button;
    }
    
    /**
     * Creates a dialog button with consistent styling
     */
    public static JButton createDialogButton(String text, String name) {
        JButton button = new JButton(text);
        button.setName(name);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * Adds hover effect to a button
     */
    public static void addHoverEffect(JButton button, Color normalColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ColorCache.getDarkerColor(normalColor));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normalColor);
            }
        });
    }
}
