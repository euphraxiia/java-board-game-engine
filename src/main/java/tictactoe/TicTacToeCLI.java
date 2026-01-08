package tictactoe;

import java.util.Scanner;

// Command-line interface - thin I/O layer that delegates game logic to GameEngine (reusable with GUI)
public class TicTacToeCLI {
    private final GameEngine engine;
    private final Scanner scanner;
    
    public TicTacToeCLI(GameEngine engine) {
        this.engine = engine;
        this.scanner = new Scanner(System.in);
    }
    
    // Main game loop - handles user interaction and delegates to engine
    public void run() {
        printWelcomeMessage();
        
        while (true) {
            printBoard();
            printCurrentPlayer();
            
            if (engine.isCurrentPlayerComputer()) {
                handleComputerTurn();
            } else {
                handleHumanTurn();
            }
            
            GameState state = engine.getState();
            if (state != GameState.PLAYING) {
                printBoard();
                printGameResult(state);
                
                if (askPlayAgain()) {
                    engine.reset();
                } else {
                    break;
                }
            }
        }
        
        scanner.close();
        printGoodbyeMessage();
    }
    
    // Handles computer player's turn by getting move from engine
    private void handleComputerTurn() {
        System.out.println("Computer is thinking...");
        Move move = engine.getComputerMove();
        if (move != null) {
            engine.processMove(move);
        }
    }
    
    // Handles human player's turn by getting input and creating Move object
    private void handleHumanTurn() {
        while (true) {
            System.out.print("Enter your move (row col, e.g., '0 2' for row 0, column 2): ");
            String input = scanner.nextLine().trim();
            
            Move move = parseMove(input);
            if (move == null) {
                System.out.println("Invalid input. Please enter two numbers (row and column, 0-2).");
                continue;
            }
            
            if (engine.processMove(move)) {
                break; // Move was successful
            } else {
                System.out.println("Invalid move. That position is already taken or out of bounds.");
            }
        }
    }
    
    // Parses user input string into Move object (returns null if invalid)
    private Move parseMove(String input) {
        try {
            String[] parts = input.split("\\s+");
            if (parts.length != 2) {
                return null;
            }
            
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            
            // Validate range (0-2 for 3x3 board)
            if (row < 0 || row > 2 || col < 0 || col > 2) {
                return null;
            }
            
            return new Move(row, col);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    // Prints the current board state
    private void printBoard() {
        Board board = engine.getBoard();
        System.out.println("\n  0   1   2");
        for (int i = 0; i < board.getSize(); i++) {
            System.out.print(i + " ");
            for (int j = 0; j < board.getSize(); j++) {
                CellState cell = board.getCell(i, j);
                char symbol = cellToChar(cell);
                System.out.print(symbol);
                if (j < board.getSize() - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
            if (i < board.getSize() - 1) {
                System.out.println("  ---------");
            }
        }
        System.out.println();
    }
    
    // Converts CellState enum to display character
    private char cellToChar(CellState cell) {
        switch (cell) {
            case X: return 'X';
            case O: return 'O';
            case EMPTY: return ' ';
            default: return ' ';
        }
    }
    
    // Prints which player's turn it is
    private void printCurrentPlayer() {
        Player current = engine.getCurrentPlayer();
        System.out.println("Current player: " + current.getName() + " (" + 
                          (current.getMark() == CellState.X ? "X" : "O") + ")");
    }
    
    // Prints the game result when game ends
    private void printGameResult(GameState state) {
        switch (state) {
            case X_WINS:
                System.out.println("X wins!");
                break;
            case O_WINS:
                System.out.println("O wins!");
                break;
            case DRAW:
                System.out.println("It's a draw!");
                break;
            default:
                break;
        }
    }
    
    // Asks player if they want to play again
    private boolean askPlayAgain() {
        System.out.print("Play again? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("yes") || response.equals("y");
    }
    
    private void printWelcomeMessage() {
        System.out.println("Welcome to Tic-Tac-Toe!");
        System.out.println("Enter moves as 'row col' (e.g., '0 0' for top-left, '1 1' for center)");
        System.out.println("First number is the row (0-2), second number is the column (0-2)\n");
    }
    
    private void printGoodbyeMessage() {
        System.out.println("Thanks for playing!");
    }
    
    // Main entry point - creates game setup and starts CLI
    public static void main(String[] args) {
        Scanner setupScanner = new Scanner(System.in);
        
        System.out.println("Select game mode:");
        System.out.println("1. Player vs Player");
        System.out.println("2. Player vs Computer");
        System.out.print("Enter choice (1 or 2): ");
        
        String choice = setupScanner.nextLine().trim();
        Player playerX, playerO;
        
        if ("2".equals(choice)) {
            playerX = new HumanPlayer(CellState.X, "Player 1");
            playerO = new ComputerPlayer(CellState.O, "Computer", Difficulty.HARD);
        } else {
            playerX = new HumanPlayer(CellState.X, "Player 1");
            playerO = new HumanPlayer(CellState.O, "Player 2");
        }
        
        // Don't close setupScanner - it wraps System.in which we need later
        // Closing it would close System.in and break subsequent input
        
        GameEngine engine = new GameEngine(playerX, playerO);
        TicTacToeCLI cli = new TicTacToeCLI(engine);
        cli.run();
    }
}

