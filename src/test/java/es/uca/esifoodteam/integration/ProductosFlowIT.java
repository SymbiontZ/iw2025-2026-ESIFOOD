package es.uca.esifoodteam.integration;

import es.uca.esifoodteam.TestAuditingConfig;
import es.uca.esifoodteam.productos.models.Producto;
import es.uca.esifoodteam.productos.models.TipoProducto;
import es.uca.esifoodteam.productos.repositories.TipoProductoRepository;
import es.uca.esifoodteam.productos.services.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestAuditingConfig.class)
class ProductoFlowIT {

    @Autowired ProductoService productoService;
    @Autowired TipoProductoRepository tipoProductoRepository;

    @Test
    void producto_endToEnd_save_andQueryByNombre_andDisponible() {
        String tag = String.valueOf(System.nanoTime());

        Producto p1 = new Producto();
        p1.setNombre("Pizza Barbacoa " + tag);
        p1.setDescripcion("desc");
        p1.setPrecio(new BigDecimal("12.00"));
        p1.setDisponible(true);

        Producto p2 = new Producto();
        p2.setNombre("Burger Doble " + tag);
        p2.setDescripcion("desc");
        p2.setPrecio(new BigDecimal("10.00"));
        p2.setDisponible(false);

        productoService.save(p1);
        productoService.save(p2);

        List<Producto> porNombre = productoService.buscarPorNombre("pizza barbacoa " + tag);
        assertTrue(
            porNombre.stream().anyMatch(p -> ("Pizza Barbacoa " + tag).equals(p.getNombre())),
            "Debe devolver el producto recién creado al buscar por nombre"
        );

        List<Producto> disponibles = productoService.findDisponibles();
        assertTrue(
            disponibles.stream().anyMatch(p -> ("Pizza Barbacoa " + tag).equals(p.getNombre())),
            "Debe aparecer el producto disponible recién creado"
        );
        assertFalse(
            disponibles.stream().anyMatch(p -> ("Burger Doble " + tag).equals(p.getNombre())),
            "No debe aparecer el producto NO disponible en findDisponibles()"
        );
    }


    @Test
    void producto_endToEnd_findByTipoProducto() {
        TipoProducto tipo = new TipoProducto();
        tipo.setNombre("PIZZA");
        tipo.setNombreId("PIZZA");
        tipo = tipoProductoRepository.save(tipo);

        Producto p = new Producto();
        p.setNombre("Margarita");
        p.setDescripcion("desc");
        p.setPrecio(new BigDecimal("9.50"));
        p.setDisponible(true);
        p.getTipos().add(tipo);

        productoService.save(p);

        List<Producto> res = productoService.findByTipoProducto(tipo);
        assertEquals(1, res.size());
        assertEquals("Margarita", res.get(0).getNombre());
        assertTrue(res.get(0).getTipos().stream().anyMatch(t -> "PIZZA".equals(t.getNombre())));
    }
}
