package controller;

import model.*;
import model.dao.*;
import model.service.ServicioDeEstadisticas;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Controlador para operaciones de la Jefa de Departamento
 * 
 * REFACTORIZACIÓN:
 * - Usa Ayudante.filtrar/obtenerActivos/etc. en lugar de ServicioDeFiltrado
 * - Usa ServicioDeEstadisticas para estadísticas agregadas
 * - JefaDepartamento genera reportes (no calcula estadísticas)
 * - JefaDepartamento ahora solo maneja datos de usuario y notificaciones
 */
public class ControladorJefaDepartamento {
    private JefaDepartamento jefaDepartamento;
    private AyudanteDAO ayudanteDAO;
    private ProyectoDAO proyectoDAO;
    private AsistenteDAO asistenteDAO;
    private TecnicoDAO tecnicoDAO;
    private NotificacionDAO notificacionDAO;

    public ControladorJefaDepartamento(JefaDepartamento jefa, AyudanteDAO ayudanteDAO, 
                                      ProyectoDAO proyectoDAO) {
        this.jefaDepartamento = jefa;
        this.ayudanteDAO = ayudanteDAO;
        this.proyectoDAO = proyectoDAO;
        this.asistenteDAO = new AsistenteDAO();
        this.tecnicoDAO = new TecnicoDAO();
        this.notificacionDAO = new NotificacionDAO();
        
        // Cargar notificaciones desde BD al inicializar
        cargarNotificacionesDelSistema();
        
        // ServicioDeEstadisticas tiene métodos estáticos
        // No es necesario instanciarlo
    }
    
    /**
     * Carga todas las notificaciones desde la BD
     */
    private void cargarNotificacionesDelSistema() {
        List<Notificacion> notificacionesBD = notificacionDAO.listarTodas();
        jefaDepartamento.cargarNotificacionesDesdeBD(notificacionesBD);
    }

    /**
     * Filtra ayudantes según criterios
     * 
     * REFACTORIZACIÓN:
     * - Usa Ayudante.filtrar en lugar de ServicioDeFiltrado
     * - JefaDepartamento ya NO tiene responsabilidad de filtrado
     */
    public List<Ayudante> filtrarAyudantes(Map<String, Object> filtros) {
        List<Ayudante> todosAyudantes = ayudanteDAO.listarTodos();
        
        // CAMBIO: Usar método estático del modelo
        return Ayudante.filtrar(todosAyudantes, filtros);
    }

    /**
     * NUEVO: Obtiene ayudantes activos
     * Usa Ayudante.obtenerActivos
     */
    public List<Ayudante> obtenerAyudantesActivos() {
        List<Ayudante> todos = ayudanteDAO.listarTodos();
        return Ayudante.obtenerActivos(todos);
    }

    /**
     * NUEVO: Obtiene ayudantes inactivos
     * Usa Ayudante.obtenerInactivos
     */
    public List<Ayudante> obtenerAyudantesInactivos() {
        List<Ayudante> todos = ayudanteDAO.listarTodos();
        return Ayudante.obtenerInactivos(todos);
    }

    /**
     * NUEVO: Obtiene ayudantes de una carrera específica
     * Usa Ayudante.porCarrera
     */
    public List<Ayudante> obtenerAyudantesPorCarrera(String carrera) {
        List<Ayudante> todos = ayudanteDAO.listarTodos();
        return Ayudante.porCarrera(todos, carrera);
    }

    /**
     * NUEVO: Obtiene ayudantes de un nivel específico
     * Usa Ayudante.porNivel
     */
    public List<Ayudante> obtenerAyudantesPorNivel(int nivel) {
        List<Ayudante> todos = ayudanteDAO.listarTodos();
        return Ayudante.porNivel(todos, nivel);
    }

    /**
     * Obtiene todos los ayudantes
     */
    public List<Ayudante> obtenerTodosAyudantes() {
        return ayudanteDAO.listarTodos();
    }

    /**
     * Obtiene todos los proyectos
     */
    public List<Proyectos> obtenerTodosProyectos() {
        return proyectoDAO.listarTodos();
    }

