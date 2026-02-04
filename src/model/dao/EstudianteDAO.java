package model.dao;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Estudiante
 */
public class EstudianteDAO implements IDAO<Estudiante> {
    private Connection conexion;
    private MiembroEPNDAO miembroDAO;
    private String lastError;

    public EstudianteDAO() {
        this.conexion = ConexionBD.getInstancia().getConexion();
        this.miembroDAO = new MiembroEPNDAO();
        this.lastError = "";
    }

    public String getLastError() {
        return lastError;
    }

    @Override
    public boolean guardar(Estudiante estudiante) {
        lastError = "";
        // First, save to miembros_epn (password is already "N/A", rol "ESTUDIANTE", estado "ACTIVO")
        if (!miembroDAO.guardar(estudiante)) {
            lastError = "Error al guardar en miembros_epn";
            return false;
        }
        
        // Then save to estudiantes
        String sql = "INSERT INTO estudiantes (codigo_unico, cedula, correo_institucional, " +
                     "nombres, apellidos, telefono, carrera, ira, nivel) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, estudiante.getCodigoUnico());
            ps.setString(2, estudiante.getCedula());
            ps.setString(3, estudiante.getCorreoInstitucional());
            ps.setString(4, estudiante.getNombres());
            ps.setString(5, estudiante.getApellidos());
            ps.setString(6, estudiante.getTelefono());
            ps.setString(7, estudiante.getCarrera());
            ps.setFloat(8, estudiante.getIRA());
            ps.setInt(9, estudiante.getNivel());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            lastError = e.getMessage();
            System.out.println("Error al guardar estudiante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Estudiante buscarPorId(String id) {
        String sql = "SELECT * FROM estudiantes WHERE codigo_unico = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearEstudiante(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar estudiante: " + e.getMessage());
        }
        return null;
    }

    public Estudiante buscarPorCodigo(String codigo) {
        return buscarPorId(codigo);
    }

    public Estudiante buscarPorCedula(String cedula) {
        String sql = "SELECT * FROM estudiantes WHERE cedula = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearEstudiante(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por cédula: " + e.getMessage());
        }
        return null;
    }

    public Estudiante buscarPorCriterio(String criterio) {
        // Intenta buscar primero por código, luego por cédula
        Estudiante estudiante = buscarPorCodigo(criterio);
        if (estudiante == null) {
            estudiante = buscarPorCedula(criterio);
        }
        return estudiante;
    }

    public List<Estudiante> buscarElegibles() {
        List<Estudiante> estudiantes = new ArrayList<>();
        String sql = "SELECT * FROM estudiantes WHERE ira >= 24 AND nivel >= 3";
        
        try (Statement st = conexion.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                estudiantes.add(mapearEstudiante(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar elegibles: " + e.getMessage());
        }
        return estudiantes;
    }

    @Override
    public List<Estudiante> listarTodos() {
        List<Estudiante> estudiantes = new ArrayList<>();
        String sql = "SELECT * FROM estudiantes";
        
        try (Statement st = conexion.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                estudiantes.add(mapearEstudiante(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar estudiantes: " + e.getMessage());
        }
        return estudiantes;
    }

    @Override
    public boolean actualizar(Estudiante estudiante) {
        String sql = "UPDATE estudiantes SET cedula = ?, correo_institucional = ?, " +
                     "nombres = ?, apellidos = ?, telefono = ?, carrera = ?, ira = ?, nivel = ? " +
                     "WHERE codigo_unico = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, estudiante.getCedula());
            ps.setString(2, estudiante.getCorreoInstitucional());
            ps.setString(3, estudiante.getNombres());
            ps.setString(4, estudiante.getApellidos());
            ps.setString(5, estudiante.getTelefono());
            ps.setString(6, estudiante.getCarrera());
            ps.setFloat(7, estudiante.getIRA());
            ps.setInt(8, estudiante.getNivel());
            ps.setString(9, estudiante.getCodigoUnico());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar estudiante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(String id) {
        String sql = "DELETE FROM estudiantes WHERE codigo_unico = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar estudiante: " + e.getMessage());
            return false;
        }
    }

    private Estudiante mapearEstudiante(ResultSet rs) throws SQLException {
        Estudiante estudiante = new Estudiante();
        estudiante.setCodigoUnico(rs.getString("codigo_unico"));
        estudiante.setCedula(rs.getString("cedula"));
        estudiante.setCorreoInstitucional(rs.getString("correo_institucional"));
        estudiante.setNombres(rs.getString("nombres"));
        estudiante.setApellidos(rs.getString("apellidos"));
        estudiante.setTelefono(rs.getString("telefono"));
        estudiante.setCarrera(rs.getString("carrera"));
        estudiante.setIRA(rs.getFloat("ira"));
        estudiante.setNivel(rs.getInt("nivel"));
        return estudiante;
    }
}
