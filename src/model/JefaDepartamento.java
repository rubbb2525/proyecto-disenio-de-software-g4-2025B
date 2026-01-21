package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Singleton que representa la Jefa de Departamento
 * Es responsable de notificaciones, filtros y consultas globales
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
     * Marca todas las notificaciones como leídas
     */
    public void marcarTodasComoLeidas() {
        notificaciones.forEach(Notificacion::marcarComoLeida);
    }

    /**
     * Filtra ayudantes según criterios
     */
    public List<Ayudante> filtrarAyudantes(List<Ayudante> ayudantes, Map<String, Object> filtros) {
        return ayudantes.stream()
                .filter(a -> {
                    // Filtro por proyecto
                    if (filtros.containsKey("proyecto")) {
                        String codigoProyecto = (String) filtros.get("proyecto");
                        if (a.getProyectoAsignado() == null || 
                            !a.getProyectoAsignado().getCodigoProyecto().equals(codigoProyecto)) {
                            return false;
                        }
                    }

                    // Filtro por carrera
                    if (filtros.containsKey("carrera")) {
                        String carrera = (String) filtros.get("carrera");
                        if (!a.getCarrera().equals(carrera)) {
                            return false;
                        }
                    }

                    // Filtro por nivel
                    if (filtros.containsKey("nivel")) {
                        Integer nivel = (Integer) filtros.get("nivel");
                        if (a.getNivel() != nivel) {
                            return false;
                        }
                    }

                    // Filtro por estado (Activos/Inactivos)
                    if (filtros.containsKey("estado")) {
                        String estado = (String) filtros.get("estado");
                        if ("Activos".equals(estado) && !a.esActivo()) {
                            return false;
                        }
                        if ("Inactivos".equals(estado) && a.esActivo()) {
                            return false;
                        }
                    }

                    return true;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean esActivo() {
        return "ACTIVO".equals(estado);
    }

    // Getters
    public List<Notificacion> getNotificacionesList() {
        return notificaciones;
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }
}
