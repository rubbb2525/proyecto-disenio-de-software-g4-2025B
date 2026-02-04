package controller;

import model.*;
import model.dao.*;
import model.service.ServicioDeEstadisticas;
import java.util.Date;
import java.util.List;

/**
 * Controlador para operaciones del Director
 * 
 * REFACTORIZACIÓN:
 * - Usa métodos del modelo (Estudiante.convertirAAyudante/Asistente)
 * - Obtiene proyecto via DAO en lugar de mantenerlo como atributo en Director
 * - Usa Ayudante.obtenerActivos para filtrado
 * - Usa ServicioDeEstadisticas para estadísticas agregadas
 */
public class ControladorDirector {
    private Director directorActual;
    private AyudanteDAO ayudanteDAO;
    private EstudianteDAO estudianteDAO;
    private ProyectoDAO proyectoDAO;
    private NotificacionDAO notificacionDAO;
    private JefaDepartamento jefaDepartamento;
    private TecnicoDAO tecnicoDAO;
    private AsistenteDAO asistenteDAO;

    // ÚNICO SERVICIO: Estadísticas agregadas
    private ServicioDeEstadisticas servicioEstadisticas;

    public ControladorDirector(
        Director director,
        AyudanteDAO ayudanteDAO,
        AsistenteDAO asistenteDAO,
        TecnicoDAO tecnicoDAO,
        EstudianteDAO estudianteDAO,
        ProyectoDAO proyectoDAO
) {
        this.directorActual = director;
        this.ayudanteDAO = ayudanteDAO;
        this.asistenteDAO = asistenteDAO;
        this.tecnicoDAO = tecnicoDAO;
        this.estudianteDAO = estudianteDAO;
        this.proyectoDAO = proyectoDAO;

        this.notificacionDAO = new NotificacionDAO();
        this.jefaDepartamento = JefaDepartamento.getInstancia();
    }

    /**
     * NUEVO: Obtiene el proyecto del director (Lazy Loading)
     * Antes: director.getProyectoAsignado() (podía ser null)
     * Ahora: Se obtiene cuando se necesita via DAO
     */
    public Proyectos obtenerProyectoDelDirector() {
        return proyectoDAO.buscarPorDirector(directorActual.getCodigoUnico());
    }

    /**
     * Busca un estudiante por criterio (código o cédula)
     */
    public Estudiante buscarEstudiante(String criterio) {
        if (criterio == null || criterio.isEmpty()) {
            return null;
        }
        return estudianteDAO.buscarPorCriterio(criterio);
    }

    /**
     * Registra un nuevo ayudante
     * 
     * REFACTORIZACIÓN:
     * - Usa validaciones del modelo (Estudiante, Proyectos)
     * - Usa Estudiante.convertirAAyudante() para la conversión
     */
    public ResultadoOperacion registrarAyudante(String codigoEstudiante, int horas, int meses) {
        // Obtener proyecto del director
        Proyectos proyecto = obtenerProyectoDelDirector();
        if (proyecto == null) {
            return ResultadoOperacion.fallido("Director sin proyecto asignado", "Proyecto no disponible");
        }

        // Buscar estudiante
        Estudiante estudiante = buscarEstudiante(codigoEstudiante);
        if (estudiante == null) {
            return ResultadoOperacion.noEncontrado("Estudiante", "Código de estudiante");
        }

        // Validar conversión usando el modelo
        ResultadoOperacion validacion = estudiante.validarConversionAyudante(horas, meses);
        if (!validacion.esExitoso()) {
            return validacion;
        }

        // Convertir usando el método del modelo
        Ayudante ayudante = estudiante.convertirAAyudante(proyecto, horas, meses);

        if (ayudante == null) {
            return ResultadoOperacion.fallido("No se pudo convertir a ayudante", "Error en la conversión");
        }

        // Guardar en BD
        if (!ayudanteDAO.guardar(ayudante)) {
            return ResultadoOperacion.errorPersistencia("guardar ayudante");
        }

        // Notificar a jefa
        Notificacion notif = Notificacion.crearRegistroAyudante(ayudante, proyecto);
        notificacionDAO.guardar(notif);
        jefaDepartamento.recibirNotificacion(notif);

        return ResultadoOperacion.exitoso("Ayudante registrado exitosamente");
    }

