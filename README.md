# 🎮 Sudoku - DIO Edition

Jogo de Sudoku em Java, desafio da trilha de java da DIO.  
Combina a lógica do modo terminal (`main` branch) com a interface gráfica Swing (`ui` branch).

---

## 📁 Estrutura do Projeto

```
sudoku/
├── pom.xml
└── src/main/java/br/com/dio/sudoku/
    ├── Main.java                          ← Ponto de entrada
    ├── model/
    │   ├── Board.java                     ← Tabuleiro 9x9
    │   ├── Space.java                     ← Célula individual
    │   ├── SpaceStatus.java               ← Status da célula (FIXED, CORRECT, WRONG, EMPTY)
    │   └── GameDifficulty.java            ← Dificuldades (Fácil → Expert)
    ├── service/
    │   ├── BoardGeneratorService.java     ← Geração aleatória de tabuleiros
    │   └── SudokuGameService.java         ← Lógica do jogo (movimentos, dicas, timer)
    └── ui/
        ├── SudokuTerminalUI.java          ← Interface no terminal (modo texto)
        ├── SudokuFrame.java               ← Janela principal Swing (GUI)
        └── BoardPanel.java               ← Componente gráfico do tabuleiro
```

---

## 🚀 Como Compilar e Executar

### Pré-requisitos
- Java 11+
- Maven 3.6+

### Compilar
```bash
cd sudoku
mvn clean package
```

### Executar (Interface Gráfica - padrão)
```bash
java -jar target/sudoku-dio.jar
```

### Executar (Terminal)
```bash
java -jar target/sudoku-dio.jar --terminal
```

---

## 🖥️ Interface Gráfica (Swing)

- Clique em uma célula para selecioná-la
- Digite números de 1-9 ou clique no teclado virtual
- Use as setas do teclado para navegar
- **Delete / Backspace** para limpar uma célula
- Botões disponíveis: Limpar, Dica, Reiniciar, Resolver, Novo Jogo

### Cores no tabuleiro
| Cor | Significado |
|-----|-------------|
| Cinza escuro (negrito) | Valor fixo (gerado pelo jogo) |
| Azul | Valor inserido correto |
| Vermelho | Valor inserido incorreto |
| Verde | Valor revelado por dica |

---

## ⌨️ Interface Terminal

### Comandos
| Comando | Ação |
|---------|------|
| `1 3 5` | Insere o valor 5 na linha 1, coluna 3 |
| `c 1 3` | Limpa a célula linha 1, coluna 3 |
| `h 1 3` | Usa uma dica na célula linha 1, coluna 3 |
| `r` | Reinicia o tabuleiro |
| `s` | Resolve automaticamente |
| `q` | Sai do jogo |

---

## 🎯 Dificuldades

| Nível | Células Vazias |
|-------|----------------|
| Fácil | 20 |
| Médio | 35 |
| Difícil | 50 |
| Expert | 60 |

---

## 🔧 Funcionalidades

- [x] Geração aleatória de tabuleiros válidos (backtracking)
- [x] 4 níveis de dificuldade
- [x] Validação em tempo real (linha, coluna, quadrante)
- [x] Sistema de dicas (máx. 3 por partida)
- [x] Resolução automática
- [x] Timer
- [x] Interface terminal colorida (ANSI)
- [x] Interface gráfica Swing
- [x] Navegação por teclado (setas) na GUI

---

## 📚 Referências

- [DIO Sudoku - Terminal](https://github.com/digitalinnovationone/sudoku)
- [DIO Sudoku - UI Branch](https://github.com/digitalinnovationone/sudoku/tree/ui)
# sudoku
