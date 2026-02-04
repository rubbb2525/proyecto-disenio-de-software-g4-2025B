╔═══════════════════════════════════════════════════════════════════════╗
║   GUÍA DE REQUISITOS VISUALES - SISTEMA GESTIÓN DE AYUDANTES        ║
║   Estándares de diseño para vistas, diálogos y formularios          ║
║   Versión: 1.0 - Enero 2026                                         ║
╚═══════════════════════════════════════════════════════════════════════╝

═══════════════════════════════════════════════════════════════════════════
🎨 1. PALETA DE COLORES OFICIAL (OBLIGATORIA)
═══════════════════════════════════════════════════════════════════════════

COLORES PRINCIPALES:
  COLOR_PRIMARIO        = new Color(0, 61, 165)      // #003DA5 - Azul EPN
  COLOR_PRIMARIO_HOVER  = new Color(16, 84, 184)     // #1054B8
  COLOR_PRIMARIO_OSCURO = new Color(0, 45, 122)      // #002D7A
  
  COLOR_SECUNDARIO      = new Color(74, 144, 226)    // #4A90E2
  COLOR_SECUNDARIO_CLARO= new Color(122, 179, 245)   // #7AB3F5

COLORES FUNCIONALES:
  COLOR_EXITO           = new Color(46, 204, 113)    // #2ECC71 - Verde
  COLOR_EXITO_HOVER     = new Color(39, 174, 96)     // #27AE60
  
  COLOR_ADVERTENCIA     = new Color(243, 156, 18)    // #F39C12 - Amarillo
  COLOR_ADVERTENCIA_HOVER=new Color(230, 126, 34)    // #E67E22
  
  COLOR_ERROR           = new Color(231, 76, 60)     // #E74C3C - Rojo
  COLOR_ERROR_HOVER     = new Color(192, 57, 43)     // #C0392B
  
  COLOR_INFO            = new Color(52, 152, 219)    // #3498DB - Azul claro
  COLOR_INFO_HOVER      = new Color(41, 128, 185)    // #2980B9

COLORES NEUTRALES:
  COLOR_FONDO_PRINCIPAL = new Color(245, 247, 250)   // #F5F7FA - Gris muy claro
  COLOR_FONDO_SECUNDARIO= new Color(236, 240, 241)   // #ECF0F1
  COLOR_TARJETA         = Color.WHITE                // #FFFFFF - Blanco
  
  COLOR_BORDE           = new Color(225, 232, 237)   // #E1E8ED - Gris claro
  COLOR_BORDE_HOVER     = new Color(189, 195, 199)   // #BDC3C7
  
  COLOR_TEXTO_PRINCIPAL = new Color(44, 62, 80)      // #2C3E50 - Gris oscuro
  COLOR_TEXTO_SECUNDARIO= new Color(127, 140, 141)   // #7F8C8D
  COLOR_TEXTO_PLACEHOLDER=new Color(149, 165, 166)   // #95A5A6
  COLOR_TEXTO_DESHABILITADO=new Color(189,195,199)   // #BDC3C7

COLORES DE ESTADO:
  COLOR_ACTIVO          = new Color(46, 204, 113)    // Verde
  COLOR_INACTIVO        = new Color(231, 76, 60)     // Rojo
  COLOR_PENDIENTE       = new Color(243, 156, 18)    // Amarillo


═══════════════════════════════════════════════════════════════════════════
🔤 2. TIPOGRAFÍA ESTÁNDAR
═══════════════════════════════════════════════════════════════════════════

FUENTE BASE: "Segoe UI" (fallback: "Tahoma", "Arial")

TAMAÑOS Y ESTILOS:
  FUENTE_TITULO_GRANDE  = new Font("Segoe UI", Font.BOLD, 24)
  FUENTE_TITULO         = new Font("Segoe UI", Font.BOLD, 20)
  FUENTE_SUBTITULO      = new Font("Segoe UI", Font.BOLD, 16)
  FUENTE_TITULO_PEQUEÑO = new Font("Segoe UI", Font.BOLD, 14)
  
  FUENTE_NORMAL_GRANDE  = new Font("Segoe UI", Font.PLAIN, 16)
  FUENTE_NORMAL         = new Font("Segoe UI", Font.PLAIN, 14)
  FUENTE_NORMAL_PEQUEÑO = new Font("Segoe UI", Font.PLAIN, 12)
  FUENTE_MUY_PEQUEÑO    = new Font("Segoe UI", Font.PLAIN, 11)
  
  FUENTE_NEGRITA        = new Font("Segoe UI", Font.BOLD, 14)
  FUENTE_NEGRITA_PEQUEÑO= new Font("Segoe UI", Font.BOLD, 12)

