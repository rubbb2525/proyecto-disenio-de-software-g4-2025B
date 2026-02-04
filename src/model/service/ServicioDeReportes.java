package model.service;

import model.Reporte;
import model.Ayudante;
import model.Proyectos;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para la generación de reportes - VERSIÓN MEJORADA
 * Genera reportes estructurados para PDFs profesionales
 */
public class ServicioDeReportes {

    /**
     * Genera un reporte general del sistema
     */
    public static Reporte generarReporteGeneral(List<Proyectos> proyectos, List<Ayudante> ayudantes) {
        Reporte reporte = new Reporte("RPT-GENERAL-" + System.currentTimeMillis(), 
                                     "GENERAL", 
                                     "REPORTE GENERAL DEL SISTEMA");
        reporte.setFechaGeneracion(new Date());

        // Calcular estadísticas completas
        Map<String, Object> estadisticas = calcularEstadisticasCompletas(ayudantes, proyectos);
        reporte.setEstadisticas(estadisticas);

        // Generar contenido estructurado
        StringBuilder contenido = new StringBuilder();
        
        contenido.append("RESUMEN EJECUTIVO\n\n");
        contenido.append("El sistema actualmente gestiona ");
        contenido.append(ayudantes.size()).append(" ayudantes distribuidos en ");
        contenido.append(proyectos.size()).append(" proyectos de investigación.\n\n");
        
        contenido.append("De los ayudantes registrados, ");
        contenido.append(estadisticas.get("ayudantesActivos"));
        contenido.append(" se encuentran activos, representando el ");
        
        long activos = (Long) estadisticas.get("ayudantesActivos");
        double porcentajeActivos = ayudantes.size() > 0 
            ? (activos * 100.0 / ayudantes.size()) 
            : 0.0;
        contenido.append(String.format("%.1f%%", porcentajeActivos));
        contenido.append(" del total.\n\n");
        
        contenido.append("El índice de rendimiento académico (IRA) promedio de los ayudantes activos es de ");
        contenido.append(String.format("%.2f", estadisticas.get("promedioIRA")));
        contenido.append(", lo que refleja un nivel académico ");
        
        double ira = (Double) estadisticas.get("promedioIRA");
        if (ira >= 85) {
            contenido.append("sobresaliente");
        } else if (ira >= 75) {
            contenido.append("muy bueno");
        } else if (ira >= 65) {
            contenido.append("bueno");
        } else {
            contenido.append("aceptable");
        }
        contenido.append(".\n\n");
        
        // Distribución por carreras
        if (estadisticas.containsKey("distribucionCarreras")) {
            Map<String, Long> distribucion = (Map<String, Long>) estadisticas.get("distribucionCarreras");
            contenido.append("DISTRIBUCIÓN POR CARRERAS:\n");
            distribucion.forEach((carrera, cantidad) -> {
                contenido.append("  - ").append(carrera).append(": ")
                        .append(cantidad).append(" ayudantes\n");
            });
            contenido.append("\n");
        }
        
        contenido.append("PROYECTOS ACTIVOS:\n");
        long proyectosActivos = proyectos.stream()
            .filter(p -> p.getAyudantesActivos() > 0)
            .count();
        contenido.append("Se encuentran activos ").append(proyectosActivos);
        contenido.append(" proyectos de investigación con participación de ayudantes.\n");

        reporte.setContenido(contenido.toString());
        
        // Incluir lista de ayudantes para tabla en PDF
        reporte.setAyudantes(ayudantes);
        
        return reporte;
    }

