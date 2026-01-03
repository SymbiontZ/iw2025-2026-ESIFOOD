package es.uca.esifoodteam.repository;

import es.uca.esifoodteam.TestAuditingConfig;
import es.uca.esifoodteam.productos.models.Ingrediente;
import es.uca.esifoodteam.productos.models.Producto;
import es.uca.esifoodteam.productos.models.TipoProducto;
import es.uca.esifoodteam.productos.repositories.IngredienteRepository;
import es.uca.esifoodteam.productos.repositories.ProductoRepository;
import es.uca.esifoodteam.productos.repositories.TipoProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestAuditingConfig.class)
class ProductoRepositoryTest {

    @Autowired ProductoRepository productoRepository;
    @Autowired TipoProductoRepository tipoProductoRepository;
    @Autowired IngredienteRepository ingredienteRepository;

    @Test
    void save_and_findByNombreContainingIgnoreCase() {
        Producto p = new Producto();
        p.setNombre("Hamburguesa Doble");
        p.setDescripcion("Con queso");
        p.setPrecio(new BigDecimal("9.50"));
        p.setDisponible(true);

        productoRepository.saveAndFlush(p);

        List<Producto> found = productoRepository.findByNombreContainingIgnoreCase("hambur");
        assertFalse(found.isEmpty());
        assertEquals("Hamburguesa Doble", found.get(0).getNombre());
    }

    @Test
    void findByDisponibleTrue_returnsOnlyDisponibles() {
        Producto ok = new Producto();
        ok.setNombre("Pizza");
        ok.setPrecio(new BigDecimal("11.00"));
        ok.setDisponible(true);

        Producto no = new Producto();
        no.setNombre("Producto Oculto");
        no.setPrecio(new BigDecimal("1.00"));
        no.setDisponible(false);

        productoRepository.save(ok);
        productoRepository.save(no);
        productoRepository.flush();

        List<Producto> disponibles = productoRepository.findByDisponibleTrue();
        assertEquals(1, disponibles.size());
        assertEquals("Pizza", disponibles.get(0).getNombre());
    }

    @Test
    void findByTiposContaining_works() {
        TipoProducto tipo = new TipoProducto();
        tipo.setNombre("BEBIDA");
        tipo = tipoProductoRepository.saveAndFlush(tipo);

        Producto p = new Producto();
        p.setNombre("Coca-Cola");
        p.setPrecio(new BigDecimal("2.00"));
        p.setDisponible(true);
        p.getTipos().add(tipo);

        productoRepository.saveAndFlush(p);

        List<Producto> found = productoRepository.findByTiposContaining(tipo);
        assertEquals(1, found.size());
        assertEquals("Coca-Cola", found.get(0).getNombre());
    }

    @Test
    void producto_ingrediente_manyToMany_persists() {
        Ingrediente ing = new Ingrediente();
        ing.setNombre("Queso");
        ing.setStock(10);
        ing.setPrecio(new BigDecimal("0.50"));
        ing = ingredienteRepository.saveAndFlush(ing);

        Producto p = new Producto();
        p.setNombre("Hamburguesa con queso");
        p.setPrecio(new BigDecimal("8.00"));
        p.setDisponible(true);
        p.getIngredientes().add(ing);

        Producto saved = productoRepository.saveAndFlush(p);

        Producto loaded = productoRepository.findById(saved.getId()).orElseThrow();
        assertEquals(1, loaded.getIngredientes().size());
        assertEquals("Queso", loaded.getIngredientes().iterator().next().getNombre());
    }
}
