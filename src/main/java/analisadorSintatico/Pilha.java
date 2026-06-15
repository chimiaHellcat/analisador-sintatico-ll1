package analisadorSintatico;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Pilha {

	private Deque<String> pilha = new ArrayDeque<>();
	

	public void push(String elemento) {

		pilha.push(elemento);
	}

	public String pop() {
		return pilha.pop();
	}

	public String peek() {
		return pilha.peek();
	}

	public boolean isEmpty() {
		return pilha.isEmpty();
	}

	public void inicializar(String simboloIni) {
		pilha.clear();
		pilha.push("$");

		pilha.push(simboloIni);
	}

	public void empilharProducao(String producao) {
		// se tiver ε não empilha nada
		if (producao.equals("3")) {
			return;

		}

		List<String> simbolos = new ArrayList<>();
		
		int i = 0;

		while (i < producao.length()) {

			if (producao.startsWith("id", i)) {
				simbolos.add("id");
				i += 2;
			} else {
				simbolos.add(String.valueOf(producao.charAt(i)));
				i++;
			}
		}	

			for (int j = simbolos.size() - 1; j >= 0; j--) {
				pilha.push(simbolos.get(j));

			}

		

	}

	@Override
	public String toString() {
		return pilha.toString();
	}
}