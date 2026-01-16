package dominio;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Mensaje implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String idMensaje;
    private Date fecha;
    private String asunto;
    private String contenido;
    private MiembroFIS remitente;
    private MiembroFIS destinatario;
    private ProyectoInvestigacion proyectoRelacionado;
    private boolean leido;
    private String respuesta;
    private Date fechaRespuesta;

    // Constructores
    public Mensaje() {
        this.idMensaje = UUID.randomUUID().toString();
        this.fecha = new Date();
        this.leido = false;
    }

    public Mensaje(MiembroFIS remitente, MiembroFIS destinatario, String contenido) {
        this();
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.contenido = contenido;
    }

    // Getters y Setters
    public String getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(String idMensaje) {
        this.idMensaje = idMensaje;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public MiembroFIS getRemitente() {
        return remitente;
    }

    public void setRemitente(MiembroFIS remitente) {
        this.remitente = remitente;
    }

    public MiembroFIS getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(MiembroFIS destinatario) {
        this.destinatario = destinatario;
    }

    public ProyectoInvestigacion getProyectoRelacionado() {
        return proyectoRelacionado;
    }

    public void setProyectoRelacionado(ProyectoInvestigacion proyectoRelacionado) {
        this.proyectoRelacionado = proyectoRelacionado;
    }

    public boolean isLeido() {
        return leido;
    }

    public void marcarComoLeido() {
        this.leido = true;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
        this.fechaRespuesta = new Date();
    }

    public Date getFechaRespuesta() {
        return fechaRespuesta;
    }

    public boolean tieneRespuesta() {
        return respuesta != null && !respuesta.isEmpty();
    }

    public void responder(String respuesta) {
        this.respuesta = respuesta;
        this.fechaRespuesta = new Date();
    }
}
