package gestores;

import dominio.Ayudante;
import dominio.ProyectoInvestigacion;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneradorReportes {
    private SimpleDateFormat formatoFecha;
    private Map<String, String> plantillasReporte;
    private List<String> formatosDisponibles;

    // Constructor
    public GeneradorReportes() {
        this.formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        this.plantillasReporte = new HashMap<>();
        this.formatosDisponibles = new ArrayList<>();
        inicializarPlantillas();
        inicializarFormatos();
    }

    private void inicializarPlantillas() {
        plantillasReporte.put("GENERAL", "Reporte General del Departamento");
        plantillasReporte.put("PROYECTO", "Reporte de Proyecto Individual");
        plantillasReporte.put("CARRERA", "Reporte por Carrera");
        plantillasReporte.put("NIVEL", "Reporte por Nivel Académico");
        plantillasReporte.put("PRESUPUESTARIO", "Reporte Presupuestario");
    }

    private void inicializarFormatos() {
        formatosDisponibles.add("PDF");
        formatosDisponibles.add("EXCEL");
        formatosDisponibles.add("HTML");
        formatosDisponibles.add("CSV");
    }

    // Métodos de generación de reportes
    public Reporte generarReporteGeneral(List<ProyectoInvestigacion> proyectos, List<Ayudante> ayudantes) {
        Reporte reporte = new Reporte("GENERAL", "Reporte General del Departamento");
        
        // Calcular estadísticas
        Map<String, Object> estadisticas = calcularEstadisticas(proyectos, ayudantes);
        
        // Agregar secciones
        reporte.agregarSeccion("Resumen Ejecutivo", generarResumenEjecutivo(estadisticas));
        reporte.agregarSeccion("Proyectos Activos", construirTablaProyectos(proyectos));
        reporte.agregarSeccion("Ayudantes Activos", construirTablaAyudantes(ayudantes));
        
        // Agregar datos
        estadisticas.forEach(reporte::agregarDato);
        
        // Conclusiones
        reporte.agregarConclusion("Total de proyectos activos: " + proyectos.size());
        reporte.agregarConclusion("Total de ayudantes activos: " + ayudantes.size());
        
        return reporte;
    }

    public Reporte generarReportePorProyecto(ProyectoInvestigacion proyecto) {
        Reporte reporte = new Reporte("PROYECTO", "Reporte del Proyecto: " + proyecto.getNombreProyecto());
        
        // Información del proyecto
        StringBuilder infoProyecto = new StringBuilder();
        infoProyecto.append("Código: ").append(proyecto.getCodigoProyecto()).append("\n");
        infoProyecto.append("Tipo: ").append(proyecto.getTipoProyecto()).append("\n");
        infoProyecto.append("Director: ").append(proyecto.getDirector().getNombresCompletos()).append("\n");
        infoProyecto.append("Presupuesto: $").append(proyecto.getPresupuesto()).append("\n");
        infoProyecto.append("Ayudantes: ").append(proyecto.getAyudantesActivos()).append("/")
                    .append(proyecto.getAyudantesPlanificados()).append("\n");
        
        reporte.agregarSeccion("Información del Proyecto", infoProyecto.toString());
        reporte.agregarSeccion("Ayudantes Asignados", construirTablaAyudantes(proyecto.getAyudantes()));
        
        // Datos
        reporte.agregarDato("presupuestoTotal", proyecto.getPresupuesto());
        reporte.agregarDato("presupuestoUtilizado", proyecto.calcularPresupuestoUtilizado());
        reporte.agregarDato("ayudantesActivos", proyecto.getAyudantesActivos());
        reporte.agregarDato("porcentajeCompletado", proyecto.getPorcentajeCompletado());
        
        return reporte;
    }

    public Reporte generarReportePorCarrera(String carrera, List<Ayudante> ayudantes) {
        List<Ayudante> ayudantesCarrera = new ArrayList<>();
        for (Ayudante a : ayudantes) {
            if (carrera.equals(a.getCarrera())) {
                ayudantesCarrera.add(a);
            }
        }
        
        Reporte reporte = new Reporte("CARRERA", "Reporte de Ayudantes - Carrera: " + carrera);
        
        reporte.agregarSeccion("Ayudantes de " + carrera, construirTablaAyudantes(ayudantesCarrera));
        reporte.agregarDato("totalAyudantes", ayudantesCarrera.size());
        reporte.agregarDato("promedioGeneral", calcularPromedioGeneral(ayudantesCarrera));
        
        return reporte;
    }

    public Reporte generarReportePorNivel(int nivel, List<Ayudante> ayudantes) {
        List<Ayudante> ayudantesNivel = new ArrayList<>();
        for (Ayudante a : ayudantes) {
            if (a.getNivel() == nivel) {
                ayudantesNivel.add(a);
            }
        }
        
        Reporte reporte = new Reporte("NIVEL", "Reporte de Ayudantes - Nivel: " + nivel);
        
        reporte.agregarSeccion("Ayudantes de Nivel " + nivel, construirTablaAyudantes(ayudantesNivel));
        reporte.agregarDato("totalAyudantes", ayudantesNivel.size());
        
        return reporte;
    }

    public Reporte generarReportePresupuestario(List<ProyectoInvestigacion> proyectos) {
        Reporte reporte = new Reporte("PRESUPUESTARIO", "Reporte Presupuestario");
        
        double presupuestoTotal = 0;
        double presupuestoUtilizado = 0;
        
        StringBuilder detalle = new StringBuilder();
        detalle.append(String.format("%-30s %-15s %-15s %-15s\n", 
            "Proyecto", "Presupuesto", "Utilizado", "Disponible"));
        detalle.append("-".repeat(75)).append("\n");
        
        for (ProyectoInvestigacion p : proyectos) {
            presupuestoTotal += p.getPresupuesto();
            presupuestoUtilizado += p.calcularPresupuestoUtilizado();
            
            detalle.append(String.format("%-30s $%-14.2f $%-14.2f $%-14.2f\n",
                p.getNombreProyecto().substring(0, Math.min(30, p.getNombreProyecto().length())),
                p.getPresupuesto(),
                p.calcularPresupuestoUtilizado(),
                p.calcularPresupuestoDisponible()
            ));
        }
        
        reporte.agregarSeccion("Detalle Presupuestario", detalle.toString());
        reporte.agregarDato("presupuestoTotal", presupuestoTotal);
        reporte.agregarDato("presupuestoUtilizado", presupuestoUtilizado);
        reporte.agregarDato("presupuestoDisponible", presupuestoTotal - presupuestoUtilizado);
        
        return reporte;
    }

    public Reporte generarReportePersonalizado(String tipo, Map<String, Object> datos) {
        String titulo = datos.getOrDefault("titulo", "Reporte Personalizado").toString();
        Reporte reporte = new Reporte(tipo, titulo);
        
        datos.forEach(reporte::agregarDato);
        
        return reporte;
    }

    public java.io.File exportarReporte(Reporte reporte, String formato) {
        switch (formato.toUpperCase()) {
            case "PDF":
                return reporte.exportarPDF();
            case "EXCEL":
                return reporte.exportarExcel();
            case "HTML":
                return reporte.exportarHTML();
            default:
                System.out.println("Formato no soportado: " + formato);
                return null;
        }
    }

    // Métodos auxiliares privados
    private String construirTablaProyectos(List<ProyectoInvestigacion> proyectos) {
        StringBuilder tabla = new StringBuilder();
        tabla.append(String.format("%-15s %-30s %-20s %-10s %-10s\n", 
            "Código", "Nombre", "Tipo", "Estado", "Ayudantes"));
        tabla.append("-".repeat(90)).append("\n");
        
        for (ProyectoInvestigacion p : proyectos) {
            tabla.append(String.format("%-15s %-30s %-20s %-10s %-10s\n",
                p.getCodigoProyecto(),
                p.getNombreProyecto().substring(0, Math.min(30, p.getNombreProyecto().length())),
                p.getTipoProyecto(),
                p.getEstado(),
                p.getAyudantesActivos() + "/" + p.getAyudantesPlanificados()
            ));
        }
        
        return tabla.toString();
    }

    private String construirTablaAyudantes(List<Ayudante> ayudantes) {
        StringBuilder tabla = new StringBuilder();
        tabla.append(String.format("%-30s %-25s %-10s %-10s %-10s\n", 
            "Nombre", "Carrera", "Nivel", "Promedio", "Estado"));
        tabla.append("-".repeat(90)).append("\n");
        
        for (Ayudante a : ayudantes) {
            tabla.append(String.format("%-30s %-25s %-10d %-10.2f %-10s\n",
                a.getNombresCompletos().substring(0, Math.min(30, a.getNombresCompletos().length())),
                a.getCarrera().substring(0, Math.min(25, a.getCarrera().length())),
                a.getNivel(),
                a.getPromedio(),
                a.getEstado()
            ));
        }
        
        return tabla.toString();
    }

    private Map<String, Object> calcularEstadisticas(List<ProyectoInvestigacion> proyectos, List<Ayudante> ayudantes) {
        Map<String, Object> estadisticas = new HashMap<>();
        
        estadisticas.put("totalProyectos", proyectos.size());
        estadisticas.put("totalAyudantes", ayudantes.size());
        estadisticas.put("ayudantesActivos", ayudantes.stream().filter(Ayudante::esActivo).count());
        estadisticas.put("promedioGeneral", calcularPromedioGeneral(ayudantes));
        
        return estadisticas;
    }

    private double calcularPromedioGeneral(List<Ayudante> ayudantes) {
        if (ayudantes.isEmpty()) return 0;
        return ayudantes.stream()
                .mapToDouble(Ayudante::getPromedio)
                .average()
                .orElse(0.0);
    }

    private String generarResumenEjecutivo(Map<String, Object> estadisticas) {
        StringBuilder resumen = new StringBuilder();
        resumen.append("Este reporte presenta un panorama general del estado actual del departamento.\n\n");
        resumen.append("Estadísticas principales:\n");
        estadisticas.forEach((k, v) -> resumen.append("  - ").append(k).append(": ").append(v).append("\n"));
        return resumen.toString();
    }

    // Getters
    public List<String> getFormatosDisponibles() {
        return formatosDisponibles;
    }
}
