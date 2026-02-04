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
        vistaJefa.refrescar(); // Inicializar contador de notificaciones
        vistaJefa.setVisible(true);
    }
}