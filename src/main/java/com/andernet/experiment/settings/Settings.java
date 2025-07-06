package com.andernet.experiment.settings;

/**
 * Settings holds user-customizable gameplay options for ClickTheButtonGame.
 */
public class Settings {
    private int gameDurationSeconds = 30;
    private int numFakeButtons = 3;
    private int moveIntervalMs = 1000;
    private boolean soundEnabled = true;
    private int mainButtonStartWidth = 140;
    private int mainButtonStartHeight = 60;
    // Future: color theme, etc.

    public int getGameDurationSeconds() { return gameDurationSeconds; }
    public void setGameDurationSeconds(int seconds) { this.gameDurationSeconds = seconds; }

    public int getNumFakeButtons() { return numFakeButtons; }
    public void setNumFakeButtons(int num) { this.numFakeButtons = num; }

    public int getMoveIntervalMs() { return moveIntervalMs; }
    public void setMoveIntervalMs(int ms) { this.moveIntervalMs = ms; }

    public boolean isSoundEnabled() { return soundEnabled; }
    public void setSoundEnabled(boolean enabled) { this.soundEnabled = enabled; }

    public int getMainButtonStartWidth() { return mainButtonStartWidth; }
    public void setMainButtonStartWidth(int width) { this.mainButtonStartWidth = width; }

    public int getMainButtonStartHeight() { return mainButtonStartHeight; }
    public void setMainButtonStartHeight(int height) { this.mainButtonStartHeight = height; }
}