    /**
     * NUEVO: Obtiene estadísticas de todos los ayudantes
     * Usa ServicioDeEstadisticas
     */
    public Map<String, Object> obtenerEstadisticasGenerales() {
        List<Ayudante> todos = ayudanteDAO.listarTodos();
        return ServicioDeEstadisticas.calcularTodas(todos);
    }

    /**
     * NUEVO: Obtiene estadísticas por carrera
     * Usa ServicioDeEstadisticas
     */
    public Map<String, Map<String, Object>> obtenerEstadisticasPorCarrera() {
        List<Ayudante> todos = ayudanteDAO.listarTodos();
        return ServicioDeEstadisticas.estadisticasPorCarrera(todos);
    }

    /**
     * NUEVO: Obtiene estadísticas por nivel
     * Usa ServicioDeEstadisticas
     */
    public Map<Integer, Map<String, Object>> obtenerEstadisticasPorNivel() {
        List<Ayudante> todos = ayudanteDAO.listarTodos();
        return ServicioDeEstadisticas.estadisticasPorNivel(todos);
    }

    /**
     * Retorna resumen por proyecto: codigo, nombre, planificados, contratados activos, 
     * cupos disponibles, estado, inicio, fin
     */
    public List<Object[]> obtenerResumenProyectos() {
        List<Object[]> resumen = new java.util.ArrayList<>();
        List<Proyectos> proyectos = proyectoDAO.listarTodos();
        for (Proyectos p : proyectos) {
            List<Ayudante> ayudantesProyecto = ayudanteDAO.buscarPorProyecto(p.getCodigoProyecto());

            Object[] fila = p.crearResumen(ayudantesProyecto);
            resumen.add(fila);
        }
        return resumen;
    }

    /**
     * Genera un reporte general
     * 
     * REFACTORIZACIÓN:
    * - JefaDepartamento genera reportes
     * - Los servicios hacen el trabajo específico
     */
    public Reporte generarReporteGeneral() {
        List<Proyectos> proyectos = proyectoDAO.listarTodos();
        List<Ayudante> ayudantes = ayudanteDAO.listarTodos();

        return jefaDepartamento.generarReporteGeneral(proyectos, ayudantes);
    }

    /**
     * Genera un reporte por proyecto
     */
    public Reporte generarReportePorProyecto(String codigoProyecto) {
        Proyectos proyecto = proyectoDAO.buscarPorId(codigoProyecto);
        if (proyecto == null) {
            return null;
        }
        return jefaDepartamento.generarReportePorProyecto(proyecto);
    }

    /**
     * Genera un reporte por carrera
     */
    public Reporte generarReportePorCarrera(String carrera) {
        List<Ayudante> ayudantes = ayudanteDAO.listarTodos();

        return jefaDepartamento.generarReportePorCarrera(carrera, ayudantes);
    }

    /**
     * Genera un reporte por nivel
     */
    public Reporte generarReportePorNivel(int nivel) {
        List<Ayudante> ayudantes = ayudanteDAO.listarTodos();

        return jefaDepartamento.generarReportePorNivel(nivel, ayudantes);
    }

    /**
     * Obtiene las notificaciones de la jefa
     */
    public List<Notificacion> obtenerNotificaciones() {
        return jefaDepartamento.getNotificaciones();
    }

    /**
     * Obtiene las notificaciones no leídas
     */
    public List<Notificacion> obtenerNotificacionesNoLeidas() {
        return jefaDepartamento.getNotificacionesNoLeidas();
    }

    /**
     * Marca todas las notificaciones como leídas
     */
    public void marcarNotificacionesComoLeidas() {
        jefaDepartamento.marcarTodasComoLeidas();
        // Sincronizar cambios en BD
        List<Notificacion> notificaciones = jefaDepartamento.getNotificaciones();
        for (Notificacion notif : notificaciones) {
            if (notif.isLeida()) {
                notificacionDAO.marcarComoLeida(notif.getIdNotificacion());
            }
        }
    }

    /**
     * Marca una notificación específica como leída
     */
    public void marcarNotificacionComoLeida(Notificacion notificacion) {
        jefaDepartamento.marcarComoLeida(notificacion);
        // Sincronizar en BD
        if (notificacion != null) {
            notificacionDAO.marcarComoLeida(notificacion.getIdNotificacion());
        }
    }

