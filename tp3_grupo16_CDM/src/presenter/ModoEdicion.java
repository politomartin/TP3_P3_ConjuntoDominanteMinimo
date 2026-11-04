package presenter;

import java.awt.Cursor;

public enum ModoEdicion {
    NINGUNO(
        "Ninguno", 
        "Seleccione una acción en el panel derecho (puede arrastrar los vértices con el mouse).", 
        Cursor.getDefaultCursor()
    ),
    CREAR_VERTICE(
        "Cancelar creación", 
        "Modo Crear Vértice: Haga clic en cualquier espacio vacío del lienzo.", 
        Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)
    ),
    ELIMINAR_VERTICE(
        "Cancelar eliminación", 
        "Modo Eliminar Vértice: Haga clic sobre el vértice que desea borrar.", 
        Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
    ),
    CREAR_ARISTA(
        "Cancelar arista", 
        "Modo Crear Arista: Seleccione con un clic el vértice de ORIGEN.", 
        Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
    ),
    ELIMINAR_ARISTA(
        "Cancelar eliminación", 
        "Modo Eliminar Arista: Seleccione con un clic el primer vértice de la arista.", 
        Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
    );

    private final String textoBoton;
    private final String mensajeEstado;
    private final Cursor cursorAsociado;
    
    ModoEdicion(String textoBoton, String mensajeEstado, Cursor cursorAsociado) {
        this.textoBoton = textoBoton;
        this.mensajeEstado = mensajeEstado;
        this.cursorAsociado = cursorAsociado;
    }
    
    public String getTextoBoton(){
    	return textoBoton; 
    }
    
    public String getMensajeEstado(){
    	return mensajeEstado;
    }
    
    public Cursor getCursorAsociado(){
    	return cursorAsociado;
    }
}