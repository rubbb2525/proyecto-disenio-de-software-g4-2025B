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
import view.componentes.IconManager;
import view.componentes.RoundedBorder;
import view.componentes.StyledButton;
import view.componentes.ToastMessage;

import javax.swing.*;
import javax.swing.table.JTableHeader;
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
 * Dashboard centralizado para Jefa de Departamento
 * - Dos pestañas: Proyectos y Detalle
 * - Usa ConstantesVisuales
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

    private JLabel lblNombreProyecto;
    private JLabel lblTipoProyecto;
    private JLabel lblDirector;
    private JLabel lblFechas;
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

    public VistaJefaDepartamento(ControladorJefaDepartamento controlador) {
        this.controlador = controlador;
        setTitle("Dashboard - Jefa de Departamento");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(ConstantesVisuales.VENTANA_DASHBOARD);
        setLocationRelativeTo(null);

        inicializarComponentes();
        cargarProyectos();
        configurarEventos();
        setVisible(true);
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(
            ConstantesVisuales.PADDING_MD,
            ConstantesVisuales.PADDING_MD,
            ConstantesVisuales.PADDING_MD,
            ConstantesVisuales.PADDING_MD
        ));

        JPanel header = crearHeader();

        tabs = new JTabbedPane();
        tabs.addTab("Proyectos", crearPanelProyectos());
        tabs.addTab("Detalle", crearPanelDetalle());

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(COLOR_FONDO);
        tabs.setPreferredSize(new Dimension(1200, 700));
        wrapper.add(tabs, new GridBagConstraints());

        panelPrincipal.add(header, BorderLayout.NORTH);
        panelPrincipal.add(wrapper, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
    }

    private JPanel crearHeader() {
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
        header.setPreferredSize(new Dimension(0, ConstantesVisuales.ALTURA_HEADER));
        header.setBorder(BorderFactory.createEmptyBorder(
            ConstantesVisuales.PADDING_MD,
            ConstantesVisuales.PADDING_MD,
            ConstantesVisuales.PADDING_MD,
            ConstantesVisuales.PADDING_MD
        ));

        JLabel titulo = new JLabel("📊 Dashboard de Proyectos");
        titulo.setFont(ConstantesVisuales.FUENTE_TITULO);
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.WEST);

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, ConstantesVisuales.PADDING_SM, 0));
        headerRight.setOpaque(false);

        btnNotificaciones = new StyledButton("Notificaciones", StyledButton.TipoBoton.SECUNDARIO);
        btnNotificaciones.setIcon(IconManager.getInstance().getIcon("bell.svg", 16));
        btnNotificaciones.setToolTipText("Ver notificaciones");

        btnReportes = new StyledButton("Reportes", StyledButton.TipoBoton.EXITO);
        btnReportes.setIcon(IconManager.getInstance().getIcon("report.svg", 16));
        btnReportes.setToolTipText("Generar reportes");

        headerRight.add(btnNotificaciones);
        headerRight.add(btnReportes);
        header.add(headerRight, BorderLayout.EAST);
        return header;
    }

    private JPanel crearPanelProyectos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);

        panelProyectos = new JPanel(new GridLayout(0, 2, ConstantesVisuales.PADDING_MD, ConstantesVisuales.PADDING_MD));
        panelProyectos.setBackground(COLOR_FONDO);
        panelProyectos.setBorder(BorderFactory.createEmptyBorder(
            ConstantesVisuales.PADDING_MD,
            ConstantesVisuales.PADDING_MD,
            ConstantesVisuales.PADDING_MD,
            ConstantesVisuales.PADDING_MD
        ));

        JScrollPane scroll = new JScrollPane(panelProyectos);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(COLOR_FONDO);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelDetalle() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_FONDO);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel cardInfo = new JPanel();
        cardInfo.setLayout(new BoxLayout(cardInfo, BoxLayout.Y_AXIS));
        cardInfo.setBackground(COLOR_TARJETA);
        cardInfo.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(ConstantesVisuales.RADIO_BORDE_GRANDE, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(
                ConstantesVisuales.PADDING_MD,
                ConstantesVisuales.PADDING_MD,
                ConstantesVisuales.PADDING_MD,
                ConstantesVisuales.PADDING_MD
            )
        ));

        lblNombreProyecto = crearLabelDetalle("Proyecto: --", ConstantesVisuales.FUENTE_SUBTITULO);
        lblTipoProyecto = crearLabelDetalle("Tipo: --", ConstantesVisuales.FUENTE_NORMAL);
        lblDirector = crearLabelDetalle("Director: --", ConstantesVisuales.FUENTE_NORMAL);
        lblFechas = crearLabelDetalle("Fechas: --", ConstantesVisuales.FUENTE_NORMAL);

        lblAyudantesResumen = crearLabelDetalle("Ayudantes: --", ConstantesVisuales.FUENTE_NORMAL);
        lblAsistentesResumen = crearLabelDetalle("Asistentes: --", ConstantesVisuales.FUENTE_NORMAL);
        lblTecnicosResumen = crearLabelDetalle("Técnicos: --", ConstantesVisuales.FUENTE_NORMAL);

        cardInfo.add(lblNombreProyecto);
        cardInfo.add(Box.createVerticalStrut(6));
        cardInfo.add(lblTipoProyecto);
        cardInfo.add(lblDirector);
        cardInfo.add(lblFechas);
        cardInfo.add(Box.createVerticalStrut(8));
        cardInfo.add(lblAyudantesResumen);
        cardInfo.add(lblAsistentesResumen);
        cardInfo.add(lblTecnicosResumen);

        panel.add(cardInfo);
        panel.add(Box.createVerticalStrut(ConstantesVisuales.MARGIN_ENTRE_SECCIONES));

        JPanel panelTablas = new JPanel(new GridLayout(1, 3, ConstantesVisuales.PADDING_MD, 0));
        panelTablas.setBackground(COLOR_FONDO);

        JPanel cardAyudantes = crearCardTabla("Ayudantes", crearTablaAyudantes());
        JPanel cardAsistentes = crearCardTabla("Asistentes", crearTablaAsistentes());
        JPanel cardTecnicos = crearCardTabla("Técnicos", crearTablaTecnicos());

        panelTablas.add(cardAyudantes);
        panelTablas.add(cardAsistentes);
        panelTablas.add(cardTecnicos);

        panel.add(panelTablas);
        return panel;
    }

    private JLabel crearLabelDetalle(String texto, Font fuente) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(fuente);
        lbl.setForeground(COLOR_TEXTO);
        return lbl;
    }

    private JPanel crearCardTabla(String titulo, JTable tabla) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(COLOR_TARJETA);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(ConstantesVisuales.RADIO_BORDE_GRANDE, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(
                ConstantesVisuales.PADDING_MD,
                ConstantesVisuales.PADDING_MD,
                ConstantesVisuales.PADDING_MD,
                ConstantesVisuales.PADDING_MD
            )
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(ConstantesVisuales.FUENTE_SUBTITULO);
        lblTitulo.setForeground(COLOR_TEXTO);
        card.add(lblTitulo, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
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
        tabla.setRowHeight(ConstantesVisuales.ALTURA_CAMPO_TEXTO);
        tabla.setFont(ConstantesVisuales.FUENTE_NORMAL);
        tabla.setForeground(COLOR_TEXTO);
        tabla.setSelectionBackground(ConstantesVisuales.COLOR_SECUNDARIO_CLARO);
        tabla.setGridColor(COLOR_BORDE);
        tabla.setShowVerticalLines(false);

        JTableHeader th = tabla.getTableHeader();
        th.setBackground(ConstantesVisuales.COLOR_PRIMARIO);
        th.setForeground(Color.WHITE);
        th.setFont(ConstantesVisuales.FUENTE_NEGRITA);
        th.setPreferredSize(new Dimension(0, 40));
    }

    private void cargarProyectos() {
        panelProyectos.removeAll();
        List<Proyectos> proyectos = controlador.obtenerTodosProyectos();

        for (Proyectos p : proyectos) {
            JPanel card = crearTarjetaProyecto(p);
            panelProyectos.add(card);
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
            new RoundedBorder(ConstantesVisuales.RADIO_BORDE_GRANDE, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(
                ConstantesVisuales.PADDING_MD,
                ConstantesVisuales.PADDING_MD,
                ConstantesVisuales.PADDING_MD,
                ConstantesVisuales.PADDING_MD
            )
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        List<Ayudante> ayudantes = controlador.obtenerAyudantesProyecto(p.getCodigoProyecto());
        long ayudantesActivos = ayudantes.stream().filter(Ayudante::esActivo).count();
        List<AsistenteInvestigacion> asistentes = controlador.obtenerAsistentesProyecto(p.getCodigoProyecto());
        List<TecnicoInvestigacion> tecnicos = controlador.obtenerTecnicosProyecto(p.getCodigoProyecto());

        String directorNombre = p.getDirector() != null
            ? p.getDirector().getNombresCompletos()
            : "Sin asignar";

        JLabel lblNombre = new JLabel(p.getNombreProyecto());
        lblNombre.setFont(ConstantesVisuales.FUENTE_SUBTITULO);
        lblNombre.setForeground(COLOR_TEXTO);

        JLabel lblTipo = new JLabel("Tipo: " + (p.getTipoProyecto() != null ? p.getTipoProyecto() : "N/A"));
        lblTipo.setFont(ConstantesVisuales.FUENTE_NORMAL_PEQUEÑO);
        lblTipo.setForeground(ConstantesVisuales.COLOR_TEXTO_SECUNDARIO);

        JLabel lblDirector = new JLabel("Director: " + directorNombre);
        lblDirector.setFont(ConstantesVisuales.FUENTE_NORMAL_PEQUEÑO);
        lblDirector.setForeground(ConstantesVisuales.COLOR_TEXTO_SECUNDARIO);

        JLabel lblAyudantes = new JLabel("Ayudantes: " + ayudantesActivos + " / " + p.getAyudantesPlanificados());
        lblAyudantes.setFont(ConstantesVisuales.FUENTE_NORMAL);

        JLabel lblAsistentes = new JLabel("Asistentes: " + asistentes.size());
        lblAsistentes.setFont(ConstantesVisuales.FUENTE_NORMAL);

        JLabel lblTecnicos = new JLabel("Técnicos: " + tecnicos.size());
        lblTecnicos.setFont(ConstantesVisuales.FUENTE_NORMAL);

        card.add(lblNombre);
        card.add(Box.createVerticalStrut(4));
        card.add(lblTipo);
        card.add(lblDirector);
        card.add(Box.createVerticalStrut(8));
        card.add(lblAyudantes);
        card.add(lblAsistentes);
        card.add(lblTecnicos);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarProyecto(p);
                tabs.setSelectedIndex(1);
            }
        });

        return card;
    }

    private void seleccionarProyecto(Proyectos p) {
        this.proyectoSeleccionado = p;

        if (p == null) {
            return;
        }

        List<Ayudante> ayudantes = controlador.obtenerAyudantesProyecto(p.getCodigoProyecto());
        List<AsistenteInvestigacion> asistentes = controlador.obtenerAsistentesProyecto(p.getCodigoProyecto());
        List<TecnicoInvestigacion> tecnicos = controlador.obtenerTecnicosProyecto(p.getCodigoProyecto());

        long ayudantesActivos = ayudantes.stream().filter(Ayudante::esActivo).count();

        String directorNombre = p.getDirector() != null
            ? p.getDirector().getNombresCompletos()
            : "Sin asignar";

        lblNombreProyecto.setText("Proyecto: " + p.getNombreProyecto());
        lblTipoProyecto.setText("Tipo: " + (p.getTipoProyecto() != null ? p.getTipoProyecto() : "N/A"));
        lblDirector.setText("Director: " + directorNombre);
        lblFechas.setText("Fechas: " + formatearFecha(p.getFechaInicio()) + " - " + formatearFecha(p.getFechaFin()));

        lblAyudantesResumen.setText("Ayudantes: " + ayudantesActivos + " / " + p.getAyudantesPlanificados());
        lblAsistentesResumen.setText("Asistentes: " + asistentes.size());
        lblTecnicosResumen.setText("Técnicos: " + tecnicos.size());

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
        if (fecha == null) {
            return "N/A";
        }
        return formatoFecha.format(fecha);
    }

    private void configurarEventos() {
        btnNotificaciones.addActionListener(e -> abrirNotificaciones());
        btnReportes.addActionListener(e -> abrirDialogoReportes());
    }

    private void abrirNotificaciones() {
        List<Notificacion> notas = controlador.obtenerNotificaciones();
        DialogoNotificaciones dialogo = new DialogoNotificaciones(this, notas);
        dialogo.onMarcarLeidasListener(() -> {
            controlador.marcarNotificacionesComoLeidas();
            ToastMessage.mostrar(this, "Notificaciones marcadas como leídas", ToastMessage.TipoToast.EXITO);
        });
        dialogo.setVisible(true);
    }

    private void abrirDialogoReportes() {
        String[] proyectos = obtenerNombresProyectos().toArray(new String[0]);
        String[] carreras = obtenerCarreras().toArray(new String[0]);
        String[] niveles = obtenerNiveles();

        DialogoGenerarReporte dialogo = new DialogoGenerarReporte(this, proyectos, carreras, niveles);
        dialogo.setVisible(true);

        if (!dialogo.seGenero()) {
            return;
        }

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

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("reporte_" + System.currentTimeMillis() + ".pdf"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            boolean exito = controlador.exportarReportePDF(reporte, chooser.getSelectedFile().getAbsolutePath());
            if (exito) {
                ToastMessage.mostrar(this, "Reporte exportado a PDF", ToastMessage.TipoToast.EXITO);
            } else {
                ToastMessage.mostrar(this, "Error al exportar reporte a PDF", ToastMessage.TipoToast.ERROR);
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