package model;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Representa un ayudante de investigación
 */
public class Ayudante extends MiembroEPN {
    private String carrera;
    private int nivel;
    private float ira;
    private Date fechaRegistro;
    private Date fechaFinalizacion;
    private String motivoSalida;
    private Proyectos proyectoAsignado;
    private int horasSemanales;
    private int mesesContratados;

    public Ayudante() {
    }

    public Ayudante(String codigoUnico, String cedula, String correoInstitucional,
                   String nombres, String apellidos, String telefono,
                   String carrera, int nivel, float ira, int horasSemanales, int mesesContratados) {
        super(codigoUnico, cedula, correoInstitucional, "N/A", nombres, apellidos, telefono, "AYUDANTE", "ACTIVO");
        this.carrera = carrera;
        this.nivel = nivel;
        this.ira = ira;
        this.horasSemanales = horasSemanales;
        this.mesesContratados = mesesContratados;
        this.fechaRegistro = new Date();
    }

    /**
     * Verifica si el ayudante está activo
     */
    @Override
    public boolean esActivo() {
        return "ACTIVO".equals(estado) && fechaFinalizacion == null;
    }

    /**
     * Da de baja el ayudante con validación
     */
    public ResultadoOperacion darDeBaja(String motivo, Date fecha) {
        ResultadoOperacion resultado = new ResultadoOperacion();

        // Validar que no esté ya dado de baja
        if (!esActivo()) {
            resultado.setMensaje("El ayudante ya está inactivo");
            resultado.agregarError("Estado inválido");
            return resultado;
        }

        // Validar motivo
        if (motivo == null || motivo.trim().isEmpty()) {
            resultado.setMensaje("El motivo de baja es obligatorio");
            resultado.agregarError("Motivo vacío");
            return resultado;
        }

        // Validar fecha
        if (fecha == null) {
            resultado.setMensaje("La fecha de baja es obligatoria");
            resultado.agregarError("Fecha nula");
            return resultado;
        }

        if (fecha.before(fechaRegistro)) {
            resultado.setMensaje("La fecha de baja no puede ser anterior a la fecha de registro");
            resultado.agregarError("Fecha inválida");
            return resultado;
        }

        // Aplicar baja
        this.estado = "INACTIVO";
        this.motivoSalida = motivo;
        this.fechaFinalizacion = fecha;

        resultado.setExitoso(true);
        resultado.setMensaje("Ayudante dado de baja exitosamente");
        return resultado;
    }

    /**
     * Calcula el costo total estimado basado en meses contratados
     */
    public double calcularCostoTotal() {
        // Estimación: horas_semanales * semanas_por_mes * meses_contratados * valor_hora
        return horasSemanales * 4.33 * mesesContratados * 5.0; // 5.0 es valor estimado por hora
    }

    // Getters y Setters
    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public float getIRA() {
        return ira;
    }

    public void setIRA(float ira) {
        this.ira = ira;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public String getMotivoSalida() {
        return motivoSalida;
    }

    public void setMotivoSalida(String motivoSalida) {
        this.motivoSalida = motivoSalida;
    }

    public Proyectos getProyectoAsignado() {
        return proyectoAsignado;
    }

    public void setProyectoAsignado(Proyectos proyectoAsignado) {
        this.proyectoAsignado = proyectoAsignado;
    }

    public int getHorasSemanales() {
        return horasSemanales;
    }

    public void setHorasSemanales(int horasSemanales) {
        if (horasSemanales > 32) {
            throw new IllegalArgumentException("Las horas semanales no pueden exceder 32");
        }
        this.horasSemanales = horasSemanales;
    }

    public int getMesesContratados() {
        return mesesContratados;
    }

    public void setMesesContratados(int mesesContratados) {
        this.mesesContratados = mesesContratados;
    }

    // ============ MÉTODOS DE FILTRADO ESTÁTICOS ============

    /**
     * Filtra ayudantes según criterios
     */
    public static List<Ayudante> filtrar(List<Ayudante> ayudantes, Map<String, Object> filtros) {
        if (ayudantes == null || ayudantes.isEmpty()) {
            return List.of();
        }
        
        if (filtros == null || filtros.isEmpty()) {
            return ayudantes;
        }
        
        return ayudantes.stream()
                .filter(a -> cumpleTodosCriterios(a, filtros))
                .collect(java.util.stream.Collectors.toList());
    }

    private static boolean cumpleTodosCriterios(Ayudante ayudante, Map<String, Object> filtros) {
        // Filtro por proyecto
        if (filtros.containsKey("proyecto")) {
            String codigoProyecto = (String) filtros.get("proyecto");
            if (ayudante.getProyectoAsignado() == null ||
                !ayudante.getProyectoAsignado().getCodigoProyecto().equals(codigoProyecto)) {
                return false;
            }
        }
        
        // Filtro por carrera
        if (filtros.containsKey("carrera")) {
            String carrera = (String) filtros.get("carrera");
            if (!carrera.equals(ayudante.getCarrera())) {
                return false;
            }
        }
        
        // Filtro por nivel
        if (filtros.containsKey("nivel")) {
            Integer nivel = (Integer) filtros.get("nivel");
            if (ayudante.getNivel() != nivel) {
                return false;
            }
        }
        
        // Filtro por estado
        if (filtros.containsKey("estado")) {
            String estado = (String) filtros.get("estado");
            if ("Activos".equals(estado) && !ayudante.esActivo()) return false;
            if ("Inactivos".equals(estado) && ayudante.esActivo()) return false;
        }
        
        // Filtro por IRA
        if (filtros.containsKey("ira_minimo")) {
            Float iraMinimo = (Float) filtros.get("ira_minimo");
            if (ayudante.getIRA() < iraMinimo) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Filtra ayudantes activos
     */
    public static List<Ayudante> obtenerActivos(List<Ayudante> ayudantes) {
        if (ayudantes == null) return List.of();
        return ayudantes.stream()
                .filter(Ayudante::esActivo)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Filtra ayudantes inactivos
     */
    public static List<Ayudante> obtenerInactivos(List<Ayudante> ayudantes) {
        if (ayudantes == null) return List.of();
        return ayudantes.stream()
                .filter(a -> !a.esActivo())
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Filtra por carrera
     */
    public static List<Ayudante> porCarrera(List<Ayudante> ayudantes, String carrera) {
        if (ayudantes == null || carrera == null) return List.of();
        return ayudantes.stream()
                .filter(a -> carrera.equals(a.getCarrera()))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Filtra por nivel
     */
    public static List<Ayudante> porNivel(List<Ayudante> ayudantes, int nivel) {
        if (ayudantes == null) return List.of();
        return ayudantes.stream()
                .filter(a -> a.getNivel() == nivel)
                .collect(java.util.stream.Collectors.toList());
    }
}
