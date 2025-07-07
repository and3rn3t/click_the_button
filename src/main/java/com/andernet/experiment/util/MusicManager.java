package com.andernet.experiment.util;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class MusicManager {
    private static volatile Clip backgroundClip;
    private static final Object lock = new Object();

    public static void playBackgroundMusic(String resourcePath, boolean loop) {
        synchronized (lock) {
            stopBackgroundMusic();
            try {
                URL url = MusicManager.class.getResource(resourcePath);
                if (url == null) {
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
                // Silently ignore audio errors
            } catch (LineUnavailableException e) {
                // Silently ignore audio errors
            } catch (IOException e) {
                // Silently ignore audio errors
            } catch (Exception e) {
                if (Boolean.getBoolean("ctb.testmode")) {
                    return;
                }
            }
        }
    }

    public static void stopBackgroundMusic() {
        synchronized (lock) {
            if (backgroundClip != null && backgroundClip.isRunning()) {
                backgroundClip.stop();
                backgroundClip.close();
                backgroundClip = null;
            }
        }
    }
}