    /**
     * Obtiene la cantidad de notificaciones no leídas
     */
    public int obtenerCantidadNotificacionesNoLeidas() {
        return jefaDepartamento.contarNotificacionesNoLeidas();
    }

    /**
     * Sincroniza las notificaciones con la BD
     * Útil para refrescar después de cambios externos
     */
    public void sincronizarNotificaciones() {
        List<Notificacion> notificacionesBD = notificacionDAO.listarTodas();
        jefaDepartamento.cargarNotificacionesDesdeBD(notificacionesBD);
    }

    /**
     * Obtiene la jefa de departamento
     */
    public JefaDepartamento getJefa() {
        return jefaDepartamento;
    }

    /**
     * Exporta reporte a PDF
     */
    public boolean exportarReportePDF(Reporte reporte, String rutaArchivo) {
        try {
            reporte.exportarPDF(rutaArchivo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sincroniza las notificaciones desde la BD hacia el modelo (JefaDepartamento)
     * Mantiene la lógica de persistencia fuera de la vista.
     */
    public void sincronizarNotificacionesConBD() {
        try {
            NotificacionDAO notifDAO = new NotificacionDAO();
            List<Notificacion> notificacionesBD = notifDAO.listarTodos();
            jefaDepartamento.setNotificaciones(notificacionesBD);
        } catch (Exception e) {
            System.out.println("Error al sincronizar notificaciones: " + e.getMessage());
        }
    }

    /**
     * NUEVO: Obtiene información sobre proyectos activos
     */
    public List<Proyectos> obtenerProyectosActivos() {
        return Proyectos.filtrarActivos(proyectoDAO.listarTodos());
    }

    /**
     * NUEVO: Cuenta total de ayudantes activos
     */
    public long contarAyudantesActivos() {
        List<Ayudante> todos = ayudanteDAO.listarTodos();
        return ServicioDeEstadisticas.contarActivos(todos);
    }

    /**
     * NUEVO: Cuenta total de ayudantes inactivos
     */
    public long contarAyudantesInactivos() {
        List<Ayudante> todos = ayudanteDAO.listarTodos();
        return ServicioDeEstadisticas.contarInactivos(todos);
    }

    /**
     * NUEVO: Obtiene proyecto por código
     */
    public Proyectos obtenerProyectoPorCodigo(String codigo) {
        return proyectoDAO.buscarPorCodigo(codigo);
    }

    /**
     * NUEVO: Obtiene nombre del director
     */
    public String obtenerNombreDirector(String codigoDirector) {
        // Este método requerirá acceso a MiembroEPN DAO
        // Por ahora retorna el código
        return codigoDirector != null ? codigoDirector : "Sin asignar";
    }

    /**
     * NUEVO: Cuenta personal por proyecto (ayudantes, asistentes, técnicos)
     */
    public Map<String, Integer> contarPersonalPorProyecto(String codigoProyecto) {
        Map<String, Integer> conteo = new HashMap<>();
        
        // Contar ayudantes
        List<Ayudante> ayudantes = ayudanteDAO.buscarPorProyecto(codigoProyecto);
        conteo.put("ayudantes", ayudantes.size());
        
        // Los asistentes y técnicos requieren DAOs adicionales
        // Por ahora los dejamos en 0
        conteo.put("asistentes", 0);
        conteo.put("tecnicos", 0);
        
        return conteo;
    }

    /**
     * NUEVO: Obtiene ayudantes de un proyecto
     */
    public List<Ayudante> obtenerAyudantesProyecto(String codigoProyecto) {
        return ayudanteDAO.buscarPorProyecto(codigoProyecto);
    }

    /**
     * NUEVO: Obtiene asistentes de un proyecto
     */
    public List<AsistenteInvestigacion> obtenerAsistentesProyecto(String codigoProyecto) {
        return asistenteDAO.buscarPorProyecto(codigoProyecto);
    }

    /**
     * NUEVO: Obtiene técnicos de un proyecto
     */
    public List<TecnicoInvestigacion> obtenerTecnicosProyecto(String codigoProyecto) {
        return tecnicoDAO.buscarPorProyecto(codigoProyecto);
    }
}