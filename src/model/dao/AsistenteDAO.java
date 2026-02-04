package model.dao;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Asistente de Investigación
 * CORREGIDO: NO modifica el rol en miembros_epn
 */
public class AsistenteDAO implements IDAO<AsistenteInvestigacion> {

    private Connection conexion;

    public AsistenteDAO() {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }

    @Override
    public boolean guardar(AsistenteInvestigacion asistente) {
        // CORREGIDO: NO actualizar rol en miembros_epn
        // El estudiante sigue siendo ESTUDIANTE incluso cuando es asistente
        
        String sql = """
            INSERT INTO asistentes (
                codigo_unico, cedula, correo_institucional,
                nombres, apellidos, telefono,
                carrera, nivel, ira,
                titulo_academico, area_especializacion,
                horas_semanales, salario_mensual,
                estado, fecha_registro, codigo_proyecto
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, asistente.getCodigoUnico());
            ps.setString(2, asistente.getCedula());
            ps.setString(3, asistente.getCorreoInstitucional());
            ps.setString(4, asistente.getNombres());
            ps.setString(5, asistente.getApellidos());
            ps.setString(6, asistente.getTelefono());
            ps.setString(7, asistente.getCarrera());
            ps.setInt(8, asistente.getNivel());
            ps.setFloat(9, asistente.getIRA());
            ps.setString(10, asistente.getTituloAcademico());
            ps.setString(11, asistente.getAreaEspecializacion());
            ps.setInt(12, asistente.getHorasSemanales());
            ps.setDouble(13, asistente.getSalarioMensual());
            ps.setString(14, "ACTIVO");
            ps.setTimestamp(15, new Timestamp(asistente.getFechaRegistro().getTime()));
            ps.setString(16,
                    asistente.getProyectoAsignado() != null
                            ? asistente.getProyectoAsignado().getCodigoProyecto()
                            : null);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✓ Asistente guardado en tabla ASISTENTES: " + asistente.getCodigoUnico());
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.out.println("✗ Error al guardar asistente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public AsistenteInvestigacion buscarPorId(String id) {
        String sql = "SELECT * FROM asistentes WHERE codigo_unico = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.out.println("Error al buscar asistente: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<AsistenteInvestigacion> listarTodos() {
        List<AsistenteInvestigacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM asistentes";

        try (Statement st = conexion.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Error al listar asistentes: " + e.getMessage());
        }
        return lista;
    }

    public List<AsistenteInvestigacion> buscarPorProyecto(String codigoProyecto) {
        List<AsistenteInvestigacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM asistentes WHERE codigo_proyecto = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, codigoProyecto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Error al buscar asistentes por proyecto: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(AsistenteInvestigacion a) {
        String sql = """
            UPDATE asistentes SET
            titulo_academico = ?, area_especializacion = ?,
            horas_semanales = ?, salario_mensual = ?, 
            estado = ?, fecha_finalizacion = ?, motivo_salida = ?
            WHERE codigo_unico = ?
        """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, a.getTituloAcademico());
            ps.setString(2, a.getAreaEspecializacion());
            ps.setInt(3, a.getHorasSemanales());
            ps.setDouble(4, a.getSalarioMensual());
            ps.setString(5, a.getEstado());
            ps.setTimestamp(6, a.getFechaFinalizacion() != null 
                ? new Timestamp(a.getFechaFinalizacion().getTime()) : null);
            ps.setString(7, a.getMotivoSalida());
            ps.setString(8, a.getCodigoUnico());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar asistente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(String id) {
        // IMPORTANTE: Soft delete - Solo marca como INACTIVO en tabla asistentes
        // NO elimina de tabla estudiantes, NO modifica rol en miembros_epn
        String sql = """
            UPDATE asistentes
            SET estado = 'INACTIVO',
                fecha_finalizacion = CURRENT_TIMESTAMP,
                motivo_salida = ?
            WHERE codigo_unico = ?
        """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, "Baja del sistema");
            ps.setString(2, id);
            int rows = ps.executeUpdate();
            
            if (rows > 0) {
                System.out.println("✓ Asistente dado de baja (soft delete, estudiante preservado): " + id);
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("✗ Error al dar de baja asistente: " + e.getMessage());
            return false;
        }
    }

    private AsistenteInvestigacion mapear(ResultSet rs) throws SQLException {
        AsistenteInvestigacion a = new AsistenteInvestigacion();
        a.setCodigoUnico(rs.getString("codigo_unico"));
        a.setCedula(rs.getString("cedula"));
        a.setCorreoInstitucional(rs.getString("correo_institucional"));
        a.setNombres(rs.getString("nombres"));
        a.setApellidos(rs.getString("apellidos"));
        a.setTelefono(rs.getString("telefono"));
        a.setCarrera(rs.getString("carrera"));
        a.setNivel(rs.getInt("nivel"));
        a.setIRA(rs.getFloat("ira"));
        a.setTituloAcademico(rs.getString("titulo_academico"));
        a.setAreaEspecializacion(rs.getString("area_especializacion"));
        a.setHorasSemanales(rs.getInt("horas_semanales"));
        a.setSalarioMensual(rs.getDouble("salario_mensual"));
        a.setEstado(rs.getString("estado"));
        
        if (rs.getTimestamp("fecha_registro") != null) {
            a.setFechaRegistro(new java.util.Date(rs.getTimestamp("fecha_registro").getTime()));
        }
        
        if (rs.getTimestamp("fecha_finalizacion") != null) {
            a.setFechaFinalizacion(new java.util.Date(rs.getTimestamp("fecha_finalizacion").getTime()));
        }
        
        a.setMotivoSalida(rs.getString("motivo_salida"));
        
        String codigoProyecto = rs.getString("codigo_proyecto");
        if (codigoProyecto != null && !codigoProyecto.isBlank()) {
            try {
                ProyectoDAO proyectoDAO = new ProyectoDAO();
                Proyectos proyecto = proyectoDAO.buscarPorId(codigoProyecto);
                a.setProyectoAsignado(proyecto);
            } catch (Exception ignored) {}
        }
        
        return a;
    }
}
