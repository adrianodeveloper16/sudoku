package br.com.dio.sudoku.model;

public enum GameDifficulty {
    EASY(20, "Fácil"),
    MEDIUM(35, "Médio"),
    HARD(50, "Difícil"),
    EXPERT(60, "Expert");

    private final int emptyCells;
    private final String label;

    GameDifficulty(int emptyCells, String label) {
        this.emptyCells = emptyCells;
        this.label = label;
    }

    public int getEmptyCells() {
        return emptyCells;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
