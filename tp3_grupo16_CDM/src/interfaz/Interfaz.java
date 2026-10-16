package interfaz;

import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import auxiliares.Graphics_2D;
import controlador.Controlador;

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

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
                    Interfaz window = new Interfaz();
                    new Controlador(window);
                    window.mostrar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

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

    public void mostrar() { frame.setVisible(true); }
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