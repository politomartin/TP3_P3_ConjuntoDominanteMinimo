package controlador;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import grafo.Grafo;
import interfaz.DialogoEjecucion;
import interfaz.Interfaz;
import modelo.AlgoritmosCDM;
import modelo.LectorArchivos;

public class Controlador {

    private enum Modo {
        NINGUNO,
        CREAR_VERTICE,
        ELIMINAR_VERTICE,
        CREAR_ARISTA,
        ELIMINAR_ARISTA
    }

    private static class ResultadoCorrida {
        String nombre;
        Set<Integer> cdm;
        double tiempoMs;

        ResultadoCorrida(String nombre, Set<Integer> cdm, double tiempoMs) {
            this.nombre = nombre;
            this.cdm = cdm;
            this.tiempoMs = tiempoMs;
        }
    }

    private Grafo grafo;
    private Interfaz vista;
    private Modo modoActual;
    private Integer primerVerticeSeleccionado;
    private Integer verticeArrastrado;
    private SwingWorker<Void, ResultadoCorrida> workerActual;
    private List<ResultadoCorrida> historialCorridas;

    public Controlador(Interfaz vista) {
        this.vista = vista;
        this.grafo = new Grafo();
        this.modoActual = Modo.NINGUNO;
        this.primerVerticeSeleccionado = null;
        this.verticeArrastrado = null;
        this.historialCorridas = new ArrayList<>();

        inicializarEventos();
    }

    private void inicializarEventos() {
        vista.getBtnCrearVertice().addActionListener(e -> cambiarModo(Modo.CREAR_VERTICE));
        vista.getBtnEliminarVertice().addActionListener(e -> cambiarModo(Modo.ELIMINAR_VERTICE));
        vista.getBtnCrearArista().addActionListener(e -> cambiarModo(Modo.CREAR_ARISTA));
        vista.getBtnEliminarArista().addActionListener(e -> cambiarModo(Modo.ELIMINAR_ARISTA));
        vista.getBtnCargarArchivo().addActionListener(e -> accionCargarArchivo());
        vista.getBtnElegirEjecucion().addActionListener(e -> abrirDialogoEjecucion());

        vista.getPanelGrafo().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (modoActual == Modo.NINGUNO) {
                    // Si no hay ningún modo activo, permite agarrar un vértice para arrastrarlo
                    verticeArrastrado = vista.getPanelGrafo().obtenerVerticeEnCoordenadas(e.getX(), e.getY());
                } else {
                    manejarClickEnLienzo(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                verticeArrastrado = null;
            }
        });

