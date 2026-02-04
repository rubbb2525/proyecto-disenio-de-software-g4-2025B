package model.dao;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Notificacion
 */
public class NotificacionDAO implements IDAO<Notificacion> {
    private Connection conexion;

    public NotificacionDAO() {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }

    @Override
    public boolean guardar(Notificacion notificacion) {
        String sql = "INSERT INTO notificaciones " +
                     "(id_notificacion, fecha, mensaje, tipo, leida, codigo_proyecto, codigo_ayudante) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, notificacion.getIdNotificacion());
            ps.setTimestamp(2, new Timestamp(notificacion.getFecha().getTime()));
            ps.setString(3, notificacion.getMensaje());
            ps.setString(4, notificacion.getTipo());
            ps.setBoolean(5, notificacion.isLeida());
            ps.setString(6, notificacion.getProyectoRelacionado() != null ? 
                        notificacion.getProyectoRelacionado().getCodigoProyecto() : null);
            ps.setString(7, notificacion.getAyudanteRelacionado() != null ? 
                        notificacion.getAyudanteRelacionado().getCodigoUnico() : null);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al guardar notificación: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Notificacion buscarPorId(String id) {
        String sql = "SELECT * FROM notificaciones WHERE id_notificacion = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearNotificacion(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar notificación: " + e.getMessage());
        }
        return null;
    }

    public List<Notificacion> listarTodas() {
        List<Notificacion> notificaciones = new ArrayList<>();
        String sql = "SELECT * FROM notificaciones ORDER BY fecha DESC";
        
        try (Statement stmt = conexion.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                notificaciones.add(mapearNotificacion(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar notificaciones: " + e.getMessage());
        }
        return notificaciones;
    }

    @Override
    public List<Notificacion> listarTodos() {
        return listarTodas();
    }

    public List<Notificacion> listarNoLeidas() {
        List<Notificacion> notificaciones = new ArrayList<>();
        String sql = "SELECT * FROM notificaciones WHERE leida = 0 ORDER BY fecha DESC";
        
        try (Statement stmt = conexion.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                notificaciones.add(mapearNotificacion(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar notificaciones no leídas: " + e.getMessage());
        }
        return notificaciones;
    }

    public boolean marcarComoLeida(String idNotificacion) {
        String sql = "UPDATE notificaciones SET leida = 1 WHERE id_notificacion = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, idNotificacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al marcar notificación como leída: " + e.getMessage());
            return false;
        }
    }

    public boolean marcarTodasComoLeidas() {
        String sql = "UPDATE notificaciones SET leida = 1 WHERE leida = 0";
        
        try (Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            System.out.println("Error al marcar todas como leídas: " + e.getMessage());
            return false;
        }
    }

    public boolean existe(String idNotificacion) {
        String sql = "SELECT COUNT(*) FROM notificaciones WHERE id_notificacion = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, idNotificacion);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar existencia: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean actualizar(Notificacion notificacion) {
        String sql = "UPDATE notificaciones SET mensaje = ?, tipo = ?, leida = ?, " +
                     "codigo_proyecto = ?, codigo_ayudante = ? WHERE id_notificacion = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, notificacion.getMensaje());
            ps.setString(2, notificacion.getTipo());
            ps.setBoolean(3, notificacion.isLeida());
            ps.setString(4, notificacion.getProyectoRelacionado() != null ? 
                        notificacion.getProyectoRelacionado().getCodigoProyecto() : null);
            ps.setString(5, notificacion.getAyudanteRelacionado() != null ? 
                        notificacion.getAyudanteRelacionado().getCodigoUnico() : null);
            ps.setString(6, notificacion.getIdNotificacion());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar notificación: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(String id) {
        String sql = "DELETE FROM notificaciones WHERE id_notificacion = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar notificación: " + e.getMessage());
            return false;
        }
    }

    private Notificacion mapearNotificacion(ResultSet rs) throws SQLException {
        Notificacion notificacion = new Notificacion();
        notificacion.setIdNotificacion(rs.getString("id_notificacion"));
        notificacion.setFecha(new java.util.Date(rs.getTimestamp("fecha").getTime()));
        notificacion.setMensaje(rs.getString("mensaje"));
        notificacion.setTipo(rs.getString("tipo"));
        notificacion.setLeida(rs.getBoolean("leida"));
        
        // Cargar proyecto relacionado
        String codigoProyecto = rs.getString("codigo_proyecto");
        if (codigoProyecto != null) {
            ProyectoDAO proyectoDAO = new ProyectoDAO();
            Proyectos proyecto = proyectoDAO.buscarPorId(codigoProyecto);
            notificacion.setProyectoRelacionado(proyecto);
        }
        
        // Cargar ayudante relacionado
        String codigoAyudante = rs.getString("codigo_ayudante");
        if (codigoAyudante != null) {
            AyudanteDAO ayudanteDAO = new AyudanteDAO();
            Ayudante ayudante = ayudanteDAO.buscarPorId(codigoAyudante);
            notificacion.setAyudanteRelacionado(ayudante);
        }
        
        return notificacion;
    }
}
