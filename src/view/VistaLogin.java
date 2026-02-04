package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import java.util.function.Consumer;
import controller.ControladorAutenticacion;
import model.ResultadoOperacion;
import view.componentes.*;

/**
 * VistaLogin - Pantalla de autenticación - VERSIÓN MEJORADA
 * Sin emojis, diseño profesional y moderno
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
    
    // Panel de carga
    private JPanel panelCargando;

    public VistaLogin() {
        setTitle("Sistema de Gestión de Ayudantes - EPN");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false); // Mantener decoración nativa

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Panel principal con diseño dual
        JPanel panelPrincipal = new JPanel(new GridLayout(1, 2, 0, 0));
        panelPrincipal.setBackground(ConstantesVisuales.COLOR_FONDO_PRINCIPAL);

        // Panel izquierdo: Branding
        JPanel panelBranding = crearPanelBranding();
        
        // Panel derecho: Formulario
        JPanel panelFormulario = crearPanelFormulario();

        panelPrincipal.add(panelBranding);
        panelPrincipal.add(panelFormulario);

        setContentPane(panelPrincipal);
    }

    private JPanel crearPanelBranding() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                     RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Degradado azul
                GradientPaint gradient = new GradientPaint(
                    0, 0, ConstantesVisuales.COLOR_PRIMARIO,
                    0, getHeight(), ConstantesVisuales.COLOR_PRIMARIO_OSCURO
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Patrón de puntos decorativos
                g2d.setColor(new Color(255, 255, 255, 20));
                for (int i = 0; i < 50; i++) {
                    int x = (int)(Math.random() * getWidth());
                    int y = (int)(Math.random() * getHeight());
                    int size = (int)(Math.random() * 3) + 1;
                    g2d.fillOval(x, y, size, size);
                }
            }
        };
        panel.setLayout(new GridBagLayout());
        
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);
        contenido.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        // Logo/Iniciales EPN
        JPanel logoPanel = crearLogoEPN();
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Título principal
        JLabel lblTitulo = new JLabel("Sistema de Gestión");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("de Ayudantes");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        lblSubtitulo.setForeground(new Color(255, 255, 255, 230));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Descripción
        JLabel lblDescripcion = new JLabel("<html><center>Plataforma integral para la administración<br>" +
            "y seguimiento de ayudantes de investigación</center></html>");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDescripcion.setForeground(new Color(255, 255, 255, 180));
        lblDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDescripcion.setHorizontalAlignment(SwingConstants.CENTER);

        // Características
        JPanel panelCaracteristicas = crearPanelCaracteristicas();
        panelCaracteristicas.setAlignmentX(Component.CENTER_ALIGNMENT);

        contenido.add(logoPanel);
        contenido.add(Box.createVerticalStrut(30));
        contenido.add(lblTitulo);
        contenido.add(Box.createVerticalStrut(5));
        contenido.add(lblSubtitulo);
        contenido.add(Box.createVerticalStrut(20));
        contenido.add(lblDescripcion);
        contenido.add(Box.createVerticalStrut(40));
        contenido.add(panelCaracteristicas);

        panel.add(contenido);
        return panel;
    }

    private JPanel crearLogoEPN() {
        JLabel logo = new JLabel();
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setVerticalAlignment(SwingConstants.CENTER);
        logo.setOpaque(false);

        ImageIcon icono = cargarLogoEPN();
        if (icono != null) {
            logo.setIcon(icono);
        } else {
            logo.setText("EPN");
            logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
            logo.setForeground(Color.WHITE);
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(110, 110));
        panel.setMaximumSize(new Dimension(110, 110));
        panel.add(logo, BorderLayout.CENTER);
        return panel;
    }

    private ImageIcon cargarLogoEPN() {
        String resourcePath = "/view/componentes/EPN_logo_big.png";
        URL recurso = getClass().getResource(resourcePath);
        try {
            BufferedImage imagen;
            if (recurso != null) {
                imagen = ImageIO.read(recurso);
            } else {
                File archivoVista = new File("src/view/componentes/EPN_logo_big.png");
                File archivoModel = new File("src/model/components/EPN_logo_big.png");
                if (archivoVista.exists()) {
                    imagen = ImageIO.read(archivoVista);
                } else if (archivoModel.exists()) {
                    imagen = ImageIO.read(archivoModel);
                } else {
                    return null;
                }
            }

            int maxWidth = 120;
            int maxHeight = 100;
            int width = imagen.getWidth();
            int height = imagen.getHeight();

            if (width <= 0 || height <= 0) {
                return null;
            }

            double scale = (double) maxHeight / height;
            int scaledWidth = (int) Math.round(width * scale);
            int scaledHeight = (int) Math.round(height * scale);

            if (scaledWidth > maxWidth) {
                scale = (double) maxWidth / width;
                scaledWidth = (int) Math.round(width * scale);
                scaledHeight = (int) Math.round(height * scale);
            }

            Image escalada = imagen.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            return new ImageIcon(escalada);
        } catch (IOException e) {
            return null;
        }
    }

    private JPanel crearPanelCaracteristicas() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        String[] caracteristicas = {
            "Gestión de proyectos de investigación",
            "Control de ayudantes y asistentes",
            "Reportes estadísticos en tiempo real"
        };

        for (String caracteristica : caracteristicas) {
            JPanel item = crearItemCaracteristica(caracteristica);
            panel.add(item);
            panel.add(Box.createVerticalStrut(12));
        }

        return panel;
    }

    private JPanel crearItemCaracteristica(String texto) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(400, 30));

        // Indicador visual (check)
        JLabel check = new JLabel("✓");
        check.setFont(new Font("Segoe UI", Font.BOLD, 16));
        check.setForeground(new Color(46, 204, 113));
        check.setPreferredSize(new Dimension(25, 25));
        check.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(255, 255, 255, 200));

        panel.add(check);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(label);

        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        // Card del formulario
        JPanel cardFormulario = new JPanel();
        cardFormulario.setLayout(new BoxLayout(cardFormulario, BoxLayout.Y_AXIS));
        cardFormulario.setBackground(Color.WHITE);
        cardFormulario.setBorder(BorderFactory.createEmptyBorder(60, 50, 60, 50));
        cardFormulario.setMaximumSize(new Dimension(400, 600));

        // Header del formulario
        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(ConstantesVisuales.COLOR_TEXTO_PRINCIPAL);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Ingrese sus credenciales institucionales");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(ConstantesVisuales.COLOR_TEXTO_SECUNDARIO);
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        cardFormulario.add(lblTitulo);
        cardFormulario.add(Box.createVerticalStrut(8));
        cardFormulario.add(lblSubtitulo);
        cardFormulario.add(Box.createVerticalStrut(40));

        // Campo correo
        JPanel campoCorreo = crearCampoCorreo();
        campoCorreo.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardFormulario.add(campoCorreo);
        cardFormulario.add(Box.createVerticalStrut(24));

        // Campo contraseña
        JPanel campoPassword = crearCampoPassword();
        campoPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardFormulario.add(campoPassword);
        cardFormulario.add(Box.createVerticalStrut(8));

        // Mensaje de error
        lblMensajeError = new JLabel();
        lblMensajeError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMensajeError.setForeground(ConstantesVisuales.COLOR_ERROR);
        lblMensajeError.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblMensajeError.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cardFormulario.add(lblMensajeError);
        cardFormulario.add(Box.createVerticalStrut(24));

        // Botón ingresar
        btnIngresar = new StyledButton("Ingresar al Sistema", StyledButton.TipoBoton.PRIMARIO);
        btnIngresar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cardFormulario.add(btnIngresar);
        cardFormulario.add(Box.createVerticalStrut(16));

        // Información adicional
        JLabel lblInfo = new JLabel("<html><center>¿Problemas para ingresar?<br>" +
            "Contacte al administrador del sistema</center></html>");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblInfo.setForeground(ConstantesVisuales.COLOR_TEXTO_SECUNDARIO);
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        cardFormulario.add(Box.createVerticalStrut(20));
        cardFormulario.add(lblInfo);

        // Footer
        JPanel footer = crearFooter();
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardFormulario.add(Box.createVerticalGlue());
        cardFormulario.add(footer);

        panel.add(cardFormulario);
        return panel;
    }

    private JPanel crearCampoCorreo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lblLabel = new JLabel("Correo Institucional");
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLabel.setForeground(ConstantesVisuales.COLOR_TEXTO_PRINCIPAL);
        lblLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtCorreo = new PlaceholderTextField("usuario@epn.edu.ec");
        txtCorreo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCorreo.setForeground(ConstantesVisuales.COLOR_TEXTO_PRINCIPAL);
        txtCorreo.setBackground(new Color(248, 250, 252));
        txtCorreo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ConstantesVisuales.COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        txtCorreo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtCorreo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Efecto focus
        txtCorreo.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                txtCorreo.setBackground(Color.WHITE);
                txtCorreo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ConstantesVisuales.COLOR_PRIMARIO, 2),
                    BorderFactory.createEmptyBorder(9, 13, 9, 13)
                ));
            }
            public void focusLost(FocusEvent e) {
                txtCorreo.setBackground(new Color(248, 250, 252));
                txtCorreo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ConstantesVisuales.COLOR_BORDE, 1),
                    BorderFactory.createEmptyBorder(10, 14, 10, 14)
                ));
            }
        });

        panel.add(lblLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(txtCorreo);

        return panel;
    }

    private JPanel crearCampoPassword() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lblLabel = new JLabel("Contraseña");
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLabel.setForeground(ConstantesVisuales.COLOR_TEXTO_PRINCIPAL);
        lblLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Contenedor del campo y botón
        JPanel contenedorCampo = new JPanel(new BorderLayout(0, 0));
        contenedorCampo.setBackground(new Color(248, 250, 252));
        contenedorCampo.setBorder(BorderFactory.createLineBorder(ConstantesVisuales.COLOR_BORDE, 1));
        contenedorCampo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        contenedorCampo.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setForeground(ConstantesVisuales.COLOR_TEXTO_PRINCIPAL);
        txtPassword.setBackground(new Color(248, 250, 252));
        txtPassword.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        // Botón mostrar/ocultar
        btnMostrarPassword = new JButton("Mostrar");
        btnMostrarPassword.setFocusPainted(false);
        btnMostrarPassword.setBorderPainted(false);
        btnMostrarPassword.setContentAreaFilled(false);
        btnMostrarPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnMostrarPassword.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnMostrarPassword.setForeground(ConstantesVisuales.COLOR_PRIMARIO);
        btnMostrarPassword.setPreferredSize(new Dimension(70, 45));
        btnMostrarPassword.addActionListener(e -> togglePasswordVisibility());

        contenedorCampo.add(txtPassword, BorderLayout.CENTER);
        contenedorCampo.add(btnMostrarPassword, BorderLayout.EAST);

        // Efecto focus
        txtPassword.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                contenedorCampo.setBackground(Color.WHITE);
                txtPassword.setBackground(Color.WHITE);
                contenedorCampo.setBorder(BorderFactory.createLineBorder(
                    ConstantesVisuales.COLOR_PRIMARIO, 2));
            }
            public void focusLost(FocusEvent e) {
                contenedorCampo.setBackground(new Color(248, 250, 252));
                txtPassword.setBackground(new Color(248, 250, 252));
                contenedorCampo.setBorder(BorderFactory.createLineBorder(
                    ConstantesVisuales.COLOR_BORDE, 1));
            }
        });

        panel.add(lblLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(contenedorCampo);

        return panel;
    }

    private JPanel crearFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        footer.setOpaque(false);
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel lblFooter = new JLabel("© 2026 Escuela Politécnica Nacional");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblFooter.setForeground(new Color(150, 150, 150));

        footer.add(lblFooter);
        return footer;
    }

    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;
        if (passwordVisible) {
            txtPassword.setEchoChar('\u0000');
            btnMostrarPassword.setText("Ocultar");
        } else {
            txtPassword.setEchoChar('●');
            btnMostrarPassword.setText("Mostrar");
        }
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
        lblMensajeError.setText("✕  " + mensaje);
        
        // Animación de shake
        Point ubicacionOriginal = getLocation();
        Timer shakeTimer = new Timer(50, null);
        final int[] contador = {0};
        
        shakeTimer.addActionListener(e -> {
            if (contador[0] < 10) {
                int offset = (contador[0] % 2 == 0) ? 5 : -5;
                setLocation(ubicacionOriginal.x + offset, ubicacionOriginal.y);
                contador[0]++;
            } else {
                setLocation(ubicacionOriginal);
                ((Timer)e.getSource()).stop();
            }
        });
        shakeTimer.start();
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
        btnIngresar.addActionListener(e -> intentarLogin());
        txtCorreo.addActionListener(e -> intentarLogin());
        txtPassword.addActionListener(e -> intentarLogin());
        
        // Enter en cualquier campo
        getRootPane().setDefaultButton(btnIngresar);
    }
    
    private void intentarLogin() {
        String correo = obtenerCorreo();
        String password = obtenerPassword();
        
        // Limpiar mensaje anterior
        lblMensajeError.setText("");
        
        // Validación básica
        if (correo.isEmpty()) {
            mostrarError("Por favor, ingrese su correo institucional");
            txtCorreo.requestFocus();
            return;
        }
        
        if (!correo.contains("@")) {
            mostrarError("El correo debe contener el símbolo @");
            txtCorreo.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            mostrarError("Por favor, ingrese su contraseña");
            txtPassword.requestFocus();
            return;
        }
        
        // Deshabilitar interacción
        btnIngresar.setEnabled(false);
        btnIngresar.setText("Verificando credenciales...");
        txtCorreo.setEnabled(false);
        txtPassword.setEnabled(false);
        
        // Cursor de espera
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        // Simular proceso en background
        Timer timer = new Timer(800, evt -> {
            ResultadoOperacion resultado = controlador.autenticar(correo, password);
            
            // Restaurar interfaz
            setCursor(Cursor.getDefaultCursor());
            btnIngresar.setEnabled(true);
            btnIngresar.setText("Ingresar al Sistema");
            txtCorreo.setEnabled(true);
            txtPassword.setEnabled(true);
            
            if (resultado.esExitoso()) {
                // Animación de éxito
                lblMensajeError.setForeground(ConstantesVisuales.COLOR_EXITO);
                lblMensajeError.setText("✓  Acceso concedido. Bienvenido!");
                
                // Delay antes de cerrar
                Timer delayTimer = new Timer(1000, e2 -> {
                    limpiarCampos();
                    cerrar();
                    if (onLoginSuccess != null) {
                        onLoginSuccess.accept(controlador.obtenerTipoUsuario());
                    }
                });
                delayTimer.setRepeats(false);
                delayTimer.start();
                
            } else {
                lblMensajeError.setForeground(ConstantesVisuales.COLOR_ERROR);
                mostrarError(resultado.getMensaje());
                txtPassword.setText("");
                txtPassword.requestFocus();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}