package com.andernet.experiment.settings;

import javax.swing.*;
import java.awt.*;

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
        super(parent, "Game Settings", true);
        this.settings = settings;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Game Duration (sec):"), gbc);
        gbc.gridx = 1;
        durationSpinner = new JSpinner(new SpinnerNumberModel(settings.getGameDurationSeconds(), 10, 120, 1));
        add(durationSpinner, gbc);
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Fake Buttons:"), gbc);
        gbc.gridx = 1;
        fakeButtonsSpinner = new JSpinner(new SpinnerNumberModel(settings.getNumFakeButtons(), 0, 10, 1));
        add(fakeButtonsSpinner, gbc);
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Button Move Interval (ms):"), gbc);
        gbc.gridx = 1;
        moveIntervalSpinner = new JSpinner(new SpinnerNumberModel(settings.getMoveIntervalMs(), 200, 3000, 100));
        add(moveIntervalSpinner, gbc);
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Sound Effects:"), gbc);
        gbc.gridx = 1;
        soundCheckBox = new JCheckBox("Enabled", settings.isSoundEnabled());
        add(soundCheckBox, gbc);
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Main Button Width:"), gbc);
        gbc.gridx = 1;
        buttonWidthSpinner = new JSpinner(new SpinnerNumberModel(settings.getMainButtonStartWidth(), 60, 300, 5));
        add(buttonWidthSpinner, gbc);
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Main Button Height:"), gbc);
        gbc.gridx = 1;
        buttonHeightSpinner = new JSpinner(new SpinnerNumberModel(settings.getMainButtonStartHeight(), 30, 150, 5));
        add(buttonHeightSpinner, gbc);
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel();
        JButton ok = new JButton("OK");
        ok.setName("okButton");
        JButton cancel = new JButton("Cancel");
        cancel.setName("cancelButton");
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
        System.out.println("[DEBUG][SettingsDialog] applySettings: duration=" + settings.getGameDurationSeconds() + ", fakeButtons=" + settings.getNumFakeButtons() + ", moveInterval=" + settings.getMoveIntervalMs() + ", sound=" + settings.isSoundEnabled() + ", width=" + settings.getMainButtonStartWidth() + ", height=" + settings.getMainButtonStartHeight());
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
