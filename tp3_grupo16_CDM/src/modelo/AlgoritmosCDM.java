package modelo;

import java.util.HashSet;
import java.util.Set;
import grafo.Grafo;

public class AlgoritmosCDM {

    public static Set<Integer> resolverGolosoMayorGrado(Grafo g) throws InterruptedException {
    	Set<Integer> resultado = new HashSet<>();
    	if(Thread.interrupted()) {
    		return resultado;
    	}
        return ConjuntoDominanteMinimo.resolver(g);
    }

    public static Set<Integer> resolverGolosoCobertura(Grafo g) throws InterruptedException {
        Thread.sleep(300);
        
        Set<Integer> resultado = new HashSet<>();
        // TODO: Implementar heurística golosa 2
        if (!g.obtenerVertices().isEmpty()) {
            resultado.add(g.obtenerVertices().iterator().next());
        }
        return resultado;
    }

    public static Set<Integer> resolverBacktracking(Grafo g) throws InterruptedException {
        Thread.sleep(700);
        
        if (Thread.currentThread().isInterrupted()) {
            return null;
        }
        
        Set<Integer> resultado = new HashSet<>();
        // TODO: Implementar Backtracking exacto
        if (!g.obtenerVertices().isEmpty()) {
            resultado.add(g.obtenerVertices().iterator().next());
        }
        return resultado;
    }
}