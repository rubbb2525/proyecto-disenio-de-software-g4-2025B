package vistas.componentes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StyledButton extends JButton {
    private Color colorNormal;
    private Color colorHover;
    private Color colorPress;

    public enum TipoBoton {
        PRIMARIO,
        SECUNDARIO,
        EXITO,
        PELIGRO,
        ADVERTENCIA
    }

    public StyledButton(String text) {
        this(text, TipoBoton.PRIMARIO);
    }

    public StyledButton(String text, TipoBoton tipo) {
        super(text);
        configurarEstilo(tipo);
        configurarEfectos();
    }

    private void configurarEstilo(TipoBoton tipo) {
        switch (tipo) {
            case PRIMARIO:
                colorNormal = new Color(0, 61, 165);      // #003DA5
                colorHover = new Color(16, 84, 184);      // #1054B8
                colorPress = new Color(0, 45, 125);
                setForeground(Color.WHITE);
                break;
            case SECUNDARIO:
                colorNormal = new Color(149, 165, 166);
                colorHover = new Color(127, 140, 141);
                colorPress = new Color(108, 122, 137);
                setForeground(Color.WHITE);
                break;
            case EXITO:
                colorNormal = new Color(39, 174, 96);     // #27AE60
                colorHover = new Color(46, 204, 113);     // #2ECC71
                colorPress = new Color(30, 130, 76);
                setForeground(Color.WHITE);
                break;
            case PELIGRO:
                colorNormal = new Color(231, 76, 60);     // #E74C3C
                colorHover = new Color(242, 120, 107);
                colorPress = new Color(192, 57, 43);
                setForeground(Color.WHITE);
                break;
            case ADVERTENCIA:
                colorNormal = new Color(243, 156, 18);    // #F39C12
                colorHover = new Color(255, 180, 50);
                colorPress = new Color(224, 130, 10);
                setForeground(Color.WHITE);
                break;
        }

        setBackground(colorNormal);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(new Font("Arial", Font.BOLD, 12));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(120, 40));
        setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    }

    private void configurarEfectos() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(colorHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(colorNormal);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(colorPress);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(colorHover);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Sin borde
    }
}
