package view;

import model.Estudiante;
import model.dao.EstudianteDAO;
import view.componentes.StyledButton;
import view.componentes.RoundedBorder;

import javax.swing.*;
import java.awt.*;

/**
 * Diálogo para registrar un nuevo estudiante en el sistema.
 * Los datos se guardan directamente en la base de datos.
 */
public class DialogoFormularioEstudiante extends JDialog {
    private static final Color COLOR_FONDO = new Color(245, 247, 250);
    private static final Color COLOR_TARJETA = Color.WHITE;
    private static final Color COLOR_BORDE = new Color(225, 232, 237);
    
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtCodigo;
    private JTextField txtCedula;
    private JTextField txtCorreo;
    private JTextField txtCarrera;
    private JTextField txtNivel;
    private JTextField txtIra;
    
    private StyledButton btnGuardar;
    private StyledButton btnCancelar;
    
    private boolean guardado = false;
    
    public DialogoFormularioEstudiante(Frame owner) {
        super(owner, "Registrar Nuevo Estudiante", true);
        initUI();
    }
    
    private void initUI() {
        setSize(520, 620);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(0, 12));
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        
        content.add(crearFormulario(), BorderLayout.CENTER);
        content.add(crearBotonera(), BorderLayout.SOUTH);
        
        add(new JScrollPane(content), BorderLayout.CENTER);
        configurarEventos();
    }
    
    private JPanel crearFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        
        // Información personal
        panel.add(crearSeccion("Información Personal", new JComponent[][]{
            {new JLabel("Nombres:*"), txtNombres = crearCampo(true)},
            {new JLabel("Apellidos:*"), txtApellidos = crearCampo(true)},
            {new JLabel("Código Único:*"), txtCodigo = crearCampo(true)},
            {new JLabel("Cédula:*"), txtCedula = crearCampo(true)},
            {new JLabel("Correo Institucional:*"), txtCorreo = crearCampo(true)}
        }));
        
        // Información académica
        panel.add(crearSeccion("Información Académica", new JComponent[][]{
            {new JLabel("Carrera:*"), txtCarrera = crearCampo(true)},
            {new JLabel("Nivel:*"), txtNivel = crearCampo(true)},
            {new JLabel("IRA:*"), txtIra = crearCampo(true)}
        }));
        
        return panel;
    }
    
    private JTextField crearCampo(boolean editable) {
        JTextField txt = new JTextField(20);
        txt.setEditable(editable);
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return txt;
    }
    
    private JPanel crearSeccion(String titulo, JComponent[][] campos) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, COLOR_BORDE, 1),
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
            fila[0].setPreferredSize(new Dimension(170, 25));
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
        btnGuardar.addActionListener(e -> guardarEstudiante());
        btnCancelar.addActionListener(e -> dispose());
    }
    
    private void guardarEstudiante() {
        java.util.List<String> errores = new java.util.ArrayList<>();
        
        // Validar campos obligatorios
        String nombres = txtNombres.getText().trim();
        if (nombres.isEmpty()) {
            errores.add("Los nombres son obligatorios");
        }
        
        String apellidos = txtApellidos.getText().trim();
        if (apellidos.isEmpty()) {
            errores.add("Los apellidos son obligatorios");
        }
        
        String codigo = txtCodigo.getText().trim();
        if (codigo.isEmpty()) {
            errores.add("El código único es obligatorio");
        }
        
        String cedula = txtCedula.getText().trim();
        if (cedula.isEmpty()) {
            errores.add("La cédula es obligatoria");
        }
        
        String correo = txtCorreo.getText().trim();
        if (correo.isEmpty()) {
            errores.add("El correo es obligatorio");
        } else if (!correo.contains("@")) {
            errores.add("El correo debe ser válido");
        }
        
        String carrera = txtCarrera.getText().trim();
        if (carrera.isEmpty()) {
            errores.add("La carrera es obligatoria");
        }
        
        int nivel = 0;
        try {
            nivel = Integer.parseInt(txtNivel.getText().trim());
            if (nivel <= 0 || nivel > 10) {
                errores.add("El nivel debe estar entre 1 y 10");
            }
        } catch (NumberFormatException e) {
            errores.add("El nivel debe ser un número válido");
        }
        
        float ira = 0.0f;
        try {
            ira = Float.parseFloat(txtIra.getText().trim());
            if (ira < 0 || ira > 40) {
                errores.add("El IRA debe estar entre 0 y 40");
            }
        } catch (NumberFormatException e) {
            errores.add("El IRA debe ser un número válido");
        }
        
        if (!errores.isEmpty()) {
            StringBuilder mensaje = new StringBuilder("Errores de validación:\n\n");
            for (String error : errores) {
                mensaje.append("• ").append(error).append("\n");
            }
            JOptionPane.showMessageDialog(this, mensaje.toString(), "Errores de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Crear el estudiante
        try {
            Estudiante nuevoEstudiante = new Estudiante();
            nuevoEstudiante.setNombres(nombres);
            nuevoEstudiante.setApellidos(apellidos);
            nuevoEstudiante.setCodigoUnico(codigo);
            nuevoEstudiante.setCedula(cedula);
            nuevoEstudiante.setCorreoInstitucional(correo);
            nuevoEstudiante.setCarrera(carrera);
            nuevoEstudiante.setNivel(nivel);
            nuevoEstudiante.setIRA(ira);
            
            // Guardar en la base de datos a través del DAO
            EstudianteDAO estudianteDAO = new EstudianteDAO();
            boolean resultado = estudianteDAO.guardar(nuevoEstudiante);
            
            if (resultado) {
                JOptionPane.showMessageDialog(this,
                    "Estudiante registrado exitosamente.\n" +
                    "Código: " + codigo + "\n" +
                    "Nombre: " + nombres + " " + apellidos,
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                guardado = true;
                dispose();
            } else {
                String errorMsg = estudianteDAO.getLastError();
                if (errorMsg.isEmpty()) {
                    errorMsg = "Verifique que no exista un registro con el mismo código, cédula o correo.";
                }
                JOptionPane.showMessageDialog(this,
                    "Error al guardar el estudiante: " + errorMsg,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar el estudiante: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    public boolean seGuardo() {
        return guardado;
    }
}