USO:
  - Títulos de ventanas:        FUENTE_TITULO (20pt, Bold)
  - Títulos de secciones:       FUENTE_SUBTITULO (16pt, Bold)
  - Labels de formulario:       FUENTE_NORMAL (14pt, Plain)
  - Texto en campos:            FUENTE_NORMAL (14pt, Plain)
  - Botones:                    FUENTE_NEGRITA (14pt, Bold)
  - Descripciones/hints:        FUENTE_NORMAL_PEQUEÑO (12pt, Plain)
  - Badges/etiquetas:           FUENTE_NEGRITA_PEQUEÑO (12pt, Bold)


═══════════════════════════════════════════════════════════════════════════
📐 3. DIMENSIONES Y ESPACIADOS ESTÁNDAR
═══════════════════════════════════════════════════════════════════════════

ESPACIADOS (Padding/Margin):
  PADDING_XXS = 4px     // Espaciado mínimo
  PADDING_XS  = 8px     // Espaciado extra pequeño
  PADDING_SM  = 12px    // Espaciado pequeño
  PADDING_MD  = 16px    // Espaciado medio (ESTÁNDAR)
  PADDING_LG  = 20px    // Espaciado grande
  PADDING_XL  = 24px    // Espaciado extra grande
  PADDING_XXL = 32px    // Espaciado máximo
  
  MARGIN_ENTRE_CAMPOS   = 10px   // Espacio vertical entre campos
  MARGIN_ENTRE_SECCIONES= 20px   // Espacio entre secciones

ALTURAS DE COMPONENTES:
  ALTURA_CAMPO_TEXTO    = 35px   // TextField, PasswordField
  ALTURA_COMBO          = 35px   // JComboBox
  ALTURA_BOTON_NORMAL   = 40px   // Botones principales
  ALTURA_BOTON_PEQUEÑO  = 32px   // Botones secundarios
  ALTURA_BOTON_GRANDE   = 48px   // Botones destacados
  ALTURA_HEADER         = 60-70px // Headers de ventanas
  ALTURA_FOOTER         = 50-60px // Footers con botones

ANCHOS DE COMPONENTES:
  ANCHO_CAMPO_CORTO     = 120px  // Código, nivel, etc.
  ANCHO_CAMPO_MEDIO     = 200px  // Nombre, apellido
  ANCHO_CAMPO_LARGO     = 300px  // Correo, dirección
  ANCHO_CAMPO_COMPLETO  = 100%   // Ocupa todo el ancho disponible
  
  ANCHO_BOTON_MINIMO    = 80px   // Botones pequeños
  ANCHO_BOTON_NORMAL    = 120px  // Botones estándar
  ANCHO_BOTON_GRANDE    = 160px  // Botones principales

RADIOS DE BORDE (Border Radius):
  RADIO_BORDE_PEQUEÑO   = 6px    // Componentes pequeños
  RADIO_BORDE_NORMAL    = 8px    // Campos, badges
  RADIO_BORDE_MEDIO     = 10px   // Botones
  RADIO_BORDE_GRANDE    = 12px   // Tarjetas, paneles
  RADIO_BORDE_XL        = 15px   // Diálogos

TAMAÑOS DE VENTANA:
  VENTANA_PEQUEÑA       = 400x300px   // Confirmaciones simples
  VENTANA_NORMAL        = 600x500px   // Login, formularios simples
  VENTANA_MEDIANA       = 800x650px   // Formularios complejos
  VENTANA_GRANDE        = 1200x800px  // Ventanas principales
  VENTANA_DASHBOARD     = 1400x850px  // Dashboards


═══════════════════════════════════════════════════════════════════════════
🎯 4. HEADERS DE VENTANAS Y DIÁLOGOS
═══════════════════════════════════════════════════════════════════════════

ESTRUCTURA ESTÁNDAR:
┌─────────────────────────────────────────────────────────────┐
│ [ICONO] Título de la Ventana           [Botones de Acción] │ ← 60-70px altura
└─────────────────────────────────────────────────────────────┘

