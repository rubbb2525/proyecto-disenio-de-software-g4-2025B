package model;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa un reporte del sistema - VERSIÓN MEJORADA
 * Genera PDFs con diseño profesional y corporativo
 */
public class Reporte {
    // Colores corporativos EPN
    private static final DeviceRgb COLOR_PRIMARIO = new DeviceRgb(11, 45, 92);      // Azul profundo
    private static final DeviceRgb COLOR_SECUNDARIO = new DeviceRgb(45, 127, 249);  // Azul acento
    private static final DeviceRgb COLOR_EXITO = new DeviceRgb(34, 197, 94);        // Verde moderno
    private static final DeviceRgb COLOR_ADVERTENCIA = new DeviceRgb(245, 158, 11); // Ámbar
    private static final DeviceRgb COLOR_ERROR = new DeviceRgb(239, 68, 68);        // Rojo suave
    private static final DeviceRgb COLOR_GRIS_CLARO = new DeviceRgb(245, 247, 251); // Fondo suave
    private static final DeviceRgb COLOR_GRIS_MEDIO = new DeviceRgb(209, 213, 219); // Bordes
    private static final DeviceRgb COLOR_TEXTO = new DeviceRgb(31, 41, 55);         // Texto principal
    
    private String idReporte;
    private Date fechaGeneracion;
    private String tipo;
    private String titulo;
    private String contenido;
    private Map<String, Object> estadisticas;
    private List<Ayudante> ayudantes;

    public Reporte() {
        this.fechaGeneracion = new Date();
        this.estadisticas = new HashMap<>();
        this.ayudantes = new java.util.ArrayList<>();
    }

    public Reporte(String idReporte, String tipo, String titulo) {
        this.idReporte = idReporte;
        this.tipo = tipo;
        this.titulo = titulo;
        this.fechaGeneracion = new Date();
        this.estadisticas = new HashMap<>();
        this.ayudantes = new java.util.ArrayList<>();
    }

    /**
     * Exporta el reporte a PDF con diseño profesional
     */
    public File exportarPDF(String rutaArchivo) throws IOException {
        PdfWriter writer = new PdfWriter(rutaArchivo);
        PdfDocument pdfDoc = new PdfDocument(writer);
        pdfDoc.setDefaultPageSize(PageSize.A4);
        
        Document document = new Document(pdfDoc);
        document.setMargins(36, 36, 36, 36);
        
        PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        // Header corporativo
        agregarHeaderCorporativo(document, regular, bold);
        
        // Título del reporte
        Paragraph titulo = new Paragraph(getTitulo())
            .setFont(bold)
            .setFontSize(22)
            .setFontColor(COLOR_PRIMARIO)
            .setTextAlignment(TextAlignment.LEFT)
            .setMarginTop(6)
            .setMarginBottom(2);
        document.add(titulo);

        Paragraph subtitulo = new Paragraph((getTipo() != null ? getTipo().toUpperCase() : "GENERAL") + " • " + obtenerTimestamp())
            .setFont(regular)
            .setFontSize(9)
            .setFontColor(COLOR_GRIS_MEDIO)
            .setMarginBottom(10);
        document.add(subtitulo);

        // Línea divisoria
        LineSeparator separador = new LineSeparator(new SolidLine(1));
        separador.setStrokeColor(COLOR_GRIS_MEDIO);
        document.add(separador);
        document.add(new Paragraph("\n").setMarginTop(6));
        
        // Información del reporte
        agregarInfoReporte(document, regular, bold);
        
        // Estadísticas (si existen)
        if (getEstadisticas() != null && !getEstadisticas().isEmpty()) {
            document.add(new Paragraph("\n"));
            agregarSeccionEstadisticas(document, regular, bold);
        }
        
        // Contenido
        if (getContenido() != null && !getContenido().isEmpty()) {
            document.add(new Paragraph("\n"));
            agregarContenido(document, regular);
        }
        
        // Tabla de ayudantes (si existen)
        if (getAyudantes() != null && !getAyudantes().isEmpty()) {
            document.add(new Paragraph("\n"));
            agregarTablaAyudantesConProyectos(document, getAyudantes(), regular, bold);
        }
        
        // Footer
        agregarFooter(document, regular);
        
        document.close();
        return new File(rutaArchivo);
    }

