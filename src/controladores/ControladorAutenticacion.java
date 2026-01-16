package controladores;

import dominio.MiembroFIS;
import sistema.SistemaGestionAyudantes;
import vistas.VistaLogin;

public class ControladorAutenticacion {
    private SistemaGestionAyudantes sistema;
    private MiembroFIS usuarioActual;
    private VistaLogin vistaLogin;

    // Constructor
    public ControladorAutenticacion() {
        this.sistema = SistemaGestionAyudantes.getInstancia();
    }

    // Métodos de autenticación
    public MiembroFIS autenticar(String correo, String password) {
        usuarioActual = sistema.autenticarUsuario(correo, password);
        return usuarioActual;
    }

    public void cerrarSesion() {
        if (usuarioActual != null) {
            System.out.println("Sesión cerrada para: " + usuarioActual.getNombresCompletos());
            usuarioActual = null;
        }
    }

    public boolean cambiarPassword(String correo, String actual, String nueva) {
        MiembroFIS usuario = sistema.autenticarUsuario(correo, actual);
        if (usuario != null) {
            return usuario.cambiarPassword(actual, nueva);
        }
        return false;
    }

    // Consultas
    public MiembroFIS getUsuarioActual() {
        return usuarioActual;
    }

    public boolean esDirector() {
        return usuarioActual != null && "DIRECTOR".equals(usuarioActual.getRol());
    }

    public boolean esJefa() {
        return usuarioActual != null && "JEFE_DEPARTAMENTO".equals(usuarioActual.getRol());
    }

    public boolean validarSesion() {
        return usuarioActual != null && usuarioActual.esActivo();
    }

    // Getters y Setters
    public VistaLogin getVistaLogin() {
        return vistaLogin;
    }

    public void setVistaLogin(VistaLogin vistaLogin) {
        this.vistaLogin = vistaLogin;
    }
}
