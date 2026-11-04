package interfaz;

import java.awt.Font;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Interfaz {

    private JFrame frame;
    private JButton btnCrearVertice;
    private JButton btnEliminarVertice;
    private JButton btnCrearArista;
    private JButton btnEliminarArista;
    private JButton btnCargarArchivo;
    private JButton btnElegirEjecucion;
    private JLabel lblEstado;
    private Graphics_2D panelGrafo;

    public Interfaz() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Conjunto dominante mínimo - TP3 Programación III");
        frame.setBounds(100, 100, 1366, 768);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.getContentPane().setLayout(null);

        lblEstado = new JLabel("Seleccione una acción en el panel derecho (puede arrastrar los vértices con el mouse).");
        lblEstado.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblEstado.setBounds(20, 10, 1000, 25);
        frame.getContentPane().add(lblEstado);

        panelGrafo = new Graphics_2D();
        panelGrafo.setBounds(20, 40, 1000, 670);
        frame.getContentPane().add(panelGrafo);

        btnCrearVertice = new JButton("Crear Nuevo Vértice");
        btnCrearVertice.setBounds(1044, 50, 290, 40);
        frame.getContentPane().add(btnCrearVertice);

        btnEliminarVertice = new JButton("Eliminar Vértice");
        btnEliminarVertice.setBounds(1044, 100, 290, 40);
        frame.getContentPane().add(btnEliminarVertice);

        btnCrearArista = new JButton("Crear Nueva Arista");
        btnCrearArista.setBounds(1044, 170, 290, 40);
        frame.getContentPane().add(btnCrearArista);

        btnEliminarArista = new JButton("Eliminar Arista");
        btnEliminarArista.setBounds(1044, 220, 290, 40);
        frame.getContentPane().add(btnEliminarArista);

        btnCargarArchivo = new JButton("Subir relaciones desde archivo");
        btnCargarArchivo.setBounds(1044, 295, 290, 45);
        frame.getContentPane().add(btnCargarArchivo);

        btnElegirEjecucion = new JButton("Elegir cómo ejecutar (CDM)");
        btnElegirEjecucion.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnElegirEjecucion.setBounds(1044, 365, 290, 50);
        frame.getContentPane().add(btnElegirEjecucion);
    }

    public void mostrar(){
    	frame.setVisible(true);
    }
    
    public void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Atención", JOptionPane.WARNING_MESSAGE);
    }
    
    public void mostrarError(String titulo, String mensaje) {
        javax.swing.JOptionPane.showMessageDialog(frame, mensaje, titulo, javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    
    public void aplicarModoVisual( presenter.ModoEdicion modo ) {
        btnCrearVertice.setText(modo == presenter.ModoEdicion.CREAR_VERTICE ? modo.getTextoBoton() : "Crear Nuevo Vértice");
        btnEliminarVertice.setText(modo == presenter.ModoEdicion.ELIMINAR_VERTICE ? modo.getTextoBoton() : "Eliminar Vértice");
        btnCrearArista.setText(modo == presenter.ModoEdicion.CREAR_ARISTA ? modo.getTextoBoton() : "Crear Nueva Arista");
        btnEliminarArista.setText(modo == presenter.ModoEdicion.ELIMINAR_ARISTA ? modo.getTextoBoton() : "Eliminar Arista");
        lblEstado.setText(modo.getMensajeEstado());
        panelGrafo.setCursor(modo.getCursorAsociado());
    }
    
    public File seleccionarArchivoTxt() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Seleccionar archivo de grafo (.txt)");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos de texto (*.txt)", "txt"));
        if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        }
        return null;
    }
    
    public JFrame getFrame() { return frame; }
    public JButton getBtnCrearVertice() { return btnCrearVertice; }
    public JButton getBtnEliminarVertice() { return btnEliminarVertice; }
    public JButton getBtnCrearArista() { return btnCrearArista; }
    public JButton getBtnEliminarArista() { return btnEliminarArista; }
    public JButton getBtnCargarArchivo() { return btnCargarArchivo; }
    public JButton getBtnElegirEjecucion() { return btnElegirEjecucion; }
    public JLabel getLblEstado() { return lblEstado; }
    public Graphics_2D getPanelGrafo() { return panelGrafo; }
}