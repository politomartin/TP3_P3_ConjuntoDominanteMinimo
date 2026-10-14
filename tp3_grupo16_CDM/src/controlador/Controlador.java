package controlador;

import java.util.Set;
import grafo.Grafo;

public class Controlador {

    private Grafo grafo;

    public Controlador() {
        this.grafo = new Grafo();
    }

    public int agregarVertice() {
        return grafo.agregarVertice();
    }

    public void eliminarVertice(int v) {
        grafo.eliminarVertice(v);
    }

    public void agregarArista(int i, int j) {
        grafo.agregarArista(i, j);
    }

    public void eliminarArista(int i, int j) {
        grafo.eliminarArista(i, j);
    }

    public Set<Integer> obtenerVertices() {
        return grafo.obtenerVertices();
    }

    public boolean existeArista(int i, int j) {
        return grafo.existeArista(i, j);
    }
}