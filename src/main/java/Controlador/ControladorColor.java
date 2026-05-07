package controlador;

import modelo.ModeloColor;
import modelo.ServicioVoz;
import vista.VentanaPrincipal;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ControladorColor {

    private final VentanaPrincipal ventana;
    private final ServicioVoz vozSintetizada;
    private final List<ModeloColor> listaCompletaColores;
    private ModeloColor colorActualmenteSeleccionado;
    private String categoriaActiva = "Todos";

    public ControladorColor(VentanaPrincipal ventana) {
        this.ventana = ventana;
        this.vozSintetizada = new ServicioVoz();
        this.listaCompletaColores = inicializarColores();
        this.colorActualmenteSeleccionado = listaCompletaColores.get(0);

        ventana.asignarControlador(this);
        ventana.mostrarListaColores(listaCompletaColores);
        ventana.actualizarVisualizacion(colorActualmenteSeleccionado);
        mostrarTonalidades(colorActualmenteSeleccionado);
    }

    public void seleccionarColor(ModeloColor modeloSeleccionado) {
        colorActualmenteSeleccionado = modeloSeleccionado;
        ventana.actualizarVisualizacion(modeloSeleccionado);
        mostrarTonalidades(modeloSeleccionado);
        vozSintetizada.pronunciar(modeloSeleccionado.getNombre());
    }

    public void seleccionarTono(Color tono) {
        String hex = String.format("#%02X%02X%02X", tono.getRed(), tono.getGreen(), tono.getBlue());
        ModeloColor temporal = new ModeloColor(hex, tono, colorActualmenteSeleccionado.getCategoria());
        ventana.actualizarVisualizacion(temporal);
    }

    public void leerColorActual() {
        if (colorActualmenteSeleccionado != null) {
            vozSintetizada.pronunciar(colorActualmenteSeleccionado.getNombre());
        }
    }

    public void filtrarPorCategoria(String categoria) {
        this.categoriaActiva = categoria;
        aplicarFiltros("");
    }

    public void buscarPorNombre(String texto) {
        aplicarFiltros(texto);
    }

    private void mostrarTonalidades(ModeloColor modelo) {
        List<Color> tonos = generarTonalidades(modelo.getColor());
        ventana.mostrarTonalidades(tonos, modelo.getNombre());
    }

    public List<Color> generarTonalidades(Color base) {
        List<Color> tonos = new ArrayList<>();
        int[] factoresTinte = {15, 30, 50, 70};

        for (int f : factoresTinte) {
            int r = 255 + (base.getRed() - 255) * f / 100;
            int g = 255 + (base.getGreen() - 255) * f / 100;
            int b = 255 + (base.getBlue() - 255) * f / 100;
            tonos.add(new Color(clamp(r), clamp(g), clamp(b)));
        }

        tonos.add(base);

        int[] factoresSombra = {25, 50, 70, 85};
        for (int f : factoresSombra) {
            int r = base.getRed() * (100 - f) / 100;
            int g = base.getGreen() * (100 - f) / 100;
            int b = base.getBlue() * (100 - f) / 100;
            tonos.add(new Color(clamp(r), clamp(g), clamp(b)));
        }

        return tonos;
    }

    private int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }

    private void aplicarFiltros(String texto) {
        List<ModeloColor> resultado = listaCompletaColores.stream()
                .filter(c -> categoriaActiva.equals("Todos") || c.getCategoria().equals(categoriaActiva))
                .filter(c -> texto.isEmpty() || c.getNombre().toLowerCase().contains(texto.toLowerCase()))
                .collect(Collectors.toList());
        ventana.mostrarListaColores(resultado);
    }

    private List<ModeloColor> inicializarColores() {
        List<ModeloColor> lista = new ArrayList<>();

        lista.add(new ModeloColor("Rojo", new Color(220, 50, 50), "Primarios"));
        lista.add(new ModeloColor("Azul", new Color(50, 80, 200), "Primarios"));
        lista.add(new ModeloColor("Amarillo", new Color(255, 210, 0), "Primarios"));

        lista.add(new ModeloColor("Verde", new Color(50, 170, 50), "Secundarios"));
        lista.add(new ModeloColor("Naranja", new Color(245, 130, 0), "Secundarios"));
        lista.add(new ModeloColor("Morado", new Color(140, 50, 180), "Secundarios"));

        lista.add(new ModeloColor("Blanco", new Color(255, 255, 255), "Neutros"));
        lista.add(new ModeloColor("Negro", new Color(15, 15, 15), "Neutros"));
        lista.add(new ModeloColor("Gris", new Color(150, 150, 150), "Neutros"));
        lista.add(new ModeloColor("Marron", new Color(139, 90, 43), "Neutros"));

        lista.add(new ModeloColor("Rosa", new Color(255, 105, 180), "Colores cálidos"));
        lista.add(new ModeloColor("Coral", new Color(255, 80, 60), "Colores cálidos"));
        lista.add(new ModeloColor("Fucsia", new Color(220, 20, 120), "Colores cálidos"));
        lista.add(new ModeloColor("Salmon", new Color(255, 140, 105), "Colores cálidos"));

        lista.add(new ModeloColor("Turquesa", new Color(30, 185, 185), "Colores fríos"));
        lista.add(new ModeloColor("Celeste", new Color(90, 190, 240), "Colores fríos"));
        lista.add(new ModeloColor("Indigo", new Color(60, 40, 160), "Colores fríos"));
        lista.add(new ModeloColor("Lavanda", new Color(180, 160, 220), "Colores fríos"));

        return lista;
    }
}
