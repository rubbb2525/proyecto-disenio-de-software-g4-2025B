package dominio;

import gestores.Notificacion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JefaDepartamento extends MiembroFIS implements Serializable {
    private static final long serialVersionUID = 1L;
    private static JefaDepartamento instancia;
    
    private String especialidad;
    private Date fechaInicioGestion;
    private List<Notificacion> notificaciones;

    // Constructor privado (Singleton)
    private JefaDepartamento() {
        super();
        setRol("JEFE_DEPARTAMENTO");
        this.notificaciones = new ArrayList<>();
        this.fechaInicioGestion = new Date();
    }

    // Método Singleton
    public static JefaDepartamento getInstancia() {
        if (instancia == null) {
            instancia = new JefaDepartamento();
        }
        return instancia;
    }

    // Métodos de negocio
    public void recibirNotificacion(Notificacion notificacion) {
        if (notificacion != null) {
            notificaciones.add(notificacion);
        }
    }

    public List<Notificacion> getNotificacionesNoLeidas() {
        return notificaciones.stream()
                .filter(n -> !n.isLeida())
                .collect(Collectors.toList());
    }

    public void marcarTodasComoLeidas() {
        for (Notificacion notif : notificaciones) {
            notif.marcarComoLeida();
        }
    }

    public List<ProyectoInvestigacion> consultarTodosProyectos() {
        // Este método será implementado por el sistema
        return new ArrayList<>();
    }

    public List<Ayudante> consultarTodosAyudantes() {
        // Este método será implementado por el sistema
        return new ArrayList<>();
    }

    public List<Ayudante> consultarAyudantesPorProyecto(ProyectoInvestigacion proyecto) {
        if (proyecto != null) {
            return proyecto.getAyudantes();
        }
        return new ArrayList<>();
    }

    public List<Ayudante> consultarAyudantesPorCarrera(String carrera) {
        // Este método será implementado por el sistema con DAO
        return new ArrayList<>();
    }

    public List<Ayudante> consultarAyudantesActivos() {
        // Este método será implementado por el sistema con DAO
        return new ArrayList<>();
    }

    public void enviarMensajeADirector(ProyectoInvestigacion proyecto, String mensaje) {
        // Este método será implementado por el sistema
    }

    public gestores.Reporte generarReporte(String tipo) {
        // Este método será implementado por el sistema
        return null;
    }

    @Override
    public String toString() {
        return String.format("Jefe de Departamento: %s", getNombresCompletos());
    }

    // Getters y Setters
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Date getFechaInicioGestion() {
        return fechaInicioGestion;
    }

    public void setFechaInicioGestion(Date fechaInicioGestion) {
        this.fechaInicioGestion = fechaInicioGestion;
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }
}
