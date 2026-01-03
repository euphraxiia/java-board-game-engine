package tictactoe;

import org.junit.Test;
import static org.junit.Assert.*;

// Unit tests for GameEngine - tests game flow, state transitions, and move processing
public class GameEngineTest {
    
    @Test
    public void testInitialState() {
        Player playerX = new HumanPlayer(CellState.X, "Player 1");
        Player playerO = new HumanPlayer(CellState.O, "Player 2");
        GameEngine engine = new GameEngine(playerX, playerO);
        
        assertEquals(GameState.PLAYING, engine.getState());
        assertEquals(playerX, engine.getCurrentPlayer());
    }
    
    @Test
    public void testProcessValidMove() {
        Player playerX = new HumanPlayer(CellState.X, "Player 1");
        Player playerO = new HumanPlayer(CellState.O, "Player 2");
        GameEngine engine = new GameEngine(playerX, playerO);
        
        Move move = new Move(0, 0);
        boolean result = engine.processMove(move);
        
        assertTrue(result);
        assertEquals(CellState.X, engine.getBoard().getCell(0, 0));
        assertEquals(playerO, engine.getCurrentPlayer()); // Turn should switch
    }
    
    @Test
    public void testProcessInvalidMove() {
        Player playerX = new HumanPlayer(CellState.X, "Player 1");
        Player playerO = new HumanPlayer(CellState.O, "Player 2");
        GameEngine engine = new GameEngine(playerX, playerO);
        
        Move move1 = new Move(0, 0);
        engine.processMove(move1);
        
        Move move2 = new Move(0, 0); // Same position
        boolean result = engine.processMove(move2);
        
        assertFalse(result);
        assertEquals(CellState.X, engine.getBoard().getCell(0, 0)); // Still X
    }
    
    @Test
    public void testXWins() {
        Player playerX = new HumanPlayer(CellState.X, "Player 1");
        Player playerO = new HumanPlayer(CellState.O, "Player 2");
        GameEngine engine = new GameEngine(playerX, playerO);
        
        // X wins with a row
        engine.processMove(new Move(0, 0)); // X
        engine.processMove(new Move(1, 0)); // O
        engine.processMove(new Move(0, 1)); // X
        engine.processMove(new Move(1, 1)); // O
        engine.processMove(new Move(0, 2)); // X wins
        
        assertEquals(GameState.X_WINS, engine.getState());
    }
    
    @Test
    public void testOWins() {
        Player playerX = new HumanPlayer(CellState.X, "Player 1");
        Player playerO = new HumanPlayer(CellState.O, "Player 2");
        GameEngine engine = new GameEngine(playerX, playerO);
        
        // O wins with a column
        engine.processMove(new Move(0, 0)); // X
        engine.processMove(new Move(0, 1)); // O
        engine.processMove(new Move(1, 0)); // X
        engine.processMove(new Move(1, 1)); // O
        engine.processMove(new Move(2, 2)); // X
        engine.processMove(new Move(2, 1)); // O wins
        
        assertEquals(GameState.O_WINS, engine.getState());
    }
    
    @Test
    public void testDraw() {
        Player playerX = new HumanPlayer(CellState.X, "Player 1");
        Player playerO = new HumanPlayer(CellState.O, "Player 2");
        GameEngine engine = new GameEngine(playerX, playerO);
        
        // Create a draw scenario
        engine.processMove(new Move(0, 0)); // X
        engine.processMove(new Move(0, 1)); // O
        engine.processMove(new Move(0, 2)); // X
        engine.processMove(new Move(1, 0)); // O
        engine.processMove(new Move(1, 2)); // X
        engine.processMove(new Move(1, 1)); // O
        engine.processMove(new Move(2, 0)); // X
        engine.processMove(new Move(2, 2)); // O
        engine.processMove(new Move(2, 1)); // X
        
        assertEquals(GameState.DRAW, engine.getState());
    }
    
    @Test
    public void testNoMoveAfterGameEnd() {
        Player playerX = new HumanPlayer(CellState.X, "Player 1");
        Player playerO = new HumanPlayer(CellState.O, "Player 2");
        GameEngine engine = new GameEngine(playerX, playerO);
        
        // X wins
        engine.processMove(new Move(0, 0)); // X
        engine.processMove(new Move(1, 0)); // O
        engine.processMove(new Move(0, 1)); // X
        engine.processMove(new Move(1, 1)); // O
        engine.processMove(new Move(0, 2)); // X wins
        
        assertEquals(GameState.X_WINS, engine.getState());
        
        // Try to make another move
        boolean result = engine.processMove(new Move(2, 2));
        assertFalse(result);
        assertEquals(GameState.X_WINS, engine.getState());
    }
    
    @Test
    public void testIsCurrentPlayerComputer() {
        Player playerX = new HumanPlayer(CellState.X, "Player 1");
        Player playerO = new ComputerPlayer(CellState.O, "Computer");
        GameEngine engine = new GameEngine(playerX, playerO);
        
        assertFalse(engine.isCurrentPlayerComputer()); // X is human
        
        engine.processMove(new Move(0, 0)); // X moves
        
        assertTrue(engine.isCurrentPlayerComputer()); // O is computer
    }
    
    @Test
    public void testGetComputerMove() {
        Player playerX = new HumanPlayer(CellState.X, "Player 1");
        Player playerO = new ComputerPlayer(CellState.O, "Computer");
        GameEngine engine = new GameEngine(playerX, playerO);
        
        engine.processMove(new Move(0, 0)); // X moves first
        
        Move computerMove = engine.getComputerMove();
        assertNotNull(computerMove);
        assertTrue(engine.getBoard().isValidMove(computerMove));
    }
    
    @Test
    public void testReset() {
        Player playerX = new HumanPlayer(CellState.X, "Player 1");
        Player playerO = new HumanPlayer(CellState.O, "Player 2");
        GameEngine engine = new GameEngine(playerX, playerO);
        
        engine.processMove(new Move(0, 0));
        engine.processMove(new Move(1, 1));
        
        engine.reset();
        
        assertEquals(GameState.PLAYING, engine.getState());
        assertEquals(playerX, engine.getCurrentPlayer());
        assertEquals(CellState.EMPTY, engine.getBoard().getCell(0, 0));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullPlayerX() {
        Player playerO = new HumanPlayer(CellState.O, "Player 2");
        new GameEngine(null, playerO);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullPlayerO() {
        Player playerX = new HumanPlayer(CellState.X, "Player 1");
        new GameEngine(playerX, null);
    }
}

