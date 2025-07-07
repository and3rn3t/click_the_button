package com.andernet.experiment.settings;

import java.io.*;
import java.util.Properties;
import javax.swing.*;
import com.andernet.experiment.util.Constants;

/**
 * SettingsPersistence handles saving and loading user settings to a properties file.
 */
public class SettingsPersistence {
    private static final String SETTINGS_FILE = System.getProperty("user.home") + File.separator + Constants.SETTINGS_FILE;

    public static void save(Settings settings) {
        Properties props = new Properties();
        props.setProperty("gameDurationSeconds", String.valueOf(settings.getGameDurationSeconds()));
        props.setProperty("numFakeButtons", String.valueOf(settings.getNumFakeButtons()));
        props.setProperty("moveIntervalMs", String.valueOf(settings.getMoveIntervalMs()));
        props.setProperty("soundEnabled", String.valueOf(settings.isSoundEnabled()));
        props.setProperty("mainButtonStartWidth", String.valueOf(settings.getMainButtonStartWidth()));
        props.setProperty("mainButtonStartHeight", String.valueOf(settings.getMainButtonStartHeight()));
        
        try (FileOutputStream out = new FileOutputStream(SETTINGS_FILE)) {
            props.store(out, Constants.SETTINGS_COMMENT);
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, 
                Constants.SAVE_ERROR, Constants.FILE_ERROR_TITLE, JOptionPane.ERROR_MESSAGE));
        }
    }

    public static void load(Settings settings) {
        File file = new File(SETTINGS_FILE);
        if (!file.exists()) return;
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            props.load(in);
            if (props.getProperty("gameDurationSeconds") != null)
                settings.setGameDurationSeconds(Integer.parseInt(props.getProperty("gameDurationSeconds")));
            if (props.getProperty("numFakeButtons") != null)
                settings.setNumFakeButtons(Integer.parseInt(props.getProperty("numFakeButtons")));
            if (props.getProperty("moveIntervalMs") != null)
                settings.setMoveIntervalMs(Integer.parseInt(props.getProperty("moveIntervalMs")));
            if (props.getProperty("soundEnabled") != null)
                settings.setSoundEnabled(Boolean.parseBoolean(props.getProperty("soundEnabled")));
            if (props.getProperty("mainButtonStartWidth") != null)
                settings.setMainButtonStartWidth(Integer.parseInt(props.getProperty("mainButtonStartWidth")));
            if (props.getProperty("mainButtonStartHeight") != null)
                settings.setMainButtonStartHeight(Integer.parseInt(props.getProperty("mainButtonStartHeight")));
        } catch (IOException | NumberFormatException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, 
                Constants.LOAD_ERROR, Constants.FILE_ERROR_TITLE, JOptionPane.ERROR_MESSAGE));
        }
    }
}
