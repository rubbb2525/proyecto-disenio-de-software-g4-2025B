package model;

import java.util.Date;
import java.util.HashMap;
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

    public java.io.File exportarPDF() {
        // Implementación pendiente - requiere iText o similar
        throw new UnsupportedOperationException("Exportación a PDF no implementada aún");
    }

    public java.io.File exportarExcel() {
        // Implementación pendiente - requiere Apache POI
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
}
