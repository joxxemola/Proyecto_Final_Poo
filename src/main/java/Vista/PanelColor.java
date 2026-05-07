package vista;  // Define el paquete donde se encuentra esta clase (componente de interfaz gráfica)

// Importa las clases necesarias para la interfaz gráfica de Swing
import javax.swing.*;
import java.awt.*;

/**
 * Panel personalizado que muestra un color y su nombre. Este panel se utiliza
 * para visualizar el color seleccionado actualmente. Extiende JPanel para crear
 * un componente Swing personalizado.
 */
public class PanelColor extends JPanel {

    // Almacena el nombre del color a mostrar (ej: "ROJO", "AZUL")
    private String nombreColor = "Selecciona un color";

    // Almacena el color actual que se muestra en el panel (inicialmente gris claro)
    private Color colorActual = Color.LIGHT_GRAY;

    // Constructor del panel - se llama cuando se crea una instancia
    public PanelColor() {
        // Establece el tamaño preferido del panel (ancho 500px, alto 200px)
        setPreferredSize(new Dimension(500, 200));

        // Crea un borde alrededor del panel (color gris oscuro, grosor 3px)
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3));
    }

    /**
     * Método sobrescrito de JPanel que dibuja el contenido del panel. Este
     * método se llama automáticamente cuando se necesita redibujar el panel.
     *
     * @param graficos Objeto Graphics que proporciona herramientas de dibujo
     */
    @Override
    protected void paintComponent(Graphics graficos) {
        // Llama al paintComponent del padre para mantener la funcionalidad base
        super.paintComponent(graficos);

        // Convierte Graphics a Graphics2D para mejores capacidades de dibujo
        Graphics2D graficos2D = (Graphics2D) graficos;

        // Activa el antialiasing (suavizado de bordes) para textos y formas
        graficos2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // ─── DIBUJAR EL FONDO DE COLOR ─────────────────────────────
        // Establece el color de dibujo al color actual
        graficos2D.setColor(colorActual);
        // Dibuja un rectángulo que ocupa todo el panel (fondo completo)
        graficos2D.fillRect(0, 0, getWidth(), getHeight());

        // ─── DIBUJAR EL NOMBRE DEL COLOR ───────────────────────────
        // Configura la fuente para el nombre (Arial, negrita, tamaño 42)
        graficos2D.setFont(new Font("Arial", Font.BOLD, 42));
        // Establece el color del texto a negro
        graficos2D.setColor(Color.BLACK);

        // Obtiene medidas de la fuente para centrar el texto
        FontMetrics medidas = graficos2D.getFontMetrics();

        // Calcula la posición X para centrar horizontalmente
        int posX = (getWidth() - medidas.stringWidth(nombreColor)) / 2;

        // Calcula la posición Y para centrar verticalmente (con ligero ajuste)
        int posY = (getHeight() / 2) + medidas.getAscent() / 2 - 10;

        // Dibuja el nombre del color en la posición calculada
        graficos2D.drawString(nombreColor, posX, posY);

        // ─── DIBUJAR EL VALOR HEXADECIMAL ──────────────────────────
        // Convierte el color RGB a formato hexadecimal (ej: #FF0000 para rojo)
        String valorHex = String.format("#%02X%02X%02X",
                colorActual.getRed(), // Componente rojo (0-255)
                colorActual.getGreen(), // Componente verde (0-255)
                colorActual.getBlue() // Componente azul (0-255)
        );

        // Configura la fuente para el valor hexadecimal (Arial, normal, tamaño 20)
        graficos2D.setFont(new Font("Arial", Font.PLAIN, 20));
        // Mantiene el color negro para el texto
        graficos2D.setColor(Color.BLACK);

        // Obtiene medidas de la fuente pequeña
        FontMetrics medidasPequenas = graficos2D.getFontMetrics();

        // Calcula la posición X para centrar el texto hexadecimal
        int posXHex = (getWidth() - medidasPequenas.stringWidth(valorHex)) / 2;

        // Dibuja el valor hexadecimal debajo del nombre (posY + 40px)
        graficos2D.drawString(valorHex, posXHex, posY + 40);
    }

    /**
     * Actualiza el color y nombre mostrados en el panel. Este método es llamado
     * desde el controlador cuando se selecciona un nuevo color.
     *
     * @param nuevoColor El nuevo color RGB a mostrar
     * @param nuevoNombre El nombre del nuevo color (se mostrará en mayúsculas)
     */
    public void actualizarColor(Color nuevoColor, String nuevoNombre) {
        this.colorActual = nuevoColor;           // Actualiza el color
        this.nombreColor = nuevoNombre.toUpperCase(); // Convierte el nombre a mayúsculas
        repaint();  // Solicita que se redibuje el panel con los nuevos valores
    }
}
