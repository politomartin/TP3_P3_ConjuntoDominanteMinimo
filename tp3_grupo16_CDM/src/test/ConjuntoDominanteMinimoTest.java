package test;

import modelo.ConjuntoDominanteMinimo;

import grafo.Grafo;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Set;

public class ConjuntoDominanteMinimoTest {

    private ConjuntoDominanteMinimo solver;

    @Before
    public void setUp() {
        solver = new ConjuntoDominanteMinimo();
    }

    @Test
    public void testGrafoVacioRetornaConjuntoVacio() {
        Grafo g = new Grafo();
        Set<Integer> dominante = solver.resolver(g);

        assertNotNull(dominante);
        assertTrue("El conjunto dominante de un grafo vacío debe ser vacío", dominante.isEmpty());
    }

    @Test
    public void testVerticeAisladoEsDominante() {
        Grafo g = new Grafo();
        int v0 = g.agregarVertice(); // Retorna 0

        Set<Integer> dominante = solver.resolver(g);

        assertEquals(1, dominante.size());
        assertTrue(dominante.contains(v0));
    }

    @Test
    public void testGrafoEstrellaEligeNodoCentral() {
        Grafo g = new Grafo();
        
        // v0 será el centro de la estrella
        int centro = g.agregarVertice(); 
        
        // Hojas
        int h1 = g.agregarVertice();
        int h2 = g.agregarVertice();
        int h3 = g.agregarVertice();

        g.agregarArista(centro, h1);
        g.agregarArista(centro, h2);
        g.agregarArista(centro, h3);

        Set<Integer> dominante = solver.resolver(g);

        assertEquals("En una estrella, el conjunto dominante mínimo tiene tamaño 1", 1, dominante.size());
        assertTrue("Debe seleccionar el nodo central", dominante.contains(centro));
    }

    @Test
    public void testGrafoCompletoRetornaUnSoloNodo() {
        Grafo g = new Grafo();
        int v0 = g.agregarVertice();
        int v1 = g.agregarVertice();
        int v2 = g.agregarVertice();
        int v3 = g.agregarVertice();

        // K4: Todos conectados entre sí
        g.agregarArista(v0, v1); g.agregarArista(v0, v2); g.agregarArista(v0, v3);
        g.agregarArista(v1, v2); g.agregarArista(v1, v3);
        g.agregarArista(v2, v3);

        Set<Integer> dominante = solver.resolver(g);

        assertEquals("En un grafo completo K4, basta con 1 solo nodo", 1, dominante.size());
        assertTrue("El nodo elegido debe ser uno de los vértices del grafo", g.obtenerVertices().containsAll(dominante));
    }

    @Test
    public void testGrafoCamino() {
        Grafo g = new Grafo();
        // Camino de 5 nodos: n0 - n1 - n2 - n3 - n4
        int n0 = g.agregarVertice();
        int n1 = g.agregarVertice();
        int n2 = g.agregarVertice();
        int n3 = g.agregarVertice();
        int n4 = g.agregarVertice();

        g.agregarArista(n0, n1);
        g.agregarArista(n1, n2);
        g.agregarArista(n2, n3);
        g.agregarArista(n3, n4);

        Set<Integer> dominante = solver.resolver(g);

        assertTrue("Para P5, la cantidad de nodos dominantes debe ser como máximo 2", dominante.size() <= 2);
        assertTrue("El conjunto devuelto debe dominar a todos los vértices", esConjuntoDominanteValido(g, dominante));
    }

    @Test
    public void testGrafoDesconectado() {
        Grafo g = new Grafo();
        
        // Componente 1: Estrella (v0 conectado a v1 y v2)
        int c1_centro = g.agregarVertice();
        int c1_h1 = g.agregarVertice();
        int c1_h2 = g.agregarVertice();
        g.agregarArista(c1_centro, c1_h1);
        g.agregarArista(c1_centro, c1_h2);

        // Componente 2: Vértice aislado (v3)
        int aislado = g.agregarVertice();

        Set<Integer> dominante = solver.resolver(g);

        assertEquals(2, dominante.size());
        assertTrue("Debe incluir el centro de la componente 1", dominante.contains(c1_centro));
        assertTrue("Debe incluir el vértice aislado", dominante.contains(aislado));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGrafoNuloLanzaExcepcion() {
        solver.resolver(null);
    }

    // --- Método Auxiliar de Verificación ---

    private boolean esConjuntoDominanteValido(Grafo g, Set<Integer> conjunto) {
        Set<Integer> dominados = new java.util.HashSet<>(conjunto);
        for (int v : conjunto) {
            dominados.addAll(g.vecinos(v));
        }
        return dominados.size() == g.tamano();
    }
}
