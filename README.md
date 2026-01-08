# Java Board Game Engine - Tic-Tac-Toe

A Tic-Tac-Toe game implementation in Java with both command-line and graphical user interfaces, demonstrating clean object-oriented design principles and separation of concerns. This project showcases how game logic can be completely decoupled from the user interface layer, making the core engine reusable across different interfaces.

## Overview

This project implements a complete Tic-Tac-Toe game with two modes: player versus player and player versus computer. The computer player uses the Minimax algorithm to play optimally. The architecture is designed with a clear separation between the game logic layer (Board, GameEngine, Player) and the presentation layer (TicTacToeCLI, GameGUI), allowing the core engine to be reused with different interfaces.

## How to Compile and Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher (tested with Java 21 LTS)
- JUnit 4 (for running tests)

### Compilation

Compile all source files:

```bash
javac -d out src/main/java/tictactoe/*.java
```

Or if using a build directory structure:

```bash
mkdir -p out
javac -d out -sourcepath src/main/java src/main/java/tictactoe/*.java
```

### Running the Game

Run the main class:

```bash
java -cp out tictactoe.TicTacToeCLI
```

The game will prompt you to select a game mode:
- Option 1: Player vs Player
- Option 2: Player vs Computer

During gameplay, enter moves as two numbers separated by a space (e.g., "1 2" for row 1, column 2). Rows and columns are numbered 0-2.

### GUI Mode

The game includes a professional graphical user interface built with Java Swing, featuring a modern pink and purple colour scheme. To run the GUI version:

```bash
java -cp out tictactoe.GameGUI
```

#### Main Menu

The GUI opens with a main menu screen where you can:

- Select game mode: "Player vs Player" or "Player vs Computer"
- Enter player names using placeholder text fields that clear automatically when clicked
- Select difficulty level (when playing against Computer): Easy, Medium, or Hard
- Start the game with the "Start Game" button

#### Game Modes

**Player vs Player**: Two human players take turns. Both players enter their names, and the game alternates between them.

**Player vs Computer**: Play against an AI opponent with three difficulty levels:
- **Easy**: Computer makes random valid moves with no strategy - perfect for beginners
- **Medium**: Computer uses optimal moves 75% of the time, providing a genuine challenge that wins against casual players most of the time
- **Hard**: Computer uses the full Minimax algorithm with alpha-beta pruning for unbeatable optimal play - extremely challenging and near-impossible to defeat

#### GUI Features

- Professional dark theme with vibrant pink and purple colour palette
- Modern, rounded button designs with smooth hover effects
- Card layout system for seamless navigation between menu and game screens
- 600x700 pixel window with clean, centered layout
- Visual distinction: X marks in hot pink, O marks in cyan
- Real-time status display showing current player's turn
- Score tracking across multiple games (wins and draws for each player)
- Difficulty indicator displayed when playing against Computer
- Winning line highlighting with pink glow effect when game ends
- "New Game" button to reset and play again
- "Back to Menu" button to return to the main menu
- Smooth animations and visual feedback
- Proper threading: AI moves computed in background using SwingWorker to prevent UI freezing
- Decorative graphics: Randomised pixel art style decorations including red roses, yellow sunflowers, white lilies, pink and purple flowers, multi-coloured stars, grass tufts, and sparkles drawn using Graphics2D with fillRect for a retro, nostalgic aesthetic. Graphics are randomly positioned and sized for an organic, lively feel without symmetrical repetition.
- Pixel art typography: Monospaced and geometric fonts that complement the pixel art style, with bold pixel-style fonts for headings and titles

#### Design Philosophy and Aesthetic

The GUI maintains the same clean architecture as the CLI version - all game logic remains in the GameEngine, with the GUI acting purely as a visual presentation layer. The MenuPanel and GamePanel classes handle display and user input, while game decisions are delegated to the engine. Difficulty levels are implemented in the ComputerPlayer class within the game engine layer, maintaining proper separation of concerns.

