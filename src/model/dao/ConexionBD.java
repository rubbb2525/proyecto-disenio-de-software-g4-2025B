package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para gestionar la conexión a la base de datos SQLite
 */
public class ConexionBD {
    private static ConexionBD instancia;
    private Connection conexion;
    
    // Datos de conexión SQLite
    private static final String URL = "jdbc:sqlite:gestion_ayudantes.db";
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
     * Inicializa las tablas si no existen
     */
    private void inicializarBaseDatos() {
        try (var statement = conexion.createStatement()) {
            // Habilitar claves foráneas en SQLite
            statement.execute("PRAGMA foreign_keys = ON");
            System.out.println("✓ Foreign keys habilitadas");
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
