package br.com.dio.sudoku;

import br.com.dio.sudoku.ui.SudokuFrame;
import br.com.dio.sudoku.ui.SudokuTerminalUI;

public class Main {

    public static void main(String[] args) {
        boolean useTerminal = false;

        for (String arg : args) {
            if ("--terminal".equalsIgnoreCase(arg) || "-t".equalsIgnoreCase(arg)) {
                useTerminal = true;
                break;
            }
        }

        if (useTerminal) {
            new SudokuTerminalUI().start();
        } else {
            SudokuFrame.launch();
        }
    }
}
