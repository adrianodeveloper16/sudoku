package br.com.dio.sudoku.ui;

import br.com.dio.sudoku.model.Board;
import br.com.dio.sudoku.model.Space;
import br.com.dio.sudoku.model.SpaceStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardPanel extends JPanel {

    private static final int CELL_SIZE = 60;
    private static final int BOARD_SIZE = CELL_SIZE * 9;
    private static final int PADDING = 5;

    private static final Color COLOR_BG = new Color(250, 250, 255);
    private static final Color COLOR_FIXED = new Color(30, 30, 30);
    private static final Color COLOR_USER = new Color(30, 100, 220);
    private static final Color COLOR_ERROR = new Color(220, 40, 40);
    private static final Color COLOR_HINT = new Color(0, 150, 80);
    private static final Color COLOR_SELECTED = new Color(193, 218, 255);
    private static final Color COLOR_SAME_NUM = new Color(220, 235, 255);
    private static final Color COLOR_GRID_THIN = new Color(180, 180, 200);
    private static final Color COLOR_GRID_THICK = new Color(50, 50, 80);
    private static final Color COLOR_FIXED_BG = new Color(235, 235, 245);

    private Board board;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private CellClickListener clickListener;

    public BoardPanel() {
        setPreferredSize(new Dimension(BOARD_SIZE + PADDING * 2, BOARD_SIZE + PADDING * 2));
        setBackground(COLOR_BG);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = (e.getX() - PADDING) / CELL_SIZE;
                int row = (e.getY() - PADDING) / CELL_SIZE;
                if (row >= 0 && row < 9 && col >= 0 && col < 9) {
                    selectedRow = row;
                    selectedCol = col;
                    repaint();
                    if (clickListener != null) {
                        clickListener.onCellClick(row, col);
                    }
                }
            }
        });
    }

    public void setBoard(Board board) {
        this.board = board;
        selectedRow = -1;
        selectedCol = -1;
        repaint();
    }

    public void setCellClickListener(CellClickListener listener) {
        this.clickListener = listener;
    }

    public void setSelectedCell(int row, int col) {
        this.selectedRow = row;
        this.selectedCol = col;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (board == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Space[][] spaces = board.getSpaces();
        int selectedValue = (selectedRow >= 0 && selectedCol >= 0 && spaces[selectedRow][selectedCol].getValue() != null)
                ? spaces[selectedRow][selectedCol].getValue() : -1;

        // Draw cells
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int x = PADDING + col * CELL_SIZE;
                int y = PADDING + row * CELL_SIZE;
                Space s = spaces[row][col];

                // Background
                if (row == selectedRow && col == selectedCol) {
                    g2.setColor(COLOR_SELECTED);
                } else if (s.isFixed()) {
                    g2.setColor(COLOR_FIXED_BG);
                } else if (selectedValue > 0 && s.getValue() != null && s.getValue() == selectedValue) {
                    g2.setColor(COLOR_SAME_NUM);
                } else if (row == selectedRow || col == selectedCol
                        || (selectedRow >= 0 && selectedCol >= 0
                            && row / 3 == selectedRow / 3 && col / 3 == selectedCol / 3)) {
                    g2.setColor(new Color(240, 240, 250));
                } else {
                    g2.setColor(Color.WHITE);
                }
                g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                // Number
                if (!s.isEmpty()) {
                    if (s.isFixed()) {
                        g2.setColor(COLOR_FIXED);
                        g2.setFont(new Font("SansSerif", Font.BOLD, 26));
                    } else if (s.getStatus() == SpaceStatus.WRONG) {
                        g2.setColor(COLOR_ERROR);
                        g2.setFont(new Font("SansSerif", Font.PLAIN, 26));
                    } else if (s.getStatus() == SpaceStatus.CORRECT) {
                        g2.setColor(COLOR_USER);
                        g2.setFont(new Font("SansSerif", Font.PLAIN, 26));
                    } else {
                        g2.setColor(COLOR_HINT);
                        g2.setFont(new Font("SansSerif", Font.PLAIN, 26));
                    }
                    String val = String.valueOf(s.getValue());
                    FontMetrics fm = g2.getFontMetrics();
                    int tx = x + (CELL_SIZE - fm.stringWidth(val)) / 2;
                    int ty = y + (CELL_SIZE - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(val, tx, ty);
                }
            }
        }

        // Draw grid lines
        for (int i = 0; i <= 9; i++) {
            if (i % 3 == 0) {
                g2.setColor(COLOR_GRID_THICK);
                g2.setStroke(new BasicStroke(2.5f));
            } else {
                g2.setColor(COLOR_GRID_THIN);
                g2.setStroke(new BasicStroke(0.8f));
            }
            int pos = PADDING + i * CELL_SIZE;
            g2.drawLine(PADDING, pos, PADDING + BOARD_SIZE, pos);
            g2.drawLine(pos, PADDING, pos, PADDING + BOARD_SIZE);
        }
    }

    public interface CellClickListener {
        void onCellClick(int row, int col);
    }
}
