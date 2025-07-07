package com.andernet.experiment.observers;

/**
 * Interface for listening to game state changes
 */
public interface GameStateListener {
    /**
     * Called when the score changes
     */
    void onScoreChanged(int newScore, int highScore);
    
    /**
     * Called when the time changes
     */
    void onTimeChanged(int timeLeft);
    
    /**
     * Called when the game state is reset
     */
    void onGameReset(int initialTime);
}
