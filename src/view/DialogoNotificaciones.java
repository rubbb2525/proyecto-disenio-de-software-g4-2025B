package view;

import model.Notificacion;
import view.componentes.StyledButton;
import view.componentes.RoundedBorder;
import view.componentes.ConstantesVisuales;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Diálogo de notificaciones - VERSIÓN MEJORADA
 * Sin emojis, diseño profesional y funcional
 */
public class DialogoNotificaciones extends JDialog {
    private static final Color COLOR_FONDO = ConstantesVisuales.COLOR_FONDO_PRINCIPAL;
    private static final Color COLOR_TARJETA = ConstantesVisuales.COLOR_TARJETA;
    private static final Color COLOR_PRIMARIO = ConstantesVisuales.COLOR_PRIMARIO;
    private static final Color COLOR_TEXTO = ConstantesVisuales.COLOR_TEXTO_PRINCIPAL;
    private static final Color COLOR_TEXTO_SECUNDARIO = ConstantesVisuales.COLOR_TEXTO_SECUNDARIO;
    private static final Color COLOR_BORDE = ConstantesVisuales.COLOR_BORDE;
    
    // Colores específicos para notificaciones
    private static final Color COLOR_NUEVA = new Color(52, 152, 219);
    private static final Color COLOR_LEIDA = new Color(149, 165, 166);
    private static final Color COLOR_EXITO = new Color(46, 204, 113);
    private static final Color COLOR_ADVERTENCIA = new Color(243, 156, 18);
    private static final Color COLOR_ERROR = new Color(231, 76, 60);
    private static final Color COLOR_INFO = new Color(52, 152, 219);

    private JPanel panelNotificaciones;
    private StyledButton btnMarcarLeidas;
    private StyledButton btnCerrar;
    private StyledButton btnFiltrarNoLeidas;
    private StyledButton btnFiltrarTodas;
    private JLabel lblContador;
    private JLabel lblContadorNoLeidas;
    
    private List<Notificacion> todasLasNotificaciones;
    private boolean mostrarSoloNoLeidas = false;

    public DialogoNotificaciones(Frame owner, List<Notificacion> notificaciones) {
        super(owner, "Centro de Notificaciones", true);
        this.todasLasNotificaciones = notificaciones;
        initUI();
    }

    private void initUI() {
        setSize(800, 700);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        setResizable(true);
        setMinimumSize(new Dimension(600, 500));
        getContentPane().setBackground(COLOR_FONDO);

        // Header
        JPanel header = crearHeaderMejorado();
        add(header, BorderLayout.NORTH);

        // Panel principal con notificaciones
        panelNotificaciones = new JPanel();
        panelNotificaciones.setLayout(new BoxLayout(panelNotificaciones, BoxLayout.Y_AXIS));
        panelNotificaciones.setBackground(COLOR_FONDO);
        panelNotificaciones.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        cargarNotificaciones();

        JScrollPane scroll = new JScrollPane(panelNotificaciones);
        scroll.setBorder(null);
        scroll.setBackground(COLOR_FONDO);
        scroll.getViewport().setBackground(COLOR_FONDO);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Estilizar scrollbar
        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = new Color(189, 195, 199);
                trackColor = COLOR_FONDO;
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return crearBotonInvisible();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return crearBotonInvisible();
            }
            