ESPECIFICACIONES:
  - Altura: 60-70px
  - Fondo: Degradado de COLOR_PRIMARIO a COLOR_PRIMARIO_HOVER
  - Padding: 12-16px horizontal, 12px vertical
  - Título: Fuente BOLD 18-20pt, color BLANCO
  - Subtítulo (opcional): Fuente PLAIN 12pt, blanco con 80% opacidad
  - Iconos: 20-24px, color BLANCO
  - Botones: Altura 40px, con iconos 16px

CÓDIGO EJEMPLO:
```java
JPanel crearHeader(String titulo, String subtitulo) {
    JPanel header = new JPanel(new BorderLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gradient = new GradientPaint(
                0, 0, COLOR_PRIMARIO,
                0, getHeight(), COLOR_PRIMARIO_HOVER
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    };
    header.setPreferredSize(new Dimension(0, 70));
    header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
    
    JPanel panelIzq = new JPanel();
    panelIzq.setOpaque(false);
    panelIzq.setLayout(new BoxLayout(panelIzq, BoxLayout.Y_AXIS));
    
    JLabel lblTitulo = new JLabel(titulo);
    lblTitulo.setForeground(Color.WHITE);
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
    
    JLabel lblSubtitulo = new JLabel(subtitulo);
    lblSubtitulo.setForeground(new Color(255, 255, 255, 200));
    lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    
    panelIzq.add(lblTitulo);
    if (subtitulo != null && !subtitulo.isEmpty()) {
        panelIzq.add(lblSubtitulo);
    }
    
    header.add(panelIzq, BorderLayout.WEST);
    return header;
}
```


═══════════════════════════════════════════════════════════════════════════
📝 5. FORMULARIOS Y CAMPOS DE ENTRADA
═══════════════════════════════════════════════════════════════════════════

ESTRUCTURA ESTÁNDAR DE CAMPO:
┌────────────────────────┐
│ Label del Campo (*)    │ ← Label con asterisco si es obligatorio
│ [Campo de entrada   ]  │ ← 35px altura, borde 1px
│ Texto de ayuda/error   │ ← 11px, gris o rojo según contexto
└────────────────────────┘
   ↑                ↑
   10px margin   10px margin

ESPECIFICACIONES DE CAMPOS:

TextField / PasswordField:
  - Altura: 35px
  - Borde normal: 1px sólido, COLOR_BORDE
  - Borde focus: 2px sólido, COLOR_PRIMARIO
  - Padding interno: 8px horizontal, 6px vertical
  - Fuente: FUENTE_NORMAL (14pt)
  - Color texto: COLOR_TEXTO_PRINCIPAL
  - Placeholder: COLOR_TEXTO_PLACEHOLDER (14pt, cursiva)
  - Border radius: 8px

JComboBox:
  - Altura: 35px
  - Borde: 1px sólido, COLOR_BORDE
  - Fondo: Blanco
  - Fuente: FUENTE_NORMAL (14pt)
  - Cursor: HAND_CURSOR
  - Border radius: 8px

JTextArea:
  - Borde: 1px sólido, COLOR_BORDE
  - Padding: 10px
  - Line wrap: true
  - Wrap style word: true
  - Fuente: FUENTE_NORMAL (14pt)
  - Scroll: Cuando altura > 150px

JSpinner:
  - Altura: 35px
  - Borde: 1px sólido, COLOR_BORDE
  - Fuente: FUENTE_NORMAL (14pt)

JCheckBox / JRadioButton:
  - Fuente: FUENTE_NORMAL (14pt)
  - Cursor: HAND_CURSOR
  - Espacio entre checkbox y texto: 8px

LABELS DE CAMPO:
  - Fuente: FUENTE_NEGRITA (14pt Bold)
  - Color: COLOR_TEXTO_PRINCIPAL
  - Margin bottom: 6px
  - Asterisco (*) para obligatorios: COLOR_ERROR

MENSAJES DE VALIDACIÓN:
  - Error: COLOR_ERROR, FUENTE_NORMAL_PEQUEÑO (12pt)
  - Success: COLOR_EXITO, FUENTE_NORMAL_PEQUEÑO (12pt)
  - Info: COLOR_INFO, FUENTE_NORMAL_PEQUEÑO (12pt)
  - Posición: Debajo del campo

