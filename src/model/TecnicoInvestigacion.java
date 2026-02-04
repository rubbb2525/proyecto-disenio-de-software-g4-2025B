package model;

import java.util.Date;

public class TecnicoInvestigacion {

    private String idTecnico;
    private String cedula;
    private String correoElectronico;
    private String nombres;
    private String apellidos;
    private String telefono;

    private int horasSemanales;
    private int mesesContratados;

    private String estado;
    private Date fechaRegistro;
    private Date fechaFinalizacion;
    private String motivoSalida;

    private Proyectos proyectoAsignado;

    // =========================
    // CONSTRUCTORES
    // =========================

    public TecnicoInvestigacion() {
        this.estado = "ACTIVO";
        this.fechaRegistro = new Date();
    }

    public TecnicoInvestigacion(String idTecnico, String cedula, String correoElectronico,
                                String nombres, String apellidos, String telefono,
                                int horasSemanales, int mesesContratados,
                                Proyectos proyectoAsignado) {

        this.idTecnico = idTecnico;
        this.cedula = cedula;
        this.correoElectronico = correoElectronico;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.horasSemanales = horasSemanales;
        this.mesesContratados = mesesContratados;
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

    public int getHorasSemanales() {
        return horasSemanales;
    }

    public void setHorasSemanales(int horasSemanales) {
        if (horasSemanales < 1 || horasSemanales > 40) {
            throw new IllegalArgumentException("Las horas semanales deben estar entre 1 y 40");
        }
        this.horasSemanales = horasSemanales;
    }

    public int getMesesContratados() {
        return mesesContratados;
    }

    public void setMesesContratados(int mesesContratados) {
        if (mesesContratados < 1 || mesesContratados > 12) {
            throw new IllegalArgumentException("Los meses contratados deben estar entre 1 y 12");
        }
        this.mesesContratados = mesesContratados;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado no puede estar vacío");
        }
        if (!estado.matches("ACTIVO|INACTIVO")) {
            throw new IllegalArgumentException("El estado debe ser ACTIVO o INACTIVO");
        }
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

    public Proyectos getProyectoAsignado() {
        return proyectoAsignado;
    }

    public void setProyectoAsignado(Proyectos proyectoAsignado) {
        this.proyectoAsignado = proyectoAsignado;
    }

    // =========================
    // MÉTODOS DE UTILIDAD
    // =========================

    /**
     * Retorna si el técnico está activo
     */
    public boolean esActivo() {
        return "ACTIVO".equals(estado);
    }

    /**
     * Retorna nombres y apellidos concatenados
     */
    public String getNombresCompletos() {
        if (nombres != null && apellidos != null) {
            return nombres + " " + apellidos;
        }
        return nombres != null ? nombres : apellidos;
    }

    /**
     * Da de baja el técnico con validación
     */
    public ResultadoOperacion darDeBaja(String motivo, Date fecha) {
        ResultadoOperacion resultado = new ResultadoOperacion();

        // Validar que no esté ya dado de baja
        if (!esActivo()) {
            resultado.setMensaje("El técnico ya está inactivo");
            resultado.agregarError("Estado inválido");
            return resultado;
        }

        // Validar motivo
        if (motivo == null || motivo.trim().isEmpty()) {
            resultado.setMensaje("El motivo de baja es obligatorio");
            resultado.agregarError("Motivo vacío");
            return resultado;
        }

        // Validar fecha
        if (fecha == null) {
            resultado.setMensaje("La fecha de baja es obligatoria");
            resultado.agregarError("Fecha nula");
            return resultado;
        }

        if (fecha.before(fechaRegistro)) {
            resultado.setMensaje("La fecha de baja no puede ser anterior a la fecha de registro");
            resultado.agregarError("Fecha inválida");
            return resultado;
        }

        // Aplicar baja
        setEstado("INACTIVO");
        this.motivoSalida = motivo;
        this.fechaFinalizacion = fecha;

        resultado.setExitoso(true);
        resultado.setMensaje("Técnico dado de baja exitosamente");
        return resultado;
    }
}