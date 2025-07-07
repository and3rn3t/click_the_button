package com.andernet.experiment.logic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.andernet.experiment.observers.GameStateListener;

public class GameState {
    private int score = 0;
    private int highScore = 0;
    private int timeLeft;
    private final List<GameStateListener> listeners = new ArrayList<>();

    public GameState(int initialTime) {
        this.timeLeft = initialTime;
    }

    /**
     * Adds a listener for game state changes
     */
    public void addListener(GameStateListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener for game state changes
     */
    public void removeListener(GameStateListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all listeners of score change
     */
    private void notifyScoreChanged() {
        for (GameStateListener listener : listeners) {
            listener.onScoreChanged(score, highScore);
        }
    }

    /**
     * Notifies all listeners of time change
     */
    private void notifyTimeChanged() {
        for (GameStateListener listener : listeners) {
            listener.onTimeChanged(timeLeft);
        }
    }

    /**
     * Notifies all listeners of game reset
     */
    private void notifyGameReset() {
        for (GameStateListener listener : listeners) {
            listener.onGameReset(timeLeft);
        }
    }

    public int getScore() { return score; }
    public int getHighScore() { return highScore; }
    public int getTimeLeft() { return timeLeft; }

    public void incrementScore() {
        score++;
        if (score > highScore) highScore = score;
        notifyScoreChanged();
    }
    public void decrementScore(int amount) {
        score = Math.max(0, score - amount);
        notifyScoreChanged();
    }
    public void reset(int initialTime) {
        score = 0;
        timeLeft = initialTime;
        notifyGameReset();
        // Do NOT reset highScore here; it should persist
    }
    public void decrementTime() {
        timeLeft--;
        notifyTimeChanged();
    }

    public void saveHighScore(File file) {
        try {
            java.nio.file.Files.write(file.toPath(), String.valueOf(highScore).getBytes());
        } catch (Exception e) {
            // Ignore save errors
        }
    }

    public void loadHighScore(File file) {
        try {
            if (file.exists()) {
                String content = new String(java.nio.file.Files.readAllBytes(file.toPath())).trim();
                int value = Integer.parseInt(content);
                highScore = value;
            } else {
                highScore = 0;
            }
        } catch (Exception e) {
            highScore = 0;
        }
    }
}
