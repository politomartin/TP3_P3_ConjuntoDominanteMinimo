package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import grafo.Grafo;

public class GrafoTest {
	
	private Grafo grafo;
	
	@Before
    public void setUp() {
        grafo = new Grafo(4); // Vértices 0, 1, 2, 3
    }
	
	@Test(expected = IllegalArgumentException.class)
	public void crearGrafoConNulosVertices() {
		new Grafo(-1);
	}
	
	@Test
    public void agregarAristaTest() {
        grafo.agregarArista(0, 1);
        assertTrue(grafo.existeArista(0, 1));
        assertTrue(grafo.existeArista(1, 0));
    }
	
	@Test(expected = IllegalArgumentException.class)
    public void agregarAristaExistenteTest() {
        grafo.agregarArista(0, 2);
        grafo.agregarArista(0, 2);
    }
	
	@Test(expected = IllegalArgumentException.class)
	public void agregarAristaEntreIgualesVerticesTest() {
		grafo.agregarArista(1, 1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void agregarAristaFueraDeGrafoTest() {
		grafo.agregarArista(0, 5);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void agregarAristaConVerticeNegativoTest() {
		grafo.agregarArista(-1, 2);
	}
	
	@Test
    public void eliminarAristaTest() {
        grafo.agregarArista(1, 3);
        grafo.eliminarArista(1, 3);
        assertFalse(grafo.existeArista(1, 3));
        assertFalse(grafo.existeArista(3, 1));
    }
	
	@Test
    public void eliminarVerticeTest() {
        grafo.agregarVertice();     
        assertTrue(grafo.existeVertice(4));
        grafo.eliminarVertice(4); 
        assertFalse(grafo.existeVertice(4));
    }
	
	@Test
	public void verticeEliminadoNoVecinoDeOtroTest() {
		grafo.agregarVertice();
		grafo.agregarArista(4, 0);
		grafo.eliminarVertice(4);
		assertFalse(grafo.vecinos(0).contains(4));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void eliminarAristaInexistente() {
		grafo.eliminarArista(1, 2);
	}
	
	@Test
	public void aristasDeVerticeEliminadoTest() {
        grafo.agregarArista(0, 2);
        grafo.agregarArista(2, 3);
	}
	
	@Test
	public void agregarVerticeTest() {
        grafo.agregarVertice();
        assertEquals(5, grafo.tamano());
	}
	

}
