package model;

/**
 * Clase base abstracta que representa a todos los miembros de la FIS-EPN
 */
public abstract class MiembroEPN {
    protected String codigoUnico;
    protected String cedula;
    protected String correoInstitucional;
    protected String password;
    protected String nombres;
    protected String apellidos;
    protected String telefono;
    protected String rol;
    protected String estado;

    public MiembroEPN() {
    }

    public MiembroEPN(String codigoUnico, String cedula, String correoInstitucional, 
                     String password, String nombres, String apellidos, 
                     String telefono, String rol, String estado) {
        this.codigoUnico = codigoUnico;
        this.cedula = cedula;
        this.correoInstitucional = correoInstitucional;
        this.password = password;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.rol = rol;
        this.estado = estado;
    }

    public String getNombresCompletos() {
        return nombres + " " + apellidos;
    }

    public boolean autenticar(String passwordIngresada) {
        return password.equals(passwordIngresada);
    }

    public boolean esActivo() {
        return "ACTIVO".equals(estado);
    }

    /**
     * Devuelve un resumen del miembro
     */
    public String obtenerResumen() {
        return String.format(
            "Usuario: %s | Rol: %s | Correo: %s | Estado: %s",
            getNombresCompletos(),
            getRol(),
            getCorreoInstitucional(),
            getEstado()
        );
    }

    /**
     * Valida el formato básico de correo
     */
    public static boolean esCorreoValido(String correo) {
        return correo != null && correo.contains("@") && correo.contains(".");
    }

    // Getters y Setters
    public String getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
