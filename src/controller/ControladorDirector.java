package controller;

import model.*;
import model.dao.*;
import model.service.*;
import java.util.Date;
import java.util.List;

/**
 * Controlador para operaciones del Director
 * 
 * REFACTORIZACIÓN:
 * - Usa ServicioConversionAyudante en lugar de llamar a Estudiante.convertirAAyudante()
 * - Obtiene proyecto via DAO en lugar de mantenerlo como atributo en Director
 * - Usa ServicioDeFiltrado para filtrado (no JefaDepartamento)
 * - Usa ServicioDeEstadisticas para estadísticas
 */
public class ControladorDirector {
    private Director directorActual;
    private AyudanteDAO ayudanteDAO;
    private EstudianteDAO estudianteDAO;
    private ProyectoDAO proyectoDAO;
    private NotificacionDAO notificacionDAO;
    private JefaDepartamento jefaDepartamento;
    
    // INYECCIÓN DE SERVICIOS
    private ServicioConversionAyudante servicioConversion;
    private ServicioDeFiltrado servicioDeFiltrado;
    private ServicioDeEstadisticas servicioEstadisticas;

    public ControladorDirector(Director director, AyudanteDAO ayudanteDAO, 
                              EstudianteDAO estudianteDAO, ProyectoDAO proyectoDAO) {
        this.directorActual = director;
        this.ayudanteDAO = ayudanteDAO;
        this.estudianteDAO = estudianteDAO;
        this.proyectoDAO = proyectoDAO;
        this.notificacionDAO = new NotificacionDAO();
        this.jefaDepartamento = JefaDepartamento.getInstancia();
        
        // Inicializar servicios (son clases utilitarias, no instancias)
        // ServicioDeFiltrado, ServicioConversion y ServicioDeEstadisticas 
        // tienen métodos estáticos
    }

