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
 * Servicio para la generación de reportes
 * 
 * RESPONSABILIDAD ÚNICA: 
 * Generar reportes del sistema con diferentes criterios
 * 
 * Patrón: Service (Utilidad estática con métodos de negocio)
 * 
 * CAMBIOS:
 * - Movidos métodos de JefaDepartamento.java para separar responsabilidades
 * - Utiliza ServicioDeEstadisticas para cálculos de datos
 */
public class ServicioDeReportes {

    /**
     * Genera un reporte general del sistema
     */
    public static Reporte generarReporteGeneral(List<Proyectos> proyectos, List<Ayudante> ayudantes) {
        Reporte reporte = new Reporte("REPORTE_001", "GENERAL", "Reporte General del Sistema");
        reporte.setFechaGeneracion(new Date());

        StringBuilder contenido = new StringBuilder();
        contenido.append("REPORTE GENERAL DE SISTEMA DE GESTIÓN DE AYUDANTES\n");
        contenido.append("=".repeat(60)).append("\n\n");

        Map<String, Object> estadisticas = calcularEstadisticas(ayudantes);
        reporte.setEstadisticas(estadisticas);

        contenido.append("ESTADÍSTICAS GENERALES:\n");
        contenido.append("Total de Ayudantes: ").append(ayudantes.size()).append("\n");
        contenido.append("Ayudantes Activos: ").append(estadisticas.get("activos")).append("\n");
        contenido.append("Promedio IRA: ").append(String.format("%.2f", estadisticas.get("promedioIRA"))).append("\n");
        contenido.append("Total Proyectos: ").append(proyectos.size()).append("\n\n");

        contenido.append("DETALLE DE AYUDANTES:\n");
        contenido.append("-".repeat(60)).append("\n");
        contenido.append(String.format("%-12s %-30s %-8s %-6s %-8s\n",
            "CÓDIGO", "NOMBRES", "CARRERA", "NIVEL", "IRA"));
        contenido.append("-".repeat(60)).append("\n");

        for (Ayudante a : ayudantes) {
            if (a.esActivo()) {
                String carreraCorta = a.getCarrera().length() > 8 ?
                    a.getCarrera().substring(0, 8) : a.getCarrera();
                contenido.append(String.format("%-12s %-30s %-8s %-6d %8.2f\n",
                    a.getCodigoUnico(),
                    a.getNombresCompletos().length() > 30 ?
                        a.getNombresCompletos().substring(0, 27) + "..." :
                        a.getNombresCompletos(),
                    carreraCorta,
                    a.getNivel(),
                    a.getIRA()));
            }
        }
        contenido.append("-".repeat(60)).append("\n");

        reporte.setContenido(contenido.toString());
        return reporte;
    }

    /**
     * Genera un reporte completo del proyecto
     */
    @SuppressWarnings("deprecation")
    public static Reporte generarReportePorProyecto(Proyectos proyecto) {
        String idReporte = "REPORTE_PROYECTO_" + proyecto.getCodigoProyecto();
        Reporte reporte = new Reporte(idReporte, "PROYECTO", "Reporte Proyecto: " + proyecto.getNombreProyecto());
        reporte.setFechaGeneracion(new Date());

        StringBuilder contenido = new StringBuilder();
        contenido.append("REPORTE DE PROYECTO\n");
        contenido.append("=".repeat(60)).append("\n\n");
        contenido.append("Código: ").append(proyecto.getCodigoProyecto()).append("\n");
        contenido.append("Nombre: ").append(proyecto.getNombreProyecto()).append("\n");
        contenido.append("Tipo: ").append(proyecto.getTipoProyecto().getDescripcion()).append("\n");
        contenido.append("Director: ").append(proyecto.getDirector().getNombresCompletos()).append("\n");
        contenido.append("Ayudantes Activos: ").append(proyecto.getAyudantesActivos()).append("\n");
        contenido.append("Cupos Disponibles: ").append(proyecto.getCuposDisponibles()).append("\n\n");

        List<Ayudante> ayudantesProyecto = proyecto.getAyudantes();
        Map<String, Object> estadisticas = calcularEstadisticas(ayudantesProyecto);
        reporte.setEstadisticas(estadisticas);

        if (!ayudantesProyecto.isEmpty()) {
            contenido.append("AYUDANTES DEL PROYECTO:\n");
            contenido.append("-".repeat(60)).append("\n");
            contenido.append(String.format("%-12s %-25s %-8s %-8s\n",
                "CÓDIGO", "NOMBRES", "NIVEL", "HORAS"));
            contenido.append("-".repeat(60)).append("\n");

            for (Ayudante a : ayudantesProyecto) {
                if (a.esActivo()) {
                    contenido.append(String.format("%-12s %-25s %-8d %-8d\n",
                        a.getCodigoUnico(),
                        a.getNombresCompletos().length() > 25 ?
                            a.getNombresCompletos().substring(0, 22) + "..." :
                            a.getNombresCompletos(),
                        a.getNivel(),
                        a.getHorasSemanales()));
                }
            }
            contenido.append("-".repeat(60)).append("\n");
        }

        reporte.setContenido(contenido.toString());
        return reporte;
    }

