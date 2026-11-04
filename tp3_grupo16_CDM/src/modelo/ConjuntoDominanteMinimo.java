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
        // Copia de los vértices que faltan ser dominados
        Set<Integer> noDominados = new HashSet<>(grafo.obtenerVertices());

        while (!noDominados.isEmpty()) {
            int mejorVertice = -1;
            int maxGanancia = -1;

            // Evaluar cuál vértice no dominado o dominado ofrece la mayor ganancia de cobertura
            for (int v : grafo.obtenerVertices()) {
                int ganancia = calcularGanancia(grafo, v, noDominados);
                
                if (ganancia > maxGanancia) {
                    maxGanancia = ganancia;
                    mejorVertice = v;
                }
            }

            // Si ningún nodo aporta nuevos dominados, finaliza la búsqueda
            if (mejorVertice == -1 || maxGanancia <= 0) {
                break;
            }

            // Seleccionar el vértice goloso
            conjuntoDominante.add(mejorVertice);

            // Marcar como dominados al nodo seleccionado y a todos sus vecinos
            noDominados.remove(mejorVertice);
            for (int vecino : grafo.vecinos(mejorVertice)) {
                noDominados.remove(vecino);
            }
        }

        return conjuntoDominante;
    }

    /**
     * Cuenta cuántos vértices de los actualmente 'no dominados' 
     * serían dominados si se elige el vértice v.
     */
    private static int calcularGanancia(Grafo grafo, int v, Set<Integer> noDominados) {
        int ganancia = 0;
        
        // Suma 1 si el propio vértice v aún no está dominado
        if (noDominados.contains(v)) {
            ganancia++;
        }
        
        // Suma 1 por cada vecino de v que aún no esté dominado
        for (int vecino : grafo.vecinos(v)) {
            if (noDominados.contains(vecino)) {
                ganancia++;
            }
        }
        
        return ganancia;
    }
}