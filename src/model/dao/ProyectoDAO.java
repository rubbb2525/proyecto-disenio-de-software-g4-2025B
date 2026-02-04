package model.dao;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Proyectos
 */
public class ProyectoDAO implements IDAO<Proyectos> {
    private Connection conexion;

    public ProyectoDAO() {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }

    @Override
    public boolean guardar(Proyectos proyecto) {
        String sql = "INSERT INTO proyectos (codigo_proyecto, nombre_proyecto, descripcion, " +
                     "fecha_inicio, fecha_fin, estado, categoria_proyecto, tipo_proyecto, " +
                     "ayudantes_planificados, tecnicos_planificados, asistentes_planificados, codigo_director) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, proyecto.getCodigoProyecto());
            ps.setString(2, proyecto.getNombreProyecto());
            ps.setString(3, proyecto.getDescripcion());
            ps.setTimestamp(4, new Timestamp(proyecto.getFechaInicio().getTime()));
            ps.setTimestamp(5, new Timestamp(proyecto.getFechaFin().getTime()));
            ps.setString(6, proyecto.getEstado());
            // Guardar la categoría principal derivada del tipo
            ps.setString(7, proyecto.getTipoProyecto() != null ? proyecto.getTipoProyecto().getCategoria().name() : null);
            ps.setString(8, proyecto.getTipoProyecto() != null ? proyecto.getTipoProyecto().name() : null);
            ps.setInt(9, proyecto.getAyudantesPlanificados());
            ps.setInt(10, proyecto.getTecnicosPlanificados());
            ps.setInt(11, proyecto.getAsistentesPlanificados());
            ps.setString(12, proyecto.getDirector() != null ? proyecto.getDirector().getCodigoUnico() : null);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al guardar proyecto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Proyectos buscarPorId(String id) {
        String sql = "SELECT * FROM proyectos WHERE codigo_proyecto = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearProyecto(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar proyecto: " + e.getMessage());
        }
        return null;
    }

    public Proyectos buscarPorDirector(String codigoUnico) {
        String sql = "SELECT * FROM proyectos WHERE codigo_director = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, codigoUnico);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearProyecto(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por director: " + e.getMessage());
        }
        return null;
    }

    public List<Proyectos> buscarActivos() {
        List<Proyectos> proyectos = new ArrayList<>();
        String sql = "SELECT * FROM proyectos WHERE estado = 'ACTIVO'";
        
        try (Statement st = conexion.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                proyectos.add(mapearProyecto(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar activos: " + e.getMessage());
        }
        return proyectos;
    }

    public Proyectos buscarPorCodigo(String codigo) {
        return buscarPorId(codigo);
    }

    @Override
    public List<Proyectos> listarTodos() {
        List<Proyectos> proyectos = new ArrayList<>();
        String sql = "SELECT * FROM proyectos";
        
        try (Statement st = conexion.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                proyectos.add(mapearProyecto(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar proyectos: " + e.getMessage());
        }
        return proyectos;
    }

    @Override
    public boolean actualizar(Proyectos proyecto) {
        String sql = "UPDATE proyectos SET nombre_proyecto = ?, descripcion = ?, " +
                     "fecha_inicio = ?, fecha_fin = ?, estado = ?, categoria_proyecto = ?, tipo_proyecto = ?, " +
                     "ayudantes_planificados = ?, tecnicos_planificados = ?, asistentes_planificados = ?, " +
                     "codigo_director = ? WHERE codigo_proyecto = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, proyecto.getNombreProyecto());
            ps.setString(2, proyecto.getDescripcion());
            ps.setTimestamp(3, new Timestamp(proyecto.getFechaInicio().getTime()));
            ps.setTimestamp(4, new Timestamp(proyecto.getFechaFin().getTime()));
            ps.setString(5, proyecto.getEstado());
            ps.setString(6, proyecto.getTipoProyecto() != null ? proyecto.getTipoProyecto().getCategoria().name() : null);
            ps.setString(7, proyecto.getTipoProyecto() != null ? proyecto.getTipoProyecto().name() : null);
            ps.setInt(8, proyecto.getAyudantesPlanificados());
            ps.setInt(9, proyecto.getTecnicosPlanificados());
            ps.setInt(10, proyecto.getAsistentesPlanificados());
            ps.setString(11, proyecto.getDirector() != null ? proyecto.getDirector().getCodigoUnico() : null);
            ps.setString(12, proyecto.getCodigoProyecto());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar proyecto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(String id) {
        String sql = "DELETE FROM proyectos WHERE codigo_proyecto = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar proyecto: " + e.getMessage());
            return false;
        }
    }

    private Proyectos mapearProyecto(ResultSet rs) throws SQLException {
        Proyectos proyecto = new Proyectos();
        proyecto.setCodigoProyecto(rs.getString("codigo_proyecto"));
        proyecto.setNombreProyecto(rs.getString("nombre_proyecto"));
        proyecto.setDescripcion(rs.getString("descripcion"));

        // SQLite almacena fechas como texto; soportar múltiples formatos
        String inicioStr = rs.getString("fecha_inicio");
        String finStr = rs.getString("fecha_fin");
        if (inicioStr != null && !inicioStr.isBlank()) {
            proyecto.setFechaInicio(parseFecha(inicioStr));
        }
        if (finStr != null && !finStr.isBlank()) {
            proyecto.setFechaFin(parseFecha(finStr));
        }

        proyecto.setEstado(rs.getString("estado"));
        // Si la BD contiene categoría explícita la usamos para consistencia (aunque el tipo también define la categoría)
        String categoriaStr = null;
        try {
            categoriaStr = rs.getString("categoria_proyecto");
        } catch (SQLException ignored) { }
        proyecto.setTipoProyecto(parseTipoProyecto(rs.getString("tipo_proyecto"), categoriaStr));
        proyecto.setAyudantesPlanificados(rs.getInt("ayudantes_planificados"));
        
        // Obtener nuevos campos de planificación
        try {
            proyecto.setTecnicosPlanificados(rs.getInt("tecnicos_planificados"));
            proyecto.setAsistentesPlanificados(rs.getInt("asistentes_planificados"));
        } catch (SQLException ignored) {
            // Para BD antigua, estos campos pueden no existir
            proyecto.setTecnicosPlanificados(0);
            proyecto.setAsistentesPlanificados(0);
        }
        
        return proyecto;
    }

    // Parsea fechas en múltiples formatos (YYYY-MM-DD, YYYY-MM-DD HH:MM:SS.mmm, o timestamp en ms)
    private java.util.Date parseFecha(String fechaStr) {
        if (fechaStr == null || fechaStr.isBlank()) {
            return null;
        }
        
        try {
            // Intentar parsear como timestamp en milisegundos (números largos)
            if (fechaStr.matches("\\d+")) {
                try {
                    long timestamp = Long.parseLong(fechaStr);
                    return new java.util.Date(timestamp);
                } catch (NumberFormatException ignored) {
                    // No es un número, intentar otros formatos
                }
            }
            
            // Intentar parsear como fecha YYYY-MM-DD (extraer solo la parte de fecha si incluye tiempo)
            String fechaSolo = fechaStr.split(" ")[0];
            return java.sql.Date.valueOf(fechaSolo);
        } catch (Exception e) {
            // Silenciosamente ignorar errores de parseo - la fecha puede no ser válida
            return null;
        }
    }

    // Normaliza valores leídos de BD a los enums definidos
    private TipoProyecto parseTipoProyecto(String valor, String categoriaOpt) {
        if (valor == null && categoriaOpt != null) {
            // Si no hay tipo pero sí categoría, inferir un valor por categoría
            String c = categoriaOpt.trim().toUpperCase();
            switch (c) {
                case "VINCULACION":
                case "VINCULACION_CON_FINANCIAMIENTO":
                    return TipoProyecto.VINCULACION_CON_FINANCIAMIENTO;
                case "TRANSFERENCIA_TECNOLOGICA":
                case "TRANSFERENCIA":
                    return TipoProyecto.TRANSFERENCIA_TECNOLOGICA;
                case "INVESTIGACION":
                default:
                    return TipoProyecto.INTERNO;
            }
        }

        if (valor == null) {
            return TipoProyecto.INTERNO;
        }
        String v = valor.trim().toUpperCase();
        switch (v) {
            case "GRUPAL":
            case "GRUPALES":
                return TipoProyecto.GRUPAL;
            case "MULTIDISCIPLINARIO":
                return TipoProyecto.MULTIDISCIPLINARIO;
            case "TRANSFERENCIA":
            case "TRANSFERENCIA_TECNOLOGICA":
                return TipoProyecto.TRANSFERENCIA_TECNOLOGICA;
            case "VINCULACION_CON_FINANCIAMIENTO":
            case "VINCULACION":
                return TipoProyecto.VINCULACION_CON_FINANCIAMIENTO;
            case "SEMILLA":
                return TipoProyecto.SEMILLA;
            case "INTERNO":
            default:
                return TipoProyecto.INTERNO;
        }
    }
}