    /**
     * Genera un reporte por carrera
     */
    public static Reporte generarReportePorCarrera(String carrera, List<Ayudante> ayudantes) {
        Reporte reporte = new Reporte("REPORTE_CARRERA_" + carrera, "CARRERA", "Reporte Carrera: " + carrera);
        reporte.setFechaGeneracion(new Date());

        List<Ayudante> ayudantesCarrera = ayudantes.stream()
                .filter(a -> carrera.equals(a.getCarrera()))
                .toList();

        StringBuilder contenido = new StringBuilder();
        contenido.append("REPORTE POR CARRERA\n");
        contenido.append("=".repeat(60)).append("\n\n");
        contenido.append("Carrera: ").append(carrera).append("\n");
        contenido.append("Total Ayudantes: ").append(ayudantesCarrera.size()).append("\n");

        Map<String, Object> estadisticas = calcularEstadisticas(ayudantesCarrera);
        reporte.setEstadisticas(estadisticas);

        contenido.append("Promedio IRA: ").append(String.format("%.2f", estadisticas.get("promedioIRA"))).append("\n");

        reporte.setContenido(contenido.toString());
        return reporte;
    }

    /**
     * Genera un reporte por nivel
     */
    public static Reporte generarReportePorNivel(int nivel, List<Ayudante> ayudantes) {
        Reporte reporte = new Reporte("REPORTE_NIVEL_" + nivel, "NIVEL", "Reporte Nivel: " + nivel);
        reporte.setFechaGeneracion(new Date());

        List<Ayudante> ayudantesNivel = ayudantes.stream()
                .filter(a -> a.getNivel() == nivel)
                .toList();

        StringBuilder contenido = new StringBuilder();
        contenido.append("REPORTE POR NIVEL\n");
        contenido.append("=".repeat(60)).append("\n\n");
        contenido.append("Nivel: ").append(nivel).append("\n");
        contenido.append("Total Ayudantes: ").append(ayudantesNivel.size()).append("\n");

        Map<String, Object> estadisticas = calcularEstadisticas(ayudantesNivel);
        reporte.setEstadisticas(estadisticas);

        reporte.setContenido(contenido.toString());
        return reporte;
    }

    /**
     * Calcula estadísticas de una lista de ayudantes
     */
    private static Map<String, Object> calcularEstadisticas(List<Ayudante> ayudantesList) {
        Map<String, Object> stats = new HashMap<>();

        if (ayudantesList.isEmpty()) {
            stats.put("total", 0);
            stats.put("activos", 0);
            stats.put("promedioIRA", 0.0);
            stats.put("promedioHoras", 0.0);
            return stats;
        }

        long activos = ayudantesList.stream().filter(Ayudante::esActivo).count();
        double promedioIRA = ayudantesList.stream().mapToDouble(Ayudante::getIRA).average().orElse(0.0);
        double promedioHoras = ayudantesList.stream().mapToDouble(Ayudante::getHorasSemanales).average().orElse(0.0);

        stats.put("total", ayudantesList.size());
        stats.put("activos", activos);
        stats.put("promedioIRA", promedioIRA);
        stats.put("promedioHoras", promedioHoras);

        return stats;
    }
}
