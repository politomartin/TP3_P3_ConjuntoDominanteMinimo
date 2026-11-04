package modelo;

import java.util.Set;
import java.util.function.Consumer;
import grafo.Grafo;

public class TareaAlgoritmo implements Runnable {

    private final String nombreAlgoritmo;
    private final Grafo grafo;
    private final Consumer<ResultadoCorrida> callbackAlTerminar;

    public TareaAlgoritmo(String nombreAlgoritmo, Grafo grafo, Consumer<ResultadoCorrida> callbackAlTerminar) {
        this.nombreAlgoritmo = nombreAlgoritmo;
        this.grafo = grafo;
        this.callbackAlTerminar = callbackAlTerminar;
    }
    

    @Override
    public void run() {
        if (Thread.interrupted()) {
            return;
        }

        long inicio = System.nanoTime();
        Set<Integer> solucion = null;

        if (nombreAlgoritmo.startsWith("Goloso 1")) {
            try {
				solucion = AlgoritmosCDM.resolverGolosoMayorGrado(grafo);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        } else if (nombreAlgoritmo.startsWith("Goloso 2")) {
            try {
				solucion = AlgoritmosCDM.resolverGolosoCobertura(grafo);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        } else {
            try {
				solucion = AlgoritmosCDM.resolverBacktracking(grafo);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }

        long fin = System.nanoTime();
        double tiempoMs = (fin - inicio) / 1_000_000.0;

        if (Thread.currentThread().isInterrupted()) {
            return;
        }

        ResultadoCorrida resultado = new ResultadoCorrida(nombreAlgoritmo, solucion, tiempoMs);
        callbackAlTerminar.accept(resultado);
    }
}