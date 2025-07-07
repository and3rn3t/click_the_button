package com.andernet.experiment.logic;

public class GameConstants {
    // Window dimensions
    public static final int WINDOW_WIDTH = 400;
    public static final int WINDOW_HEIGHT = 400;
    
    // Game defaults
    public static final int NUM_FAKE_BUTTONS = 2;
    public static final int GAME_TIME_SECONDS = 30;
    public static final int MOVE_INTERVAL_MS = 1000;
    
    // Button sizing
    public static final int MAIN_BUTTON_MIN_WIDTH = 40;
    public static final int MAIN_BUTTON_MIN_HEIGHT = 20;
    public static final int MAIN_BUTTON_START_WIDTH = 100;
    public static final int MAIN_BUTTON_START_HEIGHT = 50;
    
    // Layout constants
    public static final double LABEL_LEFT_MARGIN_RATIO = 0.025;
    public static final double LABEL_TOP_MARGIN_RATIO = 0.025;
    public static final double TIMER_LEFT_MARGIN_RATIO = 0.35;
    public static final double HIGHSCORE_LEFT_MARGIN_RATIO = 0.675;
    public static final int FAKE_BUTTON_MARGIN_TOP = 40;
    public static final int FAKE_BUTTON_MARGIN_BOTTOM = 60;
    
    // Animation and timing
    public static final int COUNTDOWN_TIMER_DELAY = 700;
    public static final int GAME_TIMER_DELAY = 1000;
    public static final int BUTTON_HIGHLIGHT_DURATION = 120;
    public static final int UI_FLUSH_DELAY = 30;
    public static final int FADE_ANIMATION_STEPS = 10;
    
    // Font sizing
    public static final int FONT_SIZE_DELTA = 2;
    public static final int MIN_FONT_SIZE = 10;
    
    // Round rectangle radius
    public static final int BUTTON_RADIUS = 30;
    public static final int OVERLAY_RADIUS = 40;
    
    // Penalties and scoring
    public static final int FAKE_BUTTON_PENALTY = 2;
    public static final int MAIN_BUTTON_SCORE = 1;
    public static final int ACHIEVEMENT_THRESHOLD = 20;
}
