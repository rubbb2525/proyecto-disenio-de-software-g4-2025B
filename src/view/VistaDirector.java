package view;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import controller.ControladorDirector;
import model.Ayudante;
import model.Proyectos;
import view.componentes.StyledButton;
import view.componentes.RoundedBorder;
import view.componentes.AdvancedTableModel;
import view.componentes.ToastMessage;
import view.componentes.PanelEstadistica;
import view.componentes.ConstantesVisuales;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Date;
import model.AsistenteInvestigacion;
import model.TecnicoInvestigacion;

/**
 * Ventana principal del Director - VERSIÓN MEJORADA
 * Sin emojis ni iconos, solo tipografía y colores
 */
public class VistaDirector extends JFrame {
    private static final Color COLOR_FONDO = ConstantesVisuales.COLOR_FONDO_PRINCIPAL;
    private static final Color COLOR_TARJETA = ConstantesVisuales.COLOR_TARJETA;
    private static final Color COLOR_PRIMARIO = ConstantesVisuales.COLOR_PRIMARIO;
    private static final Color COLOR_TEXTO = ConstantesVisuales.COLOR_TEXTO_PRINCIPAL;
    private static final Color COLOR_BORDE = ConstantesVisuales.COLOR_BORDE;

    private JTable tablaAyudantes;
    private StyledButton btnRegistrar;
    private StyledButton btnDarBaja;
    private StyledButton btnCrearProyecto;
    private StyledButton btnRefrescar;
    private JLabel lblProyectoNombre;
    private JLabel lblProyectoTipo;
    private JLabel lblProyectoFechas;
    private JLabel lblCuposInfo;
    private ControladorDirector controlador;
    private AdvancedTableModel modeloTabla;
    private AdvancedTableModel modeloAsistentes;
    private AdvancedTableModel modeloTecnicos;
    private JTable tablaAsistentes;
    private JTable tablaTecnicos;
    private JTextField campoBusqueda;
    private javax.swing.Timer timerRefresh;
    private PanelEstadistica panelTotalAyudantes;
    private PanelEstadistica panelTotalAsistentes;
    private PanelEstadistica panelTotalTecnicos;
    private PanelEstadistica panelCuposDisponibles;

