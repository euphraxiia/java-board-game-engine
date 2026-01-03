package tictactoe;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// GUI interface for Tic-Tac-Toe using Swing - visual layer that delegates to GameEngine
public class GameGUI extends JFrame {
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 600;
    
    // Pastel colour palette
    private static final Color LIGHT_PINK = new Color(255, 228, 225);
    private static final Color LIGHT_PURPLE = new Color(221, 192, 226);
    private static final Color LAVENDER = new Color(230, 230, 250);
    private static final Color CREAM = new Color(255, 253, 245);
    private static final Color SOFT_PURPLE = new Color(200, 180, 215);
    private static final Color PINK_X = new Color(255, 182, 193);
    private static final Color PURPLE_O = new Color(186, 164, 206);
    private static final Color WIN_HIGHLIGHT = new Color(255, 240, 245);
    
    private final GameEngine engine;
    private JButton[][] boardButtons;
    private JLabel statusLabel;
    private JLabel scoreLabel;
    private JButton newGameButton;
    private JPanel boardPanel;
    private final String playerXName;
    private final String playerOName;
    private int xWins = 0;
    private int oWins = 0;
    private int draws = 0;
    private boolean isWaitingForComputer = false;
    private Timer computerMoveTimer;
    
    public GameGUI(GameEngine engine, String playerXName, String playerOName) {
        this.engine = engine;
        this.playerXName = playerXName;
        this.playerOName = playerOName;
        
        setTitle("Tic-Tac-Toe");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initGUI();
        updateDisplay();
        
        if (engine.isCurrentPlayerComputer()) {
            scheduleComputerMove();
        }
    }
    
    // Initialises all GUI components and layout
    private void initGUI() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(CREAM);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JPanel topPanel = createTopPanel();
        boardPanel = createBoardPanel();
        JPanel bottomPanel = createBottomPanel();
        
