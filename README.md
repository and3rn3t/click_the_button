# Click The Button Game

A modern, interactive Java Swing game where players must click a moving button as many times as possible before time runs out, while avoiding penalty buttons that subtract points.

## Overview

Click The Button Game is a fast-paced, skill-based game built with Java Swing that challenges players to click a moving target button while avoiding fake buttons that penalize the score. The game features smooth animations, sound effects, customizable settings, and accessibility features.

## Features

### Core Gameplay
- **Moving Target**: Main button moves automatically at configurable intervals
- **Fake Buttons**: Obstacle buttons that subtract points when clicked
- **Timer System**: Countdown timer with customizable duration (10-120 seconds)
- **Progressive Difficulty**: Button shrinks as score increases
- **High Score Tracking**: Persistent high score storage

### Visual & Audio
- **Smooth Animations**: Fade-in/out effects during button movement
- **Dynamic Colors**: Randomized pastel color themes
- **Gradient Backgrounds**: Modern UI with rounded corners
- **Sound Effects**: Configurable audio feedback for actions
- **Background Music**: Optional background music support
- **Floating Score**: Visual score feedback on button clicks

### Customization & Settings
- **Game Duration**: 10-120 seconds
- **Number of Fake Buttons**: 0-10 buttons
- **Button Move Interval**: 200-3000 milliseconds
- **Button Size**: Customizable width (60-300px) and height (30-150px)
- **Sound Toggle**: Enable/disable sound effects
- **Settings Persistence**: User preferences saved between sessions

### Accessibility & Controls
- **Keyboard Navigation**: Full keyboard support with Tab navigation
- **Font Size Adjustment**: Dynamic font scaling with +/- keys
- **Tooltips**: Helpful descriptions for all interactive elements
- **Focus Management**: Proper focus handling for screen readers
- **High Contrast**: Clear visual indicators and color choices

## System Requirements

- **Java**: JDK 21 or higher
- **Maven**: 3.6+ for building
- **OS**: Windows, macOS, or Linux with GUI support

## Installation & Setup

### Prerequisites
Ensure you have Java 21 and Maven installed:
```bash
java -version
mvn -version
```

### Build and Run
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd experiment
   ```

2. Build the project:
   ```bash
   mvn clean compile
   ```

3. Run the game:
   ```bash
   mvn exec:java -Dexec.mainClass="com.andernet.experiment.ClickTheButtonGame"
   ```

   Or using Maven Spring Boot plugin:
   ```bash
   mvn spring-boot:run
   ```

4. Run tests:
   ```bash
   mvn test
   ```

## How to Play

1. **Launch Game**: Run the application to open the settings dialog
2. **Configure Settings**: Adjust game duration, difficulty, and preferences
3. **Start Playing**: Click "Start Game" to begin the countdown
4. **Click the Blue Button**: Click the moving blue button to score points
5. **Avoid Red Buttons**: Don't click the red fake buttons (they subtract points)
6. **Beat the Clock**: Score as many points as possible before time runs out
7. **Set High Scores**: Try to beat your personal best!

### Scoring System
- **Main Button Click**: +1 point
- **Fake Button Click**: -1 point (minimum score is 0)
- **Level Progression**: Button shrinks as score increases
- **Achievements**: Special messages for high scores (20+ points)

## Controls

### Mouse Controls
- **Click**: Primary interaction method
- **Hover**: Visual feedback on interactive elements

### Keyboard Shortcuts
- **Enter**: Start game or resume from pause
- **Escape**: Quit game (with confirmation)
- **P**: Pause/Resume game
- **+/=**: Increase font size
- **-**: Decrease font size
- **Tab**: Navigate between UI elements
- **Space**: Activate focused buttons

### Game Controls
- **Mute Button (🔊/🔇)**: Toggle sound effects
- **Help Button (?)**: Show game instructions
- **Settings Button**: Open configuration dialog

## Technical Details

### Architecture
The project follows a modular architecture with separation of concerns:

```
com.andernet.experiment/
├── ClickTheButtonGame.java    # Main game window and orchestration
├── logic/                     # Game logic and state management
│   ├── GameState.java        # Score, time, and high score tracking
│   ├── ButtonManager.java    # Fake button management
│   └── GameConstants.java    # Game configuration constants
├── ui/                       # User interface components
│   ├── AnimatedButton.java   # Main game button with animations
│   ├── FakeButton.java       # Penalty buttons
│   ├── GameOverlayPanel.java # Start/pause/game over screens
│   ├── UIUtils.java          # UI utility functions
│   └── Theme.java            # Color and style definitions
├── settings/                 # Configuration management
│   ├── Settings.java         # Settings data model
│   ├── SettingsDialog.java   # Settings configuration UI
│   └── SettingsPersistence.java # Settings file I/O
└── util/                     # Utility classes
    ├── ResourceManager.java  # Sound effect management
    └── MusicManager.java     # Background music handling