            private JButton crearBotonInvisible() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });
        
        add(scroll, BorderLayout.CENTER);

        // Footer
        JPanel footer = crearFooterMejorado();
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel crearHeaderMejorado() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                     RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, COLOR_PRIMARIO,
                    0, getHeight(), ConstantesVisuales.COLOR_PRIMARIO_HOVER
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(0, 100));
        header.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        // Panel izquierdo con título y contadores
        JPanel panelIzq = new JPanel();
        panelIzq.setOpaque(false);
        panelIzq.setLayout(new BoxLayout(panelIzq, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Centro de Notificaciones");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Panel de contadores
        JPanel panelContadores = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        panelContadores.setOpaque(false);

        long noLeidas = todasLasNotificaciones.stream()
            .filter(n -> !n.isLeida())
            .count();

        lblContador = new JLabel("Total: " + todasLasNotificaciones.size());
        lblContador.setForeground(new Color(255, 255, 255, 200));
        lblContador.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        lblContadorNoLeidas = new JLabel("Sin leer: " + noLeidas);
        lblContadorNoLeidas.setForeground(Color.WHITE);
        lblContadorNoLeidas.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        // Badge para no leídas
        if (noLeidas > 0) {
            JLabel badge = new JLabel(String.valueOf(noLeidas));
            badge.setOpaque(true);
            badge.setBackground(COLOR_ERROR);
            badge.setForeground(Color.WHITE);
            badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
            badge.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
            panelContadores.add(badge);
        }

        panelContadores.add(lblContador);
        panelContadores.add(new JSeparator(JSeparator.VERTICAL));
        panelContadores.add(lblContadorNoLeidas);

        panelIzq.add(lblTitulo);
        panelIzq.add(Box.createVerticalStrut(8));
        panelIzq.add(panelContadores);

        // Panel derecho con filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelFiltros.setOpaque(false);

        btnFiltrarTodas = new StyledButton("Todas", StyledButton.TipoBoton.SECUNDARIO);
        btnFiltrarTodas.setPreferredSize(new Dimension(90, 35));
        btnFiltrarTodas.setToolTipText("Mostrar todas las notificaciones");
        
        btnFiltrarNoLeidas = new StyledButton("Sin leer", StyledButton.TipoBoton.SECUNDARIO);
        btnFiltrarNoLeidas.setPreferredSize(new Dimension(90, 35));
        btnFiltrarNoLeidas.setToolTipText("Mostrar solo no leídas");

        panelFiltros.add(new JLabel("Filtrar:"));
        panelFiltros.add(btnFiltrarTodas);
        panelFiltros.add(btnFiltrarNoLeidas);

        header.add(panelIzq, BorderLayout.WEST);
        header.add(panelFiltros, BorderLayout.EAST);

        // Configurar eventos de filtros
        btnFiltrarTodas.addActionListener(e -> {
            mostrarSoloNoLeidas = false;
            cargarNotificaciones();
        });

        btnFiltrarNoLeidas.addActionListener(e -> {
            mostrarSoloNoLeidas = true;
            cargarNotificaciones();
        });

        return header;
    }

    private JPanel crearFooterMejorado() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(COLOR_FONDO);
        footer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDE),
            BorderFactory.createEmptyBorder(16, 24, 16, 24)
        ));

        // Panel izquierdo con información
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelInfo.setOpaque(false);
        
        JLabel lblInfo = new JLabel("Haga clic en una notificación para ver detalles");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfo.setForeground(COLOR_TEXTO_SECUNDARIO);
        panelInfo.add(lblInfo);

        // Panel derecho con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelBotones.setOpaque(false);

        btnMarcarLeidas = new StyledButton("Marcar todas como leídas", StyledButton.TipoBoton.EXITO);
        btnMarcarLeidas.setPreferredSize(new Dimension(200, 40));
        
        long noLeidas = todasLasNotificaciones.stream()
            .filter(n -> !n.isLeida())
            .count();
        btnMarcarLeidas.setEnabled(noLeidas > 0);

        btnCerrar = new StyledButton("Cerrar", StyledButton.TipoBoton.PRIMARIO);
        btnCerrar.setPreferredSize(new Dimension(100, 40));
        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnMarcarLeidas);
        panelBotones.add(btnCerrar);

        footer.add(panelInfo, BorderLayout.WEST);
        footer.add(panelBotones, BorderLayout.EAST);

        return footer;
    }

    private void cargarNotificaciones() {
        panelNotificaciones.removeAll();

        List<Notificacion> notificacionesFiltradas = todasLasNotificaciones;
        
        if (mostrarSoloNoLeidas) {
            notificacionesFiltradas = todasLasNotificaciones.stream()
                .filter(n -> !n.isLeida())
                .collect(Collectors.toList());
        }

        if (notificacionesFiltradas.isEmpty()) {
            mostrarMensajeVacio();
        } else {
            // Agrupar por leídas y no leídas
            List<Notificacion> noLeidas = notificacionesFiltradas.stream()
                .filter(n -> !n.isLeida())
                .collect(Collectors.toList());
            
            List<Notificacion> leidas = notificacionesFiltradas.stream()
                .filter(Notificacion::isLeida)
                .collect(Collectors.toList());

            // Mostrar no leídas primero
            if (!noLeidas.isEmpty()) {
                JLabel seccionNoLeidas = crearTituloSeccion("NUEVAS (" + noLeidas.size() + ")");
                panelNotificaciones.add(seccionNoLeidas);
                panelNotificaciones.add(Box.createVerticalStrut(12));

                for (Notificacion n : noLeidas) {
                    panelNotificaciones.add(crearTarjetaNotificacion(n));
                    panelNotificaciones.add(Box.createVerticalStrut(12));
                }
            }

            // Mostrar leídas después
            if (!leidas.isEmpty() && !mostrarSoloNoLeidas) {
                if (!noLeidas.isEmpty()) {
                    panelNotificaciones.add(Box.createVerticalStrut(12));
                }
                
                JLabel seccionLeidas = crearTituloSeccion("ANTERIORES (" + leidas.size() + ")");
                panelNotificaciones.add(seccionLeidas);
                panelNotificaciones.add(Box.createVerticalStrut(12));

                for (Notificacion n : leidas) {
                    panelNotificaciones.add(crearTarjetaNotificacion(n));
                    panelNotificaciones.add(Box.createVerticalStrut(12));
                }
            }
        }

        panelNotificaciones.add(Box.createVerticalGlue());
        panelNotificaciones.revalidate();
        panelNotificaciones.repaint();
    }

    private JLabel crearTituloSeccion(String texto) {
        JLabel titulo = new JLabel(texto);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titulo.setForeground(COLOR_TEXTO_SECUNDARIO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        titulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        return titulo;
    }

    private void mostrarMensajeVacio() {
        JPanel panelVacio = new JPanel();
        panelVacio.setLayout(new BoxLayout(panelVacio, BoxLayout.Y_AXIS));
        panelVacio.setOpaque(false);
        panelVacio.setBorder(BorderFactory.createEmptyBorder(100, 0, 100, 0));

        // Círculo con símbolo de campana
        JPanel circulo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(230, 230, 230));
                g2d.fillOval(0, 0, getWidth(), getHeight());
            }
        };
        circulo.setOpaque(false);
        circulo.setPreferredSize(new Dimension(100, 100));
        circulo.setMaximumSize(new Dimension(100, 100));
        circulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblIcono = new JLabel("NOTIFICACIONES");
        lblIcono.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblIcono.setForeground(new Color(150, 150, 150));
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
        lblIcono.setBounds(0, 35, 100, 30);
        circulo.setLayout(null);
        circulo.add(lblIcono);

        JLabel lblMensaje = new JLabel(
            mostrarSoloNoLeidas 
                ? "No hay notificaciones sin leer" 
                : "No hay notificaciones"
        );
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblMensaje.setForeground(COLOR_TEXTO_SECUNDARIO);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubmensaje = new JLabel("Todas las notificaciones aparecerán aquí");
        lblSubmensaje.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubmensaje.setForeground(new Color(180, 180, 180));
        lblSubmensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelVacio.add(circulo);
        panelVacio.add(Box.createVerticalStrut(20));
        panelVacio.add(lblMensaje);
        panelVacio.add(Box.createVerticalStrut(8));
        panelVacio.add(lblSubmensaje);

        panelNotificaciones.add(panelVacio);
    }

    private JPanel crearTarjetaNotificacion(Notificacion notif) {
        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBackground(COLOR_TARJETA);
        
        // Borde diferente si está leída o no
        if (notif.isLeida()) {
            card.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(10, COLOR_BORDE, 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
            ));
        } else {
            card.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(10, COLOR_NUEVA, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
        }
        
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Barra lateral de color según tipo
        JPanel barraLateral = new JPanel();
        barraLateral.setPreferredSize(new Dimension(4, 0));
        barraLateral.setBackground(obtenerColorPorTipo(notif.getTipo()));

        // Panel de contenido
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);
        contenido.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));

        // Header con tipo y fecha
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTipo.setOpaque(false);

        // Categoría de la notificación
        JLabel lblCategoria = new JLabel(obtenerCategoriaLegible(notif.getTipo()));
        lblCategoria.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCategoria.setForeground(obtenerColorPorTipo(notif.getTipo()));
        lblCategoria.setOpaque(true);
        lblCategoria.setBackground(new Color(
            obtenerColorPorTipo(notif.getTipo()).getRed(),
            obtenerColorPorTipo(notif.getTipo()).getGreen(),
            obtenerColorPorTipo(notif.getTipo()).getBlue(),
            30
        ));
        lblCategoria.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));

        // Indicador de nueva
        if (!notif.isLeida()) {
            JLabel lblNueva = new JLabel(" NUEVA");
            lblNueva.setFont(new Font("Segoe UI", Font.BOLD, 10));
            lblNueva.setForeground(Color.WHITE);
            lblNueva.setOpaque(true);
            lblNueva.setBackground(COLOR_ERROR);
            lblNueva.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
            panelTipo.add(lblCategoria);
            panelTipo.add(Box.createHorizontalStrut(8));
            panelTipo.add(lblNueva);
        } else {
            panelTipo.add(lblCategoria);
        }

        JLabel lblFecha = new JLabel(formatearFechaRelativa(notif.getFecha()));
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFecha.setForeground(COLOR_TEXTO_SECUNDARIO);

        header.add(panelTipo, BorderLayout.WEST);
        header.add(lblFecha, BorderLayout.EAST);

        // Mensaje
        String mensajeTexto = notif.getMensaje();
        if (mensajeTexto.length() > 150) {
            mensajeTexto = mensajeTexto.substring(0, 147) + "...";
        }

        JLabel lblMensaje = new JLabel("<html><body style='width: 650px;'>" 
            + mensajeTexto + "</body></html>");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMensaje.setForeground(notif.isLeida() ? COLOR_TEXTO_SECUNDARIO : COLOR_TEXTO);
        lblMensaje.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Información adicional si existe
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelInfo.setOpaque(false);
        panelInfo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        if (notif.getProyectoRelacionado() != null) {
            JLabel lblProyecto = new JLabel("Proyecto: " + notif.getProyectoRelacionado().getNombreProyecto());
            lblProyecto.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            lblProyecto.setForeground(COLOR_TEXTO_SECUNDARIO);
            panelInfo.add(lblProyecto);
        }

        if (notif.getAyudanteRelacionado() != null) {
            if (notif.getProyectoRelacionado() != null) {
                panelInfo.add(new JLabel(" • "));
            }
            JLabel lblAyudante = new JLabel("Colaborador: " + notif.getAyudanteRelacionado().getNombresCompletos());
            lblAyudante.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            lblAyudante.setForeground(COLOR_TEXTO_SECUNDARIO);
            panelInfo.add(lblAyudante);
        }

        contenido.add(header);
        contenido.add(Box.createVerticalStrut(8));
        contenido.add(lblMensaje);
        if (panelInfo.getComponentCount() > 0) {
            contenido.add(Box.createVerticalStrut(8));
            contenido.add(panelInfo);
        }

        card.add(barraLateral, BorderLayout.WEST);
        card.add(contenido, BorderLayout.CENTER);

        // Efecto hover
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(248, 250, 252));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(COLOR_TARJETA);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarDetalleNotificacion(notif);
            }
        });

        return card;
    }

    private void mostrarDetalleNotificacion(Notificacion notif) {
        JDialog dialogo = new JDialog(this, "Detalle de Notificación", true);
        dialogo.setSize(500, 300);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        panel.setBackground(Color.WHITE);

        JLabel lblTipo = new JLabel(obtenerCategoriaLegible(notif.getTipo()));
        lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTipo.setForeground(obtenerColorPorTipo(notif.getTipo()));

        JLabel lblFecha = new JLabel(formatearFecha(notif.getFecha()));
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFecha.setForeground(COLOR_TEXTO_SECUNDARIO);

        JTextArea txtMensaje = new JTextArea(notif.getMensaje());
        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);
        txtMensaje.setEditable(false);
        txtMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMensaje.setForeground(COLOR_TEXTO);
        txtMensaje.setBackground(Color.WHITE);
        txtMensaje.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));

        panel.add(lblTipo);
        panel.add(Box.createVerticalStrut(4));
        panel.add(lblFecha);
        panel.add(Box.createVerticalStrut(16));
        panel.add(txtMensaje);

        JButton btnCerrarDetalle = new JButton("Cerrar");
        btnCerrarDetalle.addActionListener(e -> dialogo.dispose());
        
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(btnCerrarDetalle);

        dialogo.add(panel, BorderLayout.CENTER);
        dialogo.add(panelBoton, BorderLayout.SOUTH);
        dialogo.setVisible(true);
    }

    private String obtenerCategoriaLegible(String tipo) {
        if (tipo == null) return "NOTIFICACIÓN";
        
        String tipoUpper = tipo.toUpperCase();
        if (tipoUpper.contains("NUEVO") && tipoUpper.contains("AYUDANTE")) {
            return "NUEVO AYUDANTE";
        } else if (tipoUpper.contains("BAJA") && tipoUpper.contains("AYUDANTE")) {
            return "BAJA DE AYUDANTE";
        } else if (tipoUpper.contains("PROYECTO")) {
            return "PROYECTO";
        } else if (tipoUpper.contains("ERROR")) {
            return "ERROR";
        } else if (tipoUpper.contains("ADVERTENCIA")) {
            return "ADVERTENCIA";
        }
        
        return tipo.toUpperCase();
    }

    private Color obtenerColorPorTipo(String tipo) {
        if (tipo == null) return COLOR_INFO;
        
        String tipoUpper = tipo.toUpperCase();
        if (tipoUpper.contains("ERROR") || tipoUpper.contains("BAJA")) {
            return COLOR_ERROR;
        } else if (tipoUpper.contains("EXITO") || tipoUpper.contains("NUEVO")) {
            return COLOR_EXITO;
        } else if (tipoUpper.contains("ADVERTENCIA")) {
            return COLOR_ADVERTENCIA;
        }
        
        return COLOR_INFO;
    }

    private String formatearFecha(java.util.Date fecha) {
        if (fecha == null) return "Fecha desconocida";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(fecha);
    }

    private String formatearFechaRelativa(java.util.Date fecha) {
        if (fecha == null) return "Hace un momento";
        
        long diff = System.currentTimeMillis() - fecha.getTime();
        long minutos = diff / (60 * 1000);
        long horas = diff / (60 * 60 * 1000);
        long dias = diff / (24 * 60 * 60 * 1000);
        
        if (minutos < 1) {
            return "Hace un momento";
        } else if (minutos < 60) {
            return "Hace " + minutos + " min";
        } else if (horas < 24) {
            return "Hace " + horas + " h";
        } else if (dias == 1) {
            return "Ayer";
        } else if (dias < 7) {
            return "Hace " + dias + " días";
        } else {
            return formatearFecha(fecha);
        }
    }

    public void onMarcarLeidasListener(Runnable action) {
        btnMarcarLeidas.addActionListener(e -> {
            action.run();
            dispose();
        });
    }
}