package persistencia;

import gestores.Notificacion;
import dominio.ProyectoInvestigacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
 * DAO para Notificacion usando SQLite
 */
public class NotificacionDAO implements IDAO {
    
    private static final String DB_URL = "jdbc:sqlite:bd/gestion_ayudantes.db";
    private ProyectoDAO proyectoDAO;

    public NotificacionDAO() {
        this.proyectoDAO = new ProyectoDAO();
    }

    @Override
    public boolean guardar(Object entidad) {
        if (!(entidad instanceof Notificacion)) {
            return false;
        }
        
        Notificacion notificacion = (Notificacion) entidad;
        String sql = "INSERT OR REPLACE INTO Notificacion " +
                     "(id_notificacion, fecha, mensaje, tipo, leida, prioridad, codigo_proyecto) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (notificacion.getIdNotificacion() == null || notificacion.getIdNotificacion().isEmpty()) {
                notificacion.setIdNotificacion(UUID.randomUUID().toString());
            }
            
            pstmt.setString(1, notificacion.getIdNotificacion());
            pstmt.setString(2, formatDate(notificacion.getFecha()));
            pstmt.setString(3, notificacion.getMensaje());
            pstmt.setString(4, notificacion.getTipo());
            pstmt.setBoolean(5, notificacion.isLeida());
            pstmt.setString(6, notificacion.getPrioridad());
            pstmt.setString(7, notificacion.getProyectoRelacionado() != null ? notificacion.getProyectoRelacionado().getCodigoProyecto() : null);
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("✗ Error guardando notificación: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Object buscarPorId(String idNotificacion) {
        String sql = "SELECT * FROM Notificacion WHERE id_notificacion = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, idNotificacion);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearNotificacion(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error buscando notificación: " + e.getMessage());
        }
        return null;
    }

    public List<Notificacion> buscarNoLeidas() {
        List<Notificacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM Notificacion WHERE leida = 0 ORDER BY fecha DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Notificacion n = mapearNotificacion(rs);
                if (n != null) lista.add(n);
            }
        } catch (SQLException e) {
            System.err.println("✗ Error buscando notificaciones no leídas: " + e.getMessage());
        }
        return lista;
    }

    public List<Notificacion> buscarPorTipo(String tipo) {
        List<Notificacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM Notificacion WHERE tipo = ? ORDER BY fecha DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tipo);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Notificacion n = mapearNotificacion(rs);
                    if (n != null) lista.add(n);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error buscando notificaciones por tipo: " + e.getMessage());
        }
        return lista;
    }

    public List<Notificacion> buscarImportantes() {
        List<Notificacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM Notificacion WHERE prioridad IN ('ALTA', 'URGENTE') ORDER BY fecha DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Notificacion n = mapearNotificacion(rs);
                if (n != null) lista.add(n);
            }
        } catch (SQLException e) {
            System.err.println("✗ Error buscando notificaciones importantes: " + e.getMessage());
        }
        return lista;
    }

    public boolean marcarComoLeida(String idNotificacion) {
        String sql = "UPDATE Notificacion SET leida = 1 WHERE id_notificacion = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, idNotificacion);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("✗ Error marcando notificación como leída: " + e.getMessage());
            return false;
        }
    }

    public boolean marcarTodasComoLeidas() {
        String sql = "UPDATE Notificacion SET leida = 1 WHERE leida = 0";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            System.err.println("✗ Error marcando todas como leídas: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Object> listarTodos() {
        List<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM Notificacion ORDER BY fecha DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Notificacion n = mapearNotificacion(rs);
                if (n != null) lista.add(n);
            }
        } catch (SQLException e) {
            System.err.println("✗ Error listando notificaciones: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Object entidad) {
        return guardar(entidad);
    }

    @Override
    public boolean eliminar(String id) {
        String sql = "DELETE FROM Notificacion WHERE id_notificacion = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("✗ Error eliminando notificación: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int contarTodos() {
        String sql = "SELECT COUNT(*) as total FROM Notificacion";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error contando notificaciones: " + e.getMessage());
        }
        return 0;
    }

    public int contarNoLeidas() {
        String sql = "SELECT COUNT(*) as total FROM Notificacion WHERE leida = 0";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error contando notificaciones no leídas: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Obtiene conexión a la BD SQLite
     */
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Driver SQLite no encontrado: " + e.getMessage());
        }
        
        Connection conn = DriverManager.getConnection(DB_URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }
        return conn;
    }

    /**
     * Mapea un ResultSet a un objeto Notificacion
     */
    private Notificacion mapearNotificacion(ResultSet rs) throws SQLException {
        Notificacion notificacion = new Notificacion();
        
        notificacion.setIdNotificacion(rs.getString("id_notificacion"));
        
        String fechaStr = rs.getString("fecha");
        if (fechaStr != null) {
            try {
                notificacion.setFecha(parseDate(fechaStr));
            } catch (ParseException e) {
                System.err.println("✗ Error parseando fecha: " + e.getMessage());
            }
        }
        
        notificacion.setMensaje(rs.getString("mensaje"));
        notificacion.setTipo(rs.getString("tipo"));
        notificacion.setLeida(rs.getBoolean("leida"));
        notificacion.setPrioridad(rs.getString("prioridad"));
        
        String codigoProyecto = rs.getString("codigo_proyecto");
        if (codigoProyecto != null) {
            ProyectoInvestigacion proyecto = (ProyectoInvestigacion) proyectoDAO.buscarPorId(codigoProyecto);
            if (proyecto != null) {
                notificacion.setProyectoRelacionado(proyecto);
            }
        }
        
        return notificacion;
    }

    private String formatDate(Date date) {
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    private Date parseDate(String dateStr) throws ParseException {
        if (dateStr == null || dateStr.isEmpty()) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(dateStr);
    }
}
