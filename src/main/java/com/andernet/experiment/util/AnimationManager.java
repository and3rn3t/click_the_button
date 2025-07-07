package com.andernet.experiment.util;

import javax.swing.*;
import java.awt.*;
import com.andernet.experiment.ui.AnimatedButton;
import com.andernet.experiment.logic.GameConstants;

/**
 * AnimationManager handles all animation logic for the game
 */
public class AnimationManager {
    
    /**
     * Animates a button with fade out, move, then fade in effect
     */
    public static void fadeAndMoveButton(AnimatedButton button, Runnable onComplete) {
        // Fade out
        Timer fadeOut = new Timer(50, null);
        fadeOut.addActionListener(e -> {
            float alpha = button.getAlpha() - 0.1f;
            if (alpha <= 0) {
                alpha = 0;
                button.setAlpha(alpha);
                fadeOut.stop();
                
                // Move button to new position
                moveButtonToRandomLocation(button);
                
                // Fade in
                fadeInButton(button, onComplete);
            } else {
                button.setAlpha(alpha);
            }
        });
        fadeOut.start();
    }
    
    /**
     * Fades a button in from transparent to opaque
     */
    private static void fadeInButton(AnimatedButton button, Runnable onComplete) {
        Timer fadeIn = new Timer(50, null);
        fadeIn.addActionListener(e -> {
            float alpha = button.getAlpha() + 0.1f;
            if (alpha >= 1.0f) {
                alpha = 1.0f;
                button.setAlpha(alpha);
                fadeIn.stop();
                if (onComplete != null) {
                    onComplete.run();
                }
            } else {
                button.setAlpha(alpha);
            }
        });
        fadeIn.start();
    }
    
    /**
     * Moves button to a random location within its parent container
     */
    private static void moveButtonToRandomLocation(AnimatedButton button) {
        Container parent = button.getParent();
        if (parent != null) {
            int maxX = parent.getWidth() - button.getWidth();
            int maxY = parent.getHeight() - button.getHeight() - GameConstants.FAKE_BUTTON_MARGIN_BOTTOM;
            
            if (maxX > 0 && maxY > GameConstants.FAKE_BUTTON_MARGIN_TOP) {
                int x = (int) (Math.random() * maxX);
                int y = (int) (Math.random() * (maxY - GameConstants.FAKE_BUTTON_MARGIN_TOP)) + GameConstants.FAKE_BUTTON_MARGIN_TOP;
                button.setLocation(x, y);
            }
        }
    }
    
    /**
     * Creates a button highlight effect that brightens then returns to original color
     */
    public static void highlightButton(JButton button) {
        Color originalColor = button.getBackground();
        Color brightColor = originalColor.brighter();
        
        button.setBackground(brightColor);
        
        Timer highlightTimer = new Timer(GameConstants.BUTTON_HIGHLIGHT_DURATION, e -> {
            button.setBackground(originalColor);
        });
        highlightTimer.setRepeats(false);
        highlightTimer.start();
    }
    
    /**
     * Shows a floating score label that moves up and fades out
     */
    public static void showFloatingScore(Container parent, int value, int x, int y) {
        JLabel floatingLabel = new JLabel((value > 0 ? "+" : "") + value);
        floatingLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        floatingLabel.setForeground(value > 0 ? Color.GREEN : Color.RED);
        floatingLabel.setBounds(x - 15, y - 20, 50, 30);
        floatingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        parent.add(floatingLabel, 0); // Add to front
        parent.repaint();
        
        // Animate floating up and fading out
        Timer floatTimer = new Timer(50, null);
        final int[] steps = {0};
        final int maxSteps = 20;
        
        floatTimer.addActionListener(e -> {
            steps[0]++;
            
            // Move up
            floatingLabel.setLocation(floatingLabel.getX(), floatingLabel.getY() - 2);
            
            // Fade out
            float alpha = 1.0f - (float) steps[0] / maxSteps;
            Color currentColor = floatingLabel.getForeground();
            Color fadedColor = new Color(
                currentColor.getRed(),
                currentColor.getGreen(),
                currentColor.getBlue(),
                Math.max(0, (int) (255 * alpha))
            );
            floatingLabel.setForeground(fadedColor);
            
            if (steps[0] >= maxSteps) {
                floatTimer.stop();
                parent.remove(floatingLabel);
                parent.repaint();
            }
        });
        floatTimer.start();
    }
    
    /**
     * Creates a smooth countdown animation for game start
     */
    public static void animateCountdown(JLabel label, Runnable onComplete) {
        final String[] countdownTexts = {
            "<html><div style='text-align:center;font-size:36px;'>3</div></html>",
            "<html><div style='text-align:center;font-size:36px;'>2</div></html>",
            "<html><div style='text-align:center;font-size:36px;'>1</div></html>",
            "<html><div style='text-align:center;font-size:36px;'>GO!</div></html>"
        };
        
        final int[] currentIndex = {0};
        
        Timer countdownTimer = new Timer(GameConstants.COUNTDOWN_TIMER_DELAY, null);
        countdownTimer.addActionListener(e -> {
            if (currentIndex[0] < countdownTexts.length) {
                label.setText(countdownTexts[currentIndex[0]]);
                currentIndex[0]++;
            } else {
                countdownTimer.stop();
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });
        
        // Start with first countdown number
        label.setText(countdownTexts[0]);
        currentIndex[0] = 1;
        countdownTimer.start();
    }
}
