package com.andernet.experiment.ui;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.*;

class UIUtilsTest {
    @Test
    void testRandomPastelColor() {
        Color c1 = UIUtils.getRandomPastelColor(new java.util.Random());
        Color c2 = UIUtils.getRandomPastelColor(new java.util.Random());
        assertNotNull(c1);
        assertNotNull(c2);
        // Should be in pastel range (not too dark or saturated)
        assertTrue(c1.getRed() > 100 && c1.getGreen() > 100 && c1.getBlue() > 100);
        assertTrue(c2.getRed() > 100 && c2.getGreen() > 100 && c2.getBlue() > 100);
    }
}
