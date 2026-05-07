package vista;  // Define el paquete donde se encuentra esta clase (componente de interfaz gráfica)

// Importa las clases necesarias para la interfaz gráfica de Swing
import javax.swing.*;
import java.awt.*;
// Importa List para manejar colecciones de tonalidades
import java.util.List;
// Importa Consumer para el callback funcional (evento de selección)
import java.util.function.Consumer;

/**
 * Panel que muestra una tira horizontal con todas las tonalidades generadas a
 * partir de un color base. Cada ficha es clicable y notifica al observador.
 *
 * Proporciona una interfaz visual para explorar variaciones de un color: - 4
 * tonos más claros (tintes) - El color base - 4 tonos más oscuros (sombras)
 */
public class PanelTonalidades extends JPanel {

    // Callback que se dispara cuando el usuario clica una tonalidad
    // Consumer es una interfaz funcional que recibe un Color y no devuelve nada
    private Consumer<Color> alSeleccionarTono;

    // Índice del color base dentro de la lista (posición 4 en la lista de 9 elementos)
    // Se resalta con borde dorado para identificarlo fácilmente
    private static final int INDICE_BASE = 4;

    // Constructor del panel - se llama cuando se crea una instancia
    public PanelTonalidades() {
        // Establece un layout con espacio entre norte y centro (0px horizontal, 4px vertical)
        setLayout(new BorderLayout(0, 4));

        // Establece el color de fondo del panel (gris claro)
        setBackground(new Color(240, 240, 240));

        // Crea un borde vacío alrededor del panel (arriba, izquierda, abajo, derecha)
        setBorder(BorderFactory.createEmptyBorder(6, 0, 4, 0));
    }

    /**
     * Registra quién recibe el evento de clic sobre una ficha. El callback se
     * ejecutará cuando el usuario haga clic en una tonalidad.
     *
     * @param callback Función que recibe el Color seleccionado
     */
    public void setAlSeleccionarTono(Consumer<Color> callback) {
        this.alSeleccionarTono = callback;
    }

