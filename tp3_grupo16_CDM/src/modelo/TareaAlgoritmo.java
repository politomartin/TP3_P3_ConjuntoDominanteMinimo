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

        if (Thread.currentThread().isInterrupted()) {
            return;
        }

        long inicio = System.nanoTime();
        Set<Integer> solucion;

        try {
            if (nombreAlgoritmo.contains("Goloso")) {
                solucion = AlgoritmosCDM.resolverGoloso(grafo);
            } else if (nombreAlgoritmo.contains("Backtracking")) {
                solucion = AlgoritmosCDM.resolverBacktracking(grafo);
            } else {
                throw new IllegalArgumentException(
                    "Algoritmo desconocido: " + nombreAlgoritmo
                );
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        
        if (Thread.currentThread().isInterrupted()) {
            return;
        }

        long fin = System.nanoTime();

        double tiempoMs = (fin - inicio) / 1_000_000.0;

        callbackAlTerminar.accept(
            new ResultadoCorrida(nombreAlgoritmo, solucion, tiempoMs)
        );
    }
}