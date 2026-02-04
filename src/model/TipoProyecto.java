package model;

/**
 * Enumeración que define los tipos de proyectos (subtipos y otros)
 * Cada tipo tiene asociada su categoría principal
 */
public enum TipoProyecto {
    INTERNO("Proyecto Interno", CategoriaProyecto.INVESTIGACION),
    SEMILLA("Proyecto Semilla", CategoriaProyecto.INVESTIGACION),
    GRUPAL("Proyecto Grupal", CategoriaProyecto.INVESTIGACION),
    MULTIDISCIPLINARIO("Proyecto Multidisciplinario", CategoriaProyecto.INVESTIGACION),
    VINCULACION_CON_FINANCIAMIENTO("Vinculación con Financiamiento", CategoriaProyecto.VINCULACION),
    TRANSFERENCIA_TECNOLOGICA("Transferencia Tecnológica", CategoriaProyecto.TRANSFERENCIA_TECNOLOGICA);

    private final String descripcion;
    private final CategoriaProyecto categoria;

    TipoProyecto(String descripcion, CategoriaProyecto categoria) {
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public CategoriaProyecto getCategoria() {
        return categoria;
    }
}
