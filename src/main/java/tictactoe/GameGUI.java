package tictactoe;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// Main GUI frame with menu and game panels using card layout
public class GameGUI extends JFrame {
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 700;
    
    // Professional pink/purple color palette
    private static final Color BG_DARK = new Color(25, 20, 35);
    private static final Color BG_MEDIUM = new Color(40, 30, 50);
    private static final Color BG_LIGHT = new Color(55, 45, 70);
    private static final Color BUTTON_BG = new Color(120, 80, 160);
    private static final Color BUTTON_HOVER = new Color(150, 100, 200);
    private static final Color PINK_X = new Color(255, 105, 180);
    private static final Color CYAN_O = new Color(64, 224, 208);
    private static final Color ACCENT_PURPLE = new Color(180, 120, 220);
    private static final Color ACCENT_PINK = new Color(255, 120, 200);
    private static final Color TEXT_LIGHT = new Color(255, 255, 255);
    private static final Color TEXT_DARK = new Color(200, 180, 220);
    private static final Color WIN_HIGHLIGHT = new Color(255, 105, 180, 180);
    private static final Color FLOWER_PINK = new Color(255, 182, 193);
    private static final Color FLOWER_PURPLE = new Color(221, 160, 221);
    private static final Color STAR_YELLOW = new Color(255, 215, 0);
    
    // Font helper - tries pixel-style fonts, falls back to Monospaced
    private static Font getPixelFont(int style, int size) {
        String[] fontNames = {"Monospaced", "Courier New", "Dialog"};
        for (String name : fontNames) {
            Font font = new Font(name, style, size);
            if (font.getFamily().equals(name) || name.equals("Dialog")) {
                return font;
            }
        }
        return new Font("Monospaced", style, size);
    }
    
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    
    public GameGUI() {
        setTitle("Tic-Tac-Toe");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BG_DARK);
        
        menuPanel = new MenuPanel(this);
        gamePanel = new GamePanel(this);
        
        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(gamePanel, "GAME");
        
