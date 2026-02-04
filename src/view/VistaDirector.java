package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import controller.ControladorDirector;
import model.Ayudante;
import model.Proyectos;
import view.componentes.StyledButton;
import view.componentes.IconManager;
import view.componentes.RoundedBorder;
import view.componentes.AdvancedTableModel;
import view.componentes.ToastMessage;
import view.componentes.PanelEstadistica;
import view.componentes.ConstantesVisuales;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.GradientPaint;
import java.util.Date;
import model.AsistenteInvestigacion;
import model.TecnicoInvestigacion;

/**
 * Ventana principal del Director
 * Requisitos visuales: Usa ConstantesVisuales
 */
public class VistaDirector extends JFrame {
    // Usar constantes visuales estándar
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
    private JLabel lblProyecto;
    private JLabel lblCuposDisponibles;
    private ControladorDirector controlador;
    private AdvancedTableModel modeloTabla;
    private AdvancedTableModel modeloAsistentes;
    private AdvancedTableModel modeloTecnicos;
    private JTable tablaAsistentes;
    private JTable tablaTecnicos;
    private JTextField campoBusqueda;
    private javax.swing.Timer timerRefresh;
    private PanelEstadistica panelTotalAyudantes;
    private PanelEstadistica panelCuposDisponibles;
    private PanelEstadistica panelHorasTotales;

    public VistaDirector(ControladorDirector controlador) {
        this.controlador = controlador;
        setTitle("Sistema Gestión Ayudantes - Director");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(ConstantesVisuales.VENTANA_DASHBOARD);
        setLocationRelativeTo(null);

        inicializarComponentes();
        cargarDatos();
        configurarEventos();
        
        // Timer para refrescar datos cada 3 segundos
        timerRefresh = new javax.swing.Timer(3000, e -> cargarDatos());
        timerRefresh.start();
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

        // Header con gradiente
        JPanel header = crearHeader();

        // Paneles de estadísticas
        JPanel panelEstadisticas = crearPanelEstadisticas();

        // Tarjeta proyecto
        JPanel cardProyecto = crearCardProyecto();

        // Tabla
        JPanel panelTabla = crearPanelTabla();

        // Botonera
        JPanel acciones = crearPanelAcciones();

        JPanel centro = new JPanel();
        centro.setBackground(COLOR_FONDO);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.add(panelEstadisticas);
        centro.add(Box.createVerticalStrut(ConstantesVisuales.MARGIN_ENTRE_SECCIONES));
        centro.add(cardProyecto);
        centro.add(Box.createVerticalStrut(ConstantesVisuales.MARGIN_ENTRE_SECCIONES));
        centro.add(panelTabla);
        centro.add(Box.createVerticalStrut(ConstantesVisuales.MARGIN_ENTRE_SECCIONES));
        centro.add(acciones);

        panelPrincipal.add(header, BorderLayout.NORTH);
        panelPrincipal.add(centro, BorderLayout.CENTER);

        add(panelPrincipal);
    }

