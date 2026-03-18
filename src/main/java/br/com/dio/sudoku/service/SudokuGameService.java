package br.com.dio.sudoku.service;

import br.com.dio.sudoku.model.Board;
import br.com.dio.sudoku.model.GameDifficulty;
import br.com.dio.sudoku.model.Space;
import br.com.dio.sudoku.model.SpaceStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public class SudokuGameService {

    private Board board;
    private GameDifficulty difficulty;
    private LocalDateTime startTime;
    private boolean gameOver;
    private boolean won;
    private int hints;
    private static final int MAX_HINTS = 3;

    private final BoardGeneratorService generatorService = new BoardGeneratorService();

    public void startNewGame(GameDifficulty difficulty) {
        this.difficulty = difficulty;
        this.board = generatorService.generate(difficulty);
        this.startTime = LocalDateTime.now();
        this.gameOver = false;
        this.won = false;
        this.hints = 0;
    }

    public boolean makeMove(int row, int col, int value) {
        if (gameOver) return false;
        Space space = board.getSpace(row, col);
        if (space.isFixed()) return false;

        board.setSpaceValue(row, col, value);

        if (board.isComplete()) {
            gameOver = true;
            won = true;
        }
        return true;
    }

    public boolean clearCell(int row, int col) {
        if (gameOver) return false;
        Space space = board.getSpace(row, col);
        if (space.isFixed()) return false;
        board.clearSpace(row, col);
        return true;
    }

    public boolean useHint(int row, int col) {
        if (hints >= MAX_HINTS || gameOver) return false;
        Space space = board.getSpace(row, col);
        if (space.isFixed() || space.getStatus() == SpaceStatus.CORRECT) return false;
        board.setSpaceValue(row, col, space.getCorrectValue());
        hints++;
        if (board.isComplete()) {
            gameOver = true;
            won = true;
        }
        return true;
    }

    public void solveBoard() {
        board.solve();
        gameOver = true;
        won = false;
    }

    public void resetBoard() {
        board.reset();
        startTime = LocalDateTime.now();
        gameOver = false;
        won = false;
        hints = 0;
    }

    public String getElapsedTime() {
        if (startTime == null) return "00:00";
        Duration duration = Duration.between(startTime, LocalDateTime.now());
        long minutes = duration.toMinutes();
        long seconds = duration.getSeconds() % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public Board getBoard() {
        return board;
    }

    public GameDifficulty getDifficulty() {
        return difficulty;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isWon() {
        return won;
    }

    public int getHintsUsed() {
        return hints;
    }

    public int getMaxHints() {
        return MAX_HINTS;
    }

    public int getHintsRemaining() {
        return MAX_HINTS - hints;
    }
}
