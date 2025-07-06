package com.andernet.experiment.settings;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.Properties;

class SettingsPersistenceTest {
    @Test
    void testSaveAndLoadSettings() {
        Settings s = new Settings();
        s.setGameDurationSeconds(42);
        s.setNumFakeButtons(5);
        s.setMoveIntervalMs(777);
        s.setSoundEnabled(false);
        s.setMainButtonStartWidth(123);
        s.setMainButtonStartHeight(45);
        SettingsPersistence.save(s);
        Settings loaded = new Settings();
        SettingsPersistence.load(loaded);
        assertEquals(42, loaded.getGameDurationSeconds());
        assertEquals(5, loaded.getNumFakeButtons());
        assertEquals(777, loaded.getMoveIntervalMs());
        assertFalse(loaded.isSoundEnabled());
        assertEquals(123, loaded.getMainButtonStartWidth());
        assertEquals(45, loaded.getMainButtonStartHeight());
    }
}
