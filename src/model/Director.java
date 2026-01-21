package model;

import java.util.List;

/**
 * Representa un director de proyecto de investigación
 */
public class Director extends MiembroEPN {
    private ProyectoInvestigacion proyectoAsignado;

    public Director() {
    }

    public Director(String codigoUnico, String cedula, String correoInstitucional,
                   String password, String nombres, String apellidos, String telefono) {
        super(codigoUnico, cedula, correoInstitucional, password, nombres, apellidos, telefono, "DIRECTOR", "ACTIVO");
    }

    /**
     * Registra un nuevo ayudante en el proyecto
     */
    public ResultadoOperacion registrarAyudante(Ayudante ayudante) {
        ResultadoOperacion resultado = new ResultadoOperacion();

        if (proyectoAsignado == null) {
            resultado.setMensaje("No hay proyecto asignado al director");
            resultado.agregarError("Proyecto nulo");
            return resultado;
        }

        if (!proyectoAsignado.tieneCupoDisponible()) {
            resultado.setMensaje("No hay cupos disponibles en el proyecto");
            resultado.agregarError("Cupos agotados: " + proyectoAsignado.getAyudantesPlanificados());
            return resultado;
        }

        if (proyectoAsignado.agregarAyudante(ayudante)) {
            resultado.setExitoso(true);
            resultado.setMensaje("Ayudante registrado exitosamente");
        } else {
            resultado.setMensaje("No se pudo registrar el ayudante");
            resultado.agregarError("Error al agregar a la lista");
        }

        return resultado;
    }

    /**
     * Da de baja un ayudante del proyecto
     */
    public ResultadoOperacion darDeBajaAyudante(Ayudante ayudante, String motivo, java.util.Date fecha) {
        ResultadoOperacion resultado = new ResultadoOperacion();

        if (ayudante == null) {
            resultado.setMensaje("Ayudante nulo");
            resultado.agregarError("No se puede dar de baja un ayudante nulo");
            return resultado;
        }

        ayudante.darDeBaja(motivo, fecha);
        resultado.setExitoso(true);
        resultado.setMensaje("Ayudante dado de baja exitosamente");

        return resultado;
    }

    /**
     * Consulta los ayudantes del proyecto asignado
     */
    public List<Ayudante> consultarAyudantesDelProyecto() {
        if (proyectoAsignado == null) {
            return null;
        }
        return proyectoAsignado.getAyudantes();
    }

    // Getters y Setters
    public ProyectoInvestigacion getProyectoAsignado() {
        return proyectoAsignado;
    }

    public void setProyectoAsignado(ProyectoInvestigacion proyectoAsignado) {
        this.proyectoAsignado = proyectoAsignado;
    }
}
