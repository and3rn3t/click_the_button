package com.andernet.experiment.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern button component with gradient backgrounds, smooth animations,
 * and enhanced visual effects.
 */
public class ModernButton extends JButton {
    private float alpha = 1.0f;
    private boolean isHovered = false;
    private boolean isPressed = false;
    private Color baseColor;
    private Color hoverColor;
    private Color pressedColor;
    private int borderRadius;
    private Timer animationTimer;
    private float animationProgress = 0.0f;
    
    public ModernButton(String text, Color baseColor) {
        super(text);
        this.baseColor = baseColor;
        this.hoverColor = Theme.darkenColor(baseColor, 0.1f);
        this.pressedColor = Theme.darkenColor(baseColor, 0.2f);
        this.borderRadius = Theme.BORDER_RADIUS_MD;
        
        initializeButton();
        setupAnimations();
    }
    
    public ModernButton(String text) {
        this(text, Theme.PRIMARY_BLUE);
    }
    
    private void initializeButton() {
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setFont(Theme.BUTTON_FONT);
        setForeground(Theme.TEXT_ON_ACCENT);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Set preferred size for better proportions
        setPreferredSize(new Dimension(160, 50));
        setMinimumSize(new Dimension(120, 40));
    }
    
    private void setupAnimations() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                animateToState(true);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                animateToState(false);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }
    
    private void animateToState(boolean toHover) {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        
        animationTimer = new Timer(16, e -> {
            if (toHover) {
                animationProgress = Math.min(1.0f, animationProgress + 0.15f);
            } else {
                animationProgress = Math.max(0.0f, animationProgress - 0.15f);
            }
            
            repaint();
            
            if ((toHover && animationProgress >= 1.0f) || (!toHover && animationProgress <= 0.0f)) {
                animationTimer.stop();
            }
        });
        
        animationTimer.start();
    }
    
    public void setAlpha(float alpha) {
        this.alpha = alpha;
        repaint();
    }
    
    public float getAlpha() {
        return alpha;
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
        RoundRectangle2D shadowRect = new RoundRectangle2D.Float(0, 2, width, height, 
                                                                borderRadius, borderRadius);
        
        // Draw shadow
        g2.setColor(new Color(0, 0, 0, 20));
        g2.fill(shadowRect);
        
        // Calculate button color based on state
        Color buttonColor = baseColor;
        if (isPressed) {
            buttonColor = pressedColor;
        } else if (animationProgress > 0) {
            buttonColor = interpolateColor(baseColor, hoverColor, animationProgress);
        }
        
        // Create gradient
        GradientPaint gradient = new GradientPaint(0, 0, 
                                                  Theme.brightenColor(buttonColor, 0.1f), 
                                                  0, height, 
                                                  Theme.darkenColor(buttonColor, 0.1f));
        g2.setPaint(gradient);
        g2.fill(roundRect);
        
        // Add subtle border
        g2.setColor(new Color(255, 255, 255, 30));
        g2.setStroke(new BasicStroke(1.0f));
        g2.draw(roundRect);
        
        // Draw text
        g2.setColor(getForeground());
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        
        String text = getText();
        if (text != null) {
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();
            int x = (width - textWidth) / 2;
            int y = (height - textHeight) / 2 + fm.getAscent();
            
            g2.drawString(text, x, y);
        }
        
        g2.dispose();
    }
    
    private Color interpolateColor(Color color1, Color color2, float factor) {
        if (factor <= 0) return color1;
        if (factor >= 1) return color2;
        
        int r = (int) (color1.getRed() + factor * (color2.getRed() - color1.getRed()));
        int g = (int) (color1.getGreen() + factor * (color2.getGreen() - color1.getGreen()));
        int b = (int) (color1.getBlue() + factor * (color2.getBlue() - color1.getBlue()));
        int a = (int) (color1.getAlpha() + factor * (color2.getAlpha() - color1.getAlpha()));
        
        return new Color(r, g, b, a);
    }
    
    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.width = Math.max(size.width + 40, 120); // Add padding
        size.height = Math.max(size.height + 20, 40);
        return size;
    }
}
