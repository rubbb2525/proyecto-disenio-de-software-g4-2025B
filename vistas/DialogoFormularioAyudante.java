package vistas;

import controladores.ControladorDirector;
import models.Estudiante;
import models.dao.EstudianteDAO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogoFormularioAyudante extends JDialog {
    private JTextField txtBusqueda;
    private JButton btnBuscar;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtCodigo;
    private JTextField txtCedula;
    private JTextField txtCorreo;
    private JComboBox<String> comboCarrera;
    private JSpinner spnNivel;
    private JTextField txtPromedio;
    private JTextField txtHorasSemanales;
    private JTextField txtSalarioHora;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JLabel lblEstadoBusqueda;
    private ControladorDirector controlador;
    private EstudianteDAO estudianteDAO;
    private Map<String, Object> datos;
    private boolean guardado;

    public DialogoFormularioAyudante(Frame parent, ControladorDirector controlador, EstudianteDAO estudianteDAO) {
        super(parent, "Registrar Ayudante", true);
        this.controlador = controlador;
        this.estudianteDAO = estudianteDAO;
        this.guardado = false;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setSize(550, 700);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        // Panel principal con GridBagLayout
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int fila = 0;

        // ========== SECCIÓN DE BÚSQUEDA ==========
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout(5, 5));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Buscar Estudiante"));
        
        JPanel panelBusquedaInterna = new JPanel(new GridBagLayout());
        GridBagConstraints gbcBusqueda = new GridBagConstraints();
        gbcBusqueda.fill = GridBagConstraints.HORIZONTAL;
        gbcBusqueda.insets = new Insets(5, 5, 5, 5);
        
        // Etiqueta y campo de búsqueda
        gbcBusqueda.gridx = 0;
        gbcBusqueda.gridy = 0;
        panelBusquedaInterna.add(new JLabel("Código o Cédula:"), gbcBusqueda);
        
        gbcBusqueda.gridx = 1;
        gbcBusqueda.weightx = 1.0;
        txtBusqueda = new JTextField(20);
        panelBusquedaInterna.add(txtBusqueda, gbcBusqueda);
        
        gbcBusqueda.gridx = 2;
        gbcBusqueda.weightx = 0;
        btnBuscar = new JButton("Buscar");
        panelBusquedaInterna.add(btnBuscar, gbcBusqueda);
        
        // Estado de búsqueda
        gbcBusqueda.gridx = 0;
        gbcBusqueda.gridy = 1;
        gbcBusqueda.gridwidth = 3;
        lblEstadoBusqueda = new JLabel(" ");
        lblEstadoBusqueda.setForeground(new Color(0, 100, 0));
        panelBusquedaInterna.add(lblEstadoBusqueda, gbcBusqueda);
        
        panelBusqueda.add(panelBusquedaInterna, BorderLayout.CENTER);
        
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        panelPrincipal.add(panelBusqueda, gbc);
        
        gbc.gridwidth = 1;
        gbc.weightx = 0;

        // ========== SECCIÓN DE DATOS ==========
        // Nombres
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelPrincipal.add(new JLabel("Nombres:"), gbc);
        gbc.gridx = 1;
        txtNombres = new JTextField(20);
        txtNombres.setEditable(false);
        panelPrincipal.add(txtNombres, gbc);

        // Apellidos
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelPrincipal.add(new JLabel("Apellidos:"), gbc);
        gbc.gridx = 1;
        txtApellidos = new JTextField(20);
        panelPrincipal.add(txtApellidos, gbc);

        // Código Estudiante
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelPrincipal.add(new JLabel("Código Estudiante:"), gbc);
        gbc.gridx = 1;
        txtCodigo = new JTextField(20);
        txtCodigo.setEditable(false);
        panelPrincipal.add(txtCodigo, gbc);

        // Cédula
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelPrincipal.add(new JLabel("Cédula:"), gbc);
        gbc.gridx = 1;
        txtCedula = new JTextField(20);
        txtCedula.setEditable(false);
        panelPrincipal.add(txtCedula, gbc);

        // Correo Institucional
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelPrincipal.add(new JLabel("Correo Institucional:"), gbc);
        gbc.gridx = 1;
        txtCorreo = new JTextField(20);
        panelPrincipal.add(txtCorreo, gbc);

        // Carrera
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelPrincipal.add(new JLabel("Carrera:"), gbc);
        gbc.gridx = 1;
        String[] carreras = {
            "(RRA20) SOFTWARE",
            "(RRA20) COMPUTACIÓN"
        };
        comboCarrera = new JComboBox<>(carreras);
        comboCarrera.setEditable(false);
        panelPrincipal.add(comboCarrera, gbc);

        // Nivel
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelPrincipal.add(new JLabel("Nivel:"), gbc);
        gbc.gridx = 1;
        SpinnerNumberModel modeloNivel = new SpinnerNumberModel(1, 1, 10, 1);
        spnNivel = new JSpinner(modeloNivel);
        panelPrincipal.add(spnNivel, gbc);

        // Promedio
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelPrincipal.add(new JLabel("Promedio:"), gbc);
        gbc.gridx = 1;
        txtPromedio = new JTextField(20);
        panelPrincipal.add(txtPromedio, gbc);

        // Horas Semanales
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelPrincipal.add(new JLabel("Horas Semanales:"), gbc);
        gbc.gridx = 1;
        txtHorasSemanales = new JTextField(20);
        panelPrincipal.add(txtHorasSemanales, gbc);

        // Salario por Hora
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelPrincipal.add(new JLabel("Salario por Hora:"), gbc);
        gbc.gridx = 1;
        txtSalarioHora = new JTextField(20);
        panelPrincipal.add(txtSalarioHora, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        // Agregar paneles al diálogo
        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        configurarEventos();
    }

    private void configurarEventos() {
        // Búsqueda automática
        txtBusqueda.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                realizarBusqueda();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                realizarBusqueda();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                realizarBusqueda();
            }
        });

        // Botón de búsqueda
        btnBuscar.addActionListener(e -> realizarBusqueda());

        btnGuardar.addActionListener(e -> {
            List<String> errores = validarCampos();
            if (errores.isEmpty()) {
                datos = obtenerDatos();
                guardado = true;
                dispose();
            } else {
                mostrarErrores(errores);
            }
        });

        btnCancelar.addActionListener(e -> {
            guardado = false;
            dispose();
        });
    }

    /**
     * Realiza la búsqueda de estudiantes
     */
    private void realizarBusqueda() {
        String criterio = txtBusqueda.getText().trim();
        
        if (criterio.isEmpty()) {
            limpiarDatosEstudiante();
            lblEstadoBusqueda.setText(" ");
            return;
        }

        // Buscar estudiante vía controlador (no acceder DAO desde la vista)
        Estudiante estudiante = controlador != null 
            ? controlador.buscarEstudiantePorCodigoOCedula(criterio)
            : null;
        
        if (estudiante != null) {
            // Cargar datos encontrados
            cargarDatosEstudiante(estudiante);
            lblEstadoBusqueda.setText("✓ Estudiante encontrado: " + estudiante.getNombres());
            lblEstadoBusqueda.setForeground(new Color(0, 150, 0));
        } else {
            limpiarDatosEstudiante();
            lblEstadoBusqueda.setText("✗ Estudiante no encontrado");
            lblEstadoBusqueda.setForeground(new Color(200, 0, 0));
        }
    }

    /**
     * Carga los datos del estudiante encontrado
     */
    private void cargarDatosEstudiante(Estudiante estudiante) {
        txtCodigo.setText(estudiante.getCodigoUnico());
        txtCedula.setText(estudiante.getCedula());
        txtNombres.setText(estudiante.getNombres());
        
        // Seleccionar carrera
        comboCarrera.setSelectedItem(estudiante.getCarrera());
    }

    /**
     * Limpia los datos del estudiante
     */
    private void limpiarDatosEstudiante() {
        txtCodigo.setText("");
        txtCedula.setText("");
        txtNombres.setText("");
        comboCarrera.setSelectedIndex(0);
    }

    public void mostrar() {
        setVisible(true);
    }

    public Map<String, Object> obtenerDatos() {
        if (!guardado) {
            return null;
        }

        Map<String, Object> datos = new HashMap<>();
        try {
            datos.put("numeroUnico", txtCodigo.getText().trim());
            datos.put("codigoEstudiante", txtCodigo.getText().trim());
            datos.put("cedula", txtCedula.getText().trim());
            datos.put("nombres", txtNombres.getText().trim());
            datos.put("apellidos", txtApellidos.getText().trim());
            // Normalizar clave de correo para el controlador
            datos.put("correoInstitucional", txtCorreo.getText().trim());
            datos.put("carrera", (String) comboCarrera.getSelectedItem());
            datos.put("nivel", (Integer) spnNivel.getValue());
            datos.put("promedio", Float.parseFloat(txtPromedio.getText().trim()));
            datos.put("horasSemanales", Integer.parseInt(txtHorasSemanales.getText().trim()));
            datos.put("salarioPorHora", Double.parseDouble(txtSalarioHora.getText().trim()));
        } catch (NumberFormatException e) {
            return null;
        }

        return datos;
    }

    public List<String> validarCampos() {
        List<String> errores = new ArrayList<>();

        if (txtCodigo.getText().trim().isEmpty()) {
            errores.add("Debe buscar y seleccionar un estudiante primero");
        }

        if (txtNombres.getText().trim().isEmpty()) {
            errores.add("Los nombres son obligatorios");
        }

        if (txtApellidos.getText().trim().isEmpty()) {
            errores.add("Los apellidos son obligatorios");
        }

        String correo = txtCorreo.getText().trim();
        if (correo.isEmpty()) {
            errores.add("El correo institucional es obligatorio");
        } else if (!correo.matches("^[a-zA-Z0-9._%+-]+@epn\\.edu\\.ec$")) {
            errores.add("Correo institucional inválido (debe terminar en @epn.edu.ec)");
        }

        try {
            float promedio = Float.parseFloat(txtPromedio.getText().trim());
            if (promedio < 0 || promedio > 10) {
                errores.add("El promedio debe estar entre 0 y 10");
            }
        } catch (NumberFormatException e) {
            errores.add("Promedio inválido");
        }

        try {
            int horas = Integer.parseInt(txtHorasSemanales.getText().trim());
            if (horas <= 0 || horas > 40) {
                errores.add("Las horas semanales deben estar entre 1 y 40");
            }
        } catch (NumberFormatException e) {
            errores.add("Horas semanales inválidas");
        }

        try {
            double salario = Double.parseDouble(txtSalarioHora.getText().trim());
            if (salario <= 0) {
                errores.add("El salario por hora debe ser mayor a 0");
            }
        } catch (NumberFormatException e) {
            errores.add("Salario por hora inválido");
        }

        return errores;
    }

    public void mostrarErrores(List<String> errores) {
        StringBuilder mensaje = new StringBuilder("Errores encontrados:\n\n");
        for (String error : errores) {
            mensaje.append("• ").append(error).append("\n");
        }
        JOptionPane.showMessageDialog(this, mensaje.toString(), "Errores de Validación", JOptionPane.ERROR_MESSAGE);
    }

    public void limpiarCampos() {
        txtBusqueda.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtCodigo.setText("");
        txtCedula.setText("");
        txtCorreo.setText("");
        txtPromedio.setText("");
        txtHorasSemanales.setText("");
        txtSalarioHora.setText("");
        spnNivel.setValue(1);
        comboCarrera.setSelectedIndex(0);
        lblEstadoBusqueda.setText(" ");
    }
    
    public boolean isGuardado() {
        return guardado;
    }
}
