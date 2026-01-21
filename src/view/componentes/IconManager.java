package view.componentes;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.io.File;

/**
 * Gestor sencillo de íconos cargados desde la carpeta "vistas/icons" relativa al proyecto
 * o desde el classpath (por ejemplo si se empaquetan en resources/icons).
 */
public class IconManager {
    private static IconManager instance;
    private final String basePath = "src/view/icons";

    private IconManager() {}

    public static IconManager getInstance() {
        if (instance == null) {
            instance = new IconManager();
        }
        return instance;
    }

    public ImageIcon getIcon(String fileName, int size) {
        ImageIcon icon = loadFromFileSystem(fileName, size);
        if (icon != null) return icon;
        icon = loadFromClasspath(fileName, size);
        return icon != null ? icon : new ImageIcon(new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB));
    }

    public ImageIcon getLogoIcon(int size) {
        return getIcon("logo.png", size);
    }

    private ImageIcon loadFromFileSystem(String fileName, int size) {
        try {
            File f = new File(basePath, fileName);
            if (f.exists()) {
                Image img = new ImageIcon(f.getAbsolutePath()).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception ignored) {}
        return null;
    }

    private ImageIcon loadFromClasspath(String fileName, int size) {
        try {
            URL url = getClass().getClassLoader().getResource("view/icons/" + fileName);
            if (url != null) {
                Image img = new ImageIcon(url).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception ignored) {}
        return null;
    }
}
