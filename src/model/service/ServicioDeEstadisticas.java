package model.service;

import model.Ayudante;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio para calcular estadísticas de ayudantes
 * 
 * RESPONSABILIDAD ÚNICA:
 * Realizar cálculos estadísticos sobre listas de ayudantes
 * 
 * Este servicio es REUTILIZABLE en múltiples contextos:
 * - JefaDepartamento lo usa
 * - Vistas pueden usarlo para mostrar dashboards
 * - APIs pueden usarlo para endpoints
 */
public class ServicioDeEstadisticas {
    
    /**
     * Calcula todas las estadísticas para un grupo de ayudantes
     * 
     * @param ayudantes Lista de ayudantes
     * @return Mapa con todas las estadísticas calculadas
     */
    public static Map<String, Object> calcularTodas(List<Ayudante> ayudantes) {
        Map<String, Object> stats = new HashMap<>();
        
        if (ayudantes == null || ayudantes.isEmpty()) {
            stats.put("total", 0);
            stats.put("activos", 0);
            stats.put("inactivos", 0);
            stats.put("promedioIRA", 0.0);
            stats.put("promedioHoras", 0.0);
            stats.put("mesesPromedio", 0.0);
            stats.put("costoTotal", 0.0);
            return stats;
        }
        
        // Contar total
        int total = ayudantes.size();
        stats.put("total", total);
        
        // Contar activos
        long activos = ayudantes.stream().filter(Ayudante::esActivo).count();
        stats.put("activos", activos);
        
        // Contar inactivos
        long inactivos = total - activos;
        stats.put("inactivos", inactivos);
        
        // Calcular promedio IRA
        double promedioIRA = calcularPromedioIRA(ayudantes);
        stats.put("promedioIRA", promedioIRA);
        
        // Calcular promedio horas
        double promedioHoras = calcularPromedioHoras(ayudantes);
        stats.put("promedioHoras", promedioHoras);
        
        // Calcular promedio meses
        double mesesPromedio = calcularMesesPromedio(ayudantes);
        stats.put("mesesPromedio", mesesPromedio);
        
        // Calcular costo total
        double costoTotal = calcularCostoTotal(ayudantes);
        stats.put("costoTotal", costoTotal);
        
        // Porcentaje de activos
        double porcentajeActivos = (activos / (double) total) * 100;
        stats.put("porcentajeActivos", porcentajeActivos);
        
        return stats;
    }
    
    /**
     * Calcula el promedio de IRA
     * 
     * @param ayudantes Lista de ayudantes
     * @return Promedio de IRA
     */
    public static double calcularPromedioIRA(List<Ayudante> ayudantes) {
        if (ayudantes == null || ayudantes.isEmpty()) {
            return 0.0;
        }
        
        return ayudantes.stream()
                .mapToDouble(Ayudante::getIRA)
                .average()
                .orElse(0.0);
    }
    
    /**
     * Calcula el promedio de horas semanales
     * 
     * @param ayudantes Lista de ayudantes
     * @return Promedio de horas
     */
    public static double calcularPromedioHoras(List<Ayudante> ayudantes) {
        if (ayudantes == null || ayudantes.isEmpty()) {
            return 0.0;
        }
        
        return ayudantes.stream()
                .mapToDouble(Ayudante::getHorasSemanales)
                .average()
                .orElse(0.0);
    }
    
    /**
     * Calcula el promedio de meses contratados
     * 
     * @param ayudantes Lista de ayudantes
     * @return Promedio de meses
     */
    public static double calcularMesesPromedio(List<Ayudante> ayudantes) {
        if (ayudantes == null || ayudantes.isEmpty()) {
            return 0.0;
        }
        
        return ayudantes.stream()
                .mapToInt(Ayudante::getMesesContratados)
                .average()
                .orElse(0.0);
    }
    
    /**
     * Calcula el costo total de todos los ayudantes
     * 
     * @param ayudantes Lista de ayudantes
     * @return Suma total de costos
     */
    public static double calcularCostoTotal(List<Ayudante> ayudantes) {
        if (ayudantes == null || ayudantes.isEmpty()) {
            return 0.0;
        }
        
        return ayudantes.stream()
                .mapToDouble(Ayudante::calcularCostoTotal)
                .sum();
    }
    
    /**
     * Cuenta el total de ayudantes activos
     * 
     * @param ayudantes Lista de ayudantes
     * @return Cantidad de ayudantes activos
     */
    public static long contarActivos(List<Ayudante> ayudantes) {
        if (ayudantes == null) {
            return 0;
        }
        
        return ayudantes.stream()
                .filter(Ayudante::esActivo)
                .count();
    }
    
