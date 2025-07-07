package com.andernet.experiment.ui;

import java.awt.*;
import java.util.Arrays;

/**
 * Modern theme with contemporary colors, gradients, and typography.
 * Supports both light and dark modes with a sophisticated color palette.
 */
public class Theme {
    
    // ========== MODERN COLOR PALETTE ==========
    
    // Primary Colors (Modern Blue Gradient)
    public static final Color PRIMARY_BLUE = new Color(64, 123, 255);
    public static final Color PRIMARY_BLUE_DARK = new Color(45, 85, 255);
    public static final Color PRIMARY_BLUE_LIGHT = new Color(120, 169, 255);
    
    // Secondary Colors (Modern Purple/Pink)
    public static final Color SECONDARY_PURPLE = new Color(155, 81, 224);
    public static final Color SECONDARY_PINK = new Color(255, 69, 144);
    
    // Accent Colors
    public static final Color ACCENT_GREEN = new Color(52, 199, 89);
    public static final Color ACCENT_ORANGE = new Color(255, 149, 0);
    public static final Color ACCENT_RED = new Color(255, 59, 48);
    
    // Neutral Colors (Modern Gray Scale)
    public static final Color NEUTRAL_100 = new Color(248, 250, 252);
    public static final Color NEUTRAL_200 = new Color(226, 232, 240);
    public static final Color NEUTRAL_300 = new Color(203, 213, 225);
    public static final Color NEUTRAL_400 = new Color(148, 163, 184);
    public static final Color NEUTRAL_500 = new Color(100, 116, 139);
    public static final Color NEUTRAL_600 = new Color(71, 85, 105);
    public static final Color NEUTRAL_700 = new Color(51, 65, 85);
    public static final Color NEUTRAL_800 = new Color(30, 41, 59);
    public static final Color NEUTRAL_900 = new Color(15, 23, 42);
    
    // ========== COMPONENT COLORS ==========
    
    // Main Button (Modern gradient)
    public static final Color MAIN_BUTTON_COLOR = PRIMARY_BLUE;
    public static final Color MAIN_BUTTON_HOVER = PRIMARY_BLUE_DARK;
    public static final Color MAIN_BUTTON_PRESSED = new Color(35, 70, 220);
    
    // Fake Button (Modern red gradient)
    public static final Color FAKE_BUTTON_COLOR = ACCENT_RED;
    public static final Color FAKE_BUTTON_HOVER = new Color(220, 50, 40);
    
    // Background (Modern gradient)
    public static final Color BACKGROUND_GRADIENT_TOP = new Color(99, 102, 241);
    public static final Color BACKGROUND_GRADIENT_BOTTOM = new Color(139, 69, 234);
    
    // Surface Colors
    public static final Color SURFACE_PRIMARY = new Color(255, 255, 255, 240);
    public static final Color SURFACE_SECONDARY = new Color(255, 255, 255, 200);
    public static final Color SURFACE_ELEVATED = new Color(255, 255, 255, 250);
    
    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(30, 41, 59);
    public static final Color TEXT_SECONDARY = new Color(71, 85, 105);
    public static final Color TEXT_MUTED = new Color(148, 163, 184);
    public static final Color TEXT_ON_ACCENT = Color.WHITE;
    
    // ========== MODERN TYPOGRAPHY ==========
    
    // Try to use system fonts that look modern
    private static final String[] MODERN_FONT_STACK = {
        "Segoe UI Variable", "Segoe UI", "San Francisco", "Helvetica Neue", 
        "Arial", "system-ui", "sans-serif"
    };
    
    private static Font getModernFont(int style, int size) {
        for (String fontName : MODERN_FONT_STACK) {
            if (Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
                    .contains(fontName)) {
                return new Font(fontName, style, size);
            }
        }
        return new Font("Segoe UI", style, size); // Fallback
    }
    
    // Typography Scale
    public static final Font DISPLAY_FONT = getModernFont(Font.BOLD, 48);
    public static final Font TITLE_LARGE_FONT = getModernFont(Font.BOLD, 36);
    public static final Font TITLE_MEDIUM_FONT = getModernFont(Font.BOLD, 28);
    public static final Font TITLE_SMALL_FONT = getModernFont(Font.BOLD, 22);
    public static final Font BODY_LARGE_FONT = getModernFont(Font.PLAIN, 18);
    public static final Font BODY_MEDIUM_FONT = getModernFont(Font.PLAIN, 16);
    public static final Font BODY_SMALL_FONT = getModernFont(Font.PLAIN, 14);
    public static final Font CAPTION_FONT = getModernFont(Font.PLAIN, 12);
    
    // Component-specific fonts
    public static final Font LABEL_FONT = BODY_MEDIUM_FONT;
    public static final Font BUTTON_FONT = BODY_LARGE_FONT;
    public static final Font OVERLAY_TITLE_FONT = TITLE_LARGE_FONT;
    public static final Font OVERLAY_BUTTON_FONT = TITLE_SMALL_FONT;
    
    // ========== MODERN SPACING & DIMENSIONS ==========
    
    // Spacing scale (8px base)
    public static final int SPACING_XS = 4;
    public static final int SPACING_SM = 8;
    public static final int SPACING_MD = 16;
    public static final int SPACING_LG = 24;
    public static final int SPACING_XL = 32;
    public static final int SPACING_2XL = 48;
    public static final int SPACING_3XL = 64;
    
    // Border radius
    public static final int BORDER_RADIUS_SM = 8;
    public static final int BORDER_RADIUS_MD = 12;
    public static final int BORDER_RADIUS_LG = 16;
    public static final int BORDER_RADIUS_XL = 20;
    
    // Shadow values
    public static final Color SHADOW_COLOR = new Color(0, 0, 0, 15);
    public static final Color SHADOW_COLOR_STRONG = new Color(0, 0, 0, 25);
    
    // ========== GRADIENT UTILITIES ==========
    
    /**
     * Creates a modern gradient paint for buttons and backgrounds
     */
    public static GradientPaint createGradient(Component component, Color color1, Color color2, boolean vertical) {
        if (vertical) {
            return new GradientPaint(0, 0, color1, 0, component.getHeight(), color2);
        } else {
            return new GradientPaint(0, 0, color1, component.getWidth(), 0, color2);
        }
    }
    
    /**
     * Creates a subtle gradient for buttons
     */
    public static GradientPaint createButtonGradient(Component component, Color baseColor) {
        Color lighter = brightenColor(baseColor, 0.1f);
        Color darker = darkenColor(baseColor, 0.1f);
        return new GradientPaint(0, 0, lighter, 0, component.getHeight(), darker);
    }
    
    // Color utility methods
    public static Color brightenColor(Color color, float factor) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hsb[2] = Math.min(1.0f, hsb[2] + factor);
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }
    
    public static Color darkenColor(Color color, float factor) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hsb[2] = Math.max(0.0f, hsb[2] - factor);
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }
}
