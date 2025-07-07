package com.andernet.experiment.util;

import javax.sound.sampled.*;
import java.net.URL;

public class ResourceManager {
    public static void playBeep() {
        playSound(Constants.CLICK_SOUND);
    }
    
    public static void playFakeBeep() {
        playSound(Constants.FAKE_SOUND);
    }
    
    public static void playEndBeep() {
        playSound(Constants.GAMEOVER_SOUND);
    }
    
    public static void playSound(String resourcePath) {
        new Thread(() -> {
            try {
                URL url = ResourceManager.class.getResource(resourcePath);
                if (url == null) return;
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            } catch (Exception ignored) {}
        }).start();
    }
}
