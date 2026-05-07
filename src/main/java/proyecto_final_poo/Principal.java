package proyecto_final_poo;  // Define el paquete donde se encuentra esta clase principal

// Importa el controlador desde el paquete controlador
import controlador.ControladorColor;
// Importa la ventana principal desde el paquete vista
import vista.VentanaPrincipal;

// Importa SwingUtilities para manejar la ejecución en el hilo de eventos de Swing
import javax.swing.SwingUtilities;
// Importa UIManager para gestionar la apariencia (Look and Feel) de la interfaz
import javax.swing.UIManager;

/**
 * Clase principal (entry point) de la aplicación. Esta clase contiene el método
 * main que inicia toda la aplicación.
 *
 * Sigue las mejores prácticas de Swing: - Usa invokeLater para ejecutar en el
 * hilo de eventos - Configura el Look and Feel nativo del sistema - Crea la
 * vista y el controlador siguiendo el patrón MVC
 */
public class Principal {

    /**
     * Método principal - Punto de entrada de la aplicación Java La JVM llama a
     * este método cuando se ejecuta el programa
     *
     * @param argumentos Argumentos de línea de comandos (no se usan en esta
     * app)
     */
    public static void main(String[] argumentos) {

        // ─── EJECUCIÓN EN EL HILO DE EVENTOS DE SWING ─────────────────
        // SwingUtilities.invokeLater asegura que el código se ejecute en el
        // Event Dispatch Thread (EDT) o hilo de despacho de eventos de Swing.
        // Esto es CRÍTICO para la seguridad de los hilos en Swing.
        // 
        // ¿Por qué es necesario?
        // - Todos los componentes Swing DEBEN crearse y modificarse en el EDT
        // - Si se crean en otros hilos, puede causar comportamientos erráticos
        // - invokeLater() programa la tarea para ejecutarse en el EDT
        SwingUtilities.invokeLater(() -> {

            // ─── CONFIGURACIÓN DEL LOOK AND FEEL NATIVO ───────────────
            // Intenta establecer el Look and Feel (apariencia) nativa del sistema
            // Esto hace que la aplicación se vea como una aplicación nativa:
            // - En Windows: se ve como una aplicación de Windows
            // - En macOS: se ve como una aplicación de Mac
            // - En Linux: se ve como una aplicación de GTK
            try {
                // Obtiene y establece el Look and Feel nativo del sistema operativo
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception error) {
                // Si no se puede cargar (ej: sistema no soportado), muestra el error
                // pero continúa con el Look and Feel por defecto de Java (Metal)
                System.err.println("No se cargó el estilo nativo: " + error.getMessage());
            }

            // ─── CREACIÓN Y EJECUCIÓN DE LA APLICACIÓN ─────────────────
            // 1. Crea la ventana principal (Vista)
            VentanaPrincipal ventana = new VentanaPrincipal();

            // 2. Crea el controlador pasándole la ventana
            //    El controlador se conecta automáticamente a la vista
            new ControladorColor(ventana);

            // 3. Hace visible la ventana
            //    Por defecto las ventanas Swing son invisibles
            ventana.setVisible(true);
        });
    }
}
