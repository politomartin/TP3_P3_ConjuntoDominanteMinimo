package grafo;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Grafo {

    private Map<Integer, HashSet<Integer>> _vecinos;
    private int _proximoId;

    public Grafo() {
        _vecinos = new HashMap<>();
        _proximoId = 0;
    }

    public Grafo(int n) {
        this();
        if (n < 0) {
            throw new IllegalArgumentException("La cantidad de vértices no puede ser negativa: " + n);
        }
        for (int i = 0; i < n; i++) {
            agregarVertice();
        }
    }

    public int agregarVertice() {
        int idNuevo = _proximoId;
        _proximoId++;
        _vecinos.put(idNuevo, new HashSet<>());
        return idNuevo;
    }

    public void eliminarVertice(int v) {
        verificarVertice(v);

        for (Integer vecino : _vecinos.get(v)) {
            _vecinos.get(vecino).remove(v);
        }

        _vecinos.remove(v);
    }

    public void agregarArista(int i, int j) {
        verificarVertice(i);
        verificarVertice(j);
        verificarDistintos(i, j);

        if (existeArista(i, j)) {
            throw new IllegalArgumentException("La arista entre " + i + " y " + j + " ya existe.");
        }

        _vecinos.get(i).add(j);
        _vecinos.get(j).add(i);
    }

    public void eliminarArista(int i, int j) {
        verificarVertice(i);
        verificarVertice(j);

        if (!existeArista(i, j)) {
            throw new IllegalArgumentException("La arista entre " + i + " y " + j + " no existe.");
        }

        _vecinos.get(i).remove(j);
        _vecinos.get(j).remove(i);
    }

    public boolean existeArista(int i, int j) {
        verificarVertice(i);
        verificarVertice(j);
        return _vecinos.get(i).contains(j);
    }

    public Set<Integer> obtenerVertices() {
        return Collections.unmodifiableSet(_vecinos.keySet());
    }

    public Set<Integer> vecinos(int v) {
        verificarVertice(v);
        return Collections.unmodifiableSet(_vecinos.get(v));
    }
    
    public boolean existeVertice(int v) {
    	return _vecinos.containsKey(v);
    }

    public int tamano() {
        return _vecinos.size();
    }

    private void verificarVertice(int v) {
        if (!_vecinos.containsKey(v)) {
            throw new IllegalArgumentException("El vértice " + v + " no existe en el grafo.");
        }
    }

    private void verificarDistintos(int i, int j) {
        if (i == j) {
            throw new IllegalArgumentException("No se permiten bucles (aristas de un vértice a sí mismo): " + i);
        }
    }
}