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
}
