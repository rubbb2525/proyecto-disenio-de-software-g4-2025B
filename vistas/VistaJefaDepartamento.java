package vistas;

import controladores.ControladorJefaDepartamento;
import models.*;
import models.Notificacion;
import vistas.componentes.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VistaJefaDepartamento extends JFrame {
    // Colores según guía de diseño
    private static final Color COLOR_FONDO_PRINCIPAL = new Color(245, 247, 250);   // #F5F7FA
    private static final Color COLOR_FONDO_TARJETA = Color.WHITE;                  // #FFFFFF
    private static final Color COLOR_PRIMARIO = new Color(0, 61, 165);             // #003DA5
    private static final Color COLOR_SECUNDARIO = new Color(74, 144, 226);         // #4A90E2
    private static final Color COLOR_TEXTO_PRINCIPAL = new Color(44, 62, 80);      // #2C3E50
    private static final Color COLOR_TEXTO_SECUNDARIO = new Color(149, 165, 166);  // #95A5A6
    private static final Color COLOR_EXITO = new Color(46, 204, 113);              // #2ECC71
    private static final Color COLOR_ERROR = new Color(231, 76, 60);               // #E74C3C
    private static final Color COLOR_BORDE = new Color(225, 232, 237);             // #E1E8ED

    // Componentes principales
    private JTabbedPane tabPrincipal;
    private JPanel panelAyudantes;
    private JPanel panelReportes;
    private JPanel panelNotificacionesLateral;
    
    // Pestaña Ayudantes - Filtros
    private JComboBox<String> cboProyecto;
    private JComboBox<String> cboCarrera;
    private JComboBox<String> cboNivel;
    private JComboBox<String> cboEstado;
    private PlaceholderTextField txtBuscar;
    private JCheckBox chkSoloActivos;
    private JButton btnBuscar;
    private JButton btnGenerarReporte;
    private JButton btnEnviarMensaje;
    private JButton btnActualizar;
    
    // Tabla de Ayudantes
    private JTable tablaAyudantes;
    private DefaultTableModel modeloAyudantes;
    private JLabel lblEstadisticas;
    private JButton btnAnterior;
    private JButton btnSiguiente;
    private JLabel lblPagina;
    private int paginaActual = 1;
    private int elementosPorPagina = 5;
    private List<Ayudante> ayudantesFiltrados = new ArrayList<>();
    
    // Panel Reportes
    private JComboBox<String> cboTipoReporte;
    private JButton btnGenerarReportePanel;
    private JTextArea areaReporte; // vista previa del reporte
    
    // Notificaciones
    private JPanel panelListaNotificaciones;
    private JButton btnMarcarTodas;
    
    // Control
    private ControladorJefaDepartamento controlador;
    private JefaDepartamento jefa;

    // Constructor
    public VistaJefaDepartamento() {
        super("Sistema de Gestión de Ayudantes - Jefa de Departamento");
        this.controlador = new ControladorJefaDepartamento();
    }

    public VistaJefaDepartamento(ControladorJefaDepartamento controlador) {
        this();
        this.controlador = controlador;
    }

    // Inicialización
    public void inicializar() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 800));

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO_PRINCIPAL);

        // Panel superior (encabezado)
        JPanel panelSuperior = crearPanelSuperior();
        
        // Panel contenido central
        JPanel panelCentral = new JPanel(new BorderLayout());
        
        // Tabs principales
        tabPrincipal = new JTabbedPane();
        tabPrincipal.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panelAyudantes = crearPanelAyudantes();
        panelReportes = crearPanelReportes();
        
        tabPrincipal.addTab("Ayudantes", IconManager.getInstance().getUsersIcon(20), panelAyudantes);
        tabPrincipal.addTab("Reportes", IconManager.getInstance().getChartIcon(20), panelReportes);
        
        panelCentral.add(tabPrincipal, BorderLayout.CENTER);
        
        // Panel lateral de notificaciones
        panelNotificacionesLateral = crearPanelNotificacionesLateral();
        
        // Agregar componentes
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelNotificacionesLateral, BorderLayout.EAST);
        
        add(panelPrincipal);

        // Wiring MVC
        if (controlador != null) {
            controlador.setVistaJefa(this);
            controlador.inicializar();
            controlador.refrescarVista();
        }
        configurarEventos();
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PRIMARIO);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        panel.setPreferredSize(new Dimension(0, 60));

        // Lado izquierdo
        JLabel lblBienvenida = new JLabel("Inicio", IconManager.getInstance().getHomeIcon(20), JLabel.LEFT);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 13));
        lblBienvenida.setForeground(Color.WHITE);

        // Lado derecho
        JPanel panelDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelDerecha.setOpaque(false);

        JLabel lblUsuario = new JLabel("Jefa: Pamela Flores", IconManager.getInstance().getUserIcon(20), JLabel.LEFT);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 12));
        lblUsuario.setForeground(Color.WHITE);

        BadgeButton btnMensajes = new BadgeButton("Mensajes", IconManager.getInstance().getBellIcon(20));
        btnMensajes.setCount(2);
        btnMensajes.setBackground(new Color(243, 156, 18));

        StyledButton btnSalir = new StyledButton("Salir", StyledButton.TipoBoton.PELIGRO);
        btnSalir.setIcon(IconManager.getInstance().getLogoutIcon(20));
        btnSalir.setPreferredSize(new Dimension(100, 35));
        btnSalir.addActionListener(e -> {
            System.exit(0);
        });

        panelDerecha.add(lblUsuario);
        panelDerecha.add(btnMensajes);
        panelDerecha.add(btnSalir);

        panel.add(lblBienvenida, BorderLayout.WEST);
        panel.add(panelDerecha, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelAyudantes() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(COLOR_FONDO_PRINCIPAL);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel de Filtros
        JPanel panelFiltros = crearPanelFiltros();
        
        // Panel de Botones de Acciones
        JPanel panelBotones = crearPanelBotonesAyudantes();
        
        // Panel de Tabla y Paginación
        JPanel panelTabla = crearPanelTablaAyudantes();

        // Agregar al panel principal
        JPanel panelNorte = new JPanel(new BorderLayout(0, 10));
        panelNorte.setOpaque(false);
        panelNorte.add(panelFiltros, BorderLayout.NORTH);
        panelNorte.add(panelBotones, BorderLayout.CENTER);

        panel.add(panelNorte, BorderLayout.NORTH);
        panel.add(panelTabla, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelFiltros() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_FONDO_TARJETA);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Título
        JLabel lblFiltros = new JLabel("FILTROS Y BÚSQUEDA", IconManager.getInstance().getSearchIcon(20), JLabel.LEFT);
        lblFiltros.setFont(new Font("Arial", Font.BOLD, 12));
        lblFiltros.setForeground(COLOR_TEXTO_PRINCIPAL);

        // Fila 1: Combos de filtros
        JPanel fila1 = new JPanel(new GridLayout(1, 4, 10, 0));
        fila1.setOpaque(false);

        cboProyecto = new JComboBox<>(new String[]{"Todos", "PROJ-001", "PROJ-002", "PROJ-003"});
        cboProyecto.setFont(new Font("Arial", Font.PLAIN, 11));

        cboCarrera = new JComboBox<>(new String[]{"Todas", "Ingeniería en Computación", "Ingeniería de Software"});
        cboCarrera.setFont(new Font("Arial", Font.PLAIN, 11));

        cboNivel = new JComboBox<>(new String[]{"Todos", "5", "6", "7", "8"});
        cboNivel.setFont(new Font("Arial", Font.PLAIN, 11));

        cboEstado = new JComboBox<>(new String[]{"Todos", "Activo", "Inactivo", "Pausado"});
        cboEstado.setFont(new Font("Arial", Font.PLAIN, 11));

        fila1.add(new JLabel("Proyecto:"));
        fila1.add(cboProyecto);
        fila1.add(new JLabel("Carrera:"));
        fila1.add(cboCarrera);

        // Fila 2: Búsqueda y opciones
        JPanel fila2 = new JPanel(new BorderLayout(10, 0));
        fila2.setOpaque(false);
        fila2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        fila2.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel panelBusqueda = new JPanel(new BorderLayout(5, 0));
        panelBusqueda.setOpaque(false);

        txtBuscar = new PlaceholderTextField("Buscar por nombre o código...");
        txtBuscar.setPreferredSize(new Dimension(200, 35));

        btnBuscar = new StyledButton("Buscar", StyledButton.TipoBoton.SECUNDARIO);
        btnBuscar.setIcon(IconManager.getInstance().getSearchIcon(16));
        btnBuscar.setPreferredSize(new Dimension(100, 35));

        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);

        chkSoloActivos = new JCheckBox("Solo mostrar activos");
        chkSoloActivos.setOpaque(false);
        chkSoloActivos.setFont(new Font("Arial", Font.PLAIN, 11));

        fila2.add(panelBusqueda, BorderLayout.WEST);
        fila2.add(chkSoloActivos, BorderLayout.EAST);

        // Agregar componentes
        panel.add(lblFiltros);
        panel.add(Box.createVerticalStrut(10));
        panel.add(crearFilaConEtiquetas(new JLabel("Filtrar por:"), fila1, "Nivel:", cboNivel, "Estado:", cboEstado));
        panel.add(Box.createVerticalStrut(10));
        panel.add(fila2);

        return panel;
    }

    private JPanel crearFilaConEtiquetas(JLabel etiqueta, JPanel fila1, String etiqueta2, JComboBox combo2, String etiqueta3, JComboBox combo3) {
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 5));
        panel.setOpaque(false);
        
        panel.add(new JLabel("Proyecto:"));
        panel.add(cboProyecto);
        panel.add(new JLabel("Carrera:"));
        panel.add(cboCarrera);
        
        panel.add(new JLabel(etiqueta2));
        panel.add(combo2);
        panel.add(new JLabel(etiqueta3));
        panel.add(combo3);
        
        return panel;
    }

    private JPanel crearPanelBotonesAyudantes() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);

        btnGenerarReporte = new StyledButton("Generar Reporte", StyledButton.TipoBoton.EXITO);
        btnGenerarReporte.setIcon(IconManager.getInstance().getReportIcon(20));
        btnEnviarMensaje = new StyledButton("Enviar Mensaje", StyledButton.TipoBoton.SECUNDARIO);
        btnEnviarMensaje.setIcon(IconManager.getInstance().getSendIcon(20));
        btnActualizar = new StyledButton("Actualizar", StyledButton.TipoBoton.SECUNDARIO);
        btnActualizar.setIcon(IconManager.getInstance().getRefreshIcon(20));

        panel.add(btnGenerarReporte);
        panel.add(btnEnviarMensaje);
        panel.add(btnActualizar);

        return panel;
    }

    private JPanel crearPanelTablaAyudantes() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        // Tabla
        String[] columnas = {"Código", "Nombre", "Carrera", "Nivel", "Proyecto", "Estado"};
        modeloAyudantes = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaAyudantes = new JTable(modeloAyudantes);
        tablaAyudantes.setRowHeight(28);
        tablaAyudantes.setFont(new Font("Arial", Font.PLAIN, 11));
        tablaAyudantes.setGridColor(COLOR_BORDE);
        tablaAyudantes.setSelectionBackground(new Color(74, 144, 226, 100));

        // Renderer personalizado para estado
        tablaAyudantes.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setFont(new Font("Arial", Font.BOLD, 11));
                
                if ("Activo".equals(value.toString()) || "✅ ACTIVO".equals(value.toString())) {
                    label.setForeground(new Color(39, 174, 96));
                    label.setText("✅ " + value.toString());
                } else {
                    label.setForeground(new Color(192, 57, 43));
                    label.setText("⭕ " + value.toString());
                }
                
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaAyudantes);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));

        // Panel de paginación
        JPanel panelPaginacion = crearPanelPaginacion();

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelPaginacion, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelPaginacion() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Lado izquierdo: estadísticas
        lblEstadisticas = new JLabel("Mostrando 5 de 12 ayudantes | Activos: 4 | Inactivos: 1");
        lblEstadisticas.setFont(new Font("Arial", Font.PLAIN, 11));
        lblEstadisticas.setForeground(COLOR_TEXTO_SECUNDARIO);

        // Lado derecho: navegación
        JPanel panelNav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelNav.setOpaque(false);

        btnAnterior = new StyledButton("◀️", StyledButton.TipoBoton.SECUNDARIO);
        btnAnterior.setPreferredSize(new Dimension(40, 35));

        lblPagina = new JLabel("Página 1/3");
        lblPagina.setFont(new Font("Arial", Font.PLAIN, 11));
        lblPagina.setPreferredSize(new Dimension(80, 35));
        lblPagina.setHorizontalAlignment(SwingConstants.CENTER);

        btnSiguiente = new StyledButton("▶️", StyledButton.TipoBoton.SECUNDARIO);
        btnSiguiente.setPreferredSize(new Dimension(40, 35));

        panelNav.add(btnAnterior);
        panelNav.add(lblPagina);
        panelNav.add(btnSiguiente);

        panel.add(lblEstadisticas, BorderLayout.WEST);
        panel.add(panelNav, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelReportes() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(COLOR_FONDO_PRINCIPAL);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel de selección
        JPanel panelSeleccion = new JPanel();
        panelSeleccion.setLayout(new BoxLayout(panelSeleccion, BoxLayout.Y_AXIS));
        panelSeleccion.setBackground(COLOR_FONDO_TARJETA);
        panelSeleccion.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTipo = new JLabel("Tipo de Reporte:");
        lblTipo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTipo.setForeground(COLOR_TEXTO_PRINCIPAL);

        cboTipoReporte = new JComboBox<>(new String[]{
            "General de Ayudantes",
            "Por Proyecto",
            "Por Carrera",
            "Presupuestario",
            "Estadísticas por Período"
        });
        cboTipoReporte.setFont(new Font("Arial", Font.PLAIN, 11));
        cboTipoReporte.setMaximumSize(new Dimension(300, 35));

        btnGenerarReportePanel = new StyledButton("Generar Reporte", StyledButton.TipoBoton.EXITO);
        btnGenerarReportePanel.setIcon(IconManager.getInstance().getChartIcon(20));
        btnGenerarReportePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelSeleccion.add(lblTipo);
        panelSeleccion.add(Box.createVerticalStrut(8));
        panelSeleccion.add(cboTipoReporte);
        panelSeleccion.add(Box.createVerticalStrut(15));
        panelSeleccion.add(btnGenerarReportePanel);

        // Panel de vista previa
        JPanel panelPrevia = new JPanel(new BorderLayout());
        panelPrevia.setBackground(COLOR_FONDO_TARJETA);
        panelPrevia.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblPrevia = new JLabel("Vista Previa del Reporte:");
        lblPrevia.setFont(new Font("Arial", Font.BOLD, 12));
        lblPrevia.setForeground(COLOR_TEXTO_PRINCIPAL);

        areaReporte = new JTextArea();
        areaReporte.setEditable(false);
        areaReporte.setFont(new Font("Courier", Font.PLAIN, 10));
        areaReporte.setLineWrap(true);
        areaReporte.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(areaReporte);
        scrollPane.setBorder(null);

        panelPrevia.add(lblPrevia, BorderLayout.NORTH);
        panelPrevia.add(scrollPane, BorderLayout.CENTER);

        panel.add(panelSeleccion, BorderLayout.NORTH);
        panel.add(panelPrevia, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelNotificacionesLateral() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(COLOR_FONDO_PRINCIPAL);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 20));
        panel.setPreferredSize(new Dimension(300, 0));

        // Encabezado
        JPanel panelEncabezado = new JPanel(new BorderLayout());
        panelEncabezado.setOpaque(false);

        JLabel lblNotif = new JLabel("NOTIFICACIONES", IconManager.getInstance().getBellIcon(20), JLabel.LEFT);
        lblNotif.setFont(new Font("Arial", Font.BOLD, 12));
        lblNotif.setForeground(COLOR_TEXTO_PRINCIPAL);

        btnMarcarTodas = new JButton("Marcar todas");
        btnMarcarTodas.setFont(new Font("Arial", Font.PLAIN, 10));
        btnMarcarTodas.setBorder(null);
        btnMarcarTodas.setForeground(COLOR_SECUNDARIO);
        btnMarcarTodas.setBackground(COLOR_FONDO_PRINCIPAL);
        btnMarcarTodas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panelEncabezado.add(lblNotif, BorderLayout.WEST);
        panelEncabezado.add(btnMarcarTodas, BorderLayout.EAST);

        // Panel de notificaciones (scroll)
        panelListaNotificaciones = new JPanel();
        panelListaNotificaciones.setLayout(new BoxLayout(panelListaNotificaciones, BoxLayout.Y_AXIS));
        panelListaNotificaciones.setBackground(COLOR_FONDO_PRINCIPAL);

        JScrollPane scrollNotif = new JScrollPane(panelListaNotificaciones);
        scrollNotif.setBorder(null);
        scrollNotif.setBackground(COLOR_FONDO_PRINCIPAL);

        panel.add(panelEncabezado, BorderLayout.NORTH);
        panel.add(scrollNotif, BorderLayout.CENTER);

        return panel;
    }

    private void configurarEventos() {
        // Filtros automáticos
        ActionListener filtroListener = e -> aplicarFiltros();
        cboProyecto.addActionListener(filtroListener);
        cboCarrera.addActionListener(filtroListener);
        cboNivel.addActionListener(filtroListener);
        cboEstado.addActionListener(filtroListener);
        chkSoloActivos.addActionListener(filtroListener);

        // Búsqueda en tiempo real
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { buscarAyudantes(); }
            @Override
            public void removeUpdate(DocumentEvent e) { buscarAyudantes(); }
            @Override
            public void changedUpdate(DocumentEvent e) { buscarAyudantes(); }
        });

        btnBuscar.addActionListener(e -> buscarAyudantes());

        // Paginación
        btnAnterior.addActionListener(e -> cambiarPagina(-1));
        btnSiguiente.addActionListener(e -> cambiarPagina(1));

        // Botones de acciones
        btnActualizar.addActionListener(e -> {
            aplicarFiltros();
            ToastMessage.mostrar(this, "Datos actualizados", ToastMessage.TipoToast.EXITO);
        });

        btnEnviarMensaje.addActionListener(e -> enviarMensajeDirector());
        btnGenerarReporte.addActionListener(e -> generarReporte());
    }

    private void aplicarFiltros() {
        paginaActual = 1;
        String codigoProyecto = (String) cboProyecto.getSelectedItem();
        String carrera = (String) cboCarrera.getSelectedItem();
        Integer nivel = null;
        try { nivel = Integer.valueOf((String) cboNivel.getSelectedItem()); } catch (Exception ignored) {}
        String estado = (String) cboEstado.getSelectedItem();
        boolean soloActivos = chkSoloActivos.isSelected();
        String criterio = txtBuscar.getTextReal().trim();
        List<Ayudante> resultado = controlador != null
            ? controlador.filtrarAyudantes(codigoProyecto, carrera, nivel, estado, soloActivos, criterio)
            : new ArrayList<>();
        actualizarTablaAyudantes(resultado);
    }

    private void buscarAyudantes() {
        String criterio = txtBuscar.getTextReal().trim();
        if (criterio.length() >= 3) {
            aplicarFiltros();
        }
    }

    private void cambiarPagina(int direccion) {
        paginaActual += direccion;
        if (paginaActual < 1) paginaActual = 1;
        actualizarTabla();
    }

    private void actualizarTabla() {
        modeloAyudantes.setRowCount(0);

        if (ayudantesFiltrados.isEmpty()) {
            return;
        }

        int inicio = (paginaActual - 1) * elementosPorPagina;
        int fin = Math.min(inicio + elementosPorPagina, ayudantesFiltrados.size());

        for (int i = inicio; i < fin; i++) {
            Ayudante a = ayudantesFiltrados.get(i);
            Object[] fila = {
                a.getNombres() != null ? a.getNombres().substring(0, Math.min(3, a.getNombres().length())) : "N/A",
                a.getNombresCompletos(),
                a.getCarrera() != null ? a.getCarrera() : "N/A",
                a.getNivel(),
                a.getProyectoAsignado() != null ? a.getProyectoAsignado().getCodigoProyecto() : "N/A",
                "Activo"
            };
            modeloAyudantes.addRow(fila);
        }

        int totalPaginas = (int) Math.ceil((double) ayudantesFiltrados.size() / elementosPorPagina);
        lblPagina.setText("Página " + paginaActual + "/" + (totalPaginas > 0 ? totalPaginas : 1));
        lblEstadisticas.setText(String.format("Mostrando %d de %d ayudantes", 
            Math.min(elementosPorPagina, ayudantesFiltrados.size()), 
            ayudantesFiltrados.size()));
    }

    private void enviarMensajeDirector() {
        String codigoProyecto = (String) cboProyecto.getSelectedItem();
        if (codigoProyecto == null || "Todos".equalsIgnoreCase(codigoProyecto)) {
            ToastMessage.mostrar(this, "Seleccione un proyecto", ToastMessage.TipoToast.ADVERTENCIA);
            return;
        }
        ProyectoInvestigacion proyecto = controlador != null ? controlador.obtenerProyectoPorCodigo(codigoProyecto) : null;
        if (proyecto == null) {
            ToastMessage.mostrar(this, "Proyecto no válido", ToastMessage.TipoToast.ERROR);
            return;
        }
        String mensaje = JOptionPane.showInputDialog(this, "Mensaje para el director:", "Enviar Mensaje", JOptionPane.PLAIN_MESSAGE);
        if (mensaje != null && !mensaje.trim().isEmpty()) {
            boolean ok = controlador.enviarMensajeADirector(proyecto, mensaje.trim());
            ToastMessage.mostrar(this, ok ? "Mensaje enviado" : "No se pudo enviar", ok ? ToastMessage.TipoToast.EXITO : ToastMessage.TipoToast.ERROR);
        }
    }

    private void generarReporte() {
        if (controlador == null) return;
        String tipo = (String) cboTipoReporte.getSelectedItem();
        Reporte reporte = null;
        if ("General de Ayudantes".equalsIgnoreCase(tipo)) {
            reporte = controlador.generarReporte("GENERAL");
        } else if ("Por Proyecto".equalsIgnoreCase(tipo)) {
            String codigoProyecto = (String) cboProyecto.getSelectedItem();
            ProyectoInvestigacion proyecto = controlador.obtenerProyectoPorCodigo(codigoProyecto);
            if (proyecto != null) {
                reporte = controlador.generarReportePorProyecto(proyecto);
            }
        } else if ("Por Carrera".equalsIgnoreCase(tipo)) {
            String carrera = (String) cboCarrera.getSelectedItem();
            reporte = controlador.generarReportePorCarrera(carrera);
        } else if ("Por Nivel".equalsIgnoreCase(tipo)) {
            Integer nivel = null; try { nivel = Integer.valueOf((String) cboNivel.getSelectedItem()); } catch (Exception ignored) {}
            if (nivel != null) reporte = controlador.generarReportePorNivel(nivel);
        }
        if (reporte != null) {
            areaReporte.setText(reporte.mostrarResumen());
            ToastMessage.mostrar(this, "Reporte generado", ToastMessage.TipoToast.EXITO);
        } else {
            ToastMessage.mostrar(this, "No se pudo generar el reporte", ToastMessage.TipoToast.ERROR);
        }
    }

    // Métodos de actualización
    public void actualizarTablaProyectos(List<ProyectoInvestigacion> proyectos) {
        DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
        modelo.addElement("Todos");
        for (ProyectoInvestigacion p : proyectos) {
            modelo.addElement(p.getCodigoProyecto());
        }
        cboProyecto.setModel(modelo);
    }

    public void actualizarTablaAyudantes(List<Ayudante> ayudantes) {
        ayudantesFiltrados = new ArrayList<>(ayudantes);
        paginaActual = 1;
        actualizarTabla();
    }

    public void mostrarNotificaciones(List<Notificacion> notificaciones) {
        panelListaNotificaciones.removeAll();

        for (Notificacion notif : notificaciones) {
            JPanel cardNotif = crearCardNotificacion(notif);
            panelListaNotificaciones.add(cardNotif);
        }

        panelListaNotificaciones.add(Box.createVerticalGlue());
        panelListaNotificaciones.revalidate();
        panelListaNotificaciones.repaint();
    }

    private JPanel crearCardNotificacion(Notificacion notif) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBackground(notif.isLeida() ? COLOR_FONDO_TARJETA : new Color(230, 240, 255));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDE),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(280, 90));
        card.setPreferredSize(new Dimension(280, 90));

        // Icono
        String icono = notif.isLeida() ? "✓" : "🔵";
        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Arial", Font.BOLD, 16));

        // Contenido
        JPanel panelContenido = new JPanel(new BorderLayout());
        panelContenido.setOpaque(false);

        JLabel lblMensaje = new JLabel(notif.getMensaje());
        lblMensaje.setFont(new Font("Arial", notif.isLeida() ? Font.PLAIN : Font.BOLD, 11));
        lblMensaje.setForeground(COLOR_TEXTO_PRINCIPAL);

        JLabel lblFecha = new JLabel(formatearFecha(notif.getFecha()));
        lblFecha.setFont(new Font("Arial", Font.PLAIN, 9));
        lblFecha.setForeground(COLOR_TEXTO_SECUNDARIO);

        panelContenido.add(lblMensaje, BorderLayout.NORTH);
        panelContenido.add(lblFecha, BorderLayout.SOUTH);

        card.add(lblIcono, BorderLayout.WEST);
        card.add(panelContenido, BorderLayout.CENTER);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return card;
    }

    private String formatearFecha(Date fecha) {
        if (fecha == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");
        return sdf.format(fecha);
    }

    // Métodos de visibilidad
    public void mostrar() {
        setVisible(true);
    }

    public void ocultar() {
        setVisible(false);
    }

    // Getters y Setters
    public JefaDepartamento getJefa() {
        return jefa;
    }

    public void setJefa(JefaDepartamento jefa) {
        this.jefa = jefa;
    }
}