CÓDIGO EJEMPLO:
```java
JPanel crearCampoFormulario(String label, JTextField campo, 
                            boolean obligatorio, String ayuda) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setOpaque(false);
    panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    
    // Label
    JLabel lblCampo = new JLabel(label + (obligatorio ? " *" : ""));
    lblCampo.setFont(new Font("Segoe UI", Font.BOLD, 14));
    lblCampo.setForeground(COLOR_TEXTO_PRINCIPAL);
    lblCampo.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    if (obligatorio) {
        // Colorear el asterisco de rojo
        String html = label + " <span style='color:#E74C3C'>*</span>";
        lblCampo.setText("<html>" + html + "</html>");
    }
    
    panel.add(lblCampo);
    panel.add(Box.createVerticalStrut(6));
    
    // Campo
    campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    campo.setForeground(COLOR_TEXTO_PRINCIPAL);
    campo.setBackground(Color.WHITE);
    campo.setBorder(BorderFactory.createCompoundBorder(
        new RoundedBorder(8, COLOR_BORDE, 1),
        BorderFactory.createEmptyBorder(6, 10, 6, 10)
    ));
    campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
    campo.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Efecto focus
    campo.addFocusListener(new FocusAdapter() {
        public void focusGained(FocusEvent e) {
            campo.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(8, COLOR_PRIMARIO, 2),
                BorderFactory.createEmptyBorder(5, 9, 5, 9)
            ));
        }
        public void focusLost(FocusEvent e) {
            campo.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(8, COLOR_BORDE, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
            ));
        }
    });
    
    panel.add(campo);
    
    // Texto de ayuda
    if (ayuda != null && !ayuda.isEmpty()) {
        panel.add(Box.createVerticalStrut(4));
        JLabel lblAyuda = new JLabel(ayuda);
        lblAyuda.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblAyuda.setForeground(COLOR_TEXTO_SECUNDARIO);
        lblAyuda.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblAyuda);
    }
    
    return panel;
}
```


═══════════════════════════════════════════════════════════════════════════
🔘 6. BOTONES
═══════════════════════════════════════════════════════════════════════════

TIPOS DE BOTONES:

1. BOTÓN PRIMARIO (Acción principal):
   - Fondo: COLOR_PRIMARIO
   - Hover: COLOR_PRIMARIO_HOVER
   - Texto: Blanco
   - Fuente: FUENTE_NEGRITA (14pt Bold)
   - Altura: 40px
   - Padding: 12px horizontal
   - Border radius: 10px
   - Cursor: HAND_CURSOR
   - Sin borde visible
   - Sin focus paint

2. BOTÓN ÉXITO (Confirmar, Guardar):
   - Fondo: COLOR_EXITO
   - Hover: COLOR_EXITO_HOVER
   - Texto: Blanco
   - Resto igual al primario

3. BOTÓN ERROR (Eliminar, Cancelar peligroso):
   - Fondo: COLOR_ERROR
   - Hover: COLOR_ERROR_HOVER
   - Texto: Blanco
   - Resto igual al primario

4. BOTÓN ADVERTENCIA (Acciones que requieren atención):
   - Fondo: COLOR_ADVERTENCIA
   - Hover: COLOR_ADVERTENCIA_HOVER
   - Texto: Blanco
   - Resto igual al primario

5. BOTÓN SECUNDARIO (Cancelar, Cerrar):
   - Fondo: Blanco
   - Borde: 2px sólido COLOR_PRIMARIO
   - Hover: Fondo COLOR_FONDO_PRINCIPAL
   - Texto: COLOR_PRIMARIO
   - Fuente: FUENTE_NEGRITA
   - Border radius: 10px

6. BOTÓN OUTLINE (Acciones menos importantes):
   - Similar al secundario
   - Borde 1px en lugar de 2px
   - Más discreto

ICONOS EN BOTONES:
  - Tamaño: 16px
  - Posición: Izquierda del texto
  - Espacio con texto: 8px
  - Color: Mismo que el texto

EFECTOS:
  - Hover: Cambio de color de fondo
  - Active: transform translateY(1px) (sutil)
  - Deshabilitado: Opacidad 50%, cursor default

CÓDIGO EJEMPLO:
```java
JButton crearBotonPrimario(String texto, Icon icono) {
    JButton btn = new JButton(texto, icono);
    btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
    btn.setForeground(Color.WHITE);
    btn.setBackground(COLOR_PRIMARIO);
    btn.setFocusPainted(false);
    btn.setBorderPainted(false);
    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    btn.setPreferredSize(new Dimension(120, 40));
    btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    
    // Efecto hover
    btn.addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            btn.setBackground(COLOR_PRIMARIO_HOVER);
        }
        public void mouseExited(MouseEvent e) {
            btn.setBackground(COLOR_PRIMARIO);
        }
    });
    
    return btn;
}
```


