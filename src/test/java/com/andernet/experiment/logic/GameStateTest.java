package com.andernet.experiment.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

class GameStateTest {
    @Test
    void testScoreIncrementAndHighScore() {
        GameState state = new GameState(30);
        assertEquals(0, state.getScore());
        assertEquals(0, state.getHighScore());
        state.incrementScore();
        assertEquals(1, state.getScore());
        assertEquals(1, state.getHighScore());
        state.incrementScore();
        assertEquals(2, state.getScore());
        assertEquals(2, state.getHighScore());
    }

    @Test
    void testScoreDecrement() {
        GameState state = new GameState(30);
        state.incrementScore();
        state.incrementScore();
        state.decrementScore(1);
        assertEquals(1, state.getScore());
        state.decrementScore(5);
        assertEquals(0, state.getScore());
    }

    @Test
    void testResetAndTime() {
        GameState state = new GameState(30);
        state.incrementScore();
        state.decrementTime();
        assertEquals(29, state.getTimeLeft());
        state.reset(20);
        assertEquals(0, state.getScore());
        assertEquals(20, state.getTimeLeft());
    }

    @Test
    void testHighScorePersistence() {
        GameState state = new GameState(30);
        File tmp = new File(System.getProperty("java.io.tmpdir"), "ctb_test_highscore");
        tmp.delete();
        state.incrementScore();
        state.incrementScore();
        state.saveHighScore(tmp);
        GameState loaded = new GameState(30);
        loaded.loadHighScore(tmp);
        assertEquals(2, loaded.getHighScore());
        tmp.delete();
    }
}
