package dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Director extends MiembroFIS implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String especialidad;
    private ProyectoInvestigacion proyectoAsignado;

    // Constructores
    public Director() {
        super();
        setRol("DIRECTOR");
    }

    public Director(String numeroUnico, String nombres, String apellidos) {
        this();
        setNumeroUnico(numeroUnico);
        setNombres(nombres);
        setApellidos(apellidos);
    }

    // Getters y Setters
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public ProyectoInvestigacion getProyectoAsignado() {
        return proyectoAsignado;
    }

    public void setProyectoAsignado(ProyectoInvestigacion proyectoAsignado) {
        this.proyectoAsignado = proyectoAsignado;
        if (proyectoAsignado != null) {
            proyectoAsignado.setDirector(this);
        }
    }

    public boolean tieneProyectoAsignado() {
        return proyectoAsignado != null;
    }

    // Métodos de negocio
    public void registrarAyudante(Ayudante ayudante) {
        if (proyectoAsignado != null && ayudante != null) {
            proyectoAsignado.agregarAyudante(ayudante);
            ayudante.setProyectoAsignado(proyectoAsignado);
            ayudante.setEstado("ACTIVO");
            ayudante.setFechaRegistro(new Date());
        }
    }

    public void darDeBajaAyudante(Ayudante ayudante, String motivo, Date fecha) {
        if (ayudante != null) {
            ayudante.darDeBaja(motivo, fecha);
            if (proyectoAsignado != null) {
                proyectoAsignado.removerAyudante(ayudante);
            }
        }
    }

    public List<Ayudante> consultarAyudantesDelProyecto() {
        if (proyectoAsignado != null) {
            return proyectoAsignado.getAyudantes();
        }
        return new ArrayList<>();
    }

    public List<Mensaje> verMensajesRecibidos() {
        // Este método será implementado con el DAO de mensajes
        return new ArrayList<>();
    }

    public void responderMensaje(Mensaje mensaje, String respuesta) {
        if (mensaje != null && respuesta != null) {
            mensaje.responder(respuesta);
        }
    }

    @Override
    public String toString() {
        return String.format("Director: %s - %s", getNombresCompletos(), 
                           proyectoAsignado != null ? proyectoAsignado.getNombreProyecto() : "Sin proyecto");
    }
}
