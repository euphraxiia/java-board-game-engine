package tictactoe;

import org.junit.Test;
import static org.junit.Assert.*;

// Unit tests for Board class - tests move validation, win detection, and draw detection
public class BoardTest {
    
    @Test
    public void testInitialBoardIsEmpty() {
        Board board = new Board();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(CellState.EMPTY, board.getCell(i, j));
            }
        }
    }
    
    @Test
    public void testValidMove() {
        Board board = new Board();
        Move move = new Move(1, 1);
        assertTrue(board.isValidMove(move));
    }
    
    @Test
    public void testInvalidMoveOutOfBounds() {
        Board board = new Board();
        Move move1 = new Move(-1, 0);
        Move move2 = new Move(0, -1);
        Move move3 = new Move(3, 0);
        Move move4 = new Move(0, 3);
        
        assertFalse(board.isValidMove(move1));
        assertFalse(board.isValidMove(move2));
        assertFalse(board.isValidMove(move3));
        assertFalse(board.isValidMove(move4));
    }
    
    @Test
    public void testInvalidMoveOccupiedCell() {
        Board board = new Board();
        Move move = new Move(0, 0);
        board.makeMove(move, CellState.X);
        
        assertFalse(board.isValidMove(move));
    }
    
    @Test
    public void testMakeMove() {
        Board board = new Board();
        Move move = new Move(1, 2);
        boolean result = board.makeMove(move, CellState.X);
        
        assertTrue(result);
        assertEquals(CellState.X, board.getCell(1, 2));
    }
    
    @Test
    public void testMakeMoveFailsOnInvalidMove() {
        Board board = new Board();
        Move move = new Move(0, 0);
        board.makeMove(move, CellState.X);
        
        boolean result = board.makeMove(move, CellState.O);
        assertFalse(result);
        assertEquals(CellState.X, board.getCell(0, 0));
    }
    
    @Test
    public void testWinRow() {
        Board board = new Board();
        board.makeMove(new Move(0, 0), CellState.X);
        board.makeMove(new Move(0, 1), CellState.X);
        board.makeMove(new Move(0, 2), CellState.X);
        
        assertTrue(board.hasWon(CellState.X));
        assertFalse(board.hasWon(CellState.O));
    }
    
    @Test
    public void testWinColumn() {
        Board board = new Board();
        board.makeMove(new Move(0, 1), CellState.O);
        board.makeMove(new Move(1, 1), CellState.O);
        board.makeMove(new Move(2, 1), CellState.O);
        
        assertTrue(board.hasWon(CellState.O));
    }
    
    @Test
    public void testWinMainDiagonal() {
        Board board = new Board();
        board.makeMove(new Move(0, 0), CellState.X);
        board.makeMove(new Move(1, 1), CellState.X);
        board.makeMove(new Move(2, 2), CellState.X);
        
        assertTrue(board.hasWon(CellState.X));
    }
    
    @Test
    public void testWinAntiDiagonal() {
        Board board = new Board();
        board.makeMove(new Move(0, 2), CellState.O);
        board.makeMove(new Move(1, 1), CellState.O);
        board.makeMove(new Move(2, 0), CellState.O);
        
        assertTrue(board.hasWon(CellState.O));
    }
    
    @Test
    public void testNoWin() {
        Board board = new Board();
        board.makeMove(new Move(0, 0), CellState.X);
        board.makeMove(new Move(0, 1), CellState.O);
        board.makeMove(new Move(1, 1), CellState.X);
        
        assertFalse(board.hasWon(CellState.X));
        assertFalse(board.hasWon(CellState.O));
    }
    
    @Test
    public void testIsFull() {
        Board board = new Board();
        // Fill board (not a winning pattern)
        board.makeMove(new Move(0, 0), CellState.X);
        board.makeMove(new Move(0, 1), CellState.O);
        board.makeMove(new Move(0, 2), CellState.X);
        board.makeMove(new Move(1, 0), CellState.O);
        board.makeMove(new Move(1, 1), CellState.X);
        board.makeMove(new Move(1, 2), CellState.O);
        board.makeMove(new Move(2, 0), CellState.X);
        board.makeMove(new Move(2, 1), CellState.O);
        board.makeMove(new Move(2, 2), CellState.X);
        
        assertTrue(board.isFull());
    }
    
    @Test
    public void testIsNotFull() {
        Board board = new Board();
        board.makeMove(new Move(0, 0), CellState.X);
        assertFalse(board.isFull());
    }
    
    @Test
    public void testBoardCopy() {
        Board original = new Board();
        original.makeMove(new Move(1, 1), CellState.X);
        original.makeMove(new Move(0, 0), CellState.O);
        
        Board copy = new Board(original);
        assertEquals(CellState.X, copy.getCell(1, 1));
        assertEquals(CellState.O, copy.getCell(0, 0));
        
        // Modify copy - original should be unchanged
        copy.makeMove(new Move(2, 2), CellState.X);
        assertEquals(CellState.EMPTY, original.getCell(2, 2));
        assertEquals(CellState.X, copy.getCell(2, 2));
    }
}

