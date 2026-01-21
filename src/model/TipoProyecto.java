package model;

/**
 * Enumeración que define los tipos de proyectos de investigación
 */
public enum TipoProyecto {
    INTERNO("Proyecto Interno"),
    SEMILLA("Proyecto Semilla"),
    GRUPALES("Proyecto Grupal"),
    VINCULACION_CON_FINANCIAMIENTO("Vinculación con Financiamiento"),
    TRANSFERENCIA_TECNOLOGICA("Transferencia Tecnológica");

    private final String descripcion;

    TipoProyecto(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
