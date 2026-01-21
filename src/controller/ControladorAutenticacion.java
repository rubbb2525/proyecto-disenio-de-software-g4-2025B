package controller;

import model.*;
import model.dao.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controlador para la autenticación de usuarios
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
     */
    public ResultadoOperacion autenticar(String correo, String password) {
        ResultadoOperacion resultado = new ResultadoOperacion();

        if (correo == null || correo.isEmpty() || password == null || password.isEmpty()) {
            resultado.setMensaje("Correo y contraseña son requeridos");
            resultado.agregarError("Campos vacíos");
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

        this.usuarioActual = miembro;
        resultado.setExitoso(true);
        resultado.setMensaje("Autenticación exitosa - Rol: " + miembro.getRol());

        return resultado;
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
}
