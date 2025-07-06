package com.andernet.experiment.ui;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnimatedButtonTest {
    @Test
    void testSetAlpha() {
        AnimatedButton btn = new AnimatedButton("Test");
        btn.setAlpha(0.5f);
        // No exception, alpha should be set
        assertEquals(0.5f, btn.getAlpha(), 0.01);
    }
}
