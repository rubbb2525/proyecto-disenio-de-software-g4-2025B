package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import controller.ControladorJefaDepartamento;
import model.Ayudante;
import model.ProyectoInvestigacion;
import model.Notificacion;
import model.Reporte;
import view.componentes.AdvancedTableModel;
import java.util.List;
import java.util.Map;
import java.awt.*;
import view.componentes.StyledButton;
import view.componentes.IconManager;
import view.componentes.RoundedBorder;
import view.componentes.BadgeButton;
import view.componentes.ToastMessage;
import view.componentes.PanelEstadistica;
import java.awt.event.KeyEvent;

/**
 * Ventana principal de la Jefa de Departamento
 */
public class VistaJefaDepartamento extends JFrame {
    private static final Color COLOR_FONDO = new Color(245, 247, 250);
    private static final Color COLOR_TARJETA = Color.WHITE;
    private static final Color COLOR_PRIMARIO = new Color(0, 61, 165);
    private static final Color COLOR_BORDE = new Color(225, 232, 237);
    private static final Color COLOR_TEXTO = new Color(44, 62, 80);

    private JTable tablaAyudantes;
    private JTable tablaProyectos;
    private StyledButton btnFiltrar;
    private StyledButton btnGenerarReporte;
    private BadgeButton btnVerNotificaciones;
    private JComboBox<Object> comboProyectos;
    private JComboBox<String> comboCarreras;
    private JComboBox<Integer> comboNivel;
    private JComboBox<String> comboEstado;
    private ControladorJefaDepartamento controlador;
    private AdvancedTableModel modeloTabla;
    private AdvancedTableModel modeloProyectos;
    private JTextField campoBusqueda;
    private int ultimasNotificacionesCount = 0;
    private javax.swing.Timer timerNotificaciones;
    private PanelEstadistica panelTotalAyudantes;
    private PanelEstadistica panelProyectosActivos;
    private PanelEstadistica panelCarrerasRegistradas;

    // Item para combo de proyectos: muestra nombre, guarda código
    private static class ProyectoItem {
        private final String codigo;
        private final String nombre;
        ProyectoItem(String codigo, String nombre) { this.codigo = codigo; this.nombre = nombre; }
        @Override public String toString() { return nombre; }
        public String getCodigo() { return codigo; }
        public String getNombre() { return nombre; }
    }

    public VistaJefaDepartamento(ControladorJefaDepartamento controlador) {
        this.controlador = controlador;
        setTitle("Sistema Gestión Ayudantes - Jefa Departamento");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 720);
        setLocationRelativeTo(null);

        // Sincronizar notificaciones desde BD vía controlador
        controlador.sincronizarNotificacionesConBD();
        
        inicializarComponentes();
        cargarDatos();
        configurarEventos();
        
