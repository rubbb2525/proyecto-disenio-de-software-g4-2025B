package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import model.service.ServicioDeEstadisticas;
import model.service.ServicioDeReportes;

/**
 * Singleton que representa la Jefa de Departamento
 * 
 * RESPONSABILIDAD ÚNICA:
 * Mantener los datos y responsabilidades de un usuario JEFA_DEPARTAMENTO
 * 
 * CAMBIOS PRINCIPALES:
 * - Se eliminó filtrarAyudantes() → Usar ServicioDeFiltrado
 * - Se mantienen notificaciones (son propiedad de la jefa)
 * - Se simplificó para NO mezclar responsabilidades
 */
public class JefaDepartamento extends MiembroEPN {
    private static JefaDepartamento instancia;
    private List<Notificacion> notificaciones;

    private JefaDepartamento() {
        super();
        this.notificaciones = new ArrayList<>();
        inicializarCodigoUnico("JEFA_DPTO");
        inicializarCorreo("jefa@fis.epn.edu.ec");
        inicializarPassword("admin");
        inicializarNombres("Jefa");
        inicializarApellidos("Departamento");
        inicializarRol("JEFA_DEPARTAMENTO");
        inicializarEstado("ACTIVO");
    }

    /**
     * Obtiene la instancia única (Singleton)
     */
    public static JefaDepartamento getInstancia() {
        if (instancia == null) {
            instancia = new JefaDepartamento();
        }
        return instancia;
    }

    // ============ RESPONSABILIDADES DE NOTIFICACIONES ============
    // Estas SÍ pertenecen a JefaDepartamento (es quien las recibe)
    
    /**
     * Recibe una notificación
     */
    public void recibirNotificacion(Notificacion notificacion) {
        if (notificacion != null) {
            notificaciones.add(notificacion);
        }
    }

    /**
     * Carga las notificaciones desde la BD
     */
    public void cargarNotificacionesDesdeBD(List<Notificacion> notificacionesBD) {
        if (notificacionesBD != null) {
            this.notificaciones = new ArrayList<>(notificacionesBD);
        }
    }

    /**
     * Obtiene todas las notificaciones (copia inmutable)
     */
    public List<Notificacion> getNotificaciones() {
        return Collections.unmodifiableList(notificaciones);
    }

    /**
     * Obtiene solo las notificaciones no leídas
     */
    public List<Notificacion> getNotificacionesNoLeidas() {
        return notificaciones.stream()
                .filter(n -> !n.isLeida())
                .collect(Collectors.toList());
    }

    /**
     * Obtiene la cantidad de notificaciones no leídas
     */
    public int contarNotificacionesNoLeidas() {
        return (int) notificaciones.stream()
                .filter(n -> !n.isLeida())
                .count();
    }

    /**
     * Marca todas las notificaciones como leídas
     */
    public void marcarTodasComoLeidas() {
        notificaciones.forEach(Notificacion::marcarComoLeida);
    }

    /**
     * Marca una notificación específica como leída
     */
    public void marcarComoLeida(Notificacion notificacion) {
        if (notificacion != null) {
            notificacion.marcarComoLeida();
        }
    }

    /**
     * Elimina una notificación
     */
    public void eliminarNotificacion(Notificacion notificacion) {
        if (notificacion != null) {
            notificaciones.remove(notificacion);
        }
    }

    /**
     * Limpia todas las notificaciones
     */
    public void limpiarNotificaciones() {
        notificaciones.clear();
    }

    /**
     * Obtiene el total de notificaciones
     */
    public int contarTodasLasNotificaciones() {
        return notificaciones.size();
    }

    // ============ LÓGICA DE DOMINIO (SIN PERSISTENCIA) ============

    /**
     * Filtra ayudantes según criterios
     */
    public List<Ayudante> filtrarAyudantes(List<Ayudante> ayudantes, Map<String, Object> filtros) {
        return Ayudante.filtrar(ayudantes, filtros);
    }