    private void configurarEventos() {
        // Atajos de teclado
        configurarAtajosTeclado();
        
        btnRefrescar.addActionListener(e -> {
            ToastMessage.mostrar(this, "Actualizando datos...", ToastMessage.TipoToast.INFO);
            cargarDatos();
        });

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
                ToastMessage.mostrar(this, "Ya tiene un proyecto activo", ToastMessage.TipoToast.ADVERTENCIA);
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
        // F5 - Refrescar
        getRootPane().registerKeyboardAction(
            e -> btnRefrescar.doClick(),
            KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        
        // Ctrl+N - Nuevo ayudante
        getRootPane().registerKeyboardAction(
            e -> btnRegistrar.doClick(),
            KeyStroke.getKeyStroke(KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        
        // Supr - Dar de baja
        getRootPane().registerKeyboardAction(
            e -> darDeBajaAyudante(),
            KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        // Ctrl+P - Crear Proyecto
        getRootPane().registerKeyboardAction(
            e -> btnCrearProyecto.doClick(),
            KeyStroke.getKeyStroke(KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_DOWN_MASK),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }
    
    private void darDeBajaAyudante() {
        Ayudante seleccionado = getAyudanteSeleccionado();
        if (seleccionado == null) {
            ToastMessage.mostrar(this, "Selecciona un ayudante activo", ToastMessage.TipoToast.ADVERTENCIA);
            return;
        }
        DialogoConfirmacion dialogo = new DialogoConfirmacion(this, "Confirmar baja", 
            "¿Dar de baja a " + seleccionado.getNombresCompletos() + "?");
        dialogo.setVisible(true);
        if (!dialogo.esConfirmado()) return;
            
        var res = controlador.darDeBajaAyudante(seleccionado.getCodigoUnico(), "Baja manual", new Date());
        if (res.esExitoso()) {
            ToastMessage.mostrar(this, res.getMensaje(), ToastMessage.TipoToast.EXITO);
            cargarDatos();
        } else {
            ToastMessage.mostrar(this, res.getMensaje(), ToastMessage.TipoToast.ERROR);
        }
    }

    private void actualizarBusqueda() {
        String textoBusqueda = campoBusqueda.getText();
        if (textoBusqueda.isEmpty()) {
            cargarDatos();
        } else {
            modeloTabla.buscarEnColumna(textoBusqueda, 1); // Buscar en columna Nombres
        }
    }

    private void cargarDatos() {
        Proyectos proyecto = controlador.getProyecto();
        if (proyecto != null) {
            lblProyecto.setText("Proyecto: " + proyecto.getNombreProyecto());
            lblCuposDisponibles.setText("Cupos disponibles: " + proyecto.getCuposDisponibles());
            panelCuposDisponibles.actualizarValor(String.valueOf(proyecto.getCuposDisponibles()));
        }
        
        actualizarTablaAyudantes();
        actualizarTablaAsistentes();
        actualizarTablaTecnicos();
    }

    private void actualizarTablaAyudantes() {
        modeloTabla.limpiar();
        
        java.util.List<Ayudante> ayudantes = controlador.consultarAyudantesDelProyecto();
        if (ayudantes != null) {
            // Actualizar estadísticas
            panelTotalAyudantes.actualizarValor(String.valueOf(ayudantes.size()));
            
            int horasTotales = 0;
            java.util.List<Object[]> filas = new java.util.ArrayList<>();
            for (Ayudante a : ayudantes) {
                if (a.esActivo()) {
                    horasTotales += a.getHorasSemanales();
                    Object[] fila = {
                        a.getCodigoUnico(),
                        a.getNombresCompletos(),
                        a.getCarrera(),
                        a.getNivel(),
                        String.format("%.2f", a.getIRA()),
                        a.getHorasSemanales(),
                        String.format("$%.2f", a.calcularCostoTotal())
                    };
                    filas.add(fila);
                }
            }
            panelHorasTotales.actualizarValor(String.valueOf(horasTotales));
            modeloTabla.establecerDatos(filas);
        }
        campoBusqueda.setText("");
    }

    private void actualizarTablaAsistentes() {
        modeloAsistentes.limpiar();
        
        java.util.List<AsistenteInvestigacion> asistentes = controlador.obtenerAsistentesProyecto();
        if (asistentes != null && !asistentes.isEmpty()) {
            java.util.List<Object[]> filas = new java.util.ArrayList<>();
            for (AsistenteInvestigacion a : asistentes) {
                if (a.esActivo()) {
                    Object[] fila = {
                        a.getCodigoUnico(),
                        a.getNombresCompletos(),
                        a.getCarrera(),
                        a.getNivel(),
                        String.format("%.2f", a.getIRA()),
                        a.getHorasSemanales(),
                        String.format("$%.2f", a.calcularCostoTotal())
                    };
                    filas.add(fila);
                }
            }
            modeloAsistentes.establecerDatos(filas);
        }
    }

    private void actualizarTablaTecnicos() {
        modeloTecnicos.limpiar();
        
        java.util.List<TecnicoInvestigacion> tecnicos = controlador.obtenerTecnicosProyecto();
        if (tecnicos != null && !tecnicos.isEmpty()) {
            java.util.List<Object[]> filas = new java.util.ArrayList<>();
            for (TecnicoInvestigacion t : tecnicos) {
                if (t.esActivo()) {
                    Object[] fila = {
                        t.getIdTecnico(),
                        t.getNombresCompletos(),
                        t.getHorasSemanales(),
                        String.format("$%.2f", t.calcularCostoTotal())
                    };
                    filas.add(fila);
                }
            }
            modeloTecnicos.establecerDatos(filas);
        }
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

        JLabel lblTitulo = new JLabel("👤 Director");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(ConstantesVisuales.FUENTE_TITULO);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel headerLeft = new JPanel();
        headerLeft.setOpaque(false);
        headerLeft.add(lblTitulo);

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerRight.setOpaque(false);
        btnRefrescar = new StyledButton("Refrescar", StyledButton.TipoBoton.SECUNDARIO);
        btnRefrescar.setToolTipText("Actualizar datos (F5)");
        headerRight.add(btnRefrescar);
        
        header.add(headerLeft, BorderLayout.WEST);
        header.add(headerRight, BorderLayout.EAST);
        return header;
    }

    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, ConstantesVisuales.PADDING_MD, 0));
        panel.setBackground(COLOR_FONDO);
        
        panelTotalAyudantes = new PanelEstadistica("Total Ayudantes", "0", 
            ConstantesVisuales.COLOR_INFO, "👥");
        panelCuposDisponibles = new PanelEstadistica("Cupos Disponibles", "0", 
            ConstantesVisuales.COLOR_EXITO, "✓");
        panelHorasTotales = new PanelEstadistica("Horas Semanales", "0", 
            new Color(155, 89, 182), "⏱");
        
        panel.add(panelTotalAyudantes);
        panel.add(panelCuposDisponibles);
        panel.add(panelHorasTotales);
        
        return panel;
    }

    private JPanel crearCardProyecto() {
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

        lblProyecto = new JLabel("Proyecto: Cargando...");
        lblProyecto.setFont(ConstantesVisuales.FUENTE_NEGRITA);
        lblProyecto.setForeground(COLOR_TEXTO);
        lblProyecto.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblCuposDisponibles = new JLabel("Cupos disponibles: Cargando...");
        lblCuposDisponibles.setFont(ConstantesVisuales.FUENTE_NORMAL);
        lblCuposDisponibles.setForeground(ConstantesVisuales.COLOR_TEXTO_SECUNDARIO);
        lblCuposDisponibles.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lblProyecto);
        card.add(Box.createVerticalStrut(6));
        card.add(lblCuposDisponibles);

        return card;
    }

    private JPanel crearPanelTabla() {
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(COLOR_FONDO);

        // Barra de búsqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout(ConstantesVisuales.PADDING_MD, 0));
        panelBusqueda.setBackground(COLOR_FONDO);
        
        JLabel lblBuscar = new JLabel("🔍 Buscar:");
        lblBuscar.setFont(ConstantesVisuales.FUENTE_NORMAL_PEQUEÑO);
        lblBuscar.setForeground(ConstantesVisuales.COLOR_TEXTO_PRINCIPAL);
        
        campoBusqueda = new JTextField();
        campoBusqueda.setPreferredSize(new Dimension(250, ConstantesVisuales.ALTURA_CAMPO_TEXTO));
        campoBusqueda.setFont(ConstantesVisuales.FUENTE_NORMAL);
        campoBusqueda.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(ConstantesVisuales.RADIO_BORDE_NORMAL, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(ConstantesVisuales.PADDING_XS, 
                                           ConstantesVisuales.PADDING_SM, 
                                           ConstantesVisuales.PADDING_XS, 
                                           ConstantesVisuales.PADDING_SM)
        ));
        campoBusqueda.setToolTipText("Buscar por nombre, código o carrera");
        
        panelBusqueda.add(lblBuscar, BorderLayout.WEST);
        panelBusqueda.add(campoBusqueda, BorderLayout.CENTER);

        // Crear tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(COLOR_FONDO);
        tabs.setFont(ConstantesVisuales.FUENTE_NORMAL);
        
        // Tab Ayudantes
        String[] columnasAyudantes = {"Código", "Nombres", "Carrera", "Nivel", "IRA", "Horas", "Salario"};
        modeloTabla = new AdvancedTableModel(columnasAyudantes);
        tablaAyudantes = crearTablaEstilizada(modeloTabla);
        JScrollPane scrollAyudantes = new JScrollPane(tablaAyudantes);
        scrollAyudantes.setBorder(crearBordeTabla());
        tabs.addTab("👥 Ayudantes", scrollAyudantes);
        
        // Tab Asistentes
        String[] columnasAsistentes = {"Código", "Nombres", "Carrera", "Nivel", "IRA", "Horas", "Salario"};
        modeloAsistentes = new AdvancedTableModel(columnasAsistentes);
        tablaAsistentes = crearTablaEstilizada(modeloAsistentes);
        JScrollPane scrollAsistentes = new JScrollPane(tablaAsistentes);
        scrollAsistentes.setBorder(crearBordeTabla());
        tabs.addTab("🔬 Asistentes", scrollAsistentes);
        
        // Tab Técnicos
        String[] columnasTecnicos = {"ID", "Nombres", "Horas", "Salario"};
        modeloTecnicos = new AdvancedTableModel(columnasTecnicos);
        tablaTecnicos = crearTablaEstilizada(modeloTecnicos);
        JScrollPane scrollTecnicos = new JScrollPane(tablaTecnicos);
        scrollTecnicos.setBorder(crearBordeTabla());
        tabs.addTab("⚙️ Técnicos", scrollTecnicos);
        
        panelTabla.add(panelBusqueda, BorderLayout.NORTH);
        panelTabla.add(tabs, BorderLayout.CENTER);
        return panelTabla;
    }