═══════════════════════════════════════════════════════════════════════════
🗂️ 7. TARJETAS Y PANELES
═══════════════════════════════════════════════════════════════════════════

TARJETA ESTÁNDAR:
  - Fondo: COLOR_TARJETA (Blanco)
  - Borde: 1px sólido COLOR_BORDE
  - Border radius: 12px
  - Padding: 20px
  - Sombra: 0 2px 4px rgba(0,0,0,0.1)

TARJETA HOVER (clickeable):
  - Hover: Fondo cambia a COLOR_FONDO_SECUNDARIO
  - Cursor: HAND_CURSOR
  - Borde: 2px COLOR_PRIMARIO (si está seleccionada)

PANEL DE SECCIÓN:
  - Fondo: Transparente o COLOR_FONDO_PRINCIPAL
  - Título de sección: FUENTE_SUBTITULO (16pt Bold)
  - Margin bottom del título: 12px
  - Padding: 16px

CÓDIGO EJEMPLO:
```java
JPanel crearTarjeta() {
    JPanel tarjeta = new JPanel();
    tarjeta.setBackground(Color.WHITE);
    tarjeta.setBorder(BorderFactory.createCompoundBorder(
        new RoundedBorder(12, COLOR_BORDE, 1),
        BorderFactory.createEmptyBorder(20, 20, 20, 20)
    ));
    return tarjeta;
}

JPanel crearTarjetaClickeable() {
    JPanel tarjeta = crearTarjeta();
    tarjeta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    
    tarjeta.addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            tarjeta.setBackground(COLOR_FONDO_SECUNDARIO);
        }
        public void mouseExited(MouseEvent e) {
            tarjeta.setBackground(Color.WHITE);
        }
    });
    
    return tarjeta;
}
```


═══════════════════════════════════════════════════════════════════════════
📊 8. TABLAS (JTable)
═══════════════════════════════════════════════════════════════════════════

HEADER DE TABLA:
  - Fondo: COLOR_PRIMARIO
  - Texto: Blanco
  - Fuente: FUENTE_NEGRITA (14pt Bold)
  - Altura: 40px
  - Padding: 10px horizontal
  - Sin borde entre columnas

CELDAS:
  - Fuente: FUENTE_NORMAL (14pt)
  - Color texto: COLOR_TEXTO_PRINCIPAL
  - Altura fila: 35-40px
  - Padding: 8px horizontal
  - Borde: 1px inferior COLOR_BORDE
  - Fondo alterno (opcional): Blanco y COLOR_FONDO_PRINCIPAL

SELECCIÓN:
  - Fondo: COLOR_SECUNDARIO_CLARO (con transparencia)
  - Texto: COLOR_TEXTO_PRINCIPAL (sin cambio)

HOVER:
  - Fondo: COLOR_FONDO_PRINCIPAL

CÓDIGO EJEMPLO:
```java
void estilizarTabla(JTable tabla) {
    // Header
    tabla.getTableHeader().setBackground(COLOR_PRIMARIO);
    tabla.getTableHeader().setForeground(Color.WHITE);
    tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
    tabla.getTableHeader().setPreferredSize(new Dimension(0, 40));
    tabla.getTableHeader().setBorder(null);
    
    // Tabla
    tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    tabla.setForeground(COLOR_TEXTO_PRINCIPAL);
    tabla.setBackground(Color.WHITE);
    tabla.setRowHeight(38);
    tabla.setSelectionBackground(COLOR_SECUNDARIO_CLARO);
    tabla.setSelectionForeground(COLOR_TEXTO_PRINCIPAL);
    tabla.setGridColor(COLOR_BORDE);
    tabla.setShowGrid(true);
    tabla.setShowHorizontalLines(true);
    tabla.setShowVerticalLines(false);
    tabla.setIntercellSpacing(new Dimension(0, 1));
    
    // Renderers personalizados
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    for (int i = 0; i < tabla.getColumnCount(); i++) {
        tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
}
```


═══════════════════════════════════════════════════════════════════════════
🏷️ 9. BADGES Y ETIQUETAS
═══════════════════════════════════════════════════════════════════════════

