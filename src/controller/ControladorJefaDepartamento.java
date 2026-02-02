package controller;

import model.*;
import model.dao.*;
import model.service.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Controlador para operaciones de la Jefa de Departamento
 * 
 * REFACTORIZACIÓN:
 * - Usa ServicioDeFiltrado en lugar de jefaDepartamento.filtrarAyudantes()
 * - Usa ServicioDeEstadisticas en lugar de calcularEstadisticas() directo
 * - GeneradorReportes ahora solo orquesta (no calcula estadísticas)
 * - JefaDepartamento ahora solo maneja datos de usuario y notificaciones
 */
public class ControladorJefaDepartamento {
    private JefaDepartamento jefaDepartamento;
    private AyudanteDAO ayudanteDAO;
    private ProyectoDAO proyectoDAO;
    
    // INYECCIÓN DE SERVICIOS
    private ServicioDeFiltrado servicioDeFiltrado;
    private ServicioDeEstadisticas servicioEstadisticas;

    public ControladorJefaDepartamento(JefaDepartamento jefa, AyudanteDAO ayudanteDAO, 
                                      ProyectoDAO proyectoDAO) {
        this.jefaDepartamento = jefa;
        this.ayudanteDAO = ayudanteDAO;
        this.proyectoDAO = proyectoDAO;
        
        // ServicioDeFiltrado y ServicioDeEstadisticas tienen métodos estáticos
        // No es necesario instanciarlos
    }

    /**
     * Filtra ayudantes según criterios
     * 
     * REFACTORIZACIÓN:
     * - Usa ServicioDeFiltrado en lugar de jefaDepartamento.filtrarAyudantes()
     * - JefaDepartamento ya NO tiene responsabilidad de filtrado
     */
    public List<Ayudante> filtrarAyudantes(Map<String, Object> filtros) {
        List<Ayudante> todosAyudantes = ayudanteDAO.listarTodos();
        
        // CAMBIO: Usar ServicioDeFiltrado
        return ServicioDeFiltrado.filtrarAyudantes(todosAyudantes, filtros);
    }

    /**
     * NUEVO: Obtiene ayudantes activos
     * Usa ServicioDeFiltrado
     */
    public List<Ayudante> obtenerAyudantesActivos() {
        List<Ayudante> todos = ayudanteDAO.listarTodos();
        return ServicioDeFiltrado.obtenerActivos(todos);
    }

    /**
     * NUEVO: Obtiene ayudantes inactivos
     * Usa ServicioDeFiltrado
     */
    public List<Ayudante> obtenerAyudantesInactivos() {
        List<Ayudante> todos = ayudanteDAO.listarTodos();
        return ServicioDeFiltrado.obtenerInactivos(todos);
    }

    /**
     * NUEVO: Obtiene ayudantes de una carrera específica
     * Usa ServicioDeFiltrado
     */
    public List<Ayudante> obtenerAyudantesPorCarrera(String carrera) {
        List<Ayudante> todos = ayudanteDAO.listarTodos();
        return ServicioDeFiltrado.porCarrera(todos, carrera);
    }

    /**
     * NUEVO: Obtiene ayudantes de un nivel específico
     * Usa ServicioDeFiltrado
     */
    public List<Ayudante> obtenerAyudantesPorNivel(int nivel) {
        List<Ayudante> todos = ayudanteDAO.listarTodos();
        return ServicioDeFiltrado.porNivel(todos, nivel);
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
    public List<ProyectoInvestigacion> obtenerTodosProyectos() {
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
        List<ProyectoInvestigacion> proyectos = proyectoDAO.listarTodos();
        for (ProyectoInvestigacion p : proyectos) {
            int planificados = p.getAyudantesPlanificados();
            List<Ayudante> ayudantesProyecto = ayudanteDAO.buscarPorProyecto(p.getCodigoProyecto());
            
            // Usar servicio de estadísticas para contar activos
            long contratadosActivos = ServicioDeEstadisticas.contarActivos(ayudantesProyecto);
            int cupos = Math.max(0, planificados - (int)contratadosActivos);
            Object[] fila = {
                p.getCodigoProyecto(),
                p.getNombreProyecto(),
                planificados,
                contratadosActivos,
                cupos,
                p.getEstado(),
                p.getFechaInicio(),
                p.getFechaFin()
            };
            resumen.add(fila);
        }
        return resumen;
    }

    /**
     * Genera un reporte general
     * 
     * REFACTORIZACIÓN:
     * - GeneradorReportes ahora solo orquesta
     * - Los servicios hacen el trabajo específico
     */
    public Reporte generarReporteGeneral() {
        List<ProyectoInvestigacion> proyectos = proyectoDAO.listarTodos();
        List<Ayudante> ayudantes = ayudanteDAO.listarTodos();
        
        GeneradorReportes generador = new GeneradorReportes(proyectos, ayudantes);
        return generador.generarReporteGeneral();
    }

    /**
     * Genera un reporte por proyecto
     */
    public Reporte generarReportePorProyecto(String codigoProyecto) {
        ProyectoInvestigacion proyecto = proyectoDAO.buscarPorId(codigoProyecto);
        if (proyecto == null) {
            return null;
        }
        List<ProyectoInvestigacion> proyectos = proyectoDAO.listarTodos();
        List<Ayudante> ayudantes = ayudanteDAO.listarTodos();
        
        GeneradorReportes generador = new GeneradorReportes(proyectos, ayudantes);
        return generador.generarReportePorProyecto(proyecto);
    }

    /**
     * Genera un reporte por carrera
     */
    public Reporte generarReportePorCarrera(String carrera) {
        List<ProyectoInvestigacion> proyectos = proyectoDAO.listarTodos();
        List<Ayudante> ayudantes = ayudanteDAO.listarTodos();
        
        GeneradorReportes generador = new GeneradorReportes(proyectos, ayudantes);
        return generador.generarReportePorCarrera(carrera);
    }

    /**
     * Genera un reporte por nivel
     */
    public Reporte generarReportePorNivel(int nivel) {
        List<ProyectoInvestigacion> proyectos = proyectoDAO.listarTodos();
        List<Ayudante> ayudantes = ayudanteDAO.listarTodos();
        
        GeneradorReportes generador = new GeneradorReportes(proyectos, ayudantes);
        return generador.generarReportePorNivel(nivel);
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
    }

    /**
     * NUEVO: Marca una notificación específica como leída
     */
    public void marcarNotificacionComoLeida(Notificacion notificacion) {
        jefaDepartamento.marcarComoLeida(notificacion);
    }

    /**
     * Obtiene la cantidad de notificaciones no leídas
     */
    public int obtenerCantidadNotificacionesNoLeidas() {
        return jefaDepartamento.contarNotificacionesNoLeidas();
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
            // ExportadorPDF.exportarReporte(rutaArchivo, reporte);
            // Nota: Requiere librerías iText PDF
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
    public List<ProyectoInvestigacion> obtenerProyectosActivos() {
        return proyectoDAO.listarTodos().stream()
            .filter(p -> "ACTIVO".equals(p.getEstado()))
            .collect(java.util.stream.Collectors.toList());
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
}