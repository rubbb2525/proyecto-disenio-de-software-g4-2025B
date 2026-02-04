package view;

import controller.ControladorDirector;
import view.componentes.StyledButton;
import view.componentes.RoundedBorder;

import javax.swing.*;
import java.awt.*;

/**
 * Diálogo para seleccionar el tipo de contratación antes de mostrar el formulario correspondiente
 */
public class DialogoSeleccionContratacion extends JDialog {
    private static final Color COLOR_FONDO = new Color(245, 247, 250);
    private static final Color COLOR_TARJETA = Color.WHITE;
    private static final Color COLOR_PRIMARIO = new Color(0, 61, 165);
    private static final Color COLOR_TEXTO = new Color(44, 62, 80);
    private static final Color COLOR_BORDE = new Color(225, 232, 237);
    
    private final ControladorDirector controlador;
    private boolean guardado = false;
    
    public DialogoSeleccionContratacion(Frame owner, ControladorDirector controlador) {
        super(owner, "Realizar Contratación", true);
        this.controlador = controlador;
        initUI();
    }
    
    private void initUI() {
        setSize(500, 300);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(COLOR_FONDO);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel lblTitulo = new JLabel("Seleccione el tipo de contratación");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubtitulo = new JLabel("Elija según el perfil del colaborador a contratar");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(90, 99, 110));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        content.add(lblTitulo);
        content.add(Box.createVerticalStrut(5));
        content.add(lblSubtitulo);
        content.add(Box.createVerticalStrut(30));
        
        // Botones de selección
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 15, 0));
        panelBotones.setOpaque(false);
        panelBotones.setMaximumSize(new Dimension(450, 100));
        
        // Botón Contratación Profesional
        JPanel cardProfesional = crearTarjetaOpcion(
            "Contratación Profesional",
            "Asistentes de Investigación",
            "👨‍🎓",
            new Color(52, 152, 219),
            () -> abrirFormularioProfesional()
        );
        
        // Botón Contratación Técnica
        JPanel cardTecnica = crearTarjetaOpcion(
            "Contratación Técnica",
            "Ayudantes o Técnicos de Investigación",
            "🔧",
            new Color(46, 204, 113),
            () -> abrirSeleccionTecnica()
        );
        
        panelBotones.add(cardProfesional);
        panelBotones.add(cardTecnica);
        
        content.add(panelBotones);
        content.add(Box.createVerticalStrut(30));
        
        // Botón Cancelar
        JPanel panelCancelar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelCancelar.setOpaque(false);
        StyledButton btnCancelar = new StyledButton("Cancelar", StyledButton.TipoBoton.SECUNDARIO);
        btnCancelar.addActionListener(e -> dispose());
        panelCancelar.add(btnCancelar);
        
        content.add(panelCancelar);
        
        add(content, BorderLayout.CENTER);
    }
    
    private JPanel crearTarjetaOpcion(String titulo, String descripcion, String icono, Color colorAccent, Runnable accion) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_TARJETA);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, COLOR_BORDE, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Icono
        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Arial", Font.PLAIN, 40));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Título
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 13));
        lblTitulo.setForeground(colorAccent);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Descripción
        JLabel lblDesc = new JLabel("<html><center>" + descripcion + "</center></html>");
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 11));
        lblDesc.setForeground(new Color(90, 99, 110));
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(lblIcono);
        card.add(Box.createVerticalStrut(10));
        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(5));
        card.add(lblDesc);
        
        // Efecto hover
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(12, colorAccent, 2),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(12, COLOR_BORDE, 2),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                accion.run();
            }
        });
        
        return card;
    }
    
    private void abrirFormularioProfesional() {
        dispose();
        DialogoFormularioAsistente dialogo = new DialogoFormularioAsistente(
            (Frame) getParent(), controlador
        );
        dialogo.setVisible(true);
        guardado = dialogo.seGuardo();
    }
    
    private void abrirSeleccionTecnica() {
        dispose();
        DialogoSeleccionTecnica dialogo = new DialogoSeleccionTecnica(
            (Frame) getParent(), controlador
        );
        dialogo.setVisible(true);
        guardado = dialogo.seGuardo();
    }
    
    public boolean seGuardo() {
        return guardado;
    }
}
