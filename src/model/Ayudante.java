package model;

import java.util.Date;

/**
 * Representa un ayudante de investigación
 */
public class Ayudante extends MiembroEPN {
    private String carrera;
    private int nivel;
    private float ira;
    private Date fechaRegistro;
    private Date fechaFinalizacion;
    private String motivoSalida;
    private ProyectoInvestigacion proyectoAsignado;
    private int horasSemanales;
    private double salarioMensual;

    public Ayudante() {
    }

    public Ayudante(String codigoUnico, String cedula, String correoInstitucional,
                   String nombres, String apellidos, String telefono,
                   String carrera, int nivel, float ira, int horasSemanales, double salarioMensual) {
        super(codigoUnico, cedula, correoInstitucional, "N/A", nombres, apellidos, telefono, "AYUDANTE", "ACTIVO");
        this.carrera = carrera;
        this.nivel = nivel;
        this.ira = ira;
        this.horasSemanales = horasSemanales;
        this.salarioMensual = salarioMensual;
        this.fechaRegistro = new Date();
    }

    /**
     * Verifica si el ayudante está activo
     */
    @Override
    public boolean esActivo() {
        return "ACTIVO".equals(estado) && fechaFinalizacion == null;
    }

    /**
     * Da de baja el ayudante
     */
    public void darDeBaja(String motivo, Date fecha) {
        this.estado = "INACTIVO";
        this.motivoSalida = motivo;
        this.fechaFinalizacion = fecha;
    }

    /**
     * Calcula el salario total basado en horas semanales y número de semanas
     */
    public double calcularSalarioTotal() {
        if (fechaFinalizacion == null) {
            // Si aún está activo, calcular hasta hoy
            fechaFinalizacion = new Date();
        }
        long diferenciaTiempo = fechaFinalizacion.getTime() - fechaRegistro.getTime();
        long semanas = diferenciaTiempo / (1000 * 60 * 60 * 24 * 7);
        return salarioMensual * semanas / 4.33; // Aproximadamente 4.33 semanas por mes
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

    public float getIRA() {
        return ira;
    }

    public void setIRA(float ira) {
        this.ira = ira;
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
        if (horasSemanales > 32) {
            throw new IllegalArgumentException("Las horas semanales no pueden exceder 32");
        }
        this.horasSemanales = horasSemanales;
    }

    public double getSalarioMensual() {
        return salarioMensual;
    }

    public void setSalarioMensual(double salarioMensual) {
        this.salarioMensual = salarioMensual;
    }
}
