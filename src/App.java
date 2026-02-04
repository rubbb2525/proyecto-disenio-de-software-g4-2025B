import model.*;
import model.dao.*;
import controller.*;
import view.*;

/**
 * Clase principal de la aplicación
 * 
 * REFACTORIZACIÓN:
 * - No llama a director.setProyectoAsignado() (Director ya no mantiene proyecto)
 * - El proyecto se obtiene via DAO cuando sea necesario (Lazy Loading)
 * - Importa servicios para que estén disponibles
 */
public class App {
    private static MiembroEPNDAO miembroDAO;
    private static AyudanteDAO ayudanteDAO;
    private static EstudianteDAO estudianteDAO;
    private static ProyectoDAO proyectoDAO;
    private static TecnicoDAO tecnicoDAO;
    private static AsistenteDAO asistenteDAO;

    private static ControladorAutenticacion controladorAuth;
    private static VistaLogin vistaLogin;
    private static VistaDirector vistaDirector;
    private static VistaJefaDepartamento vistaJefa;

    public static void main(String[] args) throws Exception {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║   Sistema Gestión Ayudantes - FIS-EPN                 ║");
        System.out.println("║   Versión 2.0 | SQLite + Servicios Refactorizados   ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");
        
        // Inicializar conexión a BD SQLite
        System.out.println("→ Conectando a BD SQLite...");
        ConexionBD.getInstancia().conectar();
        
        // Inicializar tablas si no existen
        if (!InitBD.verificarTablas()) {
            System.out.println("→ Creando estructura de BD...");
            InitBD.inicializar();
        } else {
            System.out.println("✓ BD ya inicializada\n");
        }
        
        // Inicializar DAOs
        inicializarDAOs();
        
        // Analizar esquema de BD
        analizarEsquemaBD();
        
        // Inicializar controlador de autenticación
        controladorAuth = new ControladorAutenticacion(miembroDAO, estudianteDAO, ayudanteDAO);
        
        // Mostrar login
        System.out.println("→ Abriendo interfaz de login...\n");
        vistaLogin = new VistaLogin();
        vistaLogin.setControlador(controladorAuth);
        vistaLogin.setOnLoginSuccess(App::navegarSegunRol);
        vistaLogin.mostrarVentana();
    }

    private static void inicializarDAOs() {
        miembroDAO = new MiembroEPNDAO();
        ayudanteDAO = new AyudanteDAO();
        estudianteDAO = new EstudianteDAO();
        proyectoDAO = new ProyectoDAO();
        tecnicoDAO = new TecnicoDAO();
        asistenteDAO = new AsistenteDAO();

        System.out.println("✓ DAOs inicializados correctamente");
    }

    private static void analizarEsquemaBD() {
        System.out.println("\n=== ANÁLISIS DEL ESQUEMA DE LA BASE DE DATOS ===\n");
        
        try {
            java.sql.Connection conn = ConexionBD.getInstancia().getConexion();
            java.sql.DatabaseMetaData meta = conn.getMetaData();
            
            // Obtener todas las tablas
            java.sql.ResultSet rs = meta.getTables(null, null, "%", new String[]{"TABLE"});
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                System.out.println("Tabla: " + tableName);
                
                // Obtener columnas de la tabla
                java.sql.ResultSet rsColumns = meta.getColumns(null, null, tableName, "%");
                while (rsColumns.next()) {
                    String columnName = rsColumns.getString("COLUMN_NAME");
                    String columnType = rsColumns.getString("TYPE_NAME");
                    int columnSize = rsColumns.getInt("COLUMN_SIZE");
                    boolean isNullable = rsColumns.getInt("NULLABLE") == 1;
                    System.out.println("  - " + columnName + " (" + columnType + "(" + columnSize + ")" + (isNullable ? "" : " NOT NULL") + ")");
                }
                rsColumns.close();
                
                // Obtener foreign keys
                java.sql.ResultSet rsFK = meta.getImportedKeys(null, null, tableName);
                while (rsFK.next()) {
                    String fkColumn = rsFK.getString("FKCOLUMN_NAME");
                    String pkTable = rsFK.getString("PKTABLE_NAME");
                    String pkColumn = rsFK.getString("PKCOLUMN_NAME");
                    System.out.println("  FK: " + fkColumn + " -> " + pkTable + "." + pkColumn);
                }
                rsFK.close();
                
                System.out.println();
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println("Error al analizar esquema: " + e.getMessage());
        }
        
        System.out.println("=== FIN ANÁLISIS ===\n");
    }


    public static void navegarSegunRol(String rol) {
        vistaLogin.cerrar();
        
        switch (rol) {
            case "DIRECTOR":
                abrirVistaDirector();
                break;
            case "JEFA_DEPARTAMENTO":
                abrirVistaJefaDepartamento();
                break;
            default:
                System.out.println("✗ Rol no reconocido: " + rol);
        }
    }

    /**
     * REFACTORIZACIÓN:
     * - NO llama a director.setProyectoAsignado()
     * - El proyecto se obtiene via DAO cuando sea necesario en ControladorDirector
     */
    private static void abrirVistaDirector() {
        MiembroEPN usuario = controladorAuth.getUsuarioActual();
        if (usuario == null) {
            System.out.println("✗ No hay usuario autenticado");
            return;
        }

        // Crear objeto Director con datos básicos
        Director director = new Director(
            usuario.getCodigoUnico(),
            usuario.getCedula(),
            usuario.getCorreoInstitucional(),
            usuario.getPassword(),
            usuario.getNombres(),
            usuario.getApellidos(),
            usuario.getTelefono()
        );

        // CAMBIO: NO asignar proyecto aquí
        // El proyecto se obtendrá via DAO en ControladorDirector.obtenerProyectoDelDirector()
        // cuando sea necesario (Lazy Loading)
        
        // Proyectos proyecto = proyectoDAO.buscarPorDirector(director.getCodigoUnico());
        // director.setProyectoAsignado(proyecto);  ← ELIMINADO

        // Crear controlador del director
        ControladorDirector ctrlDirector = new ControladorDirector(
        director,
        ayudanteDAO,
        asistenteDAO,
        tecnicoDAO,
        estudianteDAO,
        proyectoDAO
    );

    
        // Mostrar vista
        vistaDirector = new VistaDirector(ctrlDirector);
        vistaDirector.setVisible(true);
    }

    /**
     * Abre vista de Jefa de Departamento
     */
    private static void abrirVistaJefaDepartamento() {
        MiembroEPN usuario = controladorAuth.getUsuarioActual();
        JefaDepartamento jefa = JefaDepartamento.getInstancia();

        if (usuario != null) {
            // Actualizar datos de la jefa singleton
            jefa.setCodigoUnico(usuario.getCodigoUnico());
            jefa.setCedula(usuario.getCedula());
            jefa.setCorreoInstitucional(usuario.getCorreoInstitucional());
            jefa.setPassword(usuario.getPassword());
            jefa.setNombres(usuario.getNombres());
            jefa.setApellidos(usuario.getApellidos());
            jefa.setTelefono(usuario.getTelefono());
            jefa.setEstado(usuario.getEstado());
        }

        // Crear controlador de jefa
        ControladorJefaDepartamento ctrlJefa = new ControladorJefaDepartamento(
            jefa, 
            ayudanteDAO, 
            proyectoDAO
        );
        
        // Mostrar vista
        vistaJefa = new VistaJefaDepartamento(ctrlJefa);
        vistaJefa.setVisible(true);
    }
}