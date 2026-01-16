package persistencia;

import java.util.List;

public interface IDAO {
    boolean guardar(Object entidad);
    Object buscarPorId(String id);
    List<Object> listarTodos();
    boolean actualizar(Object entidad);
    boolean eliminar(String id);
    int contarTodos();
}