        // Permite mover los vértices arrastrándolos con el mouse
        vista.getPanelGrafo().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (modoActual == Modo.NINGUNO && verticeArrastrado != null) {
                    vista.getPanelGrafo().moverVertice(verticeArrastrado, e.getX(), e.getY());
                }
            }
        });
    }

    private void abrirDialogoEjecucion() {
    	
        resetearModo();
        
        if (grafo.tamano() == 0) {
            mostrarAdvertencia("El grafo esta vacio. Agregue vértices o cargue un archivo primero.");
            return;
        }

        DialogoEjecucion dialogo = new DialogoEjecucion(vista.getFrame());

        dialogo.getBtnIniciarEjecucion().addActionListener(e -> ejecutarAlgoritmosEnSegundoPlano(dialogo));

        dialogo.getBtnDetener().addActionListener(e -> {
            if (workerActual != null && !workerActual.isDone()) {
                workerActual.cancel(true);
            }
        });

        dialogo.getBtnAplicarAlGrafo().addActionListener(e -> {
            int fila = dialogo.getTablaResultados().getSelectedRow();
            if (fila == -1 && !historialCorridas.isEmpty()) {
                fila = historialCorridas.size() - 1; // Si no eligió fila, toma la última
            }
            if (fila != -1 && fila < historialCorridas.size()) {
                ResultadoCorrida res = historialCorridas.get(fila);
                vista.getPanelGrafo().mostrarResultadoCDM(res.cdm, res.nombre, res.tiempoMs);
                dialogo.dispose();
            } else {
                JOptionPane.showMessageDialog(dialogo, "Ejecute al menos un algoritmo primero.");
            }
        });

        dialogo.setVisible(true);
    }

    private void ejecutarAlgoritmosEnSegundoPlano(DialogoEjecucion dialogo) {
        List<String> seleccionados = new ArrayList<>();
        if (dialogo.getChkGolosoGrado().isSelected()) seleccionados.add("Goloso 1 (Mayor Grado)");
        if (dialogo.getChkGolosoCobertura().isSelected()) seleccionados.add("Goloso 2 (Cobertura)");
        if (dialogo.getChkBacktracking().isSelected()) seleccionados.add("Backtracking (Exacto)");

        if (seleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(dialogo, "Seleccione al menos un algoritmo para ejecutar.");
            return;
        }

        dialogo.getModeloTabla().setRowCount(0);
        historialCorridas.clear();
        dialogo.getBtnIniciarEjecucion().setEnabled(false);
        dialogo.getBtnDetener().setEnabled(true);
        dialogo.getBarraProgreso().setIndeterminate(true);
        dialogo.getBarraProgreso().setString("Ejecutando...");

        workerActual = new SwingWorker<Void, ResultadoCorrida>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (String alg : seleccionados) {
                    if (isCancelled()) break;

                    SwingUtilities.invokeLater(() -> 
                        dialogo.getLblEstadoProgreso().setText("Ejecutando: " + alg + "...")
                    );

                    long inicio = System.nanoTime();
                    Set<Integer> solucion;

                    if (alg.startsWith("Goloso 1")) {
                        solucion = AlgoritmosCDM.resolverGolosoMayorGrado(grafo);
                    } else if (alg.startsWith("Goloso 2")) {
                        solucion = AlgoritmosCDM.resolverGolosoCobertura(grafo);
                    } else {
                        solucion = AlgoritmosCDM.resolverBacktracking(grafo);
                    }

                    long fin = System.nanoTime();
                    double tiempoMs = (fin - inicio) / 1_000_000.0;

                    publish(new ResultadoCorrida(alg, solucion, tiempoMs));
                }
                return null;
            }

            @Override
            protected void process(List<ResultadoCorrida> chunks) {
                for (ResultadoCorrida res : chunks) {
                    historialCorridas.add(res);
                    dialogo.getModeloTabla().addRow(new Object[]{
                        res.nombre,
                        res.cdm.size(),
                        res.cdm.toString(),
                        String.format("%.3f ms", res.tiempoMs)
                    });
                }
            }

            @Override
            protected void done() {
                dialogo.getBarraProgreso().setIndeterminate(false);
                dialogo.getBtnIniciarEjecucion().setEnabled(true);
                dialogo.getBtnDetener().setEnabled(false);

                if (isCancelled()) {
                    dialogo.getBarraProgreso().setValue(0);
                    dialogo.getBarraProgreso().setString("Cancelado");
                    dialogo.getLblEstadoProgreso().setText("Ejecución detenida por el usuario.");
                } else {
                    dialogo.getBarraProgreso().setValue(100);
                    dialogo.getBarraProgreso().setString("100% Completado");
                    dialogo.getLblEstadoProgreso().setText("Ejecución finalizada. Seleccione una fila para verla en el grafo.");

                    // Pintamos automáticamente el último resultado en el grafo de fondo
                    if (!historialCorridas.isEmpty()) {
                        ResultadoCorrida ultimo = historialCorridas.get(historialCorridas.size() - 1);
                        vista.getPanelGrafo().mostrarResultadoCDM(ultimo.cdm, ultimo.nombre, ultimo.tiempoMs);
                        dialogo.getTablaResultados().setRowSelectionInterval(
                            historialCorridas.size() - 1, historialCorridas.size() - 1
                        );
                    }
                }
            }
        };

        workerActual.execute();
    }

    private void cambiarModo(Modo nuevoModo) {
        if (this.modoActual == nuevoModo) {
            resetearModo();
            return;
        }

        if ((nuevoModo == Modo.CREAR_ARISTA || nuevoModo == Modo.ELIMINAR_ARISTA) && grafo.tamano() < 2) {
            mostrarAdvertencia("Se necesitan al menos 2 vértices en el grafo para operar con aristas.");
            resetearModo();
            return;
        }
        if (nuevoModo == Modo.ELIMINAR_VERTICE && grafo.tamano() == 0) {
            mostrarAdvertencia("No hay vértices para eliminar.");
            resetearModo();
            return;
        }

        this.modoActual = nuevoModo;
        this.primerVerticeSeleccionado = null;
        vista.getPanelGrafo().setVerticeSeleccionado(null);
        restaurarTextosBotones();

        switch (modoActual) {
            case CREAR_VERTICE:
                vista.getBtnCrearVertice().setText("Cancelar creación");
                vista.getLblEstado().setText("Modo Crear Vértice: Haga clic en cualquier espacio vacío del lienzo.");
                vista.getPanelGrafo().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                break;
            case ELIMINAR_VERTICE:
                vista.getBtnEliminarVertice().setText("Cancelar eliminación");
                vista.getLblEstado().setText("Modo Eliminar Vértice: Haga clic sobre el vértice que desea borrar.");
                vista.getPanelGrafo().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                break;
            case CREAR_ARISTA:
                vista.getBtnCrearArista().setText("Cancelar arista");
                vista.getLblEstado().setText("Modo Crear Arista: Seleccione con un clic el vértice de ORIGEN.");
                vista.getPanelGrafo().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                break;
            case ELIMINAR_ARISTA:
                vista.getBtnEliminarArista().setText("Cancelar eliminación");
                vista.getLblEstado().setText("Modo Eliminar Arista: Seleccione con un clic el primer vértice de la arista.");
                vista.getPanelGrafo().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                break;
            default:
                break;
        }
    }

    private void manejarClickEnLienzo(int x, int y) {
        Integer verticeClickeado = vista.getPanelGrafo().obtenerVerticeEnCoordenadas(x, y);

        switch (modoActual) {
            case CREAR_VERTICE:
                if (verticeClickeado != null) {
                    mostrarAdvertencia("Ya hay un vértice en esa posición. Haga clic en un espacio libre.");
                    return;
                }
                int idNuevo = grafo.agregarVertice();
                vista.getPanelGrafo().registrarVertice(idNuevo, x, y);
                vista.getPanelGrafo().limpiarResultadoCDM();
                sincronizarVistaGrafo();
                resetearModo();
                break;

            case ELIMINAR_VERTICE:
                if (verticeClickeado == null) {
                    mostrarAdvertencia("Haga clic directamente sobre un vértice para eliminarlo.");
                    return;
                }
                grafo.eliminarVertice(verticeClickeado);
                vista.getPanelGrafo().removerVertice(verticeClickeado);
                vista.getPanelGrafo().limpiarResultadoCDM();
                sincronizarVistaGrafo();
                resetearModo();
                break;

            case CREAR_ARISTA:
                procesarSeleccionDobleVertice(verticeClickeado, true);
                break;

            case ELIMINAR_ARISTA:
                procesarSeleccionDobleVertice(verticeClickeado, false);
                break;

            default:
                break;
        }
    }

    private void procesarSeleccionDobleVertice(Integer verticeClickeado, boolean esCreacion) {
        if (verticeClickeado == null) {
            mostrarAdvertencia("Debe hacer clic sobre un vertice valido.");
            return;
        }

        if (primerVerticeSeleccionado == null) {
            primerVerticeSeleccionado = verticeClickeado;
            vista.getPanelGrafo().setVerticeSeleccionado(primerVerticeSeleccionado);
            vista.getLblEstado().setText("Vertice " + primerVerticeSeleccionado + " seleccionado. Haga clic en el segundo vertice.");
            return;
        }

        try {
            if (esCreacion) {
                grafo.agregarArista(primerVerticeSeleccionado, verticeClickeado);
            } else {
                grafo.eliminarArista(primerVerticeSeleccionado, verticeClickeado);
            }
            vista.getPanelGrafo().limpiarResultadoCDM();
            sincronizarVistaGrafo();
            resetearModo();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(vista.getFrame(), ex.getMessage(), "Operación inválida", JOptionPane.ERROR_MESSAGE);
            resetearModo();
        }
    }

    private void accionCargarArchivo() {
        resetearModo();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo de grafo (.txt)");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto (*.txt)", "txt"));

        int seleccion = fileChooser.showOpenDialog(vista.getFrame());

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            try {
                this.grafo = LectorArchivos.cargarGrafo(archivoSeleccionado.getAbsolutePath());

                vista.getPanelGrafo().limpiarLienzo();
                vista.getPanelGrafo().distribuirVerticesEnCirculo(grafo.obtenerVertices());
                sincronizarVistaGrafo();

                vista.getLblEstado().setText("Archivo cargado: " + archivoSeleccionado.getName() 
                    + " (" + grafo.tamano() + " vértices). Puede arrastrar los nodos con el mouse.");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    vista.getFrame(),
                    "Error al leer el archivo:\n" + ex.getMessage(),
                    "Error de carga",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void resetearModo() {
        this.modoActual = Modo.NINGUNO;
        this.primerVerticeSeleccionado = null;
        vista.getPanelGrafo().setVerticeSeleccionado(null);
        vista.getPanelGrafo().setCursor(Cursor.getDefaultCursor());
        vista.getLblEstado().setText("Seleccione una acción en el panel derecho (puede arrastrar los vértices con el mouse).");
        restaurarTextosBotones();
    }

    private void restaurarTextosBotones() {
        vista.getBtnCrearVertice().setText("Crear Nuevo Vértice");
        vista.getBtnEliminarVertice().setText("Eliminar Vértice");
        vista.getBtnCrearArista().setText("Crear Nueva Arista");
        vista.getBtnEliminarArista().setText("Eliminar Arista");
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(vista.getFrame(), mensaje, "Atención", JOptionPane.WARNING_MESSAGE);
    }

    private void sincronizarVistaGrafo() {
        Map<Integer, Set<Integer>> copiaAdyacencias = new HashMap<>();
        for (Integer v : grafo.obtenerVertices()) {
            copiaAdyacencias.put(v, grafo.vecinos(v));
        }
        vista.getPanelGrafo().actualizarGrafo(copiaAdyacencias);
    }
}