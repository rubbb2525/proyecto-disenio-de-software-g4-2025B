package dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProyectoInvestigacion implements Serializable {
    private static final long serialVersionUID = 1L;
    private String codigoProyecto;
    private String nombreProyecto;
    private String descripcion;
    private Date fechaInicio;
    private Date fechaFin;
    private float presupuesto;
    private String estado;
    private TipoProyecto tipoProyecto;
    private int ayudantesPlanificados;
    private String objetivos;
    private String areaInvestigacion;
    private Director director;
    private List<Ayudante> ayudantes;
    private float presupuestoUtilizado;

    // Constructores
    public ProyectoInvestigacion() {
        this.ayudantes = new ArrayList<>();
        this.estado = "ACTIVO";
        this.presupuestoUtilizado = 0;
    }

    public ProyectoInvestigacion(String codigoProyecto, String nombreProyecto, TipoProyecto tipoProyecto) {
        this();
        this.codigoProyecto = codigoProyecto;
        this.nombreProyecto = nombreProyecto;
        this.tipoProyecto = tipoProyecto;
    }

    // Métodos de negocio
    public boolean agregarAyudante(Ayudante ayudante) {
        if (ayudante != null && !ayudantes.contains(ayudante)) {
            ayudantes.add(ayudante);
            ayudante.setProyectoAsignado(this);
            return true;
        }
        return false;
    }

    public boolean removerAyudante(Ayudante ayudante) {
        if (ayudante != null && ayudantes.contains(ayudante)) {
            ayudantes.remove(ayudante);
            ayudante.setProyectoAsignado(null);
            return true;
        }
        return false;
    }

    public int getAyudantesContratados() {
        return ayudantes.size();
    }

    public int getAyudantesActivos() {
        return (int) ayudantes.stream().filter(Ayudante::esActivo).count();
    }

    public boolean estaActivo() {
        return "ACTIVO".equals(estado);
    }

    public boolean puedeSolicitarAyudantes() {
        return estaActivo() && getAyudantesContratados() < ayudantesPlanificados;
    }

    public float calcularPresupuestoUtilizado() {
        presupuestoUtilizado = 0;
        for (Ayudante ayudante : ayudantes) {
            if (ayudante.esActivo()) {
                presupuestoUtilizado += (float) ayudante.calcularSalarioMensual();
            }
        }
        return presupuestoUtilizado;
    }

    public float calcularPresupuestoDisponible() {
        return presupuesto - calcularPresupuestoUtilizado();
    }

    public float getPorcentajeCompletado() {
        if (fechaInicio == null || fechaFin == null) {
            return 0;
        }
        Date ahora = new Date();
        long tiempoTotal = fechaFin.getTime() - fechaInicio.getTime();
        long tiempoTranscurrido = ahora.getTime() - fechaInicio.getTime();
        
        if (tiempoTranscurrido <= 0) return 0;
        if (tiempoTranscurrido >= tiempoTotal) return 100;
        
        return (tiempoTranscurrido * 100.0f) / tiempoTotal;
    }

    public boolean validarDatos() {
        return codigoProyecto != null && !codigoProyecto.isEmpty() &&
               nombreProyecto != null && !nombreProyecto.isEmpty() &&
               tipoProyecto != null &&
               presupuesto > 0 &&
               ayudantesPlanificados >= 0;
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s)", codigoProyecto, nombreProyecto, tipoProyecto);
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

    public float getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(float presupuesto) {
        this.presupuesto = presupuesto;
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

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public String getAreaInvestigacion() {
        return areaInvestigacion;
    }

    public void setAreaInvestigacion(String areaInvestigacion) {
        this.areaInvestigacion = areaInvestigacion;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public List<Ayudante> getAyudantes() {
        return ayudantes;
    }

    public void setAyudantes(List<Ayudante> ayudantes) {
        this.ayudantes = ayudantes;
    }

    public float getPresupuestoUtilizado() {
        return presupuestoUtilizado;
    }

    public void setPresupuestoUtilizado(float presupuestoUtilizado) {
        this.presupuestoUtilizado = presupuestoUtilizado;
    }
}