        add(topPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    // Creates top panel with status and score labels
    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CREAM);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        statusLabel.setForeground(new Color(100, 80, 120));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        scoreLabel = new JLabel("", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
        scoreLabel.setForeground(new Color(120, 100, 140));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(statusLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(scoreLabel);
        
        return panel;
    }
    
    // Creates the 3x3 game board panel with buttons
    private JPanel createBoardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(LAVENDER);
        panel.setLayout(new GridLayout(3, 3, 8, 8));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        boardButtons = new JButton[3][3];
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton button = createBoardButton(i, j);
                boardButtons[i][j] = button;
                panel.add(button);
            }
        }
        
        return panel;
    }
    
    // Creates individual board button with styling and click handler
    private JButton createBoardButton(int row, int col) {
        JButton button = new JButton("");
        button.setFont(new Font("Dialog", Font.BOLD, 48));
        button.setFocusPainted(false);
        button.setBackground(LAVENDER);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SOFT_PURPLE, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled() && button.getText().isEmpty()) {
                    button.setBackground(LIGHT_PURPLE);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (button.getText().isEmpty()) {
                    button.setBackground(LAVENDER);
                }
            }
        });
        
        button.addActionListener(e -> handleCellClick(row, col));
        
        return button;
    }
    
    // Creates bottom panel with new game button
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CREAM);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Dialog", Font.BOLD, 14));
        newGameButton.setBackground(LIGHT_PINK);
        newGameButton.setForeground(new Color(100, 80, 120));
        newGameButton.setFocusPainted(false);
        newGameButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SOFT_PURPLE, 2),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        newGameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        newGameButton.addActionListener(e -> resetGame());
        
        panel.add(newGameButton);
        
        return panel;
    }
    
    // Handles cell button click - creates move and processes through engine
    private void handleCellClick(int row, int col) {
        if (isWaitingForComputer || engine.getState() != GameState.PLAYING) {
            return;
        }
        
        if (engine.isCurrentPlayerComputer()) {
            return;
        }
        
        Move move = new Move(row, col);
        if (engine.processMove(move)) {
            updateDisplay();
            
            if (engine.getState() == GameState.PLAYING && engine.isCurrentPlayerComputer()) {
                scheduleComputerMove();
            }
        }
    }
    
    // Schedules computer move with small delay for visual feedback
    private void scheduleComputerMove() {
        isWaitingForComputer = true;
        updateDisplay();
        
        if (computerMoveTimer != null) {
            computerMoveTimer.stop();
        }
        
        computerMoveTimer = new Timer(500, e -> {
            if (engine.getState() == GameState.PLAYING && engine.isCurrentPlayerComputer()) {
                Move computerMove = engine.getComputerMove();
                if (computerMove != null) {
                    engine.processMove(computerMove);
                    updateDisplay();
                }
            }
            isWaitingForComputer = false;
            
            if (computerMoveTimer != null) {
                computerMoveTimer.stop();
            }
        });
        
        computerMoveTimer.setRepeats(false);
        computerMoveTimer.start();
    }
    
    // Updates all visual elements based on current game state
    private void updateDisplay() {
        updateBoard();
        updateStatus();
        updateScore();
        updateButtonStates();
        
        if (engine.getState() != GameState.PLAYING) {
            highlightWinningLine();
        }
    }
    
    // Updates board buttons to reflect current board state
    private void updateBoard() {
        Board board = engine.getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                CellState cell = board.getCell(i, j);
                JButton button = boardButtons[i][j];
                
                if (cell == CellState.X) {
                    button.setText("X");
                    button.setForeground(PINK_X);
                    button.setBackground(LIGHT_PINK);
                } else if (cell == CellState.O) {
                    button.setText("O");
                    button.setForeground(PURPLE_O);
                    button.setBackground(LIGHT_PURPLE);
                } else {
                    button.setText("");
                    button.setBackground(LAVENDER);
                }
            }
        }
    }
    
    // Updates status label with current game state
    private void updateStatus() {
        GameState state = engine.getState();
        
        if (isWaitingForComputer) {
            statusLabel.setText("Computer is thinking...");
            return;
        }
        
        switch (state) {
            case PLAYING:
                Player current = engine.getCurrentPlayer();
                String name = current.getName();
                String mark = current.getMark() == CellState.X ? "X" : "O";
                statusLabel.setText(name + "'s turn (" + mark + ")");
                break;
            case X_WINS:
                statusLabel.setText(playerXName + " wins!");
                xWins++;
                break;
            case O_WINS:
                statusLabel.setText(playerOName + " wins!");
                oWins++;
                break;
            case DRAW:
                statusLabel.setText("It's a draw!");
                draws++;
                break;
        }
    }
    
    // Updates score label
    private void updateScore() {
        scoreLabel.setText(String.format("%s: %d  |  %s: %d  |  Draws: %d", 
            playerXName, xWins, playerOName, oWins, draws));
    }
    
    // Enables/disables buttons based on game state
    private void updateButtonStates() {
        boolean enabled = engine.getState() == GameState.PLAYING && !isWaitingForComputer && !engine.isCurrentPlayerComputer();
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton button = boardButtons[i][j];
                Board board = engine.getBoard();
                boolean isEmpty = board.getCell(i, j) == CellState.EMPTY;
                button.setEnabled(enabled && isEmpty);
            }
        }
    }
    
    // Highlights the winning line when game ends
    private void highlightWinningLine() {
        GameState state = engine.getState();
        if (state != GameState.X_WINS && state != GameState.O_WINS) {
            return;
        }
        
        CellState winningMark = (state == GameState.X_WINS) ? CellState.X : CellState.O;
        Board board = engine.getBoard();
        
        // Check rows
        for (int i = 0; i < 3; i++) {
            boolean isWinningRow = true;
            for (int j = 0; j < 3; j++) {
                if (board.getCell(i, j) != winningMark) {
                    isWinningRow = false;
                    break;
                }
            }
            if (isWinningRow) {
                for (int j = 0; j < 3; j++) {
                    boardButtons[i][j].setBackground(WIN_HIGHLIGHT);
                }
                return;
            }
        }
        
        // Check columns
        for (int j = 0; j < 3; j++) {
            boolean isWinningCol = true;
            for (int i = 0; i < 3; i++) {
                if (board.getCell(i, j) != winningMark) {
                    isWinningCol = false;
                    break;
                }
            }
            if (isWinningCol) {
                for (int i = 0; i < 3; i++) {
                    boardButtons[i][j].setBackground(WIN_HIGHLIGHT);
                }
                return;
            }
        }
        
        // Check main diagonal
        boolean isMainDiagonal = true;
        for (int i = 0; i < 3; i++) {
            if (board.getCell(i, i) != winningMark) {
                isMainDiagonal = false;
                break;
            }
        }
        if (isMainDiagonal) {
            for (int i = 0; i < 3; i++) {
                boardButtons[i][i].setBackground(WIN_HIGHLIGHT);
            }
            return;
        }
        
        // Check anti-diagonal
        boolean isAntiDiagonal = true;
        for (int i = 0; i < 3; i++) {
            if (board.getCell(i, 2 - i) != winningMark) {
                isAntiDiagonal = false;
                break;
            }
        }
        if (isAntiDiagonal) {
            for (int i = 0; i < 3; i++) {
                boardButtons[i][2 - i].setBackground(WIN_HIGHLIGHT);
            }
        }
    }
    
    // Resets game to initial state
    private void resetGame() {
        if (computerMoveTimer != null) {
            computerMoveTimer.stop();
        }
        isWaitingForComputer = false;
        engine.reset();
        updateDisplay();
        
        if (engine.isCurrentPlayerComputer()) {
            scheduleComputerMove();
        }
    }
    
    // Main entry point - shows setup dialog and starts GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                // Use default look and feel
            }
            
            GameSetupDialog dialog = new GameSetupDialog();
            if (dialog.isConfirmed()) {
                Player playerX = new HumanPlayer(CellState.X, dialog.getPlayerXName());
                Player playerO = dialog.isComputerOpponent() 
                    ? new ComputerPlayer(CellState.O, "Computer")
                    : new HumanPlayer(CellState.O, dialog.getPlayerOName());
                
                GameEngine engine = new GameEngine(playerX, playerO);
                GameGUI gui = new GameGUI(engine, dialog.getPlayerXName(), 
                    dialog.isComputerOpponent() ? "Computer" : dialog.getPlayerOName());
                gui.setVisible(true);
            }
        });
    }
    
    // Setup dialog for player names and game mode selection
    private static class GameSetupDialog extends JDialog {
        private JTextField playerXField;
        private JTextField playerOField;
        private JRadioButton pvpButton;
        private JRadioButton pvcButton;
        private boolean confirmed = false;
        
        public GameSetupDialog() {
            super((JFrame) null, "Game Setup", true);
            setSize(400, 250);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setResizable(false);
            
            initDialog();
        }
        
        private void initDialog() {
            JPanel panel = new JPanel();
            panel.setBackground(CREAM);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JLabel titleLabel = new JLabel("Tic-Tac-Toe Setup");
            titleLabel.setFont(new Font("Dialog", Font.BOLD, 18));
            titleLabel.setForeground(new Color(100, 80, 120));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(titleLabel);
            panel.add(Box.createVerticalStrut(15));
            
            pvpButton = new JRadioButton("Player vs Player", true);
            pvcButton = new JRadioButton("Player vs Computer");
            ButtonGroup modeGroup = new ButtonGroup();
            modeGroup.add(pvpButton);
            modeGroup.add(pvcButton);
            
            pvpButton.setBackground(CREAM);
            pvcButton.setBackground(CREAM);
            pvpButton.setForeground(new Color(100, 80, 120));
            pvcButton.setForeground(new Color(100, 80, 120));
            
            JPanel modePanel = new JPanel(new FlowLayout());
            modePanel.setBackground(CREAM);
            modePanel.add(pvpButton);
            modePanel.add(pvcButton);
            panel.add(modePanel);
            panel.add(Box.createVerticalStrut(10));
            
            playerXField = new JTextField("Player 1", 15);
            playerOField = new JTextField("Player 2", 15);
            playerXField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SOFT_PURPLE, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            playerOField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SOFT_PURPLE, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            
            JPanel namePanel = new JPanel(new GridLayout(2, 2, 5, 5));
            namePanel.setBackground(CREAM);
            namePanel.add(new JLabel("Player X:"));
            namePanel.add(playerXField);
            namePanel.add(new JLabel("Player O:"));
            namePanel.add(playerOField);
            panel.add(namePanel);
            panel.add(Box.createVerticalStrut(15));
            
            pvcButton.addActionListener(e -> playerOField.setEnabled(false));
            pvpButton.addActionListener(e -> playerOField.setEnabled(true));
            
            JButton startButton = new JButton("Start Game");
            startButton.setFont(new Font("Dialog", Font.BOLD, 14));
            startButton.setBackground(LIGHT_PINK);
            startButton.setForeground(new Color(100, 80, 120));
            startButton.setFocusPainted(false);
            startButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SOFT_PURPLE, 2),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
            ));
            startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            startButton.addActionListener(e -> {
                confirmed = true;
                dispose();
            });
            
            panel.add(startButton);
            add(panel);
        }
        
        public boolean isConfirmed() {
            return confirmed;
        }
        
        public String getPlayerXName() {
            return playerXField.getText().trim().isEmpty() ? "Player 1" : playerXField.getText().trim();
        }
        
        public String getPlayerOName() {
            return playerOField.getText().trim().isEmpty() ? "Player 2" : playerOField.getText().trim();
        }
        
        public boolean isComputerOpponent() {
            return pvcButton.isSelected();
        }
    }
}

