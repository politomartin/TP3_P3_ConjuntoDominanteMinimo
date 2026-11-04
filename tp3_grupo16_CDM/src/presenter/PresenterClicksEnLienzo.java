package presenter;

import grafo.Grafo;
import interfaz.Interfaz;

public class PresenterClicksEnLienzo {

    private ModoEdicion modoActual;
    private Integer primerVerticeSeleccionado;
    private Integer verticeArrastrado;

    public PresenterClicksEnLienzo() {
        this.modoActual = ModoEdicion.NINGUNO;
        this.primerVerticeSeleccionado = null;
        this.verticeArrastrado = null;
    }

    public boolean estaEnModoLibre() {
        return modoActual == ModoEdicion.NINGUNO;
    }

    public void cambiarModo(ModoEdicion nuevoModo, Grafo grafo, Interfaz vista) {
        if (this.modoActual == nuevoModo) {
            resetearModo(vista);
            return;
        }

        if ((nuevoModo == ModoEdicion.CREAR_ARISTA || nuevoModo == ModoEdicion.ELIMINAR_ARISTA) && grafo.tamano() < 2) {
            vista.mostrarAdvertencia("Se necesitan al menos 2 vértices en el grafo para operar con aristas.");
            resetearModo(vista);
            return;
        }
        if (nuevoModo == ModoEdicion.ELIMINAR_VERTICE && grafo.tamano() == 0) {
            vista.mostrarAdvertencia("No hay vértices para eliminar.");
            resetearModo(vista);
            return;
        }

        this.modoActual = nuevoModo;
        this.primerVerticeSeleccionado = null;
        vista.getPanelGrafo().setVerticeSeleccionado(null);
        vista.aplicarModoVisual(modoActual);
    }

    public void resetearModo(Interfaz vista) {
        this.modoActual = ModoEdicion.NINGUNO;
        this.primerVerticeSeleccionado = null;
        this.verticeArrastrado = null;
        vista.getPanelGrafo().setVerticeSeleccionado(null);
        vista.aplicarModoVisual(ModoEdicion.NINGUNO);
    }

    public void presionarMouseEnLienzo(int x, int y, Grafo grafo, Interfaz vista, Runnable alModificarGrafo) {
        if (estaEnModoLibre()) {
            verticeArrastrado = vista.getPanelGrafo().obtenerVerticeEnCoordenadas(x, y);
        } else {
            procesarClickModoActivo(x, y, grafo, vista, alModificarGrafo);
        }
    }

    public void arrastrarMouseEnLienzo(int x, int y, Interfaz vista) {
        if (estaEnModoLibre() && verticeArrastrado != null) {
            vista.getPanelGrafo().moverVertice(verticeArrastrado, x, y);
        }
    }

    public void soltarMouseEnLienzo() {
        this.verticeArrastrado = null;
    }

    private void procesarClickModoActivo(int x, int y, Grafo grafo, Interfaz vista, Runnable alModificarGrafo) {
        Integer verticeClickeado = vista.getPanelGrafo().obtenerVerticeEnCoordenadas(x, y);

        switch (modoActual) {
            case CREAR_VERTICE:
                if (verticeClickeado != null) {
                    vista.mostrarAdvertencia("Ya hay un vértice en esa posición. Haga clic en un espacio libre.");
                    return;
                }
                int idNuevo = grafo.agregarVertice();
                vista.getPanelGrafo().registrarVertice(idNuevo, x, y);
                alModificarGrafo.run();
                resetearModo(vista);
                break;

            case ELIMINAR_VERTICE:
                if (verticeClickeado == null) {
                    vista.mostrarAdvertencia("Haga clic directamente sobre un vértice para eliminarlo.");
                    return;
                }
                grafo.eliminarVertice(verticeClickeado);
                vista.getPanelGrafo().removerVertice(verticeClickeado);
                alModificarGrafo.run();
                resetearModo(vista);
                break;

            case CREAR_ARISTA:
                procesarSeleccionDobleVertice(verticeClickeado, true, grafo, vista, alModificarGrafo);
                break;

            case ELIMINAR_ARISTA:
                procesarSeleccionDobleVertice(verticeClickeado, false, grafo, vista, alModificarGrafo);
                break;

            default:
                break;
        }
    }

    private void procesarSeleccionDobleVertice(Integer verticeClickeado, boolean esCreacion, Grafo grafo, Interfaz vista, Runnable alModificarGrafo) {
        if (verticeClickeado == null) {
            vista.mostrarAdvertencia("Debe hacer clic sobre un vértice válido.");
            return;
        }

        if (primerVerticeSeleccionado == null) {
            primerVerticeSeleccionado = verticeClickeado;
            vista.getPanelGrafo().setVerticeSeleccionado(primerVerticeSeleccionado);
            vista.getLblEstado().setText("Vértice " + primerVerticeSeleccionado + " seleccionado. Haga clic en el segundo vértice.");
            return;
        }

        try {
            if (esCreacion) {
                grafo.agregarArista(primerVerticeSeleccionado, verticeClickeado);
            } else {
                grafo.eliminarArista(primerVerticeSeleccionado, verticeClickeado);
            }
            alModificarGrafo.run();
            resetearModo(vista);
        } catch (IllegalArgumentException ex) {
            vista.mostrarError("Operación inválida", ex.getMessage());
            resetearModo(vista);
        }
    }
}