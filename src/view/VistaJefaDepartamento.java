package view;

import controller.ControladorJefaDepartamento;
import model.Ayudante;
import model.AsistenteInvestigacion;
import model.Notificacion;
import model.Proyectos;
import model.Reporte;
import model.TecnicoInvestigacion;
import view.componentes.AdvancedTableModel;
import view.componentes.ConstantesVisuales;
import view.componentes.RoundedBorder;
import view.componentes.StyledButton;
import view.componentes.ToastMessage;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Dashboard Jefa de Departamento - VERSIÓN MEJORADA
 * Sin emojis ni iconos, solo tipografía y colores
 */
public class VistaJefaDepartamento extends JFrame {
    private static final Color COLOR_FONDO = ConstantesVisuales.COLOR_FONDO_PRINCIPAL;
    private static final Color COLOR_TARJETA = ConstantesVisuales.COLOR_TARJETA;
    private static final Color COLOR_PRIMARIO = ConstantesVisuales.COLOR_PRIMARIO;
    private static final Color COLOR_BORDE = ConstantesVisuales.COLOR_BORDE;
    private static final Color COLOR_TEXTO = ConstantesVisuales.COLOR_TEXTO_PRINCIPAL;

    private final ControladorJefaDepartamento controlador;
    private JPanel panelProyectos;
    private JTabbedPane tabs;
    private StyledButton btnNotificaciones;
    private StyledButton btnReportes;
    private JLabel lblContadorNotif;

    // Detalle del proyecto
    private JLabel lblNombreProyecto;
    private JLabel lblTipoProyecto;
    private JLabel lblDirector;
    private JLabel lblFechas;
    private JLabel lblEstadoProyecto;
    private JLabel lblAyudantesResumen;
    private JLabel lblAsistentesResumen;
    private JLabel lblTecnicosResumen;

    private AdvancedTableModel modeloAyudantes;
    private AdvancedTableModel modeloAsistentes;
    private AdvancedTableModel modeloTecnicos;

    private JTable tablaAyudantes;
    private JTable tablaAsistentes;
    private JTable tablaTecnicos;

    private Proyectos proyectoSeleccionado;
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    
    private JPanel tarjetaSeleccionada;

