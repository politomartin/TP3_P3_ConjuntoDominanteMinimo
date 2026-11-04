package app;

import java.awt.EventQueue;
import javax.swing.UIManager;
import interfaz.Interfaz;
import presenter.PresenterPrincipal;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                Interfaz vista = new Interfaz();
                new PresenterPrincipal(vista);
                vista.mostrar();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}