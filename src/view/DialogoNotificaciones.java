package view;

import model.Notificacion;
import view.componentes.StyledButton;
import view.componentes.RoundedBorder;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Diálogo estilizado para mostrar notificaciones.
 */
public class DialogoNotificaciones extends JDialog {
    private static final Color COLOR_FONDO = new Color(245, 247, 250);
    private static final Color COLOR_TARJETA = Color.WHITE;
    private static final Color COLOR_PRIMARIO = new Color(0, 61, 165);
    private static final Color COLOR_TEXTO = new Color(44, 62, 80);
    private static final Color COLOR_BORDE = new Color(225, 232, 237);
    private static final Color COLOR_LEIDA = new Color(149, 165, 166);
    private static final Color COLOR_NO_LEIDA = new Color(52, 152, 219);

    private JPanel panelNotificaciones;
    private StyledButton btnMarcarLeidas;
    private StyledButton btnCerrar;

    public DialogoNotificaciones(Frame owner, List<Notificacion> notificaciones) {
        super(owner, "Notificaciones", true);
        initUI(notificaciones);
    }

    private void initUI(List<Notificacion> notificaciones) {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setBackground(COLOR_PRIMARIO);
        header.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        JLabel lblTitulo = new JLabel("Notificaciones");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        header.add(lblTitulo);

        JPanel content = crearPanelNotificaciones(notificaciones);
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        pie.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
        btnMarcarLeidas = new StyledButton("Marcar todas como leídas", StyledButton.TipoBoton.SECUNDARIO);
        btnCerrar = new StyledButton("Cerrar", StyledButton.TipoBoton.PRIMARIO);
        pie.add(btnMarcarLeidas);
        pie.add(btnCerrar);
        btnCerrar.addActionListener(e -> dispose());

        add(header, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(pie, BorderLayout.SOUTH);
    }

    private JPanel crearPanelNotificaciones(List<Notificacion> notificaciones) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        if (notificaciones.isEmpty()) {
            JLabel empty = new JLabel("No hay notificaciones");
            empty.setForeground(new Color(149, 165, 166));
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(Box.createVerticalGlue());
            panel.add(empty);
            panel.add(Box.createVerticalGlue());
            return panel;
        }

        for (Notificacion not : notificaciones) {
            panel.add(crearTarjetaNotificacion(not));
            panel.add(Box.createVerticalStrut(8));
        }

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel crearTarjetaNotificacion(Notificacion not) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_TARJETA);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);

        JLabel tipo = new JLabel(not.getTipo());
        tipo.setFont(new Font("Arial", Font.BOLD, 11));
        tipo.setForeground(not.isLeida() ? COLOR_LEIDA : COLOR_NO_LEIDA);

        JLabel fecha = new JLabel(not.getFecha() != null ? not.getFecha().toString() : "");
        fecha.setFont(new Font("Arial", Font.PLAIN, 10));
        fecha.setForeground(new Color(149, 165, 166));

        encabezado.add(tipo, BorderLayout.WEST);
        encabezado.add(fecha, BorderLayout.EAST);

        JLabel mensaje = new JLabel("<html>" + not.getMensaje() + "</html>");
        mensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        mensaje.setForeground(COLOR_TEXTO);
        mensaje.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(encabezado);
        card.add(Box.createVerticalStrut(4));
        card.add(mensaje);

        return card;
    }

    public void onMarcarLeidasListener(Runnable action) {
        btnMarcarLeidas.addActionListener(e -> {
            action.run();
            dispose();
        });
    }
}
