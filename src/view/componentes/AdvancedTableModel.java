package view.componentes;

import javax.swing.table.AbstractTableModel;
import java.util.*;

public class AdvancedTableModel extends AbstractTableModel {
    
    private List<Object[]> datosOriginales;
    private List<Object[]> datosFiltrados;
    private String[] columnas;
    private Map<Integer, Boolean> ordenAscendente;
    private int columnOrdenada = -1;
    private String filtroActual = "";
    private int columnaBusqueda = -1;
    private int filasPorPagina = 20;
    private int paginaActual = 0;
    
    public AdvancedTableModel(String[] columnas) {
        this.columnas = columnas;
        this.datosOriginales = new ArrayList<>();
        this.datosFiltrados = new ArrayList<>();
        this.ordenAscendente = new HashMap<>();
    }
    
    @Override
    public int getRowCount() {
        int inicio = paginaActual * filasPorPagina;
        int fin = Math.min(inicio + filasPorPagina, datosFiltrados.size());
        return fin - inicio;
    }
    
    @Override
    public int getColumnCount() {
        return columnas.length;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        int indiceReal = paginaActual * filasPorPagina + rowIndex;
        if (indiceReal < datosFiltrados.size()) {
            return datosFiltrados.get(indiceReal)[columnIndex];
        }
        return null;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    public void agregarFila(Object[] fila) {
        datosOriginales.add(fila);
        datosFiltrados.add(fila);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }
    
    public void establecerDatos(List<Object[]> datos) {
        this.datosOriginales = new ArrayList<>(datos);
        this.datosFiltrados = new ArrayList<>(datos);
        this.paginaActual = 0;
        fireTableDataChanged();
    }
    
    public void limpiar() {
        datosOriginales.clear();
        datosFiltrados.clear();
        filtroActual = "";
        columnaBusqueda = -1;
        paginaActual = 0;
        fireTableDataChanged();
    }
    
    /**
     * Busca en vivo en una columna específica
     */
    public void buscarEnColumna(String texto, int columna) {
        this.filtroActual = texto.toLowerCase();
        this.columnaBusqueda = columna;
        this.paginaActual = 0;
        aplicarFiltro();
    }
    
    /**
     * Ordena por columna
     */
    public void ordenarPorColumna(int columna) {
        if (columnOrdenada == columna) {
            ordenAscendente.put(columna, !ordenAscendente.getOrDefault(columna, true));
        } else {
            columnOrdenada = columna;
            ordenAscendente.put(columna, true);
        }
        paginaActual = 0;
        aplicarFiltro();
    }
    
    private void aplicarFiltro() {
        datosFiltrados = new ArrayList<>();
        
        // Filtro de búsqueda
        if (!filtroActual.isEmpty() && columnaBusqueda >= 0) {
            for (Object[] fila : datosOriginales) {
                String valor = String.valueOf(fila[columnaBusqueda]).toLowerCase();
                if (valor.contains(filtroActual)) {
                    datosFiltrados.add(fila);
                }
            }
        } else {
            datosFiltrados = new ArrayList<>(datosOriginales);
        }
        
        // Ordenamiento
        if (columnOrdenada >= 0) {
            final int col = columnOrdenada;
            final boolean ascendente = ordenAscendente.getOrDefault(columnOrdenada, true);
            
            datosFiltrados.sort((a, b) -> {
                Object valA = a[col];
                Object valB = b[col];
                
                if (valA == null || valB == null) return 0;
                
                int comparacion = 0;
                if (valA instanceof Number && valB instanceof Number) {
                    double numA = ((Number) valA).doubleValue();
                    double numB = ((Number) valB).doubleValue();
                    comparacion = Double.compare(numA, numB);
                } else {
                    comparacion = String.valueOf(valA).compareTo(String.valueOf(valB));
                }
                
                return ascendente ? comparacion : -comparacion;
            });
        }
        
        fireTableDataChanged();
    }
    
    /**
     * Paginación
     */
    public void irAPagina(int pagina) {
        int totalPaginas = (datosFiltrados.size() + filasPorPagina - 1) / filasPorPagina;
        if (pagina >= 0 && pagina < totalPaginas) {
            this.paginaActual = pagina;
            fireTableDataChanged();
        }
    }
    
    public void paginaSiguiente() {
        irAPagina(paginaActual + 1);
    }
    
    public void paginaAnterior() {
        irAPagina(paginaActual - 1);
    }
    
    public int getPaginaActual() {
        return paginaActual;
    }
    
    public int getTotalPaginas() {
        return (datosFiltrados.size() + filasPorPagina - 1) / filasPorPagina;
    }
    
    public int getTotalRegistros() {
        return datosFiltrados.size();
    }
    
    public void setFilasPorPagina(int filas) {
        this.filasPorPagina = filas;
        this.paginaActual = 0;
        fireTableDataChanged();
    }
    
    public Object[] obtenerFila(int indiceVista) {
        int indiceReal = paginaActual * filasPorPagina + indiceVista;
        if (indiceReal < datosFiltrados.size()) {
            return datosFiltrados.get(indiceReal);
        }
        return null;
    }
    
    public List<Object[]> obtenerDatosFiltrados() {
        return new ArrayList<>(datosFiltrados);
    }
}
