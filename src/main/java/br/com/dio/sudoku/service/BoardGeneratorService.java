package br.com.dio.sudoku.service;

import br.com.dio.sudoku.model.Board;
import br.com.dio.sudoku.model.GameDifficulty;
import br.com.dio.sudoku.model.Space;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoardGeneratorService {

    private static final int SIZE = 9;
    private static final int BOX_SIZE = 3;

    public Board generate(GameDifficulty difficulty) {
        int[][] solution = generateSolution();
        int[][] puzzle = createPuzzle(solution, difficulty.getEmptyCells());

        Space[][] spaces = new Space[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                boolean fixed = puzzle[row][col] != 0;
                spaces[row][col] = new Space(solution[row][col], fixed);
            }
        }
        return new Board(spaces);
    }

    private int[][] generateSolution() {
        int[][] board = new int[SIZE][SIZE];
        fillBoard(board, 0, 0);
        return board;
    }

    private boolean fillBoard(int[][] board, int row, int col) {
        if (row == SIZE) return true;
        int nextRow = (col == SIZE - 1) ? row + 1 : row;
        int nextCol = (col == SIZE - 1) ? 0 : col + 1;

        List<Integer> nums = new ArrayList<>();
        for (int i = 1; i <= 9; i++) nums.add(i);
        Collections.shuffle(nums);

        for (int num : nums) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num;
                if (fillBoard(board, nextRow, nextCol)) return true;
                board[row][col] = 0;
            }
        }
        return false;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int c = 0; c < SIZE; c++) {
            if (board[row][c] == num) return false;
        }
        for (int r = 0; r < SIZE; r++) {
            if (board[r][col] == num) return false;
        }
        int boxRow = (row / BOX_SIZE) * BOX_SIZE;
        int boxCol = (col / BOX_SIZE) * BOX_SIZE;
        for (int r = boxRow; r < boxRow + BOX_SIZE; r++) {
            for (int c = boxCol; c < boxCol + BOX_SIZE; c++) {
                if (board[r][c] == num) return false;
            }
        }
        return true;
    }

    private int[][] createPuzzle(int[][] solution, int emptyCells) {
        int[][] puzzle = new int[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            System.arraycopy(solution[r], 0, puzzle[r], 0, SIZE);
        }

        List<int[]> positions = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                positions.add(new int[]{r, c});
            }
        }
        Collections.shuffle(positions);

        int removed = 0;
        for (int[] pos : positions) {
            if (removed >= emptyCells) break;
            puzzle[pos[0]][pos[1]] = 0;
            removed++;
        }
        return puzzle;
    }
}
