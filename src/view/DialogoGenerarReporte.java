package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import view.componentes.StyledButton;
import view.componentes.RoundedBorder;

public class DialogoGenerarReporte extends JDialog {
    
    // Color palette
    private static final Color COLOR_PRIMARIO = new Color(0, 61, 165);
    private static final Color COLOR_SECUNDARIO = new Color(74, 144, 226);
    private static final Color COLOR_FONDO = new Color(245, 247, 250);
    private static final Color COLOR_TARJETA = Color.WHITE;
    private static final Color COLOR_TEXTO = new Color(44, 62, 80);
    private static final Color COLOR_BORDE = new Color(225, 232, 237);
    
    private JComboBox<String> comboTipoReporte;
    private JComboBox<String> comboProyecto;
    private JComboBox<String> comboCarrera;
    private JComboBox<String> comboNivel;
    private String tipoSeleccionado;
    private String filtroSeleccionado;
    private boolean seGenero = false;
    
    public DialogoGenerarReporte(JFrame owner, String[] proyectos, String[] carreras, String[] niveles) {
        super(owner, "Generar Reporte", true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(450, 400);
        this.setLocationRelativeTo(owner);
        this.setResizable(false);
        
        inicializarComponentes(proyectos, carreras, niveles);
    }
    
    private void inicializarComponentes(String[] proyectos, String[] carreras, String[] niveles) {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(COLOR_FONDO);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(COLOR_PRIMARIO);
        headerPanel.setPreferredSize(new Dimension(450, 50));
        JLabel titleLabel = new JLabel("Generar Reporte");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        panelPrincipal.add(headerPanel);
        panelPrincipal.add(Box.createVerticalStrut(15));
        
        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(COLOR_FONDO);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Tipo de reporte
        JLabel labelTipo = new JLabel("Tipo de Reporte:");
        labelTipo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelTipo.setForeground(COLOR_TEXTO);
        contentPanel.add(labelTipo);
        
        comboTipoReporte = new JComboBox<>(new String[]{
            "General",
            "Por Proyecto",
            "Por Carrera",
            "Por Nivel"
        });
        comboTipoReporte.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        comboTipoReporte.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        comboTipoReporte.setBackground(COLOR_TARJETA);
        comboTipoReporte.addActionListener(e -> actualizarFiltros());
        contentPanel.add(comboTipoReporte);
        contentPanel.add(Box.createVerticalStrut(12));
        
        // Filtro dinámico
        JLabel labelFiltro = new JLabel("Filtrar por:");
        labelFiltro.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelFiltro.setForeground(COLOR_TEXTO);
        contentPanel.add(labelFiltro);
        
        // Proyecto
        comboProyecto = new JComboBox<>(proyectos);
        comboProyecto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        comboProyecto.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        comboProyecto.setBackground(COLOR_TARJETA);
        comboProyecto.setVisible(false);
        contentPanel.add(comboProyecto);
        
        // Carrera
        comboCarrera = new JComboBox<>(carreras);
        comboCarrera.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        comboCarrera.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        comboCarrera.setBackground(COLOR_TARJETA);
        comboCarrera.setVisible(false);
        contentPanel.add(comboCarrera);
        
        // Nivel
        comboNivel = new JComboBox<>(niveles);
        comboNivel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        comboNivel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        comboNivel.setBackground(COLOR_TARJETA);
        comboNivel.setVisible(false);
        contentPanel.add(comboNivel);
        
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Botones
        JPanel botonePanel = new JPanel();
        botonePanel.setLayout(new BoxLayout(botonePanel, BoxLayout.X_AXIS));
        botonePanel.setBackground(COLOR_FONDO);
        botonePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        StyledButton btnCancelar = new StyledButton("Cancelar", StyledButton.TipoBoton.SECUNDARIO);
        btnCancelar.addActionListener(e -> dispose());
        
        StyledButton btnGenerar = new StyledButton("Generar", StyledButton.TipoBoton.EXITO);
        btnGenerar.addActionListener(e -> generarReporte());
        
        botonePanel.add(Box.createHorizontalGlue());
        botonePanel.add(btnCancelar);
        botonePanel.add(Box.createHorizontalStrut(10));
        botonePanel.add(btnGenerar);
        
        contentPanel.add(botonePanel);
        contentPanel.add(Box.createVerticalGlue());
        
        panelPrincipal.add(contentPanel);
        this.add(panelPrincipal);
    }
    
    private void actualizarFiltros() {
        String tipo = (String) comboTipoReporte.getSelectedItem();
        
        comboProyecto.setVisible(false);
        comboCarrera.setVisible(false);
        comboNivel.setVisible(false);
        
        if ("Por Proyecto".equals(tipo)) {
            comboProyecto.setVisible(true);
        } else if ("Por Carrera".equals(tipo)) {
            comboCarrera.setVisible(true);
        } else if ("Por Nivel".equals(tipo)) {
            comboNivel.setVisible(true);
        }
        
        revalidate();
        repaint();
    }
    
    private void generarReporte() {
        tipoSeleccionado = (String) comboTipoReporte.getSelectedItem();
        
        if ("Por Proyecto".equals(tipoSeleccionado)) {
            filtroSeleccionado = (String) comboProyecto.getSelectedItem();
        } else if ("Por Carrera".equals(tipoSeleccionado)) {
            filtroSeleccionado = (String) comboCarrera.getSelectedItem();
        } else if ("Por Nivel".equals(tipoSeleccionado)) {
            filtroSeleccionado = (String) comboNivel.getSelectedItem();
        } else {
            filtroSeleccionado = null;
        }
        
        seGenero = true;
        dispose();
    }
    
    public String getTipoReporte() {
        return tipoSeleccionado;
    }
    
    public String getFiltroSeleccionado() {
        return filtroSeleccionado;
    }
    
    public boolean seGenero() {
        return seGenero;
    }
}
