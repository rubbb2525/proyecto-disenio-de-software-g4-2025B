package view;

import javax.swing.*;
import java.awt.*;
import controller.ControladorAutenticacion;
import model.ResultadoOperacion;
import view.componentes.PlaceholderTextField;
import view.componentes.StyledButton;
import view.componentes.RoundedBorder;
import view.componentes.IconManager;
import view.componentes.ToastMessage;
import java.util.function.Consumer;

/**
 * Ventana de login del sistema
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
        setSize(520, 420);
        setLocationRelativeTo(null);
        setResizable(false);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        Color fondo = new Color(245, 247, 250);
        Color textoSecundario = new Color(149, 165, 166);
        Color textoPrincipal = new Color(44, 62, 80);
        Color primario = new Color(0, 61, 165);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(fondo);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(16, new Color(225, 232, 237), 1),
            BorderFactory.createEmptyBorder(24, 28, 24, 28)
        ));
        card.setMaximumSize(new Dimension(420, 320));

        JLabel lblLogo = new JLabel(IconManager.getInstance().getLogoIcon(64));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(primario);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Sistema de Gestión de Ayudantes");
        lblSub.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSub.setForeground(textoSecundario);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblCorreo = new JLabel("Correo institucional");
        lblCorreo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblCorreo.setForeground(textoPrincipal);
        lblCorreo.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtCorreo = new PlaceholderTextField("usuario@fis.epn.edu.ec");
        txtCorreo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtCorreo.setHorizontalAlignment(JTextField.CENTER);

        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPassword.setForeground(textoPrincipal);
        lblPassword.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel de contraseña con botón toggle
        JPanel panelPassword = new JPanel(new BorderLayout());
        panelPassword.setBackground(Color.WHITE);
        panelPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 232, 237), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtPassword.setHorizontalAlignment(JTextField.CENTER);
        
        btnMostrarPassword = new JButton();
        btnMostrarPassword.setBorder(null);
        btnMostrarPassword.setBackground(Color.BLUE);
        btnMostrarPassword.setFocusPainted(false);
        btnMostrarPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnMostrarPassword.setPreferredSize(new Dimension(40, 40));
        btnMostrarPassword.setToolTipText("Mostrar/Ocultar contraseña");
        // Icono inicial: ojo cerrado
        btnMostrarPassword.setIcon(IconManager.getInstance().getIcon("eyeclosed.svg", 18));
        
        panelPassword.add(txtPassword, BorderLayout.CENTER);
        panelPassword.add(btnMostrarPassword, BorderLayout.EAST);

        lblMensajeError = new JLabel("");
        lblMensajeError.setForeground(Color.RED);
        lblMensajeError.setFont(new Font("Arial", Font.PLAIN, 11));
        lblMensajeError.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnIngresar = new StyledButton("Ingresar", StyledButton.TipoBoton.PRIMARIO);
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIngresar.setMaximumSize(new Dimension(180, 42));

        card.add(lblLogo);
        card.add(Box.createVerticalStrut(8));
        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(4));
        card.add(lblSub);
        card.add(Box.createVerticalStrut(18));
        card.add(lblCorreo);
        card.add(Box.createVerticalStrut(6));
        card.add(txtCorreo);
        card.add(Box.createVerticalStrut(14));
        card.add(lblPassword);
        card.add(Box.createVerticalStrut(6));
        card.add(panelPassword);
        card.add(Box.createVerticalStrut(12));
        card.add(btnIngresar);
        card.add(Box.createVerticalStrut(8));
        card.add(lblMensajeError);

        // Footer con versión
        JPanel footer = new JPanel();
        footer.setBackground(fondo);
        JLabel lblVersion = new JLabel("Versión 1.0 - 2026 | FIS-EPN");
        lblVersion.setFont(new Font("Arial", Font.PLAIN, 10));
        lblVersion.setForeground(textoSecundario);
        footer.add(lblVersion);

        JPanel wrapper = new JPanel();
        wrapper.setBackground(fondo);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.add(Box.createVerticalGlue());
        wrapper.add(card);
        wrapper.add(Box.createVerticalStrut(12));
        wrapper.add(footer);
        wrapper.add(Box.createVerticalGlue());

        panelPrincipal.add(wrapper, BorderLayout.CENTER);
        add(panelPrincipal);
        
        // Configurar evento de toggle password
        btnMostrarPassword.addActionListener(e -> togglePasswordVisibility());
    }

    public void mostrarVentana() {
        setVisible(true);
    }

    public void cerrar() {
        setVisible(false);
    }

    public String obtenerCorreo() {
        return txtCorreo.getTextReal();
    }

    public String obtenerPassword() {
        return new String(txtPassword.getPassword());
    }

    public void mostrarError(String mensaje) {
        lblMensajeError.setText(mensaje);
    }

    public void limpiarCampos() {
        txtCorreo.setText("");
        txtPassword.setText("");
        lblMensajeError.setText("");
    }
    
    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;
        if (passwordVisible) {
            txtPassword.setEchoChar((char) 0);
            btnMostrarPassword.setIcon(IconManager.getInstance().getIcon("eyeopen.svg", 18));
        } else {
            txtPassword.setEchoChar('•');
            btnMostrarPassword.setIcon(IconManager.getInstance().getIcon("eyeclosed.svg", 18));
        }
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
        if (correo.trim().isEmpty()) {
            mostrarError("Por favor, ingrese su correo institucional");
            txtCorreo.requestFocus();
            return;
        }
        
        if (password.trim().isEmpty()) {
            mostrarError("Por favor, ingrese su contraseña");
            txtPassword.requestFocus();
            return;
        }
        
        // Deshabilitar botón mientras se procesa
        btnIngresar.setEnabled(false);
        btnIngresar.setText("Ingresando...");
        
        // Simular proceso en background (en una app real sería un SwingWorker)
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