    /**
     * Reconstruye las fichas con la nueva lista de tonalidades. Este método se
     * llama cada vez que se selecciona un nuevo color base.
     *
     * @param tonalidades Lista de 9 Color ordenados de más claro a más oscuro
     * @param nombreBase Nombre del color principal (para la etiqueta
     * descriptiva)
     */
    public void actualizarTonalidades(List<Color> tonalidades, String nombreBase) {
        removeAll();  // Limpia todos los componentes actuales del panel

        // ─── ETIQUETA SUPERIOR (TÍTULO) ──────────────────────────────
        // Crea una etiqueta con el texto "Tonalidades de [nombreBase]:"
        JLabel titulo = new JLabel(
                "Tonalidades de " + nombreBase + ":", SwingConstants.LEFT
        );
        // Configura la fuente (Arial, negrita, tamaño 14)
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        // Establece el color del texto (gris oscuro)
        titulo.setForeground(Color.DARK_GRAY);
        // Añade un borde vacío para separación (arriba, izquierda, abajo, derecha)
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 2, 4, 0));
        // Añade el título en la parte norte (superior) del panel
        add(titulo, BorderLayout.NORTH);

        // ─── FILA DE FICHAS (TONALIDADES) ───────────────────────────
        // Crea un panel con GridLayout: 1 fila, tantas columnas como tonalidades
        // Espaciado horizontal de 5px entre fichas
        JPanel filaTonos = new JPanel(new GridLayout(1, tonalidades.size(), 5, 0));
        // Establece el mismo fondo gris para la fila
        filaTonos.setBackground(new Color(240, 240, 240));

        // Itera sobre cada tonalidad para crear su ficha visual
        for (int i = 0; i < tonalidades.size(); i++) {
            Color tono = tonalidades.get(i);
            boolean esBase = (i == INDICE_BASE);  // Si es el color base (índice 4)

            // Crea la ficha para esta tonalidad
            JPanel ficha = crearFicha(tono, esBase);
            // Añade la ficha a la fila
            filaTonos.add(ficha);
        }

        // Añade la fila de fichas en la parte central del panel
        add(filaTonos, BorderLayout.CENTER);

        // Recalcula el layout y redibuja el panel con los nuevos componentes
        revalidate();
        repaint();  // Solicita que se redibuje
    }

    // ── Construcción de cada ficha ──────────────────────────────────
    /**
     * Crea una ficha (panel pequeño) para representar una tonalidad.
     *
     * @param tono El color de la tonalidad
     * @param esBase Indica si es el color base (para resaltarlo con borde
     * dorado)
     * @return JPanel configurado que representa la ficha
     */
    private JPanel crearFicha(Color tono, boolean esBase) {
        // Crea un panel con BorderLayout para organizar sus elementos
        JPanel ficha = new JPanel(new BorderLayout());
        // Establece el color de fondo de la ficha como la tonalidad
        ficha.setBackground(tono);
        // Hace el fondo opaco para que se vea el color
        ficha.setOpaque(true);
        // Establece el tamaño preferido de cada ficha (ancho 55px, alto 60px)
        ficha.setPreferredSize(new Dimension(55, 60));
        // Cambia el cursor a mano cuando pasa sobre la ficha (indicador de clic)
        ficha.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ─── BORDE DE LA FICHA ─────────────────────────────────────
        // Borde especial para el color base (dorado, más grueso)
        if (esBase) {
            ficha.setBorder(BorderFactory.createLineBorder(new Color(200, 160, 0), 3));
        } else {
            // Borde normal para las demás tonalidades (gris, más delgado)
            ficha.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        }

        // ─── VALOR HEXADECIMAL (parte inferior) ────────────────────
        // Convierte el color RGB a formato hexadecimal (ej: #FF0000)
        String hex = String.format("#%02X%02X%02X",
                tono.getRed(), tono.getGreen(), tono.getBlue()
        );
        // Crea una etiqueta con el código hexadecimal centrado
        JLabel etHex = new JLabel(hex, SwingConstants.CENTER);
        // Usa fuente monoespaciada para mejor legibilidad, tamaño pequeño
        etHex.setFont(new Font("Monospaced", Font.PLAIN, 9));
        // Color de texto que contraste: blanco o negro según luminosidad del fondo
        etHex.setForeground(esTextoOscuro(tono) ? Color.BLACK : Color.WHITE);
        // Hace la etiqueta transparente para ver el fondo
        etHex.setOpaque(false);
        // Coloca la etiqueta en la parte inferior de la ficha
        ficha.add(etHex, BorderLayout.SOUTH);

        // ─── ETIQUETA "BASE" (parte superior, solo para el color base) ──
        if (esBase) {
            JLabel etBase = new JLabel("BASE", SwingConstants.CENTER);
            etBase.setFont(new Font("Arial", Font.BOLD, 9));
            // Mismo contraste de color según luminosidad
            etBase.setForeground(esTextoOscuro(tono) ? Color.BLACK : Color.WHITE);
            etBase.setOpaque(false);
            // Coloca la etiqueta en la parte superior de la ficha
            ficha.add(etBase, BorderLayout.NORTH);
        }

        // ─── EVENTOS DE RATÓN (interactividad) ─────────────────────
        ficha.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Cuando se hace clic, ejecuta el callback con el color seleccionado
                if (alSeleccionarTono != null) {
                    alSeleccionarTono.accept(tono);
                }
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                // Cuando el mouse entra, resalta con borde negro grueso
                ficha.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                // Aclara ligeramente el color de fondo para efecto hover
                ficha.setBackground(tono.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                // Cuando el mouse sale, restaura el color original
                ficha.setBackground(tono);
                // Restaura el borde original según si es base o no
                if (esBase) {
                    ficha.setBorder(BorderFactory.createLineBorder(new Color(200, 160, 0), 3));
                } else {
                    ficha.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
                }
            }
        });

        return ficha;  // Devuelve la ficha completamente configurada
    }

    /**
     * Decide si el texto sobre este color debe ser oscuro (negro) o claro
     * (blanco) para asegurar legibilidad. Usa la fórmula de luminancia relativa
     * percibida por el ojo humano.
     *
     * @param c El color de fondo
     * @return true si el texto debe ser oscuro, false si debe ser claro
     */
    private boolean esTextoOscuro(Color c) {
        // Fórmula de luminancia que pondera los colores según percepción humana
        // El ojo es más sensible al verde, luego al rojo, luego al azul
        double luminancia = 0.299 * c.getRed() // 29.9% del rojo
                + 0.587 * c.getGreen() // 58.7% del verde
                + 0.114 * c.getBlue();  // 11.4% del azul
        // Si la luminancia es > 128 (mitad de 255), el fondo es claro
        return luminancia > 128;  // Texto oscuro (negro) para fondos claros
    }
}
