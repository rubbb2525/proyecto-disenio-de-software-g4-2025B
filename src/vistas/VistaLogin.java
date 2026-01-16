package vistas;

import controladores.ControladorAutenticacion;
import controladores.ControladorDirector;
import controladores.ControladorJefaDepartamento;
import dominio.MiembroFIS;
import vistas.componentes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class VistaLogin extends JFrame {
    private vistas.componentes.PlaceholderTextField txtCorreo;
    private JPasswordField txtPassword;
    private StyledButton btnIngresar;
    private StyledButton btnSalir;
    private JLabel lblMensaje;
    private JButton btnMostrarPassword;
    private ControladorAutenticacion controlador;

    // Constantes de colores según guía de diseño
    private static final Color COLOR_FONDO_PRINCIPAL = new Color(245, 247, 250);   // #F5F7FA
    private static final Color COLOR_FONDO_TARJETA = Color.WHITE;                  // #FFFFFF
    private static final Color COLOR_PRIMARIO = new Color(0, 61, 165);             // #003DA5
    private static final Color COLOR_SECUNDARIO = new Color(74, 144, 226);         // #4A90E2
    private static final Color COLOR_TEXTO_PRINCIPAL = new Color(44, 62, 80);      // #2C3E50
    private static final Color COLOR_TEXTO_SECUNDARIO = new Color(149, 165, 166);  // #95A5A6

    // Constructor
    public VistaLogin() {
        super("Sistema de Gestión de Ayudantes - FIS EPN");
        this.controlador = new ControladorAutenticacion();
        this.controlador.setVistaLogin(this);
        inicializar();
    }

    // Inicialización de componentes
    public void inicializar() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal con fondo
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO_PRINCIPAL);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel central (scrollable para mantener contenido centrado)
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(COLOR_FONDO_PRINCIPAL);

        // Logo y título
        JPanel panelEncabezado = new JPanel();
        panelEncabezado.setLayout(new BoxLayout(panelEncabezado, BoxLayout.Y_AXIS));
        panelEncabezado.setBackground(COLOR_FONDO_PRINCIPAL);
        panelEncabezado.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblLogo = new JLabel("🏛️");
        lblLogo.setFont(new Font("Arial", Font.BOLD, 60));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("SISTEMA DE GESTIÓN DE AYUDANTES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(COLOR_PRIMARIO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Departamento de Informática y Computación");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSubtitulo.setForeground(COLOR_TEXTO_SECUNDARIO);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelEncabezado.add(Box.createVerticalStrut(10));
        panelEncabezado.add(lblLogo);
        panelEncabezado.add(Box.createVerticalStrut(15));
        panelEncabezado.add(lblTitulo);
        panelEncabezado.add(Box.createVerticalStrut(5));
        panelEncabezado.add(lblSubtitulo);
        panelEncabezado.add(Box.createVerticalStrut(20));

        // Panel de formulario con tarjeta
        JPanel panelFormularioTarjeta = crearPanelFormulario();

        // Panel de mensajes de error
        lblMensaje = new JLabel("");
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMensaje.setForeground(new Color(231, 76, 60));  // #E74C3C (rojo error)
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel de versión
        JPanel panelVersion = new JPanel();
        panelVersion.setBackground(COLOR_FONDO_PRINCIPAL);
        panelVersion.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblVersion = new JLabel("Versión 1.0 - 2026");
        lblVersion.setFont(new Font("Arial", Font.PLAIN, 10));
        lblVersion.setForeground(COLOR_TEXTO_SECUNDARIO);
        panelVersion.add(lblVersion);

        // Agregar componentes al panel central
        panelContenido.add(panelEncabezado);
        panelContenido.add(panelFormularioTarjeta);
        panelContenido.add(Box.createVerticalStrut(10));
        panelContenido.add(lblMensaje);
        panelContenido.add(Box.createVerticalGlue());
        panelContenido.add(panelVersion);

        // Panel wrapper para mantener el contenido centrado
        JPanel panelWrapper = new JPanel(new BorderLayout());
        panelWrapper.setBackground(COLOR_FONDO_PRINCIPAL);
        panelWrapper.add(panelContenido, BorderLayout.CENTER);

        panelPrincipal.add(panelWrapper, BorderLayout.CENTER);
        add(panelPrincipal);

        configurarEventos();
    }

    private JPanel crearPanelFormulario() {
        JPanel panelFormularioTarjeta = new JPanel();
        panelFormularioTarjeta.setLayout(new BoxLayout(panelFormularioTarjeta, BoxLayout.Y_AXIS));
        panelFormularioTarjeta.setBackground(COLOR_FONDO_TARJETA);
        panelFormularioTarjeta.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15, new Color(225, 232, 237), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        panelFormularioTarjeta.setMaximumSize(new Dimension(350, 250));
        panelFormularioTarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campo Correo
        JLabel lblCorreo = new JLabel("Correo Institucional:");
        lblCorreo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblCorreo.setForeground(COLOR_TEXTO_PRINCIPAL);

        txtCorreo = new vistas.componentes.PlaceholderTextField("ejemplo@epn.edu.ec");
        txtCorreo.setMaximumSize(new Dimension(300, 40));

        // Campo Contraseña
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPassword.setForeground(COLOR_TEXTO_PRINCIPAL);

        JPanel panelPassword = new JPanel(new BorderLayout());
        panelPassword.setBackground(COLOR_FONDO_TARJETA);
        panelPassword.setMaximumSize(new Dimension(300, 40));

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 232, 237), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        btnMostrarPassword = new JButton("👁️");
        btnMostrarPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        btnMostrarPassword.setBorder(null);
        btnMostrarPassword.setBackground(COLOR_FONDO_TARJETA);
        btnMostrarPassword.setFocusPainted(false);
        btnMostrarPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnMostrarPassword.setPreferredSize(new Dimension(40, 40));

        panelPassword.add(txtPassword, BorderLayout.CENTER);
        panelPassword.add(btnMostrarPassword, BorderLayout.EAST);

        // Botón Ingresar
        btnIngresar = new StyledButton("INGRESAR", StyledButton.TipoBoton.PRIMARIO);
        btnIngresar.setMaximumSize(new Dimension(200, 45));
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Link Olvidé contraseña
        JButton btnOlvidePassword = new JButton("¿Olvidaste tu contraseña?");
        btnOlvidePassword.setFont(new Font("Arial", Font.PLAIN, 11));
        btnOlvidePassword.setForeground(COLOR_SECUNDARIO);
        btnOlvidePassword.setBorder(null);
        btnOlvidePassword.setBackground(COLOR_FONDO_TARJETA);
        btnOlvidePassword.setFocusPainted(false);
        btnOlvidePassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnOlvidePassword.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agregar componentes
        panelFormularioTarjeta.add(lblCorreo);
        panelFormularioTarjeta.add(Box.createVerticalStrut(8));
        panelFormularioTarjeta.add(txtCorreo);
        panelFormularioTarjeta.add(Box.createVerticalStrut(15));
        panelFormularioTarjeta.add(lblPassword);
        panelFormularioTarjeta.add(Box.createVerticalStrut(8));
        panelFormularioTarjeta.add(panelPassword);
        panelFormularioTarjeta.add(Box.createVerticalStrut(15));
        panelFormularioTarjeta.add(btnIngresar);
        panelFormularioTarjeta.add(Box.createVerticalStrut(12));
        panelFormularioTarjeta.add(btnOlvidePassword);

        return panelFormularioTarjeta;
    }

    // Configuración de eventos
    private void configurarEventos() {
        btnIngresar.addActionListener(e -> autenticar());

        // Enter en campos de texto para autenticar
        txtCorreo.addActionListener(e -> autenticar());
        txtPassword.addActionListener(e -> autenticar());

        // Mostrar/ocultar contraseña
        btnMostrarPassword.addActionListener(e -> {
            if (txtPassword.getEchoChar() == 0) {
                txtPassword.setEchoChar('●');
                btnMostrarPassword.setText("👁️");
            } else {
                txtPassword.setEchoChar((char) 0);
                btnMostrarPassword.setText("🙈");
            }
        });
    }

    private void autenticar() {
        if (validarEntradas()) {
            Map<String, String> credenciales = obtenerCredenciales();
            String correo = credenciales.get("correo");
            String password = credenciales.get("password");

            // Intentar autenticar
            dominio.MiembroFIS usuario = controlador.autenticar(correo, password);

            if (usuario != null) {
                System.out.println("✓ Autenticación exitosa: " + usuario.getNombresCompletos());
                ToastMessage.mostrar(this, "Bienvenido " + usuario.getNombresCompletos(), 
                                     ToastMessage.TipoToast.EXITO);

                // Ocultar login y abrir vista correspondiente
                ocultar();

                // Navegación según rol
                if (controlador.esDirector()) {
                    abrirVistaDirector((dominio.Director) usuario);
                } else if (controlador.esJefa()) {
                    abrirVistaJefa();
                } else {
                    mostrarError("Rol de usuario no reconocido");
                    mostrar();
                }
            } else {
                mostrarError("Correo o contraseña incorrectos");
            }
        }
    }

    private void abrirVistaDirector(dominio.Director director) {
        controladores.ControladorDirector controladorDirector = new controladores.ControladorDirector();
        controladorDirector.inicializar(director);
        
        VistaDirector vistaDirector = new VistaDirector(controladorDirector);
        vistaDirector.inicializar();
        vistaDirector.setDirector(director);
        vistaDirector.actualizarDatosProyecto(director.getProyectoAsignado());
        vistaDirector.mostrar();
    }

    private void abrirVistaJefa() {
        controladores.ControladorJefaDepartamento controladorJefa = new controladores.ControladorJefaDepartamento();
        controladorJefa.inicializar();
        
        VistaJefaDepartamento vistaJefa = new VistaJefaDepartamento(controladorJefa);
        vistaJefa.inicializar();
        vistaJefa.mostrar();
        
        // Cargar datos iniciales
        java.util.List<dominio.ProyectoInvestigacion> proyectos = controladorJefa.consultarProyectos();
        java.util.List<dominio.Ayudante> ayudantes = controladorJefa.consultarAyudantes();
        java.util.List<gestores.Notificacion> notificaciones = controladorJefa.consultarNotificaciones();
        
        vistaJefa.actualizarTablaProyectos(proyectos);
        vistaJefa.actualizarTablaAyudantes(ayudantes);
        vistaJefa.mostrarNotificaciones(notificaciones);
    }

    // Métodos públicos
    public void mostrar() {
        setVisible(true);
    }

    public void ocultar() {
        setVisible(false);
    }

    public Map<String, String> obtenerCredenciales() {
        Map<String, String> credenciales = new HashMap<>();
        credenciales.put("correo", txtCorreo.getTextReal());
        credenciales.put("password", new String(txtPassword.getPassword()));
        return credenciales;
    }

    public void mostrarError(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.setForeground(new Color(231, 76, 60));
        ToastMessage.mostrar(this, mensaje, ToastMessage.TipoToast.ERROR);
    }

    public void limpiarCampos() {
        txtCorreo.setText("");
        txtPassword.setText("");
        lblMensaje.setText("");
    }

    private boolean validarEntradas() {
        String correo = txtCorreo.getTextReal().trim();
        char[] password = txtPassword.getPassword();

        if (correo.isEmpty()) {
            mostrarError("Ingrese el correo institucional");
            txtCorreo.setError(true);
            return false;
        }

        if (!correo.contains("@")) {
            mostrarError("Correo inválido");
            txtCorreo.setError(true);
            return false;
        }

        if (password.length == 0) {
            mostrarError("Ingrese la contraseña");
            return false;
        }

        txtCorreo.setError(false);
        return true;
    }

    // Main para pruebas
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VistaLogin vista = new VistaLogin();
            vista.mostrar();
        });
    }
}
