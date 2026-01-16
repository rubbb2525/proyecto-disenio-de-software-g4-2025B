package controladores;

import dominio.*;
import sistema.SistemaGestionAyudantes;
import java.util.*;

public class ControladorDirector {
    private SistemaGestionAyudantes sistema;
    private Director directorActual;
    
    public ControladorDirector() {
        this.sistema = SistemaGestionAyudantes.getInstancia();
    }
    
    public void inicializar(Director director) {
        this.directorActual = director;
    }
    
    // Métodos de Ayudantes
    
    public Ayudante registrarAyudante(Map<String, Object> datos) {
        if (directorActual == null || directorActual.getProyectoAsignado() == null) {
            System.err.println("Director no tiene proyecto asignado");
            return null;
        }
        
        try {
            Ayudante ayudante = new Ayudante();
            ayudante.setNumeroUnico((String) datos.get("numeroUnico"));
            ayudante.setNombres((String) datos.get("nombres"));
            ayudante.setApellidos((String) datos.get("apellidos"));
            ayudante.setCedula((String) datos.get("cedula"));
            ayudante.setCorreoInstitucional((String) datos.get("correo"));
            ayudante.setCarrera((String) datos.get("carrera"));
            ayudante.setNivel((int) datos.get("nivel"));
            ayudante.setPromedio((float) datos.get("promedio"));
            ayudante.setCodigoEstudiante((String) datos.get("codigo"));
            ayudante.setSemestre((int) datos.get("semestre"));
            ayudante.setHorasSemanales((int) datos.get("horasSemanales"));
            ayudante.setSalarioPorHora((double) datos.get("salarioPorHora"));
            
            if (sistema.registrarAyudante(ayudante, directorActual.getProyectoAsignado())) {
                return ayudante;
            }
        } catch (Exception e) {
            System.err.println("Error al registrar ayudante: " + e.getMessage());
        }
        
        return null;
    }
    
    public boolean actualizarAyudante(Ayudante ayudante, Map<String, Object> datos) {
        if (ayudante == null || datos == null) {
            return false;
        }
        
        try {
            if (datos.containsKey("promedio")) {
                ayudante.setPromedio((float) datos.get("promedio"));
            }
            if (datos.containsKey("nivel")) {
                ayudante.setNivel((int) datos.get("nivel"));
            }
            if (datos.containsKey("horasSemanales")) {
                ayudante.setHorasSemanales((int) datos.get("horasSemanales"));
            }
            if (datos.containsKey("salarioPorHora")) {
                ayudante.setSalarioPorHora((double) datos.get("salarioPorHora"));
            }
            
            return sistema.actualizarAyudante(ayudante);
        } catch (Exception e) {
            System.err.println("Error al actualizar ayudante: " + e.getMessage());
            return false;
        }
    }
    
    public boolean darDeBajaAyudante(Ayudante ayudante, String motivo, Date fecha) {
        if (directorActual == null) {
            return false;
        }
        
        directorActual.darDeBajaAyudante(ayudante, motivo, fecha);
        return sistema.darDeBajaAyudante(ayudante, motivo, fecha);
    }
    
    // Métodos de consulta
    
    public ProyectoInvestigacion consultarMiProyecto() {
        if (directorActual != null) {
            return directorActual.getProyectoAsignado();
        }
        return null;
    }
    
    public List<Ayudante> consultarAyudantesDeProyecto() {
        if (directorActual == null || directorActual.getProyectoAsignado() == null) {
            return new ArrayList<>();
        }
        
        return sistema.consultarAyudantesPorProyecto(directorActual.getProyectoAsignado());
    }
    
    public List<Ayudante> consultarAyudantesActivos() {
        List<Ayudante> todosLosAyudantes = consultarAyudantesDeProyecto();
        return todosLosAyudantes.stream()
                .filter(Ayudante::esActivo)
                .collect(java.util.stream.Collectors.toList());
    }
    
    // Métodos de mensajería
    
    public List<Mensaje> verMensajesRecibidos() {
        if (directorActual == null) {
            return new ArrayList<>();
        }
        
        return sistema.consultarMensajesPorDestinatario(directorActual);
    }
    
    public boolean responderMensaje(Mensaje mensaje, String respuesta) {
        if (directorActual == null || mensaje == null) {
            return false;
        }
        
        directorActual.responderMensaje(mensaje, respuesta);
        return sistema.responderMensaje(mensaje, respuesta);
    }
    
    // Getters
    
    public Director getDirectorActual() {
        return directorActual;
    }
    
    public void setVistaDirector(vistas.VistaDirector vista) {
        // Guardar referencia a la vista si es necesaria
    }
}
