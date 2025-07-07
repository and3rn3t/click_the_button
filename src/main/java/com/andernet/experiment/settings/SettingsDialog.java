package com.andernet.experiment.settings;

import javax.swing.*;
import java.awt.*;
import com.andernet.experiment.util.Constants;
import com.andernet.experiment.ui.ComponentFactory;
import com.andernet.experiment.ui.Theme;
import com.andernet.experiment.ui.ModernPanel;
import com.andernet.experiment.ui.ModernButton;

/**
 * Modern SettingsDialog with enhanced styling and user experience.
 */
public class SettingsDialog extends JDialog {
    private final Settings settings;
    private JSpinner durationSpinner;
    private JSpinner fakeButtonsSpinner;
    private JSpinner moveIntervalSpinner;
    private JCheckBox soundCheckBox;
    private JSpinner buttonWidthSpinner;
    private JSpinner buttonHeightSpinner;
    private boolean confirmed = false;

    public SettingsDialog(JFrame parent, Settings settings) {
        super(parent, Constants.SETTINGS_TITLE, true);
        this.settings = settings;
        initializeDialog();
        createComponents();
    }
    
    private void initializeDialog() {
        setLayout(new BorderLayout());
        setBackground(Theme.SURFACE_PRIMARY);
        setSize(450, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        // Modern styling
        getRootPane().setBorder(BorderFactory.createEmptyBorder(Theme.SPACING_LG, 
                                                                Theme.SPACING_LG, 
                                                                Theme.SPACING_LG, 
                                                                Theme.SPACING_LG));
    }
    
    private void createComponents() {
        // Main content panel
        ModernPanel mainPanel = new ModernPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackgroundColor(Theme.SURFACE_ELEVATED);
        mainPanel.setBorderRadius(Theme.BORDER_RADIUS_LG);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(Theme.SPACING_MD, Theme.SPACING_MD, Theme.SPACING_MD, Theme.SPACING_MD);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel(Constants.SETTINGS_TITLE);
        titleLabel.setFont(Theme.TITLE_MEDIUM_FONT);
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(Theme.SPACING_LG, Theme.SPACING_MD, Theme.SPACING_XL, Theme.SPACING_MD);
        mainPanel.add(titleLabel, gbc);
        
        // Reset for form fields
        gbc.gridwidth = 1;
        gbc.insets = new Insets(Theme.SPACING_MD, Theme.SPACING_MD, Theme.SPACING_MD, Theme.SPACING_MD);
        
        // Game Duration
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(createLabel(Constants.GAME_DURATION_LABEL), gbc);
        gbc.gridx = 1;
        durationSpinner = createModernSpinner(settings.getGameDurationSeconds(), 10, 120, 1);
        mainPanel.add(durationSpinner, gbc);
        
        // Fake Buttons
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(createLabel(Constants.FAKE_BUTTONS_LABEL), gbc);
        gbc.gridx = 1;
        fakeButtonsSpinner = createModernSpinner(settings.getNumFakeButtons(), 0, 10, 1);
        mainPanel.add(fakeButtonsSpinner, gbc);
        
        // Move Interval
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(createLabel(Constants.MOVE_INTERVAL_LABEL), gbc);
        gbc.gridx = 1;
        moveIntervalSpinner = createModernSpinner(settings.getMoveIntervalMs(), 200, 3000, 100);
        mainPanel.add(moveIntervalSpinner, gbc);
        
        // Sound Effects
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(createLabel(Constants.SOUND_EFFECTS_LABEL), gbc);
        gbc.gridx = 1;
        soundCheckBox = createModernCheckBox(Constants.ENABLED_LABEL, settings.isSoundEnabled());
        mainPanel.add(soundCheckBox, gbc);
        
        // Button Width
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(createLabel(Constants.MAIN_BUTTON_WIDTH_LABEL), gbc);
        gbc.gridx = 1;
        buttonWidthSpinner = createModernSpinner(settings.getMainButtonStartWidth(), 60, 300, 5);
        mainPanel.add(buttonWidthSpinner, gbc);
        
        // Button Height
        gbc.gridx = 0; gbc.gridy = 6;
        mainPanel.add(createLabel(Constants.MAIN_BUTTON_HEIGHT_LABEL), gbc);
        gbc.gridx = 1;
        buttonHeightSpinner = createModernSpinner(settings.getMainButtonStartHeight(), 30, 150, 5);
        mainPanel.add(buttonHeightSpinner, gbc);
        
        // Button Panel
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(Theme.SPACING_XL, Theme.SPACING_MD, Theme.SPACING_MD, Theme.SPACING_MD);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, Theme.SPACING_MD, 0));
        buttonPanel.setOpaque(false);
        JButton ok = ComponentFactory.createDialogButton(Constants.OK, "okButton");
        JButton cancel = ComponentFactory.createDialogButton(Constants.CANCEL, "cancelButton");
        buttonPanel.add(ok);
        buttonPanel.add(cancel);
        mainPanel.add(buttonPanel, gbc);
        
        // Add main panel to dialog
        add(mainPanel, BorderLayout.CENTER);
        
        ok.addActionListener(e -> {
            applySettings();
            confirmed = true;
            setVisible(false);
        });
        cancel.addActionListener(e -> setVisible(false));
        
        pack();
        setLocationRelativeTo(getParent());
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
