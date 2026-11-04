package presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.SwingWorker;
import grafo.Grafo;
import modelo.ResultadoCorrida;
import modelo.TareaAlgoritmo;

public class WorkerEjecucionParalela extends SwingWorker<Boolean, ResultadoCorrida> {

    private final List<String> algoritmosSeleccionados;
    private final Grafo grafo;
    private final PresenterEjecucion presenter;
    private final List<Thread> hilosActivos;
    private final AtomicInteger completados;

    public WorkerEjecucionParalela(List<String> algoritmosSeleccionados, Grafo grafo, PresenterEjecucion presenter) {
        this.algoritmosSeleccionados = algoritmosSeleccionados;
        this.grafo = grafo;
        this.presenter = presenter;
        this.hilosActivos = new ArrayList<>();
        this.completados = new AtomicInteger(0);
    }

    @Override
    protected Boolean doInBackground() {
        int total = algoritmosSeleccionados.size();

        // 1. Lanzamos un Thread independiente por cada algoritmo seleccionado
        for (String nombreAlg : algoritmosSeleccionados) {
            TareaAlgoritmo tarea = new TareaAlgoritmo(nombreAlg, grafo, resultado -> {
                publish(resultado);
            });

            Thread hilo = new Thread(tarea);
            hilosActivos.add(hilo);
            hilo.start();
        }

        // 2. Sincronizamos esperando a que todos terminen mediante join()
        for (Thread hilo : hilosActivos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                interrumpirTodosLosHilos();
                return false;
            }
        }

        return !isCancelled();
    }

    public void cancelarEjecucion() {
        this.cancel(true);
        interrumpirTodosLosHilos();
    }

    private void interrumpirTodosLosHilos() {
        for (Thread hilo : hilosActivos) {
            if (hilo.isAlive()) {
                hilo.interrupt();
            }
        }
    }

    @Override
    protected void process(List<ResultadoCorrida> chunks) {
        int total = algoritmosSeleccionados.size();
        for (ResultadoCorrida res : chunks) {
            int terminados = completados.incrementAndGet();
            double porcentaje = (terminados * 100.0) / total;
            presenter.recibirResultadoParcial(res, porcentaje);
        }
    }

    @Override
    protected void done() {
        presenter.finalizarEjecucion(isCancelled());
    }
}