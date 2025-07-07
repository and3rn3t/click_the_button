package com.andernet.experiment.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Enhanced animated button with modern styling and smooth animations
 */
public class AnimatedButton extends JButton {
    private float alpha = 1.0f;
    private Color baseColor = Theme.MAIN_BUTTON_COLOR;
    private int borderRadius = Theme.BORDER_RADIUS_LG;
    
    public AnimatedButton(String text) { 
        super(text);
        initializeButton();
    }
    
    public AnimatedButton(String text, Color color) {
        super(text);
        this.baseColor = color;
        initializeButton();
    }
    
    private void initializeButton() {
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setFont(Theme.BUTTON_FONT);
        setForeground(Theme.TEXT_ON_ACCENT);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    public void setAlpha(float a) { 
        alpha = a; 
        repaint(); 
    }
    
    public float getAlpha() { 
        return alpha; 
    }
    
    public void setBaseColor(Color color) {
        this.baseColor = color;
        repaint();
    }
    
    public void setBorderRadius(int radius) {
        this.borderRadius = radius;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        
        int width = getWidth();
        int height = getHeight();
        
        // Create rounded rectangle shape
        RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, width, height, 
                                                               borderRadius, borderRadius);
        
        // Draw enhanced shadow
        g2.setColor(new Color(0, 0, 0, 30));
        g2.fill(new RoundRectangle2D.Float(0, 4, width, height, borderRadius, borderRadius));
        
        // Create gradient background
        GradientPaint gradient = new GradientPaint(0, 0, 
                                                  Theme.brightenColor(baseColor, 0.15f), 
                                                  0, height, 
                                                  Theme.darkenColor(baseColor, 0.1f));
        g2.setPaint(gradient);
        g2.fill(roundRect);
        
        // Add subtle border highlight
        g2.setColor(new Color(255, 255, 255, 40));
        g2.setStroke(new BasicStroke(1.5f));
        g2.draw(roundRect);
        
        // Draw text with better positioning
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
