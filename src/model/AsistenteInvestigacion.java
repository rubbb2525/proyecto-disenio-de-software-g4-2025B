package model;

import java.util.Date;

/**
 * Representa un Asistente de Investigación (contratación profesional)
 * Son miembros de la universidad con formación profesional avanzada
 */
public class AsistenteInvestigacion extends Ayudante {
    private String tituloAcademico;
    private String areaEspecializacion;
    
    public AsistenteInvestigacion() {
        super();
    }

    public AsistenteInvestigacion(String codigoUnico, String cedula, String correoInstitucional,
                                 String nombres, String apellidos, String telefono,
                                 String carrera, int nivel, float ira, 
                                 int horasSemanales, double salarioMensual,
                                 String tituloAcademico, String areaEspecializacion) {
        super(codigoUnico, cedula, correoInstitucional, nombres, apellidos, telefono,
              carrera, nivel, ira, horasSemanales, salarioMensual);
        this.tituloAcademico = tituloAcademico;
        this.areaEspecializacion = areaEspecializacion;
    }

    // Getters y Setters
    public String getTituloAcademico() {
        return tituloAcademico;
    }

    public void setTituloAcademico(String tituloAcademico) {
        this.tituloAcademico = tituloAcademico;
    }

    public String getAreaEspecializacion() {
        return areaEspecializacion;
    }

    public void setAreaEspecializacion(String areaEspecializacion) {
        this.areaEspecializacion = areaEspecializacion;
    }

    @Override
    public String toString() {
        return "AsistenteInvestigacion{" +
                "codigo=" + codigoUnico +
                ", nombres=" + getNombresCompletos() +
                ", titulo=" + tituloAcademico +
                ", area=" + areaEspecializacion +
                '}';
    }
}
