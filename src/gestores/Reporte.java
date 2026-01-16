package gestores;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Reporte {
    private String idReporte;
    private Date fechaGeneracion;
    private String tipo;
    private String titulo;
    private String contenido;
    private Map<String, Object> datosTabla;
    private List<String> secciones;
    private List<Map<String, Object>> graficos;
    private String conclusiones;

    // Constructores
    public Reporte() {
        this.idReporte = UUID.randomUUID().toString();
        this.fechaGeneracion = new Date();
        this.datosTabla = new HashMap<>();
        this.secciones = new ArrayList<>();
        this.graficos = new ArrayList<>();
        this.contenido = "";
    }

    public Reporte(String tipo, String titulo) {
        this();
        this.tipo = tipo;
        this.titulo = titulo;
    }

    // Métodos de negocio
    public void agregarSeccion(String titulo, String contenido) {
        String seccion = String.format("\n=== %s ===\n%s\n", titulo, contenido);
        secciones.add(seccion);
        this.contenido += seccion;
    }

    public void agregarDato(String clave, Object valor) {
        datosTabla.put(clave, valor);
    }

    public void agregarGrafico(String tipo, Map<String, Object> datos) {
        Map<String, Object> grafico = new HashMap<>();
        grafico.put("tipo", tipo);
        grafico.put("datos", datos);
        graficos.add(grafico);
    }

    public void agregarConclusion(String conclusion) {
        if (this.conclusiones == null) {
            this.conclusiones = "";
        }
        this.conclusiones += conclusion + "\n";
    }

    public File exportarPDF() {
        // Simulación de exportación a PDF
        String nombreArchivo = "reporte_" + idReporte.substring(0, 8) + ".pdf";
        System.out.println("Exportando reporte a PDF: " + nombreArchivo);
        return new File(nombreArchivo);
    }

    public File exportarExcel() {
        // Simulación de exportación a Excel
        String nombreArchivo = "reporte_" + idReporte.substring(0, 8) + ".xlsx";
        System.out.println("Exportando reporte a Excel: " + nombreArchivo);
        return new File(nombreArchivo);
    }

    public File exportarHTML() {
        // Simulación de exportación a HTML
        String nombreArchivo = "reporte_" + idReporte.substring(0, 8) + ".html";
        System.out.println("Exportando reporte a HTML: " + nombreArchivo);
        return new File(nombreArchivo);
    }

    public String mostrarResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESUMEN DEL REPORTE ===\n");
        sb.append("ID: ").append(idReporte.substring(0, 8)).append("\n");
        sb.append("Tipo: ").append(tipo).append("\n");
        sb.append("Título: ").append(titulo).append("\n");
        sb.append("Fecha: ").append(fechaGeneracion).append("\n");
        sb.append("Secciones: ").append(secciones.size()).append("\n");
        sb.append("Gráficos: ").append(graficos.size()).append("\n");
        return sb.toString();
    }

    public String generarResumenEjecutivo() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESUMEN EJECUTIVO ===\n\n");
        sb.append("Reporte: ").append(titulo).append("\n");
        sb.append("Generado: ").append(fechaGeneracion).append("\n\n");
        
        if (!datosTabla.isEmpty()) {
            sb.append("Datos principales:\n");
            datosTabla.forEach((k, v) -> sb.append("  - ").append(k).append(": ").append(v).append("\n"));
        }
        
        if (conclusiones != null && !conclusiones.isEmpty()) {
            sb.append("\nConclusiones:\n").append(conclusiones);
        }
        
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("Reporte: %s - %s", tipo, titulo);
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

    public Map<String, Object> getDatosTabla() {
        return datosTabla;
    }

    public void setDatosTabla(Map<String, Object> datosTabla) {
        this.datosTabla = datosTabla;
    }

    public List<String> getSecciones() {
        return secciones;
    }

    public void setSecciones(List<String> secciones) {
        this.secciones = secciones;
    }

    public List<Map<String, Object>> getGraficos() {
        return graficos;
    }

    public void setGraficos(List<Map<String, Object>> graficos) {
        this.graficos = graficos;
    }

    public String getConclusiones() {
        return conclusiones;
    }

    public void setConclusiones(String conclusiones) {
        this.conclusiones = conclusiones;
    }
}
