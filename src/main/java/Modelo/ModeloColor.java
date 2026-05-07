package modelo;  // Define el paquete donde se encuentra esta clase (modelo de datos)

// Importa la clase Color de AWT para poder manejar colores RGB
import java.awt.Color;

/**
 * Modelo que representa un color con sus propiedades. Esta clase es parte del
 * patrón MVC (Modelo-Vista-Controlador)
 */
public class ModeloColor {

    // Almacena el nombre del color (ej: "Rojo", "Azul", "Verde")
    private String nombre;

    // Almacena el valor RGB del color usando la clase Color de Java
    private Color color;

    // Almacena la categoría a la que pertenece el color (ej: "Primarios", "Neutros")
    private String categoria;

    // Constructor - Se llama cuando se crea un nuevo objeto ModeloColor
    // Recibe tres parámetros: nombre, color RGB y categoría
    public ModeloColor(String nombre, Color color, String categoria) {
        this.nombre = nombre;      // Asigna el nombre recibido al atributo de la clase
        this.color = color;        // Asigna el color recibido al atributo de la clase
        this.categoria = categoria; // Asigna la categoría recibida al atributo de la clase
    }

    // Getters (métodos de acceso) - Permiten obtener los valores de los atributos privados
    /**
     * Devuelve el nombre del color
     *
     * @return El nombre del color como String
     */
    public String getNombre() {
        return nombre;  // Retorna el nombre almacenado
    }

    /**
     * Devuelve el objeto Color con los valores RGB
     *
     * @return El color como objeto Color de Java
     */
    public Color getColor() {
        return color;  // Retorna el objeto Color completo
    }

    /**
     * Devuelve la categoría del color
     *
     * @return La categoría como String
     */
    public String getCategoria() {
        return categoria;  // Retorna la categoría almacenada
    }

    /**
     * Sobrescribe el método toString de Object Se usa para representar el
     * objeto como texto
     *
     * @return Una cadena con el formato "nombre [categoría]"
     */
    @Override
    public String toString() {
        // Concatena el nombre, un espacio, y la categoría entre corchetes
        return nombre + " [" + categoria + "]";
    }
}
