package model.dao;

import java.util.List;

/**
 * Interfaz genérica para operaciones CRUD
 */
public interface IDAO<T> {
    boolean guardar(T objeto);
    T buscarPorId(String id);
    List<T> listarTodos();
    boolean actualizar(T objeto);
    boolean eliminar(String id);
}
