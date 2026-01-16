package dominio;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Ayudante extends MiembroFIS implements Serializable {
    private static final long serialVersionUID = 1L;
    private String carrera;
    private int nivel;
    private float promedio;
    private Date fechaRegistro;
    private Date fechaFinalizacion;
    private String motivoSalida;
    private ProyectoInvestigacion proyectoAsignado;
    private int horasSemanales;
    private double salarioPorHora;
    private String codigoEstudiante;
    private int semestre;

    // Constructores
    public Ayudante() {
        super();
        setRol("AYUDANTE");
        setEstado("ACTIVO");
        this.fechaRegistro = new Date();
    }

    public Ayudante(String numeroUnico, String nombres, String apellidos, String carrera, int nivel) {
        this();
        setNumeroUnico(numeroUnico);
        setNombres(nombres);
        setApellidos(apellidos);
        this.carrera = carrera;
        this.nivel = nivel;
    }

    // Métodos de negocio
    @Override
    public boolean esActivo() {
        return "ACTIVO".equals(getEstado()) && fechaFinalizacion == null;
    }

    public boolean esElegible() {
        return nivel >= 3 && promedio >= 7.0f && esActivo();
    }

    public void darDeBaja(String motivo, Date fecha) {
        setEstado("INACTIVO");
        this.motivoSalida = motivo;
        this.fechaFinalizacion = fecha != null ? fecha : new Date();
        
        if (proyectoAsignado != null) {
            proyectoAsignado.removerAyudante(this);
        }
    }

    public double calcularSalarioMensual() {
        // Asumiendo 4 semanas por mes
        return horasSemanales * salarioPorHora * 4;
    }

    public boolean puedeRenovarContrato() {
        return esActivo() && getTiempoEnProyecto() >= 6 && promedio >= 7.5f;
    }

    public int getTiempoEnProyecto() {
        if (fechaRegistro == null) {
            return 0;
        }
        
        Date fechaFin = fechaFinalizacion != null ? fechaFinalizacion : new Date();
        long diffInMillies = Math.abs(fechaFin.getTime() - fechaRegistro.getTime());
        long diffInMonths = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) / 30;
        
        return (int) diffInMonths;
    }

    public Map<String, Object> getInformacionCompleta() {
        Map<String, Object> info = new HashMap<>();
        info.put("nombresCompletos", getNombresCompletos());
        info.put("numeroUnico", getNumeroUnico());
        info.put("carrera", carrera);
        info.put("nivel", nivel);
        info.put("promedio", promedio);
        info.put("estado", getEstado());
        info.put("proyecto", proyectoAsignado != null ? proyectoAsignado.getNombreProyecto() : "Sin asignar");
        info.put("horasSemanales", horasSemanales);
        info.put("salarioMensual", calcularSalarioMensual());
        info.put("tiempoEnProyecto", getTiempoEnProyecto() + " meses");
        info.put("esElegible", esElegible());
        return info;
    }

    @Override
    public String toString() {
        return String.format("Ayudante: %s - %s (Nivel %d, Promedio: %.2f)", 
                           getNombresCompletos(), carrera, nivel, promedio);
    }

    // Getters y Setters
    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public float getPromedio() {
        return promedio;
    }

    public void setPromedio(float promedio) {
        this.promedio = promedio;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public String getMotivoSalida() {
        return motivoSalida;
    }

    public void setMotivoSalida(String motivoSalida) {
        this.motivoSalida = motivoSalida;
    }

    public ProyectoInvestigacion getProyectoAsignado() {
        return proyectoAsignado;
    }

    public void setProyectoAsignado(ProyectoInvestigacion proyectoAsignado) {
        this.proyectoAsignado = proyectoAsignado;
    }

    public int getHorasSemanales() {
        return horasSemanales;
    }

    public void setHorasSemanales(int horasSemanales) {
        this.horasSemanales = horasSemanales;
    }

    public double getSalarioPorHora() {
        return salarioPorHora;
    }

    public void setSalarioPorHora(double salarioPorHora) {
        this.salarioPorHora = salarioPorHora;
    }

    public String getCodigoEstudiante() {
        return codigoEstudiante;
    }

    public void setCodigoEstudiante(String codigoEstudiante) {
        this.codigoEstudiante = codigoEstudiante;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }
}
