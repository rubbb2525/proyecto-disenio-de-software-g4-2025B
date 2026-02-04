package view.componentes;

import java.awt.*;

/**
 * CONSTANTES DE DISEÑO VISUAL - Sistema Gestión de Ayudantes
 * Paleta de colores, tipografía y dimensiones estándar
 * Versión: 1.0 - Enero 2026
 */
public class ConstantesVisuales {
    
    // ═══════════════════════════════════════════════════════════════════
    // 🎨 PALETA DE COLORES
    // ═══════════════════════════════════════════════════════════════════
    
    // COLORES PRINCIPALES
    public static final Color COLOR_PRIMARIO = new Color(0, 61, 165);           // #003DA5
    public static final Color COLOR_PRIMARIO_HOVER = new Color(16, 84, 184);    // #1054B8
    public static final Color COLOR_PRIMARIO_OSCURO = new Color(0, 45, 122);    // #002D7A
    
    public static final Color COLOR_SECUNDARIO = new Color(74, 144, 226);       // #4A90E2
    public static final Color COLOR_SECUNDARIO_CLARO = new Color(122, 179, 245); // #7AB3F5
    
    // COLORES FUNCIONALES
    public static final Color COLOR_EXITO = new Color(46, 204, 113);            // #2ECC71
    public static final Color COLOR_EXITO_HOVER = new Color(39, 174, 96);       // #27AE60
    
    public static final Color COLOR_ADVERTENCIA = new Color(243, 156, 18);      // #F39C12
    public static final Color COLOR_ADVERTENCIA_HOVER = new Color(230, 126, 34);// #E67E22
    
    public static final Color COLOR_ERROR = new Color(231, 76, 60);             // #E74C3C
    public static final Color COLOR_ERROR_HOVER = new Color(192, 57, 43);       // #C0392B
    
    public static final Color COLOR_INFO = new Color(52, 152, 219);             // #3498DB
    public static final Color COLOR_INFO_HOVER = new Color(41, 128, 185);       // #2980B9
    
    // COLORES NEUTRALES
    public static final Color COLOR_FONDO_PRINCIPAL = new Color(245, 247, 250); // #F5F7FA
    public static final Color COLOR_FONDO_SECUNDARIO = new Color(236, 240, 241);// #ECF0F1
    public static final Color COLOR_TARJETA = Color.WHITE;                      // #FFFFFF
    
    public static final Color COLOR_BORDE = new Color(225, 232, 237);           // #E1E8ED
    public static final Color COLOR_BORDE_HOVER = new Color(189, 195, 199);     // #BDC3C7
    
    public static final Color COLOR_TEXTO_PRINCIPAL = new Color(44, 62, 80);    // #2C3E50
    public static final Color COLOR_TEXTO_SECUNDARIO = new Color(127, 140, 141);// #7F8C8D
    public static final Color COLOR_TEXTO_PLACEHOLDER = new Color(149, 165, 166);//#95A5A6
    public static final Color COLOR_TEXTO_DESHABILITADO = new Color(189, 195, 199);//#BDC3C7
    
    // COLORES DE ESTADO
    public static final Color COLOR_ACTIVO = COLOR_EXITO;
    public static final Color COLOR_INACTIVO = COLOR_ERROR;
    public static final Color COLOR_PENDIENTE = COLOR_ADVERTENCIA;
    
    // ═══════════════════════════════════════════════════════════════════
    // 🔤 TIPOGRAFÍA
    // ═══════════════════════════════════════════════════════════════════
    
    public static final String FUENTE_BASE = "Segoe UI";
    
    public static final Font FUENTE_TITULO_GRANDE = new Font(FUENTE_BASE, Font.BOLD, 24);
    public static final Font FUENTE_TITULO = new Font(FUENTE_BASE, Font.BOLD, 20);
    public static final Font FUENTE_SUBTITULO = new Font(FUENTE_BASE, Font.BOLD, 16);
    public static final Font FUENTE_TITULO_PEQUEÑO = new Font(FUENTE_BASE, Font.BOLD, 14);
    
