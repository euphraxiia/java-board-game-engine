# Java Board Game Engine - Tic-Tac-Toe

A command-line Tic-Tac-Toe game implementation in Java, demonstrating clean object-oriented design principles and separation of concerns. This project showcases how game logic can be completely decoupled from the user interface layer, making the core engine reusable across different interfaces.

## Overview

This project implements a complete Tic-Tac-Toe game with two modes: player versus player and player versus computer. The computer player uses the Minimax algorithm to play optimally. The architecture is designed with a clear separation between the game logic layer (Board, GameEngine, Player) and the presentation layer (TicTacToeCLI), allowing the core engine to be reused with different interfaces such as a GUI or web interface.

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

- The `Board`, `GameEngine`, and `Player` classes contain no references to `System.out`, `Scanner`, or any I/O operations
- The CLI layer (`TicTacToeCLI`) is a thin wrapper that handles user input and output, delegating all game decisions to the engine
- The engine could theoretically be used with a GUI, web interface, or even an API without modification

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
- Implements Minimax algorithm for optimal play
- Calculates the best move by exploring the game tree
- Always plays optimally (cannot be beaten, can only draw or lose if opponent plays optimally)

**GameEngine**
- Orchestrates the game flow and turn management
- Processes moves and validates them through the Board
- Updates game state (PLAYING, X_WINS, O_WINS, DRAW)
- Manages player turns and switching
- Completely interface-agnostic - works with any Player implementation

#### Presentation Layer

**TicTacToeCLI**
- Handles all user input and output
- Parses user input into Move objects
- Displays the board state
- Delegates game logic to GameEngine
- Provides game loop and user interaction flow

### Enums

**CellState**: Represents the state of a single cell (EMPTY, X, O)

**GameState**: Represents the current game state (PLAYING, X_WINS, O_WINS, DRAW)

**PlayerType**: Represents player types (HUMAN, COMPUTER) - used for configuration

## Minimax Algorithm

The computer player uses the Minimax algorithm to play optimally. Minimax is a decision-making algorithm commonly used in two-player zero-sum games like Tic-Tac-Toe.

### How It Works

The algorithm explores all possible future game states by recursively simulating moves. At each level:

- When it's the computer's turn (maximising player), it chooses the move that leads to the highest score
- When it's the opponent's turn (minimising player), it assumes the opponent will choose the move that leads to the lowest score for the computer

The algorithm assigns scores:
- +10 (minus depth) if the computer wins
- -10 (plus depth) if the opponent wins  
- 0 for a draw

By exploring the entire game tree, Minimax guarantees optimal play. In Tic-Tac-Toe, this means the computer will never lose - it can only win or draw, assuming perfect play from the opponent.

### Complexity

The time complexity is O(b^d) where b is the average branching factor (approximately 5 for Tic-Tac-Toe) and d is the maximum depth (9 for a full game). In practice, with a 3x3 board, this is very fast. For larger boards, alpha-beta pruning could be added to improve performance.

## What I Learnt

Building this project reinforced several important software design principles:

1. **Separation of Concerns**: By keeping game logic separate from I/O, the code became much more testable. I could write unit tests for Board and GameEngine without needing to mock System.out or Scanner, which made testing straightforward and reliable.

2. **Dependency Inversion**: The GameEngine doesn't depend on concrete HumanPlayer or ComputerPlayer classes - it works with the Player abstraction. This made it easy to add the computer player later without changing the engine code.

3. **Single Responsibility**: Each class has one clear purpose. Board manages state, GameEngine manages flow, CLI handles I/O. When a bug appeared, I knew exactly where to look.

4. **Value Objects**: Using an immutable Move class prevented issues with move objects being modified accidentally, and made the code more readable.

The architecture took some refactoring to get right. Initially, I had some game logic leaking into the CLI class, but extracting it into the engine made the codebase much cleaner. This iterative refinement process is something I've learnt to embrace - starting simple and improving the design as understanding grows.

## Possible Extensions

This architecture makes several extensions straightforward:

- **GUI Interface**: Create a Swing or JavaFX GUI that uses the same GameEngine, replacing TicTacToeCLI with a GUI controller
- **Different Board Sizes**: Extend Board to support NxN boards (would require updating win detection logic)
- **AI Difficulty Levels**: Modify ComputerPlayer to use simpler algorithms (random moves, first available) or add depth limiting to Minimax for easier difficulty
- **Network Play**: Add network communication layer that uses GameEngine for local game state
- **Tournament Mode**: Build a tournament system that uses multiple GameEngine instances
- **Undo/Redo**: Add move history to GameEngine to support undo functionality
- **Save/Load**: Serialize GameEngine state to save and resume games

The clean separation of concerns means these extensions can be added without modifying the core game logic, demonstrating the value of the architecture.

