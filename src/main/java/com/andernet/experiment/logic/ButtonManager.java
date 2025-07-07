package com.andernet.experiment.logic;

import com.andernet.experiment.ui.FakeButton;
import com.andernet.experiment.util.Constants;
import com.andernet.experiment.settings.Settings;
import com.andernet.experiment.handlers.FakeButtonClickHandler;
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
            fakeButtons[i] = new FakeButton(Constants.FAKE);
            
            // Create handler for each fake button
            FakeButtonClickHandler handler = new FakeButtonClickHandler(
                gameState, settings, scoreLabel, fakeButtons[i],
                moveAllButtons, randomizeColors
            );
            fakeButtons[i].addActionListener(handler);
            
            fakeButtons[i].setToolTipText(Constants.FAKE_BUTTON_TOOLTIP);
            parent.add(fakeButtons[i]);
        }
    }

    public void moveFakeButtons() {
        for (FakeButton fake : fakeButtons) {
            int x = (int) (parent.getWidth() * Math.random() * GameConstants.FAKE_BUTTON_WIDTH_RATIO);
            int y = (int) (parent.getHeight() * Math.random() * GameConstants.FAKE_BUTTON_HEIGHT_RATIO + GameConstants.FAKE_BUTTON_MARGIN_TOP);
            fake.setLocation(x, y);
        }
    }

    public FakeButton[] getFakeButtons() {
        return fakeButtons;
    }
}