    /**
     * Cuenta el total de ayudantes inactivos
     * 
     * @param ayudantes Lista de ayudantes
     * @return Cantidad de ayudantes inactivos
     */
    public static long contarInactivos(List<Ayudante> ayudantes) {
        if (ayudantes == null) {
            return 0;
        }
        
        return ayudantes.stream()
                .filter(a -> !a.esActivo())
                .count();
    }
    
    /**
     * Obtiene el IRA mínimo
     * 
     * @param ayudantes Lista de ayudantes
     * @return IRA mínimo encontrado
     */
    public static float obtenerIRAMinimo(List<Ayudante> ayudantes) {
        if (ayudantes == null || ayudantes.isEmpty()) {
            return 0.0f;
        }
        
        return (float) ayudantes.stream()
                .mapToDouble(Ayudante::getIRA)
                .min()
                .orElse(0.0);
    }
    
    /**
     * Obtiene el IRA máximo
     * 
     * @param ayudantes Lista de ayudantes
     * @return IRA máximo encontrado
     */
    public static float obtenerIRAMaximo(List<Ayudante> ayudantes) {
        if (ayudantes == null || ayudantes.isEmpty()) {
            return 0.0f;
        }
        
        return (float) ayudantes.stream()
                .mapToDouble(Ayudante::getIRA)
                .max()
                .orElse(0.0);
    }
    
    /**
     * Obtiene el nivel promedio
     * 
     * @param ayudantes Lista de ayudantes
     * @return Nivel promedio
     */
    public static double calcularNivelPromedio(List<Ayudante> ayudantes) {
        if (ayudantes == null || ayudantes.isEmpty()) {
            return 0.0;
        }
        
        return ayudantes.stream()
                .mapToDouble(Ayudante::getNivel)
                .average()
                .orElse(0.0);
    }
    
    /**
     * Calcula estadísticas por carrera
     * 
     * @param ayudantes Lista de ayudantes
     * @return Mapa con carrera -> estadísticas
     */
    public static Map<String, Map<String, Object>> estadisticasPorCarrera(
            List<Ayudante> ayudantes) {
        
        Map<String, Map<String, Object>> resultado = new HashMap<>();
        
        if (ayudantes == null || ayudantes.isEmpty()) {
            return resultado;
        }
        
        // Agrupar por carrera
        Map<String, List<Ayudante>> porCarrera = ayudantes.stream()
                .collect(java.util.stream.Collectors.groupingBy(Ayudante::getCarrera));
        
        // Calcular estadísticas para cada carrera
        for (Map.Entry<String, List<Ayudante>> entry : porCarrera.entrySet()) {
            String carrera = entry.getKey();
            List<Ayudante> ayudantesCarrera = entry.getValue();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", ayudantesCarrera.size());
            stats.put("activos", contarActivos(ayudantesCarrera));
            stats.put("promedioIRA", calcularPromedioIRA(ayudantesCarrera));
            stats.put("promedioHoras", calcularPromedioHoras(ayudantesCarrera));
            
            resultado.put(carrera, stats);
        }
        
        return resultado;
    }
    
    /**
     * Calcula estadísticas por nivel
     * 
     * @param ayudantes Lista de ayudantes
     * @return Mapa con nivel -> estadísticas
     */
    public static Map<Integer, Map<String, Object>> estadisticasPorNivel(
            List<Ayudante> ayudantes) {
        
        Map<Integer, Map<String, Object>> resultado = new HashMap<>();
        
        if (ayudantes == null || ayudantes.isEmpty()) {
            return resultado;
        }
        
        // Agrupar por nivel
        Map<Integer, List<Ayudante>> porNivel = ayudantes.stream()
                .collect(java.util.stream.Collectors.groupingBy(Ayudante::getNivel));
        
        // Calcular estadísticas para cada nivel
        for (Map.Entry<Integer, List<Ayudante>> entry : porNivel.entrySet()) {
            Integer nivel = entry.getKey();
            List<Ayudante> ayudantesNivel = entry.getValue();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", ayudantesNivel.size());
            stats.put("activos", contarActivos(ayudantesNivel));
            stats.put("promedioIRA", calcularPromedioIRA(ayudantesNivel));
            stats.put("promedioHoras", calcularPromedioHoras(ayudantesNivel));
            
            resultado.put(nivel, stats);
        }
        
        return resultado;
    }
}