The visual design features a retro pixel art aesthetic. The interface includes:
- A professional dark theme with vibrant pink and purple colour palette
- Randomised pixel art decorative graphics including red roses with green stems, yellow sunflowers with brown centres, white lilies, pink and purple flowers, multi-coloured stars (yellow, pink, cyan, white), grass tufts, and sparkles. All graphics are drawn using Java Graphics2D with fillRect for a nostalgic, 8-bit inspired feel. Decorations are randomly positioned and sized on both menu and game screens for variety and an organic, playful appearance.
- Pixel-style fonts (Monospaced, Courier New) that complement the retro gaming aesthetic, with bold pixel fonts for headings
- Smooth animations and visual feedback for an engaging user experience
- Clean, modern layout with thoughtful spacing and rounded elements

You can switch between CLI and GUI modes without any changes to the core game logic classes.

### Running Tests

To compile and run tests (requires JUnit 4 on classpath):

```bash
# Compile test files (adjust classpath to include JUnit jar)
javac -d out -cp "out:junit-4.13.2.jar:hamcrest-core-1.3.jar" src/test/java/tictactoe/*.java src/main/java/tictactoe/*.java

# Run tests
java -cp "out:junit-4.13.2.jar:hamcrest-core-1.3.jar" org.junit.runner.JUnitCore tictactoe.BoardTest tictactoe.GameEngineTest
```

Note: You'll need to download JUnit 4 and Hamcrest Core JAR files and adjust the classpath accordingly.

## Architecture

### Design Philosophy

The core principle behind this design is **separation of concerns**. The game logic is completely independent of any user interface implementation. This means:

- The `Board`, `GameEngine`, and `Player` classes contain no references to `System.out`, `Scanner`, Swing components, or any I/O operations
- The presentation layers (`TicTacToeCLI` and `GameGUI`) are thin wrappers that handle user input and output, delegating all game decisions to the engine
- The engine is used by both the CLI and GUI interfaces, demonstrating the reusability of the architecture

This separation makes the code more testable, maintainable, and demonstrates understanding of clean architecture principles. When you can test game logic without needing to mock console I/O, you know the separation is working correctly.

### Class Responsibilities

#### Core Game Logic Layer

**Board**
- Manages the 3x3 grid state
- Validates moves (bounds checking, occupied cell detection)
- Detects win conditions (rows, columns, diagonals)
- Detects draw conditions (full board)
- Provides immutable board copying for AI calculations

**Move**
- Immutable value object representing a move with row and column coordinates
- Used for move validation and passing moves between layers

**Player (Abstract Base Class)**
- Defines the interface for players
- Stores player mark (X or O) and name
- Subclasses implement `getMove()` differently (human input vs AI calculation)

**HumanPlayer**
- Represents a human player
- Note: Move selection is handled by the CLI layer, not this class

**ComputerPlayer**
- Supports three difficulty levels: Easy (random moves), Medium (mixed strategy), Hard (full Minimax)
- Easy difficulty: Makes random valid moves with no strategy
- Medium difficulty: Uses optimal moves 60% of the time, random moves otherwise
- Hard difficulty: Implements full Minimax algorithm for unbeatable optimal play
- Difficulty level is set when creating the ComputerPlayer instance

**GameEngine**
- Orchestrates the game flow and turn management
- Processes moves and validates them through the Board
- Updates game state (PLAYING, X_WINS, O_WINS, DRAW)
- Manages player turns and switching
- Completely interface-agnostic - works with any Player implementation

#### Presentation Layer

**TicTacToeCLI**
- Handles all user input and output for command-line interface
- Parses user input into Move objects
- Displays the board state in text format
- Delegates game logic to GameEngine
- Provides game loop and user interaction flow

**GameGUI**
- Main frame using CardLayout to switch between menu and game screens
- MenuPanel: Handles game mode selection, player name input, and difficulty selection
- GamePanel: Displays the game board, manages turn indicators, score tracking, and game controls
- Uses Java Swing components with custom rounded button styling
- Converts user clicks into Move objects and delegates to GameEngine
- Displays board state with vibrant pink and purple colour scheme
- Uses SwingWorker for AI move computation to prevent UI blocking
- Manages visual feedback, animations, and winning line highlighting
- Implements placeholder text fields that clear automatically on focus

### Enums

**CellState**: Represents the state of a single cell (EMPTY, X, O)

