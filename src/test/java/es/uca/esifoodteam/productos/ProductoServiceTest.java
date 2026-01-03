package es.uca.esifoodteam.productos;

import es.uca.esifoodteam.productos.models.Producto;
import es.uca.esifoodteam.productos.models.TipoProducto;
import es.uca.esifoodteam.productos.repositories.ProductoRepository;
import es.uca.esifoodteam.productos.services.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    ProductoRepository productoRepository;

    @InjectMocks
    ProductoService productoService;

    @Test
    void findAll_delegatesToRepository() {
        List<Producto> expected = Arrays.asList(new Producto(), new Producto());
        when(productoRepository.findAll()).thenReturn(expected);

        List<Producto> result = productoService.findAll();

        assertSame(expected, result);
        verify(productoRepository).findAll();
    }

    @Test
    void findById_delegatesToRepository() {
        Long id = 10L;
        Producto p = new Producto();
        when(productoRepository.findById(id)).thenReturn(Optional.of(p));

        Optional<Producto> result = productoService.findById(id);

        assertTrue(result.isPresent());
        assertSame(p, result.get());
        verify(productoRepository).findById(id);
    }

    @Test
    void findByTipoProducto_delegatesToRepository() {
        TipoProducto tipo = new TipoProducto();
        List<Producto> expected = Arrays.asList(new Producto());
        when(productoRepository.findByTiposContaining(tipo)).thenReturn(expected);

        List<Producto> result = productoService.findByTipoProducto(tipo);

        assertSame(expected, result);
        verify(productoRepository).findByTiposContaining(tipo);
    }

    @Test
    void buscarPorNombre_delegatesToRepository() {
        String filtro = "piz";
        List<Producto> expected = Arrays.asList(new Producto(), new Producto());
        when(productoRepository.findByNombreContainingIgnoreCase(filtro)).thenReturn(expected);

        List<Producto> result = productoService.buscarPorNombre(filtro);

        assertSame(expected, result);
        verify(productoRepository).findByNombreContainingIgnoreCase(filtro);
    }

    @Test
    void findDisponibles_delegatesToRepository() {
        List<Producto> expected = Arrays.asList(new Producto());
        when(productoRepository.findByDisponibleTrue()).thenReturn(expected);

        List<Producto> result = productoService.findDisponibles();

        assertSame(expected, result);
        verify(productoRepository).findByDisponibleTrue();
    }

    @Test
    void save_delegatesToRepository() {
        Producto p = new Producto();
        when(productoRepository.save(p)).thenReturn(p);

        Producto result = productoService.save(p);

        assertSame(p, result);
        verify(productoRepository).save(p);
    }

    @Test
    void delete_delegatesToRepository() {
        Long id = 5L;

        productoService.delete(id);

        verify(productoRepository).deleteById(id);
    }
}
