package model;

/**
 * Representa un estudiante de la FIS-EPN
 * RESPONSABILIDAD ÚNICA: Mantener datos y lógica específica de estudiante
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
     * 
     * NOTA: Esta clase SOLO determina elegibilidad.
     * La conversión actual se delega a ServicioConversionAyudante
     */
    public boolean esElegibleParaAyudantia() {
        return ira >= 24.0f && nivel >= 3;
    }

    /**
     * Obtiene el nombre completo del estudiante
     */
    @Override
    public String getNombresCompletos() {
        return super.getNombresCompletos();
    }

    // ============ GETTERS Y SETTERS ============
    
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

    /**
     * Validación básica de estudiante
     */
    public boolean esValido() {
        return codigoUnico != null && !codigoUnico.isEmpty() &&
               ira >= 0 && ira <= 20 &&
               nivel >= 1 && nivel <= 10 &&
               carrera != null && !carrera.isEmpty();
    }
}