package modelo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import grafo.Grafo;

public class ConjuntoDominanteMinimo {

    public static Set<Integer> resolver(Grafo grafo) {
        if (grafo == null) {
            throw new IllegalArgumentException("El grafo no puede ser nulo.");
        }

        if (grafo.tamano() == 0) {
            return Collections.emptySet();
        }

        Set<Integer> conjuntoDominante = new HashSet<>();
       
        Set<Integer> noDominados = new HashSet<>(grafo.obtenerVertices());

        while (!noDominados.isEmpty()) {
            int mejorVertice = -1;
            int maxGanancia = -1;

           
            for (int v : grafo.obtenerVertices()) {
                int ganancia = calcularGanancia(grafo, v, noDominados);
                
                if (ganancia > maxGanancia) {
                    maxGanancia = ganancia;
                    mejorVertice = v;
                }
            }

         
            if (mejorVertice == -1 || maxGanancia <= 0) {
                break;
            }

            
            conjuntoDominante.add(mejorVertice);
            
            noDominados.remove(mejorVertice);
            for (int vecino : grafo.vecinos(mejorVertice)) {
                noDominados.remove(vecino);
            }
        }

        return conjuntoDominante;
    }
 
    private static int calcularGanancia(Grafo grafo, int v, Set<Integer> noDominados) {
        int ganancia = 0;
       
        if (noDominados.contains(v)) {
            ganancia++;
        }
        
        for (int vecino : grafo.vecinos(v)) {
            if (noDominados.contains(vecino)) {
                ganancia++;
            }
        }
        
        return ganancia;
    }
}