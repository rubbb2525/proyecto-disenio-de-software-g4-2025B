package controladores;

import dominio.*;
import gestores.*;
import sistema.SistemaGestionAyudantes;
import java.io.File;
import java.util.*;

public class ControladorJefaDepartamento {
    private SistemaGestionAyudantes sistema;
    private JefaDepartamento jefa;
    
    public ControladorJefaDepartamento() {
        this.sistema = SistemaGestionAyudantes.getInstancia();
    }
    
    public void inicializar() {
        this.jefa = JefaDepartamento.getInstancia();
    }
    
    // Métodos de consulta de proyectos
    
    public List<ProyectoInvestigacion> consultarProyectos() {
        return sistema.consultarProyectos();
    }
    
    // Métodos de consulta de ayudantes
    
    public List<Ayudante> consultarAyudantes() {
        return sistema.consultarAyudantes();
    }
    
    public List<Ayudante> consultarAyudantesPorProyecto(ProyectoInvestigacion proyecto) {
        return sistema.consultarAyudantesPorProyecto(proyecto);
    }
    
    public List<Ayudante> filtrarAyudantesPorCarrera(String carrera) {
        return sistema.consultarAyudantesPorCarrera(carrera);
    }
    
    public List<Ayudante> filtrarAyudantesPorNivel(int nivel) {
        return sistema.consultarAyudantesPorNivel(nivel);
    }
    
    public List<Ayudante> filtrarAyudantesActivos() {
        return sistema.consultarAyudantesActivos();
    }
    
    public List<Ayudante> buscarAyudantes(String criterio) {
        return sistema.buscarAyudantes(criterio);
    }
    
    // Métodos de mensajería
    
    public boolean enviarMensajeADirector(ProyectoInvestigacion proyecto, String mensaje) {
        if (proyecto == null || proyecto.getDirector() == null || jefa == null) {
            return false;
        }
        
        Mensaje mensajeObj = new Mensaje(jefa, proyecto.getDirector(), mensaje);
        mensajeObj.setAsunto("Consulta sobre proyecto: " + proyecto.getNombreProyecto());
        mensajeObj.setProyectoRelacionado(proyecto);
        
        return sistema.enviarMensaje(mensajeObj);
    }
    
    // Métodos de notificaciones
    
    public List<Notificacion> consultarNotificaciones() {
        if (jefa != null) {
            return jefa.getNotificaciones();
        }
        return new ArrayList<>();
    }
    
    public List<Notificacion> consultarNotificacionesNoLeidas() {
        if (jefa != null) {
            return jefa.getNotificacionesNoLeidas();
        }
        return new ArrayList<>();
    }
    
    public void marcarNotificacionesComoLeidas() {
        if (jefa != null) {
            jefa.marcarTodasComoLeidas();
        }
    }
    
    // Métodos de reportes
    
    public Reporte generarReporte(String tipo) {
        switch (tipo.toUpperCase()) {
            case "GENERAL":
                return sistema.generarReporte("GENERAL");
            default:
                return null;
        }
    }
    
    public Reporte generarReportePorProyecto(ProyectoInvestigacion proyecto) {
        return sistema.generarReportePorProyecto(proyecto);
    }
    
    public Reporte generarReportePorCarrera(String carrera) {
        return sistema.generarReportePorCarrera(carrera);
    }
    
    public Reporte generarReportePorNivel(int nivel) {
        return sistema.generarReportePorNivel(nivel);
    }
    
    public File exportarReportePDF(Reporte reporte) {
        if (reporte == null) {
            return null;
        }
        
        try {
            return reporte.exportarPDF();
        } catch (Exception e) {
            System.err.println("Error al exportar reporte: " + e.getMessage());
            return null;
        }
    }
    
    public File exportarReporteExcel(Reporte reporte) {
        if (reporte == null) {
            return null;
        }
        
        try {
            return reporte.exportarExcel();
        } catch (Exception e) {
            System.err.println("Error al exportar reporte: " + e.getMessage());
            return null;
        }
    }
    
    // Getter
    
    public JefaDepartamento getJefa() {
        return jefa;
    }
    
    public void setVistaJefa(vistas.VistaJefaDepartamento vista) {
        // Guardar referencia a la vista si es necesaria
    }
}
