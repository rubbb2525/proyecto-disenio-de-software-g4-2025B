package model;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa un reporte del sistema
 */
public class Reporte {
    private String idReporte;
    private Date fechaGeneracion;
    private String tipo;
    private String titulo;
    private String contenido;
    private Map<String, Object> estadisticas;

    public Reporte() {
        this.fechaGeneracion = new Date();
        this.estadisticas = new HashMap<>();
    }

    public Reporte(String idReporte, String tipo, String titulo) {
        this.idReporte = idReporte;
        this.tipo = tipo;
        this.titulo = titulo;
        this.fechaGeneracion = new Date();
        this.estadisticas = new HashMap<>();
    }

    public File exportarPDF(String rutaArchivo) throws IOException {
        PdfWriter writer = new PdfWriter(rutaArchivo);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        // Encabezado
        Paragraph encabezado = new Paragraph("Sistema de Gestión de Ayudantes")
            .setFontSize(10)
            .setFont(regular)
            .setTextAlignment(TextAlignment.CENTER)
            .setFontColor(ColorConstants.GRAY);
        document.add(encabezado);

        Paragraph fecha = new Paragraph("Generado: " + obtenerTimestamp())
            .setFontSize(9)
            .setFont(regular)
            .setTextAlignment(TextAlignment.CENTER)
            .setFontColor(ColorConstants.GRAY);
        document.add(fecha);

        document.add(new Paragraph("\n"));

        // Título
        Paragraph titulo = new Paragraph(getTitulo())
            .setFontSize(18)
            .setFont(bold)
            .setTextAlignment(TextAlignment.CENTER);
        document.add(titulo);

        document.add(new Paragraph("\n"));

        // Contenido
        Paragraph contenido = new Paragraph(getContenido())
            .setFontSize(11)
            .setFont(regular)
            .setTextAlignment(TextAlignment.JUSTIFIED);
        document.add(contenido);

        // Estadísticas
        if (getEstadisticas() != null && !getEstadisticas().isEmpty()) {
            document.add(new Paragraph("\n"));

            Paragraph estadisticasHeader = new Paragraph("Estadísticas:")
                .setFontSize(12)
                .setFont(bold);
            document.add(estadisticasHeader);

            Paragraph stats = new Paragraph(String.valueOf(getEstadisticas()))
                .setFontSize(10)
                .setFont(regular);
            document.add(stats);
        }

        document.close();
        return new File(rutaArchivo);
    }

    public static File exportarAyudantesPDF(String rutaArchivo, List<Ayudante> ayudantes) throws IOException {
        PdfWriter writer = new PdfWriter(rutaArchivo);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        // Encabezado
        Paragraph encabezado = new Paragraph("Sistema de Gestión de Ayudantes")
            .setFontSize(10)
            .setFont(regular)
            .setTextAlignment(TextAlignment.CENTER)
            .setFontColor(ColorConstants.GRAY);
        document.add(encabezado);

        Paragraph fecha = new Paragraph("Generado: " + obtenerTimestamp())
            .setFontSize(9)
            .setFont(regular)
            .setTextAlignment(TextAlignment.CENTER)
            .setFontColor(ColorConstants.GRAY);
        document.add(fecha);

        document.add(new Paragraph("\n"));

        // Título
        Paragraph titulo = new Paragraph("Reporte de Ayudantes Registrados")
            .setFontSize(16)
            .setFont(bold)
            .setTextAlignment(TextAlignment.CENTER);
        document.add(titulo);

        // Resumen
        Paragraph resumen = new Paragraph("Total de registros: " + ayudantes.size())
            .setFontSize(10)
            .setFont(regular);
        document.add(resumen);
        document.add(new Paragraph("\n"));

        // Tabla
        float[] columnWidths = {60, 100, 80, 50, 50, 60};
        Table table = new Table(columnWidths);
        table.setBackgroundColor(ColorConstants.WHITE);

        // Headers
        String[] headers = {"Código", "Nombres", "Carrera", "Nivel", "IRA", "Horas Semanales"};
        for (String header : headers) {
            Cell cell = new Cell()
                .setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(74, 144, 226))
                .add(new Paragraph(header)
                    .setFont(bold)
                    .setFontColor(ColorConstants.WHITE)
                    .setTextAlignment(TextAlignment.CENTER));
            table.addCell(cell);
        }

        // Datos con colores alternados
        boolean alterno = false;
        for (Ayudante a : ayudantes) {
            com.itextpdf.kernel.colors.DeviceRgb colorFondo = alterno
                ? new com.itextpdf.kernel.colors.DeviceRgb(240, 247, 255)
                : new com.itextpdf.kernel.colors.DeviceRgb(255, 255, 255);

            Cell c1 = new Cell().setBackgroundColor(colorFondo)
                .add(new Paragraph(a.getCodigoUnico()).setFont(regular));
            Cell c2 = new Cell().setBackgroundColor(colorFondo)
                .add(new Paragraph(a.getNombresCompletos()).setFont(regular));
            Cell c3 = new Cell().setBackgroundColor(colorFondo)
                .add(new Paragraph(a.getCarrera()).setFont(regular));
            Cell c4 = new Cell().setBackgroundColor(colorFondo)
                .add(new Paragraph(String.valueOf(a.getNivel()))
                    .setFont(regular)
                    .setTextAlignment(TextAlignment.CENTER));
            Cell c5 = new Cell().setBackgroundColor(colorFondo)
                .add(new Paragraph(String.format("%.2f", a.getIRA()))
                    .setFont(regular)
                    .setTextAlignment(TextAlignment.CENTER));
            Cell c6 = new Cell().setBackgroundColor(colorFondo)
                .add(new Paragraph(String.valueOf(a.getHorasSemanales()))
                    .setFont(regular)
                    .setTextAlignment(TextAlignment.CENTER));

            table.addCell(c1);
            table.addCell(c2);
            table.addCell(c3);
            table.addCell(c4);
            table.addCell(c5);
            table.addCell(c6);

            alterno = !alterno;
        }

        document.add(table);

        // Pie de página
        document.add(new Paragraph("\n"));
        Paragraph pie = new Paragraph("Reporte confidencial - Sistema FIS-EPN")
            .setFontSize(8)
            .setFont(regular)
            .setTextAlignment(TextAlignment.CENTER)
            .setFontColor(ColorConstants.GRAY);
        document.add(pie);

        document.close();
        return new File(rutaArchivo);
    }

    public java.io.File exportarExcel() {
        // Implementación pendiente - requiere Apache POI
        throw new UnsupportedOperationException("Exportación a Excel no implementada aún");
    }

    private static String obtenerTimestamp() {
        return LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
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
}
