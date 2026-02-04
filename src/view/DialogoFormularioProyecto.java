package view;

import controller.ControladorDirector;
import model.ResultadoOperacion;
import model.TipoProyecto;
import model.CategoriaProyecto;
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
    private JSpinner spnFechaInicio;
    private JSpinner spnFechaFin;
    private JComboBox<CategoriaProyecto> cmbCategoriaProyecto;
    private JComboBox<TipoProyecto> cmbTipoProyecto;
    private JSpinner spnAyudantesPlanificados;
    private JSpinner spnTecnicosPlanificados;
    private JSpinner spnAsistentesPlanificados;
    
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
        setSize(600, 550);
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

        JLabel lblTitulo = new JLabel("Nuevo Proyecto");
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

        // Fecha de inicio con JSpinner
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        JLabel lblFechaInicio = new JLabel("Fecha de Inicio *");
        lblFechaInicio.setFont(new Font("Arial", Font.BOLD, 12));
        lblFechaInicio.setForeground(COLOR_TEXTO);
        form.add(lblFechaInicio, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        spnFechaInicio = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorInicio = new JSpinner.DateEditor(spnFechaInicio, "yyyy-MM-dd");
        spnFechaInicio.setEditor(editorInicio);
        spnFechaInicio.setFont(new Font("Arial", Font.PLAIN, 12));
        spnFechaInicio.setPreferredSize(new Dimension(300, 32));
        form.add(spnFechaInicio, gbc);

        // Fecha de fin con JSpinner
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        JLabel lblFechaFin = new JLabel("Fecha de Fin *");
        lblFechaFin.setFont(new Font("Arial", Font.BOLD, 12));
        lblFechaFin.setForeground(COLOR_TEXTO);
        form.add(lblFechaFin, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        spnFechaFin = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorFin = new JSpinner.DateEditor(spnFechaFin, "yyyy-MM-dd");
        spnFechaFin.setEditor(editorFin);
        spnFechaFin.setFont(new Font("Arial", Font.PLAIN, 12));
        spnFechaFin.setPreferredSize(new Dimension(300, 32));
        form.add(spnFechaFin, gbc);

        gbc.gridx = 0;
        gbc.weightx = 0;

        // Categoría del proyecto (niv. superior)
        gbc.gridy++;
        JLabel lblCategoria = new JLabel("Categoría de Proyecto *");
        lblCategoria.setFont(new Font("Arial", Font.BOLD, 12));
        lblCategoria.setForeground(COLOR_TEXTO);
        form.add(lblCategoria, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cmbCategoriaProyecto = new JComboBox<>(CategoriaProyecto.values());
        cmbCategoriaProyecto.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbCategoriaProyecto.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                                                         int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof CategoriaProyecto) {
                    setText(((CategoriaProyecto) value).getDescripcion());
                }
                return this;
            }
        });
        form.add(cmbCategoriaProyecto, gbc);

        gbc.gridx = 0;
        gbc.weightx = 0;

        // Tipo de proyecto (subtipo)
        gbc.gridy++;
        JLabel lblTipo = new JLabel("Tipo de Proyecto *");
        lblTipo.setFont(new Font("Arial", Font.BOLD, 12));
        lblTipo.setForeground(COLOR_TEXTO);
        form.add(lblTipo, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        // Inicialmente listar todos los tipos de proyecto
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

        // Sincronizar seleccion de categoria -> tipos disponibles (ver configurarEventos)

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

        // Técnicos planificados
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        JLabel lblTecnicos = new JLabel("Técnicos Planificados *");
        lblTecnicos.setFont(new Font("Arial", Font.BOLD, 12));
        lblTecnicos.setForeground(COLOR_TEXTO);
        form.add(lblTecnicos, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        SpinnerNumberModel spinnerTecnicosModel = new SpinnerNumberModel(0, 0, 20, 1);
        spnTecnicosPlanificados = new JSpinner(spinnerTecnicosModel);
        spnTecnicosPlanificados.setFont(new Font("Arial", Font.PLAIN, 12));
        ((JSpinner.DefaultEditor) spnTecnicosPlanificados.getEditor()).getTextField().setColumns(5);
        form.add(spnTecnicosPlanificados, gbc);

        // Asistentes planificados
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        JLabel lblAsistentes = new JLabel("Asistentes Planificados *");
        lblAsistentes.setFont(new Font("Arial", Font.BOLD, 12));
        lblAsistentes.setForeground(COLOR_TEXTO);
        form.add(lblAsistentes, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        SpinnerNumberModel spinnerAsistentesModel = new SpinnerNumberModel(0, 0, 20, 1);
        spnAsistentesPlanificados = new JSpinner(spinnerAsistentesModel);
        spnAsistentesPlanificados.setFont(new Font("Arial", Font.PLAIN, 12));
        ((JSpinner.DefaultEditor) spnAsistentesPlanificados.getEditor()).getTextField().setColumns(5);
        form.add(spnAsistentesPlanificados, gbc);

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

        // Cuando cambia la categoría, actualizar los tipos disponibles
        cmbCategoriaProyecto.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                CategoriaProyecto cat = (CategoriaProyecto) cmbCategoriaProyecto.getSelectedItem();
                DefaultComboBoxModel<TipoProyecto> model;
                if (cat == CategoriaProyecto.INVESTIGACION) {
                    model = new DefaultComboBoxModel<>(new TipoProyecto[] {
                        TipoProyecto.INTERNO,
                        TipoProyecto.SEMILLA,
                        TipoProyecto.GRUPAL,
                        TipoProyecto.MULTIDISCIPLINARIO
                    });
                    cmbTipoProyecto.setModel(model);
                    cmbTipoProyecto.setEnabled(true);
                } else if (cat == CategoriaProyecto.VINCULACION) {
                    model = new DefaultComboBoxModel<>(new TipoProyecto[] {
                        TipoProyecto.VINCULACION_CON_FINANCIAMIENTO
                    });
                    cmbTipoProyecto.setModel(model);
                    cmbTipoProyecto.setEnabled(false);
                } else { // TRANSFERENCIA_TECNOLOGICA
                    model = new DefaultComboBoxModel<>(new TipoProyecto[] {
                        TipoProyecto.TRANSFERENCIA_TECNOLOGICA
                    });
                    cmbTipoProyecto.setModel(model);
                    cmbTipoProyecto.setEnabled(false);
                }
            }
        });

        // Inicializar la selección por defecto
        cmbCategoriaProyecto.setSelectedItem(CategoriaProyecto.INVESTIGACION);

    }

    private void guardarProyecto() {
        // Obtener valores de los campos
        String codigoProyecto = txtCodigoProyecto.getText().trim();
        String nombreProyecto = txtNombreProyecto.getText().trim();
        Date fechaInicio = (Date) spnFechaInicio.getValue();
        Date fechaFin = (Date) spnFechaFin.getValue();
        TipoProyecto tipoProyecto = (TipoProyecto) cmbTipoProyecto.getSelectedItem();
        int ayudantesPlanificados = (Integer) spnAyudantesPlanificados.getValue();
        int tecnicosPlanificados = (Integer) spnTecnicosPlanificados.getValue();
        int asistentesPlanificados = (Integer) spnAsistentesPlanificados.getValue();

        // Validaciones de formato en la vista
        if (codigoProyecto.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El código del proyecto es obligatorio", 
                "Error de Validacion", 
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

        // Validar que la fecha de inicio sea anterior a la fecha de fin
        if (fechaInicio.after(fechaFin)) {
            JOptionPane.showMessageDialog(this, 
                "La fecha de inicio debe ser anterior a la fecha de fin", 
                "Error de Validacion", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Deshabilitar botón mientras se procesa
        btnGuardar.setEnabled(false);
        btnGuardar.setText("Creando...");

        // Invocar al controlador (sin descripción)
        ResultadoOperacion resultado = controlador.crearProyecto(
            codigoProyecto,
            nombreProyecto,
            "",  // Descripción vacía
            fechaInicio,
            fechaFin,
            tipoProyecto,
            ayudantesPlanificados,
            tecnicosPlanificados,
            asistentesPlanificados
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