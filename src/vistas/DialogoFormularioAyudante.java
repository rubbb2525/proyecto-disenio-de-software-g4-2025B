package vistas;

import controladores.ControladorDirector;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogoFormularioAyudante extends JDialog {
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtCodigo;
    private JTextField txtCorreo;
    private JComboBox<String> comboCarrera;
    private JSpinner spnNivel;
    private JTextField txtPromedio;
    private JTextField txtHorasSemanales;
    private JTextField txtSalarioHora;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private ControladorDirector controlador;
    private Map<String, Object> datos;
    private boolean guardado;

    public DialogoFormularioAyudante(Frame parent, ControladorDirector controlador) {
        super(parent, "Registrar Ayudante", true);
        this.controlador = controlador;
        this.guardado = false;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setSize(500, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        // Panel principal con GridBagLayout
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int fila = 0;

        // Nombres
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelPrincipal.add(new JLabel("Nombres:"), gbc);
        gbc.gridx = 1;
        txtNombres = new JTextField(20);
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
        panelPrincipal.add(txtCodigo, gbc);

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
            "Ingeniería en Sistemas",
            "Ingeniería en Software",
            "Ingeniería en Computación",
            "Ingeniería en Electrónica",
            "Ingeniería en Telecomunicaciones"
        };
        comboCarrera = new JComboBox<>(carreras);
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
            datos.put("nombres", txtNombres.getText().trim());
            datos.put("apellidos", txtApellidos.getText().trim());
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

        if (txtNombres.getText().trim().isEmpty()) {
            errores.add("Los nombres son obligatorios");
        }

        if (txtApellidos.getText().trim().isEmpty()) {
            errores.add("Los apellidos son obligatorios");
        }

        if (txtCodigo.getText().trim().isEmpty()) {
            errores.add("El código es obligatorio");
        }

        String correo = txtCorreo.getText().trim();
        if (!correo.matches("^[a-zA-Z0-9._%+-]+@epn\\.edu\\.ec$")) {
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
        txtNombres.setText("");
        txtApellidos.setText("");
        txtCodigo.setText("");
        txtCorreo.setText("");
        txtPromedio.setText("");
        txtHorasSemanales.setText("");
        txtSalarioHora.setText("");
        spnNivel.setValue(1);
        comboCarrera.setSelectedIndex(0);
    }
    
    public boolean isGuardado() {
        return guardado;
    }
}
