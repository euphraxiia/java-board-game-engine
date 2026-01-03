package tictactoe;

// Represents the game board state - handles move validation, win/draw detection, UI-independent
public class Board {
    private static final int SIZE = 3;
    private CellState[][] grid;
    
    public Board() {
        grid = new CellState[SIZE][SIZE];
        clear();
    }
    
    // Creates a copy of the board for minimax calculations
    public Board(Board other) {
        this.grid = new CellState[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                this.grid[i][j] = other.grid[i][j];
            }
        }
    }
    
    public void clear() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = CellState.EMPTY;
            }
        }
    }
    
    public int getSize() {
        return SIZE;
    }
    
    public CellState getCell(int row, int col) {
        validateBounds(row, col);
        return grid[row][col];
    }
    
    // Validates if a move is legal (within bounds and cell is empty)
    public boolean isValidMove(Move move) {
        if (move == null) {
            return false;
        }
        int row = move.getRow();
        int col = move.getCol();
        
        if (!isInBounds(row, col)) {
            return false;
        }
        return grid[row][col] == CellState.EMPTY;
    }
    
    // Places a mark on the board if move is valid (returns true if successful)
    public boolean makeMove(Move move, CellState mark) {
        if (!isValidMove(move)) {
            return false;
        }
        grid[move.getRow()][move.getCol()] = mark;
        return true;
    }
    
    // Checks if the board is full (draw condition)
    public boolean isFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == CellState.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
    
    // Checks if a player has won (checks rows, columns, and diagonals)
    public boolean hasWon(CellState mark) {
        if (mark == CellState.EMPTY) {
            return false;
        }
        
        // Check rows
        for (int i = 0; i < SIZE; i++) {
            if (checkRow(i, mark)) {
                return true;
            }
        }
        
        // Check columns
        for (int j = 0; j < SIZE; j++) {
            if (checkColumn(j, mark)) {
                return true;
            }
        }
        
        // Check diagonals
        return checkMainDiagonal(mark) || checkAntiDiagonal(mark);
    }
    
    private boolean checkRow(int row, CellState mark) {
        for (int j = 0; j < SIZE; j++) {
            if (grid[row][j] != mark) {
                return false;
            }
        }
        return true;
    }
    
    private boolean checkColumn(int col, CellState mark) {
        for (int i = 0; i < SIZE; i++) {
            if (grid[i][col] != mark) {
                return false;
            }
        }
        return true;
    }
    
    private boolean checkMainDiagonal(CellState mark) {
        for (int i = 0; i < SIZE; i++) {
            if (grid[i][i] != mark) {
                return false;
            }
        }
        return true;
    }
    
    private boolean checkAntiDiagonal(CellState mark) {
        for (int i = 0; i < SIZE; i++) {
            if (grid[i][SIZE - 1 - i] != mark) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }
    
    private void validateBounds(int row, int col) {
        if (!isInBounds(row, col)) {
            throw new IllegalArgumentException("Position out of bounds: (" + row + ", " + col + ")");
        }
    }
}

