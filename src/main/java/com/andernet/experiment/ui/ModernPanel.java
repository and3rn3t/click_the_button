package com.andernet.experiment.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern panel component with rounded corners, subtle shadows, and gradient backgrounds.
 */
public class ModernPanel extends JPanel {
    private boolean hasGradient = false;
    private Color gradientColor1;
    private Color gradientColor2;
    private int borderRadius = Theme.BORDER_RADIUS_MD;
    private boolean hasShadow = true;
    private Color backgroundColor = Theme.SURFACE_PRIMARY;
    
    public ModernPanel() {
        super();
        setOpaque(false);
    }
    
    public ModernPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }
    
    public void setGradient(Color color1, Color color2) {
        this.gradientColor1 = color1;
        this.gradientColor2 = color2;
        this.hasGradient = true;
        repaint();
    }
    
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        this.hasGradient = false;
        repaint();
    }
    
    public void setBorderRadius(int radius) {
        this.borderRadius = radius;
        repaint();
    }
    
    public void setShadow(boolean shadow) {
        this.hasShadow = shadow;
        repaint();
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
        
        // Draw shadow
        if (hasShadow) {
            RoundRectangle2D shadowRect = new RoundRectangle2D.Float(0, 2, width, height, 
                                                                    borderRadius, borderRadius);
            g2.setColor(Theme.SHADOW_COLOR);
            g2.fill(shadowRect);
        }
        
        // Fill background
        if (hasGradient && gradientColor1 != null && gradientColor2 != null) {
            GradientPaint gradient = new GradientPaint(0, 0, gradientColor1, 
                                                      0, height, gradientColor2);
            g2.setPaint(gradient);
        } else {
            g2.setColor(backgroundColor);
        }
        
        g2.fill(roundRect);
        
        // Add subtle border
        g2.setColor(new Color(255, 255, 255, 50));
        g2.setStroke(new BasicStroke(1.0f));
        g2.draw(roundRect);
        
        g2.dispose();
        super.paintComponent(g);
    }
}
