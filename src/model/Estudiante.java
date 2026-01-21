package model;

/**
 * Representa un estudiante de la FIS-EPN
 */
public class Estudiante extends MiembroEPN {
    private String carrera;
    private float ira;
    private int nivel;

    public Estudiante() {
    }

    public Estudiante(String codigoUnico, String cedula, String correoInstitucional,
                     String nombres, String apellidos, String telefono,
                     String carrera, float ira, int nivel) {
        super(codigoUnico, cedula, correoInstitucional, "N/A", nombres, apellidos, telefono, "ESTUDIANTE", "ACTIVO");
        this.carrera = carrera;
        this.ira = ira;
        this.nivel = nivel;
    }

    /**
     * Valida si un estudiante es elegible para ser ayudante
     * Requisitos: IRA >= 24, Nivel >= 3
     */
    public boolean esElegibleParaAyudantia() {
        return ira >= 24.0f && nivel >= 3;
    }

    /**
     * Convierte un estudiante elegible a ayudante
     */
    public Ayudante convertirAAyudante(ProyectoInvestigacion proyecto, int horas, double salario) {
        if (!esElegibleParaAyudantia()) {
            return null;
        }

        Ayudante ayudante = new Ayudante();
        ayudante.setCodigoUnico(this.codigoUnico);
        ayudante.setCedula(this.cedula);
        ayudante.setCorreoInstitucional(this.correoInstitucional);
        ayudante.setPassword(this.password);
        ayudante.setNombres(this.nombres);
        ayudante.setApellidos(this.apellidos);
        ayudante.setTelefono(this.telefono);
        ayudante.setRol("AYUDANTE");
        ayudante.setEstado("ACTIVO");
        ayudante.setCarrera(this.carrera);
        ayudante.setNivel(this.nivel);
        ayudante.setIRA(this.ira);
        ayudante.setHorasSemanales(horas);
        ayudante.setSalarioMensual(salario);
        ayudante.setProyectoAsignado(proyecto);
        ayudante.setFechaRegistro(new java.util.Date());
        //agregar mes por hora y salario
        return ayudante;
    }

    // Getters y Setters
    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public float getIRA() {
        return ira;
    }

    public void setIRA(float ira) {
        this.ira = ira;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
}