BADGE ESTÁNDAR:
  - Padding: 4px horizontal, 8px vertical
  - Border radius: 12px (más redondeado)
  - Fuente: FUENTE_NEGRITA_PEQUEÑO (12pt Bold)

TIPOS DE BADGES:

1. BADGE ACTIVO:
   - Fondo: COLOR_EXITO con 20% opacidad
   - Texto: COLOR_EXITO
   - Borde: 1px COLOR_EXITO con 80% opacidad

2. BADGE INACTIVO:
   - Fondo: COLOR_ERROR con 20% opacidad
   - Texto: COLOR_ERROR
   - Borde: 1px COLOR_ERROR con 80% opacidad

3. BADGE PENDIENTE:
   - Fondo: COLOR_ADVERTENCIA con 20% opacidad
   - Texto: COLOR_ADVERTENCIA
   - Borde: 1px COLOR_ADVERTENCIA con 80% opacidad

4. BADGE INFO:
   - Fondo: COLOR_INFO con 20% opacidad
   - Texto: COLOR_INFO
   - Borde: 1px COLOR_INFO con 80% opacidad

CÓDIGO EJEMPLO:
```java
JLabel crearBadge(String texto, Color color) {
    JLabel badge = new JLabel(texto);
    badge.setOpaque(true);
    badge.setBackground(new Color(
        color.getRed(), 
        color.getGreen(), 
        color.getBlue(), 
        50  // 20% opacidad
    ));
    badge.setForeground(color);
    badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
    badge.setBorder(BorderFactory.createCompoundBorder(
        new RoundedBorder(12, new Color(
            color.getRed(), 
            color.getGreen(), 
            color.getBlue(), 
            200  // 80% opacidad
        ), 1),
        BorderFactory.createEmptyBorder(4, 10, 4, 10)
    ));
    return badge;
}
```


═══════════════════════════════════════════════════════════════════════════
💬 10. MENSAJES Y NOTIFICACIONES
═══════════════════════════════════════════════════════════════════════════

TOAST MESSAGE (Preferido sobre JOptionPane):
  - Posición: Bottom-right
  - Padding: 16px horizontal, 12px vertical
  - Border radius: 10px
  - Fuente: FUENTE_NEGRITA (14pt)
  - Sombra: 0 4px 12px rgba(0,0,0,0.2)
  - Duración: 3 segundos
  - Animación: Slide-in desde derecha

TIPOS:
  - Success: Fondo COLOR_EXITO, texto blanco, icono ✓
  - Error: Fondo COLOR_ERROR, texto blanco, icono ✗
  - Warning: Fondo COLOR_ADVERTENCIA, texto blanco, icono ⚠
  - Info: Fondo COLOR_INFO, texto blanco, icono ℹ

DIÁLOGOS DE CONFIRMACIÓN (cuando es crítico):
  - Usar JOptionPane.showConfirmDialog
  - Título claro
  - Mensaje conciso
  - Botones: "Sí" / "No" o "Aceptar" / "Cancelar"

CÓDIGO TOAST:
```java
public static void mostrarToast(JFrame parent, String mensaje, 
                                TipoToast tipo) {
    JWindow toast = new JWindow(parent);
    JPanel panel = new JPanel();
    panel.setBackground(obtenerColorTipo(tipo));
    panel.setBorder(BorderFactory.createCompoundBorder(
        new RoundedBorder(10, new Color(0,0,0,50), 1),
        BorderFactory.createEmptyBorder(12, 16, 12, 16)
    ));
    
    JLabel label = new JLabel(obtenerIcono(tipo) + " " + mensaje);
    label.setForeground(Color.WHITE);
    label.setFont(new Font("Segoe UI", Font.BOLD, 14));
    panel.add(label);
    
    toast.add(panel);
    toast.pack();
    
    // Posicionar en bottom-right
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    toast.setLocation(
        screenSize.width - toast.getWidth() - 20,
        screenSize.height - toast.getHeight() - 60
    );
    
    toast.setVisible(true);
    
    // Auto-hide después de 3 segundos
    Timer timer = new Timer(3000, e -> {
        toast.setVisible(false);
        toast.dispose();
    });
    timer.setRepeats(false);
    timer.start();
}
```


═══════════════════════════════════════════════════════════════════════════
🎭 11. ANIMACIONES Y TRANSICIONES
═══════════════════════════════════════════════════════════════════════════

