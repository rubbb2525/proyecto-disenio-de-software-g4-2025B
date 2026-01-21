package vistas;

import controladores.ControladorDirector;
import models.*;
import models.Notificacion;
import vistas.componentes.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VistaDirector extends JFrame {
    // Colores según guía de diseño
    private static final Color COLOR_FONDO_PRINCIPAL = new Color(245, 247, 250);   // #F5F7FA
    private static final Color COLOR_FONDO_TARJETA = Color.WHITE;                  // #FFFFFF
    private static final Color COLOR_PRIMARIO = new Color(0, 61, 165);             // #003DA5
    private static final Color COLOR_SECUNDARIO = new Color(74, 144, 226);         // #4A90E2
    private static final Color COLOR_TEXTO_PRINCIPAL = new Color(44, 62, 80);      // #2C3E50
    private static final Color COLOR_TEXTO_SECUNDARIO = new Color(149, 165, 166);  // #95A5A6
    private static final Color COLOR_EXITO = new Color(46, 204, 113);              // #2ECC71
    private static final Color COLOR_BORDE = new Color(225, 232, 237);             // #E1E8ED

    // Componentes principales
    private JPanel panelInfoProyecto;
    private JLabel lblCodigoProyecto;
    private JLabel lblNombreProyecto;
    private JLabel lblTipoProyecto;
    
    // Tarjetas de estadísticas
    private JLabel lblPlanificados;
    private JLabel lblContratados;
    private JLabel lblActivos;
    
    // Tabla de ayudantes
    private JTable tablaAyudantes;
    private DefaultTableModel modeloAyudantes;
    private JButton btnRegistrarAyudante;
    private JButton btnDarDeBaja;
    private JButton btnActualizar;
    
    // Panel de mensajes
    private JPanel panelMensajes;
    private JLabel lblUltimoMensaje;
    
    // Control
    private ControladorDirector controlador;
    private Director director;

    // Constructor
    public VistaDirector() {
        super("Sistema de Gestión de Ayudantes - Director");
        this.controlador = new ControladorDirector();
    }

    public VistaDirector(ControladorDirector controlador) {
        this();
        this.controlador = controlador;
    }

    // Inicialización
    public void inicializar() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 700));

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO_PRINCIPAL);

        // Panel superior (encabezado)
        JPanel panelSuperior = crearPanelSuperior();
        
        // Panel de contenido con scroll
        JScrollPane scrollPrincipal = new JScrollPane(crearPanelContenido());
        scrollPrincipal.setBorder(null);
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(16);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(scrollPrincipal, BorderLayout.CENTER);

        add(panelPrincipal);

        // Wiring MVC: el controlador orquesta datos y la vista solo muestra
        if (controlador != null) {
            controlador.setVistaDirector(this);
            if (director != null) {
                controlador.inicializar(director);
                controlador.refrescarVista();
            }
        }
        configurarEventos();
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PRIMARIO);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        panel.setPreferredSize(new Dimension(0, 60));

        // Lado izquierdo
        JLabel lblInicio = new JLabel("Inicio", IconManager.getInstance().getHomeIcon(20), JLabel.LEFT);
        lblInicio.setFont(new Font("Arial", Font.BOLD, 13));
        lblInicio.setForeground(Color.WHITE);

        // Lado derecho
        JPanel panelDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelDerecha.setOpaque(false);

        JLabel lblUsuario = new JLabel("Director: " + (director != null ? director.getNombresCompletos() : "Juan Pérez"), IconManager.getInstance().getUserIcon(20), JLabel.LEFT);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 12));
        lblUsuario.setForeground(Color.WHITE);

        BadgeButton btnMensajes = new BadgeButton("Mensajes", IconManager.getInstance().getBellIcon(20));
        btnMensajes.setCount(1);
        btnMensajes.setBackground(new Color(243, 156, 18));

        StyledButton btnSalir = new StyledButton("Salir", StyledButton.TipoBoton.PELIGRO);
        btnSalir.setIcon(IconManager.getInstance().getLogoutIcon(20));
        btnSalir.setPreferredSize(new Dimension(100, 35));
        btnSalir.addActionListener(e -> System.exit(0));

        panelDerecha.add(lblUsuario);
        panelDerecha.add(btnMensajes);
        panelDerecha.add(btnSalir);

        panel.add(lblInicio, BorderLayout.WEST);
        panel.add(panelDerecha, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelContenido() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_FONDO_PRINCIPAL);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Sección 1: Información del Proyecto
        JPanel seccionProyecto = crearSeccionProyecto();
        seccionProyecto.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Sección 2: Gestión de Ayudantes
        JPanel seccionAyudantes = crearSeccionAyudantes();
        seccionAyudantes.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Sección 3: Mensajes Recibidos
        JPanel seccionMensajes = crearSeccionMensajes();
        seccionMensajes.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(seccionProyecto);
        panel.add(Box.createVerticalStrut(20));
        panel.add(seccionAyudantes);
        panel.add(Box.createVerticalStrut(20));
        panel.add(seccionMensajes);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel crearSeccionProyecto() {
        JPanel seccion = new JPanel();
        seccion.setLayout(new BoxLayout(seccion, BoxLayout.Y_AXIS));
        seccion.setBackground(COLOR_FONDO_PRINCIPAL);

        // Título
        JLabel lblTitulo = new JLabel("INFORMACIÓN DEL PROYECTO", IconManager.getInstance().getChartIcon(20), JLabel.LEFT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(COLOR_TEXTO_PRINCIPAL);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Panel con información
        panelInfoProyecto = new JPanel(new BorderLayout(0, 15));
        panelInfoProyecto.setBackground(COLOR_FONDO_TARJETA);
        panelInfoProyecto.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panelInfoProyecto.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Información básica
        JPanel panelDatos = new JPanel(new GridLayout(2, 2, 15, 8));
        panelDatos.setOpaque(false);

        JLabel lblCodLabel = new JLabel("Código:");
        lblCodLabel.setFont(new Font("Arial", Font.BOLD, 11));
        lblCodigoProyecto = new JLabel("PROJ-001");
        lblCodigoProyecto.setFont(new Font("Arial", Font.PLAIN, 11));

        JLabel lblTipoLabel = new JLabel("Tipo:");
        lblTipoLabel.setFont(new Font("Arial", Font.BOLD, 11));
        lblTipoProyecto = new JLabel("INTERNO");
        lblTipoProyecto.setFont(new Font("Arial", Font.PLAIN, 11));

        JLabel lblNomLabel = new JLabel("Nombre:");
        lblNomLabel.setFont(new Font("Arial", Font.BOLD, 11));
        lblNombreProyecto = new JLabel("Sistema de Inteligencia Artificial");
        lblNombreProyecto.setFont(new Font("Arial", Font.PLAIN, 11));

        panelDatos.add(lblCodLabel);
        panelDatos.add(lblCodigoProyecto);
        panelDatos.add(lblTipoLabel);
        panelDatos.add(lblTipoProyecto);

        // Tarjetas de estadísticas
        JPanel panelTarjetas = new JPanel(new GridLayout(1, 3, 15, 0));
        panelTarjetas.setOpaque(false);

        JPanel cardPlanificados = crearTarjetaEstadistica("Planificados", "5", "🎯", new Color(52, 152, 219));
        JPanel cardContratados = crearTarjetaEstadistica("Contratados", "5", "📝", new Color(39, 174, 96));
        JPanel cardActivos = crearTarjetaEstadistica("Activos", "3", "✅", new Color(243, 156, 18));

        panelTarjetas.add(cardPlanificados);
        panelTarjetas.add(cardContratados);
        panelTarjetas.add(cardActivos);

        // Panel superior con nombre
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.add(panelDatos, BorderLayout.WEST);

        panelInfoProyecto.add(panelSuperior, BorderLayout.NORTH);
        panelInfoProyecto.add(panelTarjetas, BorderLayout.CENTER);

        seccion.add(lblTitulo);
        seccion.add(Box.createVerticalStrut(10));
        seccion.add(panelInfoProyecto);

        return seccion;
    }

    private JPanel crearTarjetaEstadistica(String titulo, String valor, String emoji, Color colorBorde) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_FONDO_TARJETA);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, colorBorde, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 36));
        lblValor.setForeground(colorBorde);
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTitulo.setForeground(COLOR_TEXTO_SECUNDARIO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblIcono = new JLabel(emoji);
        lblIcono.setFont(new Font("Arial", Font.PLAIN, 20));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblValor);
        card.add(Box.createVerticalStrut(5));
        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(5));
        card.add(lblIcono);

        return card;
    }

    private JPanel crearSeccionAyudantes() {
        JPanel seccion = new JPanel();
        seccion.setLayout(new BoxLayout(seccion, BoxLayout.Y_AXIS));
        seccion.setBackground(COLOR_FONDO_PRINCIPAL);

        // Título
        JLabel lblTitulo = new JLabel("GESTIÓN DE AYUDANTES", IconManager.getInstance().getUsersIcon(20), JLabel.LEFT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(COLOR_TEXTO_PRINCIPAL);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Panel contenedor
        JPanel panelContenedor = new JPanel(new BorderLayout(0, 10));
        panelContenedor.setBackground(COLOR_FONDO_TARJETA);
        panelContenedor.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panelContenedor.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Botones de acción
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBotones.setOpaque(false);

        btnRegistrarAyudante = new StyledButton("Registrar Ayudante", StyledButton.TipoBoton.EXITO);
        btnRegistrarAyudante.setIcon(IconManager.getInstance().getAddIcon(20));
        btnDarDeBaja = new StyledButton("Dar de Baja", StyledButton.TipoBoton.PELIGRO);
        btnDarDeBaja.setIcon(IconManager.getInstance().getDeleteIcon(20));
        btnActualizar = new StyledButton("Actualizar", StyledButton.TipoBoton.SECUNDARIO);
        btnActualizar.setIcon(IconManager.getInstance().getRefreshIcon(20));

        panelBotones.add(btnRegistrarAyudante);
        panelBotones.add(btnDarDeBaja);
        panelBotones.add(btnActualizar);

        // Tabla de ayudantes
        String[] columnas = {"Código", "Nombre", "Carrera", "Nivel", "Estado", "Acciones"};
        modeloAyudantes = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Solo columna de acciones
            }
        };

        tablaAyudantes = new JTable(modeloAyudantes);
        tablaAyudantes.setRowHeight(35);
        tablaAyudantes.setFont(new Font("Arial", Font.PLAIN, 11));
        tablaAyudantes.setGridColor(COLOR_BORDE);
        tablaAyudantes.setSelectionBackground(new Color(74, 144, 226, 100));

        // Renderer para columna Estado
        tablaAyudantes.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value != null ? value.toString() : "");
                label.setFont(new Font("Arial", Font.BOLD, 11));
                label.setOpaque(true);
                
                if ("Activo".equals(value) || "ACTIVO".equals(value)) {
                    label.setForeground(new Color(39, 174, 96));
                    label.setText("✅ ACTIVO");
                    label.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                } else {
                    label.setForeground(new Color(192, 57, 43));
                    label.setText("⭕ INACTIVO");
                    label.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                }
                
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaAyudantes);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));

        // Label de estadísticas
        JLabel lblEstadisticas = new JLabel("Mostrando 3 de 3 ayudantes");
        lblEstadisticas.setFont(new Font("Arial", Font.PLAIN, 10));
        lblEstadisticas.setForeground(COLOR_TEXTO_SECUNDARIO);

        panelContenedor.add(panelBotones, BorderLayout.NORTH);
        panelContenedor.add(scrollPane, BorderLayout.CENTER);
        panelContenedor.add(lblEstadisticas, BorderLayout.SOUTH);

        seccion.add(lblTitulo);
        seccion.add(Box.createVerticalStrut(10));
        seccion.add(panelContenedor);

        return seccion;
    }

    private JPanel crearSeccionMensajes() {
        JPanel seccion = new JPanel();
        seccion.setLayout(new BoxLayout(seccion, BoxLayout.Y_AXIS));
        seccion.setBackground(COLOR_FONDO_PRINCIPAL);

        // Título con link "Ver todos"
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setOpaque(false);
        panelTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitulo = new JLabel("MENSAJES RECIBIDOS", IconManager.getInstance().getMessagesIcon(20), JLabel.LEFT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(COLOR_TEXTO_PRINCIPAL);

        JButton btnVerTodos = new JButton("Ver todos →");
        btnVerTodos.setFont(new Font("Arial", Font.PLAIN, 11));
        btnVerTodos.setForeground(COLOR_SECUNDARIO);
        btnVerTodos.setBorder(null);
        btnVerTodos.setBackground(COLOR_FONDO_PRINCIPAL);
        btnVerTodos.setFocusPainted(false);
        btnVerTodos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panelTitulo.add(lblTitulo, BorderLayout.WEST);
        panelTitulo.add(btnVerTodos, BorderLayout.EAST);

        // Panel de mensaje
        panelMensajes = new JPanel(new BorderLayout(15, 10));
        panelMensajes.setBackground(COLOR_FONDO_TARJETA);
        panelMensajes.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panelMensajes.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Icono de mensaje no leído
        JLabel lblIcono = new JLabel("🔴");
        lblIcono.setFont(new Font("Arial", Font.PLAIN, 20));

        // Panel de contenido
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setOpaque(false);

        JLabel lblRemitente = new JLabel("Jefa de Departamento - 15/01/2026 10:30");
        lblRemitente.setFont(new Font("Arial", Font.BOLD, 11));
        lblRemitente.setForeground(COLOR_TEXTO_PRINCIPAL);
        lblRemitente.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblUltimoMensaje = new JLabel("\"Se requieren 2 ayudantes más para cumplir con la meta\"");
        lblUltimoMensaje.setFont(new Font("Arial", Font.PLAIN, 11));
        lblUltimoMensaje.setForeground(COLOR_TEXTO_SECUNDARIO);
        lblUltimoMensaje.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Botones de acción
        JPanel panelBotonesMensaje = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBotonesMensaje.setOpaque(false);
        panelBotonesMensaje.setAlignmentX(Component.LEFT_ALIGNMENT);

        StyledButton btnVerCompleto = new StyledButton("Ver completo", StyledButton.TipoBoton.SECUNDARIO);
        btnVerCompleto.setIcon(IconManager.getInstance().getIcon("report", 16));
        btnVerCompleto.setPreferredSize(new Dimension(130, 35));

        StyledButton btnResponder = new StyledButton("Responder", StyledButton.TipoBoton.PRIMARIO);
        btnResponder.setIcon(IconManager.getInstance().getSendIcon(16));
        btnResponder.setPreferredSize(new Dimension(120, 35));

        panelBotonesMensaje.add(btnVerCompleto);
        panelBotonesMensaje.add(btnResponder);

        panelContenido.add(lblRemitente);
        panelContenido.add(Box.createVerticalStrut(5));
        panelContenido.add(lblUltimoMensaje);
        panelContenido.add(Box.createVerticalStrut(10));
        panelContenido.add(panelBotonesMensaje);

        panelMensajes.add(lblIcono, BorderLayout.WEST);
        panelMensajes.add(panelContenido, BorderLayout.CENTER);

        seccion.add(panelTitulo);
        seccion.add(Box.createVerticalStrut(10));
        seccion.add(panelMensajes);

        return seccion;
    }

    private void configurarEventos() {
        btnRegistrarAyudante.addActionListener(e -> {
            Map<String, Object> datos = mostrarFormularioAyudante();
            if (datos != null && controlador != null) {
                controlador.registrarAyudanteDesdeVista(datos);
            }
        });
        btnDarDeBaja.addActionListener(e -> {
            Map<String, Object> datos = mostrarDialogoDarDeBaja();
            if (datos != null && controlador != null) {
                int filaSeleccionada = tablaAyudantes.getSelectedRow();
                String motivo = String.valueOf(datos.get("motivo"));
                Date fecha = (Date) datos.get("fecha");
                controlador.darDeBajaAyudantePorIndice(filaSeleccionada, motivo, fecha);
            }
        });
        btnActualizar.addActionListener(e -> {
            if (controlador != null) {
                controlador.refrescarVista();
                ToastMessage.mostrar(this, "Datos actualizados", ToastMessage.TipoToast.EXITO);
            }
        });
    }

    // Métodos de actualización
    public void actualizarTablaAyudantes(List<Ayudante> ayudantes) {
        modeloAyudantes.setRowCount(0);

        for (Ayudante ayu : ayudantes) {
            Object[] fila = {
                ayu.getNombres() != null ? ayu.getNombres().substring(0, Math.min(3, ayu.getNombres().length())) : "001",
                ayu.getNombresCompletos(),
                ayu.getCarrera() != null ? ayu.getCarrera() : "N/A",
                ayu.getNivel(),
                "Activo",
                "✏️ ❌" // Iconos de acciones
            };
            modeloAyudantes.addRow(fila);
        }
    }

    public void actualizarDatosProyecto(ProyectoInvestigacion proyecto) {
        if (proyecto != null) {
            lblCodigoProyecto.setText(proyecto.getCodigoProyecto());
            lblNombreProyecto.setText(proyecto.getNombreProyecto());
            lblTipoProyecto.setText(proyecto.getTipoProyecto() != null ? proyecto.getTipoProyecto().toString() : "N/A");
            
            // Actualizar tarjetas
            actualizarTarjetaEstadistica(lblPlanificados, String.valueOf(proyecto.getAyudantesPlanificados()));
            actualizarTarjetaEstadistica(lblContratados, String.valueOf(proyecto.getAyudantesContratados()));
            actualizarTarjetaEstadistica(lblActivos, String.valueOf(proyecto.getAyudantesActivos()));
        }
    }

    private void actualizarTarjetaEstadistica(JLabel label, String valor) {
        if (label != null) {
            label.setText(valor);
        }
    }

    // Diálogos
    public Map<String, Object> mostrarFormularioAyudante() {
        DialogoFormularioAyudante dialogo = new DialogoFormularioAyudante(this, controlador, controlador.getEstudianteDAO());
        dialogo.mostrar();
        if (dialogo.isGuardado()) {
            Map<String, Object> datos = dialogo.obtenerDatos();
            return datos;
        }
        return null;
    }

    public Map<String, Object> mostrarDialogoDarDeBaja() {
        int filaSeleccionada = tablaAyudantes.getSelectedRow();
        if (filaSeleccionada < 0) {
            ToastMessage.mostrar(this, "Seleccione un ayudante de la tabla", ToastMessage.TipoToast.ADVERTENCIA);
            return null;
        }

        String nombreAyudante = (String) tablaAyudantes.getValueAt(filaSeleccionada, 1);
        
        // Diálogo personalizado de confirmación
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel lblPregunta = new JLabel("¿Está seguro de dar de baja al ayudante?");
        lblPregunta.setFont(new Font("Arial", Font.BOLD, 12));
        
        JLabel lblNombre = new JLabel(nombreAyudante);
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 11));
        
        JLabel lblMotivo = new JLabel("Motivo de salida:");
        lblMotivo.setFont(new Font("Arial", Font.PLAIN, 11));
        
        JComboBox<String> cboMotivo = new JComboBox<>(new String[]{
            "Renuncia Voluntaria",
            "Término de Contrato",
            "Bajo Rendimiento",
            "Otros"
        });
        
        JLabel lblObservaciones = new JLabel("Observaciones (opcional):");
        lblObservaciones.setFont(new Font("Arial", Font.PLAIN, 11));
        
        JTextArea txtObservaciones = new JTextArea(3, 20);
        txtObservaciones.setBorder(BorderFactory.createLineBorder(COLOR_BORDE));
        JScrollPane scrollObservaciones = new JScrollPane(txtObservaciones);
        
        panel.add(lblPregunta);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblNombre);
        panel.add(Box.createVerticalStrut(15));
        panel.add(lblMotivo);
        panel.add(cboMotivo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblObservaciones);
        panel.add(scrollObservaciones);
        
        int resultado = JOptionPane.showConfirmDialog(
            this, 
            panel, 
            "Dar de Baja Ayudante", 
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (resultado == JOptionPane.OK_OPTION) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("motivo", cboMotivo.getSelectedItem());
            datos.put("observaciones", txtObservaciones.getText());
            datos.put("fecha", new Date());
            return datos;
        }

        return null;
    }

    public File mostrarDialogoSubirDocumento() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Documento");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".pdf");
            }
            public String getDescription() {
                return "Archivos PDF (*.pdf)";
            }
        });
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            ToastMessage.mostrar(this, "Documento cargado: " + archivo.getName(), ToastMessage.TipoToast.EXITO);
            return archivo;
        }
        
        return null;
    }

    // Mensajes
    public void mostrarMensaje(String mensaje) {
        ToastMessage.mostrar(this, mensaje, ToastMessage.TipoToast.INFO);
    }

    public void mostrarError(String mensaje) {
        ToastMessage.mostrar(this, mensaje, ToastMessage.TipoToast.ERROR);
    }

    public boolean confirmar(String mensaje) {
        int resultado = JOptionPane.showConfirmDialog(this, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION);
        return resultado == JOptionPane.YES_OPTION;
    }

    // Visibilidad
    public void mostrar() {
        setVisible(true);
    }

    public void ocultar() {
        setVisible(false);
    }

    // Getters y Setters
    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }
}
