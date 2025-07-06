package com.andernet.experiment.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MusicManagerTest {
    @Test
    void testPlayAndStopBackgroundMusicDoesNotThrow() {
        assertDoesNotThrow(() -> MusicManager.playBackgroundMusic("/audio/background.wav", false));
        assertDoesNotThrow(MusicManager::stopBackgroundMusic);
    }
}
