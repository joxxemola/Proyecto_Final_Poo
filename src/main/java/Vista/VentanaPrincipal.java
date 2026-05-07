package vista;

import controlador.ControladorColor;
import modelo.ModeloColor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private PanelColor panelVisualizacion;
    private PanelTonalidades panelTonalidades;
    private JButton botonLeer;
    private JPanel panelBotones;
    private JComboBox<String> listaCategorias;
    private JTextField campoBusqueda;

    private ControladorColor controlador;

    public VentanaPrincipal() {
        inicializarComponentes();
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("Detector de Colores con Voz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(620, 780));
        pack();
        setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        JPanel contenedor = new JPanel(new BorderLayout(10, 10));
        contenedor.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contenedor.setBackground(new Color(240, 240, 240));

        JLabel etiquetaTitulo = new JLabel("Detector de Colores con Voz", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        etiquetaTitulo.setForeground(Color.BLACK);
        etiquetaTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        contenedor.add(etiquetaTitulo, BorderLayout.NORTH);

        panelVisualizacion = new PanelColor();
        panelTonalidades = new PanelTonalidades();

        panelTonalidades.setAlSeleccionarTono(tono -> {
            if (controlador != null) {
                controlador.seleccionarTono(tono);
            }
        });

        JPanel panelCentro = new JPanel(new BorderLayout(0, 6));
        panelCentro.setBackground(new Color(240, 240, 240));
        panelCentro.add(panelVisualizacion, BorderLayout.NORTH);
        panelCentro.add(panelTonalidades, BorderLayout.CENTER);
        contenedor.add(panelCentro, BorderLayout.CENTER);

        JPanel panelControles = new JPanel();
        panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.Y_AXIS));
        panelControles.setBackground(new Color(240, 240, 240));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBusqueda.setBackground(new Color(240, 240, 240));

        JLabel etBusqueda = new JLabel("🔍 Buscar:");
        etBusqueda.setFont(new Font("Arial", Font.PLAIN, 16));
        etBusqueda.setForeground(Color.BLACK);

        campoBusqueda = new JTextField();
        campoBusqueda.setFont(new Font("Arial", Font.PLAIN, 15));
        campoBusqueda.setForeground(Color.BLACK);
        campoBusqueda.setPreferredSize(new Dimension(250, 35));
        campoBusqueda.setToolTipText("Escribe el nombre de un color...");

        JButton botonLimpiar = new JButton("✕");
        botonLimpiar.setFont(new Font("Arial", Font.BOLD, 14));
        botonLimpiar.setForeground(Color.BLACK);
        botonLimpiar.setBackground(new Color(210, 210, 210));
        botonLimpiar.setOpaque(true);
        botonLimpiar.setFocusPainted(false);
        botonLimpiar.setPreferredSize(new Dimension(38, 35));
        botonLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonLimpiar.setToolTipText("Limpiar búsqueda");
        botonLimpiar.addActionListener(e -> campoBusqueda.setText(""));

        campoBusqueda.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                notificarBusqueda();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                notificarBusqueda();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                notificarBusqueda();
            }

            private void notificarBusqueda() {
                if (controlador != null) {
                    controlador.buscarPorNombre(campoBusqueda.getText().trim());
                }
            }
        });

        panelBusqueda.add(etBusqueda);
        panelBusqueda.add(campoBusqueda);
        panelBusqueda.add(botonLimpiar);
        panelControles.add(panelBusqueda);

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelFiltro.setBackground(new Color(240, 240, 240));

        JLabel etCategoria = new JLabel("Categoría:");
        etCategoria.setFont(new Font("Arial", Font.PLAIN, 16));
        etCategoria.setForeground(Color.BLACK);

        listaCategorias = new JComboBox<>(new String[]{
            "Todos", "Primarios", "Secundarios", "Neutros", "Colores cálidos", "Colores fríos"
        });
        listaCategorias.setFont(new Font("Arial", Font.PLAIN, 15));
        listaCategorias.setForeground(Color.BLACK);
        listaCategorias.setPreferredSize(new Dimension(185, 35));
        listaCategorias.addActionListener(evento -> {
            if (controlador != null) {
                campoBusqueda.setText("");
                controlador.filtrarPorCategoria((String) listaCategorias.getSelectedItem());
            }
        });

        panelFiltro.add(etCategoria);
        panelFiltro.add(listaCategorias);
        panelControles.add(panelFiltro);

        panelBotones = new JPanel(new GridLayout(0, 4, 8, 8));
        panelBotones.setBackground(new Color(240, 240, 240));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        JScrollPane desplazamiento = new JScrollPane(panelBotones);
        desplazamiento.setPreferredSize(new Dimension(560, 200));
        desplazamiento.setBorder(null);
        panelControles.add(desplazamiento);

        botonLeer = crearBotonAccion("Leer Color", new Color(30, 130, 190), Color.BLACK);
        botonLeer.addActionListener(e -> {
            if (controlador != null) {
                controlador.leerColorActual();
            }
        });

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelAcciones.setBackground(new Color(240, 240, 240));
        panelAcciones.add(botonLeer);
        panelControles.add(panelAcciones);

        contenedor.add(panelControles, BorderLayout.SOUTH);
        add(contenedor);
    }

    private JButton crearBotonAccion(String texto, Color colorFondo, Color colorTexto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 18));
        boton.setBackground(colorFondo);
        boton.setForeground(colorTexto);
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(200, 55));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return boton;
    }

    public void mostrarListaColores(List<ModeloColor> listaColores) {
        panelBotones.removeAll();

        for (ModeloColor modelo : listaColores) {
            JPanel tarjeta = new JPanel(new BorderLayout());
            tarjeta.setBackground(modelo.getColor());
            tarjeta.setOpaque(true);
            tarjeta.setPreferredSize(new Dimension(120, 60));
            tarjeta.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
            tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
            tarjeta.setToolTipText("Seleccionar: " + modelo.getNombre());

            JLabel etNombre = new JLabel(modelo.getNombre(), SwingConstants.CENTER);
            etNombre.setFont(new Font("Arial", Font.BOLD, 14));
            etNombre.setForeground(Color.BLACK);
            etNombre.setOpaque(false);
            tarjeta.add(etNombre, BorderLayout.CENTER);

            tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (controlador != null) {
                        controlador.seleccionarColor(modelo);
                    }
                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    tarjeta.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    tarjeta.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                }
            });

            panelBotones.add(tarjeta);
        }

        panelBotones.revalidate();
        panelBotones.repaint();
    }

    public void actualizarVisualizacion(ModeloColor modelo) {
        panelVisualizacion.actualizarColor(modelo.getColor(), modelo.getNombre());
    }

    public void mostrarTonalidades(java.util.List<java.awt.Color> tonos, String nombreBase) {
        panelTonalidades.actualizarTonalidades(tonos, nombreBase);
    }

    public void asignarControlador(ControladorColor controlador) {
        this.controlador = controlador;
    }
}