    private JTable crearTablaEstilizada(AdvancedTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(ConstantesVisuales.ALTURA_CAMPO_TEXTO);
        tabla.setFont(ConstantesVisuales.FUENTE_NORMAL);
        tabla.setForeground(COLOR_TEXTO);
        tabla.setSelectionBackground(ConstantesVisuales.COLOR_SECUNDARIO_CLARO);
        tabla.setGridColor(COLOR_BORDE);
        
        // Estilizar header
        JTableHeader th = tabla.getTableHeader();
        th.setBackground(ConstantesVisuales.COLOR_PRIMARIO);
        th.setForeground(Color.WHITE);
        th.setFont(ConstantesVisuales.FUENTE_NEGRITA);
        th.setPreferredSize(new Dimension(0, 40));
        
        // Diseño alternado
        tabla.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                                                          boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? ConstantesVisuales.COLOR_FONDO_PRINCIPAL : Color.WHITE);
                }
                return c;
            }
        });
        
        return tabla;
    }

    private javax.swing.border.Border crearBordeTabla() {
        return BorderFactory.createCompoundBorder(
            new RoundedBorder(ConstantesVisuales.RADIO_BORDE_MEDIO, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(ConstantesVisuales.PADDING_XXS, 
                                           ConstantesVisuales.PADDING_XXS, 
                                           ConstantesVisuales.PADDING_XXS, 
                                           ConstantesVisuales.PADDING_XXS)
        );
    }

    private JPanel crearPanelAcciones() {
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, ConstantesVisuales.PADDING_MD, 0));
        acciones.setBackground(COLOR_FONDO);
        
        btnRegistrar = new StyledButton("➕ Realizar Contratación", StyledButton.TipoBoton.PRIMARIO);
        btnRegistrar.setToolTipText("Realizar nueva contratación (Ctrl+N)");
        
        btnDarBaja = new StyledButton("🗑️ Dar de baja", StyledButton.TipoBoton.PELIGRO);
        btnDarBaja.setToolTipText("Dar de baja ayudante seleccionado (Supr)");
        
        btnCrearProyecto = new StyledButton("📋 Crear Proyecto", StyledButton.TipoBoton.EXITO);
        btnCrearProyecto.setToolTipText("Crear nuevo proyecto de investigación (Ctrl+P)");
        
        acciones.add(btnRegistrar);
        acciones.add(btnDarBaja);
        acciones.add(btnCrearProyecto);
        
        return acciones;
    }

    public void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean confirmarAccion(String mensaje) {
        int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Confirmación", JOptionPane.YES_NO_OPTION);
        return opcion == JOptionPane.YES_OPTION;
    }

    public void setControlador(ControladorDirector controlador) {
        this.controlador = controlador;
    }
}