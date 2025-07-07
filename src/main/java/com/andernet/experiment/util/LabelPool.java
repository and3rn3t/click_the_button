package com.andernet.experiment.util;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Memory pool for floating score labels to reduce garbage collection
 */
public class LabelPool {
    private static final ConcurrentLinkedQueue<JLabel> pool = new ConcurrentLinkedQueue<>();
    private static final int MAX_POOL_SIZE = 10;
    
    /**
     * Gets a label from the pool or creates a new one
     */
    public static JLabel getLabel() {
        JLabel label = pool.poll();
        if (label == null) {
            label = new JLabel();
            label.setFont(new Font("Segoe UI", Font.BOLD, 20));
            label.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return label;
    }
    
    /**
     * Returns a label to the pool for reuse
     */
    public static void returnLabel(JLabel label) {
        if (pool.size() < MAX_POOL_SIZE) {
            // Reset label state
            label.setText("");
            label.setForeground(Color.BLACK);
            label.setLocation(0, 0);
            label.setSize(50, 30);
            
            pool.offer(label);
        }
    }
}
