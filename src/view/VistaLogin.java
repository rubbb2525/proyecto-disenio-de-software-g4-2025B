package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.function.Consumer;
import controller.ControladorAutenticacion;
import model.ResultadoOperacion;
import view.componentes.*;

/**
 * VistaLogin - Pantalla de autenticación
 * Requisitos visuales: Clase ConstantesVisuales
 * Diseño: Formulario centralizado con card de 600x500px
 */
public class VistaLogin extends JFrame {
    private PlaceholderTextField txtCorreo;
    private JPasswordField txtPassword;
    private StyledButton btnIngresar;
    private JLabel lblMensajeError;
    private JButton btnMostrarPassword;
    private ControladorAutenticacion controlador;
    private Consumer<String> onLoginSuccess;
    private boolean passwordVisible = false;

    public VistaLogin() {
        setTitle("Sistema Gestión Ayudantes - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(ConstantesVisuales.VENTANA_NORMAL);
        setLocationRelativeTo(null);
        setResizable(false);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Panel principal con fondo
        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                     RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(ConstantesVisuales.COLOR_FONDO_PRINCIPAL);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelPrincipal.setLayout(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(
            ConstantesVisuales.PADDING_XXL, 
            ConstantesVisuales.PADDING_XXL, 
            ConstantesVisuales.PADDING_XXL, 
            ConstantesVisuales.PADDING_XXL
        ));

        // Card de login
        JPanel cardLogin = crearCardLogin();
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        panelPrincipal.add(cardLogin, gbc);

        setContentPane(panelPrincipal);
    }

    private JPanel crearCardLogin() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(ConstantesVisuales.COLOR_TARJETA);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(ConstantesVisuales.RADIO_BORDE_XL, 
                            ConstantesVisuales.COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(
                ConstantesVisuales.PADDING_XL,
                ConstantesVisuales.PADDING_XL,
                ConstantesVisuales.PADDING_XL,
                ConstantesVisuales.PADDING_XL
            )
        ));
        card.setMaximumSize(new Dimension(500, 600));

        // Header
        JPanel header = crearHeader();
        card.add(header);
        card.add(Box.createVerticalStrut(ConstantesVisuales.MARGIN_ENTRE_SECCIONES));

        // Campos del formulario
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
        panelCampos.setBackground(ConstantesVisuales.COLOR_TARJETA);
        panelCampos.setOpaque(false);

        // Campo correo
        txtCorreo = crearTextField("usuario@fis.epn.edu.ec");
        panelCampos.add(crearCampoFormulario("Correo Institucional", txtCorreo, true, 
            "Usa tu correo de la EPN"));
        
        panelCampos.add(Box.createVerticalStrut(ConstantesVisuales.MARGIN_ENTRE_CAMPOS));

        // Campo contraseña
        JPanel panelPassword = crearCampoPassword();
        panelCampos.add(panelPassword);

        card.add(panelCampos);
        card.add(Box.createVerticalStrut(ConstantesVisuales.MARGIN_ENTRE_SECCIONES));

