package view;

import controller.ControladorDirector;
import model.Estudiante;
import model.ResultadoOperacion;
import view.componentes.PlaceholderTextField;
import view.componentes.StyledButton;
import view.componentes.RoundedBorder;

import javax.swing.*;
import java.awt.*;

/**
 * Diálogo de registro de ayudante con búsqueda de estudiante y captura de horas/meses contratados.
 * Formulario siempre visible con scrollbar. Si no se encuentra, abre una ventana emergente separada.
 */
public class DialogoFormularioAyudante extends JDialog {
    private final ControladorDirector controlador;

    private PlaceholderTextField txtBusqueda;
    private JLabel lblEstadoBusqueda;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtCodigo;
    private JTextField txtCedula;
    private JTextField txtCorreo;
    private JTextField txtCarrera;
    private JTextField txtNivel;
    private JTextField txtIra;
    private JTextField txtHoras;
    private JTextField txtMeses;
    private StyledButton btnBuscar;
    private StyledButton btnGuardar;
    private StyledButton btnCancelar;

    private Estudiante estudianteSeleccionado;
    private boolean guardado;

    public DialogoFormularioAyudante(Frame owner, ControladorDirector controlador) {
        super(owner, "Registrar Ayudante", true);
        this.controlador = controlador;
        this.guardado = false;
        initUI();
    }

    private void initUI() {
        setSize(520, 640);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(0, 12));

        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        content.add(crearPanelBusqueda(), BorderLayout.NORTH);
        content.add(crearFormulario(), BorderLayout.CENTER);
        content.add(crearBotonera(), BorderLayout.SOUTH);

