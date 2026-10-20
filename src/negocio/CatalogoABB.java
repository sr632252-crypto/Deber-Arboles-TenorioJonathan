package negocio;

import enums.TipoRecorrido;
import java.util.ArrayList;
import java.util.List;
import modelo.Libro;

/**
 * Árbol Binario de Búsqueda de libros ordenado por código.
 * Menores a la izquierda, mayores a la derecha, sin códigos repetidos.
 * Todas las operaciones son recursivas.
 */
public class CatalogoABB {

    private NodoLibro raiz;

    public NodoLibro getRaiz() {
        return raiz;
    }

    public void setRaiz(NodoLibro raiz) {
        this.raiz = raiz;
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    // ================================================================== insertar

    /** Inserta un libro según su código. Devuelve false si el código ya existe. */
    public boolean insertar(Libro libro) {
        if (libro == null) {
            return false;
        }
        if (raiz == null) {
            raiz = new NodoLibro(libro);
            return true;
        }
        return insertar(raiz, libro);
    }

    private boolean insertar(NodoLibro nodo, Libro libro) {
        int codigo = libro.getCodigo();
        int actual = nodo.getDato().getCodigo();
        if (codigo == actual) {
            return false; // repetido
        }
        if (codigo < actual) {
            if (nodo.getIzquierdo() == null) {
                nodo.setIzquierdo(new NodoLibro(libro));
                return true;
            }
            return insertar(nodo.getIzquierdo(), libro);
        }
        if (nodo.getDerecho() == null) {
            nodo.setDerecho(new NodoLibro(libro));
            return true;
        }
        return insertar(nodo.getDerecho(), libro);
    }

    // ================================================================== buscar

    /** Busca un libro por código. Devuelve null si no existe. */
    public Libro buscar(int codigo) {
        NodoLibro nodo = buscarNodo(raiz, codigo);
        return (nodo == null) ? null : nodo.getDato();
    }

    private NodoLibro buscarNodo(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;
        }
        int actual = nodo.getDato().getCodigo();
        if (codigo == actual) {
            return nodo;
        }
        return (codigo < actual)
                ? buscarNodo(nodo.getIzquierdo(), codigo)
                : buscarNodo(nodo.getDerecho(), codigo);
    }

    // ================================================================== contar

    /** Número de libros del catálogo. */
    public int contar() {
        return contar(raiz);
    }

    private int contar(NodoLibro nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contar(nodo.getIzquierdo()) + contar(nodo.getDerecho());
    }

    // ================================================================== recorridos

