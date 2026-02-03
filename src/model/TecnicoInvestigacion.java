package model;

import java.util.Date;

public class TecnicoInvestigacion {

    private String idTecnico;
    private String cedula;
    private String correoElectronico;
    private String nombres;
    private String apellidos;
    private String telefono;

    private String especialidadTecnica;
    private int aniosExperiencia;
    private String empresaOrigen;

    private int horasSemanales;
    private double salarioMensual;

    private String estado;
    private Date fechaRegistro;
    private Date fechaFinalizacion;
    private String motivoSalida;

    private ProyectoInvestigacion proyectoAsignado;

    // =========================
    // CONSTRUCTORES
    // =========================

    public TecnicoInvestigacion() {
        this.estado = "ACTIVO";
        this.fechaRegistro = new Date();
    }

    public TecnicoInvestigacion(String idTecnico, String cedula, String correoElectronico,
                                String nombres, String apellidos, String telefono,
                                String especialidadTecnica, int aniosExperiencia,
                                String empresaOrigen, int horasSemanales,
                                double salarioMensual, ProyectoInvestigacion proyectoAsignado) {

        this.idTecnico = idTecnico;
        this.cedula = cedula;
        this.correoElectronico = correoElectronico;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.especialidadTecnica = especialidadTecnica;
        this.aniosExperiencia = aniosExperiencia;
        this.empresaOrigen = empresaOrigen;
        this.horasSemanales = horasSemanales;
        this.salarioMensual = salarioMensual;
        this.proyectoAsignado = proyectoAsignado;

        this.estado = "ACTIVO";
        this.fechaRegistro = new Date();
    }

    // =========================
    // GETTERS Y SETTERS
    // =========================

    public String getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(String idTecnico) {
        this.idTecnico = idTecnico;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEspecialidadTecnica() {
        return especialidadTecnica;
    }

    public void setEspecialidadTecnica(String especialidadTecnica) {
        this.especialidadTecnica = especialidadTecnica;
    }

    public int getAniosExperiencia() {
        return aniosExperiencia;
    }

    public void setAniosExperiencia(int aniosExperiencia) {
        this.aniosExperiencia = aniosExperiencia;
    }

    public String getEmpresaOrigen() {
        return empresaOrigen;
    }

    public void setEmpresaOrigen(String empresaOrigen) {
        this.empresaOrigen = empresaOrigen;
    }

    public int getHorasSemanales() {
        return horasSemanales;
    }

    public void setHorasSemanales(int horasSemanales) {
        this.horasSemanales = horasSemanales;
    }

    public double getSalarioMensual() {
        return salarioMensual;
    }

    public void setSalarioMensual(double salarioMensual) {
        this.salarioMensual = salarioMensual;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
}