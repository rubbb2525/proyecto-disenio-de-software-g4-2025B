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
     * Valida si puede convertirse a ayudante con parámetros específicos
     */
    public ResultadoOperacion validarConversionAyudante(int horas, int meses) {
        ResultadoOperacion resultado = new ResultadoOperacion();

        // Validar elegibilidad básica
        if (!esElegibleParaAyudantia()) {
            resultado.setMensaje("Estudiante no elegible: IRA >= 24 y Nivel >= 3 requeridos");
            resultado.agregarError("IRA actual: " + ira + ", Nivel actual: " + nivel);
            return resultado;
        }

        // Validar horas
        if (horas <= 0 || horas > 32) {
            resultado.setMensaje("Las horas semanales deben estar entre 1 y 32");
            resultado.agregarError("Horas inválidas: " + horas);
            return resultado;
        }

        // Validar meses
        if (meses <= 0 || meses > 12) {
            resultado.setMensaje("Los meses deben estar entre 1 y 12");
            resultado.agregarError("Meses inválidos: " + meses);
            return resultado;
        }

        resultado.setExitoso(true);
        resultado.setMensaje("Validación exitosa");
        return resultado;
    }

    /**
     * Valida si puede convertirse a asistente
     */
    public ResultadoOperacion validarConversionAsistente(int horas, int meses) {
        ResultadoOperacion resultado = new ResultadoOperacion();

        // Validar horas
        if (horas <= 0 || horas > 40) {
            resultado.setMensaje("Las horas semanales deben estar entre 1 y 40");
            resultado.agregarError("Horas inválidas: " + horas);
            return resultado;
        }

        // Validar meses
        if (meses <= 0 || meses > 12) {
            resultado.setMensaje("Los meses deben estar entre 1 y 12");
            resultado.agregarError("Meses inválidos: " + meses);
            return resultado;
        }

        resultado.setExitoso(true);
        resultado.setMensaje("Validación exitosa");
        return resultado;
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
        return getCodigoUnico() != null && !getCodigoUnico().isEmpty() &&
               ira >= 0 && ira <= 20 &&
               nivel >= 1 && nivel <= 10 &&
               carrera != null && !carrera.isEmpty();
    }

    // ============ MÉTODOS DE CONVERSIÓN ============

    /**
     * Convierte este estudiante a Ayudante
     */
    public Ayudante convertirAAyudante(Proyectos proyecto, int horas, int meses) {
        Ayudante ayudante = new Ayudante();
        
        // Copiar datos comunes
        ayudante.setCodigoUnico(getCodigoUnico());
        ayudante.setCedula(getCedula());
        ayudante.setCorreoInstitucional(getCorreoInstitucional());
        ayudante.setPassword(getPassword());
        ayudante.setNombres(getNombres());
        ayudante.setApellidos(getApellidos());
        ayudante.setTelefono(getTelefono());
        ayudante.setRol("AYUDANTE");
        ayudante.setEstado("ACTIVO");
        
        // Copiar datos académicos
        ayudante.setCarrera(this.carrera);
        ayudante.setNivel(this.nivel);
        ayudante.setIRA(this.ira);
        
        // Asignar datos de ayudante
        ayudante.setHorasSemanales(horas);
        ayudante.setMesesContratados(meses);
        ayudante.setProyectoAsignado(proyecto);
        ayudante.setFechaRegistro(new java.util.Date());
        
        return ayudante;
    }

    /**
     * Convierte este estudiante a AsistenteInvestigacion
     */
    public AsistenteInvestigacion convertirAAsistente(Proyectos proyecto, int horas, int meses) {
        AsistenteInvestigacion asistente = new AsistenteInvestigacion();

        // Copiar datos comunes
        asistente.setCodigoUnico(getCodigoUnico());
        asistente.setCedula(getCedula());
        asistente.setCorreoInstitucional(getCorreoInstitucional());
        asistente.setNombres(getNombres());
        asistente.setApellidos(getApellidos());
        asistente.setTelefono(getTelefono());
        asistente.setCarrera(this.carrera);
        asistente.setNivel(this.nivel);
        asistente.setIRA(this.ira);

        // Datos específicos de asistente
        asistente.setHorasSemanales(horas);
        asistente.setMesesContratados(meses);
        asistente.setEstado("ACTIVO");
        asistente.setFechaRegistro(new java.util.Date());
        asistente.setProyectoAsignado(proyecto);

        return asistente;
    }
}