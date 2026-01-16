package persistencia;

import dominio.MiembroFIS;
import dominio.Director;
import dominio.Ayudante;
import dominio.JefaDepartamento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para MiembroFIS usando SQLite
 * Migración de serialización a base de datos
 */
public class MiembroFISDAO implements IDAO {
    
    // Cadena de conexión a SQLite
    private static final String DB_URL = "jdbc:sqlite:bd/gestion_ayudantes.db";

    public MiembroFISDAO() {
        // Constructor vacío - la BD ya está creada
    }

    @Override
    public boolean guardar(Object entidad) {
        if (!(entidad instanceof MiembroFIS)) {
            return false;
        }
        
        MiembroFIS miembro = (MiembroFIS) entidad;
        String tipoMiembro = "JEFE_DEPARTAMENTO"; // default
        String especialidad = null;
        String carrera = null;
        Integer nivel = null;
        Float promedio = null;
        
        if (miembro instanceof Director) {
            tipoMiembro = "DIRECTOR";
            Director dir = (Director) miembro;
            especialidad = dir.getEspecialidad();
        } else if (miembro instanceof Ayudante) {
            tipoMiembro = "AYUDANTE";
            Ayudante ayudante = (Ayudante) miembro;
            carrera = ayudante.getCarrera();
            nivel = ayudante.getNivel();
            promedio = ayudante.getPromedio();
        }
        
        String sql = "INSERT OR REPLACE INTO MiembroFIS " +
                     "(numero_unico, cedula, correo_institucional, password, nombres, apellidos, " +
                     "telefono, tipo_miembro, especialidad_director, carrera, nivel, promedio, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, miembro.getNumeroUnico());
            pstmt.setString(2, miembro.getCedula());
            pstmt.setString(3, miembro.getCorreoInstitucional());
            pstmt.setString(4, miembro.getPassword());
            pstmt.setString(5, miembro.getNombres());
            pstmt.setString(6, miembro.getApellidos());
            pstmt.setString(7, miembro.getTelefono());
            pstmt.setString(8, tipoMiembro);
            pstmt.setString(9, especialidad);
            pstmt.setString(10, carrera);
            if (nivel != null) {
                pstmt.setInt(11, nivel);
            } else {
                pstmt.setNull(11, Types.INTEGER);
            }
            if (promedio != null) {
                pstmt.setFloat(12, promedio);
            } else {
                pstmt.setNull(12, Types.REAL);
            }
            pstmt.setString(13, miembro.getEstado());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("✗ Error guardando miembro: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Object buscarPorId(String id) {
        String sql = "SELECT * FROM MiembroFIS WHERE numero_unico = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearMiembroFIS(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error buscando miembro: " + e.getMessage());
        }
        return null;
    }

    public MiembroFIS buscarPorCorreo(String correo) {
        String sql = "SELECT * FROM MiembroFIS WHERE correo_institucional = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, correo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearMiembroFIS(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error buscando por correo: " + e.getMessage());
        }
        return null;
    }

    public MiembroFIS validarCredenciales(String correo, String password) {
        MiembroFIS miembro = buscarPorCorreo(correo);
        if (miembro != null && miembro.autenticar(password)) {
            return miembro;
        }
        return null;
    }

    @Override
    public List<Object> listarTodos() {
        List<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM MiembroFIS WHERE estado = 'ACTIVO'";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lista.add(mapearMiembroFIS(rs));
            }
        } catch (SQLException e) {
            System.err.println("✗ Error listando miembros: " + e.getMessage());
        }
        return lista;
    }

    public List<Director> listarDirectores() {
        List<Director> lista = new ArrayList<>();
        String sql = "SELECT * FROM MiembroFIS WHERE tipo_miembro = 'DIRECTOR' AND estado = 'ACTIVO'";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                MiembroFIS miembro = mapearMiembroFIS(rs);
                if (miembro instanceof Director) {
                    lista.add((Director) miembro);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error listando directores: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Object entidad) {
        return guardar(entidad); // INSERT OR REPLACE lo maneja automáticamente
    }

    @Override
    public boolean eliminar(String id) {
        String sql = "UPDATE MiembroFIS SET estado = 'BAJA' WHERE numero_unico = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("✗ Error eliminando miembro: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int contarTodos() {
        String sql = "SELECT COUNT(*) as total FROM MiembroFIS WHERE estado = 'ACTIVO'";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error contando miembros: " + e.getMessage());
        }
        return 0;
    }

    public int contarPorRol(String rol) {
        String sql = "SELECT COUNT(*) as total FROM MiembroFIS WHERE tipo_miembro = ? AND estado = 'ACTIVO'";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, rol);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error contando por rol: " + e.getMessage());
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
     * Mapea un ResultSet a un objeto MiembroFIS
     */
    private MiembroFIS mapearMiembroFIS(ResultSet rs) throws SQLException {
        String tipoMiembro = rs.getString("tipo_miembro");
        MiembroFIS miembro = null;
        
        if ("DIRECTOR".equals(tipoMiembro)) {
            Director director = new Director();
            director.setEspecialidad(rs.getString("especialidad_director"));
            miembro = director;
        } else if ("AYUDANTE".equals(tipoMiembro)) {
            Ayudante ayudante = new Ayudante();
            ayudante.setCarrera(rs.getString("carrera"));
            ayudante.setNivel(rs.getInt("nivel"));
            ayudante.setPromedio(rs.getFloat("promedio"));
            miembro = ayudante;
        } else if ("JEFE_DEPARTAMENTO".equals(tipoMiembro)) {
            JefaDepartamento jefa = JefaDepartamento.getInstancia();
            jefa.setEspecialidad(rs.getString("especialidad_director"));
            miembro = jefa;
        } else {
            // Tipo desconocido
            System.err.println("✗ Tipo de miembro desconocido: " + tipoMiembro);
            return null;
        }
        
        miembro.setNumeroUnico(rs.getString("numero_unico"));
        miembro.setCedula(rs.getString("cedula"));
        miembro.setCorreoInstitucional(rs.getString("correo_institucional"));
        miembro.setPassword(rs.getString("password"));
        miembro.setNombres(rs.getString("nombres"));
        miembro.setApellidos(rs.getString("apellidos"));
        miembro.setTelefono(rs.getString("telefono"));
        miembro.setEstado(rs.getString("estado"));
        
        return miembro;
    }
}
