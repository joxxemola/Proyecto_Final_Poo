package proyecto_final_poo;

import controlador.ControladorColor;
import vista.VentanaPrincipal;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Principal {

    public static void main(String[] argumentos) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception error) {
                System.err.println("No se cargó el estilo nativo: " + error.getMessage());
            }

            VentanaPrincipal ventana = new VentanaPrincipal();
            new ControladorColor(ventana);
            ventana.setVisible(true);
        });
    }
}
