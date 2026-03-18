#!/bin/bash
echo "=== Compilando Sudoku DIO Edition ==="
mkdir -p out
find src -name "*.java" > sources.txt
javac -d out @sources.txt
if [ $? -eq 0 ]; then
    echo "=== Compilação OK. Gerando JAR ==="
    cd out
    echo "Main-Class: br.com.dio.sudoku.Main" > manifest.txt
    jar cfm ../sudoku-dio.jar manifest.txt br/
    cd ..
    echo "=== JAR gerado: sudoku-dio.jar ==="
    echo ""
    echo "Para rodar com interface gráfica:"
    echo "  java -jar sudoku-dio.jar"
    echo ""
    echo "Para rodar no terminal:"
    echo "  java -jar sudoku-dio.jar --terminal"
else
    echo "ERRO na compilação."
fi
