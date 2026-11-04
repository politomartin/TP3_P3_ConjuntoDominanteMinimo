package presenter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import grafo.Grafo;
import interfaz.DialogoEjecucion;
import interfaz.Interfaz;
import modelo.LectorArchivos;
import modelo.ResultadoCorrida;

public class PresenterPrincipal {

    private Grafo grafo;
    private final Interfaz vista;
    private final PresenterClicksEnLienzo manejadorClicks;

    public PresenterPrincipal(Interfaz vista) {
        this.vista = vista;
        this.grafo = new Grafo();
        this.manejadorClicks = new PresenterClicksEnLienzo();
        inicializarEventos();
    }

    private void inicializarEventos() {
        vista.getBtnCrearVertice().addActionListener(e -> manejadorClicks.cambiarModo(ModoEdicion.CREAR_VERTICE, grafo, vista));
        vista.getBtnEliminarVertice().addActionListener(e -> manejadorClicks.cambiarModo(ModoEdicion.ELIMINAR_VERTICE, grafo, vista));
        vista.getBtnCrearArista().addActionListener(e -> manejadorClicks.cambiarModo(ModoEdicion.CREAR_ARISTA, grafo, vista));
        vista.getBtnEliminarArista().addActionListener(e -> manejadorClicks.cambiarModo(ModoEdicion.ELIMINAR_ARISTA, grafo, vista));
        vista.getBtnCargarArchivo().addActionListener(e -> accionCargarArchivo());
        vista.getBtnElegirEjecucion().addActionListener(e -> abrirDialogoEjecucion());

        vista.getPanelGrafo().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                manejadorClicks.presionarMouseEnLienzo(e.getX(), e.getY(), grafo, vista, () -> {
                    vista.getPanelGrafo().limpiarResultadoCDM();
                    sincronizarVistaGrafo();
                });
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                manejadorClicks.soltarMouseEnLienzo();
            }
        });

        vista.getPanelGrafo().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                manejadorClicks.arrastrarMouseEnLienzo(e.getX(), e.getY(), vista);
            }
        });
    }

    private void accionCargarArchivo() {
        manejadorClicks.resetearModo(vista);
        File archivo = vista.seleccionarArchivoTxt();

        if (archivo != null) {
            try {
                this.grafo = LectorArchivos.cargarGrafo(archivo.getAbsolutePath());
                vista.getPanelGrafo().limpiarLienzo();
                vista.getPanelGrafo().distribuirVerticesEnCirculo(grafo.obtenerVertices());
                sincronizarVistaGrafo();
                vista.getLblEstado().setText("Archivo cargado: " + archivo.getName()
                    + " (" + grafo.tamano() + " vértices). Puede arrastrar los nodos con el mouse.");
            } catch (Exception ex) {
                vista.mostrarError("Error de carga", "Error al leer el archivo:\n" + ex.getMessage());
            }
        }
    }

    private void abrirDialogoEjecucion() {
        manejadorClicks.resetearModo(vista);
        if (grafo.tamano() == 0) {
            vista.mostrarAdvertencia("El grafo está vacío. Agregue vértices o cargue un archivo primero.");
            return;
        }

        DialogoEjecucion dialogo = new DialogoEjecucion(vista.getFrame());
        new PresenterEjecucion(dialogo, grafo, this::pintarResultadoEnGrafo);
        dialogo.setVisible(true);
    }

    public void pintarResultadoEnGrafo(ResultadoCorrida res) {
        vista.getPanelGrafo().mostrarResultadoCDM(res.getCdm(), res.getNombre(), res.getTiempoMs());
    }

    private void sincronizarVistaGrafo() {
        Map<Integer, Set<Integer>> copiaAdyacencias = new HashMap<>();
        for (Integer v : grafo.obtenerVertices()) {
            copiaAdyacencias.put(v, grafo.vecinos(v));
        }
        vista.getPanelGrafo().actualizarGrafo(copiaAdyacencias);
    }
}