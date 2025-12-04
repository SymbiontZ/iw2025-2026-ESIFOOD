package es.uca.esifoodteam.productos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void menuDebeHeredarDeProducto() {
        Menu menu = new Menu();
        menu.setNombre("Menú Hamburguesa");

        assertTrue(menu instanceof Producto);
        assertEquals("Menú Hamburguesa", menu.getNombre());
    }

    @Test
    void sePuedenAñadirProductosSimplesAlMenu() {
        Menu menu = new Menu();

        Simple s1 = new Simple();
        s1.setNombre("Hamburguesa");
        Simple s2 = new Simple();
        s2.setNombre("Patatas");

        menu.getProductos().add(s1);
        menu.getProductos().add(s2);

        assertEquals(2, menu.getProductos().size());
        assertTrue(menu.getProductos().stream()
                .anyMatch(p -> "Hamburguesa".equals(p.getNombre())));
        assertTrue(menu.getProductos().stream()
                .anyMatch(p -> "Patatas".equals(p.getNombre())));
    }

    @Test
    void setProductosDebeReemplazarConjuntoCompleto() {
        Menu menu = new Menu();

        Simple s1 = new Simple();
        s1.setNombre("Hamburguesa");

        Simple s2 = new Simple();
        s2.setNombre("Patatas");

        menu.setProductos(new java.util.HashSet<>(java.util.List.of(s1, s2)));

        assertEquals(2, menu.getProductos().size());
    }
}
