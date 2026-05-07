package controlador;  // Define el paquete donde se encuentra esta clase

// Importa la clase ModeloColor del paquete modelo para manejar objetos de color
import modelo.ModeloColor;
// Importa la clase ServicioVoz del paquete modelo para la síntesis de voz
import modelo.ServicioVoz;
// Importa la clase VentanaPrincipal del paquete vista para la interfaz gráfica
import vista.VentanaPrincipal;

// Importa la clase Color de AWT para manejar colores RGB
import java.awt.Color;
// Importa ArrayList para crear listas dinámicas
import java.util.ArrayList;
// Importa List para trabajar con listas genéricas
import java.util.List;
// Importa Collectors para operaciones de recolección en Streams
import java.util.stream.Collectors;

/**
 * Controlador principal de la aplicación. Conecta el modelo con la vista y
 * gestiona toda la lógica.
 */
public class ControladorColor {

    // Ventana principal de la aplicación (vista)
    private final VentanaPrincipal ventana;
    // Servicio para sintetizar voz (lectura de nombres de colores)
    private final ServicioVoz vozSintetizada;
    // Lista completa de todos los colores disponibles en la aplicación
    private final List<ModeloColor> listaCompletaColores;
    // Color que el usuario tiene seleccionado actualmente
    private ModeloColor colorActualmenteSeleccionado;
    // Categoría activa para filtrar colores (ej: "Todos", "Primarios", etc.)
    private String categoriaActiva = "Todos";

    // Constructor del controlador - recibe la ventana principal
    public ControladorColor(VentanaPrincipal ventana) {
        // Inicializa la referencia a la ventana
        this.ventana = ventana;
        // Crea una nueva instancia del servicio de voz
        this.vozSintetizada = new ServicioVoz();
        // Carga todos los colores disponibles llamando al método inicializador
        this.listaCompletaColores = inicializarColores();
        // Selecciona el primer color de la lista como color actual
        this.colorActualmenteSeleccionado = listaCompletaColores.get(0);

        // Asigna este controlador a la ventana para que pueda comunicarse
        ventana.asignarControlador(this);
        // Muestra todos los colores en la cuadrícula de la ventana
        ventana.mostrarListaColores(listaCompletaColores);
        // Actualiza la vista para mostrar el color seleccionado actualmente
        ventana.actualizarVisualizacion(colorActualmenteSeleccionado);

        // Muestra las tonalidades del color inicial
        mostrarTonalidades(colorActualmenteSeleccionado);
    }

    // ── Acciones del usuario ────────────────────────────────────────
    /**
     * El usuario hizo clic en un color de la cuadrícula. Actualiza la vista,
     * genera sus tonalidades y lo lee.
     */
    public void seleccionarColor(ModeloColor modeloSeleccionado) {
        // Guarda el color que el usuario seleccionó
        colorActualmenteSeleccionado = modeloSeleccionado;
        // Actualiza la interfaz gráfica para mostrar este color
        ventana.actualizarVisualizacion(modeloSeleccionado);
        // Genera y muestra las tonalidades del color seleccionado
        mostrarTonalidades(modeloSeleccionado);
        // Pronuncia en voz alta el nombre del color seleccionado
        vozSintetizada.pronunciar(modeloSeleccionado.getNombre());
    }

    /**
     * El usuario clicó una ficha de tonalidad. Solo actualiza la visualización
     * principal con ese tono exacto.
     */
    public void seleccionarTono(Color tono) {
        // Convierte el color a formato hexadecimal (ej: #FF0000)
        String hex = String.format("#%02X%02X%02X",
                tono.getRed(), tono.getGreen(), tono.getBlue());
        // Crea un objeto ModeloColor temporal con el tono seleccionado
        // Mantiene la misma categoría del color original
        ModeloColor temporal = new ModeloColor(hex, tono,
                colorActualmenteSeleccionado.getCategoria());
        // Actualiza la visualización principal con este tono
        ventana.actualizarVisualizacion(temporal);
        // Sin voz: las tonalidades son solo visuales
    }

    // Lee en voz alta el nombre del color actualmente seleccionado
    public void leerColorActual() {
        if (colorActualmenteSeleccionado != null) // Verifica que haya un color seleccionado
        {
            vozSintetizada.pronunciar(colorActualmenteSeleccionado.getNombre());
        }
    }

    // Filtra los colores mostrados por categoría (ej: "Primarios", "Neutros", etc.)
    public void filtrarPorCategoria(String categoria) {
        this.categoriaActiva = categoria;  // Actualiza la categoría activa
        aplicarFiltros("");  // Aplica el filtro con texto vacío (solo categoría)
    }

    // Busca colores cuyo nombre contenga el texto especificado
    public void buscarPorNombre(String texto) {
        aplicarFiltros(texto);  // Aplica el filtro por texto manteniendo la categoría
    }

