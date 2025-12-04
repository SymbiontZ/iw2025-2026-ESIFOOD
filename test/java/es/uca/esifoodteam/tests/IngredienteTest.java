package es.uca.esifoodteam.productos;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

class IngredienteTest {

    @Test
    void sePuedenAsignarCamposBasicos() {
        Ingrediente ing = new Ingrediente();

        ing.setId(1L);
        ing.setNombre("Tomate");
        ing.setStock(10);

        assertEquals(1L, ing.getId());
        assertEquals("Tomate", ing.getNombre());
        assertEquals(10, ing.getStock());
    }

    @Test
    void coleccionDeProductosNoEsNulaPorDefecto() {
        Ingrediente ing = new Ingrediente();

        assertNotNull(ing.getProductos());
        assertTrue(ing.getProductos().isEmpty());
    }

    @Test
    void sePuedeAsignarConjuntoDeProductos() {
        Ingrediente ing = new Ingrediente();

        Producto p1 = new Producto();
        p1.setNombre("Hamburguesa");
        Producto p2 = new Producto();
        p2.setNombre("Pizza");

        ing.setProductos(Set.of(p1, p2));

        assertEquals(2, ing.getProductos().size());
        assertTrue(ing.getProductos().stream()
                .anyMatch(p -> "Hamburguesa".equals(p.getNombre())));
    }
}