```

### Key Technologies
- **Java Swing**: GUI framework
- **Spring Boot**: Application framework and dependency management
- **Maven**: Build automation and dependency management
- **JUnit 5**: Testing framework
- **Java AWT Robot**: Integration testing support

### Data Persistence
- **Settings**: Stored in `~/.ctb_settings` (Java Properties format)
- **High Score**: Stored in `~/.ctb_highscore` (plain text)
- **Cross-platform**: Uses user home directory for portability

## Configuration

### Default Settings
- **Game Duration**: 30 seconds
- **Fake Buttons**: 3 buttons
- **Move Interval**: 1000ms (1 second)
- **Sound**: Enabled
- **Button Size**: 140x60 pixels

### Customizable Options
All settings can be modified through the settings dialog:
- Game duration (10-120 seconds)
- Number of fake buttons (0-10)
- Button movement speed (200-3000ms intervals)
- Sound effects on/off
- Main button dimensions
- Settings persist between game sessions

## Testing

The project includes comprehensive test coverage:

### Test Categories
- **Unit Tests**: Individual component testing
- **Integration Tests**: Game flow and UI interaction testing
- **Settings Tests**: Configuration persistence and validation
- **Accessibility Tests**: Focus management and keyboard navigation

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ClickTheButtonGameFlowIntegrationTest

# Run with coverage
mvn test jacoco:report
```

## Development

### Project Structure
```
src/
├── main/java/               # Application source code
├── main/resources/          # Resources (audio, properties)
├── test/java/              # Test source code
└── test/resources/         # Test resources
```

### Building
```bash
# Clean build
mvn clean compile

# Package JAR
mvn package

# Skip tests (faster build)
mvn compile -DskipTests
```

### Code Style
- **Javadoc**: All public classes and methods documented
- **Exception Handling**: Graceful error handling throughout
- **Accessibility**: WCAG-compliant design patterns
- **Modular Design**: Clear separation of concerns

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes with tests
4. Ensure all tests pass
5. Submit a pull request

## License

This project is available under the MIT License. See the LICENSE file for details.

## Future Enhancements

- **Themes**: Multiple color themes and visual styles
- **Power-ups**: Special buttons with bonus effects
- **Multiplayer**: Local or network multiplayer support
- **Leaderboards**: Global high score tracking
- **Mobile Support**: Touch-friendly interface
- **More Achievements**: Extended achievement system
- **Custom Audio**: User-provided sound effects
- **Game Modes**: Different game variations and challenges

## Troubleshooting

### Common Issues
- **Java Version**: Ensure Java 21 or higher is installed
- **Audio Issues**: Check system audio settings and file permissions
- **Settings Not Saving**: Verify write permissions in user home directory
- **UI Scaling**: Use font size adjustment keys (+/-) for better visibility

### Debug Mode
Set system property for additional logging:
```bash
java -Dctb.testmode=true -jar target/experiment-0.0.1-SNAPSHOT.jar
```

---

*Click The Button Game - A fun, accessible, and challenging button-clicking experience!*