    /**
     * Da de baja un ayudante
     */
    public ResultadoOperacion darDeBajaAyudante(String codigoAyudante, String motivo, Date fecha) {
        // Buscar ayudante
        Ayudante ayudante = ayudanteDAO.buscarPorId(codigoAyudante);
        if (ayudante == null) {
            return ResultadoOperacion.noEncontrado("Ayudante", "Código de ayudante");
        }

        // Dar de baja (ahora con validación en el modelo)
        ResultadoOperacion resultado = ayudante.darDeBaja(motivo, fecha);
        if (!resultado.esExitoso()) {
            return resultado;
        }

        // Actualizar en BD
        if (!ayudanteDAO.actualizar(ayudante)) {
            return ResultadoOperacion.errorPersistencia("actualizar ayudante");
        }

        // Notificar a jefa
        Notificacion notif = Notificacion.crearBajaAyudante(ayudante, motivo);
        notificacionDAO.guardar(notif);
        jefaDepartamento.recibirNotificacion(notif);

        return ResultadoOperacion.exitoso("Ayudante dado de baja exitosamente");
    }

    public ResultadoOperacion registrarAsistente(
        String codigoEstudiante,
        int horas,
        int meses
) {
    Proyectos proyecto = obtenerProyectoDelDirector();
    if (proyecto == null) {
        return ResultadoOperacion.fallido("Director sin proyecto asignado", "Proyecto no disponible");
    }

    Estudiante estudiante = buscarEstudiante(codigoEstudiante);
    if (estudiante == null) {
        return ResultadoOperacion.noEncontrado("Estudiante", "Código de estudiante");
    }

    // Validar conversión usando el modelo
    ResultadoOperacion validacion = estudiante.validarConversionAsistente(horas, meses);
    if (!validacion.esExitoso()) {
        return validacion;
    }

    // Convertir usando el método del modelo
    AsistenteInvestigacion asistente = estudiante.convertirAAsistente(
        proyecto, horas, meses
    );

    if (!asistenteDAO.guardar(asistente)) {
        return ResultadoOperacion.errorPersistencia("guardar asistente");
    }

    Notificacion notif = Notificacion.crearRegistroAsistente(asistente, proyecto);
    notificacionDAO.guardar(notif);
    jefaDepartamento.recibirNotificacion(notif);

    return ResultadoOperacion.exitoso("Asistente registrado exitosamente");
}

public ResultadoOperacion registrarTecnico(TecnicoInvestigacion tecnico) {

    ResultadoOperacion resultado = new ResultadoOperacion();

    Proyectos proyecto = obtenerProyectoDelDirector();
    if (proyecto == null) {
        resultado.setMensaje("Director sin proyecto asignado");
        return resultado;
    }

    tecnico.setProyectoAsignado(proyecto);
    tecnico.setEstado("ACTIVO");

    if (!tecnicoDAO.guardar(tecnico)) {
        resultado.setMensaje("Error al guardar técnico en BD");
        return resultado;
    }

    Notificacion notif = Notificacion.crearRegistroTecnico(tecnico, proyecto);

    notificacionDAO.guardar(notif);
    jefaDepartamento.recibirNotificacion(notif);

    resultado.setExitoso(true);
    resultado.setMensaje("Técnico registrado exitosamente");

    return resultado;
}


    /**
     * Consulta los ayudantes del proyecto
     * 
     * REFACTORIZACIÓN:
     * - Obtiene proyecto via método (no como atributo)
     */
    public List<Ayudante> consultarAyudantesDelProyecto() {
        Proyectos proyecto = obtenerProyectoDelDirector();
        if (proyecto == null) {
            return null;
        }
        return ayudanteDAO.buscarPorProyecto(proyecto.getCodigoProyecto());
    }

    /**
     * NUEVO: Obtiene ayudantes activos del proyecto
     * Usa Ayudante.obtenerActivos para filtrado
     */
    public List<Ayudante> obtenerAyudantesActivos() {
        List<Ayudante> ayudantes = consultarAyudantesDelProyecto();
        if (ayudantes == null) {
            return null;
        }
        // Usar método estático del modelo
        return Ayudante.obtenerActivos(ayudantes);
    }

    /**
     * NUEVO: Obtiene estadísticas del proyecto
     * Usa ServicioDeEstadisticas
     */
    public java.util.Map<String, Object> obtenerEstadisticasProyecto() {
        List<Ayudante> ayudantes = consultarAyudantesDelProyecto();
        if (ayudantes == null || ayudantes.isEmpty()) {
            return new java.util.HashMap<>();
        }
        // Usar servicio de estadísticas
        return ServicioDeEstadisticas.calcularTodas(ayudantes);
    }

    /**
     * Obtiene el director actual
     */
    public Director getDirector() {
        return directorActual;
    }

    /**
     * Obtiene el proyecto del director (usando lazy loading)
     */
    public Proyectos getProyecto() {
        return obtenerProyectoDelDirector();
    }

    /**
     * NUEVO: Obtiene información del proyecto (nombre, descripción, etc)
     */
    public String obtenerNombreProyecto() {
        Proyectos proyecto = obtenerProyectoDelDirector();
        if (proyecto == null) {
            return "Sin proyecto asignado";
        }
        return proyecto.getNombreProyecto();
    }

