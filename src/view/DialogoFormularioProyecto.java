package view;

import controller.ControladorDirector;
import model.ResultadoOperacion;
import model.TipoProyecto;
import view.componentes.PlaceholderTextField;
import view.componentes.StyledButton;
import view.componentes.RoundedBorder;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Diálogo para crear un nuevo proyecto de investigación
 * 
 * RESPONSABILIDAD ÚNICA:
 * - Capturar los datos del proyecto
 * - Validar formato de campos
 * - Invocar al controlador para crear el proyecto
 */
public class DialogoFormularioProyecto extends JDialog {
    private static final Color COLOR_FONDO = new Color(245, 247, 250);
    private static final Color COLOR_TARJETA = Color.WHITE;
    private static final Color COLOR_PRIMARIO = new Color(0, 61, 165);
    private static final Color COLOR_TEXTO = new Color(44, 62, 80);
    private static final Color COLOR_BORDE = new Color(225, 232, 237);

    private final ControladorDirector controlador;

    private PlaceholderTextField txtCodigoProyecto;
    private PlaceholderTextField txtNombreProyecto;
    private JTextArea txtDescripcion;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JComboBox<TipoProyecto> cmbTipoProyecto;
    private JSpinner spnAyudantesPlanificados;
    
    private StyledButton btnGuardar;
    private StyledButton btnCancelar;

    private boolean guardado;
    private SimpleDateFormat dateFormat;

    public DialogoFormularioProyecto(Frame owner, ControladorDirector controlador) {
        super(owner, "Crear Nuevo Proyecto", true);
        this.controlador = controlador;
        this.guardado = false;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.dateFormat.setLenient(false);
        initUI();
    }

