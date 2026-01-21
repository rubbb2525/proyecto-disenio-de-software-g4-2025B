package controller;

import model.*;
import model.dao.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controlador para operaciones del Director
 */
public class ControladorDirector {
    private Director directorActual;
    private AyudanteDAO ayudanteDAO;
    private EstudianteDAO estudianteDAO;
    private ProyectoDAO proyectoDAO;
    private NotificacionDAO notificacionDAO;
    private JefaDepartamento jefaDepartamento;

    public ControladorDirector(Director director, AyudanteDAO ayudanteDAO, 
                              EstudianteDAO estudianteDAO, ProyectoDAO proyectoDAO) {
        this.directorActual = director;
        this.ayudanteDAO = ayudanteDAO;
        this.estudianteDAO = estudianteDAO;
        this.proyectoDAO = proyectoDAO;
        this.notificacionDAO = new NotificacionDAO();
        this.jefaDepartamento = JefaDepartamento.getInstancia();
    }

    /**
     * Busca un estudiante por criterio (código o cédula)
     */
    public Estudiante buscarEstudiante(String criterio) {
        if (criterio == null || criterio.isEmpty()) {
            return null;
        }
        return estudianteDAO.buscarPorCriterio(criterio);
    }

    /**
     * Registra un nuevo ayudante
     */
    public ResultadoOperacion registrarAyudante(String codigoEstudiante, int horas, double salario) {
        ResultadoOperacion resultado = new ResultadoOperacion();

        // Validar horas
        if (horas > 32) {
            resultado.setMensaje("Las horas semanales no pueden exceder 32");
            resultado.agregarError("Horas semanales inválidas");
            return resultado;
        }

        // Buscar estudiante
        Estudiante estudiante = buscarEstudiante(codigoEstudiante);
        if (estudiante == null) {
            resultado.setMensaje("Estudiante no encontrado");
            resultado.agregarError("Código de estudiante inválido");
            return resultado;
        }

        // Validar elegibilidad
        if (!estudiante.esElegibleParaAyudantia()) {
            resultado.setMensaje("Estudiante no cumple requisitos (IRA mín. 24, Nivel mín. 3)");
            resultado.agregarError("Criterios no cumplidos");
            return resultado;
        }

        // Convertir a ayudante
        Ayudante ayudante = estudiante.convertirAAyudante(
            directorActual.getProyectoAsignado(), 
            horas, 
            salario
        );

        if (ayudante == null) {
            resultado.setMensaje("No se pudo convertir a ayudante");
            resultado.agregarError("Error en la conversión");
            return resultado;
        }

        // Guardar en BD
        if (!ayudanteDAO.guardar(ayudante)) {
            resultado.setMensaje("Error al guardar en BD");
            resultado.agregarError("Error de persistencia");
            return resultado;
        }

        // Notificar a jefa
        Notificacion notif = new Notificacion(
            "NOT_" + System.currentTimeMillis(),
            "Nuevo ayudante registrado: " + ayudante.getNombresCompletos() + 
            " en proyecto " + directorActual.getProyectoAsignado().getNombreProyecto(),
            "REGISTRO_AYUDANTE"
        );
        notif.setAyudanteRelacionado(ayudante);
        notif.setProyectoRelacionado(directorActual.getProyectoAsignado());
        
        // Guardar en BD
        notificacionDAO.guardar(notif);
        
        // Enviar a Jefa en memoria
        jefaDepartamento.recibirNotificacion(notif);

        resultado.setExitoso(true);
        resultado.setMensaje("Ayudante registrado exitosamente");

        return resultado;
    }

    /**
     * Da de baja un ayudante
     */
    public ResultadoOperacion darDeBajaAyudante(String codigoAyudante, String motivo, Date fecha) {
        ResultadoOperacion resultado = new ResultadoOperacion();

        // Buscar ayudante
        Ayudante ayudante = ayudanteDAO.buscarPorId(codigoAyudante);
        if (ayudante == null) {
            resultado.setMensaje("Ayudante no encontrado");
            resultado.agregarError("Código de ayudante inválido");
            return resultado;
        }

        // Dar de baja
        ayudante.darDeBaja(motivo, fecha);

        // Actualizar en BD
        if (!ayudanteDAO.actualizar(ayudante)) {
            resultado.setMensaje("Error al actualizar en BD");
            resultado.agregarError("Error de persistencia");
            return resultado;
        }

        // Notificar a jefa
        Notificacion notif = new Notificacion(
            "NOT_" + System.currentTimeMillis(),
            "Ayudante dado de baja: " + ayudante.getNombresCompletos() + 
            " - Motivo: " + motivo,
            "BAJA_AYUDANTE"
        );
        notif.setAyudanteRelacionado(ayudante);
        
        // Guardar en BD
        notificacionDAO.guardar(notif);
        
        // Enviar a Jefa en memoria
        jefaDepartamento.recibirNotificacion(notif);

        resultado.setExitoso(true);
        resultado.setMensaje("Ayudante dado de baja exitosamente");

        return resultado;
    }

    /**
     * Consulta los ayudantes del proyecto
     */
    public List<Ayudante> consultarAyudantesDelProyecto() {
        if (directorActual.getProyectoAsignado() == null) {
            return null;
        }
        return ayudanteDAO.buscarPorProyecto(directorActual.getProyectoAsignado().getCodigoProyecto());
    }

    /**
     * Obtiene el director actual
     */
    public Director getDirector() {
        return directorActual;
    }

    /**
     * Obtiene el proyecto del director
     */
    public ProyectoInvestigacion getProyecto() {
        return directorActual.getProyectoAsignado();
    }
}
