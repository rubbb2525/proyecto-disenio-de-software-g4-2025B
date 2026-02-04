package view;

import controller.ControladorDirector;
import view.componentes.StyledButton;
import view.componentes.RoundedBorder;

import javax.swing.*;
import java.awt.*;

/**
 * Diálogo emergente que se muestra cuando un estudiante no es encontrado.
 * Ofrece la opción de registrar un nuevo estudiante.
 */
public class DialogoEstudianteNoRegistrado extends JDialog {
    private static final Color COLOR_FONDO = new Color(245, 247, 250);
    private static final Color COLOR_TARJETA = Color.WHITE;
    private static final Color COLOR_TEXTO = new Color(44, 62, 80);
    private static final Color COLOR_BORDE = new Color(225, 232, 237);
    
    private final ControladorDirector controlador;
    private final String tipoContratacion; // "asistente" o "ayudante"
    
    public DialogoEstudianteNoRegistrado(Frame owner, ControladorDirector controlador, String tipoContratacion) {
        super(owner, "Estudiante no Encontrado", true);
        this.controlador = controlador;
        this.tipoContratacion = tipoContratacion;
        initUI();
    }
    
    private void initUI() {
        setSize(450, 350);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        setResizable(false);
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(COLOR_FONDO);
        content.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Icono
        JLabel lblIcono = new JLabel("👤");
        lblIcono.setFont(new Font("Arial", Font.PLAIN, 60));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(lblIcono);
        
        content.add(Box.createVerticalStrut(20));
        
        // Título
        JLabel lblTitulo = new JLabel("Estudiante no Encontrado");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(lblTitulo);
        
        content.add(Box.createVerticalStrut(15));
        
        // Descripción
        JLabel lblDescripcion = new JLabel(
            "<html><center>El estudiante que busca no está registrado en el sistema.<br>" +
            "<br>¿Desea registrar un nuevo estudiante?</center></html>"
        );
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDescripcion.setForeground(new Color(90, 99, 110));
        lblDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(lblDescripcion);
        
        content.add(Box.createVerticalStrut(30));
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setOpaque(false);
        
        StyledButton btnRegistrar = new StyledButton("Registrar Estudiante", StyledButton.TipoBoton.PRIMARIO);
        btnRegistrar.addActionListener(e -> abrirFormularioRegistro());
        
        StyledButton btnCancelar = new StyledButton("Cancelar", StyledButton.TipoBoton.SECUNDARIO);
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);
        
        content.add(panelBotones);
        content.add(Box.createVerticalGlue());
        
        add(content, BorderLayout.CENTER);
    }
    
    private void abrirFormularioRegistro() {
        dispose(); // Cerrar esta ventana
        
        // Abrir la ventana de registro de estudiante
        DialogoFormularioEstudiante dialogo = new DialogoFormularioEstudiante(
            (Frame) SwingUtilities.getWindowAncestor(this)
        );
        dialogo.setVisible(true);
    }
}