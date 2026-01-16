package gestores;

import dominio.ProyectoInvestigacion;
import java.io.Serializable;
import java.util.Date;

public class Notificacion implements Serializable {
    private static final long serialVersionUID = 1L;
    private String idNotificacion;
    private Date fecha;
    private String mensaje;
    private String tipo;
    private boolean leida;
    private String prioridad;
    private ProyectoInvestigacion proyectoRelacionado;

    // Constructores
    public Notificacion() {
        this.fecha = new Date();
        this.leida = false;
        this.prioridad = "NORMAL";
    }

    public Notificacion(String mensaje, String tipo) {
        this();
        this.mensaje = mensaje;
        this.tipo = tipo;
    }

    // Métodos de negocio
    public void marcarComoLeida() {
        this.leida = true;
    }

    public void marcarComoNoLeida() {
        this.leida = false;
    }

    public String getContenido() {
        return String.format("[%s] %s: %s", prioridad, tipo, mensaje);
    }

    public boolean esImportante() {
        return "ALTA".equals(prioridad) || "URGENTE".equals(prioridad);
    }

    public boolean esReciente() {
        long diffInMillies = Math.abs(new Date().getTime() - fecha.getTime());
        long diffInHours = diffInMillies / (60 * 60 * 1000);
        return diffInHours < 24; // Menos de 24 horas
    }

    public String getResumen() {
        return String.format("%s - %s", tipo, mensaje.substring(0, Math.min(50, mensaje.length())));
    }

    @Override
    public String toString() {
        return getContenido();
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

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public ProyectoInvestigacion getProyectoRelacionado() {
        return proyectoRelacionado;
    }

    public void setProyectoRelacionado(ProyectoInvestigacion proyectoRelacionado) {
        this.proyectoRelacionado = proyectoRelacionado;
    }
}
