package br.com.dio.sudoku.model;

public class Board {

    private final Space[][] spaces;
    private static final int SIZE = 9;
    private static final int BOX_SIZE = 3;

    public Board(Space[][] spaces) {
        this.spaces = spaces;
    }

    public Space[][] getSpaces() {
        return spaces;
    }

    public Space getSpace(int row, int col) {
        return spaces[row][col];
    }

    public void setSpaceValue(int row, int col, int value) {
        spaces[row][col].setValue(value);
    }

    public void clearSpace(int row, int col) {
        spaces[row][col].clearValue();
    }

    public boolean isComplete() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (!spaces[row][col].isCorrect()) return false;
            }
        }
        return true;
    }

    public boolean hasErrors() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Space s = spaces[row][col];
                if (s.getStatus() == SpaceStatus.WRONG) return true;
            }
        }
        return false;
    }

    public int countEmpty() {
        int count = 0;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (spaces[row][col].isEmpty()) count++;
            }
        }
        return count;
    }

    public boolean isValidMove(int row, int col, int value) {
        // Check row
        for (int c = 0; c < SIZE; c++) {
            if (c != col && spaces[row][c].getValue() != null && spaces[row][c].getValue() == value) {
                return false;
            }
        }
        // Check column
        for (int r = 0; r < SIZE; r++) {
            if (r != row && spaces[r][col].getValue() != null && spaces[r][col].getValue() == value) {
                return false;
            }
        }
        // Check 3x3 box
        int boxRowStart = (row / BOX_SIZE) * BOX_SIZE;
        int boxColStart = (col / BOX_SIZE) * BOX_SIZE;
        for (int r = boxRowStart; r < boxRowStart + BOX_SIZE; r++) {
            for (int c = boxColStart; c < boxColStart + BOX_SIZE; c++) {
                if ((r != row || c != col) && spaces[r][c].getValue() != null && spaces[r][c].getValue() == value) {
                    return false;
                }
            }
        }
        return true;
    }

    public void reset() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (!spaces[row][col].isFixed()) {
                    spaces[row][col].clearValue();
                }
            }
        }
    }

    public void solve() {
        solveBacktrack(spaces);
    }

    private boolean solveBacktrack(Space[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col].isEmpty()) {
                    for (int num = 1; num <= 9; num++) {
                        board[row][col].setValue(num);
                        if (board[row][col].getStatus() == SpaceStatus.CORRECT) {
                            if (solveBacktrack(board)) return true;
                        }
                        board[row][col].clearValue();
                    }
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("+-------+-------+-------+\n");
        for (int row = 0; row < SIZE; row++) {
            sb.append("| ");
            for (int col = 0; col < SIZE; col++) {
                sb.append(spaces[row][col].toString());
                sb.append(" ");
                if ((col + 1) % BOX_SIZE == 0) sb.append("| ");
            }
            sb.append("\n");
            if ((row + 1) % BOX_SIZE == 0) {
                sb.append("+-------+-------+-------+\n");
            }
        }
        return sb.toString();
    }
}