        add(mainPanel);
        showMenu();
    }
    
    public void showMenu() {
        cardLayout.show(mainPanel, "MENU");
    }
    
    public void startGame(String playerXName, String playerOName, boolean vsComputer, Difficulty difficulty) {
        Player playerX = new HumanPlayer(CellState.X, playerXName);
        Player playerO = vsComputer 
            ? new ComputerPlayer(CellState.O, "Computer", difficulty)
            : new HumanPlayer(CellState.O, playerOName);
        
        GameEngine engine = new GameEngine(playerX, playerO);
        gamePanel.startNewGame(engine, playerXName, vsComputer ? "Computer" : playerOName, difficulty);
        cardLayout.show(mainPanel, "GAME");
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel
        }
        
        SwingUtilities.invokeLater(() -> {
            GameGUI gui = new GameGUI();
            gui.setVisible(true);
        });
    }
    
    // Draws pixel art flower using fillRect
    private static void drawPixelFlower(Graphics2D g2, int x, int y, int pixelSize) {
        Color petalColor = FLOWER_PINK;
        Color centerColor = new Color(255, 215, 0);
        int[][] flower = {
            {0,0,1,1,1,1,0,0},
            {0,1,1,1,1,1,1,0},
            {1,1,1,2,2,1,1,1},
            {1,1,2,2,2,2,1,1},
            {1,1,2,2,2,2,1,1},
            {1,1,1,2,2,1,1,1},
            {0,1,1,1,1,1,1,0},
            {0,0,1,1,1,1,0,0}
        };
        for (int i = 0; i < flower.length; i++) {
            for (int j = 0; j < flower[i].length; j++) {
                if (flower[i][j] == 1) {
                    g2.setColor(petalColor);
                    g2.fillRect(x + j * pixelSize, y + i * pixelSize, pixelSize, pixelSize);
                } else if (flower[i][j] == 2) {
                    g2.setColor(centerColor);
                    g2.fillRect(x + j * pixelSize, y + i * pixelSize, pixelSize, pixelSize);
                }
            }
        }
    }
    
    // Draws pixel art star
    private static void drawPixelStar(Graphics2D g2, int x, int y, int pixelSize) {
        g2.setColor(STAR_YELLOW);
        int[][] star = {
            {0,0,0,1,0,0,0},
            {0,0,1,1,1,0,0},
            {0,1,1,1,1,1,0},
            {1,1,1,1,1,1,1},
            {0,1,1,1,1,1,0},
            {0,0,1,1,1,0,0},
            {0,0,0,1,0,0,0}
        };
        for (int i = 0; i < star.length; i++) {
            for (int j = 0; j < star[i].length; j++) {
                if (star[i][j] == 1) {
                    g2.fillRect(x + j * pixelSize, y + i * pixelSize, pixelSize, pixelSize);
                }
            }
        }
    }
    
    // Draws pixel art plant
    private static void drawPixelPlant(Graphics2D g2, int x, int y, int pixelSize) {
        Color stemColor = new Color(34, 139, 34);
        Color leafColor = new Color(144, 238, 144);
        int[][] plant = {
            {0,0,0,1,0,0,0},
            {0,0,0,1,0,0,0},
            {0,0,1,1,1,0,0},
            {0,1,2,1,2,1,0},
            {1,2,2,1,2,2,1},
            {0,1,2,1,2,1,0},
            {0,0,1,1,1,0,0},
            {0,0,0,1,0,0,0},
            {0,0,0,1,0,0,0}
        };
        for (int i = 0; i < plant.length; i++) {
            for (int j = 0; j < plant[i].length; j++) {
                if (plant[i][j] == 1) {
                    g2.setColor(stemColor);
                    g2.fillRect(x + j * pixelSize, y + i * pixelSize, pixelSize, pixelSize);
                } else if (plant[i][j] == 2) {
                    g2.setColor(leafColor);
                    g2.fillRect(x + j * pixelSize, y + i * pixelSize, pixelSize, pixelSize);
                }
            }
        }
    }
    
    // Draws pixel art red rose
    private static void drawPixelRose(Graphics2D g2, int x, int y, int pixelSize) {
        Color roseColor = new Color(220, 20, 60);
        Color stemColor = new Color(34, 139, 34);
        Color leafColor = new Color(144, 238, 144);
        int[][] rose = {
            {0,0,0,3,3,0,0,0},
            {0,0,3,1,1,3,0,0},
            {0,3,1,1,1,1,3,0},
            {3,1,1,1,1,1,1,3},
            {3,1,1,1,1,1,1,3},
            {0,3,1,1,1,1,3,0},
            {0,0,3,1,1,3,0,0},
            {0,0,0,2,2,0,0,0},
            {0,0,0,2,2,0,0,0},
            {0,0,0,2,2,0,0,0},
            {0,0,2,2,2,2,0,0},
            {0,0,0,2,2,0,0,0}
        };
        for (int i = 0; i < rose.length; i++) {
            for (int j = 0; j < rose[i].length; j++) {
                if (rose[i][j] == 1) {
                    g2.setColor(roseColor);
                    g2.fillRect(x + j * pixelSize, y + i * pixelSize, pixelSize, pixelSize);
                } else if (rose[i][j] == 2) {
                    g2.setColor(stemColor);
                    g2.fillRect(x + j * pixelSize, y + i * pixelSize, pixelSize, pixelSize);
                } else if (rose[i][j] == 3) {
                    g2.setColor(leafColor);
                    g2.fillRect(x + j * pixelSize, y + i * pixelSize, pixelSize, pixelSize);
                }
            }
        }
    }
    
    // Draws pixel art sunflower
    private static void drawPixelSunflower(Graphics2D g2, int x, int y, int pixelSize) {
        Color petalColor = new Color(255, 215, 0);
        Color centerColor = new Color(139, 69, 19);
        Color stemColor = new Color(34, 139, 34);
        int[][] sunflower = {
            {0,0,1,1,1,1,0,0},
            {0,1,1,1,1,1,1,0},
            {1,1,1,2,2,1,1,1},
            {1,1,2,2,2,2,1,1},
            {1,1,2,2,2,2,1,1},
            {1,1,1,2,2,1,1,1},
            {0,1,1,1,1,1,1,0},
            {0,0,1,1,1,1,0,0},
            {0,0,0,3,3,0,0,0},
            {0,0,0,3,3,0,0,0}
        };
        for (int i = 0; i < sunflower.length; i++) {
            for (int j = 0; j < sunflower[i].length; j++) {
                if (sunflower[i][j] == 1) {
                    g2.setColor(petalColor);
                    g2.fillRect(x + j * pixelSize, y + i * pixelSize, pixelSize, pixelSize);
                } else if (sunflower[i][j] == 2) {
                    g2.setColor(centerColor);
                    g2.fillRect(x + j * pixelSize, y + i * pixelSize, pixelSize, pixelSize);
                } else if (sunflower[i][j] == 3) {
                    g2.setColor(stemColor);
                    g2.fillRect(x + j * pixelSize, y + i * pixelSize, pixelSize, pixelSize);
                }
            }
        }
    }
    
    // Draws pixel art lily
    private static void drawPixelLily(Graphics2D g2, int x, int y, int pixelSize) {
        Color lilyColor = new Color(255, 240, 245);
        Color stemColor = new Color(34, 139, 34);
        int[][] lily = {
            {0,0,0,1,0,0,0},
            {0,0,1,1,1,0,0},
            {0,1,1,1,1,1,0},
            {1,1,1,1,1,1,1},
            {0,1,1,1,1,1,0},
            {0,0,1,1,1,0,0},
            {0,0,0,2,0,0,0},
            {0,0,0,2,0,0,0},
            {0,0,0,2,0,0,0}
        };
        for (int i = 0; i < lily.length; i++) {
            for (int j = 0; j < lily[i].length; j++) {
                if (lily[i][j] == 1) {
                    g2.setColor(lilyColor);
                    g2.fillRect(x + j * pixelSize, y + i * pixelSize, pixelSize, pixelSize);
                } else if (lily[i][j] == 2) {
                    g2.setColor(stemColor);
                    g2.fillRect(x + j * pixelSize, y + i * pixelSize, pixelSize, pixelSize);
                }
            }
        }
    }
    
    // Draws small pixel art sparkle
    private static void drawPixelSparkle(Graphics2D g2, int x, int y, int pixelSize, Color color) {
        g2.setColor(color);
        int[][] sparkle = {
            {0,1,0},
            {1,1,1},
            {0,1,0}
        };
        for (int i = 0; i < sparkle.length; i++) {
            for (int j = 0; j < sparkle[i].length; j++) {
                if (sparkle[i][j] == 1) {
                    g2.fillRect(x + j * pixelSize, y + i * pixelSize, pixelSize, pixelSize);
                }
            }
        }
    }
    
    // Draws small pixel art grass tuft
    private static void drawPixelGrass(Graphics2D g2, int x, int y, int pixelSize) {
        Color grassColor = new Color(124, 252, 0);
        int[][] grass = {
            {0,1,0},
            {1,1,1},
            {0,1,0}
        };
        for (int i = 0; i < grass.length; i++) {
            for (int j = 0; j < grass[i].length; j++) {
                if (grass[i][j] == 1) {
                    g2.setColor(grassColor);
                    g2.fillRect(x + j * pixelSize, y + i * pixelSize, pixelSize, pixelSize);
                }
            }
        }
    }
    
    // Menu panel for game mode selection
    private static class MenuPanel extends JPanel {
        private final GameGUI parent;
        private JRadioButton pvpButton;
        private JRadioButton pvcButton;
        private PlaceholderTextField playerXField;
        private PlaceholderTextField playerOField;
        private JButton easyButton, mediumButton, hardButton;
        private Difficulty selectedDifficulty = Difficulty.MEDIUM;
        private JButton startButton;
        private final Random decorationRandom = new Random(42);
        
        public MenuPanel(GameGUI parent) {
            this.parent = parent;
            setBackground(BG_DARK);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(40, 60, 40, 60));
            setOpaque(true);
            
            JLabel title = new JLabel("Tic-Tac-Toe");
            title.setFont(getPixelFont(Font.BOLD, 38));
            title.setForeground(TEXT_LIGHT);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(title);
            add(Box.createVerticalStrut(40));
            
            pvpButton = createRadioButton("Player vs Player", true);
            pvcButton = createRadioButton("Player vs Computer", false);
            ButtonGroup modeGroup = new ButtonGroup();
            modeGroup.add(pvpButton);
            modeGroup.add(pvcButton);
            
            JPanel modePanel = new JPanel(new FlowLayout());
            modePanel.setBackground(BG_DARK);
            modePanel.add(pvpButton);
            modePanel.add(pvcButton);
            add(modePanel);
            add(Box.createVerticalStrut(30));
            
            playerXField = new PlaceholderTextField("Enter Player X name", 20);
            styleTextField(playerXField);
            playerOField = new PlaceholderTextField("Enter Player O name", 20);
            styleTextField(playerOField);
            
            JPanel namePanel = new JPanel(new GridLayout(2, 2, 15, 15));
            namePanel.setBackground(BG_DARK);
            JLabel labelX = createLabel("Player X:");
            JLabel labelO = createLabel("Player O:");
            namePanel.add(labelX);
            namePanel.add(playerXField);
            namePanel.add(labelO);
            namePanel.add(playerOField);
            add(namePanel);
            add(Box.createVerticalStrut(20));
            
            JPanel difficultyPanel = new JPanel(new FlowLayout());
            difficultyPanel.setBackground(BG_DARK);
            JLabel diffLabel = createLabel("Difficulty:");
            difficultyPanel.add(diffLabel);
            
            easyButton = createDifficultyButton("Easy", Difficulty.EASY);
            mediumButton = createDifficultyButton("Medium", Difficulty.MEDIUM);
            hardButton = createDifficultyButton("Hard", Difficulty.HARD);
            
            difficultyPanel.add(easyButton);
            difficultyPanel.add(mediumButton);
            difficultyPanel.add(hardButton);
            add(difficultyPanel);
            updateDifficultyButtons();
            
            pvcButton.addActionListener(e -> updateModeSelection());
            pvpButton.addActionListener(e -> updateModeSelection());
            
            add(Box.createVerticalStrut(30));
            
            startButton = createRoundedButton("Start Game", ACCENT_PURPLE);
            startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            startButton.addActionListener(e -> handleStart());
            add(startButton);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            
            Random rand = decorationRandom;
            int pixelSize = 3 + rand.nextInt(2);
            
            drawPixelRose(g2, 30 + rand.nextInt(40), 40 + rand.nextInt(30), pixelSize);
            drawPixelSunflower(g2, getWidth() - 80 + rand.nextInt(30), 50 + rand.nextInt(40), pixelSize);
            drawPixelLily(g2, 60 + rand.nextInt(50), getHeight() - 120 + rand.nextInt(40), pixelSize);
            drawPixelFlower(g2, getWidth() - 70 + rand.nextInt(40), getHeight() - 100 + rand.nextInt(30), pixelSize);
            drawPixelStar(g2, 100 + rand.nextInt(60), 80 + rand.nextInt(50), 2 + rand.nextInt(2));
            drawPixelStar(g2, getWidth() - 120 + rand.nextInt(50), 100 + rand.nextInt(60), 2 + rand.nextInt(2));
            drawPixelSparkle(g2, 150 + rand.nextInt(80), getHeight() - 150 + rand.nextInt(50), 2, new Color(255, 255, 200));
            drawPixelSparkle(g2, getWidth() - 100 + rand.nextInt(60), getHeight() - 80 + rand.nextInt(40), 2, CYAN_O);
            drawPixelGrass(g2, 40 + rand.nextInt(50), getHeight() - 60 + rand.nextInt(30), 2);
            drawPixelGrass(g2, getWidth() - 60 + rand.nextInt(40), getHeight() - 50 + rand.nextInt(30), 2);
            drawPixelPlant(g2, 20 + rand.nextInt(30), getHeight() - 140 + rand.nextInt(50), pixelSize);
            drawPixelStar(g2, getWidth() - 50 + rand.nextInt(40), 200 + rand.nextInt(100), 2 + rand.nextInt(2));
            
            g2.dispose();
        }
        
        private JLabel createLabel(String text) {
            JLabel label = new JLabel(text);
            label.setForeground(TEXT_LIGHT);
            label.setFont(getPixelFont(Font.PLAIN, 14));
            return label;
        }
        
        private JRadioButton createRadioButton(String text, boolean selected) {
            JRadioButton button = new JRadioButton(text, selected);
            button.setBackground(BG_DARK);
            button.setForeground(TEXT_LIGHT);
            button.setFont(getPixelFont(Font.PLAIN, 14));
            button.setOpaque(true);
            return button;
        }
        
        private void styleTextField(JTextField field) {
            field.setBackground(BG_LIGHT);
            field.setForeground(TEXT_LIGHT);
            field.setFont(getPixelFont(Font.PLAIN, 14));
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_PURPLE, 2),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
            ));
        }
        
        private JButton createDifficultyButton(String text, Difficulty diff) {
            JButton button = new JButton(text);
            button.setFont(getPixelFont(Font.BOLD, 12));
            button.setPreferredSize(new Dimension(80, 35));
            button.addActionListener(e -> {
                selectedDifficulty = diff;
                updateDifficultyButtons();
            });
            return button;
        }
        
        private void updateDifficultyButtons() {
            Color selectedBg = ACCENT_PURPLE;
            Color unselectedBg = new Color(50, 40, 65);
            Color selectedText = TEXT_LIGHT;
            Color unselectedText = new Color(20, 15, 35);
            
            easyButton.setBackground(selectedDifficulty == Difficulty.EASY ? selectedBg : unselectedBg);
            mediumButton.setBackground(selectedDifficulty == Difficulty.MEDIUM ? selectedBg : unselectedBg);
            hardButton.setBackground(selectedDifficulty == Difficulty.HARD ? selectedBg : unselectedBg);
            easyButton.setForeground(selectedDifficulty == Difficulty.EASY ? selectedText : unselectedText);
            mediumButton.setForeground(selectedDifficulty == Difficulty.MEDIUM ? selectedText : unselectedText);
            hardButton.setForeground(selectedDifficulty == Difficulty.HARD ? selectedText : unselectedText);
            easyButton.setOpaque(true);
            mediumButton.setOpaque(true);
            hardButton.setOpaque(true);
            easyButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(selectedDifficulty == Difficulty.EASY ? ACCENT_PINK : new Color(100, 80, 120), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            mediumButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(selectedDifficulty == Difficulty.MEDIUM ? ACCENT_PINK : new Color(100, 80, 120), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            hardButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(selectedDifficulty == Difficulty.HARD ? ACCENT_PINK : new Color(100, 80, 120), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        }
        
        private void updateModeSelection() {
            boolean vsComputer = pvcButton.isSelected();
            playerOField.setEnabled(!vsComputer);
            playerOField.setVisible(!vsComputer);
        }
        
        private JButton createRoundedButton(String text, Color bg) {
            JButton button = new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            button.setFont(getPixelFont(Font.BOLD, 18));
            button.setBackground(bg);
            button.setForeground(TEXT_LIGHT);
            button.setFocusPainted(false);
            button.setOpaque(false);
            button.setBorder(new EmptyBorder(15, 40, 15, 40));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return button;
        }
        
        private void handleStart() {
            String playerXName = playerXField.isEmpty() ? "Player 1" : playerXField.getText().trim();
            String playerOName = playerOField.isEmpty() ? "Player 2" : playerOField.getText().trim();
            boolean vsComputer = pvcButton.isSelected();
            
            if (playerXName.isEmpty()) playerXName = "Player 1";
            if (!vsComputer && playerOName.isEmpty()) playerOName = "Player 2";
            
            parent.startGame(playerXName, playerOName, vsComputer, selectedDifficulty);
        }
    }
    
    // Game panel with board and controls
    private static class GamePanel extends JPanel {
        private final GameGUI parent;
        private GameEngine engine;
        private String playerXName, playerOName;
        private Difficulty difficulty;
        private JButton[][] boardButtons;
        private JLabel statusLabel, scoreLabel, difficultyLabel;
        private JButton newGameButton, menuButton;
        private int xWins = 0, oWins = 0, draws = 0;
        private SwingWorker<Move, Void> aiWorker;
        private final Random decorationRandom = new Random(123);
        
        public GamePanel(GameGUI parent) {
            this.parent = parent;
            setBackground(BG_DARK);
            setLayout(new BorderLayout(15, 15));
            setBorder(new EmptyBorder(20, 20, 20, 20));
        }
        
        public void startNewGame(GameEngine engine, String playerXName, String playerOName, Difficulty difficulty) {
            this.engine = engine;
            this.playerXName = playerXName;
            this.playerOName = playerOName;
            this.difficulty = difficulty;
            xWins = oWins = draws = 0;
            
            removeAll();
            createComponents();
            updateDisplay();
            
            if (engine.isCurrentPlayerComputer()) {
                scheduleComputerMove();
            }
            
            revalidate();
            repaint();
        }
        
        private void createComponents() {
            JPanel topPanel = new JPanel();
            topPanel.setBackground(BG_DARK);
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
            
            statusLabel = new JLabel("", SwingConstants.CENTER);
            statusLabel.setFont(getPixelFont(Font.BOLD, 26));
            statusLabel.setForeground(TEXT_LIGHT);
            
            scoreLabel = new JLabel("", SwingConstants.CENTER);
            scoreLabel.setFont(getPixelFont(Font.PLAIN, 16));
            scoreLabel.setForeground(TEXT_DARK);
            
            difficultyLabel = new JLabel("", SwingConstants.CENTER);
            difficultyLabel.setFont(getPixelFont(Font.PLAIN, 14));
            difficultyLabel.setForeground(ACCENT_PURPLE);
            
            topPanel.add(statusLabel);
            topPanel.add(Box.createVerticalStrut(5));
            topPanel.add(scoreLabel);
            topPanel.add(Box.createVerticalStrut(5));
            topPanel.add(difficultyLabel);
            
            JPanel boardPanel = new JPanel();
            boardPanel.setBackground(BG_DARK);
            boardPanel.setLayout(new GridLayout(3, 3, 12, 12));
            boardPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
            
            boardButtons = new JButton[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    boardButtons[i][j] = createBoardButton(i, j);
                    boardPanel.add(boardButtons[i][j]);
                }
            }
            
            JPanel bottomPanel = new JPanel(new FlowLayout());
            bottomPanel.setBackground(BG_DARK);
            newGameButton = createRoundedButton("New Game", ACCENT_PURPLE);
            menuButton = createRoundedButton("Back to Menu", BG_LIGHT);
            bottomPanel.add(newGameButton);
            bottomPanel.add(menuButton);
            
            newGameButton.addActionListener(e -> resetGame());
            menuButton.addActionListener(e -> parent.showMenu());
            
            add(topPanel, BorderLayout.NORTH);
            add(boardPanel, BorderLayout.CENTER);
            add(bottomPanel, BorderLayout.SOUTH);
        }
        
        private JButton createBoardButton(int row, int col) {
            JButton button = new JButton("") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            button.setFont(getPixelFont(Font.BOLD, 60));
            button.setBackground(BG_LIGHT);
            button.setForeground(TEXT_LIGHT);
            button.setOpaque(false);
            button.setBorder(new EmptyBorder(10, 10, 10, 10));
            button.setFocusPainted(false);
            
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (button.isEnabled() && button.getText().isEmpty()) {
                        button.setBackground(BUTTON_HOVER);
                        button.repaint();
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    if (button.getText().isEmpty()) {
                        button.setBackground(BG_LIGHT);
                        button.repaint();
                    }
                }
            });
            
            button.addActionListener(e -> handleCellClick(row, col));
            return button;
        }
        
        private JButton createRoundedButton(String text, Color bg) {
            JButton button = new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            button.setFont(getPixelFont(Font.BOLD, 14));
            button.setBackground(bg);
            button.setForeground(TEXT_LIGHT);
            button.setFocusPainted(false);
            button.setOpaque(false);
            button.setBorder(new EmptyBorder(10, 25, 10, 25));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return button;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            
            Random rand = decorationRandom;
            int pixelSize = 3 + rand.nextInt(2);
            
            drawPixelRose(g2, 20 + rand.nextInt(50), 30 + rand.nextInt(40), pixelSize);
            drawPixelSunflower(g2, getWidth() - 90 + rand.nextInt(40), 50 + rand.nextInt(50), pixelSize);
            drawPixelLily(g2, 40 + rand.nextInt(60), getHeight() - 130 + rand.nextInt(50), pixelSize);
            drawPixelFlower(g2, getWidth() - 70 + rand.nextInt(50), getHeight() - 110 + rand.nextInt(40), pixelSize);
            drawPixelStar(g2, 80 + rand.nextInt(70), 60 + rand.nextInt(60), 2 + rand.nextInt(2));
            drawPixelStar(g2, getWidth() - 100 + rand.nextInt(60), 80 + rand.nextInt(70), 2 + rand.nextInt(2));
            drawPixelSparkle(g2, 120 + rand.nextInt(90), getHeight() - 160 + rand.nextInt(60), 2, PINK_X);
            drawPixelSparkle(g2, getWidth() - 80 + rand.nextInt(70), getHeight() - 90 + rand.nextInt(50), 2, CYAN_O);
            drawPixelGrass(g2, 30 + rand.nextInt(60), getHeight() - 70 + rand.nextInt(40), 2);
            drawPixelGrass(g2, getWidth() - 70 + rand.nextInt(50), getHeight() - 60 + rand.nextInt(40), 2);
            drawPixelPlant(g2, 25 + rand.nextInt(40), getHeight() - 160 + rand.nextInt(60), pixelSize);
            drawPixelStar(g2, getWidth() - 60 + rand.nextInt(50), 180 + rand.nextInt(120), 2 + rand.nextInt(2));
            drawPixelSparkle(g2, 200 + rand.nextInt(100), 150 + rand.nextInt(80), 2, TEXT_LIGHT);
            
            g2.dispose();
        }
        
        private void handleCellClick(int row, int col) {
            if (engine.getState() != GameState.PLAYING || engine.isCurrentPlayerComputer()) {
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
        
        private void scheduleComputerMove() {
            statusLabel.setText("Computer is thinking...");
            statusLabel.repaint();
            
            if (aiWorker != null && !aiWorker.isDone()) {
                aiWorker.cancel(true);
            }
            
            aiWorker = new SwingWorker<Move, Void>() {
                @Override
                protected Move doInBackground() throws Exception {
                    int delay = difficulty == Difficulty.EASY ? 300 : difficulty == Difficulty.MEDIUM ? 600 : 1000;
                    Thread.sleep(delay);
                    return engine.getComputerMove();
                }
                
                @Override
                protected void done() {
                    try {
                        Move move = get();
                        if (move != null && engine.processMove(move)) {
                            updateDisplay();
                        }
                    } catch (Exception e) {
                        // Handle exception
                    }
                }
            };
            aiWorker.execute();
        }
        
        private void updateDisplay() {
            updateBoard();
            updateStatus();
            updateScore();
            updateDifficultyLabel();
            updateButtonStates();
            
            if (engine.getState() != GameState.PLAYING) {
                highlightWinningLine();
            }
        }
        
        private void updateBoard() {
            Board board = engine.getBoard();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    CellState cell = board.getCell(i, j);
                    JButton button = boardButtons[i][j];
                    
                    if (cell == CellState.X) {
                        button.setText("X");
                        button.setForeground(PINK_X);
                        button.setBackground(BG_LIGHT);
                        button.repaint();
                    } else if (cell == CellState.O) {
                        button.setText("O");
                        button.setForeground(CYAN_O);
                        button.setBackground(BG_LIGHT);
                        button.repaint();
                    } else {
                        button.setText("");
                        button.setBackground(BG_LIGHT);
                        button.repaint();
                    }
                }
            }
        }
        
        private void updateStatus() {
            GameState state = engine.getState();
            
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
        
        private void updateScore() {
            scoreLabel.setText(String.format("%s: %d  |  %s: %d  |  Draws: %d", 
                playerXName, xWins, playerOName, oWins, draws));
        }
        
        private void updateDifficultyLabel() {
            if (playerOName.equals("Computer")) {
                String diffText = "Difficulty: " + difficulty.toString();
                difficultyLabel.setText(diffText);
            } else {
                difficultyLabel.setText("");
            }
        }
        
        private void updateButtonStates() {
            boolean enabled = engine.getState() == GameState.PLAYING && !engine.isCurrentPlayerComputer();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    Board board = engine.getBoard();
                    boolean isEmpty = board.getCell(i, j) == CellState.EMPTY;
                    boardButtons[i][j].setEnabled(enabled && isEmpty);
                }
            }
        }
        
        private void highlightWinningLine() {
            GameState state = engine.getState();
            if (state != GameState.X_WINS && state != GameState.O_WINS) {
                return;
            }
            
            CellState winningMark = (state == GameState.X_WINS) ? CellState.X : CellState.O;
            Board board = engine.getBoard();
            
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
                        boardButtons[i][j].setBackground(new Color(WIN_HIGHLIGHT.getRed(), WIN_HIGHLIGHT.getGreen(), WIN_HIGHLIGHT.getBlue()));
                        boardButtons[i][j].repaint();
                    }
                    return;
                }
            }
            
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
                        boardButtons[i][j].setBackground(new Color(WIN_HIGHLIGHT.getRed(), WIN_HIGHLIGHT.getGreen(), WIN_HIGHLIGHT.getBlue()));
                        boardButtons[i][j].repaint();
                    }
                    return;
                }
            }
            
            boolean isMainDiagonal = true;
            for (int i = 0; i < 3; i++) {
                if (board.getCell(i, i) != winningMark) {
                    isMainDiagonal = false;
                    break;
                }
            }
            if (isMainDiagonal) {
                for (int i = 0; i < 3; i++) {
                    boardButtons[i][i].setBackground(new Color(WIN_HIGHLIGHT.getRed(), WIN_HIGHLIGHT.getGreen(), WIN_HIGHLIGHT.getBlue()));
                    boardButtons[i][i].repaint();
                }
                return;
            }
            
            boolean isAntiDiagonal = true;
            for (int i = 0; i < 3; i++) {
                if (board.getCell(i, 2 - i) != winningMark) {
                    isAntiDiagonal = false;
                    break;
                }
            }
            if (isAntiDiagonal) {
                for (int i = 0; i < 3; i++) {
                    boardButtons[i][2 - i].setBackground(new Color(WIN_HIGHLIGHT.getRed(), WIN_HIGHLIGHT.getGreen(), WIN_HIGHLIGHT.getBlue()));
                    boardButtons[i][2 - i].repaint();
                }
            }
        }
        
        private void resetGame() {
            if (aiWorker != null && !aiWorker.isDone()) {
                aiWorker.cancel(true);
            }
            engine.reset();
            updateDisplay();
            if (engine.isCurrentPlayerComputer()) {
                scheduleComputerMove();
            }
        }
    }
}

