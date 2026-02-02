package model;

/**
 * Representa un director de proyecto de investigación
 */
public class Director extends MiembroEPN {
    private ProyectoInvestigacion proyectoAsignado;

    public Director() {
    }

    public Director(String codigoUnico, String cedula, String correoInstitucional,
                   String password, String nombres, String apellidos, String telefono) {
        super(codigoUnico, cedula, correoInstitucional, password, nombres, apellidos, telefono, "DIRECTOR", "ACTIVO");
    }

    // Getters y Setters
    public ProyectoInvestigacion getProyectoAsignado() {
        return proyectoAsignado;
    }

    public void setProyectoAsignado(ProyectoInvestigacion proyectoAsignado) {
        this.proyectoAsignado = proyectoAsignado;
    }
}