package com.andernet.experiment.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResourceManagerTest {
    @Test
    void testPlayBeepDoesNotThrow() {
        assertDoesNotThrow(ResourceManager::playBeep);
    }
    @Test
    void testPlayFakeBeepDoesNotThrow() {
        assertDoesNotThrow(ResourceManager::playFakeBeep);
    }
    @Test
    void testPlayEndBeepDoesNotThrow() {
        assertDoesNotThrow(ResourceManager::playEndBeep);
    }
}
