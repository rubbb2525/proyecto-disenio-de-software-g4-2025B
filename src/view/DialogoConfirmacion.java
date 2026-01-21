package view;

import view.componentes.StyledButton;
import view.componentes.RoundedBorder;

import javax.swing.*;
import java.awt.*;

/**
 * Diálogo de confirmación estilizado.
 */
public class DialogoConfirmacion extends JDialog {
    private static final Color COLOR_PRIMARIO = new Color(0, 61, 165);
    private static final Color COLOR_TEXTO = new Color(44, 62, 80);
    private static final Color COLOR_BORDE = new Color(225, 232, 237);

    private boolean confirmado = false;

    public DialogoConfirmacion(Frame owner, String titulo, String mensaje) {
        super(owner, titulo, true);
        initUI(mensaje);
    }

    private void initUI(String mensaje) {
        setSize(380, 180);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel header = new JPanel();
        header.setBackground(COLOR_PRIMARIO);
        header.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        JLabel lblTitulo = new JLabel(getTitle());
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 13));
        header.add(lblTitulo);

        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JLabel lblMensaje = new JLabel("<html><body style='text-align: center;'>" + mensaje + "</body></html>");
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMensaje.setForeground(COLOR_TEXTO);
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        content.add(lblMensaje, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        StyledButton btnConfirmar = new StyledButton("Confirmar", StyledButton.TipoBoton.EXITO);
        StyledButton btnCancelar = new StyledButton("Cancelar", StyledButton.TipoBoton.SECUNDARIO);
        botones.add(btnCancelar);
        botones.add(btnConfirmar);

        btnConfirmar.addActionListener(e -> {
            confirmado = true;
            dispose();
        });
        btnCancelar.addActionListener(e -> dispose());

        add(header, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    public boolean esConfirmado() {
        return confirmado;
    }
}
