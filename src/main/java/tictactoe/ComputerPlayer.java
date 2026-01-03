package tictactoe;

// Computer player using Minimax algorithm for optimal play (O(b^d) complexity)
public class ComputerPlayer extends Player {
    
    public ComputerPlayer(CellState mark, String name) {
        super(mark, name);
    }
    
    @Override
    public Move getMove(Board board) {
        Move bestMove = findBestMove(board);
        return bestMove;
    }
    
    // Finds the best move using minimax algorithm (returns move with highest score)
    private Move findBestMove(Board board) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;
        CellState opponentMark = (this.mark == CellState.X) ? CellState.O : CellState.X;
        
        // First, check if we can win immediately
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                Move move = new Move(i, j);
                if (board.isValidMove(move)) {
                    Board testBoard = new Board(board);
                    testBoard.makeMove(move, this.mark);
                    if (testBoard.hasWon(this.mark)) {
                        return move; // Winning move found
                    }
                }
            }
        }
        
        // Second, check if we need to block opponent from winning
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                Move move = new Move(i, j);
                if (board.isValidMove(move)) {
                    Board testBoard = new Board(board);
                    testBoard.makeMove(move, opponentMark);
                    if (testBoard.hasWon(opponentMark)) {
                        return move; // Block opponent's winning move
                    }
                }
            }
        }
        
        // Otherwise, use minimax to find best move
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                Move move = new Move(i, j);
                if (board.isValidMove(move)) {
                    Board testBoard = new Board(board);
                    testBoard.makeMove(move, this.mark);
                    
                    int score = minimax(testBoard, 0, false);
                    
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = move;
                    }
                }
            }
        }
        
        // Fallback - defensive programming (should never happen on valid board)
        if (bestMove == null) {
            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    Move move = new Move(i, j);
                    if (board.isValidMove(move)) {
                        return move;
                    }
                }
            }
        }
        
        return bestMove;
    }
    
    // Minimax algorithm: maximizes computer score, minimizes opponent score (+10 win, -10 loss, 0 draw)
    private int minimax(Board board, int depth, boolean isMaximizing) {
        CellState opponentMark = (this.mark == CellState.X) ? CellState.O : CellState.X;
        
        if (board.hasWon(this.mark)) {
            return 10 - depth; // Prefer winning sooner
        }
        if (board.hasWon(opponentMark)) {
            return depth - 10; // Prefer delaying loss
        }
        if (board.isFull()) {
            return 0;
        }
        
        if (isMaximizing) {
            int maxScore = Integer.MIN_VALUE;
            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    Move move = new Move(i, j);
                    if (board.isValidMove(move)) {
                        Board testBoard = new Board(board);
                        testBoard.makeMove(move, this.mark);
                        int score = minimax(testBoard, depth + 1, false);
                        maxScore = Math.max(maxScore, score);
                    }
                }
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    Move move = new Move(i, j);
                    if (board.isValidMove(move)) {
                        Board testBoard = new Board(board);
                        testBoard.makeMove(move, opponentMark);
                        int score = minimax(testBoard, depth + 1, true);
                        minScore = Math.min(minScore, score);
                    }
                }
            }
            return minScore;
        }
    }
}