    /** Devuelve los libros en el orden del recorrido indicado. */
    public List<Libro> recorrer(TipoRecorrido tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de recorrido es obligatorio.");
        }
        List<Libro> resultado = new ArrayList<>();
        switch (tipo) {
            case PREORDEN:
                preorden(raiz, resultado);
                break;
            case INORDEN:
                inorden(raiz, resultado);
                break;
            default:
                posorden(raiz, resultado);
                break;
        }
        return resultado;
    }

    private void preorden(NodoLibro nodo, List<Libro> lista) {
        if (nodo == null) {
            return;
        }
        lista.add(nodo.getDato());
        preorden(nodo.getIzquierdo(), lista);
        preorden(nodo.getDerecho(), lista);
    }

    private void inorden(NodoLibro nodo, List<Libro> lista) {
        if (nodo == null) {
            return;
        }
        inorden(nodo.getIzquierdo(), lista);
        lista.add(nodo.getDato());
        inorden(nodo.getDerecho(), lista);
    }

    private void posorden(NodoLibro nodo, List<Libro> lista) {
        if (nodo == null) {
            return;
        }
        posorden(nodo.getIzquierdo(), lista);
        posorden(nodo.getDerecho(), lista);
        lista.add(nodo.getDato());
    }

    /** Códigos del recorrido separados por espacio (útil para mostrar y probar). */
    public String secuencia(TipoRecorrido tipo) {
        StringBuilder sb = new StringBuilder();
        for (Libro libro : recorrer(tipo)) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(libro.getCodigo());
        }
        return sb.toString();
    }

    // ================================================================== traza

    /**
     * Traza del recorrido: "entra X" al entrar al nodo, "VISITA X" al procesarlo y
     * "null -> regresa" en cada hijo vacío; 2 espacios por nivel.
     * La última línea es "Secuencia TIPO: ...".
     */
    public String trazaRecorrido(TipoRecorrido tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de recorrido es obligatorio.");
        }
        StringBuilder sb = new StringBuilder();
        List<Integer> secuencia = new ArrayList<>();
        traza(raiz, 0, tipo, sb, secuencia);
        sb.append("Secuencia ").append(tipo).append(":");
        for (Integer codigo : secuencia) {
            sb.append(' ').append(codigo);
        }
        return sb.toString();
    }

    private void traza(NodoLibro nodo, int nivel, TipoRecorrido tipo,
                       StringBuilder sb, List<Integer> secuencia) {
        String sangria = "  ".repeat(nivel);
        if (nodo == null) {
            sb.append(sangria).append("null -> regresa").append(System.lineSeparator());
            return;
        }
        int codigo = nodo.getDato().getCodigo();
        sb.append(sangria).append("entra ").append(codigo).append(System.lineSeparator());
        if (tipo == TipoRecorrido.PREORDEN) {
            visitar(codigo, sangria, sb, secuencia);
        }
        traza(nodo.getIzquierdo(), nivel + 1, tipo, sb, secuencia);
        if (tipo == TipoRecorrido.INORDEN) {
            visitar(codigo, sangria, sb, secuencia);
        }
        traza(nodo.getDerecho(), nivel + 1, tipo, sb, secuencia);
        if (tipo == TipoRecorrido.POSORDEN) {
            visitar(codigo, sangria, sb, secuencia);
        }
    }

    private void visitar(int codigo, String sangria, StringBuilder sb, List<Integer> secuencia) {
        sb.append(sangria).append("VISITA ").append(codigo).append(System.lineSeparator());
        secuencia.add(codigo);
    }

    // ================================================================== modificar

    /**
     * Modifica título, autor y año (el código no cambia).
     * Devuelve false si el código no existe. Lanza IllegalArgumentException si los datos son inválidos.
     */
    public boolean modificar(int codigo, String titulo, String autor, int anio) {
        NodoLibro nodo = buscarNodo(raiz, codigo);
        if (nodo == null) {
            return false;
        }
        // Se validan primero todos los datos para no dejar el libro modificado a medias.
        new Libro(codigo, titulo, autor, anio);
        Libro libro = nodo.getDato();
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setAnio(anio);
        return true;
    }

    // ================================================================== eliminar

    /**
     * Elimina el libro con el código dado.
     * Casos: hoja, nodo con un hijo y nodo con dos hijos (reemplazo por el sucesor inorden).
     */
    public boolean eliminar(int codigo) {
        if (buscarNodo(raiz, codigo) == null) {
            return false;
        }
        raiz = eliminar(raiz, codigo);
        return true;
    }

    private NodoLibro eliminar(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;
        }
        int actual = nodo.getDato().getCodigo();
        if (codigo < actual) {
            nodo.setIzquierdo(eliminar(nodo.getIzquierdo(), codigo));
        } else if (codigo > actual) {
            nodo.setDerecho(eliminar(nodo.getDerecho(), codigo));
        } else {
            // Caso 1 (hoja) y caso 2 (un hijo): se reemplaza por el hijo existente o por null.
            if (nodo.getIzquierdo() == null) {
                return nodo.getDerecho();
            }
            if (nodo.getDerecho() == null) {
                return nodo.getIzquierdo();
            }
            // Caso 3 (dos hijos): se copia el sucesor inorden y luego se elimina del subárbol derecho.
            NodoLibro sucesor = minimo(nodo.getDerecho());
            nodo.setDato(sucesor.getDato());
            nodo.setDerecho(eliminar(nodo.getDerecho(), sucesor.getDato().getCodigo()));
        }
        return nodo;
    }

    /** Nodo con el menor código de un subárbol (el más a la izquierda). */
    private NodoLibro minimo(NodoLibro nodo) {
        if (nodo.getIzquierdo() == null) {
            return nodo;
        }
        return minimo(nodo.getIzquierdo());
    }

    /** Altura del ABB en aristas (vacío = -1, solo raíz = 0). */
    public int altura() {
        return altura(raiz);
    }

    private int altura(NodoLibro nodo) {
        if (nodo == null) {
            return -1;
        }
        return 1 + Math.max(altura(nodo.getIzquierdo()), altura(nodo.getDerecho()));
    }

    // ================================================================== RETOS

    /** Cuenta los códigos que están en [a, b], podando las ramas que no pueden contenerlos. */
    public int contarEnRango(int a, int b) {
        if (a > b) {
            return contarEnRango(raiz, b, a);
        }
        return contarEnRango(raiz, a, b);
    }

    private int contarEnRango(NodoLibro nodo, int a, int b) {
        if (nodo == null) {
            return 0;
        }
        int codigo = nodo.getDato().getCodigo();
        if (codigo < a) {
            return contarEnRango(nodo.getDerecho(), a, b);   // poda: todo el izquierdo es menor que a
        }
        if (codigo > b) {
            return contarEnRango(nodo.getIzquierdo(), a, b); // poda: todo el derecho es mayor que b
        }
        return 1 + contarEnRango(nodo.getIzquierdo(), a, b) + contarEnRango(nodo.getDerecho(), a, b);
    }

    /** Códigos visitados desde la raíz hasta el libro (o hasta donde termina la búsqueda). */
    public List<Integer> caminoBusqueda(int codigo) {
        List<Integer> camino = new ArrayList<>();
        caminoBusqueda(raiz, codigo, camino);
        return camino;
    }

    private void caminoBusqueda(NodoLibro nodo, int codigo, List<Integer> camino) {
        if (nodo == null) {
            return;
        }
        int actual = nodo.getDato().getCodigo();
        camino.add(actual);
        if (codigo < actual) {
            caminoBusqueda(nodo.getIzquierdo(), codigo, camino);
        } else if (codigo > actual) {
            caminoBusqueda(nodo.getDerecho(), codigo, camino);
        }
    }

    /** Verifica todo el ABB pasando límites mínimo y máximo a cada nodo. */
    public boolean esABBValido() {
        return esABBValido(raiz, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean esABBValido(NodoLibro nodo, long minimo, long maximo) {
        if (nodo == null) {
            return true;
        }
        long codigo = nodo.getDato().getCodigo();
        if (codigo <= minimo || codigo >= maximo) {
            return false;
        }
        return esABBValido(nodo.getIzquierdo(), minimo, codigo)
                && esABBValido(nodo.getDerecho(), codigo, maximo);
    }

    // ================================================================== EXTRA

    /**
     * Reconstruye un ABB a partir de su recorrido preorden en O(n), sin insertar uno por uno:
     * cada valor se coloca solo si cae dentro de los límites del subárbol actual.
     * Los libros se crean con datos genéricos.
     */
    public static CatalogoABB desdePreorden(int[] preorden) {
        if (preorden == null) {
            throw new IllegalArgumentException("El arreglo preorden no puede ser null.");
        }
        CatalogoABB catalogo = new CatalogoABB();
        int[] indice = {0};
        catalogo.setRaiz(construir(preorden, indice, Long.MIN_VALUE, Long.MAX_VALUE));
        if (indice[0] != preorden.length) {
            throw new IllegalArgumentException("El arreglo no es un preorden válido de un ABB.");
        }
        return catalogo;
    }

    private static NodoLibro construir(int[] pre, int[] indice, long minimo, long maximo) {
        if (indice[0] >= pre.length) {
            return null;
        }
        int valor = pre[indice[0]];
        if (valor <= minimo || valor >= maximo) {
            return null; // el valor pertenece a otro subárbol
        }
        indice[0]++;
        NodoLibro nodo = new NodoLibro(new Libro(valor, "Libro " + valor, "Autor desconocido", 2026));
        nodo.setIzquierdo(construir(pre, indice, minimo, valor));
        nodo.setDerecho(construir(pre, indice, valor, maximo));
        return nodo;
    }
}
