package model.dao;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Ayudante
 * CORREGIDO: NO modifica el rol en miembros_epn
 */
public class AyudanteDAO implements IDAO<Ayudante> {
    private Connection conexion;

    public AyudanteDAO() {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }

    @Override
    public boolean guardar(Ayudante ayudante) {
        // CORREGIDO: NO actualizar rol en miembros_epn
        // El estudiante sigue siendo ESTUDIANTE incluso cuando es ayudante
        
        String sql = "INSERT INTO ayudantes (codigo_unico, cedula, correo_institucional, " +
                     "nombres, apellidos, telefono, carrera, nivel, ira, horas_semanales, meses_contratados, " +
                     "estado, fecha_registro, codigo_proyecto) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, ayudante.getCodigoUnico());
            ps.setString(2, ayudante.getCedula());
            ps.setString(3, ayudante.getCorreoInstitucional());
            ps.setString(4, ayudante.getNombres());
            ps.setString(5, ayudante.getApellidos());
            ps.setString(6, ayudante.getTelefono());
            ps.setString(7, ayudante.getCarrera());
            ps.setInt(8, ayudante.getNivel());
            ps.setFloat(9, ayudante.getIRA());
            ps.setInt(10, ayudante.getHorasSemanales());
            ps.setInt(11, ayudante.getMesesContratados());
            ps.setString(12, ayudante.getEstado() != null ? ayudante.getEstado() : "ACTIVO");
            ps.setTimestamp(13, new Timestamp(ayudante.getFechaRegistro().getTime()));
            ps.setString(14, ayudante.getProyectoAsignado() != null ? 
                        ayudante.getProyectoAsignado().getCodigoProyecto() : null);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✓ Ayudante guardado en tabla AYUDANTES: " + ayudante.getCodigoUnico());
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("✗ Error al guardar ayudante: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Ayudante buscarPorId(String id) {
        String sql = "SELECT * FROM ayudantes WHERE codigo_unico = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearAyudante(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar ayudante: " + e.getMessage());
        }
        return null;
    }

    public List<Ayudante> buscarPorProyecto(String codigoProyecto) {
        List<Ayudante> ayudantes = new ArrayList<>();
        String sql = "SELECT * FROM ayudantes WHERE codigo_proyecto = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, codigoProyecto);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ayudantes.add(mapearAyudante(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por proyecto: " + e.getMessage());
        }
        return ayudantes;
    }

    public List<Ayudante> buscarActivos() {
        List<Ayudante> ayudantes = new ArrayList<>();
        String sql = "SELECT * FROM ayudantes WHERE estado = 'ACTIVO' AND fecha_finalizacion IS NULL";
        
        try (Statement st = conexion.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                ayudantes.add(mapearAyudante(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar activos: " + e.getMessage());
        }
        return ayudantes;
    }

    public List<Ayudante> buscarPorCarrera(String carrera) {
        List<Ayudante> ayudantes = new ArrayList<>();
        String sql = "SELECT * FROM ayudantes WHERE carrera = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, carrera);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ayudantes.add(mapearAyudante(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por carrera: " + e.getMessage());
        }
        return ayudantes;
    }

    public List<Ayudante> buscarPorNivel(int nivel) {
        List<Ayudante> ayudantes = new ArrayList<>();
        String sql = "SELECT * FROM ayudantes WHERE nivel = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, nivel);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ayudantes.add(mapearAyudante(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por nivel: " + e.getMessage());
        }
        return ayudantes;
    }

    @Override
    public List<Ayudante> listarTodos() {
        List<Ayudante> ayudantes = new ArrayList<>();
        String sql = "SELECT * FROM ayudantes";
        
        try (Statement st = conexion.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                ayudantes.add(mapearAyudante(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar ayudantes: " + e.getMessage());
        }
        return ayudantes;
    }

    @Override
    public boolean actualizar(Ayudante ayudante) {
        String sql = "UPDATE ayudantes SET cedula = ?, correo_institucional = ?, " +
                     "nombres = ?, apellidos = ?, telefono = ?, carrera = ?, nivel = ?, ira = ?, " +
                     "horas_semanales = ?, meses_contratados = ?, estado = ?, fecha_finalizacion = ?, " +
                     "motivo_salida = ?, codigo_proyecto = ? WHERE codigo_unico = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, ayudante.getCedula());
            ps.setString(2, ayudante.getCorreoInstitucional());
            ps.setString(3, ayudante.getNombres());
            ps.setString(4, ayudante.getApellidos());
            ps.setString(5, ayudante.getTelefono());
            ps.setString(6, ayudante.getCarrera());
            ps.setInt(7, ayudante.getNivel());
            ps.setFloat(8, ayudante.getIRA());
            ps.setInt(9, ayudante.getHorasSemanales());
            ps.setInt(10, ayudante.getMesesContratados());
            ps.setString(11, ayudante.getEstado());
            ps.setTimestamp(12, ayudante.getFechaFinalizacion() != null ? 
                           new Timestamp(ayudante.getFechaFinalizacion().getTime()) : null);
            ps.setString(13, ayudante.getMotivoSalida());
            ps.setString(14, ayudante.getProyectoAsignado() != null ? 
                        ayudante.getProyectoAsignado().getCodigoProyecto() : null);
            ps.setString(15, ayudante.getCodigoUnico());
            
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✓ Ayudante actualizado: " + ayudante.getCodigoUnico());
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("✗ Error al actualizar ayudante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(String id) {
        // IMPORTANTE: Soft delete - Solo marca como INACTIVO en tabla ayudantes
        // NO elimina de tabla estudiantes, NO modifica rol en miembros_epn
        String sql = "UPDATE ayudantes SET estado = 'INACTIVO', " +
                     "fecha_finalizacion = CURRENT_TIMESTAMP, " +
                     "motivo_salida = ? WHERE codigo_unico = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, "Baja del sistema");
            ps.setString(2, id);
            int rows = ps.executeUpdate();
            
            if (rows > 0) {
                System.out.println("✓ Ayudante dado de baja (soft delete, estudiante preservado): " + id);
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("✗ Error al eliminar ayudante: " + e.getMessage());
            return false;
        }
    }

    private Ayudante mapearAyudante(ResultSet rs) throws SQLException {
        Ayudante ayudante = new Ayudante();
        ayudante.setCodigoUnico(rs.getString("codigo_unico"));
        ayudante.setCedula(rs.getString("cedula"));
        ayudante.setCorreoInstitucional(rs.getString("correo_institucional"));
        ayudante.setNombres(rs.getString("nombres"));
        ayudante.setApellidos(rs.getString("apellidos"));
        ayudante.setTelefono(rs.getString("telefono"));
        ayudante.setCarrera(rs.getString("carrera"));
        ayudante.setNivel(rs.getInt("nivel"));
        ayudante.setIRA(rs.getFloat("ira"));
        ayudante.setHorasSemanales(rs.getInt("horas_semanales"));
        ayudante.setMesesContratados(rs.getInt("meses_contratados"));
        ayudante.setEstado(rs.getString("estado"));
        
        if (rs.getTimestamp("fecha_registro") != null) {
            ayudante.setFechaRegistro(new java.util.Date(rs.getTimestamp("fecha_registro").getTime()));
        }
        
        if (rs.getTimestamp("fecha_finalizacion") != null) {
            ayudante.setFechaFinalizacion(new java.util.Date(rs.getTimestamp("fecha_finalizacion").getTime()));
        }
        
        ayudante.setMotivoSalida(rs.getString("motivo_salida"));
        
        String codigoProyecto = rs.getString("codigo_proyecto");
        if (codigoProyecto != null && !codigoProyecto.isBlank()) {
            try {
                ProyectoDAO proyectoDAO = new ProyectoDAO();
                Proyectos proyecto = proyectoDAO.buscarPorId(codigoProyecto);
                ayudante.setProyectoAsignado(proyecto);
            } catch (Exception ignored) {}
        }

        return ayudante;
    }
}
