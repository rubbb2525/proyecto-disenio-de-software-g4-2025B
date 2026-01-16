package persistencia;

import dominio.Ayudante;
import dominio.ProyectoInvestigacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para Ayudante usando SQLite
 * Migración de serialización a base de datos
 */
public class AyudanteDAO implements IDAO {
    
    private static final String DB_URL = "jdbc:sqlite:bd/gestion_ayudantes.db";

    public AyudanteDAO() {
        // Constructor vacío - la BD ya está creada
    }

    @Override
    public boolean guardar(Object entidad) {
        if (!(entidad instanceof Ayudante)) {
            return false;
        }
        
        Ayudante ayudante = (Ayudante) entidad;
        String sql = "INSERT OR REPLACE INTO MiembroFIS " +
                     "(numero_unico, cedula, correo_institucional, password, nombres, apellidos, " +
                     "telefono, tipo_miembro, carrera, nivel, promedio, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, 'AYUDANTE', ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, ayudante.getNumeroUnico());
            pstmt.setString(2, ayudante.getCedula());
            pstmt.setString(3, ayudante.getCorreoInstitucional());
            pstmt.setString(4, ayudante.getPassword());
            pstmt.setString(5, ayudante.getNombres());
            pstmt.setString(6, ayudante.getApellidos());
            pstmt.setString(7, ayudante.getTelefono());
            pstmt.setString(8, ayudante.getCarrera());
            pstmt.setInt(9, ayudante.getNivel());
            pstmt.setFloat(10, ayudante.getPromedio());
            pstmt.setString(11, ayudante.getEstado());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("✗ Error guardando ayudante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Object buscarPorId(String numeroUnico) {
        String sql = "SELECT * FROM MiembroFIS WHERE numero_unico = ? AND tipo_miembro = 'AYUDANTE'";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, numeroUnico);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearAyudante(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error buscando ayudante: " + e.getMessage());
        }
        return null;
    }

    public List<Ayudante> buscarPorProyecto(String codigoProyecto) {
        List<Ayudante> lista = new ArrayList<>();
        String sql = "SELECT m.* FROM MiembroFIS m " +
                     "INNER JOIN AsignacionAyudanteProyecto a ON m.numero_unico = a.numero_unico_ayudante " +
                     "WHERE a.codigo_proyecto = ? AND m.tipo_miembro = 'AYUDANTE' AND m.estado = 'ACTIVO'";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, codigoProyecto);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Ayudante ayudante = mapearAyudante(rs);
                    if (ayudante != null) lista.add(ayudante);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error buscando ayudantes por proyecto: " + e.getMessage());
        }
        return lista;
    }

    public List<Ayudante> buscarPorCarrera(String carrera) {
        List<Ayudante> lista = new ArrayList<>();
        String sql = "SELECT * FROM MiembroFIS WHERE carrera = ? AND tipo_miembro = 'AYUDANTE' AND estado = 'ACTIVO'";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, carrera);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Ayudante ayudante = mapearAyudante(rs);
                    if (ayudante != null) lista.add(ayudante);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error buscando ayudantes por carrera: " + e.getMessage());
        }
        return lista;
    }

    public List<Ayudante> buscarPorNivel(int nivel) {
        List<Ayudante> lista = new ArrayList<>();
        String sql = "SELECT * FROM MiembroFIS WHERE nivel = ? AND tipo_miembro = 'AYUDANTE' AND estado = 'ACTIVO'";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, nivel);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Ayudante ayudante = mapearAyudante(rs);
                    if (ayudante != null) lista.add(ayudante);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error buscando ayudantes por nivel: " + e.getMessage());
        }
        return lista;
    }

    public List<Ayudante> buscarActivos() {
        List<Ayudante> lista = new ArrayList<>();
        String sql = "SELECT * FROM MiembroFIS WHERE tipo_miembro = 'AYUDANTE' AND estado = 'ACTIVO'";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Ayudante ayudante = mapearAyudante(rs);
                if (ayudante != null) lista.add(ayudante);
            }
        } catch (SQLException e) {
            System.err.println("✗ Error buscando ayudantes activos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Object> listarTodos() {
        List<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM MiembroFIS WHERE tipo_miembro = 'AYUDANTE'";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Ayudante ayudante = mapearAyudante(rs);
                if (ayudante != null) lista.add(ayudante);
            }
        } catch (SQLException e) {
            System.err.println("✗ Error listando ayudantes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Object entidad) {
        return guardar(entidad);
    }

    @Override
    public boolean eliminar(String id) {
        String sql = "UPDATE MiembroFIS SET estado = 'INACTIVO' WHERE numero_unico = ? AND tipo_miembro = 'AYUDANTE'";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("✗ Error eliminando ayudante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int contarTodos() {
        String sql = "SELECT COUNT(*) as total FROM MiembroFIS WHERE tipo_miembro = 'AYUDANTE'";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error contando ayudantes: " + e.getMessage());
        }
        return 0;
    }

    public int contarActivos() {
        String sql = "SELECT COUNT(*) as total FROM MiembroFIS WHERE tipo_miembro = 'AYUDANTE' AND estado = 'ACTIVO'";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error contando ayudantes activos: " + e.getMessage());
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
     * Mapea un ResultSet a un objeto Ayudante
     */
    private Ayudante mapearAyudante(ResultSet rs) throws SQLException {
        Ayudante ayudante = new Ayudante();
        
        ayudante.setNumeroUnico(rs.getString("numero_unico"));
        ayudante.setCedula(rs.getString("cedula"));
        ayudante.setCorreoInstitucional(rs.getString("correo_institucional"));
        ayudante.setPassword(rs.getString("password"));
        ayudante.setNombres(rs.getString("nombres"));
        ayudante.setApellidos(rs.getString("apellidos"));
        ayudante.setTelefono(rs.getString("telefono"));
        ayudante.setCarrera(rs.getString("carrera"));
        ayudante.setNivel(rs.getInt("nivel"));
        ayudante.setPromedio(rs.getFloat("promedio"));
        ayudante.setEstado(rs.getString("estado"));
        
        return ayudante;
    }
}
