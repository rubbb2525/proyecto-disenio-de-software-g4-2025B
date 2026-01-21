import model.*;
import model.dao.*;
import controller.*;
import view.*;

/**
 * Clase principal de la aplicación
 */
public class App {
    private static MiembroEPNDAO miembroDAO;
    private static AyudanteDAO ayudanteDAO;
    private static EstudianteDAO estudianteDAO;
    private static ProyectoDAO proyectoDAO;
    private static ControladorAutenticacion controladorAuth;
    private static VistaLogin vistaLogin;
    private static VistaDirector vistaDirector;
    private static VistaJefaDepartamento vistaJefa;

    public static void main(String[] args) throws Exception {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║   Sistema Gestión Ayudantes - FIS-EPN                 ║");
        System.out.println("║   Versión 1.0 | SQLite                               ║");
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
        
        System.out.println("DAOs inicializados correctamente");
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
                System.out.println("Rol no reconocido");
        }
    }

    private static void abrirVistaDirector() {
        MiembroEPN usuario = controladorAuth.getUsuarioActual();
        if (usuario == null) {
            System.out.println("✗ No hay usuario autenticado");
            return;
        }

        Director director = new Director(
            usuario.getCodigoUnico(),
            usuario.getCedula(),
            usuario.getCorreoInstitucional(),
            usuario.getPassword(),
            usuario.getNombres(),
            usuario.getApellidos(),
            usuario.getTelefono()
        );

        ProyectoInvestigacion proyecto = proyectoDAO.buscarPorDirector(director.getCodigoUnico());
        director.setProyectoAsignado(proyecto);

        ControladorDirector ctrlDirector = new ControladorDirector(director, ayudanteDAO, estudianteDAO, proyectoDAO);
        vistaDirector = new VistaDirector(ctrlDirector);
        vistaDirector.setVisible(true);
    }

    private static void abrirVistaJefaDepartamento() {
        MiembroEPN usuario = controladorAuth.getUsuarioActual();
        JefaDepartamento jefa = JefaDepartamento.getInstancia();

        if (usuario != null) {
            jefa.setCodigoUnico(usuario.getCodigoUnico());
            jefa.setCedula(usuario.getCedula());
            jefa.setCorreoInstitucional(usuario.getCorreoInstitucional());
            jefa.setPassword(usuario.getPassword());
            jefa.setNombres(usuario.getNombres());
            jefa.setApellidos(usuario.getApellidos());
            jefa.setTelefono(usuario.getTelefono());
            jefa.setEstado(usuario.getEstado());
        }

        ControladorJefaDepartamento ctrlJefa = new ControladorJefaDepartamento(jefa, ayudanteDAO, proyectoDAO);
        vistaJefa = new VistaJefaDepartamento(ctrlJefa);
        vistaJefa.refrescar(); // Inicializar contador de notificaciones
        vistaJefa.setVisible(true);
    }
}