FADE-IN (Al abrir ventanas):
  - Duración: 200-300ms
  - Opacidad: 0 → 1
  - Incremento: 0.05 cada 10ms

HOVER EFFECTS:
  - Duración: 150ms
  - Cambio suave de color
  - No usar transform (complejo en Swing)

PULSE (Para notificaciones):
  - Duración: 400ms
  - Scale: 1.0 → 1.05 → 1.0
  - Repeticiones: 2-3

SLIDE-IN (Para toast messages):
  - Duración: 200ms
  - Desde derecha (fuera de pantalla → posición final)

CÓDIGO FADE-IN:
```java
void aplicarFadeIn(JFrame ventana) {
    ventana.setOpacity(0f);
    ventana.setVisible(true);
    
    Timer fadeIn = new Timer(10, null);
    fadeIn.addActionListener(e -> {
        float opacity = ventana.getOpacity();
        if (opacity < 1f) {
            ventana.setOpacity(Math.min(1f, opacity + 0.05f));
        } else {
            ((Timer)e.getSource()).stop();
        }
    });
    fadeIn.start();
}
```


═══════════════════════════════════════════════════════════════════════════
📱 12. RESPONSIVIDAD Y LAYOUTS
═══════════════════════════════════════════════════════════════════════════

LAYOUTS RECOMENDADOS:

1. FORMULARIOS:
   - GridBagLayout (control preciso)
   - BoxLayout vertical (más simple)

2. LISTAS/TARJETAS:
   - BoxLayout vertical
   - FlowLayout (para wrap automático)

3. HEADERS/FOOTERS:
   - BorderLayout
   - FlowLayout

4. DASHBOARD:
   - GridBagLayout
   - GridLayout para secciones uniformes

PADDING CONSISTENTE:
  - Ventanas: 20px todos los lados
  - Diálogos: 24px todos los lados
  - Paneles: 16px todos los lados
  - Entre secciones: 20px vertical

SCROLL:
  - Siempre que contenido > altura disponible
  - Unit increment: 16px
  - Sin bordes en JScrollPane
  - Background transparente


═══════════════════════════════════════════════════════════════════════════
🎨 13. ESTADOS VISUALES
═══════════════════════════════════════════════════════════════════════════

NORMAL:
  - Colores estándar
  - Sin efectos

HOVER:
  - Color de fondo cambia
  - Cursor cambia a HAND_CURSOR
  - Borde más oscuro (opcional)

FOCUS (campos):
  - Borde cambia a COLOR_PRIMARIO
  - Grosor borde: 2px
  - Sin outline nativo del SO

DISABLED:
  - Opacidad: 50%
  - Color texto: COLOR_TEXTO_DESHABILITADO
  - Cursor: DEFAULT_CURSOR
  - No interactivo

SELECTED:
  - Fondo: COLOR_SECUNDARIO_CLARO
  - Borde: COLOR_PRIMARIO (2px)

ERROR:
  - Borde: COLOR_ERROR (2px)
  - Texto de error debajo del campo


═══════════════════════════════════════════════════════════════════════════
✅ 14. CHECKLIST DE CONSISTENCIA VISUAL
═══════════════════════════════════════════════════════════════════════════

ANTES DE FINALIZAR UNA VISTA, VERIFICAR:

COLORES:
  [ ] Usa solo colores de la paleta oficial
  [ ] Headers usan COLOR_PRIMARIO con degradado
  [ ] Fondos principales usan COLOR_FONDO_PRINCIPAL
  [ ] Tarjetas usan COLOR_TARJETA (blanco)
  [ ] Bordes usan COLOR_BORDE

TIPOGRAFÍA:
  [ ] Fuente base es "Segoe UI"
  [ ] Títulos usan 18-20pt Bold
  [ ] Texto normal usa 14pt Plain
  [ ] Botones usan 14pt Bold
  [ ] No mezcla diferentes familias de fuentes

ESPACIADOS:
  [ ] Padding consistente (16-20px en contenedores)
  [ ] Margin entre campos es 10px
  [ ] Margin entre secciones es 20px
  [ ] No hay espacios irregulares

COMPONENTES:
  [ ] Campos de texto tienen 35px de altura
  [ ] Botones tienen 40px de altura
  [ ] Border radius es consistente (8-12px)
  [ ] Todos los botones tienen cursor HAND
  [ ] Campos tienen efecto focus (borde azul)