    /**
     * Genera un reporte completo del proyecto
     */
    public static Reporte generarReportePorProyecto(Proyectos proyecto) {
        String idReporte = "RPT-PROY-" + proyecto.getCodigoProyecto() + "-" + System.currentTimeMillis();
        Reporte reporte = new Reporte(idReporte, "PROYECTO", 
                                     "Informe Detallado: " + proyecto.getNombreProyecto());
        reporte.setFechaGeneracion(new Date());

        List<Ayudante> ayudantesProyecto = proyecto.getAyudantes();
        
        // Estadísticas del proyecto
        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("totalAyudantes", ayudantesProyecto.size());
        estadisticas.put("ayudantesActivos", 
            ayudantesProyecto.stream().filter(Ayudante::esActivo).count());
        estadisticas.put("cuposDisponibles", proyecto.getCuposDisponibles());
        estadisticas.put("capacidadTotal", proyecto.getAyudantesPlanificados());
        
        if (!ayudantesProyecto.isEmpty()) {
            double iraPromedio = ayudantesProyecto.stream()
                .filter(Ayudante::esActivo)
                .mapToDouble(Ayudante::getIRA)
                .average()
                .orElse(0.0);
            estadisticas.put("promedioIRA", iraPromedio);
            
            double horasPromedio = ayudantesProyecto.stream()
                .filter(Ayudante::esActivo)
                .mapToDouble(Ayudante::getHorasSemanales)
                .average()
                .orElse(0.0);
            estadisticas.put("promedioHoras", horasPromedio);
            
            // Distribución por nivel
            Map<Integer, Long> distribucionNivel = ayudantesProyecto.stream()
                .filter(Ayudante::esActivo)
                .collect(Collectors.groupingBy(Ayudante::getNivel, Collectors.counting()));
            estadisticas.put("distribucionNivel", distribucionNivel);
        }
        
        reporte.setEstadisticas(estadisticas);

        // Contenido del reporte
        StringBuilder contenido = new StringBuilder();
        
        contenido.append("INFORMACIÓN DEL PROYECTO\n\n");
        contenido.append("Código: ").append(proyecto.getCodigoProyecto()).append("\n");
        contenido.append("Nombre: ").append(proyecto.getNombreProyecto()).append("\n");
        
        if (proyecto.getTipoProyecto() != null) {
            contenido.append("Tipo: ").append(proyecto.getTipoProyecto().getDescripcion()).append("\n");
            contenido.append("Categoría: ").append(proyecto.getTipoProyecto().getCategoria().getDescripcion()).append("\n");
        }
        
        if (proyecto.getDirector() != null) {
            contenido.append("Director: ").append(proyecto.getDirector().getNombresCompletos()).append("\n");
        }
        
        contenido.append("\nESTADO DEL PROYECTO\n\n");
        contenido.append("El proyecto cuenta con ");
        contenido.append(estadisticas.get("ayudantesActivos"));
        contenido.append(" ayudantes activos de un total de ");
        contenido.append(estadisticas.get("capacidadTotal"));
        contenido.append(" cupos planificados.\n");
        
        int cuposDisponibles = proyecto.getCuposDisponibles();
        if (cuposDisponibles > 0) {
            contenido.append("Actualmente hay ").append(cuposDisponibles);
            contenido.append(" cupos disponibles para nuevas contrataciones.\n");
        } else if (cuposDisponibles == 0) {
            contenido.append("El proyecto se encuentra al máximo de su capacidad.\n");
        } else {
            contenido.append("ATENCIÓN: El proyecto excede su capacidad planificada.\n");
        }
        
        if (estadisticas.containsKey("promedioIRA")) {
            contenido.append("\nEl rendimiento académico promedio del equipo es de ");
            contenido.append(String.format("%.2f", estadisticas.get("promedioIRA")));
            contenido.append(".\n");
        }
        
        if (estadisticas.containsKey("distribucionNivel")) {
            contenido.append("\nDISTRIBUCIÓN POR NIVEL ACADÉMICO:\n");
            Map<Integer, Long> distribucion = (Map<Integer, Long>) estadisticas.get("distribucionNivel");
            distribucion.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    contenido.append("  Nivel ").append(entry.getKey())
                            .append(": ").append(entry.getValue())
                            .append(" ayudante").append(entry.getValue() > 1 ? "s" : "")
                            .append("\n");
                });
        }

        reporte.setContenido(contenido.toString());
        return reporte;
    }

    /**
     * Genera un reporte por carrera
     */
    public static Reporte generarReportePorCarrera(String carrera, List<Ayudante> ayudantes) {
        String idReporte = "RPT-CARR-" + carrera.replaceAll("\\s+", "") + "-" + System.currentTimeMillis();
        Reporte reporte = new Reporte(idReporte, "CARRERA", 
                                     "Análisis por Carrera: " + carrera);
        reporte.setFechaGeneracion(new Date());

        List<Ayudante> ayudantesCarrera = ayudantes.stream()
                .filter(a -> carrera.equalsIgnoreCase(a.getCarrera()))
                .collect(Collectors.toList());

        // Estadísticas
        Map<String, Object> estadisticas = calcularEstadisticas(ayudantesCarrera);
        
        // Distribución por nivel
        Map<Integer, Long> distribucionNivel = ayudantesCarrera.stream()
            .filter(Ayudante::esActivo)
            .collect(Collectors.groupingBy(Ayudante::getNivel, Collectors.counting()));
        estadisticas.put("distribucionNivel", distribucionNivel);
        
        reporte.setEstadisticas(estadisticas);

        // Contenido
        StringBuilder contenido = new StringBuilder();
        contenido.append("ANÁLISIS DE CARRERA\n\n");
        contenido.append("Carrera analizada: ").append(carrera).append("\n\n");
        
        contenido.append("Se identificaron ").append(ayudantesCarrera.size());
        contenido.append(" ayudantes pertenecientes a esta carrera, de los cuales ");
        contenido.append(estadisticas.get("activos"));
        contenido.append(" se encuentran activos actualmente.\n\n");
        
        if (ayudantesCarrera.size() > 0) {
            contenido.append("INDICADORES DE RENDIMIENTO:\n");
            contenido.append("  IRA Promedio: ").append(String.format("%.2f", estadisticas.get("promedioIRA"))).append("\n");
            contenido.append("  Horas semanales promedio: ").append(String.format("%.1f", estadisticas.get("promedioHoras"))).append("\n\n");
            
            if (!distribucionNivel.isEmpty()) {
                contenido.append("DISTRIBUCIÓN POR NIVEL:\n");
                distribucionNivel.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        double porcentaje = (entry.getValue() * 100.0) / ayudantesCarrera.size();
                        contenido.append("  Nivel ").append(entry.getKey())
                                .append(": ").append(entry.getValue())
                                .append(" (").append(String.format("%.1f%%", porcentaje)).append(")\n");
                    });
            }
        }

        reporte.setContenido(contenido.toString());
        return reporte;
    }

    /**
     * Genera un reporte por nivel
     */
    public static Reporte generarReportePorNivel(int nivel, List<Ayudante> ayudantes) {
        String idReporte = "RPT-NIV-" + nivel + "-" + System.currentTimeMillis();
        Reporte reporte = new Reporte(idReporte, "NIVEL", 
                                     "Análisis por Nivel Académico: " + nivel);
        reporte.setFechaGeneracion(new Date());

        List<Ayudante> ayudantesNivel = ayudantes.stream()
                .filter(a -> a.getNivel() == nivel)
                .collect(Collectors.toList());

        // Estadísticas
        Map<String, Object> estadisticas = calcularEstadisticas(ayudantesNivel);
        
        // Distribución por carrera
        Map<String, Long> distribucionCarrera = ayudantesNivel.stream()
            .filter(Ayudante::esActivo)
            .collect(Collectors.groupingBy(Ayudante::getCarrera, Collectors.counting()));
        estadisticas.put("distribucionCarreras", distribucionCarrera);
        
        reporte.setEstadisticas(estadisticas);

        // Contenido
        StringBuilder contenido = new StringBuilder();
        contenido.append("ANÁLISIS POR NIVEL ACADÉMICO\n\n");
        contenido.append("Nivel analizado: ").append(nivel).append("\n\n");
        
        contenido.append("Se registran ").append(ayudantesNivel.size());
        contenido.append(" ayudantes en este nivel, de los cuales ");
        contenido.append(estadisticas.get("activos"));
        contenido.append(" están activos.\n\n");
        
        if (ayudantesNivel.size() > 0) {
            contenido.append("RENDIMIENTO ACADÉMICO:\n");
            contenido.append("  IRA Promedio: ").append(String.format("%.2f", estadisticas.get("promedioIRA"))).append("\n");
            contenido.append("  Dedicación horaria promedio: ").append(String.format("%.1f", estadisticas.get("promedioHoras"))).append(" horas/semana\n\n");
            
            if (!distribucionCarrera.isEmpty()) {
                contenido.append("DISTRIBUCIÓN POR CARRERA:\n");
                distribucionCarrera.entrySet().stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .forEach(entry -> {
                        double porcentaje = (entry.getValue() * 100.0) / ayudantesNivel.size();
                        contenido.append("  ").append(entry.getKey())
                                .append(": ").append(entry.getValue())
                                .append(" (").append(String.format("%.1f%%", porcentaje)).append(")\n");
                    });
            }
        }

        reporte.setContenido(contenido.toString());
        return reporte;
    }

    /**
     * Calcula estadísticas básicas de una lista de ayudantes
     */
    private static Map<String, Object> calcularEstadisticas(List<Ayudante> ayudantesList) {
        Map<String, Object> stats = new HashMap<>();

        if (ayudantesList.isEmpty()) {
            stats.put("total", 0);
            stats.put("activos", 0L);
            stats.put("promedioIRA", 0.0);
            stats.put("promedioHoras", 0.0);
            return stats;
        }

        long activos = ayudantesList.stream().filter(Ayudante::esActivo).count();
        
        double promedioIRA = ayudantesList.stream()
            .filter(Ayudante::esActivo)
            .mapToDouble(Ayudante::getIRA)
            .average()
            .orElse(0.0);
        
        double promedioHoras = ayudantesList.stream()
            .filter(Ayudante::esActivo)
            .mapToDouble(Ayudante::getHorasSemanales)
            .average()
            .orElse(0.0);

        stats.put("total", ayudantesList.size());
        stats.put("activos", activos);
        stats.put("ayudantesActivos", activos); // Alias
        stats.put("promedioIRA", promedioIRA);
        stats.put("promedioHoras", promedioHoras);

        return stats;
    }

    /**
     * Calcula estadísticas completas incluyendo proyectos
     */
    private static Map<String, Object> calcularEstadisticasCompletas(List<Ayudante> ayudantes, 
                                                                      List<Proyectos> proyectos) {
        Map<String, Object> stats = calcularEstadisticas(ayudantes);
        
        // Estadísticas de proyectos
        stats.put("totalProyectos", proyectos.size());
        
        long proyectosActivos = proyectos.stream()
            .filter(p -> p.getAyudantesActivos() > 0)
            .count();
        stats.put("proyectosActivos", proyectosActivos);
        
        // Distribución por carreras
        Map<String, Long> distribucionCarreras = ayudantes.stream()
            .filter(Ayudante::esActivo)
            .collect(Collectors.groupingBy(Ayudante::getCarrera, Collectors.counting()));
        stats.put("distribucionCarreras", distribucionCarreras);
        stats.put("totalCarreras", distribucionCarreras.size());
        
        // Distribución por nivel
        Map<Integer, Long> distribucionNivel = ayudantes.stream()
            .filter(Ayudante::esActivo)
            .collect(Collectors.groupingBy(Ayudante::getNivel, Collectors.counting()));
        stats.put("distribucionNivel", distribucionNivel);
        
        return stats;
    }
}