package negocio;

import enums.TipoUnidad;
import modelo.UnidadBiblioteca;

/**
 * Árbol general n-ario que representa la jerarquía Biblioteca -> Áreas -> Estantes.
 * Todas las operaciones se implementan de forma recursiva.
 */
public class ArbolBiblioteca {

    private final NodoUnidad raiz;

    public ArbolBiblioteca(UnidadBiblioteca biblioteca) {
        if (biblioteca == null || biblioteca.getTipo() != TipoUnidad.BIBLIOTECA) {
            throw new IllegalArgumentException("La raíz debe ser una unidad de tipo BIBLIOTECA.");
        }
        this.raiz = new NodoUnidad(biblioteca);
    }

    public NodoUnidad getRaiz() {
        return raiz;
    }

    // ------------------------------------------------------------------ agregar

    /**
     * Agrega una unidad como hija de la unidad con código codigoPadre.
     * Respeta BIBLIOTECA -> AREA -> ESTANTE y no permite códigos repetidos.
     */
    public boolean agregar(String codigoPadre, UnidadBiblioteca nueva) {
        if (codigoPadre == null || nueva == null) {
            return false;
        }
        if (buscar(nueva.getCodigo()) != null) {
            return false; // código repetido
        }
        NodoUnidad padre = buscar(codigoPadre);
        if (padre == null || !padre.getDato().getTipo().puedeTenerHijo(nueva.getTipo())) {
            return false;
        }
        padre.agregarHijo(new NodoUnidad(nueva));
        return true;
    }

    // ------------------------------------------------------------------ buscar

    /** Busca una unidad sin distinguir mayúsculas. Devuelve null si no existe. */
    public NodoUnidad buscar(String codigo) {
        if (codigo == null) {
            return null;
        }
        return buscar(raiz, codigo.trim().toUpperCase());
    }

    private NodoUnidad buscar(NodoUnidad nodo, String codigo) {
        if (nodo.getDato().getCodigo().equals(codigo)) {
            return nodo;
        }
        for (NodoUnidad hijo : nodo.getHijos()) {
            NodoUnidad encontrado = buscar(hijo, codigo);
            if (encontrado != null) {
                return encontrado;
            }
        }
        return null;
    }

    // ------------------------------------------------------------------ altura

    /** Altura del árbol medida en aristas (solo la raíz = 0). */
    public int altura() {
        return altura(raiz);
    }

    private int altura(NodoUnidad nodo) {
        if (nodo.esHoja()) {
            return 0;
        }
        int mayor = 0;
        for (NodoUnidad hijo : nodo.getHijos()) {
            mayor = Math.max(mayor, altura(hijo));
        }
        return 1 + mayor;
    }

    // ------------------------------------------------------------------ hojas

    /** Número de unidades sin hijos. */
    public int contarHojas() {
        return contarHojas(raiz);
    }

    private int contarHojas(NodoUnidad nodo) {
        if (nodo.esHoja()) {
            return 1;
        }
        int total = 0;
        for (NodoUnidad hijo : nodo.getHijos()) {
            total += contarHojas(hijo);
        }
        return total;
    }

    // ------------------------------------------------------------------ nivel

    /** Nivel de la unidad (raíz = 0). Devuelve -1 si no existe. */
    public int nivel(String codigo) {
        if (codigo == null) {
            return -1;
        }
        return nivel(raiz, codigo.trim().toUpperCase(), 0);
    }

    private int nivel(NodoUnidad nodo, String codigo, int nivelActual) {
        if (nodo.getDato().getCodigo().equals(codigo)) {
            return nivelActual;
        }
        for (NodoUnidad hijo : nodo.getHijos()) {
            int n = nivel(hijo, codigo, nivelActual + 1);
            if (n != -1) {
                return n;
            }
        }
        return -1;
    }

    // ------------------------------------------------------------------ contar unidades

    /** Número total de unidades del árbol. */
    public int contarUnidades() {
        return contarUnidades(raiz);
    }

    private int contarUnidades(NodoUnidad nodo) {
        int total = 1;
        for (NodoUnidad hijo : nodo.getHijos()) {
            total += contarUnidades(hijo);
        }
        return total;
    }

    // ------------------------------------------------------------------ jerarquía

    /** Jerarquía en preorden, con 2 espacios de sangría por nivel. */
    public String mostrarJerarquia() {
        StringBuilder sb = new StringBuilder();
        mostrarJerarquia(raiz, 0, sb);
        return sb.toString();
    }

    private void mostrarJerarquia(NodoUnidad nodo, int nivel, StringBuilder sb) {
        sb.append("  ".repeat(nivel)).append(nodo.getDato());
        if (nodo.getCatalogo() != null) {
            sb.append(" (").append(nodo.getCatalogo().contar()).append(" libros)");
        }
        sb.append(System.lineSeparator());
        for (NodoUnidad hijo : nodo.getHijos()) {
            mostrarJerarquia(hijo, nivel + 1, sb);
        }
    }

    // ------------------------------------------------------------------ libros

    /** Total de libros de todos los estantes. */
    public int contarLibrosTotal() {
        return contarLibrosTotal(raiz);
    }

    private int contarLibrosTotal(NodoUnidad nodo) {
        int total = (nodo.getCatalogo() != null) ? nodo.getCatalogo().contar() : 0;
        for (NodoUnidad hijo : nodo.getHijos()) {
            total += contarLibrosTotal(hijo);
        }
        return total;
    }
}
