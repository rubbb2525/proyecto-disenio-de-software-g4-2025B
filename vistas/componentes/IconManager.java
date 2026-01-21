package vistas.componentes;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestor centralizado de iconos para el sistema
 * Carga y cachea iconos SVG/PNG para uso en la UI
 */
public class IconManager {
    private static IconManager instancia;
    private Map<String, ImageIcon> cache;
    private static final String ICONS_PATH = "/vistas/icons/";
    
    // Tamaños estándar
    public static final int SMALL = 16;
    public static final int MEDIUM = 24;
    public static final int LARGE = 32;
    public static final int XLARGE = 48;
    
    private IconManager() {
        cache = new HashMap<>();
    }
    
    public static IconManager getInstance() {
        if (instancia == null) {
            instancia = new IconManager();
        }
        return instancia;
    }
    
    /**
     * Carga un icono por nombre con tamaño por defecto (24px)
     */
    public ImageIcon getIcon(String nombre) {
        return getIcon(nombre, MEDIUM);
    }
    
    /**
     * Carga un icono por nombre con tamaño específico
     */
    public ImageIcon getIcon(String nombre, int size) {
        String key = nombre + "_" + size;
        
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        
        try {
            // Intentar cargar SVG primero, luego PNG
            ImageIcon icon = loadIcon(nombre + ".svg", size);
            if (icon == null) {
                icon = loadIcon(nombre + ".png", size);
            }
            
            if (icon != null) {
                cache.put(key, icon);
                return icon;
            }
        } catch (Exception e) {
            System.err.println("Error cargando icono: " + nombre + " - " + e.getMessage());
        }
        
        // Retornar icono por defecto si falla
        return createDefaultIcon(size);
    }
    
    /**
     * Carga un icono desde resources y lo escala
     */
    private ImageIcon loadIcon(String filename, int size) {
        try {
            URL url = getClass().getResource(ICONS_PATH + filename);
            if (url == null) {
                return null;
            }
            
            ImageIcon original = new ImageIcon(url);
            if (original.getIconWidth() <= 0) {
                return null;
            }
            
            // Escalar manteniendo aspecto
            Image scaled = original.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Crea un icono por defecto cuando no se encuentra el solicitado
     */
    private ImageIcon createDefaultIcon(int size) {
        Image img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setColor(new Color(200, 200, 200));
        g2d.fillRect(0, 0, size, size);
        g2d.dispose();
        return new ImageIcon(img);
    }
    
    /**
     * Limpia el cache de iconos
     */
    public void clearCache() {
        cache.clear();
    }
    
    // Métodos de conveniencia para iconos comunes
    public ImageIcon getHomeIcon() { return getIcon("home"); }
    public ImageIcon getHomeIcon(int size) { return getIcon("home", size); }
    
    public ImageIcon getUserIcon() { return getIcon("user"); }
    public ImageIcon getUserIcon(int size) { return getIcon("user", size); }
    
    public ImageIcon getBellIcon() { return getIcon("bell"); }
    public ImageIcon getBellIcon(int size) { return getIcon("bell", size); }
    
    public ImageIcon getLogoutIcon() { return getIcon("logout"); }
    public ImageIcon getLogoutIcon(int size) { return getIcon("logout", size); }
    
    public ImageIcon getAddIcon() { return getIcon("add"); }
    public ImageIcon getAddIcon(int size) { return getIcon("add", size); }
    
    public ImageIcon getDeleteIcon() { return getIcon("delete"); }
    public ImageIcon getDeleteIcon(int size) { return getIcon("delete", size); }
    
    public ImageIcon getRefreshIcon() { return getIcon("refresh"); }
    public ImageIcon getRefreshIcon(int size) { return getIcon("refresh", size); }
    
    public ImageIcon getSearchIcon() { return getIcon("search"); }
    public ImageIcon getSearchIcon(int size) { return getIcon("search", size); }
    
    public ImageIcon getReportIcon() { return getIcon("report"); }
    public ImageIcon getReportIcon(int size) { return getIcon("report", size); }
    
    public ImageIcon getSendIcon() { return getIcon("send"); }
    public ImageIcon getSendIcon(int size) { return getIcon("send", size); }
    
    public ImageIcon getEyeIcon() { return getIcon("eye"); }
    public ImageIcon getEyeIcon(int size) { return getIcon("eye", size); }
    
    public ImageIcon getEyeCrossedIcon() { return getIcon("eye-crossed"); }
    public ImageIcon getEyeCrossedIcon(int size) { return getIcon("eye-crossed", size); }
    
    public ImageIcon getSuccessIcon() { return getIcon("sucess"); }
    public ImageIcon getSuccessIcon(int size) { return getIcon("sucess", size); }
    
    public ImageIcon getErrorIcon() { return getIcon("error"); }
    public ImageIcon getErrorIcon(int size) { return getIcon("error", size); }
    
    public ImageIcon getWarningIcon() { return getIcon("warning"); }
    public ImageIcon getWarningIcon(int size) { return getIcon("warning", size); }
    
    public ImageIcon getInfoIcon() { return getIcon("info"); }
    public ImageIcon getInfoIcon(int size) { return getIcon("info", size); }
    
    public ImageIcon getUsersIcon() { return getIcon("users"); }
    public ImageIcon getUsersIcon(int size) { return getIcon("users", size); }
    
    public ImageIcon getChartIcon() { return getIcon("chart"); }
    public ImageIcon getChartIcon(int size) { return getIcon("chart", size); }
    
    public ImageIcon getMessagesIcon() { return getIcon("messages"); }
    public ImageIcon getMessagesIcon(int size) { return getIcon("messages", size); }
    
    public ImageIcon getArrowLeftIcon() { return getIcon("arrow-left"); }
    public ImageIcon getArrowLeftIcon(int size) { return getIcon("arrow-left", size); }
    
    public ImageIcon getArrowRightIcon() { return getIcon("arrow-right"); }
    public ImageIcon getArrowRightIcon(int size) { return getIcon("arrow-right", size); }
    
    public ImageIcon getCheckCircleIcon() { return getIcon("check-circle"); }
    public ImageIcon getCheckCircleIcon(int size) { return getIcon("check-circle", size); }
    
    public ImageIcon getSaveIcon() { return getIcon("save"); }
    public ImageIcon getSaveIcon(int size) { return getIcon("save", size); }
    
    public ImageIcon getCancelIcon() { return getIcon("cancel"); }
    public ImageIcon getCancelIcon(int size) { return getIcon("cancel", size); }
    
    public ImageIcon getFilterIcon() { return getIcon("filter"); }
    public ImageIcon getFilterIcon(int size) { return getIcon("filter", size); }
    
    public ImageIcon getLogoIcon() { return getIcon("logo"); }
    public ImageIcon getLogoIcon(int size) { return getIcon("logo", size); }
}
