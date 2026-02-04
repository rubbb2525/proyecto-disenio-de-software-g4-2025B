package model;

import java.util.Date;

/**
 * Representa un Asistente de Investigación (contratación profesional)
 * Son miembros de la universidad con formación profesional avanzada
 */
public class AsistenteInvestigacion extends Ayudante {
    
    public AsistenteInvestigacion() {
        super();
    }

    public AsistenteInvestigacion(String codigoUnico, String cedula, String correoInstitucional,
                                 String nombres, String apellidos, String telefono,
                                 String carrera, int nivel, float ira, 
                                 int horasSemanales, int mesesContratados) {
        super(codigoUnico, cedula, correoInstitucional, nombres, apellidos, telefono,
              carrera, nivel, ira, horasSemanales, mesesContratados);
    }

    @Override
    public String toString() {
        return "AsistenteInvestigacion{" +
                "codigo=" + codigoUnico +
                ", nombres=" + getNombresCompletos() +
                ", meses=" + getMesesContratados() +
                '}';
    }
}
