@echo off
setlocal
title Analisador Sintatico LL(1)

where java >nul 2>nul
if errorlevel 1 (
    echo Java nao foi encontrado.
    echo Instale o Java 21 ou superior e execute este arquivo novamente.
    echo.
    pause
    exit /b 1
)

echo Iniciando o Analisador Sintatico LL(1)...
echo.
echo Se o navegador nao abrir automaticamente, acesse:
echo http://localhost:8080/analisador_sintatico.html
echo.

start "" /b powershell -NoProfile -WindowStyle Hidden -Command "Start-Sleep -Seconds 4; Start-Process 'http://localhost:8080/analisador_sintatico.html'"
java -jar "%~dp0analisador-sintatico-ll1.jar"

echo.
echo Aplicacao encerrada.
pause
