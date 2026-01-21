package model.dao;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de MiembroEPN
 */
public class MiembroEPNDAO implements IDAO<MiembroEPN> {
    private Connection conexion;

    public MiembroEPNDAO() {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }

    @Override
    public boolean guardar(MiembroEPN miembro) {
        String sql = "INSERT INTO miembros_epn (codigo_unico, cedula, correo_institucional, password, " +
                     "nombres, apellidos, telefono, rol, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, miembro.getCodigoUnico());
            ps.setString(2, miembro.getCedula());
            ps.setString(3, miembro.getCorreoInstitucional());
            ps.setString(4, miembro.getPassword());
            ps.setString(5, miembro.getNombres());
            ps.setString(6, miembro.getApellidos());
            ps.setString(7, miembro.getTelefono());
            ps.setString(8, miembro.getRol());
            ps.setString(9, miembro.getEstado());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al guardar miembro: " + e.getMessage());
            return false;
        }
    }

    @Override
    public MiembroEPN buscarPorId(String id) {
        String sql = "SELECT * FROM miembros_epn WHERE codigo_unico = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearMiembro(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar miembro: " + e.getMessage());
        }
        return null;
    }

    public MiembroEPN buscarPorCorreo(String correo) {
        String sql = "SELECT * FROM miembros_epn WHERE correo_institucional = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearMiembro(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por correo: " + e.getMessage());
        }
        return null;
    }

    public List<MiembroEPN> buscarPorRol(String rol) {
        List<MiembroEPN> miembros = new ArrayList<>();
        String sql = "SELECT * FROM miembros_epn WHERE rol = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, rol);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                miembros.add(mapearMiembro(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por rol: " + e.getMessage());
        }
        return miembros;
    }

    @Override
    public List<MiembroEPN> listarTodos() {
        List<MiembroEPN> miembros = new ArrayList<>();
        String sql = "SELECT * FROM miembros_epn";
        
        try (Statement st = conexion.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                miembros.add(mapearMiembro(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar miembros: " + e.getMessage());
        }
        return miembros;
    }

    @Override
    public boolean actualizar(MiembroEPN miembro) {
        String sql = "UPDATE miembros_epn SET cedula = ?, correo_institucional = ?, password = ?, " +
                     "nombres = ?, apellidos = ?, telefono = ?, rol = ?, estado = ? WHERE codigo_unico = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, miembro.getCedula());
            ps.setString(2, miembro.getCorreoInstitucional());
            ps.setString(3, miembro.getPassword());
            ps.setString(4, miembro.getNombres());
            ps.setString(5, miembro.getApellidos());
            ps.setString(6, miembro.getTelefono());
            ps.setString(7, miembro.getRol());
            ps.setString(8, miembro.getEstado());
            ps.setString(9, miembro.getCodigoUnico());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar miembro: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(String id) {
        String sql = "DELETE FROM miembros_epn WHERE codigo_unico = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar miembro: " + e.getMessage());
            return false;
        }
    }

    private MiembroEPN mapearMiembro(ResultSet rs) throws SQLException {
        MiembroEPN miembro = new MiembroEPN() {};
        miembro.setCodigoUnico(rs.getString("codigo_unico"));
        miembro.setCedula(rs.getString("cedula"));
        miembro.setCorreoInstitucional(rs.getString("correo_institucional"));
        miembro.setPassword(rs.getString("password"));
        miembro.setNombres(rs.getString("nombres"));
        miembro.setApellidos(rs.getString("apellidos"));
        miembro.setTelefono(rs.getString("telefono"));
        miembro.setRol(rs.getString("rol"));
        miembro.setEstado(rs.getString("estado"));
        return miembro;
    }
}
