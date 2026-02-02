package model.service;

import model.Ayudante;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para filtrar ayudantes según criterios
 * 
 * RESPONSABILIDAD ÚNICA:
 * Aplicar filtros a listas de ayudantes
 * 
 */
public class ServicioDeFiltrado {
    
    /**
     * Filtra una lista de ayudantes según criterios específicos
     * 
     * Criterios soportados:
     * - "proyecto": Código del proyecto (String)
     * - "carrera": Nombre de carrera (String)
     * - "nivel": Nivel académico (Integer)
     * - "estado": "Activos" o "Inactivos" (String)
     * - "ira_minimo": IRA mínimo (Float)
     * 
     * @param ayudantes Lista original de ayudantes
     * @param filtros Mapa con criterios de filtrado
     * @return Lista filtrada de ayudantes
     */
    public static List<Ayudante> filtrarAyudantes(
            List<Ayudante> ayudantes,
            Map<String, Object> filtros) {
        
        if (ayudantes == null || ayudantes.isEmpty()) {
            return List.of();
        }
        
        if (filtros == null || filtros.isEmpty()) {
            return ayudantes;
        }
        
        return ayudantes.stream()
                .filter(a -> cumpleTodosCriterios(a, filtros))
                .collect(Collectors.toList());
    }
    
    /**
     * Verifica si un ayudante cumple TODOS los criterios
     * (AND lógico)
     * 
     * @param ayudante Ayudante a evaluar
     * @param filtros Criterios de filtrado
     * @return true si cumple todos, false si falla alguno
     */
    private static boolean cumpleTodosCriterios(
            Ayudante ayudante,
            Map<String, Object> filtros) {
        
        // Filtro por proyecto
        if (filtros.containsKey("proyecto")) {
            if (!cumpleFiltroProyecto(ayudante, filtros)) {
                return false;
            }
        }
        
        // Filtro por carrera
        if (filtros.containsKey("carrera")) {
            if (!cumpleFiltroPorCarrera(ayudante, filtros)) {
                return false;
            }
        }
        
        // Filtro por nivel
        if (filtros.containsKey("nivel")) {
            if (!cumpleFiltroPorNivel(ayudante, filtros)) {
                return false;
            }
        }
        
        // Filtro por estado (Activo/Inactivo)
        if (filtros.containsKey("estado")) {
            if (!cumpleFiltroPorEstado(ayudante, filtros)) {
                return false;
            }
        }
        
        // Filtro por IRA mínimo
        if (filtros.containsKey("ira_minimo")) {
            if (!cumpleFiltroPorIRA(ayudante, filtros)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Verifica el filtro por proyecto
     */
    private static boolean cumpleFiltroProyecto(
            Ayudante ayudante,
            Map<String, Object> filtros) {
        
        String codigoProyecto = (String) filtros.get("proyecto");
        
        // Si el ayudante no tiene proyecto asignado
        if (ayudante.getProyectoAsignado() == null) {
            return false;
        }
        
        // Comparar códigos de proyecto
        return ayudante.getProyectoAsignado()
                .getCodigoProyecto()
                .equals(codigoProyecto);
    }
    
    /**
     * Verifica el filtro por carrera
     */
    private static boolean cumpleFiltroPorCarrera(
            Ayudante ayudante,
            Map<String, Object> filtros) {
        
        String carrera = (String) filtros.get("carrera");
        return carrera != null && 
               carrera.equals(ayudante.getCarrera());
    }
    
    /**
     * Verifica el filtro por nivel
     */
    private static boolean cumpleFiltroPorNivel(
            Ayudante ayudante,
            Map<String, Object> filtros) {
        
        Object nivelObj = filtros.get("nivel");
        
        if (nivelObj == null) {
            return true;
        }
        
        Integer nivel = (Integer) nivelObj;
        return ayudante.getNivel() == nivel;
    }
    
    /**
     * Verifica el filtro por estado
     */
    private static boolean cumpleFiltroPorEstado(
            Ayudante ayudante,
            Map<String, Object> filtros) {
        
        String estado = (String) filtros.get("estado");
        
        if (estado == null) {
            return true;
        }
        
        switch (estado) {
            case "Activos":
                return ayudante.esActivo();
            case "Inactivos":
                return !ayudante.esActivo();
            default:
                return true;
        }
    }
    
    /**
     * Verifica el filtro por IRA mínimo
     */
    private static boolean cumpleFiltroPorIRA(
            Ayudante ayudante,
            Map<String, Object> filtros) {
        
        Object iraObj = filtros.get("ira_minimo");
        
        if (iraObj == null) {
            return true;
        }
        
        Float iraMinimo = (Float) iraObj;
        return ayudante.getIRA() >= iraMinimo;
    }
    
    /**
     * Filtra ayudantes activos
     * 
     * @param ayudantes Lista de ayudantes
     * @return Solo los ayudantes activos
     */
    public static List<Ayudante> obtenerActivos(List<Ayudante> ayudantes) {
        if (ayudantes == null) {
            return List.of();
        }
        
        return ayudantes.stream()
                .filter(Ayudante::esActivo)
                .collect(Collectors.toList());
    }
    
    /**
     * Filtra ayudantes inactivos
     * 
     * @param ayudantes Lista de ayudantes
     * @return Solo los ayudantes inactivos
     */
    public static List<Ayudante> obtenerInactivos(List<Ayudante> ayudantes) {
        if (ayudantes == null) {
            return List.of();
        }
        
        return ayudantes.stream()
                .filter(a -> !a.esActivo())
                .collect(Collectors.toList());
    }
    
    /**
     * Filtra ayudantes por carrera
     * 
     * @param ayudantes Lista de ayudantes
     * @param carrera Carrera a filtrar
     * @return Ayudantes de esa carrera
     */
    public static List<Ayudante> porCarrera(
            List<Ayudante> ayudantes,
            String carrera) {
        
        if (ayudantes == null || carrera == null) {
            return List.of();
        }
        
        return ayudantes.stream()
                .filter(a -> carrera.equals(a.getCarrera()))
                .collect(Collectors.toList());
    }
    
    /**
     * Filtra ayudantes por nivel
     * 
     * @param ayudantes Lista de ayudantes
     * @param nivel Nivel a filtrar
     * @return Ayudantes de ese nivel
     */
    public static List<Ayudante> porNivel(
            List<Ayudante> ayudantes,
            int nivel) {
        
        if (ayudantes == null) {
            return List.of();
        }
        
        return ayudantes.stream()
                .filter(a -> a.getNivel() == nivel)
                .collect(Collectors.toList());
    }
}