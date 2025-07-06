package com.andernet.experiment.settings;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SettingsTest {
    @Test
    void testDefaultSettings() {
        Settings s = new Settings();
        assertEquals(30, s.getGameDurationSeconds());
        assertEquals(3, s.getNumFakeButtons());
        assertEquals(1000, s.getMoveIntervalMs());
        assertTrue(s.isSoundEnabled());
        assertEquals(140, s.getMainButtonStartWidth());
        assertEquals(60, s.getMainButtonStartHeight());
    }
    @Test
    void testSetters() {
        Settings s = new Settings();
        s.setGameDurationSeconds(99);
        s.setNumFakeButtons(7);
        s.setMoveIntervalMs(555);
        s.setSoundEnabled(false);
        s.setMainButtonStartWidth(200);
        s.setMainButtonStartHeight(80);
        assertEquals(99, s.getGameDurationSeconds());
        assertEquals(7, s.getNumFakeButtons());
        assertEquals(555, s.getMoveIntervalMs());
        assertFalse(s.isSoundEnabled());
        assertEquals(200, s.getMainButtonStartWidth());
        assertEquals(80, s.getMainButtonStartHeight());
    }
}
