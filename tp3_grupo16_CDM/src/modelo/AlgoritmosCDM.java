package modelo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    public static Set<Integer> resolverBacktracking(Grafo g)
            throws InterruptedException {

        
    	List<Integer> vertices = new ArrayList<>(g.obtenerVertices());

        
        Set<Integer> actual = new HashSet<>();

        
        Set<Integer> mejor = new HashSet<>(vertices);

        
        buscarBacktracking(g, vertices, 0, actual, mejor);

        return mejor;
    }
    
    private static void buscarBacktracking(Grafo g, List<Integer> vertices,int indice, Set<Integer> actual,Set<Integer> mejor) 
    		throws InterruptedException {
    	
    	 if (g == null) {
    	        throw new IllegalArgumentException("El grafo no puede ser nulo.");
    	    }
    	 
    	if (Thread.currentThread().isInterrupted()) {
    	    throw new InterruptedException("Backtracking interrumpido");
    	}
    	
    	  if (actual.size() >= mejor.size()) {
    	        return;
    	    }
    	  if (esDominante(g, actual)) {
	            mejor.clear();
	            mejor.addAll(actual);
	            return;
	        }
    	  if (indice == vertices.size()) {
    	        return;
    	    }
    	int vertice = vertices.get(indice);
    	
    	actual.add(vertice);
    	buscarBacktracking(g, vertices, indice + 1, actual, mejor);
    	
    	actual.remove(vertice);
    	buscarBacktracking(g, vertices, indice + 1, actual, mejor);
    }
    
    private static boolean esDominante(Grafo g, Set<Integer> seleccionados) {
    	
        for (int vertice : g.obtenerVertices()) {
        	if(!seleccionados.contains(vertice)) {
        		boolean tieneVecinoSeleccionado = false;
        		for (int verticeVecino : g.vecinos(vertice)) {
        			if(seleccionados.contains(verticeVecino)){
        				tieneVecinoSeleccionado = true;
        				break;
        			}
        		}
        		if (!tieneVecinoSeleccionado) {
                    return false;
                }
        	}
        }
        

        return true;
    }
}