package vistas.componentes;

import javax.swing.*;
import java.awt.*;

public class ToastMessage {
    public enum TipoToast {
        EXITO,
        ERROR,
        ADVERTENCIA,
        INFO
    }

    public static void mostrar(JFrame parent, String mensaje, TipoToast tipo) {
        SwingUtilities.invokeLater(() -> {
            JWindow toast = new JWindow(parent);
            toast.setAlwaysOnTop(true);

            JPanel panel = new JPanel(new BorderLayout(12, 0));
            panel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

            Color bgColor = switch (tipo) {
                case EXITO -> new Color(46, 204, 113);      // #2ECC71
                case ERROR -> new Color(231, 76, 60);       // #E74C3C
                case ADVERTENCIA -> new Color(243, 156, 18); // #F39C12
                case INFO -> new Color(52, 152, 219);       // #3498DB
            };

            String icono = switch (tipo) {
                case EXITO -> "✓";
                case ERROR -> "✕";
                case ADVERTENCIA -> "⚠";
                case INFO -> "ℹ";
            };

            panel.setBackground(bgColor);

            JLabel lblIcono = new JLabel(icono);
            lblIcono.setFont(new Font("Arial", Font.BOLD, 18));
            lblIcono.setForeground(Color.WHITE);

            JLabel lblMensaje = new JLabel(mensaje);
            lblMensaje.setForeground(Color.WHITE);
            lblMensaje.setFont(new Font("Arial", Font.PLAIN, 12));

            panel.add(lblIcono, BorderLayout.WEST);
            panel.add(lblMensaje, BorderLayout.CENTER);

            toast.add(panel);
            toast.pack();

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (screenSize.width - toast.getWidth()) / 2;
            int y = screenSize.height - toast.getHeight() - 50;
            toast.setLocation(x, y);

            toast.setVisible(true);

            Timer timer = new Timer(3000, e -> toast.dispose());
            timer.setRepeats(false);
            timer.start();
        });
    }
}
