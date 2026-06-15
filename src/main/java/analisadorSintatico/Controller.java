package analisadorSintatico;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class Controller {

    // ======= Endpoint principal =======
    @PostMapping("/processar")
    public Map<String, Object> processar(@RequestBody Map<String, Object> body) {

        // Recebe as produções
        Map<String, List<String>> prodsRaw =
            (Map<String, List<String>>) body.get("producoes");
        String simboloInicial = (String) body.get("simboloInicial");
        String sentenca       = (String) body.get("sentenca");

        
        Gramatica g   = new Gramatica(prodsRaw, simboloInicial);
        FirstFollowDinamico ff = new FirstFollowDinamico(g);
        TabelaDinamica td      = new TabelaDinamica(g, ff);

        
        List<Map<String, String>> passos = analisar(sentenca, td, g);

        
        Map<String, Object> resposta = new LinkedHashMap<>();
        resposta.put("first",    ff.getFirst());
        resposta.put("follow",   ff.getFollow());
        resposta.put("tabela",   td.getTabela());
        resposta.put("terminais",   g.getTerminais());
        resposta.put("naoTerminais", g.getNaoTerminais());
        resposta.put("passos",   passos);
        return resposta;
    }

    private List<Map<String, String>> analisar(
            String sentenca,
            TabelaDinamica td,
            Gramatica g) {

        String[] tokens = tokenizarSentenca(sentenca, g);
        String[] entrada = Arrays.copyOf(tokens, tokens.length + 1);
        entrada[tokens.length] = "$";

        Pilha pilha = new Pilha();
        pilha.inicializar(g.getSimboloInicial());

        List<Map<String, String>> passos = new ArrayList<>();
        int pos = 0, contador = 0;

        while (!pilha.isEmpty()) {
            contador++;
            String topo  = pilha.peek();
            String token = entrada[pos];

            StringBuilder entStr = new StringBuilder();
            for (int i = pos; i < entrada.length; i++)
                entStr.append(entrada[i]).append(" ");

            Map<String, String> passo = new LinkedHashMap<>();
            passo.put("pilha",  pilha.toString());
            passo.put("entrada", entStr.toString().trim());

            if (topo.equals(token)) {
                if (token.equals("$")) {
                    passo.put("acao", "OK em " + contador);
                    passo.put("tipo", "aceito");
                    passos.add(passo);
                    break;
                }
                passo.put("acao", "Lê: " + token);
                passo.put("tipo", "le");
                pilha.pop(); pos++;

            } else if (td.getTabela().get(topo) != null
                    && td.getTabela().get(topo).get(token) != null
                    && !td.getTabela().get(topo).get(token).isEmpty()) {

                String prod  = td.getTabela().get(topo).get(token);
                String exib  = prod.equals("3") ? topo + " → ε" : topo + " → " + prod;
                passo.put("acao", exib);
                passo.put("tipo", prod.equals("3") ? "eps" : "expand");
                pilha.pop();
                pilha.empilharProducao(prod);

            } else {
                passo.put("acao", "ERRO em " + contador);
                passo.put("tipo", "erro");
                passos.add(passo);
                break;
            }
            passos.add(passo);
            if (contador > 500) break;
        }
        return passos;
    }

    private String[] tokenizarSentenca(String sentenca, Gramatica g) {
        if (sentenca == null || sentenca.trim().isEmpty()) {
            return new String[0];
        }

        String texto = sentenca.trim();
        if (texto.matches(".*\\s+.*")) {
            return texto.split("\\s+");
        }

        return g.quebrar(texto).toArray(new String[0]);
    }
}