    public VistaJefaDepartamento(ControladorJefaDepartamento controlador) {
        this.controlador = controlador;
        setTitle("Dashboard - Jefa de Departamento");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 850);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 700));

        inicializarComponentes();
        cargarProyectos();
        configurarEventos();
        setVisible(true);
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 0));
        panelPrincipal.setBackground(COLOR_FONDO);

        // Header
        JPanel header = crearHeaderMejorado();

        // Tabs
        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(COLOR_FONDO);
        
        tabs.addTab("Vista General de Proyectos", crearPanelProyectos());
        tabs.addTab("Detalle del Proyecto", crearPanelDetalle());

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(COLOR_FONDO);
        centro.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        centro.add(tabs, BorderLayout.CENTER);

        panelPrincipal.add(header, BorderLayout.NORTH);
        panelPrincipal.add(centro, BorderLayout.CENTER);

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
        
        JLabel lblTitulo = new JLabel("Panel de la Jefa de Departamento");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        
        JLabel lblSubtitulo = new JLabel("Supervisión y Gestión de Proyectos de Investigación");
        lblSubtitulo.setForeground(new Color(255, 255, 255, 180));
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        panelIzq.add(lblTitulo);
        panelIzq.add(lblSubtitulo);

        // Panel derecho
        JPanel panelDer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelDer.setOpaque(false);

        // Botón notificaciones con contador
        JPanel contenedorNotif = new JPanel(new BorderLayout(0, 0));
        contenedorNotif.setOpaque(false);
        
        btnNotificaciones = new StyledButton("Notificaciones", StyledButton.TipoBoton.SECUNDARIO);
        btnNotificaciones.setPreferredSize(new Dimension(140, 40));
        btnNotificaciones.setToolTipText("Ver notificaciones del sistema");
        
        lblContadorNotif = new JLabel("");
        lblContadorNotif.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblContadorNotif.setForeground(Color.WHITE);
        lblContadorNotif.setBackground(new Color(231, 76, 60));
        lblContadorNotif.setOpaque(true);
        lblContadorNotif.setHorizontalAlignment(SwingConstants.CENTER);
        lblContadorNotif.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        lblContadorNotif.setVisible(false);
        
        contenedorNotif.add(btnNotificaciones, BorderLayout.CENTER);
        contenedorNotif.add(lblContadorNotif, BorderLayout.EAST);

        btnReportes = new StyledButton("Generar Reportes", StyledButton.TipoBoton.EXITO);
        btnReportes.setPreferredSize(new Dimension(160, 40));
        btnReportes.setToolTipText("Generar reportes estadísticos");

        panelDer.add(contenedorNotif);
        panelDer.add(btnReportes);

        header.add(panelIzq, BorderLayout.WEST);
        header.add(panelDer, BorderLayout.EAST);
        return header;
    }

    private JPanel crearPanelProyectos() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Título de sección
        JLabel lblTitulo = new JLabel("PROYECTOS DE INVESTIGACIÓN ACTIVOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(100, 100, 100));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        panelProyectos = new JPanel(new GridLayout(0, 1, 20, 20));
        panelProyectos.setBackground(COLOR_FONDO);

        JScrollPane scroll = new JScrollPane(panelProyectos);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(COLOR_FONDO);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(COLOR_FONDO);
        contenedor.add(lblTitulo, BorderLayout.NORTH);
        contenedor.add(scroll, BorderLayout.CENTER);

        panel.add(contenedor, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelDetalle() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_FONDO);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Título de sección
        JLabel lblTitulo = new JLabel("INFORMACIÓN DETALLADA DEL PROYECTO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(100, 100, 100));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(12));

        // Card de información del proyecto
        JPanel cardInfo = crearCardInformacion();
        cardInfo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        panel.add(cardInfo);
        panel.add(Box.createVerticalStrut(20));

        // Título de subsección
        JLabel lblSubtitulo = new JLabel("COLABORADORES DEL PROYECTO");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSubtitulo.setForeground(new Color(100, 100, 100));
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblSubtitulo);
        panel.add(Box.createVerticalStrut(12));

        // Panel de tablas
        JPanel panelTablas = crearPanelTablas();
        panelTablas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        panel.add(panelTablas);

        return panel;
    }

    private JPanel crearCardInformacion() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_TARJETA);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(24, 28, 24, 28)
        ));

        // Nombre del proyecto (destacado)
        lblNombreProyecto = new JLabel("Seleccione un proyecto de la pestaña anterior");
        lblNombreProyecto.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblNombreProyecto.setForeground(COLOR_PRIMARIO);
        lblNombreProyecto.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Panel de metadatos (2 columnas)
        JPanel panelMeta = new JPanel(new GridLayout(2, 2, 20, 8));
        panelMeta.setOpaque(false);
        panelMeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        lblTipoProyecto = crearLabelInfo("Tipo de proyecto: --");
        lblDirector = crearLabelInfo("Director responsable: --");
        lblFechas = crearLabelInfo("Periodo de ejecución: --");
        lblEstadoProyecto = crearLabelInfo("Estado: --");

        panelMeta.add(lblTipoProyecto);
        panelMeta.add(lblDirector);
        panelMeta.add(lblFechas);
        panelMeta.add(lblEstadoProyecto);

        // Separador
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(COLOR_BORDE);

        // Panel de resumen de colaboradores
        JPanel panelResumen = new JPanel(new GridLayout(1, 3, 16, 0));
        panelResumen.setOpaque(false);
        panelResumen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        lblAyudantesResumen = crearLabelResumen("Ayudantes: --");
        lblAsistentesResumen = crearLabelResumen("Asistentes: --");
        lblTecnicosResumen = crearLabelResumen("Técnicos: --");

        panelResumen.add(lblAyudantesResumen);
        panelResumen.add(lblAsistentesResumen);
        panelResumen.add(lblTecnicosResumen);

        card.add(lblNombreProyecto);
        card.add(Box.createVerticalStrut(16));
        card.add(panelMeta);
        card.add(Box.createVerticalStrut(16));
        card.add(sep);
        card.add(Box.createVerticalStrut(16));
        card.add(panelResumen);

        return card;
    }

    private JLabel crearLabelInfo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(120, 120, 120));
        return lbl;
    }

    private JLabel crearLabelResumen(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(COLOR_TEXTO);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    private JPanel crearPanelTablas() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 16, 0));
        panel.setOpaque(false);

        JPanel cardAyudantes = crearCardTabla("Ayudantes", crearTablaAyudantes());
        JPanel cardAsistentes = crearCardTabla("Asistentes", crearTablaAsistentes());
        JPanel cardTecnicos = crearCardTabla("Técnicos", crearTablaTecnicos());

        panel.add(cardAyudantes);
        panel.add(cardAsistentes);
        panel.add(cardTecnicos);

        return panel;
    }

    private JPanel crearCardTabla(String titulo, JTable tabla) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(COLOR_TARJETA);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(COLOR_TEXTO);
        card.add(lblTitulo, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        card.add(scroll, BorderLayout.CENTER);

        return card;
    }

    private JTable crearTablaAyudantes() {
        String[] columnas = {"Código", "Nombre", "Carrera", "Nivel", "Horas", "Estado"};
        modeloAyudantes = new AdvancedTableModel(columnas);
        tablaAyudantes = new JTable(modeloAyudantes);
        estilizarTabla(tablaAyudantes);
        return tablaAyudantes;
    }

    private JTable crearTablaAsistentes() {
        String[] columnas = {"Código", "Nombre", "Carrera", "Nivel", "Horas", "Estado"};
        modeloAsistentes = new AdvancedTableModel(columnas);
        tablaAsistentes = new JTable(modeloAsistentes);
        estilizarTabla(tablaAsistentes);
        return tablaAsistentes;
    }

    private JTable crearTablaTecnicos() {
        String[] columnas = {"ID", "Nombre", "Correo", "Horas", "Estado"};
        modeloTecnicos = new AdvancedTableModel(columnas);
        tablaTecnicos = new JTable(modeloTecnicos);
        estilizarTabla(tablaTecnicos);
        return tablaTecnicos;
    }

    private void estilizarTabla(JTable tabla) {
        tabla.setRowHeight(35);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setForeground(COLOR_TEXTO);
        tabla.setSelectionBackground(new Color(52, 152, 219, 30));
        tabla.setSelectionForeground(COLOR_TEXTO);
        tabla.setGridColor(new Color(220, 220, 220));
        tabla.setShowVerticalLines(false);

        JTableHeader th = tabla.getTableHeader();
        th.setBackground(COLOR_PRIMARIO);
        th.setForeground(Color.WHITE);
        th.setFont(new Font("Segoe UI", Font.BOLD, 12));
        th.setPreferredSize(new Dimension(0, 35));

        // Filas alternadas
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                }
                
                if (c instanceof JLabel) {
                    ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                }
                
                return c;
            }
        });
    }

    private void cargarProyectos() {
        panelProyectos.removeAll();
        List<Proyectos> proyectos = controlador.obtenerTodosProyectos();

        if (proyectos.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay proyectos disponibles");
            lblVacio.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            lblVacio.setForeground(new Color(150, 150, 150));
            lblVacio.setHorizontalAlignment(SwingConstants.CENTER);
            panelProyectos.add(lblVacio);
        } else {
            for (Proyectos p : proyectos) {
                JPanel card = crearTarjetaProyecto(p);
                panelProyectos.add(card);
            }
        }

        panelProyectos.revalidate();
        panelProyectos.repaint();

        if (!proyectos.isEmpty()) {
            seleccionarProyecto(proyectos.get(0));
        }
    }

    private JPanel crearTarjetaProyecto(Proyectos p) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_TARJETA);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Obtener datos
        List<Ayudante> ayudantes = controlador.obtenerAyudantesProyecto(p.getCodigoProyecto());
        long ayudantesActivos = ayudantes.stream().filter(Ayudante::esActivo).count();
        List<AsistenteInvestigacion> asistentes = controlador.obtenerAsistentesProyecto(p.getCodigoProyecto());
        List<TecnicoInvestigacion> tecnicos = controlador.obtenerTecnicosProyecto(p.getCodigoProyecto());

        String directorNombre = p.getDirector() != null
            ? p.getDirector().getNombresCompletos()
            : "Sin asignar";

        // Nombre del proyecto
        JLabel lblNombre = new JLabel(p.getNombreProyecto());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNombre.setForeground(COLOR_PRIMARIO);
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Código del proyecto
        JLabel lblCodigo = new JLabel("Código: " + p.getCodigoProyecto());
        lblCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCodigo.setForeground(new Color(150, 150, 150));
        lblCodigo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Metadata
        JLabel lblTipo = new JLabel("Tipo: " + (p.getTipoProyecto() != null ? p.getTipoProyecto() : "N/A"));
        lblTipo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTipo.setForeground(new Color(120, 120, 120));
        lblTipo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDirector = new JLabel("Director: " + directorNombre);
        lblDirector.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDirector.setForeground(new Color(120, 120, 120));
        lblDirector.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Separador
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(COLOR_BORDE);

        // Panel de estadísticas
        JPanel panelStats = new JPanel(new GridLayout(3, 1, 0, 6));
        panelStats.setOpaque(false);
        panelStats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lblAyudantes = crearLabelStat("Ayudantes:", ayudantesActivos, p.getAyudantesPlanificados());
        JLabel lblAsistentes = crearLabelStat("Asistentes:", asistentes.size(), p.getAsistentesPlanificados());
        JLabel lblTecnicos = crearLabelStat("Técnicos:", tecnicos.size(), p.getTecnicosPlanificados());

        panelStats.add(lblAyudantes);
        panelStats.add(lblAsistentes);
        panelStats.add(lblTecnicos);

        card.add(lblNombre);
        card.add(Box.createVerticalStrut(2));
        card.add(lblCodigo);
        card.add(Box.createVerticalStrut(12));
        card.add(lblTipo);
        card.add(lblDirector);
        card.add(Box.createVerticalStrut(12));
        card.add(sep);
        card.add(Box.createVerticalStrut(12));
        card.add(panelStats);

        // Efecto hover y click
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (card != tarjetaSeleccionada) {
                    card.setBackground(new Color(248, 250, 252));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (card != tarjetaSeleccionada) {
                    card.setBackground(COLOR_TARJETA);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarTarjetaVisual(card);
                seleccionarProyecto(p);
                tabs.setSelectedIndex(1);
            }
        });

        return card;
    }

    private JLabel crearLabelStat(String label, long actual, int total) {
        String texto = label + " " + actual + " / " + total;
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(COLOR_TEXTO);
        
        // Colorear según disponibilidad
        if (actual < total) {
            lbl.setForeground(new Color(46, 204, 113)); // Verde
        } else if (actual == total) {
            lbl.setForeground(new Color(243, 156, 18)); // Amarillo
        } else {
            lbl.setForeground(new Color(231, 76, 60)); // Rojo
        }
        
        return lbl;
    }

    private void seleccionarTarjetaVisual(JPanel card) {
        if (tarjetaSeleccionada != null) {
            tarjetaSeleccionada.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(12, COLOR_BORDE, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));
            tarjetaSeleccionada.setBackground(COLOR_TARJETA);
        }
        
        tarjetaSeleccionada = card;
        tarjetaSeleccionada.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, COLOR_PRIMARIO, 3),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        tarjetaSeleccionada.setBackground(new Color(230, 240, 255));
    }

    private void seleccionarProyecto(Proyectos p) {
        this.proyectoSeleccionado = p;

        if (p == null) return;

        List<Ayudante> ayudantes = controlador.obtenerAyudantesProyecto(p.getCodigoProyecto());
        List<AsistenteInvestigacion> asistentes = controlador.obtenerAsistentesProyecto(p.getCodigoProyecto());
        List<TecnicoInvestigacion> tecnicos = controlador.obtenerTecnicosProyecto(p.getCodigoProyecto());

        long ayudantesActivos = ayudantes.stream().filter(Ayudante::esActivo).count();

        String directorNombre = p.getDirector() != null
            ? p.getDirector().getNombresCompletos()
            : "Sin asignar";

        lblNombreProyecto.setText(p.getNombreProyecto());
        lblTipoProyecto.setText("Tipo de proyecto: " + (p.getTipoProyecto() != null ? p.getTipoProyecto() : "N/A"));
        lblDirector.setText("Director responsable: " + directorNombre);
        lblFechas.setText("Periodo de ejecución: " + formatearFecha(p.getFechaInicio()) + " - " + formatearFecha(p.getFechaFin()));
        lblEstadoProyecto.setText("Estado: " + (p.getEstado() != null ? p.getEstado() : "Activo"));

        lblAyudantesResumen.setText("Ayudantes: " + ayudantesActivos + " / " + p.getAyudantesPlanificados());
        lblAsistentesResumen.setText("Asistentes: " + asistentes.size() + " / " + p.getAsistentesPlanificados());
        lblTecnicosResumen.setText("Técnicos: " + tecnicos.size() + " / " + p.getTecnicosPlanificados());

        cargarTablaAyudantes(ayudantes);
        cargarTablaAsistentes(asistentes);
        cargarTablaTecnicos(tecnicos);
    }

    private void cargarTablaAyudantes(List<Ayudante> ayudantes) {
        modeloAyudantes.limpiar();
        java.util.List<Object[]> filas = new java.util.ArrayList<>();
        for (Ayudante a : ayudantes) {
            Object[] fila = {
                a.getCodigoUnico(),
                a.getNombresCompletos(),
                a.getCarrera(),
                a.getNivel(),
                a.getHorasSemanales(),
                a.esActivo() ? "Activo" : "Inactivo"
            };
            filas.add(fila);
        }
        modeloAyudantes.establecerDatos(filas);
    }

    private void cargarTablaAsistentes(List<AsistenteInvestigacion> asistentes) {
        modeloAsistentes.limpiar();
        java.util.List<Object[]> filas = new java.util.ArrayList<>();
        for (AsistenteInvestigacion a : asistentes) {
            Object[] fila = {
                a.getCodigoUnico(),
                a.getNombresCompletos(),
                a.getCarrera(),
                a.getNivel(),
                a.getHorasSemanales(),
                a.esActivo() ? "Activo" : "Inactivo"
            };
            filas.add(fila);
        }
        modeloAsistentes.establecerDatos(filas);
    }

    private void cargarTablaTecnicos(List<TecnicoInvestigacion> tecnicos) {
        modeloTecnicos.limpiar();
        java.util.List<Object[]> filas = new java.util.ArrayList<>();
        for (TecnicoInvestigacion t : tecnicos) {
            Object[] fila = {
                t.getIdTecnico(),
                t.getNombres() + " " + t.getApellidos(),
                t.getCorreoElectronico(),
                t.getHorasSemanales(),
                t.getEstado()
            };
            filas.add(fila);
        }
        modeloTecnicos.establecerDatos(filas);
    }

    private String formatearFecha(java.util.Date fecha) {
        if (fecha == null) return "N/A";
        return formatoFecha.format(fecha);
    }

    private void configurarEventos() {
        btnNotificaciones.addActionListener(e -> abrirNotificaciones());
        btnReportes.addActionListener(e -> abrirDialogoReportes());
        
        actualizarBadgeNotificaciones();
    }

    private void actualizarBadgeNotificaciones() {
        int cantidadNoLeidas = controlador.obtenerCantidadNotificacionesNoLeidas();
        if (cantidadNoLeidas > 0) {
            lblContadorNotif.setText(String.valueOf(cantidadNoLeidas));
            lblContadorNotif.setVisible(true);
        } else {
            lblContadorNotif.setVisible(false);
        }
    }

    private void abrirNotificaciones() {
        List<Notificacion> notas = controlador.obtenerNotificaciones();
        DialogoNotificaciones dialogo = new DialogoNotificaciones(this, notas);
        dialogo.onMarcarLeidasListener(() -> {
            controlador.marcarNotificacionesComoLeidas();
            ToastMessage.mostrar(this, "Notificaciones marcadas como leídas", ToastMessage.TipoToast.EXITO);
            actualizarBadgeNotificaciones();
        });
        dialogo.setVisible(true);
        actualizarBadgeNotificaciones();
    }

    private void abrirDialogoReportes() {
        String[] proyectos = obtenerNombresProyectos().toArray(new String[0]);
        String[] carreras = obtenerCarreras().toArray(new String[0]);
        String[] niveles = obtenerNiveles();

        DialogoGenerarReporte dialogo = new DialogoGenerarReporte(this, proyectos, carreras, niveles);
        dialogo.setVisible(true);

        if (!dialogo.seGenero()) return;

        Reporte reporte = generarReporte(dialogo.getTipoReporte(), dialogo.getFiltroSeleccionado());
        if (reporte == null) {
            ToastMessage.mostrar(this, "Error al generar reporte", ToastMessage.TipoToast.ERROR);
            return;
        }

        exportarReporte(reporte);
    }

    private Reporte generarReporte(String tipo, String filtro) {
        if ("General".equals(tipo)) {
            return controlador.generarReporteGeneral();
        }
        if ("Por Proyecto".equals(tipo)) {
            Proyectos proyecto = buscarProyectoPorNombre(filtro);
            return proyecto != null ? controlador.generarReportePorProyecto(proyecto.getCodigoProyecto()) : null;
        }
        if ("Por Carrera".equals(tipo)) {
            return controlador.generarReportePorCarrera(filtro);
        }
        if ("Por Nivel".equals(tipo)) {
            try {
                int nivel = Integer.parseInt(filtro);
                return controlador.generarReportePorNivel(nivel);
            } catch (NumberFormatException ex) {
                return null;
            }
        }
        return null;
    }

    private void exportarReporte(Reporte reporte) {
        int opcion = JOptionPane.showConfirmDialog(this,
            "¿Desea exportar el reporte a PDF?",
            "Exportar Reporte",
            JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) return;

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("reporte_" + System.currentTimeMillis() + ".pdf"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            boolean exito = controlador.exportarReportePDF(reporte, chooser.getSelectedFile().getAbsolutePath());
            if (exito) {
                ToastMessage.mostrar(this, "Reporte exportado exitosamente", ToastMessage.TipoToast.EXITO);
            } else {
                ToastMessage.mostrar(this, "Error al exportar reporte", ToastMessage.TipoToast.ERROR);
            }
        }
    }

    private List<String> obtenerNombresProyectos() {
        List<String> nombres = new ArrayList<>();
        for (Proyectos p : controlador.obtenerTodosProyectos()) {
            nombres.add(p.getNombreProyecto());
        }
        return nombres;
    }

    private List<String> obtenerCarreras() {
        Set<String> carreras = new TreeSet<>();
        for (Ayudante a : controlador.obtenerTodosAyudantes()) {
            if (a.getCarrera() != null && !a.getCarrera().isBlank()) {
                carreras.add(a.getCarrera());
            }
        }
        if (carreras.isEmpty()) {
            carreras.add("Sin datos");
        }
        return new ArrayList<>(carreras);
    }

    private String[] obtenerNiveles() {
        return new String[] {"1","2","3","4","5","6","7","8","9","10"};
    }

    private Proyectos buscarProyectoPorNombre(String nombre) {
        for (Proyectos p : controlador.obtenerTodosProyectos()) {
            if (p.getNombreProyecto().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }
}