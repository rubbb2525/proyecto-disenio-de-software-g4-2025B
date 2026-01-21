package view.componentes;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Componente spinner de carga con animación
 */
public class LoadingIndicator extends JComponent {
    private float rotacion = 0;
    private boolean animando = false;
    private Timer timerAnimacion;
    
    public LoadingIndicator() {
        setPreferredSize(new Dimension(48, 48));
        setOpaque(false);
    }
    
    public void iniciar() {
        if (animando) return;
        animando = true;
        setVisible(true);
        
        timerAnimacion = new Timer(30, e -> {
            rotacion += 6;
            if (rotacion >= 360) rotacion = 0;
            repaint();
        });
        timerAnimacion.start();
    }
    
    public void detener() {
        if (timerAnimacion != null) {
            timerAnimacion.stop();
        }
        animando = false;
        rotacion = 0;
        setVisible(false);
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (!animando) return;
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        
        int w = getWidth();
        int h = getHeight();
        int centerX = w / 2;
        int centerY = h / 2;
        int radio = Math.min(w, h) / 2 - 4;
        
        // Guardar transformación original
        AffineTransform original = g2.getTransform();
        
        // Rotar alrededor del centro
        AffineTransform at = AffineTransform.getTranslateInstance(centerX, centerY);
        at.rotate(Math.toRadians(rotacion));
        at.translate(-centerX, -centerY);
        g2.setTransform(at);
        
        // Dibujar arco giratorio (estilo doughnut)
        g2.setColor(new Color(52, 152, 219));
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        int startAngle = 0;
        int arcAngle = 270;
        g2.drawArc(centerX - radio, centerY - radio, radio * 2, radio * 2, startAngle, arcAngle);
        
        g2.setTransform(original);
        
        // Sombra suave
        g2.setColor(new Color(0, 0, 0, 20));
        g2.fillOval(centerX - radio - 2, centerY - radio + 2, radio * 2 + 4, radio * 2 + 4);
        
        g2.dispose();
    }
    
    public boolean estaAnimando() {
        return animando;
    }
}
