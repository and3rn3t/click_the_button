package com.andernet.experiment;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.io.File;
import com.andernet.experiment.ui.AnimatedButton;
import com.andernet.experiment.ui.FakeButton;
import com.andernet.experiment.ui.GameOverlayPanel;
import com.andernet.experiment.ui.UIUtils;
import com.andernet.experiment.ui.ComponentFactory;
import com.andernet.experiment.ui.ModernPanel;
import com.andernet.experiment.logic.GameConstants;
import com.andernet.experiment.logic.GameState;
import com.andernet.experiment.logic.ButtonManager;
import com.andernet.experiment.ui.Theme;
import com.andernet.experiment.util.ResourceManager;
import com.andernet.experiment.util.MusicManager;
import com.andernet.experiment.util.Constants;
import com.andernet.experiment.settings.Settings;
import com.andernet.experiment.settings.SettingsDialog;
import com.andernet.experiment.settings.SettingsPersistence;
import com.andernet.experiment.util.AnimationManager;
import com.andernet.experiment.handlers.MainButtonClickHandler;
import com.andernet.experiment.handlers.SettingsButtonClickHandler;
import java.awt.event.KeyEvent;

/**
 * ClickTheButtonGame is a modern, graphical Java Swing game where the player
 * must click the moving main button as many times as possible before time runs
 * out.
 * Features include:
 * <ul>
 * <li>Animated button movement and fade effects</li>
 * <li>Timer and scoring system with high score tracking</li>
 * <li>Randomized pastel color themes</li>
 * <li>Fake (obstacle) buttons that penalize the score</li>
 * <li>Sound effects for actions and game over</li>
 * <li>Overlay screens for start and game over</li>
 * <li>Level progression (button shrinks as score increases)</li>
 * </ul>
 *
 * The code is modularized for maintainability, with UI and logic components
 * separated into sub-packages.
 */
public class ClickTheButtonGame extends JFrame {
    // Main animated button the player must click
    private AnimatedButton button;
    // Displays current score
    private JLabel scoreLabel;
    // Displays remaining time
    private JLabel timerLabel;
    // Displays high score
    private JLabel highScoreLabel;
    private GameState gameState;
    private boolean fontAdjustmentInProgress = false;
    private ButtonManager buttonManager;
    private Random random = new Random();
    // Timer for game countdown
    private Timer gameTimer;
    // Timer for moving buttons automatically
    private Timer moveTimer;
    // Overlay panel for start/game over screens
    private GameOverlayPanel overlayPanel;
    // Settings is always set in constructor, so no need for initializer
    private Settings settings;

    // Event handlers
    private MainButtonClickHandler mainButtonClickHandler;
    private SettingsButtonClickHandler settingsButtonClickHandler;

    // High score file path (single source of truth)
    private static final File HIGH_SCORE_FILE = new File(System.getProperty("user.home"), ".ctb_highscore");

    /**
     * Constructs the game window and initializes all UI components and game state.
     */
    public ClickTheButtonGame(Settings settings) {
        this.settings = settings;
        initializeWindow();
        initializeGameState();
        createUIComponents();
        setupKeyboardShortcuts();
        setupEventHandlers();
        finalizeInitialization();
    }
    
