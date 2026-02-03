package model.dao;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TecnicoDAO implements IDAO<TecnicoInvestigacion> {

    private Connection conexion;

    public TecnicoDAO() {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }

    @Override
    public boolean guardar(TecnicoInvestigacion tecnico) {

        String sql = """
            INSERT INTO tecnicos (
                id_tecnico, cedula, correo_electronico,
                nombres, apellidos, telefono,
                especialidad_tecnica, anios_experiencia, empresa_origen,
                horas_semanales, salario_mensual,
                estado, fecha_registro, codigo_proyecto
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, tecnico.getIdTecnico());
            ps.setString(2, tecnico.getCedula());
            ps.setString(3, tecnico.getCorreoElectronico());
            ps.setString(4, tecnico.getNombres());
            ps.setString(5, tecnico.getApellidos());
            ps.setString(6, tecnico.getTelefono());
            ps.setString(7, tecnico.getEspecialidadTecnica());
            ps.setInt(8, tecnico.getAniosExperiencia());
            ps.setString(9, tecnico.getEmpresaOrigen());
            ps.setInt(10, tecnico.getHorasSemanales());
            ps.setDouble(11, tecnico.getSalarioMensual());
            ps.setString(12, tecnico.getEstado() != null ? tecnico.getEstado() : "ACTIVO");
            ps.setTimestamp(13, new Timestamp(tecnico.getFechaRegistro().getTime()));
            ps.setString(14,
                    tecnico.getProyectoAsignado() != null
                            ? tecnico.getProyectoAsignado().getCodigoProyecto()
                            : null
            );

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✓ Técnico guardado en tabla TECNICOS: " + tecnico.getIdTecnico());
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.out.println("✗ Error al guardar técnico: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public TecnicoInvestigacion buscarPorId(String id) {
        String sql = "SELECT * FROM tecnicos WHERE id_tecnico = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearTecnico(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar técnico: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<TecnicoInvestigacion> listarTodos() {
        List<TecnicoInvestigacion> tecnicos = new ArrayList<>();
        String sql = "SELECT * FROM tecnicos";

        try (Statement st = conexion.createStatement()) {
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                tecnicos.add(mapearTecnico(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar técnicos: " + e.getMessage());
        }
        return tecnicos;
    }

    public List<TecnicoInvestigacion> buscarPorProyecto(String codigoProyecto) {
        List<TecnicoInvestigacion> tecnicos = new ArrayList<>();
        String sql = "SELECT * FROM tecnicos WHERE codigo_proyecto = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, codigoProyecto);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tecnicos.add(mapearTecnico(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar técnicos por proyecto: " + e.getMessage());
        }
        return tecnicos;
    }

    @Override
    public boolean actualizar(TecnicoInvestigacion tecnico) {

        String sql = """
            UPDATE tecnicos SET
                cedula = ?,
                correo_electronico = ?,
                nombres = ?,
                apellidos = ?,
                telefono = ?,
                especialidad_tecnica = ?,
                anios_experiencia = ?,
                empresa_origen = ?,
                horas_semanales = ?,
                salario_mensual = ?,
                estado = ?,
                fecha_finalizacion = ?,
                motivo_salida = ?,
                codigo_proyecto = ?
            WHERE id_tecnico = ?
        """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, tecnico.getCedula());
            ps.setString(2, tecnico.getCorreoElectronico());
            ps.setString(3, tecnico.getNombres());
            ps.setString(4, tecnico.getApellidos());
            ps.setString(5, tecnico.getTelefono());
            ps.setString(6, tecnico.getEspecialidadTecnica());
            ps.setInt(7, tecnico.getAniosExperiencia());
            ps.setString(8, tecnico.getEmpresaOrigen());
            ps.setInt(9, tecnico.getHorasSemanales());
            ps.setDouble(10, tecnico.getSalarioMensual());
            ps.setString(11, tecnico.getEstado());
            ps.setTimestamp(12,
                    tecnico.getFechaFinalizacion() != null
                            ? new Timestamp(tecnico.getFechaFinalizacion().getTime())
                            : null
            );
            ps.setString(13, tecnico.getMotivoSalida());
            ps.setString(14,
                    tecnico.getProyectoAsignado() != null
                            ? tecnico.getProyectoAsignado().getCodigoProyecto()
                            : null
            );
            ps.setString(15, tecnico.getIdTecnico());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar técnico: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(String id) {
        // IMPORTANTE: Soft delete - Solo marca como INACTIVO
        // NO elimina el registro físicamente
        String sql = """
            UPDATE tecnicos
            SET estado = 'INACTIVO',
                fecha_finalizacion = CURRENT_TIMESTAMP,
                motivo_salida = ?
            WHERE id_tecnico = ?
        """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, "Baja del sistema");
            ps.setString(2, id);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("✓ Técnico dado de baja (soft delete): " + id);
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.out.println("✗ Error al dar de baja técnico: " + e.getMessage());
            return false;
        }
    }

    private TecnicoInvestigacion mapearTecnico(ResultSet rs) throws SQLException {

        TecnicoInvestigacion tecnico = new TecnicoInvestigacion();
        tecnico.setIdTecnico(rs.getString("id_tecnico"));
        tecnico.setCedula(rs.getString("cedula"));
        tecnico.setCorreoElectronico(rs.getString("correo_electronico"));
        tecnico.setNombres(rs.getString("nombres"));
        tecnico.setApellidos(rs.getString("apellidos"));
        tecnico.setTelefono(rs.getString("telefono"));
        tecnico.setEspecialidadTecnica(rs.getString("especialidad_tecnica"));
        tecnico.setAniosExperiencia(rs.getInt("anios_experiencia"));
        tecnico.setEmpresaOrigen(rs.getString("empresa_origen"));
        tecnico.setHorasSemanales(rs.getInt("horas_semanales"));
        tecnico.setSalarioMensual(rs.getDouble("salario_mensual"));
        tecnico.setEstado(rs.getString("estado"));

        if (rs.getTimestamp("fecha_registro") != null) {
            tecnico.setFechaRegistro(new java.util.Date(rs.getTimestamp("fecha_registro").getTime()));
        }

        if (rs.getTimestamp("fecha_finalizacion") != null) {
            tecnico.setFechaFinalizacion(new java.util.Date(rs.getTimestamp("fecha_finalizacion").getTime()));
        }

        tecnico.setMotivoSalida(rs.getString("motivo_salida"));

        String codigoProyecto = rs.getString("codigo_proyecto");
        if (codigoProyecto != null && !codigoProyecto.isBlank()) {
            try {
                ProyectoDAO proyectoDAO = new ProyectoDAO();
                ProyectoInvestigacion proyecto = proyectoDAO.buscarPorId(codigoProyecto);
                tecnico.setProyectoAsignado(proyecto);
            } catch (Exception ignored) {}
        }

        return tecnico;
    }
}
