package analisadorSintatico;

import java.util.*;

public class Gramatica {
//gramatica criada pelo usuario.
    private Map<String, List<String>> producoes = new LinkedHashMap<>();
    private String simboloInicial;
    private Set<String> terminais    = new LinkedHashSet<>();
    private Set<String> naoTerminais = new LinkedHashSet<>();

    public Gramatica(Map<String, List<String>> producoes, String simboloInicial) {
        this.producoes      = producoes;
        this.simboloInicial = simboloInicial;
        identificarSimbolos();
    }

    // Detecta automaticamente terminais e não-terminais
    private void identificarSimbolos() {
        naoTerminais.addAll(producoes.keySet());

        for (List<String> prods : producoes.values()) {
            for (String prod : prods) {
                if (prod.equals("3")) continue; // ε
                List<String> simbs = quebrar(prod);
                for (String s : simbs) {
                    if (!naoTerminais.contains(s) && !s.equals("3")) {
                        terminais.add(s);
                    }
                }
            }
        }
        terminais.add("$");
    }

  
    public List<String> quebrar(String prod) {
        List<String> lista = new ArrayList<>();

        int i = 0;

        while (i < prod.length()) {

            if (prod.startsWith("id", i)) {
                lista.add("id");
                i += 2;
            } else {
                lista.add(String.valueOf(prod.charAt(i)));
                i++;
            }
        }

        return lista;
    }

    public Map<String, List<String>> getProducoes()  { return producoes; }
    public String getSimboloInicial()                 { return simboloInicial; }
    public Set<String> getTerminais()                 { return terminais; }
    public Set<String> getNaoTerminais()              { return naoTerminais; }
}