    /**
     * NUEVO: Obtiene el proyecto del director (Lazy Loading)
     * Antes: director.getProyectoAsignado() (podía ser null)
     * Ahora: Se obtiene cuando se necesita via DAO
     */
    public ProyectoInvestigacion obtenerProyectoDelDirector() {
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
     * - Usa ServicioConversionAyudante en lugar de estudiante.convertirAAyudante()
     * - Valida antes de convertir usando puedeConvertirse()
     * - Obtiene mensajes de error descriptivos
     */
    public ResultadoOperacion registrarAyudante(String codigoEstudiante, int horas, double salario) {
        ResultadoOperacion resultado = new ResultadoOperacion();

        // Obtener proyecto del director
        ProyectoInvestigacion proyecto = obtenerProyectoDelDirector();
        if (proyecto == null) {
            resultado.setMensaje("Director sin proyecto asignado");
            resultado.agregarError("Proyecto no disponible");
            return resultado;
        }

        // Buscar estudiante
        Estudiante estudiante = buscarEstudiante(codigoEstudiante);
        if (estudiante == null) {
            resultado.setMensaje("Estudiante no encontrado");
            resultado.agregarError("Código de estudiante inválido");
            return resultado;
        }

        // CAMBIO: Usar ServicioConversionAyudante
        // Validar que se puede convertir
        if (!ServicioConversionAyudante.puedeConvertirse(estudiante, horas, salario)) {
            String errorMsg = ServicioConversionAyudante.obtenerMensajeError(
                estudiante, horas, salario
            );
            resultado.setMensaje(errorMsg);
            resultado.agregarError("Conversión no válida");
            return resultado;
        }

        // Convertir usando el servicio
        Ayudante ayudante = ServicioConversionAyudante.convertirEstudianteAAyudante(
            estudiante,
            proyecto,
            horas,
            salario
        );

        if (ayudante == null) {
            resultado.setMensaje("No se pudo convertir a ayudante");
            resultado.agregarError("Error en la conversión");
            return resultado;
        }

        // Guardar en BD
        if (!ayudanteDAO.guardar(ayudante)) {
            resultado.setMensaje("Error al guardar en BD");
            resultado.agregarError("Error de persistencia");
            return resultado;
        }

        // Notificar a jefa
        Notificacion notif = new Notificacion(
            "NOT_" + System.currentTimeMillis(),
            "Nuevo ayudante registrado: " + ayudante.getNombresCompletos() + 
            " en proyecto " + proyecto.getNombreProyecto(),
            "REGISTRO_AYUDANTE"
        );
        notif.setAyudanteRelacionado(ayudante);
        notif.setProyectoRelacionado(proyecto);
        
        // Guardar en BD
        notificacionDAO.guardar(notif);
        
        // Enviar a Jefa en memoria
        jefaDepartamento.recibirNotificacion(notif);

        resultado.setExitoso(true);
        resultado.setMensaje("Ayudante registrado exitosamente");

        return resultado;
    }

    /**
     * Da de baja un ayudante
     */
    public ResultadoOperacion darDeBajaAyudante(String codigoAyudante, String motivo, Date fecha) {
        ResultadoOperacion resultado = new ResultadoOperacion();

        // Buscar ayudante
        Ayudante ayudante = ayudanteDAO.buscarPorId(codigoAyudante);
        if (ayudante == null) {
            resultado.setMensaje("Ayudante no encontrado");
            resultado.agregarError("Código de ayudante inválido");
            return resultado;
        }

        // Dar de baja
        ayudante.darDeBaja(motivo, fecha);

        // Actualizar en BD
        if (!ayudanteDAO.actualizar(ayudante)) {
            resultado.setMensaje("Error al actualizar en BD");
            resultado.agregarError("Error de persistencia");
            return resultado;
        }

        // Notificar a jefa
        Notificacion notif = new Notificacion(
            "NOT_" + System.currentTimeMillis(),
            "Ayudante dado de baja: " + ayudante.getNombresCompletos() + 
            " - Motivo: " + motivo,
            "BAJA_AYUDANTE"
        );
        notif.setAyudanteRelacionado(ayudante);
        
        // Guardar en BD
        notificacionDAO.guardar(notif);
        
        // Enviar a Jefa en memoria
        jefaDepartamento.recibirNotificacion(notif);

        resultado.setExitoso(true);
        resultado.setMensaje("Ayudante dado de baja exitosamente");

        return resultado;
    }

    /**
     * Consulta los ayudantes del proyecto
     * 
     * REFACTORIZACIÓN:
     * - Obtiene proyecto via método (no como atributo)
     */
    public List<Ayudante> consultarAyudantesDelProyecto() {
        ProyectoInvestigacion proyecto = obtenerProyectoDelDirector();
        if (proyecto == null) {
            return null;
        }
        return ayudanteDAO.buscarPorProyecto(proyecto.getCodigoProyecto());
    }

    /**
     * NUEVO: Obtiene ayudantes activos del proyecto
     * Usa ServicioDeFiltrado para filtrado
     */
    public List<Ayudante> obtenerAyudantesActivos() {
        List<Ayudante> ayudantes = consultarAyudantesDelProyecto();
        if (ayudantes == null) {
            return null;
        }
        // Usar servicio de filtrado
        return ServicioDeFiltrado.obtenerActivos(ayudantes);
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
    public ProyectoInvestigacion getProyecto() {
        return obtenerProyectoDelDirector();
    }

    /**
     * NUEVO: Obtiene información del proyecto (nombre, descripción, etc)
     */
    public String obtenerNombreProyecto() {
        ProyectoInvestigacion proyecto = obtenerProyectoDelDirector();
        if (proyecto == null) {
            return "Sin proyecto asignado";
        }
        return proyecto.getNombreProyecto();
    }

    /**
     * NUEVO: Obtiene cupos disponibles en el proyecto
     */
    public int obtenerCuposDisponibles() {
        ProyectoInvestigacion proyecto = obtenerProyectoDelDirector();
        if (proyecto == null) {
            return 0;
        }
        return proyecto.getCuposDisponibles();
    }

    /**
     * NUEVO: Verifica si hay cupo disponible
     */
    public boolean hayCapoDisponible() {
        ProyectoInvestigacion proyecto = obtenerProyectoDelDirector();
        if (proyecto == null) {
            return false;
        }
        return proyecto.tieneCupoDisponible();
    }
}