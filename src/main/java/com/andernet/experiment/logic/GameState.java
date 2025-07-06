package com.andernet.experiment.logic;

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
}
