package com.andernet.experiment.logic;

import java.io.*;
import javax.swing.*;

public class GameState {
    private int score = 0;
    private int highScore = 0;
    private int timeLeft;

    public GameState(int initialTime) {
        this.timeLeft = initialTime;
    }

    public int getScore() { return score; }
    public int getHighScore() { return highScore; }
    public int getTimeLeft() { return timeLeft; }

    public void incrementScore() {
        score++;
        if (score > highScore) highScore = score;
    }
    public void decrementScore(int amount) {
        score = Math.max(0, score - amount);
    }
    public void reset(int initialTime) {
        score = 0;
        timeLeft = initialTime;
        // Do NOT reset highScore here; it should persist
    }
    public void decrementTime() { timeLeft--; }

    public void saveHighScore(File file) {
        try {
            java.nio.file.Files.write(file.toPath(), String.valueOf(highScore).getBytes());
            System.out.println("[DEBUG] GameState.saveHighScore: file=" + file.getAbsolutePath() + ", value=" + highScore);
        } catch (Exception e) {
            System.out.println("[DEBUG] GameState.saveHighScore: error: " + e);
        }
    }
    public void loadHighScore(File file) {
        try {
            if (file.exists()) {
                String content = new String(java.nio.file.Files.readAllBytes(file.toPath())).trim();
                System.out.println("[DEBUG] GameState.loadHighScore: file=" + file.getAbsolutePath() + ", content=" + content);
                int value = Integer.parseInt(content);
                highScore = value;
            } else {
                System.out.println("[DEBUG] GameState.loadHighScore: file does not exist: " + file.getAbsolutePath());
                highScore = 0;
            }
        } catch (Exception e) {
            System.out.println("[DEBUG] GameState.loadHighScore: error: " + e);
            highScore = 0;
        }
    }
}
