package com.andernet.experiment.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple performance monitoring utility for development
 */
public class PerformanceMonitor {
    private static final ConcurrentHashMap<String, Long> timings = new ConcurrentHashMap<>();
    private static final boolean ENABLED = Boolean.getBoolean("ctb.perf.monitor");
    
    /**
     * Start timing an operation
     */
    public static void startTiming(String operation) {
        if (ENABLED) {
            timings.put(operation, System.nanoTime());
        }
    }
    
    /**
     * End timing an operation and log the result
     */
    public static void endTiming(String operation) {
        if (ENABLED) {
            Long startTime = timings.remove(operation);
            if (startTime != null) {
                long duration = System.nanoTime() - startTime;
                System.out.println("[PERF] " + operation + ": " + (duration / 1_000_000.0) + " ms");
            }
        }
    }
    
    /**
     * Log memory usage
     */
    public static void logMemoryUsage() {
        if (ENABLED) {
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            
            System.out.println("[MEMORY] Used: " + (usedMemory / 1024 / 1024) + " MB, " +
                             "Free: " + (freeMemory / 1024 / 1024) + " MB, " +
                             "Total: " + (totalMemory / 1024 / 1024) + " MB");
        }
    }
}
