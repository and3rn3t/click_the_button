package com.andernet.experiment.util;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class MusicManager {
    private static Clip backgroundClip;

    public static void playBackgroundMusic(String resourcePath, boolean loop) {
        stopBackgroundMusic();
        try {
            URL url = MusicManager.class.getResource(resourcePath);
            if (url == null) {
                System.err.println("[MusicManager] Audio resource not found: " + resourcePath);
                return;
            }
            try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(url)) {
                backgroundClip = AudioSystem.getClip();
                backgroundClip.open(audioIn);
                if (loop) {
                    backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    backgroundClip.start();
                }
            }
        } catch (UnsupportedAudioFileException e) {
            System.err.println("[MusicManager] Unsupported audio file: " + resourcePath);
        } catch (LineUnavailableException e) {
            System.err.println("[MusicManager] Audio line unavailable: " + resourcePath);
        } catch (IOException e) {
            System.err.println("[MusicManager] IO error playing audio: " + resourcePath);
        } catch (Exception e) {
            // Suppress NullPointerException and other errors in test mode
            if (Boolean.getBoolean("ctb.testmode")) {
                // Silently ignore in test mode
                return;
            }
            System.err.println("[MusicManager] Unexpected error: " + resourcePath);
        }
    }

    public static void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
            backgroundClip = null;
        }
    }
}
