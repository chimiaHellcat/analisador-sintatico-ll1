package analisadorSintatico;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AnalisadorSintaticoApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void aceitaSentencaCompactaValida() {
		Map<String, Object> resposta = processarSentenca("dadacbcacbcacac");
		List<Map<String, String>> passos = (List<Map<String, String>>) resposta.get("passos");

		assertEquals("aceito", passos.get(passos.size() - 1).get("tipo"));
	}

	@Test
	void rejeitaSentencaCompactaInvalida() {
		Map<String, Object> resposta = processarSentenca("dadacbcacbcacaa");
		List<Map<String, String>> passos = (List<Map<String, String>>) resposta.get("passos");

		assertEquals("erro", passos.get(passos.size() - 1).get("tipo"));
	}

	private Map<String, Object> processarSentenca(String sentenca) {
		Map<String, List<String>> producoes = new LinkedHashMap<>();
		producoes.put("S", List.of("dA", "cA"));
		producoes.put("A", List.of("aB", "c"));
		producoes.put("B", List.of("dA", "cC", "3"));
		producoes.put("C", List.of("bS", "aC", "c"));

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("simboloInicial", "S");
		body.put("producoes", producoes);
		body.put("sentenca", sentenca);

		return new Controller().processar(body);
	}

}