    public VistaDirector(ControladorDirector controlador) {
        this.controlador = controlador;
        setTitle("Sistema de Gestión de Ayudantes - Director");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 850);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 700));

        inicializarComponentes();
        cargarDatos();
        configurarEventos();
        
        timerRefresh = new javax.swing.Timer(5000, e -> cargarDatos());
        timerRefresh.start();
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 0));
        panelPrincipal.setBackground(COLOR_FONDO);

        // Header mejorado
        JPanel header = crearHeaderMejorado();

        // Contenido principal con scroll
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(COLOR_FONDO);
        contenido.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // Sección 1: Estadísticas (4 cards horizontales)
        JPanel seccionEstadisticas = crearSeccionEstadisticas();
        contenido.add(seccionEstadisticas);
        contenido.add(Box.createVerticalStrut(24));

        // Sección 2: Información del Proyecto con Botones (layout horizontal)
        JPanel seccionProyectoConBotones = crearSeccionProyectoConBotones();
        contenido.add(seccionProyectoConBotones);
        contenido.add(Box.createVerticalStrut(24));

        // Sección 3: Tabla con pestañas
        JPanel seccionTablas = crearSeccionTablas();
        contenido.add(seccionTablas);

        JScrollPane scrollPane = new JScrollPane(contenido);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panelPrincipal.add(header, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
    }

    private JPanel crearHeaderMejorado() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                     RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, ConstantesVisuales.COLOR_PRIMARIO,
                    0, getHeight(), ConstantesVisuales.COLOR_PRIMARIO_HOVER
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));

        // Panel izquierdo
        JPanel panelIzq = new JPanel();
        panelIzq.setOpaque(false);
        panelIzq.setLayout(new BoxLayout(panelIzq, BoxLayout.Y_AXIS));
        
        JLabel lblTitulo = new JLabel("Panel del Director");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        
        JLabel lblSubtitulo = new JLabel("Gestión de Proyectos y Colaboradores");
        lblSubtitulo.setForeground(new Color(255, 255, 255, 180));
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        panelIzq.add(lblTitulo);
        panelIzq.add(lblSubtitulo);

        // Panel derecho con botón
        JPanel panelDer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelDer.setOpaque(false);
        
        btnRefrescar = new StyledButton("Actualizar Datos", StyledButton.TipoBoton.SECUNDARIO);
        btnRefrescar.setPreferredSize(new Dimension(140, 40));
        btnRefrescar.setToolTipText("Refrescar datos (F5)");
        panelDer.add(btnRefrescar);

        header.add(panelIzq, BorderLayout.WEST);
        header.add(panelDer, BorderLayout.EAST);
        
        return header;
    }

    private JPanel crearSeccionEstadisticas() {
        JPanel seccion = new JPanel();
        seccion.setLayout(new BoxLayout(seccion, BoxLayout.Y_AXIS));
        seccion.setOpaque(false);
        
        // Título de sección
        JLabel lblTitulo = new JLabel("RESUMEN GENERAL");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(100, 100, 100));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        seccion.add(lblTitulo);
        seccion.add(Box.createVerticalStrut(12));
        
        // Panel con las 4 estadísticas
        JPanel panelStats = new JPanel(new GridLayout(1, 4, 16, 0));
        panelStats.setOpaque(false);
        panelStats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        panelTotalAyudantes = new PanelEstadistica("Ayudantes Activos", "0", 
            new Color(52, 152, 219), "");
        panelTotalAsistentes = new PanelEstadistica("Asistentes Activos", "0", 
            new Color(46, 204, 113), "");
        panelTotalTecnicos = new PanelEstadistica("Técnicos Activos", "0", 
            new Color(241, 196, 15), "");
        panelCuposDisponibles = new PanelEstadistica("Cupos Disponibles", "0", 
            new Color(155, 89, 182), "");
        
        panelStats.add(panelTotalAyudantes);
        panelStats.add(panelTotalAsistentes);
        panelStats.add(panelTotalTecnicos);
        panelStats.add(panelCuposDisponibles);
        
        seccion.add(panelStats);
        
        return seccion;
    }

    private JPanel crearSeccionProyecto() {
        JPanel seccion = new JPanel();
        seccion.setLayout(new BoxLayout(seccion, BoxLayout.Y_AXIS));
        seccion.setOpaque(false);
        
        // Título de sección
        JLabel lblTitulo = new JLabel("PROYECTO ASIGNADO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(100, 100, 100));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        seccion.add(lblTitulo);
        seccion.add(Box.createVerticalStrut(12));
        
        // Card del proyecto
        JPanel card = new JPanel();
        card.setBackground(COLOR_TARJETA);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(20, 24, 20, 24)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        // Nombre del proyecto (destacado)
        lblProyectoNombre = new JLabel("Cargando información del proyecto...");
        lblProyectoNombre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblProyectoNombre.setForeground(COLOR_PRIMARIO);
        lblProyectoNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Tipo de proyecto
        lblProyectoTipo = new JLabel("Tipo: --");
        lblProyectoTipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblProyectoTipo.setForeground(new Color(120, 120, 120));
        lblProyectoTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Fechas
        lblProyectoFechas = new JLabel("Periodo: --");
        lblProyectoFechas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblProyectoFechas.setForeground(new Color(120, 120, 120));
        lblProyectoFechas.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Separador visual
        JSeparator separador = new JSeparator();
        separador.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separador.setForeground(COLOR_BORDE);
        
        // Información de cupos
        lblCuposInfo = new JLabel("Cupos: --");
        lblCuposInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCuposInfo.setForeground(COLOR_TEXTO);
        lblCuposInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(lblProyectoNombre);
        card.add(Box.createVerticalStrut(8));
        card.add(lblProyectoTipo);
        card.add(lblProyectoFechas);
        card.add(Box.createVerticalStrut(12));
        card.add(separador);
        card.add(Box.createVerticalStrut(12));
        card.add(lblCuposInfo);
        
        seccion.add(card);
        
        return seccion;
    }

    private JPanel crearSeccionProyectoConBotones() {
        JPanel seccion = new JPanel();
        seccion.setLayout(new BoxLayout(seccion, BoxLayout.Y_AXIS));
        seccion.setOpaque(false);
        
        // Título de sección
        JLabel lblTitulo = new JLabel("PROYECTO ASIGNADO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(100, 100, 100));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        seccion.add(lblTitulo);
        seccion.add(Box.createVerticalStrut(12));
        
        // Panel contenedor horizontal: botones a la izquierda, proyecto a la derecha
        JPanel contenedorHorizontal = new JPanel();
        contenedorHorizontal.setLayout(new BorderLayout(20, 0));
        contenedorHorizontal.setOpaque(false);
        contenedorHorizontal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        contenedorHorizontal.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Panel de botones a la izquierda (estrecho, fuera de la vista)
        JPanel panelBotonesIzq = new JPanel();
        panelBotonesIzq.setLayout(new BoxLayout(panelBotonesIzq, BoxLayout.Y_AXIS));
        panelBotonesIzq.setOpaque(false);
        panelBotonesIzq.setPreferredSize(new Dimension(160, 180));
        
        btnRegistrar = new StyledButton("Nueva\nContratación", StyledButton.TipoBoton.PRIMARIO);
        btnRegistrar.setPreferredSize(new Dimension(150, 50));
        btnRegistrar.setMaximumSize(new Dimension(150, 50));
        btnRegistrar.setToolTipText("Realizar nueva contratación (Ctrl+N)");
        
        btnDarBaja = new StyledButton("Dar de\nBaja", StyledButton.TipoBoton.PELIGRO);
        btnDarBaja.setPreferredSize(new Dimension(150, 50));
        btnDarBaja.setMaximumSize(new Dimension(150, 50));
        btnDarBaja.setToolTipText("Dar de baja al colaborador seleccionado (Supr)");
        
        btnCrearProyecto = new StyledButton("Crear\nProyecto", StyledButton.TipoBoton.EXITO);
        btnCrearProyecto.setPreferredSize(new Dimension(150, 50));
        btnCrearProyecto.setMaximumSize(new Dimension(150, 50));
        btnCrearProyecto.setToolTipText("Crear nuevo proyecto de investigación (Ctrl+P)");
        
        panelBotonesIzq.add(btnRegistrar);
        panelBotonesIzq.add(Box.createVerticalStrut(8));
        panelBotonesIzq.add(btnDarBaja);
        panelBotonesIzq.add(Box.createVerticalStrut(8));
        panelBotonesIzq.add(btnCrearProyecto);
        panelBotonesIzq.add(Box.createVerticalGlue());
        
        // Card del proyecto a la derecha (información)
        JPanel card = new JPanel();
        card.setBackground(COLOR_TARJETA);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(20, 24, 20, 24)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Nombre del proyecto (destacado)
        lblProyectoNombre = new JLabel("Cargando información del proyecto...");
        lblProyectoNombre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblProyectoNombre.setForeground(COLOR_PRIMARIO);
        lblProyectoNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Tipo de proyecto
        lblProyectoTipo = new JLabel("Tipo: --");
        lblProyectoTipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblProyectoTipo.setForeground(new Color(120, 120, 120));
        lblProyectoTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Fechas
        lblProyectoFechas = new JLabel("Periodo: --");
        lblProyectoFechas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblProyectoFechas.setForeground(new Color(120, 120, 120));
        lblProyectoFechas.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Separador visual
        JSeparator separador = new JSeparator();
        separador.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separador.setForeground(COLOR_BORDE);
        
        // Información de cupos
        lblCuposInfo = new JLabel("Cupos: --");
        lblCuposInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCuposInfo.setForeground(COLOR_TEXTO);
        lblCuposInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(lblProyectoNombre);
        card.add(Box.createVerticalStrut(8));
        card.add(lblProyectoTipo);
        card.add(lblProyectoFechas);
        card.add(Box.createVerticalStrut(12));
        card.add(separador);
        card.add(Box.createVerticalStrut(12));
        card.add(lblCuposInfo);
        card.add(Box.createVerticalGlue());
        
        // Agregar botones a la izquierda y card a la derecha
        contenedorHorizontal.add(panelBotonesIzq, BorderLayout.WEST);
        contenedorHorizontal.add(card, BorderLayout.CENTER);
        
        seccion.add(contenedorHorizontal);
        
        return seccion;
    }

    private JPanel crearSeccionTablas() {
        JPanel seccion = new JPanel();
        seccion.setLayout(new BoxLayout(seccion, BoxLayout.Y_AXIS));
        seccion.setOpaque(false);
        
        // Título de sección
        JLabel lblTitulo = new JLabel("COLABORADORES DEL PROYECTO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(100, 100, 100));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        seccion.add(lblTitulo);
        seccion.add(Box.createVerticalStrut(12));
        
        // Panel contenedor de búsqueda y tabs
        JPanel panelContenedor = new JPanel(new BorderLayout(0, 12));
        panelContenedor.setOpaque(false);
        panelContenedor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 450));
        
        // Barra de búsqueda mejorada
        JPanel panelBusqueda = crearBarraBusqueda();
        panelContenedor.add(panelBusqueda, BorderLayout.NORTH);
        
        // Tabs con las tablas
        JTabbedPane tabs = crearTabsTablas();
        panelContenedor.add(tabs, BorderLayout.CENTER);
        
        seccion.add(panelContenedor);
        
        return seccion;
    }

    private JPanel crearBarraBusqueda() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setBackground(COLOR_TARJETA);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        
        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblBuscar.setForeground(COLOR_TEXTO);
        
        campoBusqueda = new JTextField();
        campoBusqueda.setPreferredSize(new Dimension(300, 35));
        campoBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campoBusqueda.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        campoBusqueda.setToolTipText("Buscar por nombre, código o carrera");
        
        // Efecto focus
        campoBusqueda.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                campoBusqueda.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_PRIMARIO, 2),
                    BorderFactory.createEmptyBorder(5, 11, 5, 11)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                campoBusqueda.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_BORDE, 1),
                    BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            }
        });
        
        JLabel lblHint = new JLabel("Escriba para filtrar resultados en tiempo real");
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHint.setForeground(new Color(150, 150, 150));
        
        JPanel panelBusquedaCompleto = new JPanel(new BorderLayout(8, 0));
        panelBusquedaCompleto.setOpaque(false);
        panelBusquedaCompleto.add(campoBusqueda, BorderLayout.CENTER);
        panelBusquedaCompleto.add(lblHint, BorderLayout.EAST);
        
        panel.add(lblBuscar, BorderLayout.WEST);
        panel.add(panelBusquedaCompleto, BorderLayout.CENTER);
        
        return panel;
    }

    private JTabbedPane crearTabsTablas() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBackground(COLOR_TARJETA);
        
        // Tab Ayudantes
        String[] colsAyudantes = {"Código", "Nombre Completo", "Carrera", "Nivel", "IRA", "Meses"};
        modeloTabla = new AdvancedTableModel(colsAyudantes);
        tablaAyudantes = crearTablaEstilizada(modeloTabla);
        JScrollPane scrollAyudantes = new JScrollPane(tablaAyudantes);
        scrollAyudantes.setBorder(BorderFactory.createEmptyBorder());
        tabs.addTab("Ayudantes de Investigación", scrollAyudantes);
        
        // Tab Asistentes
        String[] colsAsistentes = {"Código", "Nombre Completo", "Carrera", "Nivel", "IRA", "Meses"};
        modeloAsistentes = new AdvancedTableModel(colsAsistentes);
        tablaAsistentes = crearTablaEstilizada(modeloAsistentes);
        JScrollPane scrollAsistentes = new JScrollPane(tablaAsistentes);
        scrollAsistentes.setBorder(BorderFactory.createEmptyBorder());
        tabs.addTab("Asistentes de Investigación", scrollAsistentes);
        
        // Tab Técnicos
        String[] colsTecnicos = {"ID Técnico", "Nombre Completo", "Meses Contratados"};
        modeloTecnicos = new AdvancedTableModel(colsTecnicos);
        tablaTecnicos = crearTablaEstilizada(modeloTecnicos);
        JScrollPane scrollTecnicos = new JScrollPane(tablaTecnicos);
        scrollTecnicos.setBorder(BorderFactory.createEmptyBorder());
        tabs.addTab("Técnicos de Investigación", scrollTecnicos);
        
        return tabs;
    }

    private JTable crearTablaEstilizada(AdvancedTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(40);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setForeground(COLOR_TEXTO);
        tabla.setSelectionBackground(new Color(52, 152, 219, 30));
        tabla.setSelectionForeground(COLOR_TEXTO);
        tabla.setGridColor(new Color(220, 220, 220));
        tabla.setShowVerticalLines(false);
        tabla.setShowHorizontalLines(true);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        
        // Header estilizado
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(COLOR_PRIMARIO);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 45));
        header.setBorder(BorderFactory.createEmptyBorder());
        
        // Renderer para filas alternadas
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, 
                    isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(248, 250, 252));
                    }
                }
                
                // Padding en celdas
                if (c instanceof JLabel) {
                    ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                }
                
                return c;
            }
        });
        
        return tabla;
    }

    private JPanel crearSeccionAcciones() {
        JPanel seccion = new JPanel();
        seccion.setLayout(new BoxLayout(seccion, BoxLayout.Y_AXIS));
        seccion.setOpaque(false);
        
        // Título de sección
        JLabel lblTitulo = new JLabel("ACCIONES DISPONIBLES");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(100, 100, 100));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        seccion.add(lblTitulo);
        seccion.add(Box.createVerticalStrut(12));
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panelBotones.setOpaque(false);
        panelBotones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        btnRegistrar = new StyledButton("Nueva Contratación", StyledButton.TipoBoton.PRIMARIO);
        btnRegistrar.setPreferredSize(new Dimension(180, 45));
        btnRegistrar.setToolTipText("Realizar nueva contratación (Ctrl+N)");
        
        btnDarBaja = new StyledButton("Dar de Baja", StyledButton.TipoBoton.PELIGRO);
        btnDarBaja.setPreferredSize(new Dimension(140, 45));
        btnDarBaja.setToolTipText("Dar de baja al colaborador seleccionado (Supr)");
        
        btnCrearProyecto = new StyledButton("Crear Proyecto", StyledButton.TipoBoton.EXITO);
        btnCrearProyecto.setPreferredSize(new Dimension(160, 45));
        btnCrearProyecto.setToolTipText("Crear nuevo proyecto de investigación (Ctrl+P)");
        
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnDarBaja);
        panelBotones.add(btnCrearProyecto);
        
        seccion.add(panelBotones);
        
        return seccion;
    }

    private void configurarEventos() {
        configurarAtajosTeclado();
        
        btnRefrescar.addActionListener(e -> {
            ToastMessage.mostrar(this, "Actualizando datos del sistema...", ToastMessage.TipoToast.INFO);
            cargarDatos();
        });

        campoBusqueda.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarBusqueda(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarBusqueda(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { }
        });

        tablaAyudantes.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int columna = tablaAyudantes.columnAtPoint(e.getPoint());
                if (columna >= 0) {
                    modeloTabla.ordenarPorColumna(columna);
                }
            }
        });

        btnDarBaja.addActionListener(e -> darDeBajaAyudante());

        btnRegistrar.addActionListener(e -> {
            DialogoSeleccionContratacion dialogo = new DialogoSeleccionContratacion(this, controlador);
            dialogo.setVisible(true);
            if (dialogo.seGuardo()) {
                ToastMessage.mostrar(this, "Contratación realizada exitosamente", ToastMessage.TipoToast.EXITO);
                cargarDatos();
            }
        });

        btnCrearProyecto.addActionListener(e -> {
            if (!controlador.puedeCrearProyecto()) {
                ToastMessage.mostrar(this, "Ya tiene un proyecto activo asignado", ToastMessage.TipoToast.ADVERTENCIA);
                return;
            }
            DialogoFormularioProyecto dialogo = new DialogoFormularioProyecto(this, controlador);
            dialogo.setVisible(true);
            if (dialogo.seGuardo()) {
                ToastMessage.mostrar(this, "Proyecto creado exitosamente", ToastMessage.TipoToast.EXITO);
                cargarDatos();
            }
        });
    }
    
    private void configurarAtajosTeclado() {
        getRootPane().registerKeyboardAction(
            e -> btnRefrescar.doClick(),
            KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        
        getRootPane().registerKeyboardAction(
            e -> btnRegistrar.doClick(),
            KeyStroke.getKeyStroke(KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        
        getRootPane().registerKeyboardAction(
            e -> darDeBajaAyudante(),
            KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        getRootPane().registerKeyboardAction(
            e -> btnCrearProyecto.doClick(),
            KeyStroke.getKeyStroke(KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_DOWN_MASK),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }
    
    private void darDeBajaAyudante() {
        Ayudante seleccionado = getAyudanteSeleccionado();
        if (seleccionado == null) {
            ToastMessage.mostrar(this, "Debe seleccionar un ayudante activo de la tabla", ToastMessage.TipoToast.ADVERTENCIA);
            return;
        }
        
        String[] motivos = {"FIN_CONTRATO", "RETIRO_VOLUNTARIO", "FUERZA_MAYOR"};
        String motivoSeleccionado = (String) JOptionPane.showInputDialog(
            this,
            "Seleccione el motivo de baja para:\n" + seleccionado.getNombresCompletos(),
            "Motivo de Baja",
            JOptionPane.QUESTION_MESSAGE,
            null,
            motivos,
            motivos[0]
        );
        
        if (motivoSeleccionado == null) return;
        
        DialogoConfirmacion dialogo = new DialogoConfirmacion(this, "Confirmar Baja", 
            "¿Está seguro de dar de baja a " + seleccionado.getNombresCompletos() + "?\nMotivo: " + motivoSeleccionado);
        dialogo.setVisible(true);
        if (!dialogo.esConfirmado()) return;
            
        var res = controlador.darDeBajaAyudante(seleccionado.getCodigoUnico(), motivoSeleccionado, new Date());
        if (res.esExitoso()) {
            ToastMessage.mostrar(this, res.getMensaje(), ToastMessage.TipoToast.EXITO);
            cargarDatos();
        } else {
            ToastMessage.mostrar(this, res.getMensaje(), ToastMessage.TipoToast.ERROR);
        }
    }

    private void actualizarBusqueda() {
        String textoBusqueda = campoBusqueda.getText().trim();
        if (textoBusqueda.isEmpty()) {
            cargarDatos();
        } else {
            modeloTabla.buscarEnColumna(textoBusqueda, 1);
        }
    }

    private void cargarDatos() {
        Proyectos proyecto = controlador.getProyecto();
        
        if (proyecto != null) {
            // Actualizar información del proyecto
            lblProyectoNombre.setText(proyecto.getNombreProyecto());
            lblProyectoTipo.setText("Tipo: " + (proyecto.getTipoProyecto() != null ? proyecto.getTipoProyecto() : "No especificado"));
            
            String fechaInicio = proyecto.getFechaInicio() != null 
                ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(proyecto.getFechaInicio()) 
                : "N/A";
            String fechaFin = proyecto.getFechaFin() != null 
                ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(proyecto.getFechaFin()) 
                : "N/A";
            lblProyectoFechas.setText("Periodo: " + fechaInicio + " - " + fechaFin);
            
            // Actualizar tablas
            actualizarTablaAyudantes();
            actualizarTablaAsistentes();
            actualizarTablaTecnicos();
            
            // Calcular cupos
            int totalAyudantes = Integer.parseInt(panelTotalAyudantes.obtenerValor());
            int cuposDisponibles = proyecto.getAyudantesPlanificados() - totalAyudantes;
            lblCuposInfo.setText("Cupos utilizados: " + totalAyudantes + " de " + proyecto.getAyudantesPlanificados() 
                + " (Disponibles: " + cuposDisponibles + ")");
            panelCuposDisponibles.actualizarValor(String.valueOf(cuposDisponibles));
        } else {
            lblProyectoNombre.setText("Sin proyecto asignado");
            lblProyectoTipo.setText("Tipo: --");
            lblProyectoFechas.setText("Periodo: --");
            lblCuposInfo.setText("Cupos: No disponible");
            panelTotalAyudantes.actualizarValor("0");
            panelTotalAsistentes.actualizarValor("0");
            panelTotalTecnicos.actualizarValor("0");
            panelCuposDisponibles.actualizarValor("0");
        }
    }

    private void actualizarTablaAyudantes() {
        modeloTabla.limpiar();
        java.util.List<Ayudante> ayudantes = controlador.consultarAyudantesDelProyecto();
        if (ayudantes == null) ayudantes = new java.util.ArrayList<>();
        
        int totalActivos = 0;
        java.util.List<Object[]> filas = new java.util.ArrayList<>();
        
        for (Ayudante a : ayudantes) {
            if (a.esActivo()) {
                totalActivos++;
                Object[] fila = {
                    a.getCodigoUnico(),
                    a.getNombresCompletos(),
                    a.getCarrera(),
                    String.valueOf(a.getNivel()),
                    String.format("%.2f", a.getIRA()),
                    String.valueOf(a.getMesesContratados())
                };
                filas.add(fila);
            }
        }
        
        panelTotalAyudantes.actualizarValor(String.valueOf(totalActivos));
        modeloTabla.establecerDatos(filas);
    }

    private void actualizarTablaAsistentes() {
        modeloAsistentes.limpiar();
        java.util.List<AsistenteInvestigacion> asistentes = controlador.obtenerAsistentesProyecto();
        if (asistentes == null) asistentes = new java.util.ArrayList<>();
        
        int totalActivos = 0;
        java.util.List<Object[]> filas = new java.util.ArrayList<>();
        
        for (AsistenteInvestigacion a : asistentes) {
            if (a.esActivo()) {
                totalActivos++;
                Object[] fila = {
                    a.getCodigoUnico(),
                    a.getNombresCompletos(),
                    a.getCarrera(),
                    String.valueOf(a.getNivel()),
                    String.format("%.2f", a.getIRA()),
                    String.valueOf(a.getMesesContratados())
                };
                filas.add(fila);
            }
        }
        
        panelTotalAsistentes.actualizarValor(String.valueOf(totalActivos));
        modeloAsistentes.establecerDatos(filas);
    }

    private void actualizarTablaTecnicos() {
        modeloTecnicos.limpiar();
        java.util.List<TecnicoInvestigacion> tecnicos = controlador.obtenerTecnicosProyecto();
        if (tecnicos == null) tecnicos = new java.util.ArrayList<>();
        
        int totalActivos = 0;
        java.util.List<Object[]> filas = new java.util.ArrayList<>();
        
        for (TecnicoInvestigacion t : tecnicos) {
            if (t.esActivo()) {
                totalActivos++;
                Object[] fila = {
                    t.getIdTecnico(),
                    t.getNombresCompletos(),
                    String.valueOf(t.getMesesContratados())
                };
                filas.add(fila);
            }
        }
        
        panelTotalTecnicos.actualizarValor(String.valueOf(totalActivos));
        modeloTecnicos.establecerDatos(filas);
    }

    public void mostrarVentana() {
        setVisible(true);
    }

    public void cerrar() {
        if (timerRefresh != null) {
            timerRefresh.stop();
        }
        setVisible(false);
    }

    public Ayudante getAyudanteSeleccionado() {
        int fila = tablaAyudantes.getSelectedRow();
        if (fila >= 0) {
            Object[] filaActual = modeloTabla.obtenerFila(fila);
            if (filaActual != null) {
                String codigo = (String) filaActual[0];
                return controlador.consultarAyudantesDelProyecto().stream()
                    .filter(a -> a.getCodigoUnico().equals(codigo))
                    .findFirst()
                    .orElse(null);
            }
        }
        return null;
    }

    public void mostrarMensajeExito(String mensaje) {
        ToastMessage.mostrar(this, mensaje, ToastMessage.TipoToast.EXITO);
    }

    public void mostrarMensajeError(String mensaje) {
        ToastMessage.mostrar(this, mensaje, ToastMessage.TipoToast.ERROR);
    }

    public boolean confirmarAccion(String mensaje) {
        int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Confirmación", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return opcion == JOptionPane.YES_OPTION;
    }

    public void setControlador(ControladorDirector controlador) {
        this.controlador = controlador;
    }
}