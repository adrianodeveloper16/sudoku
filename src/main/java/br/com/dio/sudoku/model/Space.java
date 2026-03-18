package br.com.dio.sudoku.model;

import java.util.Objects;

public class Space {

    private Integer value;
    private final int correctValue;
    private final boolean fixed;
    private SpaceStatus status;

    public Space(int correctValue, boolean fixed) {
        this.correctValue = correctValue;
        this.fixed = fixed;
        if (fixed) {
            this.value = correctValue;
            this.status = SpaceStatus.FIXED;
        } else {
            this.value = null;
            this.status = SpaceStatus.EMPTY;
        }
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        if (fixed) return;
        this.value = value;
        if (value == null) {
            this.status = SpaceStatus.EMPTY;
        } else if (value.equals(correctValue)) {
            this.status = SpaceStatus.CORRECT;
        } else {
            this.status = SpaceStatus.WRONG;
        }
    }

    public void clearValue() {
        if (!fixed) {
            this.value = null;
            this.status = SpaceStatus.EMPTY;
        }
    }

    public int getCorrectValue() {
        return correctValue;
    }

    public boolean isFixed() {
        return fixed;
    }

    public SpaceStatus getStatus() {
        return status;
    }

    public boolean isEmpty() {
        return value == null;
    }

    public boolean isCorrect() {
        return Objects.equals(value, correctValue);
    }

    @Override
    public String toString() {
        if (value == null) return ".";
        return String.valueOf(value);
    }
}
