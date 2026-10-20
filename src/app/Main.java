package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import java.util.Scanner;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoUnidad;

/**
 * Menú interactivo del catálogo de la Biblioteca FISEI.
 */
public class Main {

    private static final Scanner SC = new Scanner(System.in);
    private static ArbolBiblioteca biblioteca = DatosPrueba.crearBibliotecaVacia();

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Opción: ");
            try {
                procesar(opcion);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("========= CATÁLOGO BIBLIOTECA FISEI =========");
        System.out.println(" 1. Cargar estructura inicial");
        System.out.println(" 2. Mostrar jerarquía");
        System.out.println(" 3. Agregar unidad");
        System.out.println(" 4. Buscar unidad");
        System.out.println(" 5. Consultar altura, hojas, nivel y total de libros");
        System.out.println(" 6. Insertar libro en un estante");
        System.out.println(" 7. Buscar libro");
        System.out.println(" 8. Modificar libro");
        System.out.println(" 9. Eliminar libro");
        System.out.println("10. Mostrar recorridos con su traza");
        System.out.println("11. Retos (rango, camino, validar ABB, desde preorden)");
        System.out.println("12. Ejecutar PruebasBiblioteca");
        System.out.println(" 0. Salir");
    }

    private static void procesar(int opcion) {
        switch (opcion) {
            case 1:
                biblioteca = DatosPrueba.cargarEstructuraInicial();
                System.out.println("Estructura inicial cargada: " + biblioteca.contarUnidades()
                        + " unidades y " + biblioteca.contarLibrosTotal() + " libros.");
                break;
            case 2:
                System.out.print(biblioteca.mostrarJerarquia());
                break;
            case 3:
                agregarUnidad();
                break;
            case 4:
                buscarUnidad();
                break;
            case 5:
                consultas();
                break;
            case 6:
                insertarLibro();
                break;
            case 7:
                buscarLibro();
                break;
            case 8:
                modificarLibro();
                break;
            case 9:
                eliminarLibro();
                break;
            case 10:
                recorridos();
                break;
            case 11:
                retos();
                break;
            case 12:
                PruebasBiblioteca.ejecutar();
                break;
            case 0:
                System.out.println("¡Hasta pronto!");
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    // ------------------------------------------------------------ unidades

    private static void agregarUnidad() {
        String padre = leerTexto("Código del padre: ");
        String codigo = leerTexto("Código de la nueva unidad: ");
        String nombre = leerTexto("Nombre: ");
        System.out.println("Tipo: 1) AREA  2) ESTANTE");
        int t = leerEntero("Tipo: ");
        TipoUnidad tipo = (t == 1) ? TipoUnidad.AREA : (t == 2) ? TipoUnidad.ESTANTE : null;
        if (tipo == null) {
            System.out.println("Tipo no válido.");
            return;
        }
        boolean ok = biblioteca.agregar(padre, new UnidadBiblioteca(codigo, nombre, tipo));
        System.out.println(ok ? "Unidad agregada."
                : "No se pudo agregar (padre inexistente, código repetido o jerarquía inválida).");
    }

    private static void buscarUnidad() {
        String codigo = leerTexto("Código de la unidad: ");
        NodoUnidad nodo = biblioteca.buscar(codigo);
        if (nodo == null) {
            System.out.println("null (la unidad no existe)");
            return;
        }
        System.out.println("Encontrada: " + nodo + " | nivel " + biblioteca.nivel(codigo)
                + " | hijos " + nodo.getHijos().size()
                + (nodo.getCatalogo() != null ? " | libros " + nodo.getCatalogo().contar() : ""));
    }

    private static void consultas() {
        System.out.println("Altura (aristas): " + biblioteca.altura());
        System.out.println("Hojas: " + biblioteca.contarHojas());
        System.out.println("Total de libros: " + biblioteca.contarLibrosTotal());
        String codigo = leerTexto("Código para consultar su nivel (Enter para omitir): ");
        if (!codigo.isEmpty()) {
            System.out.println("Nivel de " + codigo + ": " + biblioteca.nivel(codigo));
        }
    }

    // ------------------------------------------------------------ libros

    /** Pide el código de un estante y devuelve su catálogo, o null si no es válido. */
    private static CatalogoABB pedirCatalogo() {
        String codigo = leerTexto("Código del estante: ");
        NodoUnidad nodo = biblioteca.buscar(codigo);
        if (nodo == null || nodo.getCatalogo() == null) {
            System.out.println("El código no corresponde a un ESTANTE existente.");
            return null;
        }
        return nodo.getCatalogo();
    }

    private static void insertarLibro() {
        CatalogoABB cat = pedirCatalogo();
        if (cat == null) {
            return;
        }
        int codigo = leerEntero("Código del libro: ");
        String titulo = leerTexto("Título: ");
        String autor = leerTexto("Autor: ");
        int anio = leerEntero("Año: ");
        boolean ok = cat.insertar(new Libro(codigo, titulo, autor, anio));
        System.out.println(ok ? "Libro insertado." : "false: el código ya existe en el estante.");
    }

    private static void buscarLibro() {
        CatalogoABB cat = pedirCatalogo();
        if (cat == null) {
            return;
        }
        Libro libro = cat.buscar(leerEntero("Código del libro: "));
        System.out.println(libro == null ? "null (no existe)" : libro);
    }

    private static void modificarLibro() {
        CatalogoABB cat = pedirCatalogo();
        if (cat == null) {
            return;
        }
        int codigo = leerEntero("Código del libro: ");
        if (cat.buscar(codigo) == null) {
            System.out.println("false: el libro no existe.");
            return;
        }
        String titulo = leerTexto("Nuevo título: ");
        String autor = leerTexto("Nuevo autor: ");
        int anio = leerEntero("Nuevo año: ");
        System.out.println(cat.modificar(codigo, titulo, autor, anio) ? "Libro modificado." : "false");
    }

    private static void eliminarLibro() {
        CatalogoABB cat = pedirCatalogo();
        if (cat == null) {
            return;
        }
        boolean ok = cat.eliminar(leerEntero("Código del libro: "));
        System.out.println(ok ? "Libro eliminado." : "false: el libro no existe.");
        if (ok) {
            System.out.println("Preorden actual: " + cat.secuencia(TipoRecorrido.PREORDEN));
        }
    }

    private static void recorridos() {
        CatalogoABB cat = pedirCatalogo();
        if (cat == null) {
            return;
        }
        for (TipoRecorrido tipo : TipoRecorrido.values()) {
            System.out.println("----- " + tipo + " -----");
            System.out.println(cat.trazaRecorrido(tipo));
        }
    }

    // ------------------------------------------------------------ retos

    private static void retos() {
        System.out.println("1) contarEnRango  2) caminoBusqueda  3) esABBValido  4) desdePreorden (extra)");
        int r = leerEntero("Reto: ");
        if (r == 4) {
            String linea = leerTexto("Preorden separado por espacios: ");
            String[] partes = linea.trim().split("\\s+");
            int[] pre = new int[partes.length];
            for (int i = 0; i < partes.length; i++) {
                pre[i] = Integer.parseInt(partes[i]);
            }
            CatalogoABB nuevo = CatalogoABB.desdePreorden(pre);
            System.out.println("Preorden: " + nuevo.secuencia(TipoRecorrido.PREORDEN));
            System.out.println("Inorden:  " + nuevo.secuencia(TipoRecorrido.INORDEN));
            System.out.println("Posorden: " + nuevo.secuencia(TipoRecorrido.POSORDEN));
            System.out.println("¿ABB válido? " + nuevo.esABBValido());
            return;
        }
        CatalogoABB cat = pedirCatalogo();
        if (cat == null) {
            return;
        }
        switch (r) {
            case 1:
                int a = leerEntero("Desde a: ");
                int b = leerEntero("Hasta b: ");
                System.out.println("contarEnRango(" + a + ", " + b + ") = " + cat.contarEnRango(a, b));
                break;
            case 2:
                int c = leerEntero("Código a buscar: ");
                System.out.println("caminoBusqueda(" + c + ") = " + cat.caminoBusqueda(c));
                break;
            case 3:
                System.out.println("esABBValido() = " + cat.esABBValido());
                break;
            default:
                System.out.println("Reto no válido.");
        }
    }

    // ------------------------------------------------------------ lectura

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return SC.hasNextLine() ? SC.nextLine().trim() : "";
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (!SC.hasNextLine()) {
                return 0; // fin de la entrada: salir
            }
            String linea = SC.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero.");
            }
        }
    }
}
