package vista;  // Define el paquete donde se encuentra esta clase (interfaz gráfica principal)

// Importa el controlador para comunicarse con la lógica de la aplicación
import controlador.ControladorColor;
// Importa el modelo de color para mostrar los datos
import modelo.ModeloColor;

// Importa clases de Swing para componentes gráficos
import javax.swing.*;
// Importa eventos del documento para detectar cambios en el campo de texto
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
// Importa AWT para layout, colores y dimensiones
import java.awt.*;
// Importa List para manejar colecciones de colores
import java.util.List;

/**
 * Ventana principal de la aplicación. Es la vista principal que muestra todos
 * los componentes gráficos: - Panel de visualización del color seleccionado -
 * Tonalidades del color - Cuadrícula de colores disponibles - Controles de
 * búsqueda y filtrado - Botón para leer el color con voz
 */
public class VentanaPrincipal extends JFrame {  // Extiende JFrame (ventana de Swing)

    // Componentes gráficos de la ventana
    private PanelColor panelVisualizacion;   // Panel que muestra el color actual
    private PanelTonalidades panelTonalidades;     // Panel que muestra las 9 tonalidades
    private JButton botonLeer;            // Botón para leer el color con voz
    private JPanel panelBotones;         // Panel con la cuadrícula de colores
    private JComboBox<String> listaCategorias;      // ComboBox para filtrar por categoría
    private JTextField campoBusqueda;        // Campo de texto para buscar por nombre

    private ControladorColor controlador;  // Referencia al controlador de la aplicación

    // Constructor - se llama al crear la ventana
    public VentanaPrincipal() {
        inicializarComponentes();  // Crea y configura todos los componentes
        configurarVentana();       // Configura las propiedades de la ventana
    }

    // Configura las propiedades básicas de la ventana
    private void configurarVentana() {
        setTitle("Detector de Colores con Voz");     // Título de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la app al cerrar ventana
        setMinimumSize(new Dimension(620, 780));     // Tamaño mínimo
        pack();                          // Ajusta el tamaño automáticamente
        setLocationRelativeTo(null);     // Centra la ventana en la pantalla
    }

