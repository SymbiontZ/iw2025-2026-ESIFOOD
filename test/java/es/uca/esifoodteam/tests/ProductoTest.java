package es.uca.esifoodteam.productos;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

class ProductoTest {

    @Test
    void porDefectoProductoEsDisponible() {
        Producto producto = new Producto();

        assertTrue(producto.isDisponible(),
                "Un producto nuevo debería estar disponible por defecto");
    }

    @Test
    void sePuedenActualizarCamposBasicos() {
        Producto producto = new Producto();
        producto.setNombre("Hamburguesa");
        producto.setDescripcion("Hamburguesa sencilla");
        producto.setPrecio(new BigDecimal("4.50"));
        producto.setDisponible(false);

        assertEquals("Hamburguesa", producto.getNombre());
        assertEquals("Hamburguesa sencilla", producto.getDescripcion());
        assertEquals(new BigDecimal("4.50"), producto.getPrecio());
        assertFalse(producto.isDisponible());
    }

    @Test
    void coleccionesDeIngredientesYLineasNoSonNulasPorDefecto() {
        Producto producto = new Producto();

        assertNotNull(producto.getIngredientes());
        assertNotNull(producto.getLineas());
        assertTrue(producto.getIngredientes().isEmpty());
        assertTrue(producto.getLineas().isEmpty());
    }

    @Test
    void sePuedeAsignarConjuntoDeIngredientes() {
        Producto producto = new Producto();

        Ingrediente ing1 = new Ingrediente();
        ing1.setNombre("Tomate");
        Ingrediente ing2 = new Ingrediente();
        ing2.setNombre("Queso");

        producto.setIngredientes(Set.of(ing1, ing2));

        assertEquals(2, producto.getIngredientes().size());
        assertTrue(producto.getIngredientes().stream()
                .anyMatch(i -> "Tomate".equals(i.getNombre())));
        assertTrue(producto.getIngredientes().stream()
                .anyMatch(i -> "Queso".equals(i.getNombre())));
    }

    @Test
    void sePuedeAsignarListaDeLineasPedido() {
        Producto producto = new Producto();

        // No necesitamos LineaPedido real, pero asumimos que existe.
        // Para que compile bastará con que la clase exista.
        es.uca.esifoodteam.pedidos.LineaPedido l1 =
                new es.uca.esifoodteam.pedidos.LineaPedido();
        es.uca.esifoodteam.pedidos.LineaPedido l2 =
                new es.uca.esifoodteam.pedidos.LineaPedido();

        producto.setLineas(List.of(l1, l2));

        assertEquals(2, producto.getLineas().size());
    }
}
