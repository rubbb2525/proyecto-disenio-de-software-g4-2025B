package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import controller.ControladorDirector;
import model.Ayudante;
import model.ProyectoInvestigacion;
import view.componentes.StyledButton;
import view.componentes.IconManager;
import view.componentes.RoundedBorder;
import view.componentes.AdvancedTableModel;
import view.componentes.ToastMessage;
import view.componentes.PanelEstadistica;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Date;
import model.AsistenteInvestigacion;
import model.TecnicoInvestigacion;

/**
 * Ventana principal del Director
 */
public class VistaDirector extends JFrame {
    private static final Color COLOR_FONDO = new Color(245, 247, 250);
    private static final Color COLOR_TARJETA = Color.WHITE;
    private static final Color COLOR_PRIMARIO = new Color(0, 61, 165);
    private static final Color COLOR_TEXTO = new Color(44, 62, 80);
    private static final Color COLOR_BORDE = new Color(225, 232, 237);

    private JTable tablaAyudantes;
    private StyledButton btnRegistrar;
    private StyledButton btnDarBaja;
    private StyledButton btnCrearProyecto;
    private StyledButton btnRefrescar;
    private JLabel lblProyecto;
    private JLabel lblCuposDisponibles;
    private ControladorDirector controlador;
    private AdvancedTableModel modeloTabla;
    private JTextField campoBusqueda;
    private javax.swing.Timer timerRefresh;
    private PanelEstadistica panelTotalAyudantes;
    private PanelEstadistica panelCuposDisponibles;
    private PanelEstadistica panelHorasTotales;

    public VistaDirector(ControladorDirector controlador) {
        this.controlador = controlador;
        setTitle("Sistema Gestión Ayudantes - Director");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 680);
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
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_PRIMARIO);
        header.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        JLabel lblTitulo = new JLabel(" Director");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        lblTitulo.setIcon(IconManager.getInstance().getIcon("user.svg", 18));
        header.add(lblTitulo, BorderLayout.WEST);

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerRight.setOpaque(false);
        btnRefrescar = new StyledButton("Refrescar", StyledButton.TipoBoton.SECUNDARIO);
        btnRefrescar.setIcon(IconManager.getInstance().getIcon("refresh.svg", 16));
        btnRefrescar.setToolTipText("Actualizar datos (F5)");
        headerRight.add(btnRefrescar);
        header.add(headerRight, BorderLayout.EAST);

        // Paneles de estadísticas
        JPanel panelEstadisticas = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panelEstadisticas.setBackground(COLOR_FONDO);
        
        panelTotalAyudantes = new PanelEstadistica("Total Ayudantes", "0", new Color(52, 152, 219), "👥");
        panelCuposDisponibles = new PanelEstadistica("Cupos Disponibles", "0", new Color(46, 204, 113), "✓");
        panelHorasTotales = new PanelEstadistica("Horas Semanales", "0", new Color(155, 89, 182), "⏱");
        
        panelEstadisticas.add(panelTotalAyudantes);
        panelEstadisticas.add(panelCuposDisponibles);
        panelEstadisticas.add(panelHorasTotales);

        // Tarjeta proyecto
        JPanel cardProyecto = new JPanel();
        cardProyecto.setLayout(new BoxLayout(cardProyecto, BoxLayout.Y_AXIS));
        cardProyecto.setBackground(COLOR_TARJETA);
        cardProyecto.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));

        lblProyecto = new JLabel("Proyecto: Cargando...");
        lblProyecto.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
        lblProyecto.setForeground(COLOR_TEXTO);

        lblCuposDisponibles = new JLabel("Cupos disponibles: Cargando...");
        lblCuposDisponibles.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
        lblCuposDisponibles.setForeground(new Color(90, 99, 110));

        cardProyecto.add(lblProyecto);
        cardProyecto.add(Box.createVerticalStrut(6));
        cardProyecto.add(lblCuposDisponibles);

        // Tabla
        String[] columnas = {"Código", "Nombres", "Carrera", "Nivel", "IRA", "Horas", "Salario"};
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
        th.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        
        // Barra de búsqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout(8, 0));
        panelBusqueda.setBackground(COLOR_FONDO);
        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 11));
        campoBusqueda = new JTextField();
        campoBusqueda.setPreferredSize(new Dimension(250, 35));
        campoBusqueda.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 11));
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

        // Botonera
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        acciones.setBackground(COLOR_FONDO);
        btnRegistrar = new StyledButton("Realizar Contratación", StyledButton.TipoBoton.PRIMARIO);
        btnRegistrar.setIcon(IconManager.getInstance().getIcon("add.svg", 16));
        btnRegistrar.setToolTipText("Realizar nueva contratación (Ctrl+N)");
        btnDarBaja = new StyledButton("Dar de baja", StyledButton.TipoBoton.PELIGRO);
        btnDarBaja.setIcon(IconManager.getInstance().getIcon("delete.svg", 16));
        btnDarBaja.setToolTipText("Dar de baja ayudante seleccionado (Supr)");
        acciones.add(btnRegistrar);
        acciones.add(btnDarBaja);
        btnCrearProyecto = new StyledButton("Crear Proyecto", StyledButton.TipoBoton.EXITO);
        btnCrearProyecto.setIcon(IconManager.getInstance().getIcon("project.svg", 16));
        btnCrearProyecto.setToolTipText("Crear nuevo proyecto de investigación (Ctrl+P)");
        acciones.add(btnCrearProyecto);

        JPanel centro = new JPanel();
        centro.setBackground(COLOR_FONDO);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.add(panelEstadisticas);
        centro.add(Box.createVerticalStrut(10));
        centro.add(cardProyecto);
        centro.add(Box.createVerticalStrut(10));
        centro.add(panelBusqueda);
        centro.add(Box.createVerticalStrut(8));
        centro.add(scroll);
        centro.add(Box.createVerticalStrut(10));
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
        ProyectoInvestigacion proyecto = controlador.getProyecto();
        if (proyecto != null) {
            lblProyecto.setText("Proyecto: " + proyecto.getNombreProyecto());
            lblCuposDisponibles.setText("Cupos disponibles: " + proyecto.getCuposDisponibles());
            panelCuposDisponibles.actualizarValor(String.valueOf(proyecto.getCuposDisponibles()));
        }
        
        actualizarTabla();
    }

    private void actualizarTabla() {
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
                        String.format("$%.2f", a.getSalarioMensual())
                    };
                    filas.add(fila);
                }
            }
            panelHorasTotales.actualizarValor(String.valueOf(horasTotales));
            modeloTabla.establecerDatos(filas);
        }
        campoBusqueda.setText("");
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