package analisadorSintatico;

import java.util.*;

public class TabelaDinamica {

    private Map<String, Map<String, String>> tabela = new LinkedHashMap<>();
    private Gramatica g;
    private FirstFollowDinamico ff;

    public TabelaDinamica(Gramatica g, FirstFollowDinamico ff) {
        this.g  = g;
        this.ff = ff;

        for (String nt : g.getNaoTerminais()) {
            tabela.put(nt, new LinkedHashMap<>());
            for (String t : g.getTerminais()) {
                tabela.get(nt).put(t, "");
            }
        }
        construir();
    }

    private void construir() {
        for (String nt : g.getNaoTerminais()) {
            for (String prod : g.getProducoes().get(nt)) {
                if (prod.equals("3")) {
                    // A -> ε: usa Follow(A)
                    for (String t : ff.getFollow().get(nt)) {
                        tabela.get(nt).put(t, "3");
                    }
                } else {
                    // Para cada terminal em First(prod)
                    List<String> simbs = g.quebrar(prod);
                    Set<String> fp = firstDeProd(simbs);
                    for (String t : fp) {
                        if (!t.equals("ε")) {
                            tabela.get(nt).put(t, prod);
                        }
                    }
                    // Se ε em First(prod), usa Follow(nt)
                    if (fp.contains("ε")) {
                        for (String t : ff.getFollow().get(nt)) {
                            tabela.get(nt).put(t, "3");
                        }
                    }
                }
            }
        }
    }

    private Set<String> firstDeProd(List<String> simbs) {
        Set<String> res = new LinkedHashSet<>();
        for (String s : simbs) {
            if (g.getNaoTerminais().contains(s)) {
                Set<String> fS = new LinkedHashSet<>(ff.getFirst().get(s));
                fS.remove("ε");
                res.addAll(fS);
                if (!ff.getFirst().get(s).contains("ε")) return res;
            } else {
                res.add(s);
                return res;
            }
        }
        res.add("ε");
        return res;
    }

    public Map<String, Map<String, String>> getTabela() { return tabela; }
}