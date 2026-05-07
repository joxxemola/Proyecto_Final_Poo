package modelo;  // Define el paquete donde se encuentra esta clase (modelo de servicios)

/**
 * Servicio que proporciona funcionalidad de síntesis de voz (texto a voz).
 * Permite pronunciar texto usando el sintetizador de voz nativo del sistema
 * operativo.
 */
public class ServicioVoz {

    /**
     * Método principal que convierte texto a voz y lo reproduce. Ejecuta la
     * pronunciación en un hilo separado para no bloquear la interfaz de
     * usuario.
     *
     * @param texto El texto que se quiere pronunciar (ej: nombre de un color)
     */
    public void pronunciar(String texto) {
        // Crea un nuevo hilo (thread) para que la voz no bloquee la aplicación
        new Thread(() -> {
            try {
                // Detecta el sistema operativo donde se está ejecutando la aplicación
                // Convierte el nombre del SO a minúsculas para facilitar la comparación
                String sistemaOperativo = System.getProperty("os.name").toLowerCase();

                // ProcessBuilder permite ejecutar comandos del sistema operativo
                ProcessBuilder constructor;

                // ─── WINDOWS ─────────────────────────────────────────────
                // Verifica si el sistema operativo contiene "win" (Windows)
                if (sistemaOperativo.contains("win")) {

                    // Comando PowerShell que:
                    // 1. Carga la biblioteca System.Speech (sintetizador de voz de Windows)
                    // 2. Crea un objeto SpeechSynthesizer
                    // 3. Ajusta la velocidad de habla a -2 (más lento que normal)
                    // 4. Pronuncia el texto proporcionado
                    String comando = "Add-Type -AssemblyName System.Speech; "
                            + "$sintetizador = New-Object System.Speech.Synthesis.SpeechSynthesizer; "
                            + "$sintetizador.Rate = -2; "
                            + "$sintetizador.Speak('" + texto + "')";

                    // Configura el comando para ejecutarse con PowerShell
                    constructor = new ProcessBuilder("powershell", "-Command", comando);

                    // ─── MACOS ─────────────────────────────────────────────
                    // Verifica si el sistema operativo contiene "mac" (macOS)
                } else if (sistemaOperativo.contains("mac")) {

                    // Comando 'say' es el sintetizador nativo de macOS
                    // -r 140: ajusta la velocidad de habla a 140 palabras por minuto
                    constructor = new ProcessBuilder("say", "-r", "140", texto);

                    // ─── LINUX / UNIX ─────────────────────────────────────
                } else {

                    // Comando 'espeak' es el sintetizador de voz para Linux
                    // -v es: selecciona la voz en español
                    // -s 130: ajusta la velocidad a 130 palabras por minuto
                    constructor = new ProcessBuilder("espeak", "-v", "es", "-s", "130", texto);
                }

                // Hereda la entrada/salida del proceso actual para que se pueda escuchar el audio
                constructor.inheritIO();

                // Inicia el proceso (ejecuta el comando del sistema operativo)
                Process proceso = constructor.start();

                // Espera a que termine el proceso (que termine de hablar)
                proceso.waitFor();

            } catch (Exception error) {
                // Si ocurre algún error (ej: comando no encontrado), lo muestra en consola
                System.err.println("Error en el servicio de voz: " + error.getMessage());
            }
        }).start();  // Inicia el hilo
    }
}
