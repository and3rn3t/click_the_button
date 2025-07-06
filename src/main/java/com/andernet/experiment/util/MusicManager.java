package com.andernet.experiment.util;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class MusicManager {
    private static Clip backgroundClip;

    public static void playBackgroundMusic(String resourcePath, boolean loop) {
        stopBackgroundMusic();
        try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(MusicManager.class.getResource(resourcePath))) {
            URL url = MusicManager.class.getResource(resourcePath);
            if (url == null) {
                System.err.println("[MusicManager] Audio resource not found: " + resourcePath);
                return;
            }
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioIn);
            if (loop) {
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                backgroundClip.start();
            }
        } catch (UnsupportedAudioFileException e) {
            System.err.println("[MusicManager] Unsupported audio file: " + resourcePath);
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.err.println("[MusicManager] Audio line unavailable: " + resourcePath);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("[MusicManager] IO error playing audio: " + resourcePath);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[MusicManager] Unexpected error: " + resourcePath);
            e.printStackTrace();
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
