package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsula el resultado de una operación del sistema
 */
public class ResultadoOperacion {
    private boolean exitoso;
    private String mensaje;
    private List<String> errores;

    public ResultadoOperacion() {
        this.exitoso = false;
        this.errores = new ArrayList<>();
    }

    public ResultadoOperacion(boolean exitoso, String mensaje) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
        this.errores = new ArrayList<>();
    }

    public boolean esExitoso() {
        return exitoso;
    }

    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<String> getErrores() {
        return errores;
    }

    public void agregarError(String error) {
        errores.add(error);
    }

    public boolean tieneErrores() {
        return !errores.isEmpty();
    }

    public String getMensajeCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append(mensaje);
        if (tieneErrores()) {
            sb.append("\nErrores:\n");
            for (String error : errores) {
                sb.append("- ").append(error).append("\n");
            }
        }
        return sb.toString();
    }
}
