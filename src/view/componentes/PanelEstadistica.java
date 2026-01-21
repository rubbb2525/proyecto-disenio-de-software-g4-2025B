package view.componentes;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de estadística visual con icono, valor y etiqueta
 */
public class PanelEstadistica extends JPanel {
    private JLabel lblValor;
    private JLabel lblEtiqueta;
    private Color colorPrimario;
    
    public PanelEstadistica(String etiqueta, String valorInicial, Color color, String icono) {
        this.colorPrimario = color;
        setLayout(new BorderLayout(8, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, new Color(225, 232, 237), 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        setPreferredSize(new Dimension(200, 90));
        setMaximumSize(new Dimension(250, 90));
        
        // Lado izquierdo - icono
        JPanel panelIcono = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Círculo de fondo con opacidad
                g2.setColor(new Color(colorPrimario.getRed(), colorPrimario.getGreen(), colorPrimario.getBlue(), 30));
                g2.fillOval(0, 0, 48, 48);
                g2.dispose();
            }
        };
        panelIcono.setOpaque(false);
        panelIcono.setPreferredSize(new Dimension(48, 48));
        panelIcono.setLayout(new GridBagLayout());
        
        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Arial", Font.BOLD, 24));
        lblIcono.setForeground(colorPrimario);
        panelIcono.add(lblIcono);
        
        // Lado derecho - valor y etiqueta
        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setOpaque(false);
        
        lblValor = new JLabel(valorInicial);
        lblValor.setFont(new Font("Arial", Font.BOLD, 28));
        lblValor.setForeground(new Color(44, 62, 80));
        lblValor.setAlignmentX(LEFT_ALIGNMENT);
        
        lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Arial", Font.PLAIN, 12));
        lblEtiqueta.setForeground(new Color(127, 140, 141));
        lblEtiqueta.setAlignmentX(LEFT_ALIGNMENT);
        
        panelTexto.add(lblValor);
        panelTexto.add(Box.createVerticalStrut(2));
        panelTexto.add(lblEtiqueta);
        
        add(panelIcono, BorderLayout.WEST);
        add(panelTexto, BorderLayout.CENTER);
    }
    
    public void actualizarValor(String nuevoValor) {
        String valorAnterior = lblValor.getText();
        lblValor.setText(nuevoValor);
        
        // Animación simple de cambio
        if (!valorAnterior.equals(nuevoValor)) {
            Timer timer = new Timer(100, null);
            final float[] scale = {1.2f};
            timer.addActionListener(e -> {
                scale[0] -= 0.05f;
                if (scale[0] <= 1.0f) {
                    scale[0] = 1.0f;
                    timer.stop();
                }
                lblValor.setFont(lblValor.getFont().deriveFont(28f * scale[0]));
                repaint();
            });
            timer.start();
        }
    }
    
    public void actualizarEtiqueta(String nuevaEtiqueta) {
        lblEtiqueta.setText(nuevaEtiqueta);
    }
}
