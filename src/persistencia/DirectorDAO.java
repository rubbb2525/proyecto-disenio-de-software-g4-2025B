package persistencia;

import dominio.Director;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para Director usando SQLite
 */
public class DirectorDAO implements IDAO {
    
    private static final String DB_URL = "jdbc:sqlite:bd/gestion_ayudantes.db";

    public DirectorDAO() {
        // Constructor vacío - la BD ya está creada
    }

    @Override
    public boolean guardar(Object entidad) {
        if (!(entidad instanceof Director)) {
            return false;
        }
        
        Director director = (Director) entidad;
        String sql = "INSERT OR REPLACE INTO MiembroFIS " +
                     "(numero_unico, cedula, correo_institucional, password, nombres, apellidos, " +
                     "telefono, tipo_miembro, especialidad_director, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, 'DIRECTOR', ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, director.getNumeroUnico());
            pstmt.setString(2, director.getCedula());
            pstmt.setString(3, director.getCorreoInstitucional());
            pstmt.setString(4, director.getPassword());
            pstmt.setString(5, director.getNombres());
            pstmt.setString(6, director.getApellidos());
            pstmt.setString(7, director.getTelefono());
            pstmt.setString(8, director.getEspecialidad());
            pstmt.setString(9, director.getEstado());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("✗ Error guardando director: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Object buscarPorId(String numeroUnico) {
        String sql = "SELECT * FROM MiembroFIS WHERE numero_unico = ? AND tipo_miembro = 'DIRECTOR'";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, numeroUnico);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearDirector(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error buscando director: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Object> listarTodos() {
        List<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM MiembroFIS WHERE tipo_miembro = 'DIRECTOR' AND estado = 'ACTIVO'";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Director director = mapearDirector(rs);
                if (director != null) lista.add(director);
            }
        } catch (SQLException e) {
            System.err.println("✗ Error listando directores: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Object entidad) {
        return guardar(entidad);
    }

    @Override
    public boolean eliminar(String id) {
        String sql = "UPDATE MiembroFIS SET estado = 'INACTIVO' WHERE numero_unico = ? AND tipo_miembro = 'DIRECTOR'";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("✗ Error eliminando director: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int contarTodos() {
        String sql = "SELECT COUNT(*) as total FROM MiembroFIS WHERE tipo_miembro = 'DIRECTOR' AND estado = 'ACTIVO'";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error contando directores: " + e.getMessage());
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
     * Mapea un ResultSet a un objeto Director
     */
    private Director mapearDirector(ResultSet rs) throws SQLException {
        Director director = new Director();
        
        director.setNumeroUnico(rs.getString("numero_unico"));
        director.setCedula(rs.getString("cedula"));
        director.setCorreoInstitucional(rs.getString("correo_institucional"));
        director.setPassword(rs.getString("password"));
        director.setNombres(rs.getString("nombres"));
        director.setApellidos(rs.getString("apellidos"));
        director.setTelefono(rs.getString("telefono"));
        director.setEspecialidad(rs.getString("especialidad_director"));
        director.setEstado(rs.getString("estado"));
        
        return director;
    }
}
