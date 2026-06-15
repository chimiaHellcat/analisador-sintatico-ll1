# Analisador Sintatico Preditivo LL(1)

Trabalho da disciplina de Compiladores.

## Requisitos

- Java 21 ou superior

## Execucao

Para executar sem instalar Maven:

1. Abra a pasta `entrega-professor`.
2. Execute `executar.bat`.
3. Acesse:

```text
http://localhost:8080/analisador_sintatico.html
```

## Gerar o JAR novamente

No Windows:

```bat
.\mvnw.cmd clean package
```

Depois atualize o arquivo:

```text
entrega-professor/analisador-sintatico-ll1.jar
```

usando o JAR gerado em:

```text
target/analisadorSintatico-0.0.1-SNAPSHOT.jar
```
