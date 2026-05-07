package modelo;

import java.awt.Color;

public class ModeloColor {

    private String nombre;
    private Color color;
    private String categoria;

    public ModeloColor(String nombre, Color color, String categoria) {
        this.nombre = nombre;
        this.color = color;
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public Color getColor() {
        return color;
    }

    public String getCategoria() {
        return categoria;
    }

    @Override
    public String toString() {
        return nombre + " [" + categoria + "]";
    }
}
