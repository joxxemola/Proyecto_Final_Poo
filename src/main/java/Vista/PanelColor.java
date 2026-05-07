package vista;

import javax.swing.*;
import java.awt.*;

public class PanelColor extends JPanel {

    private String nombreColor = "Selecciona un color";
    private Color colorActual = Color.LIGHT_GRAY;

    public PanelColor() {
        setPreferredSize(new Dimension(500, 200));
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3));
    }

    @Override
    protected void paintComponent(Graphics graficos) {
        super.paintComponent(graficos);
        Graphics2D graficos2D = (Graphics2D) graficos;

        graficos2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graficos2D.setColor(colorActual);
        graficos2D.fillRect(0, 0, getWidth(), getHeight());

        graficos2D.setFont(new Font("Arial", Font.BOLD, 42));
        graficos2D.setColor(Color.BLACK);

        FontMetrics medidas = graficos2D.getFontMetrics();
        int posX = (getWidth() - medidas.stringWidth(nombreColor)) / 2;
        int posY = (getHeight() / 2) + medidas.getAscent() / 2 - 10;
        graficos2D.drawString(nombreColor, posX, posY);

        String valorHex = String.format("#%02X%02X%02X",
                colorActual.getRed(), colorActual.getGreen(), colorActual.getBlue());

        graficos2D.setFont(new Font("Arial", Font.PLAIN, 20));
        graficos2D.setColor(Color.BLACK);

        FontMetrics medidasPequenas = graficos2D.getFontMetrics();
        int posXHex = (getWidth() - medidasPequenas.stringWidth(valorHex)) / 2;
        graficos2D.drawString(valorHex, posXHex, posY + 40);
    }

    public void actualizarColor(Color nuevoColor, String nuevoNombre) {
        this.colorActual = nuevoColor;
        this.nombreColor = nuevoNombre.toUpperCase();
        repaint();
    }
}
