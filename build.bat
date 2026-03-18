@echo off
echo === Compilando Sudoku DIO Edition ===
mkdir out 2>nul
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt
if %errorlevel% == 0 (
    echo === Compilacao OK. Gerando JAR ===
    cd out
    echo Main-Class: br.com.dio.sudoku.Main > manifest.txt
    jar cfm ..\sudoku-dio.jar manifest.txt br\
    cd ..
    echo === JAR gerado: sudoku-dio.jar ===
    echo.
    echo Para rodar com interface grafica:
    echo   java -jar sudoku-dio.jar
    echo.
    echo Para rodar no terminal:
    echo   java -jar sudoku-dio.jar --terminal
) else (
    echo ERRO na compilacao.
)