        // Agregar scroll al contenido principal
        add(new JScrollPane(content), BorderLayout.CENTER);
        configurarEventos();
    }

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new BorderLayout(8, 6));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, new Color(225, 232, 237), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JLabel lbl = new JLabel("Buscar estudiante (código o cédula)");
        lbl.setFont(new Font("Arial", Font.BOLD, 12));

        JPanel fila = new JPanel(new BorderLayout(6, 0));
        txtBusqueda = new PlaceholderTextField("Ej: 202111079 o 1760107340");
        btnBuscar = new StyledButton("Buscar", StyledButton.TipoBoton.PRIMARIO);
        fila.add(txtBusqueda, BorderLayout.CENTER);
        fila.add(btnBuscar, BorderLayout.EAST);

        lblEstadoBusqueda = new JLabel(" ");
        lblEstadoBusqueda.setForeground(new Color(90, 99, 110));
        lblEstadoBusqueda.setFont(new Font("Arial", Font.PLAIN, 11));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(fila, BorderLayout.CENTER);
        panel.add(lblEstadoBusqueda, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearFormulario() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;

        addField(form, gbc, "Nombres", txtNombres = buildReadOnly());
        addField(form, gbc, "Apellidos", txtApellidos = buildReadOnly());
        addField(form, gbc, "Código", txtCodigo = buildReadOnly());
        addField(form, gbc, "Cédula", txtCedula = buildReadOnly());
        addField(form, gbc, "Correo", txtCorreo = buildReadOnly());
        addField(form, gbc, "Carrera", txtCarrera = buildReadOnly());
        addField(form, gbc, "Nivel", txtNivel = buildReadOnly());
        addField(form, gbc, "IRA", txtIra = buildReadOnly());
        addField(form, gbc, "Horas semanales (máx 32)", txtHoras = new JTextField());
        addField(form, gbc, "Meses Contratados", txtMeses = new JTextField());

        return form;
    }

    private JPanel crearBotonera() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        btnGuardar = new StyledButton("Guardar", StyledButton.TipoBoton.EXITO);
        btnCancelar = new StyledButton("Cancelar", StyledButton.TipoBoton.SECUNDARIO);
        panel.add(btnGuardar);
        panel.add(btnCancelar);
        return panel;
    }

    private void addField(JPanel form, GridBagConstraints gbc, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.weightx = 0;
        form.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        form.add(field, gbc);
        gbc.gridy++;
    }

    private JTextField buildReadOnly() {
        JTextField tf = new JTextField();
        tf.setEditable(false);
        tf.setBackground(new Color(245, 247, 250));
        return tf;
    }

    private void configurarEventos() {
        // Solo buscar cuando se presiona Enter en el campo o se hace clic en el botón
        btnBuscar.addActionListener(e -> buscar());
        txtBusqueda.addActionListener(e -> buscar());

        btnCancelar.addActionListener(e -> {
            guardado = false;
            dispose();
        });

        btnGuardar.addActionListener(e -> guardarConValidacion());
    }

    private void buscar() {
        String criterio = txtBusqueda.getTextReal().trim();
        if (criterio.isEmpty()) {
            lblEstadoBusqueda.setText(" ");
            limpiarCampos();
            estudianteSeleccionado = null;
            return;
        }
        
        Estudiante est = controlador.buscarEstudiante(criterio);
        if (est == null) {
            lblEstadoBusqueda.setText("✗ Estudiante no registrado");
            lblEstadoBusqueda.setForeground(new Color(192, 57, 43));
            limpiarCampos();
            
            // Abrir ventana emergente de "No registrado"
            DialogoEstudianteNoRegistrado dialogo = new DialogoEstudianteNoRegistrado(
                (Frame) SwingUtilities.getWindowAncestor(this),
                controlador,
                "ayudante"
            );
            dialogo.setVisible(true);
            
            // Si se registró el estudiante en la ventana emergente, intentar buscarlo nuevamente
            Estudiante estudianteNuevo = controlador.buscarEstudiante(criterio);
            if (estudianteNuevo != null) {
                est = estudianteNuevo;
                lblEstadoBusqueda.setText("✓ Estudiante registrado");
                lblEstadoBusqueda.setForeground(new Color(39, 174, 96));
                estudianteSeleccionado = est;
                poblarDatos(est);
            }
            return;
        }
        
        // Verificar elegibilidad
        if (!est.esElegibleParaAyudantia()) {
            lblEstadoBusqueda.setText("✗ Estudiante no cumple requisitos (IRA ≥ 24, Nivel ≥ 3)");
            lblEstadoBusqueda.setForeground(new Color(192, 57, 43));
            estudianteSeleccionado = null;
            poblarDatos(est);
            return;
        }
        
        lblEstadoBusqueda.setText("✓ Estudiante registrado: " + est.getNombresCompletos());
        lblEstadoBusqueda.setForeground(new Color(39, 174, 96));
        estudianteSeleccionado = est;
        poblarDatos(est);
    }

    private void poblarDatos(Estudiante est) {
        txtNombres.setText(est.getNombres());
        txtApellidos.setText(est.getApellidos());
        txtCodigo.setText(est.getCodigoUnico());
        txtCedula.setText(est.getCedula());
        txtCorreo.setText(est.getCorreoInstitucional());
        txtCarrera.setText(est.getCarrera());
        txtNivel.setText(String.valueOf(est.getNivel()));
        txtIra.setText(String.format("%.2f", est.getIRA()));
    }

    private void limpiarCampos() {
        txtNombres.setText("");
        txtApellidos.setText("");
        txtCodigo.setText("");
        txtCedula.setText("");
        txtCorreo.setText("");
        txtCarrera.setText("");
        txtNivel.setText("");
        txtIra.setText("");
        txtHoras.setText("");
        txtMeses.setText("");
    }

    private void guardar() {
        if (estudianteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Primero busque y seleccione un estudiante elegible.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validación mejorada
        java.util.List<String> errores = new java.util.ArrayList<>();
        
        // Validar horas
        int horas = 0;
        try {
            horas = Integer.parseInt(txtHoras.getText().trim());
            if (horas <= 0 || horas > 32) {
                errores.add("Las horas semanales deben estar entre 1 y 32");
            }
        } catch (NumberFormatException e) {
            errores.add("Horas semanales inválidas (debe ser un número entero)");
        }
        
        // Validar meses
        int meses = 0;
        try {
            meses = Integer.parseInt(txtMeses.getText().trim());
            if (meses <= 0 || meses > 12) {
                errores.add("Los meses deben estar entre 1 y 12");
            }
        } catch (NumberFormatException e) {
            errores.add("Meses inválidos (debe ser un número entero)");
        }
        
        if (!errores.isEmpty()) {
            StringBuilder mensaje = new StringBuilder("<html><body><b>Errores de validación:</b><br><br>");
            for (String error : errores) {
                mensaje.append("• ").append(error).append("<br>");
            }
            mensaje.append("</body></html>");
            JOptionPane.showMessageDialog(this, mensaje.toString(), "Errores de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ResultadoOperacion res = controlador.registrarAyudante(estudianteSeleccionado.getCodigoUnico(), horas, meses);
        if (res.esExitoso()) {
            JOptionPane.showMessageDialog(this, res.getMensaje(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            guardado = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, res.getMensaje(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void guardarConValidacion() {
        if (estudianteSeleccionado == null) {
            mostrarErrorValidacion("Primero busque y seleccione un estudiante elegible.");
            return;
        }
        
        java.util.List<String> errores = new java.util.ArrayList<>();
        
        // Validar horas
        try {
            int horas = Integer.parseInt(txtHoras.getText().trim());
            if (horas <= 0 || horas > 32) {
                errores.add("Las horas semanales deben estar entre 1 y 32");
            }
        } catch (NumberFormatException e) {
            errores.add("Horas semanales inválidas (debe ser un número entero)");
        }
        
        // Validar meses
        try {
            int meses = Integer.parseInt(txtMeses.getText().trim());
            if (meses <= 0 || meses > 12) {
                errores.add("Los meses deben estar entre 1 y 12");
            }
        } catch (NumberFormatException e) {
            errores.add("Meses inválidos (debe ser un número entero)");
        }
        
        if (!errores.isEmpty()) {
            StringBuilder mensaje = new StringBuilder("Errores de validación:\n\n");
            for (String error : errores) {
                mensaje.append("• ").append(error).append("\n");
            }
            JOptionPane.showMessageDialog(this, mensaje.toString(), "Errores de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Si todo válido, guardar
        guardar();
    }
    
    private void mostrarErrorValidacion(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Validación", JOptionPane.WARNING_MESSAGE);
    }

    public boolean seGuardo() {
        return guardado;
    }
}