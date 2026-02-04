package model;

import java.util.Date;

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
     * Valida el registro de un ayudante bajo el proyecto del director
     */
    public ResultadoOperacion validarRegistroAyudante(Proyectos proyecto, Estudiante estudiante, int horas, int meses) {
        if (proyecto == null) {
            return ResultadoOperacion.fallido("Director sin proyecto asignado", "Proyecto no disponible");
        }
        if (!proyecto.tieneEstadoActivo()) {
            return ResultadoOperacion.fallido("Proyecto inactivo", "No se puede registrar ayudantes en proyectos inactivos");
        }
        if (estudiante == null) {
            return ResultadoOperacion.noEncontrado("Estudiante", "Código de estudiante");
        }
        if (!proyecto.tieneCupoDisponible()) {
            return ResultadoOperacion.fallido("Sin cupos disponibles", "No hay cupos para ayudantes");
        }
        return estudiante.validarConversionAyudante(horas, meses);
    }

    /**
     * Valida el registro de un asistente de investigación bajo el proyecto del director
     */
    public ResultadoOperacion validarRegistroAsistente(Proyectos proyecto, Estudiante estudiante, int horas, int meses) {
        if (proyecto == null) {
            return ResultadoOperacion.fallido("Director sin proyecto asignado", "Proyecto no disponible");
        }
        if (!proyecto.tieneEstadoActivo()) {
            return ResultadoOperacion.fallido("Proyecto inactivo", "No se puede registrar asistentes en proyectos inactivos");
        }
        if (estudiante == null) {
            return ResultadoOperacion.noEncontrado("Estudiante", "Código de estudiante");
        }
        return estudiante.validarConversionAsistente(horas, meses);
    }

    /**
     * Prepara un técnico para ser asignado al proyecto
     */
    public ResultadoOperacion prepararTecnico(TecnicoInvestigacion tecnico, Proyectos proyecto) {
        if (proyecto == null) {
            return ResultadoOperacion.fallido("Director sin proyecto asignado", "Proyecto no disponible");
        }
        if (tecnico == null) {
            return ResultadoOperacion.fallido("Técnico no válido", "Datos de técnico vacíos");
        }
        tecnico.setProyectoAsignado(proyecto);
        tecnico.setEstado("ACTIVO");
        return ResultadoOperacion.exitoso("Técnico preparado para registro");
    }

    
    public Proyectos crearProyecto(String codigoProyecto, String nombreProyecto,
                                   String descripcion, Date fechaInicio, Date fechaFin,
                                   TipoProyecto tipoProyecto, int ayudantesPlanificados,
                                   int tecnicosPlanificados, int asistentesPlanificados) {
        Proyectos nuevoProyecto = new Proyectos(
            codigoProyecto,
            nombreProyecto,
            descripcion,
            fechaInicio,
            fechaFin,
            "ACTIVO",
            tipoProyecto,
            ayudantesPlanificados,
            tecnicosPlanificados,
            asistentesPlanificados
        );
        nuevoProyecto.setDirector(this);
        return nuevoProyecto;
    }

    /**
     * Verifica si un proyecto pertenece a este director
     */
    public boolean esProyectoPropio(Proyectos proyecto) {
        return proyecto != null && proyecto.getDirector() != null
            && getCodigoUnico() != null
            && getCodigoUnico().equals(proyecto.getDirector().getCodigoUnico());
    }

    /**
     * Obtiene nombre de proyecto del director
     */
    public String obtenerNombreProyecto(Proyectos proyecto) {
        if (proyecto == null) {
            return "Sin proyecto asignado";
        }
        return proyecto.getNombreProyecto();
    }

    /**
     * Obtiene cupos disponibles del proyecto
     */
    public int obtenerCuposDisponibles(Proyectos proyecto) {
        if (proyecto == null) {
            return 0;
        }
        return proyecto.getCuposDisponibles();
    }

    /**
     * Verifica si hay cupo disponible en el proyecto
     */
    public boolean hayCupoDisponible(Proyectos proyecto) {
        if (proyecto == null) {
            return false;
        }
        return proyecto.tieneCupoDisponible();
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