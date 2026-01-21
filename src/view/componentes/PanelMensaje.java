package view.componentes;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de mensaje mejorado con icono y estilos consistentes
 */
public class PanelMensaje extends JPanel {
    public enum TipoMensaje { INFO, EXITO, ERROR, ADVERTENCIA }
    
    private TipoMensaje tipo;
    private String titulo;
    private String mensaje;
    
    public PanelMensaje(String titulo, String mensaje, TipoMensaje tipo) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.tipo = tipo;
        
        setLayout(new BorderLayout(12, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        setMaximumSize(new Dimension(400, 100));
        
        // Definir colores según tipo
        Color colorFondo, colorBorde;
        String icono;
        switch (tipo) {
            case EXITO:
                colorFondo = new Color(209, 250, 229);
                colorBorde = new Color(16, 185, 129);
                icono = "✓";
                break;
            case ERROR:
                colorFondo = new Color(254, 226, 226);
                colorBorde = new Color(239, 68, 68);
                icono = "✕";
                break;
            case ADVERTENCIA:
                colorFondo = new Color(254, 243, 199);
                colorBorde = new Color(217, 119, 6);
                icono = "!";
                break;
            default:
                colorFondo = new Color(219, 234, 254);
                colorBorde = new Color(59, 130, 246);
                icono = "ⓘ";
        }
        
        setBackground(colorFondo);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorBorde, 2),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        
        // Icono
        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Arial", Font.BOLD, 20));
        lblIcono.setForeground(colorBorde);
        lblIcono.setPreferredSize(new Dimension(32, 32));
        add(lblIcono, BorderLayout.WEST);
        
        // Texto
        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setOpaque(false);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 13));
        lblTitulo.setForeground(new Color(44, 62, 80));
        lblTitulo.setAlignmentX(LEFT_ALIGNMENT);
        
        JTextArea txtMensaje = new JTextArea(mensaje);
        txtMensaje.setFont(new Font("Arial", Font.PLAIN, 11));
        txtMensaje.setForeground(new Color(75, 85, 99));
        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);
        txtMensaje.setOpaque(false);
        txtMensaje.setEditable(false);
        txtMensaje.setAlignmentX(LEFT_ALIGNMENT);
        txtMensaje.setMaximumSize(new Dimension(350, 100));
        
        panelTexto.add(lblTitulo);
        panelTexto.add(Box.createVerticalStrut(4));
        panelTexto.add(txtMensaje);
        
        add(panelTexto, BorderLayout.CENTER);
    }
}
