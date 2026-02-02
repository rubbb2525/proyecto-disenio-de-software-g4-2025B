package model.service;

import model.*;
import java.util.Date;

/**
 * Servicio para convertir estudiantes elegibles a ayudantes
 * 
 * RESPONSABILIDAD ÚNICA: 
 * Transformar un Estudiante en un Ayudante cuando es elegible
 * 
 * Es un SERVICIO de transformación de datos entre entidades
 */
public class ServicioConversionAyudante {
    
    /**
     * Convierte un estudiante elegible a ayudante
     * 
     * @param estudiante El estudiante a convertir
     * @param proyecto El proyecto asignado al ayudante
     * @param horas Horas semanales de trabajo
     * @param salario Salario mensual
     * @return Nuevo objeto Ayudante, o null si no es elegible
     */
    public static Ayudante convertirEstudianteAAyudante(
            Estudiante estudiante,
            ProyectoInvestigacion proyecto,
            int horas,
            double salario) {
        
        // Validación: El estudiante debe ser elegible
        if (estudiante == null || !estudiante.esElegibleParaAyudantia()) {
            return null;
        }
        
        // Crear nuevo Ayudante
        Ayudante ayudante = new Ayudante();
        
        // Copiar datos comunes (de MiembroEPN)
        ayudante.setCodigoUnico(estudiante.getCodigoUnico());
        ayudante.setCedula(estudiante.getCedula());
        ayudante.setCorreoInstitucional(estudiante.getCorreoInstitucional());
        ayudante.setPassword(estudiante.getPassword());
        ayudante.setNombres(estudiante.getNombres());
        ayudante.setApellidos(estudiante.getApellidos());
        ayudante.setTelefono(estudiante.getTelefono());
        ayudante.setRol("AYUDANTE");
        ayudante.setEstado("ACTIVO");
        
        // Copiar datos específicos de estudiante (que ahora es ayudante)
        ayudante.setCarrera(estudiante.getCarrera());
        ayudante.setNivel(estudiante.getNivel());
        ayudante.setIRA(estudiante.getIRA());
        
        // Asignar datos específicos de ayudante
        ayudante.setHorasSemanales(horas);
        ayudante.setSalarioMensual(salario);
        ayudante.setProyectoAsignado(proyecto);
        ayudante.setFechaRegistro(new Date());
        
        return ayudante;
    }
    
    /**
     * Valida que la conversión sea válida
     * Revisa todos los requisitos antes de convertir
     * 
     * @param estudiante Estudiante a validar
     * @param horas Horas propuestas (máximo 32)
     * @param salario Salario propuesto (debe ser positivo)
     * @return true si puede convertirse, false si hay problemas
     */
    public static boolean puedeConvertirse(
            Estudiante estudiante,
            int horas,
            double salario) {
        
        if (estudiante == null) {
            return false;
        }
        
        // Validar elegibilidad académica
        if (!estudiante.esElegibleParaAyudantia()) {
            return false;
        }
        
        // Validar parámetros
        if (horas <= 0 || horas > 32) {
            return false;
        }
        
        if (salario <= 0) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Obtiene mensaje de error si la conversión no es posible
     * 
     * @param estudiante Estudiante a validar
     * @param horas Horas propuestas
     * @param salario Salario propuesto
     * @return Mensaje descriptivo del error, o null si puede convertirse
     */
    public static String obtenerMensajeError(
            Estudiante estudiante,
            int horas,
            double salario) {
        
        if (estudiante == null) {
            return "El estudiante es nulo";
        }
        
        if (!estudiante.esElegibleParaAyudantia()) {
            return String.format(
                "Estudiante no elegible. IRA: %.2f (min: 24.0), Nivel: %d (min: 3)",
                estudiante.getIRA(),
                estudiante.getNivel()
            );
        }
        
        if (horas <= 0 || horas > 32) {
            return String.format(
                "Horas inválidas: %d (deben estar entre 1 y 32)",
                horas
            );
        }
        
        if (salario <= 0) {
            return String.format(
                "Salario inválido: %.2f (debe ser mayor a 0)",
                salario
            );
        }
        
        return null;
    }
}