package es.uca.esifoodteam.productos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SimpleTest {

    @Test
    void simpleDebeHeredarDeProducto() {
        Simple s = new Simple();
        s.setNombre("Coca-Cola");
        s.setDisponible(true);

        assertTrue(s instanceof Producto);
        assertEquals("Coca-Cola", s.getNombre());
        assertTrue(s.isDisponible());
    }

    @Test
    void sePuedeAsignarTipoProducto() {
        Simple s = new Simple();
        TipoProducto tipo = new TipoProducto();
        tipo.setNombre("Bebida");

        s.setTipoProducto(tipo);

        assertNotNull(s.getTipoProducto());
        assertEquals("Bebida", s.getTipoProducto().getNombre());
    }
}
