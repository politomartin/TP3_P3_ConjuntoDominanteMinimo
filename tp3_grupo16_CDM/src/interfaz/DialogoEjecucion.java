package interfaz;

import java.awt.Font;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DialogoEjecucion extends JDialog {

    private JCheckBox chkGolosoGrado;
    private JCheckBox chkGolosoCobertura;
    private JCheckBox chkBacktracking;
    private JButton btnIniciarEjecucion;
    private JButton btnDetener;
    private JButton btnAplicarAlGrafo;
    private JProgressBar barraProgreso;
    private JLabel lblEstadoProgreso;
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;

    public DialogoEjecucion(JFrame parent) {
        super(parent, "Elegir modo de ejecución - Conjunto Dominante Mínimo", true);
        setSize(680, 450);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setLayout(null);

        JLabel lblTitulo = new JLabel("Seleccione los algoritmos que desea ejecutar y comparar:");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTitulo.setBounds(20, 15, 450, 20);
        getContentPane().add(lblTitulo);

        chkGolosoGrado = new JCheckBox("Algoritmo Goloso 1 (Por mayor grado)");
        chkGolosoGrado.setSelected(true);
        chkGolosoGrado.setBounds(20, 42, 300, 25);
        getContentPane().add(chkGolosoGrado);

        chkGolosoCobertura = new JCheckBox("Algoritmo Goloso 2 (Por mayor cantidad de vecinos no dominados)");
        chkGolosoCobertura.setSelected(true);
        chkGolosoCobertura.setBounds(20, 68, 430, 25);
        getContentPane().add(chkGolosoCobertura);

        chkBacktracking = new JCheckBox("Algoritmo Backtracking (Solución exacta óptima)");
        chkBacktracking.setSelected(true);
        chkBacktracking.setBounds(20, 94, 350, 25);
        getContentPane().add(chkBacktracking);

        btnIniciarEjecucion = new JButton("Ejecutar Seleccionados");
        btnIniciarEjecucion.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnIniciarEjecucion.setBounds(460, 42, 190, 35);
        getContentPane().add(btnIniciarEjecucion);

        btnDetener = new JButton("Detener");
        btnDetener.setEnabled(false);
        btnDetener.setBounds(460, 84, 190, 30);
        getContentPane().add(btnDetener);

        lblEstadoProgreso = new JLabel("Listo para ejecutar.");
        lblEstadoProgreso.setBounds(20, 130, 630, 20);
        getContentPane().add(lblEstadoProgreso);

        barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setStringPainted(true);
        barraProgreso.setBounds(20, 153, 630, 25);
        getContentPane().add(barraProgreso);

        String[] columnas = {"Algoritmo", "Tamaño CDM", "Conjunto Obtenido", "Tiempo (ms)"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaResultados = new JTable(modeloTabla);
        tablaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaResultados.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(tablaResultados);
        scrollPane.setBounds(20, 195, 630, 155);
        getContentPane().add(scrollPane);

        btnAplicarAlGrafo = new JButton("Pintar resultado seleccionado en el Grafo");
        btnAplicarAlGrafo.setBounds(200, 365, 280, 35);
        getContentPane().add(btnAplicarAlGrafo);
    }

    public JCheckBox getChkGolosoGrado(){ 
    	return chkGolosoGrado; 
    }
    
    public JCheckBox getChkGolosoCobertura(){
    	return chkGolosoCobertura;
    }
    
    public JCheckBox getChkBacktracking(){
    	return chkBacktracking;
    }
    
    public JButton getBtnIniciarEjecucion(){
    	return btnIniciarEjecucion;
    }
    
    public JButton getBtnDetener(){
    	return btnDetener;
    }
    
    public JButton getBtnAplicarAlGrafo(){
    	return btnAplicarAlGrafo;
    }
    
    public JProgressBar getBarraProgreso(){
    	return barraProgreso;
    }
    
    public JLabel getLblEstadoProgreso(){
    	return lblEstadoProgreso;
    }
    
    public JTable getTablaResultados(){
    	return tablaResultados;
    }
    
    public DefaultTableModel getModeloTabla(){
    	return modeloTabla;
    }
    
}