package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        this.codigoUnico = "JEFA_DPTO";
        this.correoInstitucional = "jefa@fis.epn.edu.ec";
        this.password = "admin";
        this.nombres = "Jefa";
        this.apellidos = "Departamento";
        this.rol = "JEFA_DEPARTAMENTO";
        this.estado = "ACTIVO";
        this.notificaciones = new ArrayList<>();
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
     * Obtiene todas las notificaciones
     */
    public List<Notificacion> getNotificaciones() {
        return new ArrayList<>(notificaciones);
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

    // ============ RESPONSABILIDAD DE USUARIO ============
    
    @Override
    public boolean esActivo() {
        return "ACTIVO".equals(estado);
    }

    // ============ GETTERS Y SETTERS ============
    
    public List<Notificacion> getNotificacionesList() {
        return notificaciones;
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
                ", correo='" + correoInstitucional + '\'' +
                ", estado='" + estado + '\'' +
                ", notificacionesNoLeidas=" + contarNotificacionesNoLeidas() +
                '}';
    }
}