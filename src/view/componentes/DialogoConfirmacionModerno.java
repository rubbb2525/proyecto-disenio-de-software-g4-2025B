package view.componentes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Diálogo de confirmación mejorado con estilo moderno
 */
public class DialogoConfirmacionModerno extends JDialog {
    private boolean confirmado = false;
    
    public DialogoConfirmacionModerno(Frame owner, String titulo, String mensaje) {
        super(owner, titulo, true);
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setUndecorated(false);
        
        // Contenido
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Icono
        JLabel lblIcono = new JLabel("⚠");
        lblIcono.setFont(new Font("Arial", Font.BOLD, 32));
        lblIcono.setForeground(new Color(243, 156, 18));
        lblIcono.setAlignmentX(CENTER_ALIGNMENT);
        panelContenido.add(lblIcono);
        panelContenido.add(Box.createVerticalStrut(10));
        
        // Título
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(44, 62, 80));
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        panelContenido.add(lblTitulo);
        panelContenido.add(Box.createVerticalStrut(12));
        
        // Mensaje
        JTextArea txtMensaje = new JTextArea(mensaje);
        txtMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        txtMensaje.setForeground(new Color(75, 85, 99));
        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);
        txtMensaje.setOpaque(false);
        txtMensaje.setEditable(false);
        txtMensaje.setAlignmentX(CENTER_ALIGNMENT);
        txtMensaje.setMaximumSize(new Dimension(300, 80));
        panelContenido.add(txtMensaje);
        panelContenido.add(Box.createVerticalStrut(20));
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotones.setOpaque(false);
        
        JButton btnSi = new StyledButton("Sí", StyledButton.TipoBoton.PRIMARIO);
        btnSi.setPreferredSize(new Dimension(100, 36));
        btnSi.addActionListener(e -> {
            confirmado = true;
            dispose();
        });
        
        JButton btnNo = new StyledButton("No", StyledButton.TipoBoton.SECUNDARIO);
        btnNo.setPreferredSize(new Dimension(100, 36));
        btnNo.addActionListener(e -> {
            confirmado = false;
            dispose();
        });
        
        panelBotones.add(btnSi);
        panelBotones.add(btnNo);
        panelContenido.add(panelBotones);
        
        add(panelContenido);
        setSize(380, 280);
        setLocationRelativeTo(owner);
    }
    
    public boolean esConfirmado() {
        return confirmado;
    }
}
