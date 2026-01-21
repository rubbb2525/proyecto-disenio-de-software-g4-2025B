package view.componentes;

import javax.swing.*;
import java.awt.*;

/**
 * Toast mejorado con animaciones de entrada/salida, sombra y esquinas redondeadas.
 */
public class ToastMessage extends JDialog {
    public enum TipoToast { INFO, ERROR, EXITO, ADVERTENCIA }
    
    private float opacity = 0.0f;
    private int targetY;
    private int currentY;

    private ToastMessage(JFrame owner, String mensaje, TipoToast tipo) {
        super(owner, false);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setLayout(new BorderLayout());

        Color bg;
        String icon;
        switch (tipo) {
            case ERROR: 
                bg = new Color(231, 76, 60); 
                icon = "\u274C "; 
                break;
            case EXITO: 
                bg = new Color(46, 204, 113); 
                icon = "\u2714 "; 
                break;
            case ADVERTENCIA:
                bg = new Color(243, 156, 18);
                icon = "\u26A0 ";
                break;
            default: 
                bg = new Color(52, 152, 219);
                icon = "\u2139 ";
        }

        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Sombra
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 4, 16, 16);
                
                // Fondo
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 6, 16, 16);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JLabel label = new JLabel(icon + mensaje);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(label, BorderLayout.CENTER);
        
        add(panel);
        pack();
        
        // Aumentar tamaño mínimo
        if (getWidth() < 200) {
            setSize(Math.max(getWidth(), 200), getHeight());
        }

        // Posicionar centro arriba
        Point parent = owner.getLocationOnScreen();
        int x = parent.x + (owner.getWidth() - getWidth()) / 2;
        targetY = parent.y + 60;
        currentY = parent.y + 20;
        setLocation(x, currentY);
    }

    public static void mostrar(JFrame owner, String mensaje, TipoToast tipo) {
        if (owner == null) return;
        
        SwingUtilities.invokeLater(() -> {
            ToastMessage toast = new ToastMessage(owner, mensaje, tipo);
            toast.animarEntrada();
            
            // Ocultar después de 2.5 segundos
            Timer hideTimer = new Timer(2500, e -> toast.animarSalida());
            hideTimer.setRepeats(false);
            hideTimer.start();
        });
    }
    
    private void animarEntrada() {
        setVisible(true);
        Timer timer = new Timer(15, null);
        timer.addActionListener(e -> {
            opacity += 0.08f;
            currentY += (targetY - currentY) / 3;
            
            if (opacity >= 1.0f) {
                opacity = 1.0f;
                currentY = targetY;
                timer.stop();
            }
            
            setOpacity(opacity);
            setLocation(getX(), currentY);
        });
        timer.start();
    }
    
    private void animarSalida() {
        Timer timer = new Timer(15, null);
        timer.addActionListener(e -> {
            opacity -= 0.08f;
            currentY -= 1;
            
            if (opacity <= 0.0f) {
                timer.stop();
                dispose();
            }
            
            setOpacity(Math.max(0, opacity));
            setLocation(getX(), currentY);
        });
        timer.start();
    }
}
