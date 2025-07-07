package com.andernet.experiment.handlers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.andernet.experiment.logic.GameState;
import com.andernet.experiment.settings.Settings;
import com.andernet.experiment.util.ResourceManager;
import com.andernet.experiment.util.AnimationManager;
import com.andernet.experiment.util.Constants;
import com.andernet.experiment.logic.GameConstants;

/**
 * Handles fake button click events
 */
public class FakeButtonClickHandler implements ActionListener {
    private final GameState gameState;
    private final Settings settings;
    private final JLabel scoreLabel;
    private final JButton fakeButton;
    private final Runnable moveAllButtons;
    private final Runnable randomizeColors;
    
    public FakeButtonClickHandler(GameState gameState, Settings settings,
                                JLabel scoreLabel, JButton fakeButton,
                                Runnable moveAllButtons, Runnable randomizeColors) {
        this.gameState = gameState;
        this.settings = settings;
        this.scoreLabel = scoreLabel;
        this.fakeButton = fakeButton;
        this.moveAllButtons = moveAllButtons;
        this.randomizeColors = randomizeColors;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Update game state (penalty)
        gameState.decrementScore(GameConstants.FAKE_BUTTON_PENALTY);
        
        // Update UI
        scoreLabel.setText(Constants.SCORE_PREFIX + gameState.getScore());
        
        // Play penalty sound
        if (settings.isSoundEnabled()) {
            ResourceManager.playFakeBeep();
        }
        
        // Visual feedback for penalty
        AnimationManager.showFloatingScore(fakeButton.getParent(),
            -GameConstants.FAKE_BUTTON_PENALTY,
            fakeButton.getX() + fakeButton.getWidth() / 2,
            fakeButton.getY());
        
        // Move buttons and change colors
        moveAllButtons.run();
        randomizeColors.run();
    }
}
