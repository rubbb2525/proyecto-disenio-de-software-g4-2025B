package dominio;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Pattern;

public abstract class MiembroFIS implements Serializable {
    private static final long serialVersionUID = 1L;
    private String numeroUnico;
    private String cedula;
    private String correoInstitucional;
    private String password;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String direccion;
    private Date fechaIngresoEPN;
    private String estado;
    private String rol;

    // Constructores
    public MiembroFIS() {
        this.estado = "ACTIVO";
        this.fechaIngresoEPN = new Date();
    }

    public MiembroFIS(String numeroUnico, String cedula, String correoInstitucional) {
        this();
        this.numeroUnico = numeroUnico;
        this.cedula = cedula;
        this.correoInstitucional = correoInstitucional;
    }

    // Métodos de negocio
    public String getNombresCompletos() {
        return nombres + " " + apellidos;
    }

    public boolean autenticar(String password) {
        return this.password != null && this.password.equals(password);
    }

    public boolean esActivo() {
        return "ACTIVO".equals(estado);
    }

    public boolean validarCorreoInstitucional() {
        if (correoInstitucional == null || correoInstitucional.isEmpty()) {
            return false;
        }
        String regex = "^[a-zA-Z0-9._%+-]+@epn\\.edu\\.ec$";
        return Pattern.matches(regex, correoInstitucional);
    }

    public boolean cambiarPassword(String actual, String nueva) {
        if (autenticar(actual)) {
            this.password = nueva;
            return true;
        }
        return false;
    }

    public boolean validarDatos() {
        return numeroUnico != null && !numeroUnico.isEmpty() &&
               cedula != null && !cedula.isEmpty() &&
               validarCorreoInstitucional() &&
               nombres != null && !nombres.isEmpty() &&
               apellidos != null && !apellidos.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("%s %s (%s) - %s", nombres, apellidos, numeroUnico, rol);
    }

    // Getters y Setters
    public String getNumeroUnico() {
        return numeroUnico;
    }

    public void setNumeroUnico(String numeroUnico) {
        this.numeroUnico = numeroUnico;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFechaIngresoEPN() {
        return fechaIngresoEPN;
    }

    public void setFechaIngresoEPN(Date fechaIngresoEPN) {
        this.fechaIngresoEPN = fechaIngresoEPN;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
