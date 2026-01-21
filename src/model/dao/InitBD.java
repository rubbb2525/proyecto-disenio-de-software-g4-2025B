package model.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Inicializador de la base de datos SQLite
 * Ejecuta el script SQL para crear tablas e insertar datos de prueba
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
            
            // Leer y ejecutar el script SQL
            String scriptPath = "schema_bd.sql";
            String sqlScript = new String(Files.readAllBytes(Paths.get(scriptPath)));
            
            // Dividir por puntos y coma (simple parser)
            String[] statements = sqlScript.split(";");
            
            stmt = conn.createStatement();
            int tablas = 0;
            int inserts = 0;
            
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
                    
                    if (sql.toUpperCase().contains("CREATE TABLE")) {
                        tablas++;
                    } else if (sql.toUpperCase().contains("INSERT")) {
                        inserts++;
                    }
                } catch (SQLException e) {
                    if (!e.getMessage().contains("already exists")) {
                        System.out.println("⚠ Advertencia: " + e.getMessage());
                    }
                }
            }
            
            stmt.close();
            
            System.out.println("✓ Tablas creadas: " + tablas);
            System.out.println("✓ Registros insertados: " + inserts);
            System.out.println("\n✓ Base de datos inicializada correctamente\n");
            
        } catch (Exception e) {
            System.out.println("✗ Error al inicializar BD: " + e.getMessage());
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
