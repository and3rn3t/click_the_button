package com.andernet.experiment;

import javax.swing.*;
import java.awt.*;

public class AnimatedButton extends JButton {
    private float alpha = 1.0f;
    public AnimatedButton(String text) { super(text); }
    public void setAlpha(float a) { alpha = a; repaint(); }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        // Drop shadow
        g2.setColor(new Color(0,0,0,60));
        g2.fillRoundRect(4, 4, getWidth()-8, getHeight()-8, 30, 30);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth()-8, getHeight()-8, 30, 30);
        super.paintComponent(g2);
        g2.dispose();
    }
}
