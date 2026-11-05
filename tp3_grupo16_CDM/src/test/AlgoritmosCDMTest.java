package test;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import grafo.Grafo;
import modelo.AlgoritmosCDM;

public class AlgoritmosCDMTest {
	@Test
	public void testBacktrackingGrafoVacio() throws InterruptedException {

	    Grafo g = new Grafo();

	    Set<Integer> resultado = AlgoritmosCDM.resolverBacktracking(g);

	    assertNotNull(resultado);
	    assertTrue(resultado.isEmpty());
	}
	
	@Test
	public void testBacktrackingVerticesAislados()
	        throws InterruptedException {

	    Grafo g = new Grafo();

	    int v0 = g.agregarVertice();
	    int v1 = g.agregarVertice();
	    int v2 = g.agregarVertice();

	    Set<Integer> resultado = AlgoritmosCDM.resolverBacktracking(g);

	    assertEquals(3, resultado.size());
        assertTrue(resultado.contains(v0));
        assertTrue(resultado.contains(v1));
        assertTrue(resultado.contains(v2));
	}
	
	@Test
    public void testGrafoEstrellaEligeNodoCentral() throws InterruptedException {
		 
        Grafo g = new Grafo();
        
        int centro = g.agregarVertice(); 
        
        int h1 = g.agregarVertice();
        int h2 = g.agregarVertice();
        int h3 = g.agregarVertice();

        g.agregarArista(centro, h1);
        g.agregarArista(centro, h2);
        g.agregarArista(centro, h3);

        Set<Integer> resultado = AlgoritmosCDM.resolverBacktracking(g);

        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(centro));
    }
	
	@Test
	public void testBacktrackingGrafoCamino() throws InterruptedException {

	    Grafo g = new Grafo();

	    int v0 = g.agregarVertice();
	    int v1 = g.agregarVertice();
	    int v2 = g.agregarVertice();
	    int v3 = g.agregarVertice();
	    int v4 = g.agregarVertice();

	    g.agregarArista(v0, v1);
	    g.agregarArista(v1, v2);
	    g.agregarArista(v2, v3);
	    g.agregarArista(v3, v4);

	    Set<Integer> resultado = AlgoritmosCDM.resolverBacktracking(g);

	    assertEquals(2, resultado.size());
        assertTrue(esConjuntoDominanteValido(g, resultado));
	}
	
	@Test
	public void testBacktrackingGrafoCompleto()
	        throws InterruptedException {

	    Grafo g = new Grafo();

	    int v0 = g.agregarVertice();
	    int v1 = g.agregarVertice();
	    int v2 = g.agregarVertice();
	    int v3 = g.agregarVertice();

	    g.agregarArista(v0, v1);
	    g.agregarArista(v0, v2);
	    g.agregarArista(v0, v3);
	    g.agregarArista(v1, v2);
	    g.agregarArista(v1, v3);
	    g.agregarArista(v2, v3);

	    Set<Integer> resultado = AlgoritmosCDM.resolverBacktracking(g);

	    assertEquals(1, resultado.size());
	    assertTrue(esConjuntoDominanteValido(g, resultado));
	}
	
	@Test
	public void testBacktrackingVerticesNoConsecutivos() throws InterruptedException {

	    Grafo g = new Grafo();

	    int v0 = g.agregarVertice();
	    int v1 = g.agregarVertice();
	    int v2 = g.agregarVertice();
	    int v3 = g.agregarVertice();
	    int v4 = g.agregarVertice();
	    
	    g.eliminarVertice(v1);
	    g.eliminarVertice(v3);

	    g.agregarArista(v0, v2);
	    g.agregarArista(v2, v4);

	    Set<Integer> resultado = AlgoritmosCDM.resolverBacktracking(g);

	    assertEquals(1, resultado.size());
	    assertTrue(resultado.contains(v2));
	    assertTrue(esConjuntoDominanteValido(g, resultado));
	}
	
	 @Test
	    public void testGrafoDesconectado() throws InterruptedException {
	        Grafo g = new Grafo();
	        
	        int c1_centro = g.agregarVertice();
	        int c1_h1 = g.agregarVertice();
	        int c1_h2 = g.agregarVertice();
	        g.agregarArista(c1_centro, c1_h1);
	        g.agregarArista(c1_centro, c1_h2);

	        int aislado = g.agregarVertice();

	        Set<Integer> resultado = AlgoritmosCDM.resolverBacktracking(g);

	        assertEquals(2, resultado.size());
	        assertTrue(resultado.contains(c1_centro));
	        assertTrue(resultado.contains(aislado));
	    }
	 
	 @Test(expected = InterruptedException.class)
	 public void testBacktrackingInterrumpido()
	         throws InterruptedException {

	     Grafo g = new Grafo();
	     g.agregarVertice();
	     g.agregarVertice();
	     g.agregarVertice();

	     Thread.currentThread().interrupt();

	     try {
	         AlgoritmosCDM.resolverBacktracking(g);
	     } finally {
	         Thread.interrupted();
	     }
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