**GameState**: Represents the current game state (PLAYING, X_WINS, O_WINS, DRAW)

**PlayerType**: Represents player types (HUMAN, COMPUTER) - used for configuration

**Difficulty**: Represents computer opponent difficulty levels (EASY, MEDIUM, HARD)

## Computer Player and Difficulty Levels

The computer player supports three difficulty levels, providing varying levels of challenge for players.

### Easy Difficulty

The computer makes completely random valid moves with no strategy. This provides a relaxed gameplay experience suitable for beginners or casual play.

### Medium Difficulty

The computer uses depth-limited Minimax algorithm with a maximum depth of 5 levels, making optimal moves 80% of the time and random moves 20% of the time. This creates a challenging opponent that uses strategic thinking and looks several moves ahead, but is not perfect. Most casual players will find this difficulty challenging and will win or draw occasionally. The depth-limited search provides intelligent play while the occasional random move keeps the game beatable.

### Hard Difficulty

The computer uses the full Minimax algorithm with alpha-beta pruning for mathematically perfect play. This difficulty is unbeatable - it plays optimally every single move, exploring the entire game tree to guarantee the best possible outcome. With perfect play, the best a player can achieve is a draw. The algorithm always blocks winning moves, takes winning opportunities when available, and creates forks (multiple winning threats) when possible. Alpha-beta pruning optimizes the algorithm by cutting off branches that cannot possibly affect the final decision, improving performance whilst maintaining optimal play.

#### How Minimax Works

The algorithm explores all possible future game states by recursively simulating moves. At each level:

- When it's the computer's turn (maximising player), it chooses the move that leads to the highest score
- When it's the opponent's turn (minimising player), it assumes the opponent will choose the move that leads to the lowest score for the computer

The algorithm assigns scores:
- +100 (minus depth) if the computer wins
- -100 (plus depth) if the opponent wins  
- 0 for a draw

By exploring the entire game tree, Minimax guarantees optimal play. In Tic-Tac-Toe, this means the computer will never lose - it can only win or draw, assuming perfect play from the opponent.

#### Complexity

The time complexity is O(b^d) where b is the average branching factor (approximately 5 for Tic-Tac-Toe) and d is the maximum depth (9 for a full game). In practice, with a 3x3 board, this is very fast. The Hard difficulty uses alpha-beta pruning to optimize the algorithm by cutting off branches that cannot affect the final decision, improving performance whilst maintaining optimal play.

The difficulty level is set when creating a ComputerPlayer instance and affects the move selection strategy throughout the game.

## What I Learnt

Building this project reinforced several important software design principles:

1. **Separation of Concerns**: By keeping game logic separate from I/O, the code became much more testable. I could write unit tests for Board and GameEngine without needing to mock System.out or Scanner, which made testing straightforward and reliable.

2. **Dependency Inversion**: The GameEngine doesn't depend on concrete HumanPlayer or ComputerPlayer classes - it works with the Player abstraction. This made it easy to add the computer player later without changing the engine code.

3. **Single Responsibility**: Each class has one clear purpose. Board manages state, GameEngine manages flow, CLI handles I/O. When a bug appeared, I knew exactly where to look.

4. **Value Objects**: Using an immutable Move class prevented issues with move objects being modified accidentally, and made the code more readable.

The architecture took some refactoring to get right. Initially, I had some game logic leaking into the CLI class, but extracting it into the engine made the codebase much cleaner. This iterative refinement process is something I've learnt to embrace - starting simple and improving the design as understanding grows.

## Possible Extensions

This architecture makes several extensions straightforward:

- **Different Board Sizes**: Extend Board to support NxN boards (would require updating win detection logic)
- **AI Difficulty Levels**: Modify ComputerPlayer to use simpler algorithms (random moves, first available) or add depth limiting to Minimax for easier difficulty
- **Network Play**: Add network communication layer that uses GameEngine for local game state
- **Tournament Mode**: Build a tournament system that uses multiple GameEngine instances
- **Undo/Redo**: Add move history to GameEngine to support undo functionality
- **Save/Load**: Serialize GameEngine state to save and resume games

The clean separation of concerns means these extensions can be added without modifying the core game logic, demonstrating the value of the architecture.

