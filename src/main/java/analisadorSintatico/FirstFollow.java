package analisadorSintatico;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FirstFollow {
// ver pra apagar isso aqui depois!!!!!!!
	
    //   First e Follow
    private Map<String, Set<String>> first  = new HashMap<>();
    private Map<String, Set<String>> follow = new HashMap<>();

    public FirstFollow() {
        // Inicializa os conjuntos vazios
        String[] naoTerminais = {"S", "A", "B", "C", "D"};
        for (String nt : naoTerminais) {
            first.put(nt, new HashSet<>());
            follow.put(nt, new HashSet<>());
        }

        calcularFirst();
        calcularFollow();
    }

    // FIRST
    private void calcularFirst() {

        // First(D) = {c, id}
        // D -> cd | id
        first.get("D").add("c");
        first.get("D").add("id");

        // First(B) = First(D) = {c, id}
        // B -> DC
        first.get("B").addAll(first.get("D"));

        // First(C) = {e, ε}
        // C -> eDC | ε
        first.get("C").add("e");
        first.get("C").add("ε");

        // First(A) = {v, ε}
        // A -> vBA | ε
        first.get("A").add("v");
        first.get("A").add("ε");

        // First(S) = First(B) = {c, id}
        // S -> BA
        first.get("S").addAll(first.get("B"));
    }

    // lgica do follow
    private void calcularFollow() {

        
        follow.get("S").add("$");

      
        follow.get("B").add("v");        
        follow.get("B").addAll(follow.get("S")); 

      
        follow.get("A").addAll(follow.get("S"));
        

     
        follow.get("D").add("e");        
        follow.get("D").addAll(follow.get("B")); 

        follow.get("C").addAll(follow.get("B"));
     
        follow.get("C").addAll(follow.get("A"));
    }

    public Set<String> getFirst(String naoTerminal) {
        return first.get(naoTerminal);
    }

    public Set<String> getFollow(String naoTerminal) {
        return follow.get(naoTerminal);
    }

    public void imprimir() {
        System.out.println("=".repeat(40));
        System.out.println("CONJUNTOS FIRST E FOLLOW");
        System.out.println("=".repeat(40));
        System.out.printf("%-5s %-20s %-20s%n", "NT", "FIRST", "FOLLOW");
        System.out.println("-".repeat(40));

        for (String nt : new String[]{"S", "A", "B", "C", "D"}) {
            System.out.printf("%-5s %-20s %-20s%n",
                nt,
                first.get(nt).toString(),
                follow.get(nt).toString());
        }
    }
}