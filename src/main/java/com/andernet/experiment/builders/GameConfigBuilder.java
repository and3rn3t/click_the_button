package com.andernet.experiment.builders;

import com.andernet.experiment.settings.Settings;
import com.andernet.experiment.logic.GameConstants;

/**
 * Builder pattern for creating game configurations
 */
public class GameConfigBuilder {
    private int gameDurationSeconds = GameConstants.GAME_TIME_SECONDS;
    private int numFakeButtons = GameConstants.NUM_FAKE_BUTTONS;
    private int moveIntervalMs = GameConstants.MOVE_INTERVAL_MS;
    private boolean soundEnabled = true;
    private int mainButtonStartWidth = GameConstants.MAIN_BUTTON_START_WIDTH;
    private int mainButtonStartHeight = GameConstants.MAIN_BUTTON_START_HEIGHT;
    
    public GameConfigBuilder withGameDuration(int seconds) {
        this.gameDurationSeconds = seconds;
        return this;
    }
    
    public GameConfigBuilder withFakeButtons(int count) {
        this.numFakeButtons = count;
        return this;
    }
    
    public GameConfigBuilder withMoveInterval(int ms) {
        this.moveIntervalMs = ms;
        return this;
    }
    
    public GameConfigBuilder withSound(boolean enabled) {
        this.soundEnabled = enabled;
        return this;
    }
    
    public GameConfigBuilder withMainButtonSize(int width, int height) {
        this.mainButtonStartWidth = width;
        this.mainButtonStartHeight = height;
        return this;
    }
    
    public Settings build() {
        Settings settings = new Settings();
        settings.setGameDurationSeconds(gameDurationSeconds);
        settings.setNumFakeButtons(numFakeButtons);
        settings.setMoveIntervalMs(moveIntervalMs);
        settings.setSoundEnabled(soundEnabled);
        settings.setMainButtonStartWidth(mainButtonStartWidth);
        settings.setMainButtonStartHeight(mainButtonStartHeight);
        return settings;
    }
    
    /**
     * Creates a quick game configuration (shorter duration, fewer obstacles)
     */
    public static Settings createQuickGame() {
        return new GameConfigBuilder()
            .withGameDuration(15)
            .withFakeButtons(1)
            .withMoveInterval(1500)
            .build();
    }
    
    /**
     * Creates a challenge game configuration (longer duration, more obstacles)
     */
    public static Settings createChallengeGame() {
        return new GameConfigBuilder()
            .withGameDuration(60)
            .withFakeButtons(5)
            .withMoveInterval(800)
            .withMainButtonSize(80, 40)
            .build();
    }
}
