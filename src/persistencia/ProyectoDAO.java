package persistencia;

import dominio.ProyectoInvestigacion;
import dominio.Director;
import dominio.TipoProyecto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * DAO para ProyectoInvestigacion usando SQLite
 * Migración de serialización a base de datos
 */
public class ProyectoDAO implements IDAO {
    
    private static final String DB_URL = "jdbc:sqlite:bd/gestion_ayudantes.db";
    private DirectorDAO directorDAO;

    public ProyectoDAO() {
        this.directorDAO = new DirectorDAO();
    }

    @Override
    public boolean guardar(Object entidad) {
        if (!(entidad instanceof ProyectoInvestigacion)) {
            return false;
        }
        
        ProyectoInvestigacion proyecto = (ProyectoInvestigacion) entidad;
        String sql = "INSERT OR REPLACE INTO ProyectoInvestigacion " +
                     "(codigo_proyecto, nombre_proyecto, descripcion, fecha_inicio, fecha_fin, " +
                     "presupuesto, estado, tipo_proyecto, ayudantes_planificados, director_numero_unico) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, proyecto.getCodigoProyecto());
            pstmt.setString(2, proyecto.getNombreProyecto());
            pstmt.setString(3, proyecto.getDescripcion());
            
            if (proyecto.getFechaInicio() != null) {
                pstmt.setString(4, formatDate(proyecto.getFechaInicio()));
            } else {
                pstmt.setNull(4, Types.VARCHAR);
            }
            
            if (proyecto.getFechaFin() != null) {
                pstmt.setString(5, formatDate(proyecto.getFechaFin()));
            } else {
                pstmt.setNull(5, Types.VARCHAR);
            }
            
            pstmt.setFloat(6, proyecto.getPresupuesto());
            pstmt.setString(7, proyecto.getEstado());
            pstmt.setString(8, proyecto.getTipoProyecto() != null ? proyecto.getTipoProyecto().name() : null);
            pstmt.setInt(9, proyecto.getAyudantesPlanificados());
            
            if (proyecto.getDirector() != null) {
                pstmt.setString(10, proyecto.getDirector().getNumeroUnico());
            } else {
                pstmt.setNull(10, Types.VARCHAR);
            }
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("✗ Error guardando proyecto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Object buscarPorId(String codigo) {
        return buscarPorCodigo(codigo);
    }

    public ProyectoInvestigacion buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM ProyectoInvestigacion WHERE codigo_proyecto = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, codigo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearProyecto(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error buscando proyecto: " + e.getMessage());
        }
        return null;
    }

    public ProyectoInvestigacion buscarPorDirector(Director director) {
        if (director == null) return null;
        
        String sql = "SELECT * FROM ProyectoInvestigacion WHERE director_numero_unico = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, director.getNumeroUnico());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearProyecto(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error buscando proyecto por director: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Object> listarTodos() {
        List<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM ProyectoInvestigacion";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ProyectoInvestigacion p = mapearProyecto(rs);
                if (p != null) lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("✗ Error listando proyectos: " + e.getMessage());
        }
        return lista;
    }

    public List<ProyectoInvestigacion> listarActivos() {
        List<ProyectoInvestigacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM ProyectoInvestigacion WHERE estado = 'ACTIVO'";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ProyectoInvestigacion p = mapearProyecto(rs);
                if (p != null) lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("✗ Error listando proyectos activos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Object entidad) {
        return guardar(entidad);
    }

    @Override
    public boolean eliminar(String id) {
        String sql = "UPDATE ProyectoInvestigacion SET estado = 'CANCELADO' WHERE codigo_proyecto = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("✗ Error eliminando proyecto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int contarTodos() {
        String sql = "SELECT COUNT(*) as total FROM ProyectoInvestigacion";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error contando proyectos: " + e.getMessage());
        }
        return 0;
    }

    public int contarProyectosActivos() {
        String sql = "SELECT COUNT(*) as total FROM ProyectoInvestigacion WHERE estado = 'ACTIVO'";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error contando proyectos activos: " + e.getMessage());
        }
        return 0;
    }

    public boolean actualizarAyudantes(String proyectoId, List<String> numeroUnicos) {
        // Implementar actualización de asignaciones de ayudantes
        return true;
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
     * Mapea un ResultSet a un objeto ProyectoInvestigacion
     */
    private ProyectoInvestigacion mapearProyecto(ResultSet rs) throws SQLException {
        ProyectoInvestigacion proyecto = new ProyectoInvestigacion();
        
        proyecto.setCodigoProyecto(rs.getString("codigo_proyecto"));
        proyecto.setNombreProyecto(rs.getString("nombre_proyecto"));
        proyecto.setDescripcion(rs.getString("descripcion"));
        
        String fechaInicio = rs.getString("fecha_inicio");
        if (fechaInicio != null) {
            try {
                proyecto.setFechaInicio(parseDate(fechaInicio));
            } catch (ParseException e) {
                System.err.println("✗ Error parseando fecha inicio: " + e.getMessage());
            }
        }
        
        String fechaFin = rs.getString("fecha_fin");
        if (fechaFin != null) {
            try {
                proyecto.setFechaFin(parseDate(fechaFin));
            } catch (ParseException e) {
                System.err.println("✗ Error parseando fecha fin: " + e.getMessage());
            }
        }
        
        proyecto.setPresupuesto(rs.getFloat("presupuesto"));
        proyecto.setEstado(rs.getString("estado"));
        
        String tipoProyectoStr = rs.getString("tipo_proyecto");
        if (tipoProyectoStr != null) {
            try {
                proyecto.setTipoProyecto(TipoProyecto.valueOf(tipoProyectoStr));
            } catch (IllegalArgumentException e) {
                System.err.println("✗ Tipo proyecto inválido: " + tipoProyectoStr);
            }
        }
        
        proyecto.setAyudantesPlanificados(rs.getInt("ayudantes_planificados"));
        
        String numeroDirector = rs.getString("director_numero_unico");
        if (numeroDirector != null) {
            Director director = (Director) directorDAO.buscarPorId(numeroDirector);
            if (director != null) {
                proyecto.setDirector(director);
            }
        }
        
        return proyecto;
    }

    private String formatDate(Date date) {
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private Date parseDate(String dateStr) throws ParseException {
        if (dateStr == null || dateStr.isEmpty()) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateStr);
    }
}
