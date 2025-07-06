package com.andernet.experiment.logic;

import com.andernet.experiment.settings.Settings;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javax.swing.*;

class ButtonManagerTest {
    @Test
    void testCreateFakeButtons() {
        Settings s = new Settings();
        GameState gs = new GameState(30);
        JLabel scoreLabel = new JLabel();
        JPanel panel = new JPanel();
        ButtonManager bm = new ButtonManager(s, gs, scoreLabel, ()->{}, ()->{}, panel);
        bm.createFakeButtons();
        assertEquals(s.getNumFakeButtons(), bm.getFakeButtons().length);
        for (var fake : bm.getFakeButtons()) {
            assertNotNull(fake);
            assertTrue(panel.isAncestorOf(fake));
        }
    }
}
