package persistencia;

import dominio.Mensaje;
import dominio.MiembroFIS;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * DAO para Mensaje usando SQLite
 */
public class MensajeDAO implements IDAO {
    
    private static final String DB_URL = "jdbc:sqlite:bd/gestion_ayudantes.db";
    private MiembroFISDAO miembroDAO;

    public MensajeDAO() {
        this.miembroDAO = new MiembroFISDAO();
    }

    @Override
    public boolean guardar(Object entidad) {
        if (!(entidad instanceof Mensaje)) {
            return false;
        }
        
        Mensaje mensaje = (Mensaje) entidad;
        String sql = "INSERT OR REPLACE INTO Mensaje " +
                     "(id_mensaje, fecha, asunto, contenido, numero_unico_remitente, " +
                     "numero_unico_destinatario, codigo_proyecto, leido, respuesta, fecha_respuesta) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, mensaje.getIdMensaje());
            pstmt.setString(2, formatDate(mensaje.getFecha()));
            pstmt.setString(3, mensaje.getAsunto());
            pstmt.setString(4, mensaje.getContenido());
            pstmt.setString(5, mensaje.getRemitente() != null ? mensaje.getRemitente().getNumeroUnico() : null);
            pstmt.setString(6, mensaje.getDestinatario() != null ? mensaje.getDestinatario().getNumeroUnico() : null);
            pstmt.setString(7, mensaje.getProyectoRelacionado() != null ? mensaje.getProyectoRelacionado().getCodigoProyecto() : null);
            pstmt.setBoolean(8, mensaje.isLeido());
            pstmt.setString(9, mensaje.getRespuesta());
            pstmt.setString(10, formatDate(mensaje.getFechaRespuesta()));
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("✗ Error guardando mensaje: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Object buscarPorId(String idMensaje) {
        String sql = "SELECT * FROM Mensaje WHERE id_mensaje = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, idMensaje);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearMensaje(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error buscando mensaje: " + e.getMessage());
        }
        return null;
    }

    public List<Mensaje> buscarPorDestinatario(String numeroUnicoDestinatario) {
        List<Mensaje> lista = new ArrayList<>();
        String sql = "SELECT * FROM Mensaje WHERE numero_unico_destinatario = ? ORDER BY fecha DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, numeroUnicoDestinatario);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Mensaje m = mapearMensaje(rs);
                    if (m != null) lista.add(m);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error buscando mensajes del destinatario: " + e.getMessage());
        }
        return lista;
    }

    public List<Mensaje> buscarNoLeidos(String numeroUnicoDestinatario) {
        List<Mensaje> lista = new ArrayList<>();
        String sql = "SELECT * FROM Mensaje WHERE numero_unico_destinatario = ? AND leido = 0 ORDER BY fecha DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, numeroUnicoDestinatario);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Mensaje m = mapearMensaje(rs);
                    if (m != null) lista.add(m);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error buscando mensajes no leídos: " + e.getMessage());
        }
        return lista;
    }

    public boolean marcarComoLeido(String idMensaje) {
        String sql = "UPDATE Mensaje SET leido = 1 WHERE id_mensaje = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, idMensaje);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("✗ Error marcando mensaje como leído: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Object> listarTodos() {
        List<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM Mensaje ORDER BY fecha DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Mensaje m = mapearMensaje(rs);
                if (m != null) lista.add(m);
            }
        } catch (SQLException e) {
            System.err.println("✗ Error listando mensajes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Object entidad) {
        return guardar(entidad);
    }

    @Override
    public boolean eliminar(String id) {
        String sql = "DELETE FROM Mensaje WHERE id_mensaje = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("✗ Error eliminando mensaje: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int contarTodos() {
        String sql = "SELECT COUNT(*) as total FROM Mensaje";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error contando mensajes: " + e.getMessage());
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
     * Mapea un ResultSet a un objeto Mensaje
     */
    private Mensaje mapearMensaje(ResultSet rs) throws SQLException {
        Mensaje mensaje = new Mensaje();
        
        mensaje.setIdMensaje(rs.getString("id_mensaje"));
        
        // Nota: fecha se establece en el constructor, aquí es inmutable
        
        mensaje.setAsunto(rs.getString("asunto"));
        mensaje.setContenido(rs.getString("contenido"));
        
        String remitenteId = rs.getString("numero_unico_remitente");
        if (remitenteId != null) {
            MiembroFIS remitente = (MiembroFIS) miembroDAO.buscarPorId(remitenteId);
            if (remitente != null) {
                mensaje.setRemitente(remitente);
            }
        }
        
        String destinatarioId = rs.getString("numero_unico_destinatario");
        if (destinatarioId != null) {
            MiembroFIS destinatario = (MiembroFIS) miembroDAO.buscarPorId(destinatarioId);
            if (destinatario != null) {
                mensaje.setDestinatario(destinatario);
            }
        }
        
        if (rs.getBoolean("leido")) {
            mensaje.marcarComoLeido();
        }
        
        String respuesta = rs.getString("respuesta");
        if (respuesta != null) {
            mensaje.setRespuesta(respuesta);
        }
        
        return mensaje;
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
