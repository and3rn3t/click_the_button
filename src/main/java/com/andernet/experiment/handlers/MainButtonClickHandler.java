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
 * Handles main button click events
 */
public class MainButtonClickHandler implements ActionListener {
    private final GameState gameState;
    private final Settings settings;
    private final JLabel scoreLabel;
    private final JLabel highScoreLabel;
    private final JButton button;
    private final Runnable moveAllButtons;
    private final Runnable randomizeColors;
    private final Runnable nextLevel;
    
    public MainButtonClickHandler(GameState gameState, Settings settings, 
                                JLabel scoreLabel, JLabel highScoreLabel, 
                                JButton button, Runnable moveAllButtons, 
                                Runnable randomizeColors, Runnable nextLevel) {
        this.gameState = gameState;
        this.settings = settings;
        this.scoreLabel = scoreLabel;
        this.highScoreLabel = highScoreLabel;
        this.button = button;
        this.moveAllButtons = moveAllButtons;
        this.randomizeColors = randomizeColors;
        this.nextLevel = nextLevel;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Update game state
        gameState.incrementScore();
        
        // Update UI
        updateScoreLabels();
        
        // Play sound effect
        if (settings.isSoundEnabled()) {
            ResourceManager.playBeep();
        }
        
        // Visual effects
        AnimationManager.highlightButton(button);
        AnimationManager.showFloatingScore(button.getParent(), 
            GameConstants.MAIN_BUTTON_SCORE, 
            button.getX() + button.getWidth() / 2, 
            button.getY());
        
        // Game mechanics
        nextLevel.run();
        moveAllButtons.run();
        randomizeColors.run();
    }
    
    private void updateScoreLabels() {
        scoreLabel.setText(Constants.SCORE_PREFIX + gameState.getScore());
        highScoreLabel.setText(Constants.HIGH_SCORE_PREFIX + gameState.getHighScore());
        
        // Force UI update for tests
        SwingUtilities.invokeLater(() -> {
            scoreLabel.revalidate();
            scoreLabel.repaint();
            highScoreLabel.revalidate();
            highScoreLabel.repaint();
        });
    }
}
