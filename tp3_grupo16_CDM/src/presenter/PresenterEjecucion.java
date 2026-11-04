package presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import grafo.Grafo;
import interfaz.DialogoEjecucion;
import modelo.ResultadoCorrida;

public class PresenterEjecucion {

    private final DialogoEjecucion dialogo;
    private final Grafo grafo;
    private final Consumer<ResultadoCorrida> alAplicarResultado;
    private final List<ResultadoCorrida> historialCorridas;
    private WorkerEjecucionParalela workerActual;

    public PresenterEjecucion(DialogoEjecucion dialogo, Grafo grafo, Consumer<ResultadoCorrida> alAplicarResultado) {
        this.dialogo = dialogo;
        this.grafo = grafo;
        this.alAplicarResultado = alAplicarResultado;
        this.historialCorridas = new ArrayList<>();
        inicializarEventos();
    }

    private void inicializarEventos() {
        dialogo.getBtnIniciarEjecucion().addActionListener(e -> iniciarEjecucionParalela());
        dialogo.getBtnDetener().addActionListener(e -> detenerEjecucion());
        dialogo.getBtnAplicarAlGrafo().addActionListener(e -> aplicarResultadoSeleccionado());
    }

    private void iniciarEjecucionParalela() {
        List<String> seleccionados = dialogo.obtenerAlgoritmosSeleccionados();
        if (seleccionados.isEmpty()) {
            dialogo.mostrarMensaje("Seleccione al menos un algoritmo para ejecutar.");
            return;
        }

        historialCorridas.clear();
        dialogo.prepararInicioEjecucion(seleccionados.size());

        workerActual = new WorkerEjecucionParalela(seleccionados, grafo, this);
        workerActual.execute();
    }

    private void detenerEjecucion() {
        if (workerActual != null && !workerActual.isDone()) {
            workerActual.cancelarEjecucion();
        }
    }

    public void recibirResultadoParcial(ResultadoCorrida res, double porcentajeAvance) {
        historialCorridas.add(res);
        dialogo.agregarFilaResultado(res.getNombre(), res.getCdm().size(), res.getCdm().toString(), res.getTiempoMs());
        dialogo.mostrarAvance(porcentajeAvance, "Completado: " + res.getNombre());
    }

    public void finalizarEjecucion(boolean fueCancelado) {
        if (fueCancelado) {
            dialogo.marcarEjecucionCancelada();
        } else {
            dialogo.marcarEjecucionCompletada();
            if (!historialCorridas.isEmpty()) {
                int ultimaFila = historialCorridas.size() - 1;
                alAplicarResultado.accept(historialCorridas.get(ultimaFila));
                dialogo.seleccionarFila(ultimaFila);
            }
        }
    }

    private void aplicarResultadoSeleccionado() {
        int fila = dialogo.obtenerFilaSeleccionada();
        if (fila == -1 && !historialCorridas.isEmpty()) {
            fila = historialCorridas.size() - 1;
        }
        if (fila != -1 && fila < historialCorridas.size()) {
            alAplicarResultado.accept(historialCorridas.get(fila));
            dialogo.dispose();
        } else {
            dialogo.mostrarMensaje("Ejecute al menos un algoritmo primero.");
        }
    }
}