package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import java.util.Arrays;
import java.util.List;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoLibro;

/**
 * Casos de prueba obligatorios del deber. Muestra "X/N pruebas OK" al final.
 */
public class PruebasBiblioteca {

    private static int total = 0;
    private static int correctas = 0;

    public static void main(String[] args) {
        ejecutar();
    }

    public static void ejecutar() {
        total = 0;
        correctas = 0;
        System.out.println("===== PRUEBAS BIBLIOTECA FISEI =====");

        // 1. Biblioteca solo con la raíz
        ArbolBiblioteca soloRaiz = DatosPrueba.crearBibliotecaVacia();
        verificar("Solo raíz: altura 0 y 1 hoja",
                soloRaiz.altura() == 0 && soloRaiz.contarHojas() == 1,
                "altura=" + soloRaiz.altura() + ", hojas=" + soloRaiz.contarHojas());

        // 2. Biblioteca inicial
        ArbolBiblioteca bib = DatosPrueba.cargarEstructuraInicial();
        verificar("Inicial: 7 unidades, altura 2, 4 hojas y 13 libros",
                bib.contarUnidades() == 7 && bib.altura() == 2
                        && bib.contarHojas() == 4 && bib.contarLibrosTotal() == 13,
                "unidades=" + bib.contarUnidades() + ", altura=" + bib.altura()
                        + ", hojas=" + bib.contarHojas() + ", libros=" + bib.contarLibrosTotal());

        // 3. Niveles
        int nBib = bib.nivel("BIB");
        int nSw = bib.nivel("sw");
        int nSwE2 = bib.nivel("SW-E2");
        int nXyz = bib.nivel("XYZ");
        verificar("Nivel de BIB, sw, SW-E2 y XYZ = 0, 1, 2, -1",
                nBib == 0 && nSw == 1 && nSwE2 == 2 && nXyz == -1,
                nBib + ", " + nSw + ", " + nSwE2 + ", " + nXyz);

        // 4. ESTANTE como hijo de BIBLIOTECA
        boolean agregado = bib.agregar("BIB",
                new UnidadBiblioteca("BIB-E9", "Estante directo", TipoUnidad.ESTANTE));
        verificar("Agregar ESTANTE como hijo de BIBLIOTECA = false", !agregado,
                "resultado=" + agregado);

        // 5. Recorridos de SW-E1
        CatalogoABB swE1 = bib.buscar("SW-E1").getCatalogo();
        String pre = swE1.secuencia(TipoRecorrido.PREORDEN);
        String in = swE1.secuencia(TipoRecorrido.INORDEN);
        String pos = swE1.secuencia(TipoRecorrido.POSORDEN);
        verificar("Recorridos de SW-E1 (pre, in, pos)",
                pre.equals("60 35 20 45 40 80 70 75 90")
                        && in.equals("20 35 40 45 60 70 75 80 90")
                        && pos.equals("20 40 45 35 75 70 90 80 60"),
                "pre=" + pre + " | in=" + in + " | pos=" + pos);

        // 6. Traza: última línea con la secuencia
        String traza = swE1.trazaRecorrido(TipoRecorrido.PREORDEN);
        String ultima = traza.substring(traza.lastIndexOf('\n') + 1);
        verificar("Traza preorden termina con la secuencia",
                ultima.equals("Secuencia PREORDEN: 60 35 20 45 40 80 70 75 90"), ultima);

        // 7. Código repetido
        boolean repetido = swE1.insertar(new Libro(60, "Repetido", "Autor", 2020));
        verificar("Insertar código repetido = false", !repetido, "resultado=" + repetido);

        // 8. Retos: camino y rango
        List<Integer> camino = swE1.caminoBusqueda(42);
        int rango = swE1.contarEnRango(40, 75);
        verificar("caminoBusqueda(42) = [60, 35, 45, 40] y contarEnRango(40, 75) = 5",
                camino.equals(Arrays.asList(60, 35, 45, 40)) && rango == 5,
                "camino=" + camino + ", rango=" + rango);

        // 9. Insertar 42 y eliminar 20, 70, 60
        boolean ins42 = swE1.insertar(new Libro(42, "Libro 42", "Autor 42", 2024));
        boolean e20 = swE1.eliminar(20); // hoja
        boolean e70 = swE1.eliminar(70); // un hijo
        boolean e60 = swE1.eliminar(60); // dos hijos
        String preFinal = swE1.secuencia(TipoRecorrido.PREORDEN);
        verificar("Insertar 42, eliminar 20, 70 y 60 -> preorden 75 35 45 40 42 80 90",
                ins42 && e20 && e70 && e60 && preFinal.equals("75 35 45 40 42 80 90")
                        && swE1.esABBValido(),
                "preorden=" + preFinal);

        // 10. Código inexistente
        Libro noExiste = swE1.buscar(999);
        boolean mod = swE1.modificar(999, "X", "Y", 2000);
        boolean eli = swE1.eliminar(999);
        verificar("Buscar / modificar / eliminar inexistente = null / false / false",
                noExiste == null && !mod && !eli,
                noExiste + " / " + mod + " / " + eli);

        // 11. Datos inválidos
        verificar("Código 0 -> IllegalArgumentException", lanzaExcepcion(() -> new Libro(0, "T", "A", 2000)), "");
        verificar("Año 1800 -> IllegalArgumentException", lanzaExcepcion(() -> new Libro(5, "T", "A", 1800)), "");
        verificar("Título vacío -> IllegalArgumentException", lanzaExcepcion(() -> new Libro(5, " ", "A", 2000)), "");

        // 12. esABBValido con un nieto mal ubicado
        CatalogoABB malo = new CatalogoABB();
        NodoLibro n50 = new NodoLibro(new Libro(50, "L50", "A", 2000));
        NodoLibro n30 = new NodoLibro(new Libro(30, "L30", "A", 2000));
        NodoLibro n70 = new NodoLibro(new Libro(70, "L70", "A", 2000));
        NodoLibro n60 = new NodoLibro(new Libro(60, "L60", "A", 2000)); // nieto mal ubicado
        n50.setIzquierdo(n30);
        n50.setDerecho(n70);
        n30.setDerecho(n60); // 60 > 50 pero está en el subárbol izquierdo
        malo.setRaiz(n50);
        verificar("esABBValido() con nieto mal ubicado = false", !malo.esABBValido(), "");

        // 13. Modificar un libro existente
        boolean modOk = swE1.modificar(45, "Patrones de Diseño (2da ed.)", "E. Gamma", 2020);
        Libro l45 = swE1.buscar(45);
        verificar("Modificar libro existente",
                modOk && l45.getAnio() == 2020 && l45.getCodigo() == 45, String.valueOf(l45));

        // 14. Extra: desdePreorden
        CatalogoABB reconstruido = CatalogoABB.desdePreorden(
                new int[]{60, 35, 20, 45, 40, 80, 70, 75, 90});
        verificar("Extra desdePreorden reconstruye SW-E1",
                reconstruido.secuencia(TipoRecorrido.INORDEN).equals("20 35 40 45 60 70 75 80 90")
                        && reconstruido.secuencia(TipoRecorrido.POSORDEN).equals("20 40 45 35 75 70 90 80 60")
                        && reconstruido.esABBValido(),
                "posorden=" + reconstruido.secuencia(TipoRecorrido.POSORDEN));

        System.out.println("------------------------------------");
        System.out.println(correctas + "/" + total + " pruebas OK");
    }

    private static void verificar(String nombre, boolean condicion, String detalle) {
        total++;
        if (condicion) {
            correctas++;
        }
        System.out.printf("[%s] %2d. %s%s%n", condicion ? "OK" : "FALLO", total, nombre,
                detalle.isEmpty() ? "" : "  ->  " + detalle);
    }

    private static boolean lanzaExcepcion(Runnable accion) {
        try {
            accion.run();
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }
}
