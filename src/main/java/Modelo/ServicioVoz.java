package modelo;

public class ServicioVoz {

    public void pronunciar(String texto) {
        new Thread(() -> {
            try {
                String sistemaOperativo = System.getProperty("os.name").toLowerCase();
                ProcessBuilder constructor;

                if (sistemaOperativo.contains("win")) {
                    String comando = "Add-Type -AssemblyName System.Speech; "
                            + "$sintetizador = New-Object System.Speech.Synthesis.SpeechSynthesizer; "
                            + "$sintetizador.Rate = -2; "
                            + "$sintetizador.Speak('" + texto + "')";
                    constructor = new ProcessBuilder("powershell", "-Command", comando);
                } else if (sistemaOperativo.contains("mac")) {
                    constructor = new ProcessBuilder("say", "-r", "140", texto);
                } else {
                    constructor = new ProcessBuilder("espeak", "-v", "es", "-s", "130", texto);
                }

                constructor.inheritIO();
                Process proceso = constructor.start();
                proceso.waitFor();

            } catch (Exception error) {
                System.err.println("Error en el servicio de voz: " + error.getMessage());
            }
        }).start();
    }
}
