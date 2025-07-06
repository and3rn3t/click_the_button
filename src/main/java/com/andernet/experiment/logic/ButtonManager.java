package com.andernet.experiment.logic;

import com.andernet.experiment.ui.FakeButton;
import com.andernet.experiment.util.ResourceManager;
import com.andernet.experiment.settings.Settings;
import javax.swing.*;

/**
 * ButtonManager handles creation and management of fake buttons.
 */
public class ButtonManager {
    private FakeButton[] fakeButtons;
    private final Settings settings;
    private final GameState gameState;
    private final JLabel scoreLabel;
    private final Runnable moveAllButtons;
    private final Runnable randomizeColors;
    private final JPanel parent;

    public ButtonManager(Settings settings, GameState gameState, JLabel scoreLabel, Runnable moveAllButtons, Runnable randomizeColors, JPanel parent) {
        this.settings = settings;
        this.gameState = gameState;
        this.scoreLabel = scoreLabel;
        this.moveAllButtons = moveAllButtons;
        this.randomizeColors = randomizeColors;
        this.parent = parent;
    }

    public void createFakeButtons() {
        if (fakeButtons != null) {
            for (FakeButton fake : fakeButtons) parent.remove(fake);
        }
        fakeButtons = new FakeButton[settings.getNumFakeButtons()];
        for (int i = 0; i < settings.getNumFakeButtons(); i++) {
            fakeButtons[i] = new FakeButton("Fake!");
            fakeButtons[i].addActionListener(e -> {
                gameState.decrementScore(2);
                scoreLabel.setText("Score: " + gameState.getScore());
                ResourceManager.playFakeBeep();
                moveAllButtons.run();
                randomizeColors.run();
            });
            fakeButtons[i].setToolTipText("Don't click! These are fake buttons.");
            parent.add(fakeButtons[i]);
        }
    }

    public void moveFakeButtons() {
        for (FakeButton fake : fakeButtons) {
            int x = (int) (parent.getWidth() * Math.random() * 0.8);
            int y = (int) (parent.getHeight() * Math.random() * 0.7 + 40);
            fake.setLocation(x, y);
        }
    }

    public FakeButton[] getFakeButtons() {
        return fakeButtons;
    }
}