    /**
     * Initialize basic window properties
     */
    private void initializeWindow() {
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        setTitle(Constants.APP_TITLE);
        setSize(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        setBackground(new Color(0, 0, 0, 0));
        setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Modern gradient background
                GradientPaint gp = new GradientPaint(0, 0, Theme.BACKGROUND_GRADIENT_TOP, 
                                                    0, getHeight(), Theme.BACKGROUND_GRADIENT_BOTTOM);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Add subtle texture overlay
                g2.setColor(new Color(255, 255, 255, 10));
                for (int x = 0; x < getWidth(); x += 60) {
                    for (int y = 0; y < getHeight(); y += 60) {
                        g2.fillOval(x, y, 2, 2);
                    }
                }
                
                g2.dispose();
            }
        });
        getContentPane().setLayout(null);
        setResizable(true);
    }
    
    /**
     * Initialize game state and ensure valid settings
     */
    private void initializeGameState() {
        // Ensure minimum duration for test compatibility
        if (settings.getGameDurationSeconds() < 10) {
            settings.setGameDurationSeconds(10);
        }
        gameState = new GameState(settings.getGameDurationSeconds());
        gameState.loadHighScore(HIGH_SCORE_FILE);
    }
    
    /**
     * Initialize event handlers
     */
    private void initializeEventHandlers() {
        mainButtonClickHandler = new MainButtonClickHandler(
            gameState, settings, scoreLabel, highScoreLabel, button,
            this::moveAllButtons, this::randomizeColors, this::nextLevel
        );
        
        settingsButtonClickHandler = new SettingsButtonClickHandler(
            this, settings, gameState, HIGH_SCORE_FILE,
            timerLabel, scoreLabel, highScoreLabel,
            () -> buttonManager.createFakeButtons(),
            moveTimer
        );
        
        // Set the event handlers on the components
        button.addActionListener(mainButtonClickHandler);
        overlayPanel.getSettingsButton().addActionListener(settingsButtonClickHandler);
    }
    
    /**
     * Create all UI components
     */
    private void createUIComponents() {
        createLabels();
        createMainButton();
        createFakeButtons();
        createOverlayPanel();
        createControlButtons();
        
        // Initialize event handlers after UI components are created
        initializeEventHandlers();
    }
    
    /**
     * Create score, timer, and high score labels with modern styling
     */
    private void createLabels() {
        Font labelFont = Theme.LABEL_FONT;
        
        // Create modern info panels instead of basic labels
        ModernPanel scorePanel = UIUtils.createInfoPanel(Constants.SCORE_PREFIX + "0", 10, 10, 120, 35, labelFont);
        scorePanel.setName("scorePanel");
        scorePanel.setToolTipText(Constants.SCORE_TOOLTIP);
        add(scorePanel);
        
        // Extract the label from the panel for score updates
        scoreLabel = (JLabel) scorePanel.getComponent(0);
        
        ModernPanel timerPanel = UIUtils.createInfoPanel(Constants.TIME_PREFIX + settings.getGameDurationSeconds(), 140, 10, 120, 35, labelFont);
        timerPanel.setName("timerPanel");
        timerPanel.setToolTipText(Constants.TIMER_TOOLTIP);
        add(timerPanel);
        
        // Extract the label from the panel for timer updates
        timerLabel = (JLabel) timerPanel.getComponent(0);
        
        ModernPanel highScorePanel = UIUtils.createInfoPanel(Constants.HIGH_SCORE_PREFIX + gameState.getHighScore(), 270, 10, 150, 35, labelFont);
        highScorePanel.setName("highScorePanel");
        highScorePanel.setToolTipText(Constants.HIGH_SCORE_TOOLTIP);
        add(highScorePanel);
        
        // Extract the label from the panel for high score updates
        highScoreLabel = (JLabel) highScorePanel.getComponent(0);
    }
    
    /**
     * Create and configure the main game button
     */
    private void createMainButton() {
        button = ComponentFactory.createMainButton(Constants.CLICK_ME, 
            settings.getMainButtonStartWidth(), settings.getMainButtonStartHeight());
        
        // Add hover effect
        ComponentFactory.addHoverEffect(button, Theme.MAIN_BUTTON_COLOR);
        add(button);
    }
    
    /**
     * Create fake buttons using ButtonManager
     */
    private void createFakeButtons() {
        buttonManager = new ButtonManager(settings, gameState, scoreLabel, this::moveAllButtons, this::randomizeColors,
                (JPanel) getContentPane());
        buttonManager.createFakeButtons();
    }
    
    /**
     * Create overlay panel for start/game over screens
     */
    private void createOverlayPanel() {
        overlayPanel = new GameOverlayPanel(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        overlayPanel.getOverlayButton().setName("overlayButton");
        overlayPanel.getSettingsButton().setName("settingsButton");
        overlayPanel.getOverlayButton().addActionListener(e -> {
            hideOverlay();
            startGame();
        });
        
        // Settings button handler will be set after initialization
        overlayPanel.setVisible(true);
        getLayeredPane().add(overlayPanel, JLayeredPane.POPUP_LAYER);
    }
    
    /**
     * Create control buttons (mute, help)
     */
    private void createControlButtons() {
        createMuteButton();
        createHelpButton();
    }
    
    /**
     * Create mute/unmute button
     */
    private void createMuteButton() {
        JButton muteButton = ComponentFactory.createMuteButton(settings.isSoundEnabled());
        muteButton.addActionListener(e -> {
            settings.setSoundEnabled(!settings.isSoundEnabled());
            muteButton.setText(settings.isSoundEnabled() ? Constants.SOUND_ON : Constants.SOUND_OFF);
            if (settings.isSoundEnabled()) {
                MusicManager.playBackgroundMusic(Constants.BACKGROUND_MUSIC, true);
            } else {
                MusicManager.stopBackgroundMusic();
            }
        });
        add(muteButton);
    }
    
    /**
     * Create help button
     */
    private void createHelpButton() {
        JButton helpButton = ComponentFactory.createHelpButton();
        helpButton.setBounds(10, 10, 40, 36);
        add(helpButton);
        helpButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, Constants.HELP_MESSAGE, Constants.HELP_BUTTON_TOOLTIP, JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    /**
     * Setup all keyboard shortcuts
     */
    private void setupKeyboardShortcuts() {
        setupFontSizeAdjustment();
        setupOverlayShortcuts();
        setupPauseResume();
    }
    
    /**
     * Setup font size adjustment shortcuts
     */
    private void setupFontSizeAdjustment() {
        getRootPane().registerKeyboardAction(e -> adjustFontSize(GameConstants.FONT_SIZE_DELTA), 
            KeyStroke.getKeyStroke('+'), JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> adjustFontSize(GameConstants.FONT_SIZE_DELTA), 
            KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.SHIFT_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> adjustFontSize(-GameConstants.FONT_SIZE_DELTA), 
            KeyStroke.getKeyStroke('-'), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
    
    /**
     * Setup overlay keyboard shortcuts
     */
    private void setupOverlayShortcuts() {
        overlayPanel.getOverlayButton().setMnemonic('S');
        overlayPanel.getSettingsButton().setMnemonic('E');
        getRootPane().setDefaultButton(overlayPanel.getOverlayButton());
        
        getRootPane().registerKeyboardAction(e -> {
            if (overlayPanel.isVisible()) {
                hideOverlay();
                startGame();
            }
        }, KeyStroke.getKeyStroke("ENTER"), JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        getRootPane().registerKeyboardAction(e -> {
            if (overlayPanel.isVisible()) {
                int result = JOptionPane.showConfirmDialog(this, Constants.QUIT_CONFIRMATION, Constants.QUIT_TITLE, JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        }, KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
    
    /**
     * Setup pause/resume functionality
     */
    private void setupPauseResume() {
        getRootPane().registerKeyboardAction(e -> {
            if (gameTimer != null && gameTimer.isRunning()) {
                gameTimer.stop();
                moveTimer.stop();
                showOverlay(Constants.PAUSED, Constants.RESUME, false);
            } else if (overlayPanel.isVisible() && overlayPanel.getOverlayButton().getText().equals(Constants.RESUME)) {
                hideOverlay();
                gameTimer.start();
                moveTimer.start();
            }
        }, KeyStroke.getKeyStroke('P'), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
    
    /**
     * Setup event handlers and tooltips
     */
    private void setupEventHandlers() {
        setupTooltips();
        setupResizeHandler();
    }
    
    /**
     * Setup tooltips for accessibility
     */
    private void setupTooltips() {
        for (FakeButton fake : buttonManager.getFakeButtons()) {
            fake.setToolTipText(Constants.FAKE_BUTTON_TOOLTIP);
        }
        overlayPanel.getOverlayButton().setToolTipText(Constants.START_BUTTON_TOOLTIP);
        overlayPanel.getSettingsButton().setToolTipText(Constants.SETTINGS_BUTTON_TOOLTIP);
    }
    
    /**
     * Setup window resize handler
     */
    private void setupResizeHandler() {
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                handleWindowResize();
            }
        });
    }
    
    /**
     * Handles window resize events by repositioning components
     */
    private void handleWindowResize() {
        int w = getWidth();
        int h = getHeight();
        
        // Reposition labels
        UIUtils.positionResponsively(scoreLabel, GameConstants.LABEL_LEFT_MARGIN_RATIO, GameConstants.LABEL_TOP_MARGIN_RATIO, w, h);
        UIUtils.positionResponsively(timerLabel, GameConstants.TIMER_LEFT_MARGIN_RATIO, GameConstants.LABEL_TOP_MARGIN_RATIO, w, h);
        UIUtils.positionResponsively(highScoreLabel, GameConstants.HIGHSCORE_LEFT_MARGIN_RATIO, GameConstants.LABEL_TOP_MARGIN_RATIO, w, h);
        
        // Reposition overlay panel
        overlayPanel.setBounds(0, 0, w, h);
        
        // Reposition main button proportionally
        button.setLocation((int) (w * GameConstants.MAIN_BUTTON_CENTER_X_RATIO - button.getWidth() / 2), 
                         (int) (h * GameConstants.MAIN_BUTTON_CENTER_Y_RATIO - button.getHeight() / 2));
        
        // Reposition fake buttons randomly within new bounds
        repositionFakeButtons(w, h);
    }
    
    /**
     * Repositions fake buttons within window bounds
     */
    private void repositionFakeButtons(int windowWidth, int windowHeight) {
        for (FakeButton fake : buttonManager.getFakeButtons()) {
            int fx = (int) (Math.random() * (windowWidth - fake.getWidth()));
            int fy = (int) (Math.random() * (windowHeight - fake.getHeight() - GameConstants.FAKE_BUTTON_MARGIN_BOTTOM) + GameConstants.FAKE_BUTTON_MARGIN_TOP);
            fake.setLocation(fx, fy);
        }
    }
    
    /**
     * Finalize initialization
     */
    private void finalizeInitialization() {
        // Ensure game UI is hidden until game starts
        setGameUIVisible(false);
        showOverlay(Constants.APP_TITLE, Constants.START_GAME, false);

        // Start the button move timer
        moveTimer = new Timer(settings.getMoveIntervalMs(), e -> moveAllButtons());
        moveTimer.setInitialDelay(settings.getMoveIntervalMs());
        moveTimer.start();

        // Start background music if sound is enabled
        if (settings.isSoundEnabled()) {
            MusicManager.playBackgroundMusic(Constants.BACKGROUND_MUSIC, true);
        }
    }

    /**
     * Moves the main and fake buttons with fade animation.
     */
    // Helper to move and randomize all buttons
    private void moveAllButtons() {
        AnimationManager.fadeAndMoveButton(button, null);
        buttonManager.moveFakeButtons();
    }

    /**
     * Shows the overlay panel with a message and button.
     * 
     * @param message    The message to display
     * @param buttonText The text for the overlay button
     * @param showScore  Whether to show the score (unused, for future use)
     */
    private void showOverlay(String message, String buttonText, boolean showScore) {
        overlayPanel.getOverlayLabel().setText("<html><div style='text-align:center;'>" + message + "</div></html>");
        overlayPanel.getOverlayButton().setText(buttonText);
        overlayPanel.getOverlayButton().setVisible(true);
        overlayPanel.getOverlayButton().setEnabled(true);
        overlayPanel.getOverlayButton().setFocusable(true);
        overlayPanel.setVisible(true);
        overlayPanel.setFocusable(true);
        overlayPanel.requestFocusInWindow();
        overlayPanel.requestFocus();
        overlayPanel.revalidate();
        overlayPanel.repaint();
        getLayeredPane().revalidate();
        getLayeredPane().repaint();
        this.revalidate();
        this.repaint();
        Toolkit.getDefaultToolkit().sync();
        try {
            Thread.sleep(60); // Give the UI thread a moment to process
        } catch (InterruptedException ignored) {}
        setGameUIVisible(false);
        // Accessibility: focus overlay panel and then overlay button robustly
        JButton overlayBtn = overlayPanel.getOverlayButton();
        overlayBtn.setFocusable(true);
        overlayBtn.setRequestFocusEnabled(true);
        overlayBtn.setEnabled(true);
        overlayPanel.setFocusable(true);
        // Try to request focus multiple times with increasing delays
        for (int delay : new int[]{0, 100, 250, 500}) {
            new Timer(delay, ev -> {
                overlayPanel.requestFocusInWindow();
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
                overlayBtn.requestFocusInWindow();
                overlayBtn.requestFocus();
                overlayBtn.grabFocus();
                ((Timer)ev.getSource()).stop();
            }).start();
        }
    }

    /**
     * Hides the overlay panel and shows the main game UI.
     */
    private void hideOverlay() {
        overlayPanel.setVisible(false);
        overlayPanel.revalidate();
        overlayPanel.repaint();
        Toolkit.getDefaultToolkit().sync();
        try { Thread.sleep(50); } catch (InterruptedException ignored) {}
        setGameUIVisible(true);
    }

    /**
     * Sets the visibility of all main game UI components.
     * 
     * @param visible true to show, false to hide
     */
    private void setGameUIVisible(boolean visible) {
        scoreLabel.setVisible(visible);
        timerLabel.setVisible(visible);
        highScoreLabel.setVisible(visible);
        button.setVisible(visible);
        for (FakeButton fake : buttonManager.getFakeButtons())
            fake.setVisible(visible);
    }

    /**
     * Starts or restarts the game, resetting state and timers.
     */
    private void startGame() {
        if (settings.isSoundEnabled()) {
            MusicManager.playBackgroundMusic("/audio/background.wav", true);
        }
        // Countdown before game starts
        setGameUIVisible(false);
        overlayPanel.getOverlayButton().setVisible(false);
        overlayPanel.setVisible(true);
        
        // Use AnimationManager for countdown
        AnimationManager.animateCountdown(overlayPanel.getOverlayLabel(), () -> {
            // Game starts after countdown
            overlayPanel.setVisible(false);
            setGameUIVisible(true);
            gameState.reset(settings.getGameDurationSeconds());
            gameState.loadHighScore(HIGH_SCORE_FILE);
            scoreLabel.setText(Constants.SCORE_PREFIX + "0");
            timerLabel.setText(Constants.TIME_PREFIX + gameState.getTimeLeft());
            highScoreLabel.setText(Constants.HIGH_SCORE_PREFIX + gameState.getHighScore());
            button.setEnabled(true);
            for (FakeButton fake : buttonManager.getFakeButtons())
                fake.setEnabled(true);
            gameTimer = new Timer(1000, ev -> {
                gameState.decrementTime();
                timerLabel.setText(Constants.TIME_PREFIX + gameState.getTimeLeft());
                if (gameState.getTimeLeft() <= 0) {
                    endGame();
                }
            });
            gameTimer.start();
            moveTimer.setDelay(settings.getMoveIntervalMs());
            moveTimer.restart();
        });
    }

    /**
     * Ends the game, disables input, and shows the game over overlay.
     */
    private void endGame() {
        MusicManager.stopBackgroundMusic();
        if (settings.isSoundEnabled()) ResourceManager.playEndBeep();
        
        // Update score label to show final score
        scoreLabel.setText(Constants.SCORE_PREFIX + gameState.getScore());
        
        // Save high score after game ends
        gameState.saveHighScore(HIGH_SCORE_FILE);
        // Immediately reload and update label to ensure persistence in test
        gameState.loadHighScore(HIGH_SCORE_FILE);
        highScoreLabel.setText(Constants.HIGH_SCORE_PREFIX + gameState.getHighScore());
        
        // Show summary screen with stats and achievements (if any)
        StringBuilder summary = new StringBuilder();
        summary.append("Game Over!<br>Your score: ").append(gameState.getScore());
        summary.append("<br>High score: ").append(gameState.getHighScore());
        // Example achievement: 20+ points
        if (gameState.getScore() >= 20) {
            summary.append("<br><b>Achievement: Quick Clicker!</b>");
        }
        showOverlay(summary.toString(), "Play Again", true);
    }

    /**
     * Advances the level by shrinking the main button, but not below its text size.
     */
    private void nextLevel() {
        Dimension minSize = button.getPreferredSize();
        int minWidth = (int) minSize.getWidth();
        int minHeight = (int) minSize.getHeight();
        int newWidth = Math.max(minWidth, GameConstants.MAIN_BUTTON_START_WIDTH - gameState.getScore() * 2);
        int newHeight = Math.max(minHeight, GameConstants.MAIN_BUTTON_START_HEIGHT - gameState.getScore());
        button.setSize(newWidth, newHeight);
    }

    /**
     * Randomizes the background and button colors using pastel shades.
     * Allows for future theme support.
     */
    private void randomizeColors() {
        Color bg = UIUtils.getRandomPastelColor(random);
        Color btn = UIUtils.getRandomPastelColor(random).darker();
        getContentPane().setBackground(bg);
        button.setBackground(btn);
        for (FakeButton fake : buttonManager.getFakeButtons()) {
            fake.setBackground(UIUtils.getRandomPastelColor(random).darker());
        }
    }

    /**
     * Main entry point. Launches the game window.
     */
    public static void main(String[] args) {
        // Global exception handler for uncaught exceptions
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null,
                "An unexpected error occurred:\n" + e.toString(),
                "Unexpected Error", JOptionPane.ERROR_MESSAGE));
        });
        SwingUtilities.invokeLater(() -> {
            // Load settings from disk
            Settings settings = new Settings();
            SettingsPersistence.load(settings);
            JFrame dummy = new JFrame(); // Temporary invisible frame for dialog parenting
            SettingsDialog dialog = new SettingsDialog(dummy, settings);
            dialog.setVisible(true);
            dummy.dispose();
            if (!dialog.isConfirmed()) {
                System.exit(0);
            }
            // Save settings after dialog
            SettingsPersistence.save(settings);
            ClickTheButtonGame game = new ClickTheButtonGame(settings);
            game.setVisible(true);
        });
    }

    // Add this method to the class:
    private void adjustFontSize(int delta) {
        if (fontAdjustmentInProgress) {
            return;
        }
        fontAdjustmentInProgress = true;
        
        try {
            // Make sure we're on the EDT
            if (!SwingUtilities.isEventDispatchThread()) {
                SwingUtilities.invokeLater(() -> adjustFontSize(delta));
                return;
            }
            
            // Update all components recursively
            adjustFontSizeRecursive(getContentPane(), delta);
            
            // Force repaint
            revalidate();
            repaint();
        } finally {
            fontAdjustmentInProgress = false;
        }
    }
    
    private void adjustFontSizeRecursive(Container container, int delta) {
        for (Component c : container.getComponents()) {
            if (c instanceof JComponent) {
                Font f = c.getFont();
                if (f != null) {
                    Font newFont = f.deriveFont((float)Math.max(10, f.getSize() + delta));
                    c.setFont(newFont);
                    flushComponentUI((JComponent)c);
                }
            }
            // Recursively handle containers
            if (c instanceof Container) {
                adjustFontSizeRecursive((Container)c, delta);
            }
        }
    }

    // Utility to robustly update and flush UI changes for a component
    private static void flushComponentUI(JComponent comp) {
        try {
            if (!SwingUtilities.isEventDispatchThread()) {
                SwingUtilities.invokeAndWait(() -> flushComponentUI(comp));
                return;
            }
            comp.revalidate();
            comp.repaint();
            comp.updateUI();
            // Also update parent if possible
            if (comp.getParent() != null && comp.getParent() instanceof JComponent) {
                ((JComponent)comp.getParent()).revalidate();
                ((JComponent)comp.getParent()).repaint();
            }
            Toolkit.getDefaultToolkit().sync();
            // Give the event queue a moment to process
            try { Thread.sleep(30); } catch (InterruptedException ignored) {}
        } catch (Exception e) {
            // Ignore UI flush errors
        }
    }
}
