import java.sql.*;

/**
 * Clase para gestionar la conexión a la base de datos SQLite
 * Gestión de Ayudantes FIS-EPN
 */
public class DatabaseConnectionSQLite {
    
    // Ruta relativa a la base de datos
    private static final String DB_URL = "jdbc:sqlite:bd/gestion_ayudantes.db";
    
    /**
     * Establece una conexión a la base de datos SQLite
     * @return Conexión a la base de datos
     */
    public static Connection connect() {
        Connection conn = null;
        try {
            // Cargar el driver JDBC de SQLite
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);
            
            // Habilitar claves foráneas (deshabilitadas por defecto en SQLite)
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
            
            System.out.println("✓ Conexión exitosa a SQLite: gestion_ayudantes.db");
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Driver JDBC SQLite no encontrado. Asegúrate de tener:");
            System.err.println("  - sqlite-jdbc-3.44.0.0.jar en la carpeta /lib/");
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("✗ Error conectando a SQLite: " + e.getMessage());
        }
        return conn;
    }
    
    /**
     * Cierra una conexión a la base de datos
     * @param conn Conexión a cerrar
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("✓ Conexión cerrada");
            } catch (SQLException e) {
                System.err.println("✗ Error cerrando conexión: " + e.getMessage());
            }
        }
    }
    
    /**
     * Obtiene el total de directores activos
     * @return Número de directores activos
     */
    public static int getTotalDirectoresActivos() {
        String sql = "SELECT COUNT(*) as total FROM MiembroFIS WHERE tipo_miembro='DIRECTOR' AND estado='ACTIVO'";
        int total = 0;
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error: " + e.getMessage());
        }
        return total;
    }
    
    /**
     * Obtiene el total de ayudantes activos
     * @return Número de ayudantes activos
     */
    public static int getTotalAyudantesActivos() {
        String sql = "SELECT COUNT(*) as total FROM MiembroFIS WHERE tipo_miembro='AYUDANTE' AND estado='ACTIVO'";
        int total = 0;
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error: " + e.getMessage());
        }
        return total;
    }
    
    /**
     * Obtiene el total de proyectos activos
     * @return Número de proyectos activos
     */
    public static int getTotalProyectosActivos() {
        String sql = "SELECT COUNT(*) as total FROM ProyectoInvestigacion WHERE estado='ACTIVO'";
        int total = 0;
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error: " + e.getMessage());
        }
        return total;
    }
    
    /**
     * Obtiene información de un director por su número único
     * @param numeroUnico Número único del director
     * @return Array con datos: {nombres, apellidos, especialidad, email}
     */
    public static String[] getDirectorInfo(String numeroUnico) {
        String sql = "SELECT nombres, apellidos, especialidad_director, correo_institucional " +
                     "FROM MiembroFIS WHERE numero_unico = ? AND tipo_miembro = 'DIRECTOR'";
        
        String[] info = new String[4];
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, numeroUnico);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    info[0] = rs.getString("nombres");
                    info[1] = rs.getString("apellidos");
                    info[2] = rs.getString("especialidad_director");
                    info[3] = rs.getString("correo_institucional");
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error: " + e.getMessage());
        }
        return info;
    }
    
    /**
     * Obtiene todos los ayudantes activos de un proyecto
     * @param codigoProyecto Código del proyecto
     * @return ResultSet con nombres, apellidos, carrera y nivel
     */
    public static void getAyudantesDelProyecto(String codigoProyecto) {
        String sql = "SELECT m.nombres, m.apellidos, m.carrera, m.nivel, a.salario_por_hora " +
                     "FROM AsignacionAyudanteProyecto a " +
                     "JOIN MiembroFIS m ON a.ayudante_numero_unico = m.numero_unico " +
                     "WHERE a.proyecto_codigo = ? AND a.estado = 'ACTIVO'";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, codigoProyecto);
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\nAyudantes del proyecto: " + codigoProyecto);
                System.out.println("-------------------------------------------");
                while (rs.next()) {
                    System.out.printf("%s %s - %s (Nivel %d) - $%.2f/h%n",
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("carrera"),
                        rs.getInt("nivel"),
                        rs.getDouble("salario_por_hora"));
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error: " + e.getMessage());
        }
    }
    
    /**
     * Ejemplo de uso - Pruebas de conexión
     */
    public static void main(String[] args) {
        System.out.println("================================");
        System.out.println("SQLite Database Connection Test");
        System.out.println("================================\n");
        
        // Probar conexión básica
        try (Connection conn = connect()) {
            if (conn != null) {
                System.out.println("\n📊 Estadísticas de la Base de Datos:");
                System.out.println("-----------------------------------");
                System.out.println("Directores activos: " + getTotalDirectoresActivos());
                System.out.println("Ayudantes activos:  " + getTotalAyudantesActivos());
                System.out.println("Proyectos activos:  " + getTotalProyectosActivos());
                
                System.out.println("\n📋 Información de Director:");
                System.out.println("-----------------------------------");
                String[] directorInfo = getDirectorInfo("EPN001");
                if (directorInfo[0] != null) {
                    System.out.printf("Director: %s %s%n", directorInfo[0], directorInfo[1]);
                    System.out.printf("Especialidad: %s%n", directorInfo[2]);
                    System.out.printf("Email: %s%n", directorInfo[3]);
                }
                
                System.out.println();
                getAyudantesDelProyecto("PII-19-02");
                
                System.out.println("\n✓ Pruebas completadas exitosamente");
            }
        } catch (Exception e) {
            System.err.println("✗ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
