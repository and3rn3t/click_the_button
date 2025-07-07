package com.andernet.experiment.util;

/**
 * Contains string constants used throughout the application
 */
public class Constants {
    
    // Application strings
    public static final String APP_TITLE = "Click the Button Game";
    public static final String SETTINGS_TITLE = "Game Settings";
    
    // Button text
    public static final String START_GAME = "Start Game";
    public static final String PLAY_AGAIN = "Play Again";
    public static final String RESUME = "Resume";
    public static final String PAUSED = "Paused";
    public static final String SETTINGS = "Settings";
    public static final String OK = "OK";
    public static final String CANCEL = "Cancel";
    public static final String CLICK_ME = "Click me!";
    public static final String FAKE = "Fake!";
    public static final String HELP = "?";
    
    // Labels and messages
    public static final String SCORE_PREFIX = "Score: ";
    public static final String TIME_PREFIX = "Time: ";
    public static final String HIGH_SCORE_PREFIX = "High Score: ";
    public static final String GAME_OVER = "Game Over!";
    public static final String YOUR_SCORE = "Your score: ";
    public static final String ACHIEVEMENT_QUICK_CLICKER = "Achievement: Quick Clicker!";
    
    // Settings labels
    public static final String GAME_DURATION_LABEL = "Game Duration (sec):";
    public static final String FAKE_BUTTONS_LABEL = "Fake Buttons:";
    public static final String MOVE_INTERVAL_LABEL = "Button Move Interval (ms):";
    public static final String SOUND_EFFECTS_LABEL = "Sound Effects:";
    public static final String ENABLED_LABEL = "Enabled";
    public static final String MAIN_BUTTON_WIDTH_LABEL = "Main Button Width:";
    public static final String MAIN_BUTTON_HEIGHT_LABEL = "Main Button Height:";
    
    // Tooltips
    public static final String SCORE_TOOLTIP = "Your current score";
    public static final String TIMER_TOOLTIP = "Time left in the round";
    public static final String HIGH_SCORE_TOOLTIP = "Your all-time high score";
    public static final String MAIN_BUTTON_TOOLTIP = "Click me to score points!";
    public static final String FAKE_BUTTON_TOOLTIP = "Don't click! These are fake buttons.";
    public static final String START_BUTTON_TOOLTIP = "Start or restart the game";
    public static final String SETTINGS_BUTTON_TOOLTIP = "Change game settings";
    public static final String MUTE_BUTTON_TOOLTIP = "Toggle sound effects";
    public static final String HELP_BUTTON_TOOLTIP = "How to play";
    
    // Help text
    public static final String INSTRUCTIONS = "<html><div style='text-align:center;'>Click the blue button as many times as you can in 30 seconds!<br>Avoid the red fake buttons.</div></html>";
    public static final String HELP_MESSAGE = 
        "Click the blue button as many times as you can before time runs out!\n" +
        "Avoid the fake buttons—they subtract points.\n" +
        "You can change settings or mute sound using the buttons above.\n\n" +
        "Keyboard Shortcuts:\n" +
        "  Enter: Start/Resume\n  Esc: Quit\n  P: Pause/Resume\n  +/-: Adjust font size\n  Tab: Navigate\n";
    
    // Sound emoji
    public static final String SOUND_ON = "🔊";
    public static final String SOUND_OFF = "🔇";
    
    // Error messages
    public static final String SAVE_ERROR = "Could not save settings.";
    public static final String LOAD_ERROR = "Could not load settings.";
    public static final String FILE_ERROR_TITLE = "File Error";
    public static final String UNEXPECTED_ERROR = "An unexpected error occurred:\n";
    public static final String UNEXPECTED_ERROR_TITLE = "Unexpected Error";
    public static final String QUIT_CONFIRMATION = "Are you sure you want to quit?";
    public static final String QUIT_TITLE = "Quit";
    
    // File names
    public static final String HIGH_SCORE_FILE = ".ctb_highscore";
    public static final String SETTINGS_FILE = ".ctb_settings";
    public static final String SETTINGS_COMMENT = "ClickTheButtonGame User Settings";
    
    // Audio files
    public static final String CLICK_SOUND = "/audio/click.wav";
    public static final String FAKE_SOUND = "/audio/fake.wav";
    public static final String GAMEOVER_SOUND = "/audio/gameover.wav";
    public static final String BACKGROUND_MUSIC = "/audio/background.wav";
    
    // System properties
    public static final String TEST_MODE_PROPERTY = "ctb.testmode";
}
