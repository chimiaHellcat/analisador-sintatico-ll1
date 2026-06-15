package analisadorSintatico;

import java.util.LinkedHashMap;
import java.util.Map;

public class TabelaParsing {
// teste rever ou apagar depoiis
    private Map<String, Map<String, String>> tabela = new LinkedHashMap<>();
    private FirstFollow ff;

    public TabelaParsing() {
        ff = new FirstFollow();

        // Inicializa as linhas da tabela
        String[] naoTerminais = {"S", "A", "B", "C", "D"};
        String[] terminais = {"id", "v", "e", "c", "$"};

        for (String nt : naoTerminais) {
            tabela.put(nt, new LinkedHashMap<>());
            for (String t : terminais) {
                tabela.get(nt).put(t, "");
            }
        }

        construirTabela();
    }

    private void construirTabela() {

        //  S -> BA 

        for (String t : ff.getFirst("B")) {
            if (!t.equals("ε")) {
                tabela.get("S").put(t, "BA");
            }
        }

        // A -> vBA 
        // First(vBA) = {v}
        tabela.get("A").put("v", "vBA");

        //  A -> 3 
        // epsilon em First(A),  usar Follow(A) = {$}
        for (String t : ff.getFollow("A")) {
            tabela.get("A").put(t, "3"); // 3 = ε
        }

        // B -> DC 
        // First(DC) = First(D) = {id, c}
        for (String t : ff.getFirst("D")) {
            if (!t.equals("ε")) {
                tabela.get("B").put(t, "DC");
            }
        }

        // C -> eDC 
        // First(eDC) = {e}
        tabela.get("C").put("e", "eDC");

        // C -> ε 
    
        for (String t : ff.getFollow("C")) {
            tabela.get("C").put(t, "3"); // 3 = ε
        }

        // D -> cd 
      
        tabela.get("D").put("c", "cD");

        //  D -> id 
     
        tabela.get("D").put("id", "id");
    }

    public Map<String, Map<String, String>> getTabela() {
        return tabela;
    }

    // ==================== IMPRIMIR ====================
    public void imprimir() {
        String[] terminais = {"id", "v", "e", "c", "$"};
        String[] naoTerminais = {"S", "A", "B", "C", "D"};

        System.out.println("TABELA DE PARSING (gerada automaticamente)");
        System.out.println("=".repeat(50));

        // Cabeçalho
        System.out.printf("%-5s", "");
        for (String t : terminais) {
            System.out.printf("| %-6s", t);
        }
        System.out.println("|");
        System.out.println("-".repeat(45));

        // Linhas
        for (String nt : naoTerminais) {
            System.out.printf("%-5s", nt);
            Map<String, String> linha = tabela.get(nt);
            for (String t : terminais) {
                String valor = linha.getOrDefault(t, "");
                // Exibe ε no lugar de 3
                System.out.printf("| %-6s", valor.equals("3") ? "ε" : valor);
            }
            System.out.println("|");
        }
    }
}