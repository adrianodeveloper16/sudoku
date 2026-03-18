package br.com.dio.sudoku.ui;

import br.com.dio.sudoku.model.Board;
import br.com.dio.sudoku.model.GameDifficulty;
import br.com.dio.sudoku.model.Space;
import br.com.dio.sudoku.model.SpaceStatus;
import br.com.dio.sudoku.service.SudokuGameService;

import java.util.Scanner;

public class SudokuTerminalUI {

    private final SudokuGameService gameService = new SudokuGameService();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        printWelcome();
        GameDifficulty difficulty = chooseDifficulty();
        gameService.startNewGame(difficulty);
        gameLoop();
    }

    private void printWelcome() {
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║     SUDOKU - DIO Edition     ║");
        System.out.println("╚══════════════════════════════╝");
        System.out.println();
    }

    private GameDifficulty chooseDifficulty() {
        System.out.println("Escolha a dificuldade:");
        System.out.println("  1 - Fácil");
        System.out.println("  2 - Médio");
        System.out.println("  3 - Difícil");
        System.out.println("  4 - Expert");
        System.out.print("Opção: ");

        while (true) {
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1": return GameDifficulty.EASY;
                case "2": return GameDifficulty.MEDIUM;
                case "3": return GameDifficulty.HARD;
                case "4": return GameDifficulty.EXPERT;
                default:
                    System.out.print("Opção inválida. Tente novamente: ");
            }
        }
    }

    private void gameLoop() {
        while (!gameService.isGameOver()) {
            printBoard();
            printStatus();
            printMenu();
            handleInput();
        }
        printBoard();
        if (gameService.isWon()) {
            System.out.println("\n🎉 Parabéns! Você completou o Sudoku!");
            System.out.println("Tempo total: " + gameService.getElapsedTime());
        } else {
            System.out.println("\nJogo encerrado.");
        }
    }

    private void printBoard() {
        Board board = gameService.getBoard();
        Space[][] spaces = board.getSpaces();
        System.out.println("\n     1   2   3   4   5   6   7   8   9");
        System.out.println("   ┌───────────┬───────────┬───────────┐");
        for (int row = 0; row < 9; row++) {
            System.out.print((row + 1) + "  │");
            for (int col = 0; col < 9; col++) {
                Space s = spaces[row][col];
                String val = s.isEmpty() ? "." : String.valueOf(s.getValue());
                if (s.getStatus() == SpaceStatus.WRONG) {
                    System.out.print(" \033[31m" + val + "\033[0m ");
                } else if (s.isFixed()) {
                    System.out.print(" \033[1m" + val + "\033[0m ");
                } else if (s.getStatus() == SpaceStatus.CORRECT) {
                    System.out.print(" \033[34m" + val + "\033[0m ");
                } else {
                    System.out.print(" " + val + " ");
                }
                if ((col + 1) % 3 == 0) System.out.print("│");
                else System.out.print(" ");
            }
            System.out.println();
            if ((row + 1) % 3 == 0 && row < 8) {
                System.out.println("   ├───────────┼───────────┼───────────┤");
            }
        }
        System.out.println("   └───────────┴───────────┴───────────┘");
    }

    private void printStatus() {
        System.out.printf("Dificuldade: %s | Tempo: %s | Dicas: %d/%d%n",
                gameService.getDifficulty(),
                gameService.getElapsedTime(),
                gameService.getHintsUsed(),
                gameService.getMaxHints());
    }

    private void printMenu() {
        System.out.println("\nComandos:");
        System.out.println("  [linha] [coluna] [valor]  - Inserir valor (ex: 1 3 5)");
        System.out.println("  c [linha] [coluna]        - Limpar célula (ex: c 1 3)");
        System.out.println("  h [linha] [coluna]        - Dica para célula (ex: h 1 3)");
        System.out.println("  r                         - Reiniciar");
        System.out.println("  s                         - Resolver automaticamente");
        System.out.println("  q                         - Sair");
        System.out.print("> ");
    }

    private void handleInput() {
        String line = scanner.nextLine().trim().toLowerCase();
        if (line.isEmpty()) return;

        if (line.equals("q")) {
            gameService.solveBoard();
            return;
        }
        if (line.equals("r")) {
            gameService.resetBoard();
            return;
        }
        if (line.equals("s")) {
            System.out.println("Resolvendo...");
            gameService.solveBoard();
            return;
        }

        String[] parts = line.split("\\s+");
        try {
            if (parts[0].equals("c") && parts.length == 3) {
                int row = Integer.parseInt(parts[1]) - 1;
                int col = Integer.parseInt(parts[2]) - 1;
                if (!gameService.clearCell(row, col)) {
                    System.out.println("Não é possível limpar essa célula.");
                }
            } else if (parts[0].equals("h") && parts.length == 3) {
                int row = Integer.parseInt(parts[1]) - 1;
                int col = Integer.parseInt(parts[2]) - 1;
                if (!gameService.useHint(row, col)) {
                    System.out.println("Dica não disponível (sem dicas restantes ou célula já correta).");
                }
            } else if (parts.length == 3) {
                int row = Integer.parseInt(parts[0]) - 1;
                int col = Integer.parseInt(parts[1]) - 1;
                int val = Integer.parseInt(parts[2]);
                if (val < 1 || val > 9) {
                    System.out.println("Valor deve ser entre 1 e 9.");
                } else if (!gameService.makeMove(row, col, val)) {
                    System.out.println("Movimento inválido (célula fixa).");
                }
            } else {
                System.out.println("Comando inválido. Siga o formato correto.");
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Entrada inválida. Tente novamente.");
        }
    }
}
