package app;

import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;

/**
 * Construye la estructura inicial de la Biblioteca FISEI indicada en el enunciado.
 */
public final class DatosPrueba {

    private DatosPrueba() {
    }

    public static ArbolBiblioteca crearBibliotecaVacia() {
        return new ArbolBiblioteca(new UnidadBiblioteca("BIB", "Biblioteca FISEI", TipoUnidad.BIBLIOTECA));
    }

    public static ArbolBiblioteca cargarEstructuraInicial() {
        ArbolBiblioteca arbol = crearBibliotecaVacia();

        arbol.agregar("BIB", new UnidadBiblioteca("SW", "Software", TipoUnidad.AREA));
        arbol.agregar("SW", new UnidadBiblioteca("SW-E1", "Estante 1 de Software", TipoUnidad.ESTANTE));
        arbol.agregar("SW", new UnidadBiblioteca("SW-E2", "Estante 2 de Software", TipoUnidad.ESTANTE));
        arbol.agregar("BIB", new UnidadBiblioteca("TI", "Tecnologías de la Información", TipoUnidad.AREA));
        arbol.agregar("TI", new UnidadBiblioteca("TI-E1", "Estante 1 de TI", TipoUnidad.ESTANTE));
        arbol.agregar("BIB", new UnidadBiblioteca("TEL", "Telecomunicaciones", TipoUnidad.AREA));

        // SW-E1 -> 60, 35, 80, 20, 45, 70, 90, 40, 75 (en ese orden)
        CatalogoABB swE1 = arbol.buscar("SW-E1").getCatalogo();
        swE1.insertar(new Libro(60, "Estructuras de Datos en Java", "Mark Allen Weiss", 2013));
        swE1.insertar(new Libro(35, "Introducción a los Algoritmos", "Thomas H. Cormen", 2009));
        swE1.insertar(new Libro(80, "Código Limpio", "Robert C. Martin", 2008));
        swE1.insertar(new Libro(20, "Java: Cómo Programar", "Paul Deitel", 2016));
        swE1.insertar(new Libro(45, "Patrones de Diseño", "Erich Gamma", 1994));
        swE1.insertar(new Libro(70, "Refactoring", "Martin Fowler", 2018));
        swE1.insertar(new Libro(90, "El Programador Pragmático", "Andrew Hunt", 1999));
        swE1.insertar(new Libro(40, "Algorithms", "Robert Sedgewick", 2011));
        swE1.insertar(new Libro(75, "Effective Java", "Joshua Bloch", 2018));

        // SW-E2 -> 15, 10, 25
        CatalogoABB swE2 = arbol.buscar("SW-E2").getCatalogo();
        swE2.insertar(new Libro(15, "Ingeniería del Software", "Ian Sommerville", 2011));
        swE2.insertar(new Libro(10, "Ingeniería de Software: Un Enfoque Práctico", "Roger Pressman", 2010));
        swE2.insertar(new Libro(25, "UML Gota a Gota", "Martin Fowler", 1999));

        // TI-E1 -> 50
        CatalogoABB tiE1 = arbol.buscar("TI-E1").getCatalogo();
        tiE1.insertar(new Libro(50, "Sistemas de Bases de Datos", "Ramez Elmasri", 2015));

        return arbol;
    }
}