        // Mensaje de error
        lblMensajeError = new JLabel();
        lblMensajeError.setFont(ConstantesVisuales.FUENTE_NORMAL_PEQUEÑO);
        lblMensajeError.setForeground(ConstantesVisuales.COLOR_ERROR);
        lblMensajeError.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblMensajeError);
        card.add(Box.createVerticalStrut(8));

        // Botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
        panelBotones.setBackground(ConstantesVisuales.COLOR_TARJETA);
        panelBotones.setOpaque(false);

        btnIngresar = new StyledButton("Ingresar", StyledButton.TipoBoton.PRIMARIO);
        btnIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 
            ConstantesVisuales.ALTURA_BOTON_NORMAL));
        
        panelBotones.add(btnIngresar);
        card.add(panelBotones);

        // Agregar glue para centrar
        card.add(Box.createVerticalGlue());

        return card;
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(ConstantesVisuales.COLOR_TARJETA);
        header.setOpaque(false);

        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(ConstantesVisuales.FUENTE_TITULO);
        lblTitulo.setForeground(ConstantesVisuales.COLOR_PRIMARIO);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Sistema de Gestión de Ayudantes EPN");
        lblSubtitulo.setFont(ConstantesVisuales.FUENTE_NORMAL_PEQUEÑO);
        lblSubtitulo.setForeground(ConstantesVisuales.COLOR_TEXTO_SECUNDARIO);
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(lblTitulo);
        header.add(Box.createVerticalStrut(4));
        header.add(lblSubtitulo);

        return header;
    }

    private JPanel crearCampoFormulario(String label, JTextField campo, 
                                        boolean obligatorio, String ayuda) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 
            ConstantesVisuales.MARGIN_ENTRE_CAMPOS, 0));

        // Label
        JLabel lblCampo = new JLabel(label + (obligatorio ? " *" : ""));
        lblCampo.setFont(ConstantesVisuales.FUENTE_NEGRITA);
        lblCampo.setForeground(ConstantesVisuales.COLOR_TEXTO_PRINCIPAL);
        lblCampo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblCampo);
        panel.add(Box.createVerticalStrut(6));

        // Campo
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 
            ConstantesVisuales.ALTURA_CAMPO_TEXTO));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(campo);

        // Ayuda
        if (ayuda != null && !ayuda.isEmpty()) {
            panel.add(Box.createVerticalStrut(4));
            JLabel lblAyuda = new JLabel(ayuda);
            lblAyuda.setFont(ConstantesVisuales.FUENTE_MUY_PEQUEÑO);
            lblAyuda.setForeground(ConstantesVisuales.COLOR_TEXTO_SECUNDARIO);
            lblAyuda.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(lblAyuda);
        }

        return panel;
    }

    private PlaceholderTextField crearTextField(String placeholder) {
        PlaceholderTextField txt = new PlaceholderTextField(placeholder);
        txt.setFont(ConstantesVisuales.FUENTE_NORMAL);
        txt.setForeground(ConstantesVisuales.COLOR_TEXTO_PRINCIPAL);
        txt.setBackground(Color.WHITE);
        txt.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(ConstantesVisuales.RADIO_BORDE_NORMAL, 
                            ConstantesVisuales.COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        // Efecto focus
        txt.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                txt.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(ConstantesVisuales.RADIO_BORDE_NORMAL, 
                                    ConstantesVisuales.COLOR_PRIMARIO, 2),
                    BorderFactory.createEmptyBorder(5, 9, 5, 9)
                ));
            }
            public void focusLost(FocusEvent e) {
                txt.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(ConstantesVisuales.RADIO_BORDE_NORMAL, 
                                    ConstantesVisuales.COLOR_BORDE, 1),
                    BorderFactory.createEmptyBorder(6, 10, 6, 10)
                ));
            }
        });

        return txt;
    }

    private JPanel crearCampoPassword() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // Label
        JLabel lblPassword = new JLabel("Contraseña *");
        lblPassword.setFont(ConstantesVisuales.FUENTE_NEGRITA);
        lblPassword.setForeground(ConstantesVisuales.COLOR_TEXTO_PRINCIPAL);
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblPassword);
        panel.add(Box.createVerticalStrut(6));

        // Panel con campo y botón
        JPanel panelContenedor = new JPanel(new BorderLayout());
        panelContenedor.setBackground(ConstantesVisuales.COLOR_TARJETA);
        panelContenedor.setOpaque(false);

        txtPassword = new JPasswordField();
        txtPassword.setFont(ConstantesVisuales.FUENTE_NORMAL);
        txtPassword.setForeground(ConstantesVisuales.COLOR_TEXTO_PRINCIPAL);
        txtPassword.setBackground(Color.WHITE);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(ConstantesVisuales.RADIO_BORDE_NORMAL, 
                            ConstantesVisuales.COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        // Efecto focus
        txtPassword.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                txtPassword.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(ConstantesVisuales.RADIO_BORDE_NORMAL, 
                                    ConstantesVisuales.COLOR_PRIMARIO, 2),
                    BorderFactory.createEmptyBorder(5, 9, 5, 9)
                ));
            }
            public void focusLost(FocusEvent e) {
                txtPassword.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(ConstantesVisuales.RADIO_BORDE_NORMAL, 
                                    ConstantesVisuales.COLOR_BORDE, 1),
                    BorderFactory.createEmptyBorder(6, 10, 6, 10)
                ));
            }
        });

        btnMostrarPassword = new JButton("👁");
        btnMostrarPassword.setFocusPainted(false);
        btnMostrarPassword.setBorderPainted(false);
        btnMostrarPassword.setContentAreaFilled(false);
        btnMostrarPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnMostrarPassword.setFont(ConstantesVisuales.FUENTE_NORMAL);
        btnMostrarPassword.setForeground(ConstantesVisuales.COLOR_PRIMARIO);
        btnMostrarPassword.setPreferredSize(new Dimension(40, 35));
        btnMostrarPassword.addActionListener(e -> togglePasswordVisibility());

        panelContenedor.add(txtPassword, BorderLayout.CENTER);
        panelContenedor.add(btnMostrarPassword, BorderLayout.EAST);
        panelContenedor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 
            ConstantesVisuales.ALTURA_CAMPO_TEXTO));

        panel.add(panelContenedor);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        return panel;
    }

    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;
        txtPassword.setEchoChar(passwordVisible ? '\u0000' : '\u2022');
        btnMostrarPassword.setText(passwordVisible ? "🙈" : "👁");
    }

    public void mostrarVentana() {
        setVisible(true);
    }

    public void cerrar() {
        setVisible(false);
    }

    public String obtenerCorreo() {
        return txtCorreo.getText().trim();
    }

    public String obtenerPassword() {
        return new String(txtPassword.getPassword());
    }

    public void mostrarError(String mensaje) {
        lblMensajeError.setText("⚠️ " + mensaje);
    }

    public void limpiarCampos() {
        txtCorreo.setText("");
        txtPassword.setText("");
        lblMensajeError.setText("");
    }

    public void setControlador(ControladorAutenticacion controlador) {
        this.controlador = controlador;
        configurarAcciones();
    }

    public void setOnLoginSuccess(Consumer<String> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }

    private void configurarAcciones() {
        // Acción del botón ingresar
        btnIngresar.addActionListener(e -> intentarLogin());
        
        // Enter en campos de texto
        txtCorreo.addActionListener(e -> intentarLogin());
        txtPassword.addActionListener(e -> intentarLogin());
    }
    
    private void intentarLogin() {
        String correo = obtenerCorreo();
        String password = obtenerPassword();
        
        // Validación básica
        if (correo.isEmpty()) {
            mostrarError("Por favor, ingrese su correo institucional");
            txtCorreo.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            mostrarError("Por favor, ingrese su contraseña");
            txtPassword.requestFocus();
            return;
        }
        
        // Deshabilitar botón mientras se procesa
        btnIngresar.setEnabled(false);
        btnIngresar.setText("Ingresando...");
        
        // Simular proceso en background
        Timer timer = new Timer(300, evt -> {
            ResultadoOperacion resultado = controlador.autenticar(correo, password);
            
            btnIngresar.setEnabled(true);
            btnIngresar.setText("Ingresar");
            
            if (resultado.esExitoso()) {
                ToastMessage.mostrar(this, "¡Bienvenido al sistema!", ToastMessage.TipoToast.EXITO);
                
                // Pequeño delay para ver el mensaje
                Timer delay = new Timer(500, e2 -> {
                    limpiarCampos();
                    cerrar();
                    if (onLoginSuccess != null) {
                        onLoginSuccess.accept(controlador.obtenerTipoUsuario());
                    }
                });
                delay.setRepeats(false);
                delay.start();
            } else {
                mostrarError(resultado.getMensaje());
                ToastMessage.mostrar(this, "Credenciales incorrectas", ToastMessage.TipoToast.ERROR);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}
