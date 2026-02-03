package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Clase para gestionar la conexión a la base de datos SQLite
 */
public class ConexionBD {
    private static ConexionBD instancia;
    private Connection conexion;
    
    // Datos de conexión SQLite
    private static final String URL = "jdbc:sqlite:./bd/gestion_ayudantes.db";
    private static final String DRIVER = "org.sqlite.JDBC";

    private ConexionBD() {
    }

    /**
     * Obtiene la instancia única de la conexión (Singleton)
     */
    public static ConexionBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    /**
     * Establece la conexión a la base de datos SQLite
     */
    public void conectar() {
        try {
            Class.forName(DRIVER);
            conexion = DriverManager.getConnection(URL);
            System.out.println("✓ Conexión a BD SQLite exitosa");
            inicializarBaseDatos();
        } catch (ClassNotFoundException e) {
            System.out.println("✗ Error: Driver SQLite no encontrado - " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("✗ Error de conexión a BD - " + e.getMessage());
        }
    }

    /**
     * Inicializa las tablas si no existen y aplica pequeñas migraciones necesarias
     */
    private void inicializarBaseDatos() {
        try (var statement = conexion.createStatement()) {
            // Habilitar claves foráneas en SQLite
            statement.execute("PRAGMA foreign_keys = ON");
            System.out.println("✓ Foreign keys habilitadas");

            // Asegurar que la columna categoria_proyecto exista en las tablas relevantes
            String[] tablas = {"proyectos", "ProyectoInvestigacion"};
            for (String tabla : tablas) {
                try (ResultSet rs = statement.executeQuery("PRAGMA table_info('" + tabla + "')")) {
                    boolean found = false;
                    while (rs.next()) {
                        String name = rs.getString("name");
                        if ("categoria_proyecto".equalsIgnoreCase(name)) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        try {
                            statement.execute("ALTER TABLE " + tabla + " ADD COLUMN categoria_proyecto TEXT NOT NULL DEFAULT 'INVESTIGACION'");
                            System.out.println("✓ Columna 'categoria_proyecto' agregada a tabla " + tabla);
                        } catch (SQLException ex) {
                            System.out.println("✗ No se pudo agregar columna 'categoria_proyecto' a " + tabla + " - " + ex.getMessage());
                        }

                        // Actualizar categorías basadas en tipo_proyecto para registros existentes
                        try {
                            String updateSql = "UPDATE " + tabla + " SET categoria_proyecto = CASE " +
                                " WHEN tipo_proyecto IN ('VINCULACION_CON_FINANCIAMIENTO','VINCULACION') THEN 'VINCULACION' " +
                                " WHEN tipo_proyecto IN ('TRANSFERENCIA','TRANSFERENCIA_TECNOLOGICA') THEN 'TRANSFERENCIA_TECNOLOGICA' " +
                                " ELSE 'INVESTIGACION' END";
                            statement.executeUpdate(updateSql);
                            System.out.println("✓ Categorías actualizadas en " + tabla);
                        } catch (SQLException ex2) {
                            System.out.println("Aviso: no se pudo actualizar categorias para " + tabla + " - " + ex2.getMessage());
                        }
                    }
                } catch (SQLException e) {
                    // Tabla no existe en este esquema; ignorar
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al inicializar BD: " + e.getMessage());
        }
    }

    /**
     * Obtiene la conexión actual
     */
    public Connection getConexion() {
        if (conexion == null) {
            conectar();
        }
        return conexion;
    }

    /**
     * Cierra la conexión a la base de datos
     */
    public void desconectar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("✓ Desconexión de BD exitosa");
            }
        } catch (SQLException e) {
            System.out.println("✗ Error al desconectar - " + e.getMessage());
        }
    }

    /**
     * Verifica si la conexión está activa
     */
    public boolean estaConectado() {
        try {
            return conexion != null && !conexion.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
