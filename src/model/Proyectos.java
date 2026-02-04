package model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un proyecto de investigación
 */
public class Proyectos {
    private String codigoProyecto;
    private String nombreProyecto;
    private String descripcion;
    private Date fechaInicio;
    private Date fechaFin;
    private String estado;
    private TipoProyecto tipoProyecto;
    private int ayudantesPlanificados;
    private List<Ayudante> ayudantes;
    private Director director;

    public Proyectos() {
        this.ayudantes = new ArrayList<>();
    }

    public Proyectos(String codigoProyecto, String nombreProyecto, String descripcion,
                                Date fechaInicio, Date fechaFin, String estado,
                                TipoProyecto tipoProyecto, int ayudantesPlanificados) {
        this.codigoProyecto = codigoProyecto;
        this.nombreProyecto = nombreProyecto;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.tipoProyecto = tipoProyecto;
        this.ayudantesPlanificados = ayudantesPlanificados;
        this.ayudantes = new ArrayList<>();
    }

    /**
     * Agrega un ayudante al proyecto si hay cupo disponible
     */
    public boolean agregarAyudante(Ayudante ayudante) {
        if (tieneCupoDisponible() && ayudante != null) {
            return ayudantes.add(ayudante);
        }
        return false;
    }

    /**
     * Remueve un ayudante del proyecto
     */
    public boolean removerAyudante(Ayudante ayudante) {
        if (ayudante != null) {
            return ayudantes.remove(ayudante);
        }
        return false;
    }

    /**
     * Retorna el número de ayudantes activos
     */
    public int getAyudantesActivos() {
        return (int) ayudantes.stream().filter(Ayudante::esActivo).count();
    }

    /**
     * Verifica si el proyecto está activo
     */
    public boolean estaActivo() {
        Date hoy = new Date();
        return "ACTIVO".equals(estado) && 
               hoy.after(fechaInicio) && 
               hoy.before(fechaFin);
    }

    /**
     * Verifica si hay cupo disponible para más ayudantes
     */
    public boolean tieneCupoDisponible() {
        return getAyudantesActivos() < ayudantesPlanificados;
    }

    /**
     * Retorna el número de cupos disponibles
     */
    public int getCuposDisponibles() {
        return ayudantesPlanificados - getAyudantesActivos();
    }

    // Getters y Setters
    public String getCodigoProyecto() {
        return codigoProyecto;
    }

    public void setCodigoProyecto(String codigoProyecto) {
        this.codigoProyecto = codigoProyecto;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public TipoProyecto getTipoProyecto() {
        return tipoProyecto;
    }

    public void setTipoProyecto(TipoProyecto tipoProyecto) {
        this.tipoProyecto = tipoProyecto;
    }

    public int getAyudantesPlanificados() {
        return ayudantesPlanificados;
    }

    public void setAyudantesPlanificados(int ayudantesPlanificados) {
        this.ayudantesPlanificados = ayudantesPlanificados;
    }

    public List<Ayudante> getAyudantes() {
        return ayudantes;
    }

    public void setAyudantes(List<Ayudante> ayudantes) {
        this.ayudantes = ayudantes;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }
}
