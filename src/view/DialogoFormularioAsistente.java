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
 * Diálogo de registro de Asistente de Investigación
 * Formulario siempre visible. Si no se encuentra el estudiante, abre una ventana emergente separada.
 */
public class DialogoFormularioAsistente extends JDialog {
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
    
    // CAMPOS ADICIONALES PARA ASISTENTE
    private JTextField txtHoras;
    private JTextField txtMeses;
    private StyledButton btnBuscar;
    private StyledButton btnGuardar;
    private StyledButton btnCancelar;

    private Estudiante estudianteSeleccionado;
    private boolean guardado;

    public DialogoFormularioAsistente(Frame owner, ControladorDirector controlador) {
        super(owner, "Registrar Asistente de Investigación", true);
        this.controlador = controlador;
        this.guardado = false;
        initUI();
    }

    private void initUI() {
        setSize(520, 740);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(0, 12));

        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        content.add(crearPanelBusqueda(), BorderLayout.NORTH);
        content.add(crearFormulario(), BorderLayout.CENTER);
        content.add(crearBotonera(), BorderLayout.SOUTH);

        add(new JScrollPane(content), BorderLayout.CENTER);
        configurarEventos();
    }

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new BorderLayout(8, 6));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, new Color(225, 232, 237), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JLabel lbl = new JLabel("Buscar estudiante/miembro universitario (código o cédula)");
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
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        // Información personal
        panel.add(crearSeccion("Información Personal", new JComponent[][]{
            {new JLabel("Nombres:"), txtNombres = crearCampo(false)},
            {new JLabel("Apellidos:"), txtApellidos = crearCampo(false)},
            {new JLabel("Código:"), txtCodigo = crearCampo(false)},
            {new JLabel("Cédula:"), txtCedula = crearCampo(false)},
            {new JLabel("Correo:"), txtCorreo = crearCampo(false)}
        }));

        // Información académica
        panel.add(crearSeccion("Información Académica", new JComponent[][]{
            {new JLabel("Carrera:"), txtCarrera = crearCampo(false)},
            {new JLabel("Nivel:"), txtNivel = crearCampo(false)},
            {new JLabel("IRA:"), txtIra = crearCampo(false)}
        }));

        // Información laboral
        panel.add(crearSeccion("Información Laboral", new JComponent[][]{
            {new JLabel("Horas Semanales (máx 32):*"), txtHoras = crearCampo(true)},
            {new JLabel("Meses Contratados:*"), txtMeses = crearCampo(true)}
        }));

        return panel;
    }

    private JTextField crearCampo(boolean editable) {
        JTextField txt = new JTextField(20);
        txt.setEditable(editable);
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        if (!editable) {
            txt.setBackground(new Color(245, 245, 245));
        }
        return txt;
    }

    private JPanel crearSeccion(String titulo, JComponent[][] campos) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, new Color(225, 232, 237), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 13));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(8));

        for (JComponent[] fila : campos) {
            JPanel panelFila = new JPanel(new BorderLayout(6, 0));
            panelFila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            panelFila.setAlignmentX(Component.LEFT_ALIGNMENT);
            fila[0].setPreferredSize(new Dimension(200, 25));
            panelFila.add(fila[0], BorderLayout.WEST);
            panelFila.add(fila[1], BorderLayout.CENTER);
            panel.add(panelFila);
            panel.add(Box.createVerticalStrut(6));
        }

        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
        return panel;
    }

    private JPanel crearBotonera() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnGuardar = new StyledButton("Guardar", StyledButton.TipoBoton.EXITO);
        btnCancelar = new StyledButton("Cancelar", StyledButton.TipoBoton.SECUNDARIO);
        panel.add(btnGuardar);
        panel.add(btnCancelar);
        return panel;
    }

    private void configurarEventos() {
        btnBuscar.addActionListener(e -> buscarEstudiante());
        btnGuardar.addActionListener(e -> guardarConValidacion());
        btnCancelar.addActionListener(e -> dispose());

        txtBusqueda.addActionListener(e -> buscarEstudiante());
    }

    private void buscarEstudiante() {
        String criterio = txtBusqueda.getText().trim();
        if (criterio.isEmpty()) {
            lblEstadoBusqueda.setText("⚠ Ingrese un código o cédula");
            lblEstadoBusqueda.setForeground(new Color(214, 158, 46));
            return;
        }

        Estudiante e = controlador.buscarEstudiante(criterio);
        if (e == null) {
            lblEstadoBusqueda.setText("✗ Estudiante no encontrado");
            lblEstadoBusqueda.setForeground(new Color(220, 53, 69));
            
            // Abrir ventana emergente de "No registrado"
            DialogoEstudianteNoRegistrado dialogo = new DialogoEstudianteNoRegistrado(
                (Frame) SwingUtilities.getWindowAncestor(this),
                controlador,
                "asistente"
            );
            dialogo.setVisible(true);
            
            // Si se registró el estudiante en la ventana emergente, intentar buscarlo nuevamente
            Estudiante estudianteNuevo = controlador.buscarEstudiante(criterio);
            if (estudianteNuevo != null) {
                estudianteSeleccionado = estudianteNuevo;
                llenarFormulario(estudianteNuevo);
                lblEstadoBusqueda.setText("✓ Estudiante encontrado");
                lblEstadoBusqueda.setForeground(new Color(40, 167, 69));
            }
        } else {
            lblEstadoBusqueda.setText("✓ Estudiante encontrado");
            lblEstadoBusqueda.setForeground(new Color(40, 167, 69));
            estudianteSeleccionado = e;
            llenarFormulario(e);
        }
    }

    private void llenarFormulario(Estudiante e) {
        txtNombres.setText(e.getNombres());
        txtApellidos.setText(e.getApellidos());
        txtCodigo.setText(e.getCodigoUnico());
        txtCedula.setText(e.getCedula());
        txtCorreo.setText(e.getCorreoInstitucional());
        txtCarrera.setText(e.getCarrera());
        txtNivel.setText(String.valueOf(e.getNivel()));
        txtIra.setText(String.format("%.2f", e.getIRA()));
    }

    private void limpiarFormulario() {
        txtNombres.setText("");
        txtApellidos.setText("");
        txtCodigo.setText("");
        txtCedula.setText("");
        txtCorreo.setText("");
        txtCarrera.setText("");
        txtNivel.setText("");
        txtIra.setText("");
        estudianteSeleccionado = null;
    }

    private void guardarConValidacion() {
        java.util.List<String> errores = new java.util.ArrayList<>();

        if (estudianteSeleccionado == null) {
            errores.add("Debe buscar y seleccionar un estudiante primero");
        }

        int horas = 0;
        try {
            horas = Integer.parseInt(txtHoras.getText().trim());
            if (horas <= 0 || horas > 32) {
                errores.add("Las horas deben estar entre 1 y 32");
            }
        } catch (NumberFormatException ex) {
            errores.add("Horas inválidas");
        }

        int meses = 0;
        try {
            meses = Integer.parseInt(txtMeses.getText().trim());
            if (meses <= 0 || meses > 12) {
                errores.add("Los meses deben estar entre 1 y 12");
            }
        } catch (NumberFormatException ex) {
            errores.add("Meses inválidos");
        }
        
        if (!errores.isEmpty()) {
            StringBuilder mensaje = new StringBuilder("Errores de validación:\n\n");
            for (String error : errores) {
                mensaje.append("• ").append(error).append("\n");
            }
            JOptionPane.showMessageDialog(this, mensaje.toString(), "Errores de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ResultadoOperacion res = controlador.registrarAsistente(
            estudianteSeleccionado.getCodigoUnico(), 
            horas, 
            meses
        );
        
        if (res.esExitoso()) {
            JOptionPane.showMessageDialog(this, 
                "Asistente de Investigación registrado exitosamente.\n" + res.getMensaje(), 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            guardado = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, res.getMensaje(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean seGuardo() {
        return guardado;
    }
}