    /**
     * Exporta lista de ayudantes a PDF con tabla profesional
     */
    public static File exportarAyudantesPDF(String rutaArchivo, List<Ayudante> ayudantes) throws IOException {
        PdfWriter writer = new PdfWriter(rutaArchivo);
        PdfDocument pdfDoc = new PdfDocument(writer);
        pdfDoc.setDefaultPageSize(PageSize.A4);
        
        Document document = new Document(pdfDoc);
        document.setMargins(36, 36, 36, 36);
        
        PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        // Header corporativo
        agregarHeaderCorporativo(document, regular, bold);
        
        // Título
        Paragraph titulo = new Paragraph("Reporte de Ayudantes Registrados")
            .setFont(bold)
            .setFontSize(22)
            .setFontColor(COLOR_PRIMARIO)
            .setMarginTop(6)
            .setMarginBottom(2);
        document.add(titulo);

        Paragraph subtitulo = new Paragraph("RESUMEN CONSOLIDADO • " + obtenerTimestamp())
            .setFont(regular)
            .setFontSize(9)
            .setFontColor(COLOR_GRIS_MEDIO)
            .setMarginBottom(10);
        document.add(subtitulo);
        
        // Línea divisoria
        LineSeparator separador = new LineSeparator(new SolidLine(1));
        separador.setStrokeColor(COLOR_GRIS_MEDIO);
        document.add(separador);
        document.add(new Paragraph("\n").setMarginTop(6));
        
        // Resumen en cards
        agregarResumenAyudantes(document, ayudantes, regular, bold);
        document.add(new Paragraph("\n"));
        
        // Tabla de ayudantes
        agregarTablaAyudantes(document, ayudantes, regular, bold);
        
        // Footer
        agregarFooter(document, regular);
        
        document.close();
        return new File(rutaArchivo);
    }

    /**
     * Header corporativo del documento
     */
    private static void agregarHeaderCorporativo(Document document, PdfFont regular, PdfFont bold) {
        Table headerTable = new Table(new float[]{1, 3});
        headerTable.setWidth(UnitValue.createPercentValue(100));
        headerTable.setBackgroundColor(COLOR_PRIMARIO);
        headerTable.setBorder(null);

        Cell logoCell = new Cell()
            .setBackgroundColor(COLOR_PRIMARIO)
            .setBorder(null)
            .setPadding(10)
            .setTextAlignment(TextAlignment.CENTER)
            .setVerticalAlignment(VerticalAlignment.MIDDLE);

        Image logoImagen = crearLogoEPNImagen(120, 48);
        if (logoImagen != null) {
            logoCell.add(logoImagen);
        } else {
            Paragraph logo = new Paragraph("EPN")
                .setFont(bold)
                .setFontSize(16)
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.CENTER);
            logoCell.add(logo);
        }

        Paragraph institucion = new Paragraph()
            .add(new Text("ESCUELA POLITÉCNICA NACIONAL\n")
                .setFont(bold)
                .setFontSize(12)
                .setFontColor(ColorConstants.WHITE))
            .add(new Text("Facultad de Ingeniería de Sistemas\n")
                .setFont(regular)
                .setFontSize(10)
                .setFontColor(ColorConstants.WHITE))
            .add(new Text("Sistema de Gestión de Ayudantes")
                .setFont(regular)
                .setFontSize(9)
                .setFontColor(ColorConstants.WHITE))
            .setTextAlignment(TextAlignment.RIGHT);

        Cell infoCell = new Cell()
            .add(institucion)
            .setBorder(null)
            .setPadding(10)
            .setVerticalAlignment(VerticalAlignment.MIDDLE);

        headerTable.addCell(logoCell);
        headerTable.addCell(infoCell);

