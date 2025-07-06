package com.andernet.experiment.logic;

import java.io.*;

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
    }
    public void decrementTime() { timeLeft--; }

    public void saveHighScore(File file) {
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            out.println(highScore);
        } catch (IOException ignored) {}
    }
    public void loadHighScore(File file) {
        if (!file.exists()) return;
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line = in.readLine();
            if (line != null) highScore = Integer.parseInt(line.trim());
        } catch (IOException | NumberFormatException ignored) {}
    }
}
