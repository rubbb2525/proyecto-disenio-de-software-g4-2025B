package view;

import controller.ControladorDirector;
import model.ResultadoOperacion;
import model.TecnicoInvestigacion;
import view.componentes.PlaceholderTextField;
import view.componentes.StyledButton;
import view.componentes.RoundedBorder;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

/**
 * Diálogo de registro de Técnico de Investigación
 * Los técnicos son personal externo sin vinculación universitaria
 * CORREGIDO: No llama a setFechaRegistro() ya que se establece automáticamente en el constructor
 */
public class DialogoFormularioTecnico extends JDialog {
    private final ControladorDirector controlador;

    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtCedula;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtEspecialidad;
    private JTextField txtExperiencia;
    private JTextField txtEmpresa;
    private JTextField txtHoras;
    private JTextField txtSalario;
    private StyledButton btnGuardar;
    private StyledButton btnCancelar;

    private boolean guardado;

    public DialogoFormularioTecnico(Frame owner, ControladorDirector controlador) {
        super(owner, "Registrar Técnico de Investigación", true);
        this.controlador = controlador;
        this.guardado = false;
        initUI();
    }

    private void initUI() {
        setSize(520, 620);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(0, 12));

        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Panel informativo
        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, new Color(52, 152, 219, 40), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JLabel lblInfo = new JLabel("<html><b>ℹ Información:</b> Los técnicos son personal externo sin vinculación universitaria.</html>");
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(41, 128, 185));
        panelInfo.add(lblInfo);

        content.add(panelInfo, BorderLayout.NORTH);
        content.add(crearFormulario(), BorderLayout.CENTER);
        content.add(crearBotonera(), BorderLayout.SOUTH);

        add(new JScrollPane(content), BorderLayout.CENTER);
        configurarEventos();
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        // Datos personales
        panel.add(crearSeccion("Datos Personales", new JComponent[][]{
            {new JLabel("Nombres:*"), txtNombres = crearCampo()},
            {new JLabel("Apellidos:*"), txtApellidos = crearCampo()},
            {new JLabel("Cédula:*"), txtCedula = crearCampo()},
            {new JLabel("Teléfono:*"), txtTelefono = crearCampo()},
            {new JLabel("Correo Electrónico:*"), txtCorreo = crearCampo()}
        }));

        // Datos profesionales
        panel.add(crearSeccion("Datos Profesionales", new JComponent[][]{
            {new JLabel("Especialidad Técnica:*"), txtEspecialidad = crearCampo()},
            {new JLabel("Años de Experiencia:*"), txtExperiencia = crearCampo()},
            {new JLabel("Empresa/Organización:"), txtEmpresa = crearCampo()}
        }));

        // Datos contractuales
        panel.add(crearSeccion("Datos Contractuales", new JComponent[][]{
            {new JLabel("Horas Semanales (máx 40):*"), txtHoras = crearCampo()},
            {new JLabel("Salario Mensual:*"), txtSalario = crearCampo()}
        }));

        return panel;
    }

    private JTextField crearCampo() {
        JTextField txt = new JTextField(20);
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
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
        btnGuardar.addActionListener(e -> guardarConValidacion());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void guardarConValidacion() {
        java.util.List<String> errores = new java.util.ArrayList<>();
        
        // Validar campos obligatorios
        if (txtNombres.getText().trim().isEmpty()) {
            errores.add("El campo Nombres es obligatorio");
        }
        if (txtApellidos.getText().trim().isEmpty()) {
            errores.add("El campo Apellidos es obligatorio");
        }
        if (txtCedula.getText().trim().isEmpty()) {
            errores.add("El campo Cédula es obligatorio");
        } else if (!txtCedula.getText().trim().matches("\\d{10,13}")) {
            errores.add("La cédula debe tener entre 10 y 13 dígitos");
        }
        if (txtTelefono.getText().trim().isEmpty()) {
            errores.add("El campo Teléfono es obligatorio");
        }
        if (txtCorreo.getText().trim().isEmpty()) {
            errores.add("El campo Correo electrónico es obligatorio");
        } else if (!txtCorreo.getText().trim().contains("@")) {
            errores.add("El correo electrónico debe ser válido");
        }
        if (txtEspecialidad.getText().trim().isEmpty()) {
            errores.add("El campo Especialidad Técnica es obligatorio");
        }
        
        // Validar años de experiencia
        int experiencia = 0;
        try {
            experiencia = Integer.parseInt(txtExperiencia.getText().trim());
            if (experiencia < 0 || experiencia > 50) {
                errores.add("Los años de experiencia deben estar entre 0 y 50");
            }
        } catch (NumberFormatException e) {
            errores.add("Años de experiencia inválidos (debe ser un número entero)");
        }
        
        // Validar horas
        int horas = 0;
        try {
            horas = Integer.parseInt(txtHoras.getText().trim());
            if (horas <= 0 || horas > 40) {
                errores.add("Las horas semanales deben estar entre 1 y 40");
            }
        } catch (NumberFormatException e) {
            errores.add("Horas semanales inválidas (debe ser un número entero)");
        }
        
        // Validar salario
        double salario = 0;
        try {
            salario = Double.parseDouble(txtSalario.getText().trim());
            if (salario <= 0) {
                errores.add("El salario mensual debe ser mayor a 0");
            }
        } catch (NumberFormatException e) {
            errores.add("Salario mensual inválido (debe ser un número)");
        }
        
        if (!errores.isEmpty()) {
            StringBuilder mensaje = new StringBuilder("Errores de validación:\n\n");
            for (String error : errores) {
                mensaje.append("• ").append(error).append("\n");
            }
            JOptionPane.showMessageDialog(this, mensaje.toString(), "Errores de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Crear objeto TecnicoInvestigacion
        // IMPORTANTE: El constructor establece fechaRegistro automáticamente
        TecnicoInvestigacion tecnico = new TecnicoInvestigacion();
        
        // Generar ID único (formato: TEC-YYYYMMDD-HHmmss)
        String id = String.format("TEC-%tY%<tm%<td-%<tH%<tM%<tS", new Date());
        tecnico.setIdTecnico(id);
        
        // Establecer datos del técnico
        tecnico.setCedula(txtCedula.getText().trim());
        tecnico.setNombres(txtNombres.getText().trim());
        tecnico.setApellidos(txtApellidos.getText().trim());
        tecnico.setTelefono(txtTelefono.getText().trim());
        tecnico.setCorreoElectronico(txtCorreo.getText().trim());
        tecnico.setEspecialidadTecnica(txtEspecialidad.getText().trim());
        tecnico.setAniosExperiencia(experiencia);
        tecnico.setEmpresaOrigen(txtEmpresa.getText().trim());
        tecnico.setHorasSemanales(horas);
        tecnico.setSalarioMensual(salario);
        tecnico.setEstado("ACTIVO");
        // NO llamar a setFechaRegistro() - ya se establece en el constructor
        
        // Llamar al controlador
        ResultadoOperacion res = controlador.registrarTecnico(tecnico);
        
        if (res.esExitoso()) {
            JOptionPane.showMessageDialog(this, 
                "Técnico de Investigación registrado exitosamente.\n" + res.getMensaje(), 
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
