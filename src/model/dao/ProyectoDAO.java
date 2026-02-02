package model.dao;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de ProyectoInvestigacion
 */
public class ProyectoDAO implements IDAO<ProyectoInvestigacion> {
    private Connection conexion;

    public ProyectoDAO() {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }

    @Override
    public boolean guardar(ProyectoInvestigacion proyecto) {
        String sql = "INSERT INTO proyectos (codigo_proyecto, nombre_proyecto, descripcion, " +
                     "fecha_inicio, fecha_fin, estado, tipo_proyecto, ayudantes_planificados, codigo_director) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, proyecto.getCodigoProyecto());
            ps.setString(2, proyecto.getNombreProyecto());
            ps.setString(3, proyecto.getDescripcion());
            ps.setTimestamp(4, new Timestamp(proyecto.getFechaInicio().getTime()));
            ps.setTimestamp(5, new Timestamp(proyecto.getFechaFin().getTime()));
            ps.setString(6, proyecto.getEstado());
            ps.setString(7, proyecto.getTipoProyecto().name());
            ps.setInt(8, proyecto.getAyudantesPlanificados());
            ps.setString(9, proyecto.getDirector() != null ? proyecto.getDirector().getCodigoUnico() : null);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al guardar proyecto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ProyectoInvestigacion buscarPorId(String id) {
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

    public ProyectoInvestigacion buscarPorDirector(String codigoUnico) {
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

    public List<ProyectoInvestigacion> buscarActivos() {
        List<ProyectoInvestigacion> proyectos = new ArrayList<>();
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

    public ProyectoInvestigacion buscarPorCodigo(String codigo) {
        return buscarPorId(codigo);
    }

    @Override
    public List<ProyectoInvestigacion> listarTodos() {
        List<ProyectoInvestigacion> proyectos = new ArrayList<>();
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
    public boolean actualizar(ProyectoInvestigacion proyecto) {
        String sql = "UPDATE proyectos SET nombre_proyecto = ?, descripcion = ?, " +
                     "fecha_inicio = ?, fecha_fin = ?, estado = ?, tipo_proyecto = ?, " +
                     "ayudantes_planificados = ?, codigo_director = ? WHERE codigo_proyecto = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, proyecto.getNombreProyecto());
            ps.setString(2, proyecto.getDescripcion());
            ps.setTimestamp(3, new Timestamp(proyecto.getFechaInicio().getTime()));
            ps.setTimestamp(4, new Timestamp(proyecto.getFechaFin().getTime()));
            ps.setString(5, proyecto.getEstado());
            ps.setString(6, proyecto.getTipoProyecto().name());
            ps.setInt(7, proyecto.getAyudantesPlanificados());
            ps.setString(8, proyecto.getDirector() != null ? proyecto.getDirector().getCodigoUnico() : null);
            ps.setString(9, proyecto.getCodigoProyecto());
            
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

    private ProyectoInvestigacion mapearProyecto(ResultSet rs) throws SQLException {
        ProyectoInvestigacion proyecto = new ProyectoInvestigacion();
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
        proyecto.setTipoProyecto(parseTipoProyecto(rs.getString("tipo_proyecto")));
        proyecto.setAyudantesPlanificados(rs.getInt("ayudantes_planificados"));
        
        return proyecto;
    }

    // Parsea fechas en múltiples formatos (YYYY-MM-DD o YYYY-MM-DD HH:MM:SS.mmm)
    private java.util.Date parseFecha(String fechaStr) {
        if (fechaStr == null || fechaStr.isBlank()) {
            return null;
        }
        
        try {
            // Si la fecha incluye timestamp, extraer solo la parte de la fecha
            String fechaSolo = fechaStr.split(" ")[0];
            return java.sql.Date.valueOf(fechaSolo);
        } catch (IllegalArgumentException e) {
            System.out.println("Error al parsear fecha: " + fechaStr);
            return null;
        }
    }

    // Normaliza valores leídos de BD a los enums definidos
    private TipoProyecto parseTipoProyecto(String valor) {
        if (valor == null) {
            return TipoProyecto.INTERNO;
        }
        String v = valor.trim().toUpperCase();
        switch (v) {
            case "GRUPAL":
            case "GRUPALES":
                return TipoProyecto.GRUPALES;
            case "TRANSFERENCIA":
            case "TRANSFERENCIA_TECNOLOGICA":
                return TipoProyecto.TRANSFERENCIA_TECNOLOGICA;
            case "VINCULACION_CON_FINANCIAMIENTO":
                return TipoProyecto.VINCULACION_CON_FINANCIAMIENTO;
            case "SEMILLA":
                return TipoProyecto.SEMILLA;
            case "INTERNO":
            default:
                return TipoProyecto.INTERNO;
        }
    }
}