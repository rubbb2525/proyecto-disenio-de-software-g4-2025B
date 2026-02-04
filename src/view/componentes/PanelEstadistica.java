package view.componentes;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Panel de estadística visual con diseño compacto y moderno
 * Muestra valor principal, etiqueta y subtítulo en un diseño de tarjeta
 */
public class PanelEstadistica extends JPanel {
    private JLabel lblValor;
    private JLabel lblEtiqueta;
    private JLabel lblSubtitulo;
    private Color colorPrimario;
    private Color colorSecundario;
    
    public PanelEstadistica(String etiqueta, String valorInicial, Color color, String subtitulo) {
        this.colorPrimario = color;
        this.colorSecundario = new Color(
            Math.min(255, color.getRed() + 40),
            Math.min(255, color.getGreen() + 40),
            Math.min(255, color.getBlue() + 40)
        );
        
        setLayout(new BorderLayout());
        setOpaque(false);
        
        // Border personalizado con gradiente
        setBorder(new PanelBorder(color));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(160, 100));
        setMaximumSize(new Dimension(200, 120));
        setMinimumSize(new Dimension(140, 90));
        
        // Panel principal contenedor
        JPanel panelContenedor = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo con gradiente sutil
                int width = getWidth();
                int height = getHeight();
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 255, 255),
                    0, height, new Color(248, 251, 255)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, width, height, 12, 12);
            }
        };
        
        panelContenedor.setLayout(new BorderLayout());
        panelContenedor.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        
        // Panel superior: valor y círculo de color
        JPanel panelSuperior = new JPanel(new BorderLayout(8, 0));
        panelSuperior.setOpaque(false);
        
        // Indicador de color circular
        JPanel indicador = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int size = Math.min(getWidth(), getHeight());
                g2d.setColor(colorPrimario);
                g2d.fillOval((getWidth() - size) / 2, (getHeight() - size) / 2, size, size);
                
                // Brillo
                g2d.setColor(new Color(255, 255, 255, 80));
                g2d.fillOval((getWidth() - size) / 2 + 2, (getHeight() - size) / 2 + 2, size / 3, size / 3);
            }
        };
        indicador.setOpaque(false);
        indicador.setPreferredSize(new Dimension(12, 12));
        
        lblValor = new JLabel(valorInicial);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblValor.setForeground(new Color(44, 62, 80));
        
        panelSuperior.add(indicador, BorderLayout.WEST);
        panelSuperior.add(lblValor, BorderLayout.CENTER);
        
        // Panel inferior: etiqueta y subtítulo
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.Y_AXIS));
        panelInferior.setOpaque(false);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(6, 14, 0, 0));
        
        lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEtiqueta.setForeground(new Color(52, 73, 94));
        lblEtiqueta.setAlignmentX(LEFT_ALIGNMENT);
        
        lblSubtitulo = new JLabel(subtitulo);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblSubtitulo.setForeground(new Color(149, 165, 166));
        lblSubtitulo.setAlignmentX(LEFT_ALIGNMENT);
        
        panelInferior.add(lblEtiqueta);
        panelInferior.add(Box.createVerticalStrut(2));
        panelInferior.add(lblSubtitulo);
        
        // Agregar al contenedor
        panelContenedor.add(panelSuperior, BorderLayout.NORTH);
        panelContenedor.add(panelInferior, BorderLayout.CENTER);
        
        add(panelContenedor, BorderLayout.CENTER);
    }
    
    public void actualizarValor(String nuevoValor) {
        String valorAnterior = lblValor.getText();
        lblValor.setText(nuevoValor);
        
        // Animación de cambio de valor
        if (!valorAnterior.equals(nuevoValor)) {
            Timer timer = new Timer(50, null);
            final float[] scale = {1.15f};
            timer.addActionListener(e -> {
                scale[0] -= 0.08f;
                if (scale[0] <= 1.0f) {
                    scale[0] = 1.0f;
                    timer.stop();
                }
                lblValor.setFont(lblValor.getFont().deriveFont(22f * scale[0]));
                repaint();
            });
            timer.start();
        }
    }
    
    public void actualizarEtiqueta(String nuevaEtiqueta) {
        lblEtiqueta.setText(nuevaEtiqueta);
    }
    
    public void actualizarSubtitulo(String nuevoSubtitulo) {
        lblSubtitulo.setText(nuevoSubtitulo);
    }
    
    public String obtenerValor() {
        return lblValor.getText();
    }
    
    /**
     * Border personalizado con línea coloreada en el lado izquierdo
     */
    private static class PanelBorder extends AbstractBorder {
        private Color color;
        private static final int THICKNESS = 4;
        private static final int CORNER_RADIUS = 12;
        
        public PanelBorder(Color color) {
            this.color = color;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Borde redondeado sutil
            g2d.setColor(new Color(220, 230, 240));
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.drawRoundRect(x, y, width - 1, height - 1, CORNER_RADIUS, CORNER_RADIUS);
            
            // Línea coloreada en el lado izquierdo
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(THICKNESS));
            g2d.drawLine(x + 2, y + 4, x + 2, y + height - 4);
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(8, 8, 8, 8);
        }
    }
}
