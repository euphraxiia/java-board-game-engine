package tictactoe;

// Manages game flow, turn logic, and state transitions - UI-independent, works with Board/Player only
public class GameEngine {
    private Board board;
    private Player playerX;
    private Player playerO;
    private Player currentPlayer;
    private GameState state;
    
    public GameEngine(Player playerX, Player playerO) {
        if (playerX == null || playerO == null) {
            throw new IllegalArgumentException("Players cannot be null");
        }
        if (playerX.getMark() != CellState.X || playerO.getMark() != CellState.O) {
            throw new IllegalArgumentException("Player marks must match X and O");
        }
        
        this.playerX = playerX;
        this.playerO = playerO;
        this.board = new Board();
        this.currentPlayer = playerX; // X always starts
        this.state = GameState.PLAYING;
    }
    
    public GameState getState() {
        return state;
    }
    
    public Board getBoard() {
        return board;
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    // Processes a move from current player (returns true if successful, false if invalid)
    public boolean processMove(Move move) {
        if (state != GameState.PLAYING) {
            return false; // Game already over
        }
        
        if (!board.isValidMove(move)) {
            return false; // Invalid move
        }
        
        board.makeMove(move, currentPlayer.getMark());
        updateGameState();
        
        if (state == GameState.PLAYING) {
            switchCurrentPlayer();
        }
        
        return true;
    }
    
    // Gets next move from current player (for computer players only)
    public Move getComputerMove() {
        if (state != GameState.PLAYING) {
            return null;
        }
        
        if (currentPlayer instanceof ComputerPlayer) {
            return currentPlayer.getMove(board);
        }
        
        return null; // Current player is human
    }
    
    // Checks if the current player is a computer player
    public boolean isCurrentPlayerComputer() {
        return currentPlayer instanceof ComputerPlayer;
    }
    
    // Updates game state based on board condition (win/draw/playing)
    private void updateGameState() {
        if (board.hasWon(CellState.X)) {
            state = GameState.X_WINS;
        } else if (board.hasWon(CellState.O)) {
            state = GameState.O_WINS;
        } else if (board.isFull()) {
            state = GameState.DRAW;
        } else {
            state = GameState.PLAYING;
        }
    }
    
    // Switches turn to the other player
    private void switchCurrentPlayer() {
        currentPlayer = (currentPlayer == playerX) ? playerO : playerX;
    }
    
    // Resets the game to initial state
    public void reset() {
        board.clear();
        currentPlayer = playerX;
        state = GameState.PLAYING;
    }
}