INTERACCIONES:
  [ ] Hover effects en elementos clickeables
  [ ] Mensajes usan Toast en lugar de JOptionPane
  [ ] Confirmaciones críticas usan diálogos
  [ ] Atajos de teclado documentados en tooltips

ACCESIBILIDAD:
  [ ] Labels asociados a campos
  [ ] Tooltips informativos en botones
  [ ] Campos obligatorios marcados con (*)
  [ ] Contraste suficiente texto/fondo
  [ ] Elementos clicables ≥ 40px


═══════════════════════════════════════════════════════════════════════════
📚 15. PLANTILLAS DE CÓDIGO
═══════════════════════════════════════════════════════════════════════════

PLANTILLA DIÁLOGO SIMPLE:
```java
public class DialogoEjemplo extends JDialog {
    private JTextField txtCampo;
    private JButton btnAceptar;
    private JButton btnCancelar;
    private boolean aceptado = false;
    
    public DialogoEjemplo(JFrame parent) {
        super(parent, "Título del Diálogo", true);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO_PRINCIPAL);
        
        // Header
        JPanel header = crearHeader("Título", "Subtítulo");
        
        // Contenido
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        contenido.setBackground(COLOR_FONDO_PRINCIPAL);
        
        // Agregar campos...
        
        // Footer con botones
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footer.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        footer.setBackground(COLOR_FONDO_PRINCIPAL);
        
        btnCancelar = new JButton("Cancelar");
        estilizarBotonSecundario(btnCancelar);
        btnCancelar.addActionListener(e -> dispose());
        
        btnAceptar = new JButton("Aceptar");
        estilizarBotonPrimario(btnAceptar);
        btnAceptar.addActionListener(e -> {
            aceptado = true;
            dispose();
        });
        
        footer.add(btnCancelar);
        footer.add(btnAceptar);
        
        panelPrincipal.add(header, BorderLayout.NORTH);
        panelPrincipal.add(contenido, BorderLayout.CENTER);
        panelPrincipal.add(footer, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    public boolean fueAceptado() {
        return aceptado;
    }
}
```

PLANTILLA VENTANA PRINCIPAL:
```java
public class VentanaPrincipal extends JFrame {
    private ControladorXXX controlador;
    
    public VentanaPrincipal(ControladorXXX controlador) {
        this.controlador = controlador;
        setTitle("Título de la Ventana");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));
        
        inicializarComponentes();
        cargarDatos();
        configurarEventos();
        aplicarFadeIn(this);
    }
    
    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO_PRINCIPAL);
        
        // Header
        JPanel header = crearHeader("Título Principal", "Descripción");
        
        // Contenido
        JPanel contenido = new JPanel();
        contenido.setBackground(COLOR_FONDO_PRINCIPAL);
        contenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // ... agregar componentes
        
        panelPrincipal.add(header, BorderLayout.NORTH);
        panelPrincipal.add(contenido, BorderLayout.CENTER);
        
        add(panelPrincipal);
    }
    
    private void cargarDatos() {
        // Cargar datos iniciales
    }
    
    private void configurarEventos() {
        // Configurar listeners
    }
}
```


═══════════════════════════════════════════════════════════════════════════
🎯 RESUMEN EJECUTIVO
═══════════════════════════════════════════════════════════════════════════

PILARES DE DISEÑO CONSISTENTE:

1. PALETA DE COLORES ÚNICA
   → Usa SOLO los colores definidos
   
2. TIPOGRAFÍA UNIFORME
   → "Segoe UI" en todos lados
   → Tamaños consistentes
   
3. ESPACIADOS REGULARES
   → 16-20px padding en contenedores
   → 10px entre campos
   → 20px entre secciones
   
4. COMPONENTES ESTANDARIZADOS
   → 35px altura campos
   → 40px altura botones
   → 8-12px border radius
   
5. INTERACCIONES PREDECIBLES
   → Hover en clickeables
   → Focus en campos
   → Toast messages
   
6. ANIMACIONES SUTILES
   → Fade-in al abrir
   → Transiciones suaves
   → Sin exageraciones

═══════════════════════════════════════════════════════════════════════════

✅ GUÍA DE REQUISITOS VISUALES COMPLETADA
📅 Versión: 1.0 - Enero 2026
🎨 Estado: LISTA PARA IMPLEMENTAR

═══════════════════════════════════════════════════════════════════════════
