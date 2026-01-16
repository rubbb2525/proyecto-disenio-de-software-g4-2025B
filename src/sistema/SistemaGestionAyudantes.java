package sistema;

import dominio.*;
import gestores.*;
import persistencia.*;
import java.util.*;
import java.util.stream.Collectors;

public class SistemaGestionAyudantes {
    private static SistemaGestionAyudantes instancia;
    
    // DAOs
    private ProyectoDAO proyectoDAO;
    private AyudanteDAO ayudanteDAO;
    private MiembroFISDAO miembroDAO;
    private MensajeDAO mensajeDAO;
    
    // Gestores
    private GeneradorNotificaciones generadorNotif;
    private GeneradorReportes generadorReportes;
    
    // Constructor privado (Singleton)
    private SistemaGestionAyudantes() {
        // Inicializar DAOs - Ahora usan SQLite automáticamente
        this.proyectoDAO = new ProyectoDAO();
        this.ayudanteDAO = new AyudanteDAO();
        this.miembroDAO = new MiembroFISDAO();
        this.mensajeDAO = new MensajeDAO();
        
        // Inicializar Gestores
        this.generadorNotif = new GeneradorNotificaciones();
        this.generadorReportes = new GeneradorReportes();
    }
    
    // Método Singleton
    public static SistemaGestionAyudantes getInstancia() {
        if (instancia == null) {
            instancia = new SistemaGestionAyudantes();
        }
        return instancia;
    }
    
    // Métodos de inicialización
    public void inicializarSistema() {
        System.out.println("=== Iniciando Sistema de Gestión de Ayudantes ===");
        cargarDatosIniciales();
        System.out.println("Sistema inicializado correctamente");
    }
    
    public void cargarDatosIniciales() {
        System.out.println("Cargando datos iniciales...");
        
        // Crear usuario de prueba - Jefa de Departamento
        if (miembroDAO.buscarPorCorreo("jefa@epn.edu.ec") == null) {
            JefaDepartamento jefa = JefaDepartamento.getInstancia();
            jefa.setNumeroUnico("JEF001");
            jefa.setNombres("María");
            jefa.setApellidos("García López");
            jefa.setCedula("1234567890");
            jefa.setCorreoInstitucional("jefa@epn.edu.ec");
            jefa.setPassword("123456");
            jefa.setRol("JEFE_DEPARTAMENTO");
            jefa.setEspecialidad("Gestión Académica");
            miembroDAO.guardar(jefa);
            System.out.println("✓ Usuario Jefa creado: jefa@epn.edu.ec / 123456");
        }
        
        // Crear usuario de prueba - Director
        if (miembroDAO.buscarPorCorreo("director@epn.edu.ec") == null) {
            Director director = new Director("DIR001", "Juan", "Rodríguez");
            director.setCedula("0987654321");
            director.setCorreoInstitucional("director@epn.edu.ec");
            director.setPassword("123456");
            director.setEspecialidad("Ingeniería de Software");
            miembroDAO.guardar(director);
            System.out.println("✓ Usuario Director creado: director@epn.edu.ec / 123456");
            
            // Crear un proyecto y asignarlo al director
            ProyectoInvestigacion proyecto = new ProyectoInvestigacion("PROY001", "Sistema IoT para Agricultura", TipoProyecto.VINCULACION_CON_FINANCIAMIENTO);
            proyecto.setDescripcion("Desarrollo de sistema IoT para monitoreo agrícola");
            proyecto.setPresupuesto(50000);
            proyecto.setAyudantesPlanificados(3);
            proyecto.setDirector(director);
            director.setProyectoAsignado(proyecto);
            proyectoDAO.guardar(proyecto);
            miembroDAO.actualizar(director);
            System.out.println("✓ Proyecto creado y asignado al director");
        }
        
        System.out.println("\n=== Credenciales de Prueba ===");
        System.out.println("Jefa: jefa@epn.edu.ec / 123456");
        System.out.println("Director: director@epn.edu.ec / 123456");
        System.out.println("\nProyectos: " + proyectoDAO.contarTodos());
        System.out.println("Ayudantes: " + ayudanteDAO.contarTodos());
        System.out.println("Miembros: " + miembroDAO.contarTodos());
    }
    
    // === MÉTODOS DE AUTENTICACIÓN ===
    
    public MiembroFIS autenticarUsuario(String correo, String password) {
        return miembroDAO.validarCredenciales(correo, password);
    }
    
    // === MÉTODOS DE AYUDANTES ===
    
    public boolean registrarAyudante(Ayudante ayudante, ProyectoInvestigacion proyecto) {
        if (ayudante == null || proyecto == null) {
            return false;
        }
        
        try {
            ayudante.setProyectoAsignado(proyecto);
            ayudante.setEstado("ACTIVO");
            ayudante.setFechaRegistro(new Date());
            
            boolean guardado = ayudanteDAO.guardar(ayudante);
            
            if (guardado) {
                proyecto.agregarAyudante(ayudante);
                proyectoDAO.actualizar(proyecto);
                
                // Generar notificación para la jefa
                Notificacion notif = generadorNotif.generarNotificacionNuevoAyudante(ayudante, proyecto);
                notificarJefa(notif);
                
                System.out.println("Ayudante registrado: " + ayudante.getNombresCompletos());
            }
            
            return guardado;
        } catch (Exception e) {
            System.err.println("Error al registrar ayudante: " + e.getMessage());
            return false;
        }
    }
    
    public boolean actualizarAyudante(Ayudante ayudante) {
        if (ayudante == null) {
            return false;
        }
        return ayudanteDAO.actualizar(ayudante);
    }
    
