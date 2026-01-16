package gestores;

import dominio.Ayudante;
import dominio.ProyectoInvestigacion;

public class GeneradorNotificaciones {
    
    public Notificacion generarNotificacionNuevoAyudante(Ayudante ayudante, ProyectoInvestigacion proyecto) {
        String mensaje = String.format(
            "Nuevo ayudante registrado: %s en el proyecto %s",
            ayudante.getNombresCompletos(),
            proyecto.getNombreProyecto()
        );
        
        Notificacion notif = new Notificacion(mensaje, "NUEVO_AYUDANTE");
        notif.setProyectoRelacionado(proyecto);
        notif.setPrioridad("MEDIA");
        
        return notif;
    }
    
    public Notificacion generarNotificacionAyudanteDadoDeBaja(Ayudante ayudante, ProyectoInvestigacion proyecto) {
        String mensaje = String.format(
            "Ayudante dado de baja: %s del proyecto %s. Motivo: %s",
            ayudante.getNombresCompletos(),
            proyecto.getNombreProyecto(),
            ayudante.getMotivoSalida() != null ? ayudante.getMotivoSalida() : "No especificado"
        );
        
        Notificacion notif = new Notificacion(mensaje, "AYUDANTE_BAJA");
        notif.setProyectoRelacionado(proyecto);
        notif.setPrioridad("ALTA");
        
        return notif;
    }
}
