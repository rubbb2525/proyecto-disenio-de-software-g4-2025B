package vistas.componentes;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class BadgeButton extends JButton {
    private int count = 0;
    private static final int BADGE_SIZE = 20;

    public BadgeButton(String text) {
        super(text);
        configurarEstilo();
    }

    public BadgeButton(String text, Icon icon) {
        super(text, icon);
        configurarEstilo();
    }

    private void configurarEstilo() {
        setFont(new Font("Arial", Font.BOLD, 11));
        setForeground(Color.WHITE);
        setBackground(new Color(0, 61, 165));
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(120, 35));
    }

    public void setCount(int count) {
        this.count = count;
        repaint();
    }

    public int getCount() {
        return count;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (count > 0) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // Dibujar círculo rojo
            int badgeX = getWidth() - BADGE_SIZE - 5;
            int badgeY = 5;

            g2d.setColor(new Color(231, 76, 60));
            g2d.fillOval(badgeX, badgeY, BADGE_SIZE, BADGE_SIZE);

            // Dibujar borde blanco
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(badgeX, badgeY, BADGE_SIZE, BADGE_SIZE);

            // Dibujar número
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            String text = count > 9 ? "9+" : String.valueOf(count);
            FontMetrics fm = g2d.getFontMetrics();
            int x = badgeX + (BADGE_SIZE - fm.stringWidth(text)) / 2;
            int y = badgeY + ((BADGE_SIZE - fm.getHeight()) / 2) + fm.getAscent();
            g2d.drawString(text, x, y);
        }
    }
}