    private void initUI() {
        setSize(600, 700);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(0, 12));
        setResizable(false);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(COLOR_FONDO);
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        content.add(crearHeader(), BorderLayout.NORTH);
        content.add(crearFormulario(), BorderLayout.CENTER);
        content.add(crearBotonera(), BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);
        configurarEventos();
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_PRIMARIO);
        header.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JLabel lblTitulo = new JLabel("Nuevo Proyecto de Investigación");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        header.add(lblTitulo, BorderLayout.WEST);

        return header;
    }

    private JPanel crearFormulario() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(COLOR_TARJETA);
        form.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.weightx = 0;

        // Código del proyecto
        txtCodigoProyecto = new PlaceholderTextField("Ej: PROY-2024-001");
        addField(form, gbc, "Código del Proyecto *", txtCodigoProyecto);

        // Nombre del proyecto
        txtNombreProyecto = new PlaceholderTextField("Nombre descriptivo del proyecto");
        addField(form, gbc, "Nombre del Proyecto *", txtNombreProyecto);

        // Descripción
        gbc.gridy++;
        JLabel lblDesc = new JLabel("Descripción");
        lblDesc.setFont(new Font("Arial", Font.BOLD, 12));
        lblDesc.setForeground(COLOR_TEXTO);
        form.add(lblDesc, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtDescripcion = new JTextArea(4, 30);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setPreferredSize(new Dimension(300, 80));
        form.add(scrollDesc, gbc);

        gbc.gridx = 0;
        gbc.weightx = 0;

        // Fecha de inicio
        txtFechaInicio = new JTextField(15);
        txtFechaInicio.setFont(new Font("Arial", Font.PLAIN, 12));
        addField(form, gbc, "Fecha de Inicio * (AAAA-MM-DD)", txtFechaInicio);

        // Fecha de fin
        txtFechaFin = new JTextField(15);
        txtFechaFin.setFont(new Font("Arial", Font.PLAIN, 12));
        addField(form, gbc, "Fecha de Fin * (AAAA-MM-DD)", txtFechaFin);

        // Tipo de proyecto
        gbc.gridy++;
        JLabel lblTipo = new JLabel("Tipo de Proyecto *");
        lblTipo.setFont(new Font("Arial", Font.BOLD, 12));
        lblTipo.setForeground(COLOR_TEXTO);
        form.add(lblTipo, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cmbTipoProyecto = new JComboBox<>(TipoProyecto.values());
        cmbTipoProyecto.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbTipoProyecto.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                                                         int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TipoProyecto) {
                    setText(((TipoProyecto) value).getDescripcion());
                }
                return this;
            }
        });
        form.add(cmbTipoProyecto, gbc);

        gbc.gridx = 0;
        gbc.weightx = 0;

        // Ayudantes planificados
        gbc.gridy++;
        JLabel lblAyudantes = new JLabel("Ayudantes Planificados *");
        lblAyudantes.setFont(new Font("Arial", Font.BOLD, 12));
        lblAyudantes.setForeground(COLOR_TEXTO);
        form.add(lblAyudantes, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(3, 0, 20, 1);
        spnAyudantesPlanificados = new JSpinner(spinnerModel);
        spnAyudantesPlanificados.setFont(new Font("Arial", Font.PLAIN, 12));
        ((JSpinner.DefaultEditor) spnAyudantesPlanificados.getEditor()).getTextField().setColumns(5);
        form.add(spnAyudantesPlanificados, gbc);

        // Nota de campos obligatorios
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JLabel lblNota = new JLabel("* Campos obligatorios");
        lblNota.setFont(new Font("Arial", Font.ITALIC, 11));
        lblNota.setForeground(new Color(127, 140, 141));
        form.add(lblNota, gbc);

        return form;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JTextField field) {
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setForeground(COLOR_TEXTO);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        field.setPreferredSize(new Dimension(300, 32));
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        panel.add(field, gbc);
    }

    private JPanel crearBotonera() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnCancelar = new StyledButton("Cancelar", StyledButton.TipoBoton.SECUNDARIO);
        btnGuardar = new StyledButton("Crear Proyecto", StyledButton.TipoBoton.PRIMARIO);

        panel.add(btnCancelar);
        panel.add(btnGuardar);

        return panel;
    }

    private void configurarEventos() {
        btnCancelar.addActionListener(e -> {
            guardado = false;
            dispose();
        });

        btnGuardar.addActionListener(e -> guardarProyecto());

        // Enter en los campos de texto ejecuta guardar
        KeyStroke enterKey = KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0);
        txtCodigoProyecto.registerKeyboardAction(
            e -> guardarProyecto(), 
            enterKey, 
            JComponent.WHEN_FOCUSED
        );
        txtNombreProyecto.registerKeyboardAction(
            e -> guardarProyecto(), 
            enterKey, 
            JComponent.WHEN_FOCUSED
        );
    }

    private void guardarProyecto() {
        // Obtener valores de los campos
        String codigoProyecto = txtCodigoProyecto.getText().trim();
        String nombreProyecto = txtNombreProyecto.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String fechaInicioStr = txtFechaInicio.getText().trim();
        String fechaFinStr = txtFechaFin.getText().trim();
        TipoProyecto tipoProyecto = (TipoProyecto) cmbTipoProyecto.getSelectedItem();
        int ayudantesPlanificados = (Integer) spnAyudantesPlanificados.getValue();

        // Validaciones de formato en la vista
        if (codigoProyecto.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El código del proyecto es obligatorio", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            txtCodigoProyecto.requestFocus();
            return;
        }

        if (nombreProyecto.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El nombre del proyecto es obligatorio", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            txtNombreProyecto.requestFocus();
            return;
        }

        // Validar y parsear fechas
        Date fechaInicio = null;
        Date fechaFin = null;

        try {
            fechaInicio = dateFormat.parse(fechaInicioStr);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Formato de fecha de inicio inválido. Use: AAAA-MM-DD", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            txtFechaInicio.requestFocus();
            return;
        }

        try {
            fechaFin = dateFormat.parse(fechaFinStr);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Formato de fecha de fin inválido. Use: AAAA-MM-DD", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            txtFechaFin.requestFocus();
            return;
        }

        // Deshabilitar botón mientras se procesa
        btnGuardar.setEnabled(false);
        btnGuardar.setText("Creando...");

        // Invocar al controlador
        ResultadoOperacion resultado = controlador.crearProyecto(
            codigoProyecto,
            nombreProyecto,
            descripcion,
            fechaInicio,
            fechaFin,
            tipoProyecto,
            ayudantesPlanificados
        );

        // Habilitar botón nuevamente
        btnGuardar.setEnabled(true);
        btnGuardar.setText("Crear Proyecto");

        // Procesar resultado
        if (resultado.esExitoso()) {
            JOptionPane.showMessageDialog(this, 
                resultado.getMensaje(), 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            guardado = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                resultado.getMensajeCompleto(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean seGuardo() {
        return guardado;
    }
}