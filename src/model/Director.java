package model;

/**
 * Representa un director de proyecto de investigación
 * 
 * RESPONSABILIDAD ÚNICA:
 * Mantener datos y comportamiento específico de Director
 * PATRÓN: Lazy Loading
 * En lugar de mantener el proyecto como atributo,
 * se obtiene cuando se necesita via: ProyectoDAO.buscarPorDirector()
 */
public class Director extends MiembroEPN {

    public Director() {
    }

    public Director(String codigoUnico, String cedula, String correoInstitucional,
                   String password, String nombres, String apellidos, String telefono) {
        super(codigoUnico, cedula, correoInstitucional, password, 
              nombres, apellidos, telefono, "DIRECTOR", "ACTIVO");
    }

    /**
     * Valida si el director puede dirigir un proyecto
     * (Ejemplo de lógica que SÍ debe estar aquí)
     */
    public boolean puedeDirigirProyecto() {
        return "ACTIVO".equals(getEstado()) && 
               getCodigoUnico() != null && 
               !getCodigoUnico().isEmpty();
    }

    /**
     * Valida si el director puede crear un nuevo proyecto
     */
    public ResultadoOperacion validarCreacionProyecto(Proyectos proyectoExistente) {
        ResultadoOperacion resultado = new ResultadoOperacion();

        // Validar que el director esté activo
        if (!puedeDirigirProyecto()) {
            resultado.setMensaje("Director no está habilitado para dirigir proyectos");
            resultado.agregarError("Estado inválido o código faltante");
            return resultado;
        }

        // Validar que no tenga ya un proyecto activo
        if (proyectoExistente != null && "ACTIVO".equals(proyectoExistente.getEstado())) {
            resultado.setMensaje("El director ya tiene un proyecto activo");
            resultado.agregarError("No se puede crear más de un proyecto activo simultáneamente");
            return resultado;
        }

        resultado.setExitoso(true);
        resultado.setMensaje("El director puede crear un proyecto");
        return resultado;
    }

    /**
     * Obtiene el nombre completo del director
     */
    @Override
    public String getNombresCompletos() {
        return super.getNombresCompletos();
    }

    /**
     * Para obtener el proyecto del director, usar:
     * ProyectoDAO.buscarPorDirector(director.getCodigoUnico())
     * 
     * Ejemplo de uso en controlador:
     * 
     * Director director = new Director(...)
     * Proyectos proyecto = 
     *     proyectoDAO.buscarPorDirector(director.getCodigoUnico());
     * 
     * if (proyecto != null) {
     *     // Usar proyecto
     * }
     */

    @Override
    public String toString() {
        return "Director{" +
                "nombre='" + getNombresCompletos() + '\'' +
                ", codigo='" + getCodigoUnico() + '\'' +
                ", correo='" + getCorreoInstitucional() + '\'' +
                ", estado='" + getEstado() + '\'' +
                '}';
    }

    /**
     * Validación básica de director
     */
    public boolean esValido() {
        return getCodigoUnico() != null && !getCodigoUnico().isEmpty() &&
               getCedula() != null && !getCedula().isEmpty() &&
               getCorreoInstitucional() != null && !getCorreoInstitucional().isEmpty() &&
               getPassword() != null && !getPassword().isEmpty() &&
               getNombres() != null && !getNombres().isEmpty() &&
               getApellidos() != null && !getApellidos().isEmpty() &&
               "DIRECTOR".equals(getRol()) &&
               "ACTIVO".equals(getEstado());
    }
}