    // Inicializa todos los componentes gráficos
    private void inicializarComponentes() {
        // Layout principal de la ventana (BorderLayout con espacios de 10px)
        setLayout(new BorderLayout(10, 10));

        // Panel contenedor principal con borde y fondo
        JPanel contenedor = new JPanel(new BorderLayout(10, 10));
        contenedor.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contenedor.setBackground(new Color(240, 240, 240));  // Gris claro

        // ── Título de la aplicación ──────────────────────────────────
        JLabel etiquetaTitulo = new JLabel(
                "Detector de Colores con Voz", SwingConstants.CENTER
        );
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 26));  // Fuente grande y negrita
        etiquetaTitulo.setForeground(Color.BLACK);  // Color negro
        etiquetaTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        contenedor.add(etiquetaTitulo, BorderLayout.NORTH);  // Título arriba

        // ── Zona central: visualización principal + tonalidades ─────
        panelVisualizacion = new PanelColor();      // Panel que muestra el color
        panelTonalidades = new PanelTonalidades(); // Panel de tonalidades

        // Configura el callback de las tonalidades (qué hacer cuando se hace clic)
        panelTonalidades.setAlSeleccionarTono(tono -> {
            if (controlador != null) {
                controlador.seleccionarTono(tono);  // Notifica al controlador
            }
        });

        // Panel que contiene ambos (visualización arriba, tonalidades abajo)
        JPanel panelCentro = new JPanel(new BorderLayout(0, 6));
        panelCentro.setBackground(new Color(240, 240, 240));
        panelCentro.add(panelVisualizacion, BorderLayout.NORTH);  // Color seleccionado
        panelCentro.add(panelTonalidades, BorderLayout.CENTER); // Tonalidades
        contenedor.add(panelCentro, BorderLayout.CENTER);  // Centro de la ventana

        // ── Panel de controles (parte inferior) ──────────────────────
        JPanel panelControles = new JPanel();
        panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.Y_AXIS));  // Layout vertical
        panelControles.setBackground(new Color(240, 240, 240));

        // ── BUSCADOR POR NOMBRE ─────────────────────────────────────
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBusqueda.setBackground(new Color(240, 240, 240));

        JLabel etBusqueda = new JLabel("🔍 Buscar:");  // Etiqueta con lupa
        etBusqueda.setFont(new Font("Arial", Font.PLAIN, 16));
        etBusqueda.setForeground(Color.BLACK);

        campoBusqueda = new JTextField();   // Campo de texto para escribir
        campoBusqueda.setFont(new Font("Arial", Font.PLAIN, 15));
        campoBusqueda.setForeground(Color.BLACK);
        campoBusqueda.setPreferredSize(new Dimension(250, 35));
        campoBusqueda.setToolTipText("Escribe el nombre de un color...");  // Tooltip

        // Botón para limpiar la búsqueda
        JButton botonLimpiar = new JButton("✕");
        botonLimpiar.setFont(new Font("Arial", Font.BOLD, 14));
        botonLimpiar.setForeground(Color.BLACK);
        botonLimpiar.setBackground(new Color(210, 210, 210));
        botonLimpiar.setOpaque(true);  // Hace visible el fondo
        botonLimpiar.setFocusPainted(false);  // Quita el borde de enfoque
        botonLimpiar.setPreferredSize(new Dimension(38, 35));
        botonLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));  // Cursor mano
        botonLimpiar.setToolTipText("Limpiar búsqueda");
        botonLimpiar.addActionListener(e -> campoBusqueda.setText(""));  // Limpia el texto

        // Listener para detectar cambios en el campo de búsqueda
        campoBusqueda.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                notificarBusqueda();
            }  // Al escribir

            @Override
            public void removeUpdate(DocumentEvent e) {
                notificarBusqueda();
            }  // Al borrar

            @Override
            public void changedUpdate(DocumentEvent e) {
                notificarBusqueda();
            }  // Cambios de formato

            private void notificarBusqueda() {
                if (controlador != null) {
                    controlador.buscarPorNombre(campoBusqueda.getText().trim());  // Filtra colores
                }
            }
        });

        panelBusqueda.add(etBusqueda);
        panelBusqueda.add(campoBusqueda);
        panelBusqueda.add(botonLimpiar);
        panelControles.add(panelBusqueda);

        // ── FILTRO POR CATEGORÍA ────────────────────────────────────
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelFiltro.setBackground(new Color(240, 240, 240));

        JLabel etCategoria = new JLabel("Categoría:");
        etCategoria.setFont(new Font("Arial", Font.PLAIN, 16));
        etCategoria.setForeground(Color.BLACK);

        // ComboBox con todas las categorías disponibles
        listaCategorias = new JComboBox<>(new String[]{
            "Todos", "Primarios", "Secundarios",
            "Neutros", "Colores cálidos", "Colores fríos"
        });
        listaCategorias.setFont(new Font("Arial", Font.PLAIN, 15));
        listaCategorias.setForeground(Color.BLACK);
        listaCategorias.setPreferredSize(new Dimension(185, 35));
        listaCategorias.addActionListener(evento -> {
            if (controlador != null) {
                campoBusqueda.setText("");  // Limpia la búsqueda al cambiar categoría
                controlador.filtrarPorCategoria((String) listaCategorias.getSelectedItem());
            }
        });

        panelFiltro.add(etCategoria);
        panelFiltro.add(listaCategorias);
        panelControles.add(panelFiltro);

        // ── CUADRÍCULA DE COLORES (con scroll) ──────────────────────
        panelBotones = new JPanel(new GridLayout(0, 4, 8, 8));  // 4 columnas, espacio 8px
        panelBotones.setBackground(new Color(240, 240, 240));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        // Panel desplazable (scroll) para la cuadrícula
        JScrollPane desplazamiento = new JScrollPane(panelBotones);
        desplazamiento.setPreferredSize(new Dimension(560, 200));
        desplazamiento.setBorder(null);  // Sin borde
        panelControles.add(desplazamiento);

        // ── BOTONES DE ACCIÓN ───────────────────────────────────────
        botonLeer = crearBotonAccion("Leer Color", new Color(30, 130, 190), Color.BLACK);
        botonLeer.addActionListener(e -> {
            if (controlador != null) {
                controlador.leerColorActual();  // Lee el color actual
            }
        });

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelAcciones.setBackground(new Color(240, 240, 240));
        panelAcciones.add(botonLeer);
        panelControles.add(panelAcciones);

        contenedor.add(panelControles, BorderLayout.SOUTH);  // Controles abajo
        add(contenedor);  // Añade el contenedor principal a la ventana
    }

    /**
     * Método auxiliar para crear botones con estilo uniforme
     *
     * @param texto Texto del botón
     * @param colorFondo Color de fondo
     * @param colorTexto Color del texto
     * @return JButton configurado
     */
    private JButton crearBotonAccion(String texto, Color colorFondo, Color colorTexto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 18));
        boton.setBackground(colorFondo);
        boton.setForeground(colorTexto);
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setFocusPainted(false);  // Sin borde de enfoque
        boton.setPreferredSize(new Dimension(200, 55));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));  // Cursor mano
        boton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return boton;
    }

    // ── Métodos públicos para que el Controlador interactúe con la vista ──
    /**
     * Muestra la lista de colores en la cuadrícula
     *
     * @param listaColores Lista de ModeloColor a mostrar
     */
    public void mostrarListaColores(List<ModeloColor> listaColores) {
        panelBotones.removeAll();  // Limpia la cuadrícula actual

        // Crea una tarjeta (botón) para cada color
        for (ModeloColor modelo : listaColores) {
            JPanel tarjeta = new JPanel(new BorderLayout());
            tarjeta.setBackground(modelo.getColor());  // Fondo del color
            tarjeta.setOpaque(true);
            tarjeta.setPreferredSize(new Dimension(120, 60));
            tarjeta.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
            tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
            tarjeta.setToolTipText("Seleccionar: " + modelo.getNombre());

            JLabel etNombre = new JLabel(modelo.getNombre(), SwingConstants.CENTER);
            etNombre.setFont(new Font("Arial", Font.BOLD, 14));
            etNombre.setForeground(Color.BLACK);
            etNombre.setOpaque(false);  // Transparente para ver el fondo color
            tarjeta.add(etNombre, BorderLayout.CENTER);

            // Eventos de ratón para la tarjeta
            tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (controlador != null) {
                        controlador.seleccionarColor(modelo);  // Selecciona color
                    }
                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    tarjeta.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));  // Resalta
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    tarjeta.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));  // Restaura
                }
            });

            panelBotones.add(tarjeta);
        }

        panelBotones.revalidate();  // Recalcula el layout
        panelBotones.repaint();     // Redibuja el panel
    }

    /**
     * Actualiza el panel de visualización con un nuevo color
     *
     * @param modelo ModeloColor con el nuevo color y nombre
     */
    public void actualizarVisualizacion(ModeloColor modelo) {
        panelVisualizacion.actualizarColor(modelo.getColor(), modelo.getNombre());
    }

    /**
     * Muestra las tonalidades del color seleccionado
     *
     * @param tonos Lista de 9 colores (tonalidades)
     * @param nombreBase Nombre del color base
     */
    public void mostrarTonalidades(java.util.List<java.awt.Color> tonos, String nombreBase) {
        panelTonalidades.actualizarTonalidades(tonos, nombreBase);
    }

    /**
     * Asigna el controlador a la vista
     *
     * @param controlador ControladorColor que manejará la lógica
     */
    public void asignarControlador(ControladorColor controlador) {
        this.controlador = controlador;
    }
}

    /**
     * EJEMPLO  VISUAL
     * 
     * 
┌─────────────────────────────────────────────────┐
│         Detector de Colores con Voz             │ (Título)
├─────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────┐    │
│  │  ROJO                                   │    │ Panel de
│  │  #FF0000                                │    │ visualización
│  └─────────────────────────────────────────┘    │
│  ┌────┬────┬────┬────┬────┬────┬────┬────┬────┐ │
│  │ #FF│ #FF│ #FF│ #FF│BASE│ #CC│ #99│ #66│ #33│ │ Panel de
│  │ C4C│ 999│ 666│ 333│ ROJ│ 000│ 000│ 000│ 000│ │ tonalidades
│  └────┴────┴────┴────┴────┴────┴────┴────┴────┘ │
├─────────────────────────────────────────────────┤
│  Buscar: [____________] [✕]                    │ Buscador
│  Categoría: [Primarios▼]                        │ Filtro
│  ┌─────┬─────┬─────┬─────┐                      │
│  │Rojo │Azul │Ama...│Verde│                     │
│  ├─────┼─────┼─────┼─────┤                      │ Cuadrícula
│  │Nara...│Mora...│Bla...│Negro│                 │ de colores
│  └─────┴─────┴─────┴─────┘                      │ (con scroll)
│  ┌─────────────────────────────┐                │
│  │       Leer Color            │                │ Botón acción
│  └─────────────────────────────┘                │
└─────────────────────────────────────────────────┘
     */