        // Iniciar timer para actualizar notificaciones cada 2 segundos
        iniciarTimerNotificaciones();
    }
    
    private void iniciarTimerNotificaciones() {
        timerNotificaciones = new javax.swing.Timer(2000, e -> {
            int actualCount = controlador.obtenerCantidadNotificacionesNoLeidas();
            if (actualCount != ultimasNotificacionesCount) {
                ultimasNotificacionesCount = actualCount;
                actualizarContadorNotificaciones(actualCount);
                // Refrescar tabla de ayudantes por si hubo cambios
                actualizarTabla();
            }
        });
        timerNotificaciones.start();
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_PRIMARIO);
        header.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        JLabel lblTitulo = new JLabel(" Jefa de Departamento");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setIcon(IconManager.getInstance().getIcon("users.svg", 18));
        header.add(lblTitulo, BorderLayout.WEST);

        btnVerNotificaciones = new BadgeButton("Notificaciones", IconManager.getInstance().getIcon("bell.svg", 16));
        btnVerNotificaciones.setForeground(Color.WHITE);
        btnVerNotificaciones.setBackground(new Color(74, 144, 226));
        btnVerNotificaciones.setBorderPainted(false);
        btnVerNotificaciones.setFocusPainted(false);
        btnVerNotificaciones.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVerNotificaciones.setToolTipText("Ver notificaciones nuevas (Ctrl+N)");
        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        headerRight.setOpaque(false);
        headerRight.add(btnVerNotificaciones);
        header.add(headerRight, BorderLayout.EAST);

        // Filtros card (para ayudantes)
        JPanel cardFiltros = new JPanel();
        cardFiltros.setLayout(new BoxLayout(cardFiltros, BoxLayout.Y_AXIS));
        cardFiltros.setBackground(COLOR_TARJETA);
        cardFiltros.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        // Paneles de estadísticas
        JPanel panelEstadisticas = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panelEstadisticas.setBackground(COLOR_TARJETA);
        panelEstadisticas.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        
        panelTotalAyudantes = new PanelEstadistica("Total Ayudantes", "0", new Color(52, 152, 219), "👥");
        panelProyectosActivos = new PanelEstadistica("Proyectos Activos", "0", new Color(46, 204, 113), "📊");
        panelCarrerasRegistradas = new PanelEstadistica("Carreras", "0", new Color(155, 89, 182), "🎓");
        
        panelEstadisticas.add(panelTotalAyudantes);
        panelEstadisticas.add(panelProyectosActivos);
        panelEstadisticas.add(panelCarrerasRegistradas);
        cardFiltros.add(panelEstadisticas);
        cardFiltros.add(Box.createVerticalStrut(8));

        JPanel filtrosRow = new JPanel(new GridLayout(2, 4, 10, 6));
        filtrosRow.setOpaque(false);

        comboProyectos = new JComboBox<>();
        comboCarreras = new JComboBox<>();
        comboNivel = new JComboBox<>();
        comboEstado = new JComboBox<>();
        
        // Añadir "Todos" al nivel
        comboNivel.addItem(0); // 0 representa "Todos"
        for (int i = 1; i <= 10; i++) comboNivel.addItem(i);
        
        // Añadir estados
        comboEstado.addItem("Todos");
        comboEstado.addItem("Activos");
        comboEstado.addItem("Inactivos");

        filtrosRow.add(new JLabel("Proyecto"));
        filtrosRow.add(new JLabel("Carrera"));
        filtrosRow.add(new JLabel("Nivel"));
        filtrosRow.add(new JLabel("Estado"));
        filtrosRow.add(comboProyectos);
        filtrosRow.add(comboCarreras);
        filtrosRow.add(comboNivel);
        filtrosRow.add(comboEstado);

        JPanel filtrosAccion = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filtrosAccion.setOpaque(false);
        btnFiltrar = new StyledButton("Filtrar", StyledButton.TipoBoton.PRIMARIO);
        btnFiltrar.setIcon(IconManager.getInstance().getIcon("filter.svg", 16));
        btnFiltrar.setToolTipText("Aplicar filtros seleccionados (Ctrl+F)");
        btnGenerarReporte = new StyledButton("Reporte", StyledButton.TipoBoton.EXITO);
        btnGenerarReporte.setIcon(IconManager.getInstance().getIcon("report.svg", 16));
        btnGenerarReporte.setToolTipText("Generar reporte general (Ctrl+R)");
        filtrosAccion.add(btnFiltrar);
        filtrosAccion.add(btnGenerarReporte);

        cardFiltros.add(filtrosRow);
        cardFiltros.add(Box.createVerticalStrut(8));
        cardFiltros.add(filtrosAccion);

        // Tabla de ayudantes
        String[] columnas = {"Código", "Nombres", "Carrera", "Nivel", "IRA", "Proyecto", "Horas"};
        modeloTabla = new AdvancedTableModel(columnas);
        tablaAyudantes = new JTable(modeloTabla);
        tablaAyudantes.setRowHeight(28);
        
        // Diseño alternado
        tablaAyudantes.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(240, 247, 255) : Color.WHITE);
                }
                return c;
            }
        });
        
        JTableHeader th = tablaAyudantes.getTableHeader();
        th.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Barra de búsqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout(8, 0));
        panelBusqueda.setBackground(COLOR_FONDO);
        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(new Font("Arial", Font.PLAIN, 11));
        campoBusqueda = new JTextField();
        campoBusqueda.setPreferredSize(new Dimension(250, 35));
        campoBusqueda.setFont(new Font("Arial", Font.PLAIN, 11));
        campoBusqueda.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        campoBusqueda.setToolTipText("Buscar por nombre, código o carrera");
        panelBusqueda.add(lblBuscar, BorderLayout.WEST);
        panelBusqueda.add(campoBusqueda, BorderLayout.CENTER);
        
        JScrollPane scroll = new JScrollPane(tablaAyudantes);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));

        // Panel AYUDANTES (contenido de la pestaña)
        JPanel panelAyudantes = new JPanel();
        panelAyudantes.setBackground(COLOR_FONDO);
        panelAyudantes.setLayout(new BoxLayout(panelAyudantes, BoxLayout.Y_AXIS));
        panelAyudantes.add(cardFiltros);
        panelAyudantes.add(Box.createVerticalStrut(12));
        panelAyudantes.add(panelBusqueda);
        panelAyudantes.add(Box.createVerticalStrut(8));
        panelAyudantes.add(scroll);

        // Panel PROYECTOS (nueva pestaña)
        JPanel panelProyectosTab = new JPanel();
        panelProyectosTab.setBackground(COLOR_FONDO);
        panelProyectosTab.setLayout(new BoxLayout(panelProyectosTab, BoxLayout.Y_AXIS));

        // Tabla de proyectos: columnas para planificados vs contratados activos
        String[] columnasProy = {"Código", "Proyecto", "Planificados", "Contratados Activos", "Cupos Disponibles", "Estado", "Inicio", "Fin"};
        modeloProyectos = new AdvancedTableModel(columnasProy);
        tablaProyectos = new JTable(modeloProyectos);
        tablaProyectos.setRowHeight(28);
        JTableHeader thProy = tablaProyectos.getTableHeader();
        thProy.setFont(new Font("Arial", Font.BOLD, 12));

        JScrollPane scrollProy = new JScrollPane(tablaProyectos);
        scrollProy.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));

        // Panel de resumen arriba de la tabla de proyectos
        JPanel resumenProy = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        resumenProy.setBackground(COLOR_TARJETA);
        resumenProy.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        JLabel lblResumen = new JLabel("Resumen de proyectos: planificados vs contratados activos");
        lblResumen.setFont(new Font("Arial", Font.PLAIN, 12));
        resumenProy.add(lblResumen);

        panelProyectosTab.add(resumenProy);
        panelProyectosTab.add(Box.createVerticalStrut(8));
        panelProyectosTab.add(scrollProy);

        // Contenedor con pestañas
        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("Ayudantes", IconManager.getInstance().getIcon("users.svg", 16), panelAyudantes);
        pestañas.addTab("Proyectos", IconManager.getInstance().getIcon("project.svg", 16), panelProyectosTab);

        panelPrincipal.add(header, BorderLayout.NORTH);
        panelPrincipal.add(pestañas, BorderLayout.CENTER);

        add(panelPrincipal);
    }

    private void configurarEventos() {
        // Atajos de teclado
        configurarAtajosTeclado();
        
        // Búsqueda en vivo
        campoBusqueda.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarBusqueda(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarBusqueda(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { }
        });
        
        // Ordenamiento por columna
        tablaAyudantes.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int columna = tablaAyudantes.columnAtPoint(e.getPoint());
                if (columna >= 0) {
                    modeloTabla.ordenarPorColumna(columna);
                }
            }
        });

        btnFiltrar.addActionListener(e -> {
            ToastMessage.mostrar(this, "Aplicando filtros...", ToastMessage.TipoToast.INFO);
            Map<String, Object> filtros = obtenerFiltrosSeleccionados();
            List<Ayudante> filtrados = controlador.filtrarAyudantes(filtros);
            modeloTabla.limpiar();
            java.util.List<Object[]> filas = new java.util.ArrayList<>();
            for (Ayudante a : filtrados) {
                Object[] fila = {
                    a.getCodigoUnico(),
                    a.getNombresCompletos(),
                    a.getCarrera(),
                    a.getNivel(),
                    String.format("%.2f", a.getIRA()),
                    a.getProyectoAsignado() != null ? a.getProyectoAsignado().getNombreProyecto() : "N/A",
                    a.getHorasSemanales()
                };
                filas.add(fila);
            }
            modeloTabla.establecerDatos(filas);
            campoBusqueda.setText("");
            ToastMessage.mostrar(this, filtrados.size() + " ayudante(s) encontrado(s)", ToastMessage.TipoToast.EXITO);
        });

        btnGenerarReporte.addActionListener(e -> {
            // Preparar datos para el diálogo
            String[] proyectos = obtenerNombresProyectos().toArray(new String[0]);
            String[] carreras = new String[]{"Computacion", "Software"}; 
            String[] niveles = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
            
            DialogoGenerarReporte dialogo = new DialogoGenerarReporte(this, proyectos, carreras, niveles);
            dialogo.setVisible(true);
            
            if (!dialogo.seGenero()) return;
            
            ToastMessage.mostrar(this, "Generando reporte...", ToastMessage.TipoToast.INFO);
            var reporte = controlador.generarReporteGeneral();
            if (reporte != null) {
                ToastMessage.mostrar(this, "Reporte generado exitosamente", ToastMessage.TipoToast.EXITO);
                ofrecerExportacion(reporte);
            } else {
                ToastMessage.mostrar(this, "Error al generar reporte", ToastMessage.TipoToast.ERROR);
            }
        });

        btnVerNotificaciones.addActionListener(e -> {
            List<Notificacion> notas = controlador.obtenerNotificaciones();
            DialogoNotificaciones dialogo = new DialogoNotificaciones(this, notas);
            dialogo.onMarcarLeidasListener(() -> {
                controlador.marcarNotificacionesComoLeidas();
                actualizarContadorNotificaciones(controlador.obtenerCantidadNotificacionesNoLeidas());
                ToastMessage.mostrar(this, "Notificaciones marcadas como leídas", ToastMessage.TipoToast.EXITO);
            });
            dialogo.setVisible(true);
        });
    }
    
    private void configurarAtajosTeclado() {
        // Ctrl+F - Filtrar
        getRootPane().registerKeyboardAction(
            e -> btnFiltrar.doClick(),
            KeyStroke.getKeyStroke(KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_DOWN_MASK),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        
        // Ctrl+R - Generar reporte
        getRootPane().registerKeyboardAction(
            e -> btnGenerarReporte.doClick(),
            KeyStroke.getKeyStroke(KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_DOWN_MASK),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        
        // Ctrl+N - Notificaciones
        getRootPane().registerKeyboardAction(
            e -> btnVerNotificaciones.doClick(),
            KeyStroke.getKeyStroke(KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        
        // F5 - Refrescar
        getRootPane().registerKeyboardAction(
            e -> refrescar(),
            KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    private void cargarDatos() {
        // Cargar proyectos (desde BD)
        comboProyectos.removeAllItems();
        comboProyectos.addItem("Todos");
        List<ProyectoInvestigacion> proyectos = controlador.obtenerTodosProyectos();
        for (ProyectoInvestigacion p : proyectos) {
            comboProyectos.addItem(new ProyectoItem(p.getCodigoProyecto(), p.getNombreProyecto()));
        }
        panelProyectosActivos.actualizarValor(String.valueOf(proyectos.size()));
        
        // Cargar carreras (desde BD)
        comboCarreras.removeAllItems();
        comboCarreras.addItem("Todas");
        java.util.Set<String> carrerasUnicas = new java.util.TreeSet<>();
        for (Ayudante a : controlador.obtenerTodosAyudantes()) {
            if (a.getCarrera() != null && !a.getCarrera().isBlank()) {
                carrerasUnicas.add(a.getCarrera());
            }
        }
        for (String c : carrerasUnicas) comboCarreras.addItem(c);
        panelCarrerasRegistradas.actualizarValor(String.valueOf(carrerasUnicas.size()));
        
        // Actualizar tablas
        actualizarTabla();
        actualizarTablaProyectos();
    }

    private void actualizarBusqueda() {
        String textoBusqueda = campoBusqueda.getText();
        if (textoBusqueda.isEmpty()) {
            cargarDatos();
        } else {
            modeloTabla.buscarEnColumna(textoBusqueda, 1); // Buscar en columna Nombres
        }
    }

    private void actualizarTabla() {
        modeloTabla.limpiar();
        
        List<Ayudante> ayudantes = controlador.obtenerTodosAyudantes();
        panelTotalAyudantes.actualizarValor(String.valueOf(ayudantes.size()));
        
        java.util.List<Object[]> filas = new java.util.ArrayList<>();
        for (Ayudante a : ayudantes) {
            Object[] fila = {
                a.getCodigoUnico(),
                a.getNombresCompletos(),
                a.getCarrera(),
                a.getNivel(),
                String.format("%.2f", a.getIRA()),
                a.getProyectoAsignado() != null ? a.getProyectoAsignado().getNombreProyecto() : "N/A",
                a.getHorasSemanales()
            };
            filas.add(fila);
        }
        modeloTabla.establecerDatos(filas);
        campoBusqueda.setText("");
    }

    private void actualizarTablaProyectos() {
        modeloProyectos.limpiar();
        java.util.List<Object[]> filas = controlador.obtenerResumenProyectos();
        modeloProyectos.establecerDatos(filas);
    }

    public void mostrarVentana() {
        setVisible(true);
    }

    public void cerrar() {
        if (timerNotificaciones != null) {
            timerNotificaciones.stop();
        }
        setVisible(false);
    }

    public Map<String, Object> obtenerFiltrosSeleccionados() {
        Object itemProyecto = comboProyectos.getSelectedItem();
        String carrera = comboCarreras.getSelectedItem() != null ? comboCarreras.getSelectedItem().toString() : "Todas";
        Integer nivel = comboNivel.getSelectedItem() != null ? (Integer) comboNivel.getSelectedItem() : 0;
        String estado = comboEstado.getSelectedItem() != null ? comboEstado.getSelectedItem().toString() : "Todos";

        var builder = new java.util.HashMap<String, Object>();
        if (itemProyecto != null && !(itemProyecto instanceof String)) {
            ProyectoItem pi = (ProyectoItem) itemProyecto;
            builder.put("proyecto", pi.getCodigo());
        }
        if (!"Todas".equalsIgnoreCase(carrera)) builder.put("carrera", carrera);
        if (nivel != 0) builder.put("nivel", nivel);
        if (!"Todos".equalsIgnoreCase(estado)) builder.put("estado", estado);
        return builder;
    }

    private java.util.List<String> obtenerNombresProyectos() {
        var nombres = new java.util.ArrayList<String>();
        List<ProyectoInvestigacion> proyectos = controlador.obtenerTodosProyectos();
        for (ProyectoInvestigacion p : proyectos) {
            nombres.add(p.getNombreProyecto());
        }
        return nombres;
    }

    public void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void actualizarContadorNotificaciones(int cantidad) {
        btnVerNotificaciones.setCount(cantidad);
    }

    public void refrescar() {
        cargarDatos();
        actualizarContadorNotificaciones(controlador.obtenerCantidadNotificacionesNoLeidas());
    }

    public void setControlador(ControladorJefaDepartamento controlador) {
        this.controlador = controlador;
    }

    private void ofrecerExportacion(Reporte reporte) {
        int opcion = JOptionPane.showConfirmDialog(this,
            "¿Desea exportar el reporte a PDF?",
            "Exportar Reporte",
            JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new java.io.File("reporte_" + System.currentTimeMillis() + ".pdf"));
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                boolean exito = controlador.exportarReportePDF(reporte, chooser.getSelectedFile().getAbsolutePath());
                if (exito) {
                    mostrarMensajeExito("Reporte exportado a PDF: " + chooser.getSelectedFile().getAbsolutePath());
                } else {
                    mostrarMensajeError("Error al exportar reporte a PDF");
                }
            }
        }
    }
}
