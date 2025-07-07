package com.andernet.experiment.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern fake button with enhanced styling and warning appearance
 */
public class FakeButton extends JButton {
    private Color baseColor = Theme.FAKE_BUTTON_COLOR;
    private int borderRadius = Theme.BORDER_RADIUS_MD;
    
    public FakeButton(String text) {
        super(text);
        initializeButton();
    }
    
    private void initializeButton() {
        setFont(Theme.BODY_LARGE_FONT);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setBackground(baseColor);
        setForeground(Theme.TEXT_ON_ACCENT);
        setBounds(0, 0, 80, 40);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        
        // Create rounded rectangle shape
        RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, width, height, 
                                                               borderRadius, borderRadius);
        
        // Draw enhanced shadow
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fill(new RoundRectangle2D.Float(0, 3, width, height, borderRadius, borderRadius));
        
        // Create warning gradient (red to slightly darker red)
        GradientPaint gradient = new GradientPaint(0, 0, 
                                                  Theme.brightenColor(baseColor, 0.1f), 
                                                  0, height, 
                                                  Theme.darkenColor(baseColor, 0.1f));
        g2.setPaint(gradient);
        g2.fill(roundRect);
        
        // Add warning border
        g2.setColor(new Color(255, 255, 255, 50));
        g2.setStroke(new BasicStroke(1.5f));
        g2.draw(roundRect);
        
        // Draw text
        g2.setColor(getForeground());
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        
        String text = getText();
        if (text != null && !text.isEmpty()) {
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();
            int x = (width - textWidth) / 2;
            int y = (height - textHeight) / 2 + fm.getAscent();
            
            g2.drawString(text, x, y);
        }
        
        g2.dispose();
    }
}
