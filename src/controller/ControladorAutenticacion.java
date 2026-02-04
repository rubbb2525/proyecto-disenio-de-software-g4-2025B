package controller;

import model.*;
import model.dao.*;
import java.util.Date;

/**
 * Controlador para la autenticación de usuarios
 * 
 * CAMBIOS MENORES:
 * - Este controlador ya tiene buena estructura
 * - Responsabilidad clara: Autenticación
 * - Pero se mejora documentación y se agregan métodos auxiliares
 */
public class ControladorAutenticacion {
    private MiembroEPNDAO miembroDAO;
    private EstudianteDAO estudianteDAO;
    private AyudanteDAO ayudanteDAO;
    private MiembroEPN usuarioActual;

    public ControladorAutenticacion(MiembroEPNDAO miembroDAO, EstudianteDAO estudianteDAO, AyudanteDAO ayudanteDAO) {
        this.miembroDAO = miembroDAO;
        this.estudianteDAO = estudianteDAO;
        this.ayudanteDAO = ayudanteDAO;
        this.usuarioActual = null;
    }

    /**
     * Autentica a un usuario
     * Valida credenciales y estado
     */
    public ResultadoOperacion autenticar(String correo, String password) {
        ResultadoOperacion resultado = new ResultadoOperacion();

        // Validar campos no vacíos
        if (correo == null || correo.isEmpty() || password == null || password.isEmpty()) {
            resultado.setMensaje("Correo y contraseña son requeridos");
            resultado.agregarError("Campos vacíos");
            return resultado;
        }

        // Validar formato de correo
        if (!MiembroEPN.esCorreoValido(correo)) {
            resultado.setMensaje("Formato de correo inválido");
            resultado.agregarError("Correo incorrecto");
            return resultado;
        }

        // Buscar usuario por correo
        MiembroEPN miembro = miembroDAO.buscarPorCorreo(correo);

        if (miembro == null) {
            resultado.setMensaje("Usuario no encontrado");
            resultado.agregarError("Correo incorrecto");
            return resultado;
        }

        // Validar contraseña
        if (!miembro.autenticar(password)) {
            resultado.setMensaje("Contraseña incorrecta");
            resultado.agregarError("Credenciales inválidas");
            return resultado;
        }

        // Validar que esté activo
        if (!miembro.esActivo()) {
            resultado.setMensaje("Usuario inactivo");
            resultado.agregarError("Cuenta desactivada");
            return resultado;
        }

        // Guardar usuario autenticado
        this.usuarioActual = miembro;
        resultado.setExitoso(true);
        resultado.setMensaje("Autenticación exitosa - Rol: " + miembro.getRol());

        return resultado;
    }

    /**
     * NUEVO: Valida si el usuario tiene el rol especificado
     */
    public boolean tieneRol(String rolRequerido) {
        if (usuarioActual == null) {
            return false;
        }
        return rolRequerido.equals(usuarioActual.getRol());
    }

    /**
     * NUEVO: Verifica si es director
     */
    public boolean esDirector() {
        return tieneRol("DIRECTOR");
    }

    /**
     * NUEVO: Verifica si es jefa de departamento
     */
    public boolean esJefaDepartamento() {
        return tieneRol("JEFA_DEPARTAMENTO");
    }

    /**
     * NUEVO: Verifica si es ayudante
     */
    public boolean esAyudante() {
        return tieneRol("AYUDANTE");
    }

    /**
     * NUEVO: Verifica si es estudiante
     */
    public boolean esEstudiante() {
        return tieneRol("ESTUDIANTE");
    }

    /**
     * Obtiene el usuario autenticado
     */
    public MiembroEPN getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Valida que exista un usuario actual
     */
    public boolean hayUsuarioAutenticado() {
        return usuarioActual != null;
    }

    /**
     * Cierra la sesión actual
     */
    public void cerrarSesion() {
        usuarioActual = null;
    }

    /**
     * Determina el rol del usuario para navegación
     */
    public String obtenerTipoUsuario() {
        if (usuarioActual == null) {
            return null;
        }
        return usuarioActual.getRol();
    }

    /**
     * NUEVO: Obtiene el código único del usuario actual
     */
    public String obtenerCodigoUsuarioActual() {
        if (usuarioActual == null) {
            return null;
        }
        return usuarioActual.getCodigoUnico();
    }

    /**
     * NUEVO: Obtiene el correo del usuario actual
     */
    public String obtenerCorreoUsuarioActual() {
        if (usuarioActual == null) {
            return null;
        }
        return usuarioActual.getCorreoInstitucional();
    }

    /**
     * NUEVO: Obtiene el nombre completo del usuario actual
     */
    public String obtenerNombreUsuarioActual() {
        if (usuarioActual == null) {
            return null;
        }
        return usuarioActual.getNombresCompletos();
    }

    /**
     * NUEVO: Carga las notificaciones para el usuario actual
     * Si es Jefa, obtiene sus notificaciones
     */
    public void cargarNotificacionesUsuarioActual() {
        if (usuarioActual == null) {
            return;
        }

        if ("JEFA_DEPARTAMENTO".equals(usuarioActual.getRol())) {
            // Cargar notificaciones de la jefa
            JefaDepartamento jefa = JefaDepartamento.getInstancia();
            NotificacionDAO notifDAO = new NotificacionDAO();
            java.util.List<Notificacion> notificaciones = notifDAO.listarTodos();
            jefa.setNotificaciones(notificaciones);
        }
    }

    /**
     * NUEVO: Obtiene información resumida del usuario autenticado
     */
    public String obtenerResumenUsuario() {
        if (usuarioActual == null) {
            return "No hay usuario autenticado";
        }
        return usuarioActual.obtenerResumen();
    }
}