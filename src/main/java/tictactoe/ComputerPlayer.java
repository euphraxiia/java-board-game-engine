package tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Computer player with difficulty levels - Easy (random), Medium (depth-limited minimax), Hard (full minimax with alpha-beta)
public class ComputerPlayer extends Player {
    private final Difficulty difficulty;
    private final Random random;
    
    public ComputerPlayer(CellState mark, String name, Difficulty difficulty) {
        super(mark, name);
        this.difficulty = difficulty;
        this.random = new Random();
    }
    
    @Override
    public Move getMove(Board board) {
        switch (difficulty) {
            case EASY:
                return getRandomMove(board);
            case MEDIUM:
                return getMediumMove(board);
            case HARD:
                return getBestMove(board);
            default:
                return getRandomMove(board);
        }
    }
    
    // Easy: Returns a random valid move
    private Move getRandomMove(Board board) {
        List<Move> validMoves = new ArrayList<>();
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                Move move = new Move(i, j);
                if (board.isValidMove(move)) {
                    validMoves.add(move);
                }
            }
        }
        if (validMoves.isEmpty()) {
            return null;
        }
        return validMoves.get(random.nextInt(validMoves.size()));
    }
    
    // Medium: Uses depth-limited minimax (depth 5) 80% of the time, random 20% for occasional mistakes
    private Move getMediumMove(Board board) {
        if (random.nextDouble() < 0.80) {
            return getBestMoveWithDepth(board, 5);
        } else {
            return getRandomMove(board);
        }
    }
    
    // Hard: Uses full minimax with alpha-beta pruning for unbeatable play
    private Move getBestMove(Board board) {
        CellState opponentMark = (this.mark == CellState.X) ? CellState.O : CellState.X;
        List<Move> validMoves = getValidMoves(board);
        
        for (Move move : validMoves) {
            Board testBoard = new Board(board);
            testBoard.makeMove(move, this.mark);
            if (testBoard.hasWon(this.mark)) {
                return move;
            }
        }
        
        for (Move move : validMoves) {
            Board testBoard = new Board(board);
            testBoard.makeMove(move, opponentMark);
            if (testBoard.hasWon(opponentMark)) {
                return move;
            }
        }
        
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;
        for (Move move : validMoves) {
            Board testBoard = new Board(board);
            testBoard.makeMove(move, this.mark);
            int score = minimaxWithAlphaBeta(testBoard, 0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        
        return bestMove != null ? bestMove : getRandomMove(board);
    }
    
    // Medium: Uses depth-limited minimax for challenging but not perfect play
    private Move getBestMoveWithDepth(Board board, int maxDepth) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;
        CellState opponentMark = (this.mark == CellState.X) ? CellState.O : CellState.X;
        List<Move> validMoves = getValidMoves(board);
        
        for (Move move : validMoves) {
            Board testBoard = new Board(board);
            testBoard.makeMove(move, this.mark);
            int score = minimaxWithDepth(testBoard, 0, false, maxDepth);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        
        return bestMove != null ? bestMove : getRandomMove(board);
    }
    
    // Full minimax with alpha-beta pruning for Hard mode (unbeatable)
    private int minimaxWithAlphaBeta(Board board, int depth, boolean isMaximizing, int alpha, int beta) {
        CellState opponentMark = (this.mark == CellState.X) ? CellState.O : CellState.X;
        
        if (board.hasWon(this.mark)) {
            return 100 - depth;
        }
        if (board.hasWon(opponentMark)) {
            return depth - 100;
        }
        if (board.isFull()) {
            return 0;
        }
        
        List<Move> validMoves = getValidMoves(board);
        
        if (isMaximizing) {
            int maxScore = Integer.MIN_VALUE;
            for (Move move : validMoves) {
                Board testBoard = new Board(board);
                testBoard.makeMove(move, this.mark);
                int score = minimaxWithAlphaBeta(testBoard, depth + 1, false, alpha, beta);
                maxScore = Math.max(maxScore, score);
                alpha = Math.max(alpha, score);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (Move move : validMoves) {
                Board testBoard = new Board(board);
                testBoard.makeMove(move, opponentMark);
                int score = minimaxWithAlphaBeta(testBoard, depth + 1, true, alpha, beta);
                minScore = Math.min(minScore, score);
                beta = Math.min(beta, score);
                if (beta <= alpha) {
                    break;
                }
            }
            return minScore;
        }
    }
    
    // Depth-limited minimax for Medium mode (challenging but not perfect)
    private int minimaxWithDepth(Board board, int depth, boolean isMaximizing, int maxDepth) {
        CellState opponentMark = (this.mark == CellState.X) ? CellState.O : CellState.X;
        
        if (board.hasWon(this.mark)) {
            return 100 - depth;
        }
        if (board.hasWon(opponentMark)) {
            return depth - 100;
        }
        if (board.isFull() || depth >= maxDepth) {
            return evaluatePosition(board);
        }
        
        List<Move> validMoves = getValidMoves(board);
        
        if (isMaximizing) {
            int maxScore = Integer.MIN_VALUE;
            for (Move move : validMoves) {
                Board testBoard = new Board(board);
                testBoard.makeMove(move, this.mark);
                int score = minimaxWithDepth(testBoard, depth + 1, false, maxDepth);
                maxScore = Math.max(maxScore, score);
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (Move move : validMoves) {
                Board testBoard = new Board(board);
                testBoard.makeMove(move, opponentMark);
                int score = minimaxWithDepth(testBoard, depth + 1, true, maxDepth);
                minScore = Math.min(minScore, score);
            }
            return minScore;
        }
    }
    
    // Evaluates board position for depth-limited minimax (heuristic evaluation)
    private int evaluatePosition(Board board) {
        CellState opponentMark = (this.mark == CellState.X) ? CellState.O : CellState.X;
        
        int score = 0;
        score += countLines(board, this.mark) * 3;
        score -= countLines(board, opponentMark) * 3;
        
        return score;
    }
    
    // Counts potential winning lines (2 in a row with empty third cell)
    private int countLines(Board board, CellState mark) {
        int count = 0;
        int size = board.getSize();
        CellState empty = CellState.EMPTY;
        
        for (int i = 0; i < size; i++) {
            int rowMark = 0, rowEmpty = 0;
            int colMark = 0, colEmpty = 0;
            for (int j = 0; j < size; j++) {
                if (board.getCell(i, j) == mark) rowMark++;
                else if (board.getCell(i, j) == empty) rowEmpty++;
                if (board.getCell(j, i) == mark) colMark++;
                else if (board.getCell(j, i) == empty) colEmpty++;
            }
            if (rowMark == 2 && rowEmpty == 1) count++;
            if (colMark == 2 && colEmpty == 1) count++;
        }
        
        int diag1Mark = 0, diag1Empty = 0;
        int diag2Mark = 0, diag2Empty = 0;
        for (int i = 0; i < size; i++) {
            if (board.getCell(i, i) == mark) diag1Mark++;
            else if (board.getCell(i, i) == empty) diag1Empty++;
            if (board.getCell(i, size - 1 - i) == mark) diag2Mark++;
            else if (board.getCell(i, size - 1 - i) == empty) diag2Empty++;
        }
        if (diag1Mark == 2 && diag1Empty == 1) count++;
        if (diag2Mark == 2 && diag2Empty == 1) count++;
        
        return count;
    }
    
    // Helper to get all valid moves
    private List<Move> getValidMoves(Board board) {
        List<Move> validMoves = new ArrayList<>();
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                Move move = new Move(i, j);
                if (board.isValidMove(move)) {
                    validMoves.add(move);
                }
            }
        }
        return validMoves;
    }
}
