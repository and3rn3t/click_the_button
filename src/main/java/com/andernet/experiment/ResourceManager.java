package com.andernet.experiment;

import java.awt.*;

public class ResourceManager {
    // Placeholder for future image, sound, or other resource management
    public static void playBeep() {
        Toolkit.getDefaultToolkit().beep();
    }
    public static void playFakeBeep() {
        new Thread(() -> {
            try {
                Toolkit.getDefaultToolkit().beep();
                Thread.sleep(50);
                Toolkit.getDefaultToolkit().beep();
            } catch (InterruptedException ignored) {}
        }).start();
    }
    public static void playEndBeep() {
        new Thread(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    Toolkit.getDefaultToolkit().beep();
                    Thread.sleep(100);
                }
            } catch (InterruptedException ignored) {}
        }).start();
    }
}
