package com.andernet.experiment;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.andernet.experiment.settings.Settings;

class ClickTheButtonGameSmokeTest {
    @Test
    void testGameWindowConstructs() {
        assertDoesNotThrow(() -> {
            Settings s = new Settings();
            ClickTheButtonGame game = new ClickTheButtonGame(s);
            assertNotNull(game);
            // Don't show the window in test
            game.dispose();
        });
    }
}
