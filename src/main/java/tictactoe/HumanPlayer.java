package tictactoe;

// Human player - move selection handled by CLI layer, this class just holds player data
public class HumanPlayer extends Player {
    
    public HumanPlayer(CellState mark, String name) {
        super(mark, name);
    }
    
    // Human moves come from CLI input, not calculated here
    @Override
    public Move getMove(Board board) {
        // This should not be called - moves come from CLI input
        throw new UnsupportedOperationException("HumanPlayer moves come from CLI input");
    }
}

