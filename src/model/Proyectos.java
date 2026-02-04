package model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un proyecto de investigación
 */
public class Proyectos {
    private String codigoProyecto;
    private String nombreProyecto;
    private String descripcion;
    private Date fechaInicio;
    private Date fechaFin;
    private String estado;
    private TipoProyecto tipoProyecto;
    private int ayudantesPlanificados;
    private int tecnicosPlanificados;
    private int asistentesPlanificados;
    private Director director;

    public Proyectos() {
    }

    public Proyectos(String codigoProyecto, String nombreProyecto, String descripcion,
                                Date fechaInicio, Date fechaFin, String estado,
                                TipoProyecto tipoProyecto, int ayudantesPlanificados) {
        this.codigoProyecto = codigoProyecto;
        this.nombreProyecto = nombreProyecto;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.tipoProyecto = tipoProyecto;
        this.ayudantesPlanificados = ayudantesPlanificados;
        this.tecnicosPlanificados = 0;
        this.asistentesPlanificados = 0;
    }

    public Proyectos(String codigoProyecto, String nombreProyecto, String descripcion,
                                Date fechaInicio, Date fechaFin, String estado,
                                TipoProyecto tipoProyecto, int ayudantesPlanificados,
                                int tecnicosPlanificados, int asistentesPlanificados) {
        this.codigoProyecto = codigoProyecto;
        this.nombreProyecto = nombreProyecto;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.tipoProyecto = tipoProyecto;
        this.ayudantesPlanificados = ayudantesPlanificados;
        this.tecnicosPlanificados = tecnicosPlanificados;
        this.asistentesPlanificados = asistentesPlanificados;
    }

    /**
     * Retorna el número de ayudantes activos
     */
    public int getAyudantesActivos() {
        // Este método debe ser usado con AyudanteDAO para obtener datos reales de BD
        // Placeholder para compatibilidad
        return 0;
    }

    /**
     * @deprecated Usar tieneEstadoActivo() en su lugar. Este método usa lógica redundante.
     * Verifica si el proyecto está activo considerando solo fechas sin validar estado.
     */
    @Deprecated
    public boolean estaActivo() {
        Date hoy = new Date();
        return "ACTIVO".equals(estado) && 
               hoy.after(fechaInicio) && 
               hoy.before(fechaFin);
    }

    /**
     * Verifica si el proyecto está en estado ACTIVO (sin considerar fechas)
     */
    public boolean tieneEstadoActivo() {
        return "ACTIVO".equals(estado);
    }

    /**
     * Filtra proyectos con estado ACTIVO
     */
    public static List<Proyectos> filtrarActivos(List<Proyectos> proyectos) {
        if (proyectos == null) {
            return new ArrayList<>();
        }
        return proyectos.stream()
            .filter(Proyectos::tieneEstadoActivo)
            .toList();
    }

    /**
     * Verifica si hay cupo disponible para más ayudantes
     */
    public boolean tieneCupoDisponible() {
        return getAyudantesActivos() < ayudantesPlanificados;
    }

    /**
     * Retorna el número de cupos disponibles
     */
    public int getCuposDisponibles() {
        return ayudantesPlanificados - getAyudantesActivos();
    }

    /**
     * Crea el resumen del proyecto para vistas/tablas
     */
    public Object[] crearResumen(List<Ayudante> ayudantesProyecto) {
        long contratadosActivos = ayudantesProyecto == null ? 0
            : ayudantesProyecto.stream().filter(Ayudante::esActivo).count();
        int cupos = Math.max(0, ayudantesPlanificados - (int) contratadosActivos);
        return new Object[] {
            codigoProyecto,
            nombreProyecto,
            ayudantesPlanificados,
            tecnicosPlanificados,
            asistentesPlanificados,
            contratadosActivos,
            cupos,
            estado,
            fechaInicio,
            fechaFin
        };
    }

    /**
     * Determina si se puede crear un nuevo proyecto
     */
    public static boolean puedeCrearNuevoProyecto(Proyectos proyectoExistente) {
        return proyectoExistente == null || !"ACTIVO".equals(proyectoExistente.getEstado());
    }

