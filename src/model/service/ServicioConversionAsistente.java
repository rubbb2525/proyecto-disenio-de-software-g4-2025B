package model.service;

import model.*;

/**
 * Servicio para convertir un Estudiante en Asistente de Investigación
 */
public class ServicioConversionAsistente {

    public static boolean puedeConvertirse(
            Estudiante estudiante,
            int horas,
            double salario,
            String tituloAcademico,
            String areaEspecializacion
    ) {

        if (estudiante == null) return false;
        if (horas <= 0 || horas > 32) return false;
        if (salario <= 0) return false;
        if (tituloAcademico == null || tituloAcademico.isBlank()) return false;
        if (areaEspecializacion == null || areaEspecializacion.isBlank()) return false;

        // Ejemplo de regla académica (ajústala si quieres)
        return estudiante.getNivel() >= 7;
    }

    public static String obtenerMensajeError(
            Estudiante estudiante,
            int horas,
            double salario,
            String tituloAcademico,
            String areaEspecializacion
    ) {
        if (estudiante == null) return "Estudiante inválido";
        if (horas <= 0 || horas > 32) return "Horas inválidas (máx. 32)";
        if (salario <= 0) return "Salario inválido";
        if (tituloAcademico == null || tituloAcademico.isBlank())
            return "Título académico obligatorio";
        if (areaEspecializacion == null || areaEspecializacion.isBlank())
            return "Área de especialización obligatoria";
        if (estudiante.getNivel() < 7)
            return "Nivel académico insuficiente para asistente";

        return "Conversión no válida";
    }

    public static AsistenteInvestigacion convertirEstudianteAAsistente(
            Estudiante estudiante,
            ProyectoInvestigacion proyecto,
            int horas,
            double salario,
            String tituloAcademico,
            String areaEspecializacion
    ) {

        AsistenteInvestigacion asistente = new AsistenteInvestigacion();

        asistente.setCodigoUnico(estudiante.getCodigoUnico());
        asistente.setCedula(estudiante.getCedula());
        asistente.setCorreoInstitucional(estudiante.getCorreoInstitucional());
        asistente.setNombres(estudiante.getNombres());
        asistente.setApellidos(estudiante.getApellidos());
        asistente.setTelefono(estudiante.getTelefono());
        asistente.setCarrera(estudiante.getCarrera());
        asistente.setNivel(estudiante.getNivel());
        asistente.setIRA(estudiante.getIRA());

        asistente.setTituloAcademico(tituloAcademico);
        asistente.setAreaEspecializacion(areaEspecializacion);

        asistente.setHorasSemanales(horas);
        asistente.setSalarioMensual(salario);
        asistente.setEstado("ACTIVO");
        asistente.setFechaRegistro(new java.util.Date());
        asistente.setProyectoAsignado(proyecto);

        return asistente;
    }
}
