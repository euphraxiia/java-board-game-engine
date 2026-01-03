package tictactoe;

// Abstract base class for players - separates player logic from game engine
public abstract class Player {
    protected CellState mark;
    protected String name;
    
    public Player(CellState mark, String name) {
        this.mark = mark;
        this.name = name;
    }
    
    public CellState getMark() {
        return mark;
    }
    
    public String getName() {
        return name;
    }
    
    // Gets the next move from the player (implemented by subclasses)
    public abstract Move getMove(Board board);
}