    /**
     * Valida los datos para crear un proyecto
     */
    public static ResultadoOperacion validarCreacion(String codigoProyecto, String nombreProyecto,
                                                     Date fechaInicio, Date fechaFin,
                                                     TipoProyecto tipoProyecto, int ayudantesPlanificados) {
        ResultadoOperacion resultado = new ResultadoOperacion();

        // Validar código del proyecto
        if (codigoProyecto == null || codigoProyecto.trim().isEmpty()) {
            resultado.setMensaje("El código del proyecto es obligatorio");
            resultado.agregarError("Código de proyecto vacío");
            return resultado;
        }

        // Validar nombre del proyecto
        if (nombreProyecto == null || nombreProyecto.trim().isEmpty()) {
            resultado.setMensaje("El nombre del proyecto es obligatorio");
            resultado.agregarError("Nombre de proyecto vacío");
            return resultado;
        }

        // Validar fechas
        if (fechaInicio == null || fechaFin == null) {
            resultado.setMensaje("Las fechas son obligatorias");
            resultado.agregarError("Fechas incompletas");
            return resultado;
        }

        if (fechaFin.before(fechaInicio)) {
            resultado.setMensaje("La fecha de fin debe ser posterior a la fecha de inicio");
            resultado.agregarError("Fechas inválidas");
            return resultado;
        }

        // Validar tipo de proyecto
        if (tipoProyecto == null) {
            resultado.setMensaje("El tipo de proyecto es obligatorio");
            resultado.agregarError("Tipo de proyecto no especificado");
            return resultado;
        }

        // Validar número de ayudantes planificados
        if (ayudantesPlanificados < 0) {
            resultado.setMensaje("El número de ayudantes planificados debe ser mayor o igual a 0");
            resultado.agregarError("Número de ayudantes inválido");
            return resultado;
        }

        if (ayudantesPlanificados > 20) {
            resultado.setMensaje("El número de ayudantes planificados no puede exceder 20");
            resultado.agregarError("Demasiados ayudantes planificados");
            return resultado;
        }

        resultado.setExitoso(true);
        resultado.setMensaje("Validación exitosa");
        return resultado;
    }

    // Getters y Setters
    public String getCodigoProyecto() {
        return codigoProyecto;
    }

    public void setCodigoProyecto(String codigoProyecto) {
        if (codigoProyecto == null || codigoProyecto.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del proyecto no puede estar vacío");
        }
        this.codigoProyecto = codigoProyecto;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        if (nombreProyecto == null || nombreProyecto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del proyecto no puede estar vacío");
        }
        this.nombreProyecto = nombreProyecto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        if (fechaInicio == null) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser nula");
        }
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        if (fechaFin == null) {
            throw new IllegalArgumentException("La fecha de fin no puede ser nula");
        }
        if (this.fechaInicio != null && fechaFin.before(this.fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado del proyecto no puede estar vacío");
        }
        if (!estado.matches("ACTIVO|INACTIVO|SUSPENDIDO")) {
            throw new IllegalArgumentException("El estado debe ser ACTIVO, INACTIVO o SUSPENDIDO");
        }
        this.estado = estado;
    }

    public TipoProyecto getTipoProyecto() {
        return tipoProyecto;
    }

    public void setTipoProyecto(TipoProyecto tipoProyecto) {
        this.tipoProyecto = tipoProyecto;
    }

    public int getAyudantesPlanificados() {
        return ayudantesPlanificados;
    }

    public void setAyudantesPlanificados(int ayudantesPlanificados) {
        this.ayudantesPlanificados = ayudantesPlanificados;
    }

    public int getTecnicosPlanificados() {
        return tecnicosPlanificados;
    }

    public void setTecnicosPlanificados(int tecnicosPlanificados) {
        this.tecnicosPlanificados = tecnicosPlanificados;
    }

    public int getAsistentesPlanificados() {
        return asistentesPlanificados;
    }

    public void setAsistentesPlanificados(int asistentesPlanificados) {
        this.asistentesPlanificados = asistentesPlanificados;
    }

    /**
     * @deprecated No usar para obtener ayudantes del proyecto.
     * Usar AyudanteDAO.buscarPorProyecto(codigoProyecto) en su lugar.
     * Este método solo retorna lista vacía por compatibilidad.
     */
    @Deprecated
    public List<Ayudante> getAyudantes() {
        return new ArrayList<>();
    }

    /**
     * @deprecated No usar para manipular ayudantes del proyecto.
     * Usar AyudanteDAO en su lugar.
     */
    @Deprecated
    public void setAyudantes(List<Ayudante> ayudantes) {
        // Método vacío por compatibilidad
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }
}
