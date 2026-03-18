package br.com.dio.sudoku.ui;

import br.com.dio.sudoku.model.GameDifficulty;
import br.com.dio.sudoku.model.Space;
import br.com.dio.sudoku.service.SudokuGameService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SudokuFrame extends JFrame {

    private final SudokuGameService gameService = new SudokuGameService();
    private final BoardPanel boardPanel = new BoardPanel();

    private JLabel timerLabel;
    private JLabel hintsLabel;
    private JLabel statusLabel;
    private Timer uiTimer;

    private int selectedRow = -1;
    private int selectedCol = -1;

    private static final Color COLOR_PRIMARY = new Color(37, 99, 235);
    private static final Color COLOR_DANGER  = new Color(220, 38, 38);
    private static final Color COLOR_SUCCESS = new Color(22, 163, 74);
    private static final Color COLOR_NEUTRAL = new Color(100, 116, 139);

    public SudokuFrame() {
        setTitle("Sudoku - DIO Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        buildUI();
        setupKeyboardInput();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        showDifficultyDialog();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(15, 15, 15, 15));
        root.setBackground(new Color(248, 250, 252));

        // Header
        root.add(buildHeader(), BorderLayout.NORTH);

        // Board
        boardPanel.setCellClickListener((row, col) -> {
            selectedRow = row;
            selectedCol = col;
            boardPanel.setSelectedCell(row, col);
        });
        root.add(boardPanel, BorderLayout.CENTER);

        // Controls (number pad + buttons)
        root.add(buildControls(), BorderLayout.EAST);

        // Status bar
        root.add(buildStatusBar(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel title = new JLabel("SUDOKU");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(COLOR_PRIMARY);
        panel.add(title, BorderLayout.WEST);

        JPanel info = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        info.setOpaque(false);

        timerLabel = new JLabel("00:00");
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        timerLabel.setForeground(COLOR_NEUTRAL);

        hintsLabel = new JLabel("💡 3");
        hintsLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        info.add(timerLabel);
        info.add(hintsLabel);
        panel.add(info, BorderLayout.EAST);

        // Timer
        uiTimer = new Timer(1000, e -> {
            if (!gameService.isGameOver()) {
                timerLabel.setText(gameService.getElapsedTime());
            }
        });

        return panel;
    }

    private JPanel buildControls() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 10, 0, 0));

        // Number pad
        JPanel numPad = new JPanel(new GridLayout(3, 3, 5, 5));
        numPad.setOpaque(false);
        for (int i = 1; i <= 9; i++) {
            final int num = i;
            JButton btn = createButton(String.valueOf(i), COLOR_PRIMARY);
            btn.setFont(new Font("SansSerif", Font.BOLD, 20));
            btn.setPreferredSize(new Dimension(55, 55));
            btn.addActionListener(e -> handleNumberInput(num));
            numPad.add(btn);
        }
        panel.add(numPad);
        panel.add(Box.createVerticalStrut(10));

        // Action buttons
        JButton clearBtn  = createButton("🗑 Limpar", COLOR_NEUTRAL);
        JButton hintBtn   = createButton("💡 Dica",   new Color(234, 179, 8));
        JButton resetBtn  = createButton("↺ Reiniciar", COLOR_DANGER);
        JButton solveBtn  = createButton("✓ Resolver", COLOR_SUCCESS);
        JButton newBtn    = createButton("+ Novo Jogo", COLOR_PRIMARY);

        Dimension btnSize = new Dimension(140, 38);
        for (JButton btn : new JButton[]{clearBtn, hintBtn, resetBtn, solveBtn, newBtn}) {
            btn.setPreferredSize(btnSize);
            btn.setMaximumSize(btnSize);
            panel.add(btn);
            panel.add(Box.createVerticalStrut(6));
        }

        clearBtn.addActionListener(e -> handleClear());
        hintBtn.addActionListener(e  -> handleHint());
        resetBtn.addActionListener(e -> handleReset());
        solveBtn.addActionListener(e -> handleSolve());
        newBtn.addActionListener(e   -> showDifficultyDialog());

        return panel;
    }

    private JPanel buildStatusBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        statusLabel = new JLabel("Selecione uma célula e digite um número");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        statusLabel.setForeground(COLOR_NEUTRAL);
        panel.add(statusLabel);
        return panel;
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setOpaque(true);
        return btn;
    }

    private void setupKeyboardInput() {
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code >= KeyEvent.VK_1 && code <= KeyEvent.VK_9) {
                    handleNumberInput(code - KeyEvent.VK_0);
                } else if (code == KeyEvent.VK_DELETE || code == KeyEvent.VK_BACK_SPACE) {
                    handleClear();
                } else if (selectedRow >= 0 && selectedCol >= 0) {
                    if (code == KeyEvent.VK_UP    && selectedRow > 0) selectedRow--;
                    if (code == KeyEvent.VK_DOWN  && selectedRow < 8) selectedRow++;
                    if (code == KeyEvent.VK_LEFT  && selectedCol > 0) selectedCol--;
                    if (code == KeyEvent.VK_RIGHT && selectedCol < 8) selectedCol++;
                    boardPanel.setSelectedCell(selectedRow, selectedCol);
                }
            }
        });
    }

    private void handleNumberInput(int num) {
        if (selectedRow < 0 || selectedCol < 0) {
            setStatus("Selecione uma célula primeiro.");
            return;
        }
        Space space = gameService.getBoard().getSpace(selectedRow, selectedCol);
        if (space.isFixed()) {
            setStatus("Célula fixa, não pode ser alterada.");
            return;
        }
        gameService.makeMove(selectedRow, selectedCol, num);
        boardPanel.repaint();
        hintsLabel.setText("💡 " + gameService.getHintsRemaining());

        if (gameService.isWon()) {
            uiTimer.stop();
            setStatus("🎉 Parabéns! Você completou o Sudoku em " + gameService.getElapsedTime() + "!");
            JOptionPane.showMessageDialog(this,
                    "Parabéns! Você completou o Sudoku!\nTempo: " + gameService.getElapsedTime(),
                    "Vitória!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            setStatus(space.getValue() != null ? "Jogada registrada." : "");
        }
    }

    private void handleClear() {
        if (selectedRow < 0 || selectedCol < 0) return;
        gameService.clearCell(selectedRow, selectedCol);
        boardPanel.repaint();
        setStatus("Célula limpa.");
    }

    private void handleHint() {
        if (selectedRow < 0 || selectedCol < 0) {
            setStatus("Selecione uma célula para receber dica.");
            return;
        }
        if (gameService.getHintsRemaining() == 0) {
            setStatus("Sem dicas restantes!");
            return;
        }
        boolean used = gameService.useHint(selectedRow, selectedCol);
        if (used) {
            boardPanel.repaint();
            hintsLabel.setText("💡 " + gameService.getHintsRemaining());
            setStatus("Dica usada! Restam " + gameService.getHintsRemaining() + " dica(s).");
            if (gameService.isWon()) {
                uiTimer.stop();
                JOptionPane.showMessageDialog(this, "Sudoku completo!", "Vitória!", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            setStatus("Dica não disponível para esta célula.");
        }
    }

    private void handleReset() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja reiniciar o tabuleiro?", "Reiniciar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            gameService.resetBoard();
            boardPanel.repaint();
            hintsLabel.setText("💡 " + gameService.getMaxHints());
            uiTimer.start();
            setStatus("Jogo reiniciado.");
        }
    }

    private void handleSolve() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Resolver automaticamente? Você perderá o progresso atual.",
                "Resolver", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            gameService.solveBoard();
            uiTimer.stop();
            boardPanel.repaint();
            setStatus("Tabuleiro resolvido automaticamente.");
        }
    }

    private void showDifficultyDialog() {
        if (uiTimer != null) uiTimer.stop();

        GameDifficulty[] options = GameDifficulty.values();
        String[] labels = new String[options.length];
        for (int i = 0; i < options.length; i++) labels[i] = options[i].getLabel();

        int choice = JOptionPane.showOptionDialog(this,
                "Escolha a dificuldade:",
                "Novo Jogo",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, labels, labels[0]);

        if (choice < 0) choice = 0;
        GameDifficulty diff = options[choice];
        gameService.startNewGame(diff);
        boardPanel.setBoard(gameService.getBoard());
        selectedRow = -1;
        selectedCol = -1;
        hintsLabel.setText("💡 " + gameService.getMaxHints());
        timerLabel.setText("00:00");
        setTitle("Sudoku - DIO Edition | " + diff.getLabel());
        setStatus("Jogo iniciado: " + diff.getLabel());
        uiTimer.start();
    }

    private void setStatus(String msg) {
        statusLabel.setText(msg);
    }

    public static void launch() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new SudokuFrame();
        });
    }
}