    // ── Tonalidades ─────────────────────────────────────────────────
    // Genera y muestra las tonalidades de un color modelo
    private void mostrarTonalidades(ModeloColor modelo) {
        // Genera la lista de 9 tonalidades (4 claras, base, 4 oscuras)
        List<Color> tonos = generarTonalidades(modelo.getColor());
        // Muestra las tonalidades en la ventana con el nombre base
        ventana.mostrarTonalidades(tonos, modelo.getNombre());
    }

    /**
     * Genera 9 tonalidades a partir de un color base: 4 tintes (mezcla con
     * blanco) + base + 4 sombras (mezcla con negro).
     */
    public List<Color> generarTonalidades(Color base) {
        List<Color> tonos = new ArrayList<>();  // Lista para almacenar los tonos

        // 4 tintes: mezcla el color base con blanco (aclarado)
        int[] factoresTinte = {15, 30, 50, 70};  // Porcentajes de mezcla
        for (int f : factoresTinte) {
            // Interpola entre blanco (255) y el color base
            int r = 255 + (base.getRed() - 255) * f / 100;
            int g = 255 + (base.getGreen() - 255) * f / 100;
            int b = 255 + (base.getBlue() - 255) * f / 100;
            tonos.add(new Color(clamp(r), clamp(g), clamp(b)));  // Añade el tinte
        }

        // Color base puro (índice 4 en la lista)
        tonos.add(base);

        // 4 sombras: mezcla el color base con negro (oscurecido)
        int[] factoresSombra = {25, 50, 70, 85};  // Porcentajes de oscurecimiento
        for (int f : factoresSombra) {
            // Reduce cada componente RGB según el porcentaje
            int r = base.getRed() * (100 - f) / 100;
            int g = base.getGreen() * (100 - f) / 100;
            int b = base.getBlue() * (100 - f) / 100;
            tonos.add(new Color(clamp(r), clamp(g), clamp(b)));  // Añade la sombra
        }

        return tonos;  // Retorna la lista completa de 9 tonalidades
    }

    // Asegura que un valor esté dentro del rango válido RGB (0-255)
    private int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }

    // ── Filtros ─────────────────────────────────────────────────────
    // Aplica filtros de categoría y búsqueda por nombre
    private void aplicarFiltros(String texto) {
        // Filtra la lista completa usando Stream API
        List<ModeloColor> resultado = listaCompletaColores.stream()
                // Filtra por categoría (si no es "Todos", solo los que coinciden)
                .filter(c -> categoriaActiva.equals("Todos")
                || c.getCategoria().equals(categoriaActiva))
                // Filtra por nombre (si hay texto, solo los que contienen ese texto)
                .filter(c -> texto.isEmpty()
                || c.getNombre().toLowerCase().contains(texto.toLowerCase()))
                // Recoge los resultados en una nueva lista
                .collect(Collectors.toList());
        // Muestra la lista filtrada en la ventana
        ventana.mostrarListaColores(resultado);
    }

    // ── Base de datos de colores ────────────────────────────────────
    // Inicializa la base de datos con todos los colores disponibles
    private List<ModeloColor> inicializarColores() {
        List<ModeloColor> lista = new ArrayList<>();

        // Colores primarios
        lista.add(new ModeloColor("Rojo", new Color(220, 50, 50), "Primarios"));
        lista.add(new ModeloColor("Azul", new Color(50, 80, 200), "Primarios"));
        lista.add(new ModeloColor("Amarillo", new Color(255, 210, 0), "Primarios"));

        // Colores secundarios
        lista.add(new ModeloColor("Verde", new Color(50, 170, 50), "Secundarios"));
        lista.add(new ModeloColor("Naranja", new Color(245, 130, 0), "Secundarios"));
        lista.add(new ModeloColor("Morado", new Color(140, 50, 180), "Secundarios"));

        // Colores neutros
        lista.add(new ModeloColor("Blanco", new Color(255, 255, 255), "Neutros"));
        lista.add(new ModeloColor("Negro", new Color(15, 15, 15), "Neutros"));
        lista.add(new ModeloColor("Gris", new Color(150, 150, 150), "Neutros"));
        lista.add(new ModeloColor("Marron", new Color(139, 90, 43), "Neutros"));

        // Colores cálidos
        lista.add(new ModeloColor("Rosa", new Color(255, 105, 180), "Colores cálidos"));
        lista.add(new ModeloColor("Coral", new Color(255, 80, 60), "Colores cálidos"));
        lista.add(new ModeloColor("Fucsia", new Color(220, 20, 120), "Colores cálidos"));
        lista.add(new ModeloColor("Salmon", new Color(255, 140, 105), "Colores cálidos"));

        // Colores fríos
        lista.add(new ModeloColor("Turquesa", new Color(30, 185, 185), "Colores fríos"));
        lista.add(new ModeloColor("Celeste", new Color(90, 190, 240), "Colores fríos"));
        lista.add(new ModeloColor("Indigo", new Color(60, 40, 160), "Colores fríos"));
        lista.add(new ModeloColor("Lavanda", new Color(180, 160, 220), "Colores fríos"));

        return lista;  // Retorna la lista completa de colores
    }
}