    /**
     * Obtiene ayudantes activos
     */
    public List<Ayudante> obtenerAyudantesActivos(List<Ayudante> ayudantes) {
        return Ayudante.obtenerActivos(ayudantes);
    }

    /**
     * Obtiene ayudantes inactivos
     */
    public List<Ayudante> obtenerAyudantesInactivos(List<Ayudante> ayudantes) {
        return Ayudante.obtenerInactivos(ayudantes);
    }

    /**
     * Obtiene ayudantes por carrera
     */
    public List<Ayudante> obtenerAyudantesPorCarrera(List<Ayudante> ayudantes, String carrera) {
        return Ayudante.porCarrera(ayudantes, carrera);
    }

    /**
     * Obtiene ayudantes por nivel
     */
    public List<Ayudante> obtenerAyudantesPorNivel(List<Ayudante> ayudantes, int nivel) {
        return Ayudante.porNivel(ayudantes, nivel);
    }

    /**
     * Obtiene estadísticas generales
     */
    public Map<String, Object> obtenerEstadisticasGenerales(List<Ayudante> ayudantes) {
        return ServicioDeEstadisticas.calcularTodas(ayudantes);
    }

    /**
     * Obtiene estadísticas por carrera
     */
    public Map<String, Map<String, Object>> obtenerEstadisticasPorCarrera(List<Ayudante> ayudantes) {
        return ServicioDeEstadisticas.estadisticasPorCarrera(ayudantes);
    }

    /**
     * Obtiene estadísticas por nivel
     */
    public Map<Integer, Map<String, Object>> obtenerEstadisticasPorNivel(List<Ayudante> ayudantes) {
        return ServicioDeEstadisticas.estadisticasPorNivel(ayudantes);
    }

    /**
     * Retorna resumen por proyecto: codigo, nombre, planificados, contratados activos,
     * cupos disponibles, estado, inicio, fin
     */
    public List<Object[]> obtenerResumenProyectos(List<Proyectos> proyectos, Map<String, List<Ayudante>> ayudantesPorProyecto) {
        List<Object[]> resumen = new ArrayList<>();
        for (Proyectos p : proyectos) {
            List<Ayudante> ayudantesProyecto = ayudantesPorProyecto.getOrDefault(p.getCodigoProyecto(), List.of());
            Object[] fila = p.crearResumen(ayudantesProyecto);
            resumen.add(fila);
        }
        return resumen;
    }

    /**
     * Genera un reporte general
     */
    public Reporte generarReporteGeneral(List<Proyectos> proyectos, List<Ayudante> ayudantes) {
        return ServicioDeReportes.generarReporteGeneral(proyectos, ayudantes);
    }

    /**
     * Genera un reporte por proyecto
     */
    public Reporte generarReportePorProyecto(Proyectos proyecto) {
        if (proyecto == null) {
            return null;
        }
        return ServicioDeReportes.generarReportePorProyecto(proyecto);
    }

    /**
     * Genera un reporte por carrera
     */
    public Reporte generarReportePorCarrera(String carrera, List<Ayudante> ayudantes) {
        return ServicioDeReportes.generarReportePorCarrera(carrera, ayudantes);
    }

    /**
     * Genera un reporte por nivel
     */
    public Reporte generarReportePorNivel(int nivel, List<Ayudante> ayudantes) {
        return ServicioDeReportes.generarReportePorNivel(nivel, ayudantes);
    }

    // ============ RESPONSABILIDAD DE USUARIO ============
    
    @Override
    public boolean esActivo() {
        return "ACTIVO".equals(getEstado());
    }

    // ============ GETTERS Y SETTERS ============
    
    public List<Notificacion> getNotificacionesList() {
        return Collections.unmodifiableList(notificaciones);
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }

    /**
     * Para validación de acceso
     */
    @Override
    public String toString() {
        return "JefaDepartamento{" +
                "nombre='" + getNombresCompletos() + '\'' +
                ", correo='" + getCorreoInstitucional() + '\'' +
                ", estado='" + getEstado() + '\'' +
                ", notificacionesNoLeidas=" + contarNotificacionesNoLeidas() +
                '}';
    }
}