        document.add(headerTable);
        document.add(new Paragraph("\n").setMarginTop(6));
    }

    private static Image crearLogoEPNImagen(float maxWidth, float maxHeight) {
        try {
            ImageData data = cargarLogoEPNData();
            if (data == null) {
                return null;
            }
            Image image = new Image(data);
            image.scaleToFit(maxWidth, maxHeight);
            return image;
        } catch (IOException e) {
            return null;
        }
    }

    private static ImageData cargarLogoEPNData() throws IOException {
        try (InputStream stream = Reporte.class.getResourceAsStream("/view/componentes/EPN_logo_big.png")) {
            if (stream != null) {
                return ImageDataFactory.create(stream.readAllBytes());
            }
        }

        Path pathVista = Paths.get("src/view/componentes/EPN_logo_big.png");
        if (Files.exists(pathVista)) {
            return ImageDataFactory.create(pathVista.toAbsolutePath().toString());
        }

        Path pathModel = Paths.get("src/model/components/EPN_logo_big.png");
        if (Files.exists(pathModel)) {
            return ImageDataFactory.create(pathModel.toAbsolutePath().toString());
        }

        return null;
    }

    /**
     * Información del reporte en formato de cards
     */
    private void agregarInfoReporte(Document document, PdfFont regular, PdfFont bold) {
        Paragraph tituloResumen = new Paragraph("RESUMEN DEL REPORTE")
            .setFont(bold)
            .setFontSize(12)
            .setFontColor(COLOR_PRIMARIO)
            .setMarginBottom(6);
        document.add(tituloResumen);

        Table infoTable = new Table(new float[]{1, 1, 1});
        infoTable.setWidth(UnitValue.createPercentValue(100));
        infoTable.setMarginBottom(6);
        
        // Card 1: Tipo
        Cell cardTipo = crearCard(
            "TIPO DE REPORTE",
            getTipo() != null ? getTipo().toUpperCase() : "GENERAL",
            regular, bold, COLOR_SECUNDARIO
        );
        
        // Card 2: ID
        Cell cardId = crearCard(
            "ID REPORTE",
            getIdReporte() != null ? getIdReporte() : "N/A",
            regular, bold, COLOR_EXITO
        );
        
        // Card 3: Fecha
        Cell cardFecha = crearCard(
            "FECHA GENERACIÓN",
            obtenerTimestamp(),
            regular, bold, COLOR_ADVERTENCIA
        );
        
        infoTable.addCell(cardTipo);
        infoTable.addCell(cardId);
        infoTable.addCell(cardFecha);
        
        document.add(infoTable);
    }

    /**
     * Sección de estadísticas con diseño de cards
     */
    private void agregarSeccionEstadisticas(Document document, PdfFont regular, PdfFont bold) {
        Paragraph tituloEstadisticas = new Paragraph("INDICADORES CLAVE")
            .setFont(bold)
            .setFontSize(12)
            .setFontColor(COLOR_PRIMARIO)
            .setMarginBottom(6);
        document.add(tituloEstadisticas);
        
        Map<String, Object> stats = getEstadisticas();
        
        // Crear tabla de estadísticas
        int numStats = stats.size();
        int cols = Math.min(numStats, 4);
        float[] columnWidths = new float[cols];
        for (int i = 0; i < cols; i++) {
            columnWidths[i] = 1;
        }
        
        Table statsTable = new Table(columnWidths);
        statsTable.setWidth(UnitValue.createPercentValue(100));
        statsTable.setMarginBottom(4);
        
        int contador = 0;
        for (Map.Entry<String, Object> entry : stats.entrySet()) {
            String clave = entry.getKey();
            String valor = formatearValorEstadistica(entry.getValue());
            
            Cell statCell = crearStatCard(
                formatearNombreEstadistica(clave),
                valor,
                regular, bold
            );
            
            statsTable.addCell(statCell);
            contador++;
        }
        
        // Rellenar celdas vacías si es necesario
        while (contador % cols != 0) {
            statsTable.addCell(new Cell().setBorder(null));
            contador++;
        }
        
        document.add(statsTable);
    }

    /**
     * Resumen de ayudantes en cards
     */
    private static void agregarResumenAyudantes(Document document, List<Ayudante> ayudantes, 
                                                PdfFont regular, PdfFont bold) {
        Paragraph tituloResumen = new Paragraph("RESUMEN DE AYUDANTES")
            .setFont(bold)
            .setFontSize(12)
            .setFontColor(COLOR_PRIMARIO)
            .setMarginBottom(6);
        document.add(tituloResumen);

        long activos = ayudantes.stream().filter(Ayudante::esActivo).count();
        long inactivos = ayudantes.size() - activos;
        double iraPromedio = ayudantes.stream()
            .filter(Ayudante::esActivo)
            .mapToDouble(Ayudante::getIRA)
            .average()
            .orElse(0.0);
        
        Table resumenTable = new Table(new float[]{1, 1, 1, 1});
        resumenTable.setWidth(UnitValue.createPercentValue(100));
        
        resumenTable.addCell(crearCard("TOTAL", String.valueOf(ayudantes.size()), 
            regular, bold, COLOR_PRIMARIO));
        resumenTable.addCell(crearCard("ACTIVOS", String.valueOf(activos), 
            regular, bold, COLOR_EXITO));
        resumenTable.addCell(crearCard("INACTIVOS", String.valueOf(inactivos), 
            regular, bold, COLOR_ERROR));
        resumenTable.addCell(crearCard("IRA PROMEDIO", String.format("%.2f", iraPromedio), 
            regular, bold, COLOR_ADVERTENCIA));
        
        document.add(resumenTable);
    }

    /**
     * Tabla profesional de ayudantes
     */
    private static void agregarTablaAyudantes(Document document, List<Ayudante> ayudantes, 
                                             PdfFont regular, PdfFont bold) {
        Paragraph tituloTabla = new Paragraph("DETALLE DE AYUDANTES")
            .setFont(bold)
            .setFontSize(12)
            .setFontColor(COLOR_PRIMARIO)
            .setMarginBottom(6);
        document.add(tituloTabla);
        
        float[] columnWidths = {70, 120, 90, 40, 45, 55, 60};
        Table table = new Table(columnWidths);
        table.setWidth(UnitValue.createPercentValue(100));
        table.setBorder(new SolidBorder(COLOR_GRIS_MEDIO, 0.5f));
        
        // Headers
        String[] headers = {"Código", "Nombres", "Carrera", "Nivel", "IRA", "Horas", "Estado"};
        for (String header : headers) {
            Cell headerCell = new Cell()
                .add(new Paragraph(header)
                    .setFont(bold)
                    .setFontSize(9)
                    .setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(COLOR_SECUNDARIO)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPadding(7);
            table.addHeaderCell(headerCell);
        }
        
        // Datos con filas alternadas
        int fila = 0;
        for (Ayudante a : ayudantes) {
            DeviceRgb colorFondo = (fila % 2 == 0) ? new DeviceRgb(255, 255, 255) : COLOR_GRIS_CLARO;
            
            // Código
            table.addCell(crearCeldaDato(a.getCodigoUnico(), regular, colorFondo, TextAlignment.LEFT));
            
            // Nombres
            String nombreCompleto = a.getNombresCompletos();
            if (nombreCompleto.length() > 25) {
                nombreCompleto = nombreCompleto.substring(0, 22) + "...";
            }
            table.addCell(crearCeldaDato(nombreCompleto, regular, colorFondo, TextAlignment.LEFT));
            
            // Carrera
            String carrera = a.getCarrera();
            if (carrera.length() > 15) {
                carrera = carrera.substring(0, 12) + "...";
            }
            table.addCell(crearCeldaDato(carrera, regular, colorFondo, TextAlignment.LEFT));
            
            // Nivel
            table.addCell(crearCeldaDato(String.valueOf(a.getNivel()), regular, 
                colorFondo, TextAlignment.CENTER));
            
            // IRA
            table.addCell(crearCeldaDato(String.format("%.2f", a.getIRA()), regular, 
                colorFondo, TextAlignment.CENTER));
            
            // Horas
            table.addCell(crearCeldaDato(String.valueOf(a.getHorasSemanales()), regular, 
                colorFondo, TextAlignment.CENTER));
            
            // Estado con color
            Cell estadoCell = new Cell()
                .add(new Paragraph(a.esActivo() ? "Activo" : "Inactivo")
                    .setFont(bold)
                    .setFontSize(8)
                    .setFontColor(a.esActivo() ? COLOR_EXITO : COLOR_ERROR))
                .setBackgroundColor(colorFondo)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPadding(6);
            table.addCell(estadoCell);
            
            fila++;
        }
        
        document.add(table);
    }

    /**
     * Tabla de ayudantes con sus proyectos asignados (para reportes generales)
     */
    private static void agregarTablaAyudantesConProyectos(Document document, List<Ayudante> ayudantes, 
                                                         PdfFont regular, PdfFont bold) {
        Paragraph tituloTabla = new Paragraph("LISTA DE AYUDANTES Y PROYECTOS ASIGNADOS")
            .setFont(bold)
            .setFontSize(12)
            .setFontColor(COLOR_PRIMARIO)
            .setMarginBottom(6);
        document.add(tituloTabla);
        
        float[] columnWidths = {80, 130, 90, 40, 35, 60, 75};
        Table table = new Table(columnWidths);
        table.setWidth(UnitValue.createPercentValue(100));
        table.setBorder(new SolidBorder(COLOR_GRIS_MEDIO, 0.5f));
        
        // Headers
        String[] headers = {"Código", "Nombres", "Proyecto", "Nivel", "IRA", "Horas", "Estado"};
        for (String header : headers) {
            Cell headerCell = new Cell()
                .add(new Paragraph(header)
                    .setFont(bold)
                    .setFontSize(9)
                    .setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(COLOR_SECUNDARIO)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPadding(7);
            table.addHeaderCell(headerCell);
        }
        
        // Datos con filas alternadas
        int fila = 0;
        for (Ayudante a : ayudantes) {
            DeviceRgb colorFondo = (fila % 2 == 0) ? new DeviceRgb(255, 255, 255) : COLOR_GRIS_CLARO;
            
            // Código
            table.addCell(crearCeldaDato(a.getCodigoUnico(), regular, colorFondo, TextAlignment.CENTER));
            
            // Nombres
            String nombreCompleto = a.getNombresCompletos();
            if (nombreCompleto.length() > 25) {
                nombreCompleto = nombreCompleto.substring(0, 22) + "...";
            }
            table.addCell(crearCeldaDato(nombreCompleto, regular, colorFondo, TextAlignment.LEFT));
            
            // Proyecto asignado
            String proyecto = "N/A";
            if (a.getProyectoAsignado() != null) {
                proyecto = a.getProyectoAsignado().getNombreProyecto();
                if (proyecto.length() > 20) {
                    proyecto = proyecto.substring(0, 17) + "...";
                }
            }
            table.addCell(crearCeldaDato(proyecto, regular, colorFondo, TextAlignment.LEFT));
            
            // Nivel
            table.addCell(crearCeldaDato(String.valueOf(a.getNivel()), regular, 
                colorFondo, TextAlignment.CENTER));
            
            // IRA
            table.addCell(crearCeldaDato(String.format("%.2f", a.getIRA()), regular, 
                colorFondo, TextAlignment.CENTER));
            
            // Horas
            table.addCell(crearCeldaDato(String.valueOf(a.getHorasSemanales()), regular, 
                colorFondo, TextAlignment.CENTER));
            
            // Estado con color
            Cell estadoCell = new Cell()
                .add(new Paragraph(a.esActivo() ? "Activo" : "Inactivo")
                    .setFont(bold)
                    .setFontSize(8)
                    .setFontColor(a.esActivo() ? COLOR_EXITO : COLOR_ERROR))
                .setBackgroundColor(colorFondo)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPadding(6);
            table.addCell(estadoCell);
            
            fila++;
        }
        
        document.add(table);
    }

    /**
     * Footer del documento
     */
    private static void agregarFooter(Document document, PdfFont regular) {
        document.add(new Paragraph("\n"));

        Table footerTable = new Table(new float[]{3, 2});
        footerTable.setWidth(UnitValue.createPercentValue(100));
        footerTable.setBackgroundColor(COLOR_GRIS_CLARO);
        footerTable.setBorder(new SolidBorder(COLOR_GRIS_MEDIO, 0.5f));

        Paragraph izquierda = new Paragraph("Documento confidencial - Sistema de Gestión de Ayudantes FIS-EPN")
            .setFont(regular)
            .setFontSize(8)
            .setFontColor(COLOR_TEXTO);

        Paragraph derecha = new Paragraph("Generado: " + obtenerTimestamp())
            .setFont(regular)
            .setFontSize(8)
            .setFontColor(COLOR_TEXTO)
            .setTextAlignment(TextAlignment.RIGHT);

        footerTable.addCell(new Cell().add(izquierda).setBorder(null).setPadding(6));
        footerTable.addCell(new Cell().add(derecha).setBorder(null).setPadding(6));

        document.add(footerTable);
    }

    /**
     * Agrega contenido en formato de párrafos
     */
    private void agregarContenido(Document document, PdfFont regular) throws IOException {
        Paragraph tituloContenido = new Paragraph("DETALLES DEL REPORTE")
            .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
            .setFontSize(12)
            .setFontColor(COLOR_PRIMARIO)
            .setMarginBottom(6);
        document.add(tituloContenido);

        Paragraph contenidoParagraph = new Paragraph(getContenido())
            .setFont(regular)
            .setFontSize(9)
            .setTextAlignment(TextAlignment.JUSTIFIED)
            .setFontColor(COLOR_TEXTO);

        Table box = new Table(1);
        box.setWidth(UnitValue.createPercentValue(100));
        box.setBackgroundColor(COLOR_GRIS_CLARO);
        box.setBorder(new SolidBorder(COLOR_GRIS_MEDIO, 0.5f));
        box.addCell(new Cell().add(contenidoParagraph).setBorder(null).setPadding(10));

        document.add(box);
    }

    /**
     * Crea un card visual para información
     */
    private static Cell crearCard(String titulo, String valor, PdfFont regular, 
                                 PdfFont bold, DeviceRgb color) {
        Paragraph cardContent = new Paragraph()
            .add(new Text(titulo + "\n")
                .setFont(regular)
                .setFontSize(7.5f)
                .setFontColor(COLOR_GRIS_MEDIO))
            .add(new Text(valor)
                .setFont(bold)
                .setFontSize(15)
                .setFontColor(COLOR_TEXTO))
            .setTextAlignment(TextAlignment.LEFT)
            .setMarginTop(2)
            .setMarginBottom(2);
        
        Cell cell = new Cell()
            .add(cardContent)
            .setBackgroundColor(COLOR_GRIS_CLARO)
            .setBorder(new SolidBorder(COLOR_GRIS_MEDIO, 0.5f))
            .setBorderLeft(new SolidBorder(color, 2))
            .setPadding(10)
            .setTextAlignment(TextAlignment.LEFT)
            .setVerticalAlignment(VerticalAlignment.MIDDLE);
        
        return cell;
    }

    /**
     * Crea un card de estadística
     */
    private static Cell crearStatCard(String nombre, String valor, PdfFont regular, PdfFont bold) {
        Paragraph statContent = new Paragraph()
            .add(new Text(nombre + "\n")
                .setFont(regular)
                .setFontSize(8)
                .setFontColor(COLOR_GRIS_MEDIO))
            .add(new Text(valor)
                .setFont(bold)
                .setFontSize(14)
                .setFontColor(COLOR_TEXTO))
            .setTextAlignment(TextAlignment.LEFT);
        
        Cell cell = new Cell()
            .add(statContent)
            .setBackgroundColor(ColorConstants.WHITE)
            .setBorder(new SolidBorder(COLOR_GRIS_MEDIO, 0.5f))
            .setPadding(10)
            .setTextAlignment(TextAlignment.LEFT)
            .setVerticalAlignment(VerticalAlignment.MIDDLE)
            .setMarginRight(4)
            .setMarginBottom(4);
        
        return cell;
    }

    /**
     * Crea una celda de dato para la tabla
     */
    private static Cell crearCeldaDato(String texto, PdfFont font, DeviceRgb fondo, 
                                      TextAlignment alineacion) {
        return new Cell()
            .add(new Paragraph(texto)
                .setFont(font)
                .setFontSize(8.5f)
                .setFontColor(COLOR_TEXTO))
            .setBackgroundColor(fondo)
            .setBorder(new SolidBorder(COLOR_GRIS_MEDIO, 0.3f))
            .setTextAlignment(alineacion)
            .setVerticalAlignment(VerticalAlignment.MIDDLE)
            .setPadding(6);
    }

    /**
     * Formatea el nombre de una estadística
     */
    private String formatearNombreEstadistica(String clave) {
        return clave.replace("_", " ")
                   .replace("promedio", "PROMEDIO")
                   .replace("total", "TOTAL")
                   .replace("activos", "ACTIVOS")
                   .toUpperCase();
    }

    /**
     * Formatea el valor de una estadística
     */
    private String formatearValorEstadistica(Object valor) {
        if (valor instanceof Double) {
            return String.format("%.2f", (Double) valor);
        } else if (valor instanceof Float) {
            return String.format("%.2f", (Float) valor);
        }
        return String.valueOf(valor);
    }

    /**
     * Obtiene timestamp formateado
     */
    private static String obtenerTimestamp() {
        return LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    // Exportar a Excel (placeholder)
    public java.io.File exportarExcel() {
        throw new UnsupportedOperationException("Exportación a Excel no implementada aún");
    }

    // Getters y Setters
    public String getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(String idReporte) {
        this.idReporte = idReporte;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Map<String, Object> getEstadisticas() {
        return estadisticas;
    }

    public void setEstadisticas(Map<String, Object> estadisticas) {
        this.estadisticas = estadisticas;
    }

    public List<Ayudante> getAyudantes() {
        return ayudantes;
    }

    public void setAyudantes(List<Ayudante> ayudantes) {
        this.ayudantes = ayudantes;
    }
}