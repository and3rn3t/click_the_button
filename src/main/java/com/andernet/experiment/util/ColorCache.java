package com.andernet.experiment.util;

import java.awt.Color;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Color cache for performance optimization
 */
public class ColorCache {
    private static final ConcurrentHashMap<String, Color> colorCache = new ConcurrentHashMap<>();
    
    /**
     * Gets a cached darker version of a color
     */
    public static Color getDarkerColor(Color original) {
        String key = "darker_" + original.getRGB();
        return colorCache.computeIfAbsent(key, k -> original.darker());
    }
    
    /**
     * Gets a cached brighter version of a color
     */
    public static Color getBrighterColor(Color original) {
        String key = "brighter_" + original.getRGB();
        return colorCache.computeIfAbsent(key, k -> original.brighter());
    }
    
    /**
     * Gets a cached translucent version of a color
     */
    public static Color getTranslucentColor(Color original, int alpha) {
        String key = "translucent_" + original.getRGB() + "_" + alpha;
        return colorCache.computeIfAbsent(key, k -> new Color(
            original.getRed(), 
            original.getGreen(), 
            original.getBlue(), 
            alpha
        ));
    }
    
    /**
     * Clears the color cache (useful for memory management)
     */
    public static void clearCache() {
        colorCache.clear();
    }
}
