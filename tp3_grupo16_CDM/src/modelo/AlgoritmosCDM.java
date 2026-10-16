package modelo;

import java.util.HashSet;
import java.util.Set;
import grafo.Grafo;

public class AlgoritmosCDM {

    public static Set<Integer> resolverGolosoMayorGrado(Grafo g) throws InterruptedException {
        // Simulación de tiempo para ver la barra de progreso (borrar cuando implementen el real)
        Thread.sleep(250);
        
        Set<Integer> resultado = new HashSet<>();
        // TODO: Implementar heurística golosa 1
        if (!g.obtenerVertices().isEmpty()) {
            resultado.add(g.obtenerVertices().iterator().next());
        }
        return resultado;
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
        
        Set<Integer> resultado = new HashSet<>();
        // TODO: Implementar Backtracking exacto
        if (!g.obtenerVertices().isEmpty()) {
            resultado.add(g.obtenerVertices().iterator().next());
        }
        return resultado;
    }
}