package analisadorSintatico;

import java.util.*;

public class FirstFollowDinamico {
//usar esse!
	
	// first follow "automatizado cai criar sentenças a partira da classe Gramatica.
	// oonde o USER define as sentenças.
	
    private Gramatica g;
    private Map<String, Set<String>> first  = new LinkedHashMap<>();
    private Map<String, Set<String>> follow = new LinkedHashMap<>();

    public FirstFollowDinamico(Gramatica g) {
        this.g = g;
        for (String nt : g.getNaoTerminais()) {
            first.put(nt,  new LinkedHashSet<>());
            follow.put(nt, new LinkedHashSet<>());
        }
        calcularFirst();
        calcularFollow();
    }

    private void calcularFirst() {
        boolean mudou = true;
        while (mudou) {
            mudou = false;
            for (String nt : g.getNaoTerminais()) {
                for (String prod : g.getProducoes().get(nt)) {
                    if (prod.equals("3")) {
                        mudou |= first.get(nt).add("ε");
                    } else {
                        List<String> simbs = g.quebrar(prod);
                        for (String s : simbs) {
                            if (g.getNaoTerminais().contains(s)) {
                                Set<String> fS = new LinkedHashSet<>(first.get(s));
                                fS.remove("ε");
                                mudou |= first.get(nt).addAll(fS);
                                if (!first.get(s).contains("ε")) break;
                            } else {
                                mudou |= first.get(nt).add(s);
                                break;
                            }
                        }
                       
                        boolean todosEps = simbs.stream()
                            .allMatch(s -> g.getNaoTerminais().contains(s)
                                && first.get(s).contains("ε"));
                        if (todosEps) mudou |= first.get(nt).add("ε");
                    }
                }
            }
        }
    }

    private void calcularFollow() {
 
        follow.get(g.getSimboloInicial()).add("$");

        boolean mudou = true;
        while (mudou) {
            mudou = false;
            for (String nt : g.getNaoTerminais()) {
                for (String prod : g.getProducoes().get(nt)) {
                    if (prod.equals("3")) continue;
                    List<String> simbs = g.quebrar(prod);
                    for (int i = 0; i < simbs.size(); i++) {
                        String s = simbs.get(i);
                        if (!g.getNaoTerminais().contains(s)) continue;

                      
                        List<String> resto = simbs.subList(i + 1, simbs.size());
                        Set<String> firstResto = firstDe(resto);

                        Set<String> semEps = new LinkedHashSet<>(firstResto);
                        semEps.remove("ε");
                        mudou |= follow.get(s).addAll(semEps);

                      
                        if (firstResto.contains("ε") || resto.isEmpty()) {
                            mudou |= follow.get(s).addAll(follow.get(nt));
                        }
                    }
                }
            }
        }
    }

    private Set<String> firstDe(List<String> simbs) {
        Set<String> resultado = new LinkedHashSet<>();
        if (simbs.isEmpty()) { resultado.add("ε"); return resultado; }
        for (String s : simbs) {
            if (g.getNaoTerminais().contains(s)) {
                Set<String> fS = new LinkedHashSet<>(first.get(s));
                fS.remove("ε");
                resultado.addAll(fS);
                if (!first.get(s).contains("ε")) return resultado;
            } else {
                resultado.add(s);
                return resultado;
            }
        }
        resultado.add("ε");
        return resultado;
    }

    public Map<String, Set<String>> getFirst()  { return first; }
    public Map<String, Set<String>> getFollow() { return follow; }
}