    public static final Font FUENTE_NORMAL_GRANDE = new Font(FUENTE_BASE, Font.PLAIN, 16);
    public static final Font FUENTE_NORMAL = new Font(FUENTE_BASE, Font.PLAIN, 14);
    public static final Font FUENTE_NORMAL_PEQUEÑO = new Font(FUENTE_BASE, Font.PLAIN, 12);
    public static final Font FUENTE_MUY_PEQUEÑO = new Font(FUENTE_BASE, Font.PLAIN, 11);
    
    public static final Font FUENTE_NEGRITA = new Font(FUENTE_BASE, Font.BOLD, 14);
    public static final Font FUENTE_NEGRITA_PEQUEÑO = new Font(FUENTE_BASE, Font.BOLD, 12);
    
    // ═══════════════════════════════════════════════════════════════════
    // 📐 DIMENSIONES Y ESPACIADOS
    // ═══════════════════════════════════════════════════════════════════
    
    // Espaciados
    public static final int PADDING_XXS = 4;
    public static final int PADDING_XS = 8;
    public static final int PADDING_SM = 12;
    public static final int PADDING_MD = 16;    // ESTÁNDAR
    public static final int PADDING_LG = 20;
    public static final int PADDING_XL = 24;
    public static final int PADDING_XXL = 32;
    
    public static final int MARGIN_ENTRE_CAMPOS = 10;
    public static final int MARGIN_ENTRE_SECCIONES = 20;
    
    // Alturas de componentes
    public static final int ALTURA_CAMPO_TEXTO = 35;
    public static final int ALTURA_COMBO = 35;
    public static final int ALTURA_BOTON_NORMAL = 40;
    public static final int ALTURA_BOTON_PEQUEÑO = 32;
    public static final int ALTURA_BOTON_GRANDE = 48;
    public static final int ALTURA_HEADER = 70;
    public static final int ALTURA_FOOTER = 60;
    
    // Anchos de componentes
    public static final int ANCHO_CAMPO_CORTO = 120;
    public static final int ANCHO_CAMPO_MEDIO = 200;
    public static final int ANCHO_CAMPO_LARGO = 300;
    
    public static final int ANCHO_BOTON_MINIMO = 80;
    public static final int ANCHO_BOTON_NORMAL = 120;
    public static final int ANCHO_BOTON_GRANDE = 160;
    
    // Border radius
    public static final int RADIO_BORDE_PEQUEÑO = 6;
    public static final int RADIO_BORDE_NORMAL = 8;
    public static final int RADIO_BORDE_MEDIO = 10;
    public static final int RADIO_BORDE_GRANDE = 12;
    public static final int RADIO_BORDE_XL = 15;
    
    // Tamaños de ventana
    public static final Dimension VENTANA_PEQUEÑA = new Dimension(400, 300);
    public static final Dimension VENTANA_NORMAL = new Dimension(600, 500);
    public static final Dimension VENTANA_MEDIANA = new Dimension(800, 650);
    public static final Dimension VENTANA_GRANDE = new Dimension(1200, 800);
    public static final Dimension VENTANA_DASHBOARD = new Dimension(1400, 850);
    
    // ═══════════════════════════════════════════════════════════════════
    // 🚫 CONSTRUCTOR PRIVADO
    // ═══════════════════════════════════════════════════════════════════
    
    private ConstantesVisuales() {
        // Clase de constantes - No instanciar
    }
    
    // ═══════════════════════════════════════════════════════════════════
    // 🛠️ UTILIDADES
    // ═══════════════════════════════════════════════════════════════════
    
    /**
     * Crear color con transparencia
     */
    public static Color conTransparencia(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    
    /**
     * Obtener color de estado
     */
    public static Color obtenerColorEstado(String estado) {
        return switch (estado.toLowerCase()) {
            case "activo" -> COLOR_ACTIVO;
            case "inactivo" -> COLOR_INACTIVO;
            case "pendiente" -> COLOR_PENDIENTE;
            default -> COLOR_TEXTO_SECUNDARIO;
        };
    }
}
