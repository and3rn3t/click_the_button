package com.andernet.experiment.settings;

import javax.swing.*;
import java.awt.*;
import com.andernet.experiment.util.Constants;
import com.andernet.experiment.ui.ComponentFactory;

/**
 * SettingsDialog allows the user to customize gameplay options before starting the game.
 */
public class SettingsDialog extends JDialog {
    private final Settings settings;
    private final JSpinner durationSpinner;
    private final JSpinner fakeButtonsSpinner;
    private final JSpinner moveIntervalSpinner;
    private final JCheckBox soundCheckBox;
    private final JSpinner buttonWidthSpinner;
    private final JSpinner buttonHeightSpinner;
    private boolean confirmed = false;

    public SettingsDialog(JFrame parent, Settings settings) {
        super(parent, Constants.SETTINGS_TITLE, true);
        this.settings = settings;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        
        add(new JLabel(Constants.GAME_DURATION_LABEL), gbc);
        gbc.gridx = 1;
        durationSpinner = new JSpinner(new SpinnerNumberModel(settings.getGameDurationSeconds(), 10, 120, 1));
        add(durationSpinner, gbc);
        gbc.gridx = 0; gbc.gridy++;
        
        add(new JLabel(Constants.FAKE_BUTTONS_LABEL), gbc);
        gbc.gridx = 1;
        fakeButtonsSpinner = new JSpinner(new SpinnerNumberModel(settings.getNumFakeButtons(), 0, 10, 1));
        add(fakeButtonsSpinner, gbc);
        gbc.gridx = 0; gbc.gridy++;
        
        add(new JLabel(Constants.MOVE_INTERVAL_LABEL), gbc);
        gbc.gridx = 1;
        moveIntervalSpinner = new JSpinner(new SpinnerNumberModel(settings.getMoveIntervalMs(), 200, 3000, 100));
        add(moveIntervalSpinner, gbc);
        gbc.gridx = 0; gbc.gridy++;
        
        add(new JLabel(Constants.SOUND_EFFECTS_LABEL), gbc);
        gbc.gridx = 1;
        soundCheckBox = new JCheckBox(Constants.ENABLED_LABEL, settings.isSoundEnabled());
        add(soundCheckBox, gbc);
        gbc.gridx = 0; gbc.gridy++;
        
        add(new JLabel(Constants.MAIN_BUTTON_WIDTH_LABEL), gbc);
        gbc.gridx = 1;
        buttonWidthSpinner = new JSpinner(new SpinnerNumberModel(settings.getMainButtonStartWidth(), 60, 300, 5));
        add(buttonWidthSpinner, gbc);
        gbc.gridx = 0; gbc.gridy++;
        
        add(new JLabel(Constants.MAIN_BUTTON_HEIGHT_LABEL), gbc);
        gbc.gridx = 1;
        buttonHeightSpinner = new JSpinner(new SpinnerNumberModel(settings.getMainButtonStartHeight(), 30, 150, 5));
        add(buttonHeightSpinner, gbc);
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        
        JPanel buttonPanel = new JPanel();
        JButton ok = ComponentFactory.createDialogButton(Constants.OK, "okButton");
        JButton cancel = ComponentFactory.createDialogButton(Constants.CANCEL, "cancelButton");
        buttonPanel.add(ok);
        buttonPanel.add(cancel);
        add(buttonPanel, gbc);
        
        ok.addActionListener(e -> {
            applySettings();
            confirmed = true;
            setVisible(false);
        });
        cancel.addActionListener(e -> setVisible(false));
        pack();
        setLocationRelativeTo(parent);
    }

    private void applySettings() {
        settings.setGameDurationSeconds((Integer) durationSpinner.getValue());
        settings.setNumFakeButtons((Integer) fakeButtonsSpinner.getValue());
        settings.setMoveIntervalMs((Integer) moveIntervalSpinner.getValue());
        settings.setSoundEnabled(soundCheckBox.isSelected());
        settings.setMainButtonStartWidth((Integer) buttonWidthSpinner.getValue());
        settings.setMainButtonStartHeight((Integer) buttonHeightSpinner.getValue());
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
