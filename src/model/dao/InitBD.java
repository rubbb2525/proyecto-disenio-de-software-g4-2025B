package model.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Inicializador de la base de datos SQLite
 * Ejecuta los scripts SQL para crear tablas e insertar datos de prueba
 * Actualizado para incluir migración de asistentes y técnicos
 */
public class InitBD {
    
    public static void inicializar() {
        Connection conn = ConexionBD.getInstancia().getConexion();
        
        if (conn == null) {
            System.out.println("✗ Error: No hay conexión a BD");
            return;
        }
        
        try {
            System.out.println("\n=== INICIALIZANDO BASE DE DATOS ===\n");
            
            // Habilitar foreign keys
            Statement stmt = conn.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.close();
            
            // 1. Ejecutar el script principal de schema
            System.out.println("→ Ejecutando script principal (schema_bd.sql)...");
            ejecutarScript(conn, "schema_bd.sql");
            
            // 2. Ejecutar el script de migración de asistentes y técnicos
            System.out.println("\n→ Ejecutando script de migración (migracion_asistentes_tecnicos_sqlite.sql)...");
            ejecutarScript(conn, "bd/migracion_asistentes_tecnicos_sqlite.sql");
            
            System.out.println("\n✓ Base de datos inicializada correctamente\n");
            
        } catch (Exception e) {
            System.out.println("✗ Error al inicializar BD: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Ejecuta un script SQL desde un archivo
     * @param conn Conexión a la base de datos
     * @param scriptPath Ruta del archivo SQL a ejecutar
     */
    private static void ejecutarScript(Connection conn, String scriptPath) {
        try {
            String sqlScript = new String(Files.readAllBytes(Paths.get(scriptPath)));
            
            // Dividir por puntos y coma (simple parser)
            String[] statements = sqlScript.split(";");
            
            Statement stmt = conn.createStatement();
            int tablas = 0;
            int inserts = 0;
            int vistas = 0;
            int indices = 0;
            
            for (String statement : statements) {
                String sql = statement.trim();

                // Elimina comentarios de línea al inicio para no saltar sentencias válidas
                while (sql.startsWith("--")) {
                    int salto = sql.indexOf('\n');
                    if (salto == -1) {
                        sql = ""; // solo era comentario
                        break;
                    }
                    sql = sql.substring(salto + 1).trim();
                }

                if (sql.isEmpty() || sql.startsWith("PRAGMA")) {
                    continue;
                }
                
                try {
                    stmt.execute(sql);
                    
                    String sqlUpper = sql.toUpperCase();
                    if (sqlUpper.contains("CREATE TABLE")) {
                        tablas++;
                    } else if (sqlUpper.contains("INSERT")) {
                        inserts++;
                    } else if (sqlUpper.contains("CREATE VIEW")) {
                        vistas++;
                    } else if (sqlUpper.contains("CREATE INDEX")) {
                        indices++;
                    }
                } catch (SQLException e) {
                    if (!e.getMessage().contains("already exists")) {
                        System.out.println("⚠ Advertencia en " + scriptPath + ": " + e.getMessage());
                    }
                }
            }
            
            stmt.close();
            
            System.out.println("  ✓ Tablas creadas: " + tablas);
            if (vistas > 0) System.out.println("  ✓ Vistas creadas: " + vistas);
            if (indices > 0) System.out.println("  ✓ Índices creados: " + indices);
            System.out.println("  ✓ Registros insertados: " + inserts);
            
        } catch (Exception e) {
            System.out.println("✗ Error al ejecutar " + scriptPath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Verifica si las tablas existen
     */
    public static boolean verificarTablas() {
        Connection conn = ConexionBD.getInstancia().getConexion();
        
        if (conn == null) return false;
        
        try {
            var rs = conn.getMetaData().getTables(null, null, "miembros_epn", null);
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }
}