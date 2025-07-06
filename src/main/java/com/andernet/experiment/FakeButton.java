package com.andernet.experiment;

import javax.swing.*;
import java.awt.*;

public class FakeButton extends JButton {
    public FakeButton(String text) {
        super(text);
        setFont(new Font("Segoe UI", Font.PLAIN, 18));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setBackground(new Color(244, 67, 54));
        setForeground(Color.WHITE);
        setBounds(0, 0, 80, 40);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0,0,0,40));
        g2.fillRoundRect(4, 4, getWidth()-8, getHeight()-8, 30, 30);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth()-8, getHeight()-8, 30, 30);
        super.paintComponent(g2);
        g2.dispose();
    }
}