    public boolean darDeBajaAyudante(Ayudante ayudante, String motivo, Date fecha) {
        if (ayudante == null) {
            return false;
        }
        
        try {
            ayudante.darDeBaja(motivo, fecha);
            boolean actualizado = ayudanteDAO.actualizar(ayudante);
            
            if (actualizado) {
                ProyectoInvestigacion proyecto = ayudante.getProyectoAsignado();
                if (proyecto != null) {
                    proyecto.removerAyudante(ayudante);
                    proyectoDAO.actualizar(proyecto);
                    
                    // Generar notificación para la jefa
                    Notificacion notif = generadorNotif.generarNotificacionAyudanteDadoDeBaja(ayudante, proyecto);
                    notificarJefa(notif);
                }
                
                System.out.println("Ayudante dado de baja: " + ayudante.getNombresCompletos());
            }
            
            return actualizado;
        } catch (Exception e) {
            System.err.println("Error al dar de baja ayudante: " + e.getMessage());
            return false;
        }
    }
    
    // === MÉTODOS DE CONSULTA DE PROYECTOS ===
    
    public List<ProyectoInvestigacion> consultarProyectos() {
        return proyectoDAO.listarTodos().stream()
                .map(obj -> (ProyectoInvestigacion) obj)
                .collect(Collectors.toList());
    }
    
    public ProyectoInvestigacion consultarProyectoPorCodigo(String codigo) {
        return proyectoDAO.buscarPorCodigo(codigo);
    }
    
    public ProyectoInvestigacion consultarProyectoPorDirector(Director director) {
        return proyectoDAO.buscarPorDirector(director);
    }
    
    // === MÉTODOS DE CONSULTA DE AYUDANTES ===
    
    public List<Ayudante> consultarAyudantes() {
        return ayudanteDAO.listarTodos().stream()
                .map(obj -> (Ayudante) obj)
                .collect(Collectors.toList());
    }
    
    public List<Ayudante> consultarAyudantesPorProyecto(ProyectoInvestigacion proyecto) {
        if (proyecto == null) {
            return new ArrayList<>();
        }
        return ayudanteDAO.buscarPorProyecto(proyecto.getCodigoProyecto());
    }
    
    public List<Ayudante> consultarAyudantesPorCarrera(String carrera) {
        return ayudanteDAO.buscarPorCarrera(carrera);
    }
    
    public List<Ayudante> consultarAyudantesPorNivel(int nivel) {
        return ayudanteDAO.buscarPorNivel(nivel);
    }
    
    public List<Ayudante> consultarAyudantesActivos() {
        return ayudanteDAO.buscarActivos();
    }
    
    public List<Ayudante> buscarAyudantes(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return consultarAyudantes();
        }
        
        String criterioBusqueda = criterio.toLowerCase();
        return consultarAyudantes().stream()
                .filter(a -> 
                    a.getNombres().toLowerCase().contains(criterioBusqueda) ||
                    a.getApellidos().toLowerCase().contains(criterioBusqueda) ||
                    a.getCarrera().toLowerCase().contains(criterioBusqueda) ||
                    a.getNumeroUnico().toLowerCase().contains(criterioBusqueda)
                )
                .collect(Collectors.toList());
    }
    
    // === MÉTODOS DE MENSAJERÍA ===
    
    public boolean enviarMensaje(Mensaje mensaje) {
        if (mensaje == null) {
            return false;
        }
        return mensajeDAO.guardar(mensaje);
    }
    
    public List<Mensaje> consultarMensajesPorDestinatario(MiembroFIS destinatario) {
        if (destinatario == null) return new ArrayList<>();
        return mensajeDAO.buscarPorDestinatario(destinatario.getNumeroUnico());
    }
    
    public boolean responderMensaje(Mensaje mensaje, String respuesta) {
        if (mensaje == null || respuesta == null) {
            return false;
        }
        
        mensaje.responder(respuesta);
        return mensajeDAO.actualizar(mensaje);
    }
    
    // === MÉTODOS DE NOTIFICACIONES ===
    
    public void notificarJefa(Notificacion notificacion) {
        if (notificacion != null) {
            JefaDepartamento jefa = JefaDepartamento.getInstancia();
            jefa.recibirNotificacion(notificacion);
            System.out.println("Notificación enviada a la Jefa: " + notificacion.getMensaje());
        }
    }
    
    // === MÉTODOS DE REPORTES ===
    
    public Reporte generarReporte(String tipo) {
        switch (tipo.toUpperCase()) {
            case "GENERAL":
                return generadorReportes.generarReporteGeneral(consultarProyectos(), consultarAyudantes());
            case "POR_CARRERA":
                // Requiere parámetro adicional de carrera
                return null;
            case "POR_NIVEL":
                // Requiere parámetro adicional de nivel
                return null;
            default:
                return null;
        }
    }
    
    public Reporte generarReportePorProyecto(ProyectoInvestigacion proyecto) {
        return generadorReportes.generarReportePorProyecto(proyecto);
    }
    
    public Reporte generarReportePorCarrera(String carrera) {
        List<Ayudante> ayudantes = consultarAyudantesPorCarrera(carrera);
        return generadorReportes.generarReportePorCarrera(carrera, ayudantes);
    }
    
    public Reporte generarReportePorNivel(int nivel) {
        List<Ayudante> ayudantes = consultarAyudantesPorNivel(nivel);
        return generadorReportes.generarReportePorNivel(nivel, ayudantes);
    }
    
    // Getters para los DAOs (por si se necesitan externamente)
    public ProyectoDAO getProyectoDAO() {
        return proyectoDAO;
    }
    
    public AyudanteDAO getAyudanteDAO() {
        return ayudanteDAO;
    }
    
    public MiembroFISDAO getMiembroDAO() {
        return miembroDAO;
    }
    
    public MensajeDAO getMensajeDAO() {
        return mensajeDAO;
    }
}
