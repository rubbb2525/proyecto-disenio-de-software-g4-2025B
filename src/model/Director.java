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
        return "ACTIVO".equals(estado) && 
               codigoUnico != null && 
               !codigoUnico.isEmpty();
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
                ", codigo='" + codigoUnico + '\'' +
                ", correo='" + correoInstitucional + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }

    /**
     * Validación básica de director
     */
    public boolean esValido() {
        return codigoUnico != null && !codigoUnico.isEmpty() &&
               cedula != null && !cedula.isEmpty() &&
               correoInstitucional != null && !correoInstitucional.isEmpty() &&
               password != null && !password.isEmpty() &&
               nombres != null && !nombres.isEmpty() &&
               apellidos != null && !apellidos.isEmpty() &&
               "DIRECTOR".equals(rol) &&
               "ACTIVO".equals(estado);
    }
}