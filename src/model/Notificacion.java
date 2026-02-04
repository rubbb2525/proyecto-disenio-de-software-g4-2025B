package model;

import java.util.Date;

/**
 * Representa una notificación del sistema
 */
public class Notificacion {
    private String idNotificacion;
    private Date fecha;
    private String mensaje;
    private String tipo;
    private boolean leida;
    private Proyectos proyectoRelacionado;
    private Ayudante ayudanteRelacionado;

    public Notificacion() {
        this.leida = false;
        this.fecha = new Date();
    }

    public Notificacion(String idNotificacion, String mensaje, String tipo) {
        this.idNotificacion = idNotificacion;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.leida = false;
        this.fecha = new Date();
    }

    public void marcarComoLeida() {
        this.leida = true;
    }

    public void marcarComoNoLeida() {
        this.leida = false;
    }

    // Getters y Setters
    public String getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(String idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }

    public Proyectos getProyectoRelacionado() {
        return proyectoRelacionado;
    }

    public void setProyectoRelacionado(Proyectos proyectoRelacionado) {
        this.proyectoRelacionado = proyectoRelacionado;
    }

    public Ayudante getAyudanteRelacionado() {
        return ayudanteRelacionado;
    }

    public void setAyudanteRelacionado(Ayudante ayudanteRelacionado) {
        this.ayudanteRelacionado = ayudanteRelacionado;
    }
}