    /**
     * NUEVO: Obtiene cupos disponibles en el proyecto
     */
    public int obtenerCuposDisponibles() {
        Proyectos proyecto = obtenerProyectoDelDirector();
        if (proyecto == null) {
            return 0;
        }
        return proyecto.getCuposDisponibles();
    }

    /**
     * NUEVO: Verifica si hay cupo disponible
     */
    public boolean hayCapoDisponible() {
        Proyectos proyecto = obtenerProyectoDelDirector();
        if (proyecto == null) {
            return false;
        }
        return proyecto.tieneCupoDisponible();
    }

    /**
     * NUEVO: Crea un nuevo proyecto de investigación
     * 
     * RESPONSABILIDAD ÚNICA:
     * - Valida los datos del proyecto
     * - Verifica que el director no tenga proyecto activo
     * - Crea el proyecto y lo persiste en BD
     * - Notifica a la jefa de departamento
     * 
     * @param codigoProyecto Código único del proyecto
     * @param nombreProyecto Nombre del proyecto
     * @param descripcion Descripción del proyecto
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de finalización
     * @param tipoProyecto Tipo de proyecto (INTERNO, SEMILLA, etc.)
     * @param ayudantesPlanificados Número de ayudantes planificados
     * @return ResultadoOperacion con el resultado de la operación
     */
    public ResultadoOperacion crearProyecto(String codigoProyecto, String nombreProyecto, 
                                           String descripcion, Date fechaInicio, Date fechaFin,
                                           TipoProyecto tipoProyecto, int ayudantesPlanificados) {
        // Validar que el director pueda crear proyecto
        Proyectos proyectoExistente = obtenerProyectoDelDirector();
        ResultadoOperacion validacionDirector = directorActual.validarCreacionProyecto(proyectoExistente);
        if (!validacionDirector.esExitoso()) {
            return validacionDirector;
        }

        // Validar que el código no esté duplicado
        Proyectos proyectoDuplicado = proyectoDAO.buscarPorCodigo(codigoProyecto);
        if (proyectoDuplicado != null) {
            return ResultadoOperacion.fallido(
                "El código de proyecto ya existe",
                "Código duplicado: " + codigoProyecto
            );
        }

        // Validar datos del proyecto usando el modelo
        ResultadoOperacion validacionProyecto = Proyectos.validarCreacion(
            codigoProyecto, nombreProyecto, fechaInicio, fechaFin,
            tipoProyecto, ayudantesPlanificados
        );
        if (!validacionProyecto.esExitoso()) {
            return validacionProyecto;
        }

        // Crear el proyecto
        Proyectos nuevoProyecto = new Proyectos(
            codigoProyecto,
            nombreProyecto,
            descripcion,
            fechaInicio,
            fechaFin,
            "ACTIVO",
            tipoProyecto,
            ayudantesPlanificados
        );

        // Asignar el director al proyecto
        nuevoProyecto.setDirector(directorActual);

        // Guardar en BD
        if (!proyectoDAO.guardar(nuevoProyecto)) {
            return ResultadoOperacion.errorPersistencia("guardar el proyecto");
        }

        // Notificar a la jefa de departamento
        Notificacion notif = Notificacion.crearNuevoProyecto(nuevoProyecto, directorActual);
        notificacionDAO.guardar(notif);
        jefaDepartamento.recibirNotificacion(notif);

        return ResultadoOperacion.exitoso("Proyecto creado exitosamente");
    }

    /**
     * NUEVO: Obtiene asistentes del proyecto
     */
    public List<AsistenteInvestigacion> obtenerAsistentesProyecto() {
        Proyectos proyecto = obtenerProyectoDelDirector();
        if (proyecto == null) {
            return new java.util.ArrayList<>();
        }
        return asistenteDAO.buscarPorProyecto(proyecto.getCodigoProyecto());
    }

    /**
     * NUEVO: Obtiene técnicos del proyecto
     */
    public List<TecnicoInvestigacion> obtenerTecnicosProyecto() {
        Proyectos proyecto = obtenerProyectoDelDirector();
        if (proyecto == null) {
            return new java.util.ArrayList<>();
        }
        return tecnicoDAO.buscarPorProyecto(proyecto.getCodigoProyecto());
    }

    /**
     * NUEVO: Verifica si el director puede crear un proyecto
     * 
     * @return true si el director no tiene proyecto activo, false en caso contrario
     */
    public boolean puedeCrearProyecto() {
        Proyectos proyectoExistente = obtenerProyectoDelDirector();
        return Proyectos.puedeCrearNuevoProyecto(proyectoExistente);
    }
}