package model;

/**
 * Categoría principal de un proyecto (nivel superior)
 */
public enum CategoriaProyecto {
    INVESTIGACION("Investigación"),
    VINCULACION("Vinculación"),
    TRANSFERENCIA_TECNOLOGICA("Transferencia Tecnológica");

    private final String descripcion;

    CategoriaProyecto(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() { return descripcion; }
}