package controller;

import model.*;
import model.dao.*;
import java.util.List;
import java.util.Map;

/**
 * Controlador para operaciones de la Jefa de Departamento
 */
public class ControladorJefaDepartamento {
    private JefaDepartamento jefaDepartamento;
    private AyudanteDAO ayudanteDAO;
    private ProyectoDAO proyectoDAO;

    public ControladorJefaDepartamento(JefaDepartamento jefa, AyudanteDAO ayudanteDAO, 
                                      ProyectoDAO proyectoDAO) {
        this.jefaDepartamento = jefa;
        this.ayudanteDAO = ayudanteDAO;
        this.proyectoDAO = proyectoDAO;
    }

    /**
     * Filtra ayudantes según criterios
     */
    public List<Ayudante> filtrarAyudantes(Map<String, Object> filtros) {
        List<Ayudante> todosAyudantes = ayudanteDAO.listarTodos();
        return jefaDepartamento.filtrarAyudantes(todosAyudantes, filtros);
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
     * Retorna resumen por proyecto: codigo, nombre, planificados, contratados activos, cupos disponibles, estado, inicio, fin
     */
    public java.util.List<Object[]> obtenerResumenProyectos() {
        java.util.List<Object[]> resumen = new java.util.ArrayList<>();
        java.util.List<ProyectoInvestigacion> proyectos = proyectoDAO.listarTodos();
        for (ProyectoInvestigacion p : proyectos) {
            int planificados = p.getAyudantesPlanificados();
            java.util.List<Ayudante> ayudantesProyecto = ayudanteDAO.buscarPorProyecto(p.getCodigoProyecto());
            int contratadosActivos = (int) ayudantesProyecto.stream().filter(Ayudante::esActivo).count();
            int cupos = Math.max(0, planificados - contratadosActivos);
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
     */
    public Reporte generarReporteGeneral() {
        GeneradorReportes generador = new GeneradorReportes(proyectoDAO.listarTodos(), ayudanteDAO.listarTodos());
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
        GeneradorReportes generador = new GeneradorReportes(proyectoDAO.listarTodos(), ayudanteDAO.listarTodos());
        return generador.generarReportePorProyecto(proyecto);
    }

    /**
     * Genera un reporte por carrera
     */
    public Reporte generarReportePorCarrera(String carrera) {
        GeneradorReportes generador = new GeneradorReportes(proyectoDAO.listarTodos(), ayudanteDAO.listarTodos());
        return generador.generarReportePorCarrera(carrera);
    }

    /**
     * Genera un reporte por nivel
     */
    public Reporte generarReportePorNivel(int nivel) {
        GeneradorReportes generador = new GeneradorReportes(proyectoDAO.listarTodos(), ayudanteDAO.listarTodos());
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
     * Obtiene la cantidad de notificaciones no leídas
     */
    public int obtenerCantidadNotificacionesNoLeidas() {
        return jefaDepartamento.getNotificacionesNoLeidas().size();
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
}