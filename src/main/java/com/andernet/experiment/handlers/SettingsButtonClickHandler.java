package com.andernet.experiment.handlers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.andernet.experiment.settings.Settings;
import com.andernet.experiment.settings.SettingsDialog;
import com.andernet.experiment.settings.SettingsPersistence;
import com.andernet.experiment.logic.GameState;
import com.andernet.experiment.util.Constants;
import java.io.File;

/**
 * Handles settings button click events
 */
public class SettingsButtonClickHandler implements ActionListener {
    private final JFrame parentFrame;
    private final Settings settings;
    private final GameState gameState;
    private final File highScoreFile;
    private final JLabel timerLabel;
    private final JLabel scoreLabel;
    private final JLabel highScoreLabel;
    private final Runnable recreateFakeButtons;
    private final Timer moveTimer;
    
    public SettingsButtonClickHandler(JFrame parentFrame, Settings settings, 
                                    GameState gameState, File highScoreFile,
                                    JLabel timerLabel, JLabel scoreLabel, 
                                    JLabel highScoreLabel, Runnable recreateFakeButtons,
                                    Timer moveTimer) {
        this.parentFrame = parentFrame;
        this.settings = settings;
        this.gameState = gameState;
        this.highScoreFile = highScoreFile;
        this.timerLabel = timerLabel;
        this.scoreLabel = scoreLabel;
        this.highScoreLabel = highScoreLabel;
        this.recreateFakeButtons = recreateFakeButtons;
        this.moveTimer = moveTimer;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        SettingsDialog dialog = new SettingsDialog(parentFrame, settings);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            applyNewSettings();
        }
    }
    
    private void applyNewSettings() {
        // Create new game state with updated duration
        GameState newGameState = new GameState(settings.getGameDurationSeconds());
        newGameState.loadHighScore(highScoreFile);
        
        // Update game state reference (this would need to be handled by the caller)
        // For now, we'll update the current gameState
        gameState.reset(settings.getGameDurationSeconds());
        gameState.loadHighScore(highScoreFile);
        
        // Update UI labels
        updateLabels();
        
        // Recreate fake buttons with new settings
        recreateFakeButtons.run();
        
        // Update move timer interval (only if timer exists)
        if (moveTimer != null) {
            moveTimer.setDelay(settings.getMoveIntervalMs());
            moveTimer.setInitialDelay(settings.getMoveIntervalMs());
        }
        
        // Save settings to disk
        SettingsPersistence.save(settings);
        
        // Small delay for UI updates in test environments
        try { 
            Thread.sleep(100); 
        } catch (InterruptedException ignored) {}
    }
    
    private void updateLabels() {
        timerLabel.setText(Constants.TIME_PREFIX + settings.getGameDurationSeconds());
        scoreLabel.setText(Constants.SCORE_PREFIX + "0");
        highScoreLabel.setText(Constants.HIGH_SCORE_PREFIX + gameState.getHighScore());
        
        // Force UI update
        SwingUtilities.invokeLater(() -> {
            timerLabel.revalidate();
            timerLabel.repaint();
            scoreLabel.revalidate();
            scoreLabel.repaint();
            highScoreLabel.revalidate();
            highScoreLabel.repaint();
        });
    }
}
