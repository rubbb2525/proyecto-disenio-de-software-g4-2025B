package view.componentes;

import javax.swing.*;
import java.awt.*;

/**
 * Botón con badge numérico sencillo.
 */
public class BadgeButton extends JButton {
    private int count = 0;
    private Color badgeColor = new Color(231, 76, 60);

    public BadgeButton(String text, Icon icon) {
        super(text, icon);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setHorizontalTextPosition(SwingConstants.RIGHT);
        setHorizontalAlignment(SwingConstants.LEFT);
    }

    public void setCount(int count) {
        this.count = Math.max(count, 0);
        repaint();
    }

    public int getCount() {
        return count;
    }

    public void setBadgeColor(Color badgeColor) {
        this.badgeColor = badgeColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (count > 0) {
            String text = String.valueOf(count);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int diameter = Math.max(18, 12 + text.length() * 6);
            int x = getWidth() - diameter - 6;
            int y = 6;

            g2.setColor(badgeColor);
            g2.fillOval(x, y, diameter, diameter);

            g2.setColor(Color.WHITE);
            g2.setFont(getFont().deriveFont(Font.BOLD, 11f));
            FontMetrics fm = g2.getFontMetrics();
            int tx = x + (diameter - fm.stringWidth(text)) / 2;
            int ty = y + (diameter + fm.getAscent()) / 2 - 2;
            g2.drawString(text, tx, ty);

            g2.dispose();
        }
    }
}
