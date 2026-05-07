package vista;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class PanelTonalidades extends JPanel {

    private Consumer<Color> alSeleccionarTono;
    private static final int INDICE_BASE = 4;

    public PanelTonalidades() {
        setLayout(new BorderLayout(0, 4));
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createEmptyBorder(6, 0, 4, 0));
    }

    public void setAlSeleccionarTono(Consumer<Color> callback) {
        this.alSeleccionarTono = callback;
    }

    public void actualizarTonalidades(List<Color> tonalidades, String nombreBase) {
        removeAll();

        JLabel titulo = new JLabel("Tonalidades de " + nombreBase + ":", SwingConstants.LEFT);
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setForeground(Color.DARK_GRAY);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 2, 4, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel filaTonos = new JPanel(new GridLayout(1, tonalidades.size(), 5, 0));
        filaTonos.setBackground(new Color(240, 240, 240));

        for (int i = 0; i < tonalidades.size(); i++) {
            Color tono = tonalidades.get(i);
            boolean esBase = (i == INDICE_BASE);
            JPanel ficha = crearFicha(tono, esBase);
            filaTonos.add(ficha);
        }

        add(filaTonos, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JPanel crearFicha(Color tono, boolean esBase) {
        JPanel ficha = new JPanel(new BorderLayout());
        ficha.setBackground(tono);
        ficha.setOpaque(true);
        ficha.setPreferredSize(new Dimension(55, 60));
        ficha.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (esBase) {
            ficha.setBorder(BorderFactory.createLineBorder(new Color(200, 160, 0), 3));
        } else {
            ficha.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        }

        String hex = String.format("#%02X%02X%02X", tono.getRed(), tono.getGreen(), tono.getBlue());
        JLabel etHex = new JLabel(hex, SwingConstants.CENTER);
        etHex.setFont(new Font("Monospaced", Font.PLAIN, 9));
        etHex.setForeground(esTextoOscuro(tono) ? Color.BLACK : Color.WHITE);
        etHex.setOpaque(false);
        ficha.add(etHex, BorderLayout.SOUTH);

        if (esBase) {
            JLabel etBase = new JLabel("BASE", SwingConstants.CENTER);
            etBase.setFont(new Font("Arial", Font.BOLD, 9));
            etBase.setForeground(esTextoOscuro(tono) ? Color.BLACK : Color.WHITE);
            etBase.setOpaque(false);
            ficha.add(etBase, BorderLayout.NORTH);
        }

        ficha.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (alSeleccionarTono != null) {
                    alSeleccionarTono.accept(tono);
                }
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                ficha.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                ficha.setBackground(tono.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                ficha.setBackground(tono);
                if (esBase) {
                    ficha.setBorder(BorderFactory.createLineBorder(new Color(200, 160, 0), 3));
                } else {
                    ficha.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
                }
            }
        });

        return ficha;
    }

    private boolean esTextoOscuro(Color c) {
        double luminancia = 0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue();
        return luminancia > 128;
    }
}
