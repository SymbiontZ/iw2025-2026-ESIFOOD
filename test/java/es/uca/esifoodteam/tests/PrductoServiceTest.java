package es.uca.esifoodteam.productos;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto crearProductoDemo(Long id, String nombre, boolean disponible) {
        Producto p = new Producto();
        p.setId(id);
        p.setNombre(nombre);
        p.setDescripcion("Desc " + nombre);
        p.setPrecio(new BigDecimal("10.00"));
        p.setDisponible(disponible);
        return p;
    }

    @Test
    void findAll_debeDevolverTodosLosProductos() {
        Producto p1 = crearProductoDemo(1L, "Hamburguesa", true);
        Producto p2 = crearProductoDemo(2L, "Pizza", false);

        when(productoRepository.findAll()).thenReturn(List.of(p1, p2));

        List<Producto> resultado = productoService.findAll();

        assertEquals(2, resultado.size());
        assertEquals("Hamburguesa", resultado.get(0).getNombre());
        assertEquals("Pizza", resultado.get(1).getNombre());
        verify(productoRepository, times(1)).findAll();
        verifyNoMoreInteractions(productoRepository);
    }

    @Nested
    class FindByIdTests {

        @Test
        void findById_debeDevolverProductoCuandoExiste() {
            Producto p = crearProductoDemo(1L, "Hamburguesa", true);
            when(productoRepository.findById(1L)).thenReturn(Optional.of(p));

            Optional<Producto> resultado = productoService.findById(1L);

            assertTrue(resultado.isPresent());
            assertEquals("Hamburguesa", resultado.get().getNombre());
            verify(productoRepository, times(1)).findById(1L);
        }

        @Test
        void findById_debeDevolverOptionalVacioCuandoNoExiste() {
            when(productoRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Producto> resultado = productoService.findById(99L);

            assertTrue(resultado.isEmpty());
            verify(productoRepository, times(1)).findById(99L);
        }
    }

    @Nested
    class BuscarPorNombreTests {

        @Test
        void buscarPorNombre_debeLlamarAlRepositorioConFiltro() {
            String filtro = "burguer";

            Producto p = crearProductoDemo(1L, "Big Burguer", true);
            when(productoRepository.findByNombreContainingIgnoreCase(filtro))
                    .thenReturn(List.of(p));

            List<Producto> resultado = productoService.buscarPorNombre(filtro);

            assertEquals(1, resultado.size());
            assertEquals("Big Burguer", resultado.get(0).getNombre());
            verify(productoRepository, times(1))
                    .findByNombreContainingIgnoreCase(filtro);
        }

        @Test
        void buscarPorNombre_conFiltroVacioDebeDevolverListaVaciaDelRepo() {
            when(productoRepository.findByNombreContainingIgnoreCase(""))
                    .thenReturn(Collections.emptyList());

            List<Producto> resultado = productoService.buscarPorNombre("");

            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
            verify(productoRepository, times(1))
                    .findByNombreContainingIgnoreCase("");
        }
    }

    @Test
    void findByLocal_debeDevolverProductosDelLocal() {
        Long localId = 10L;

        Producto p = crearProductoDemo(1L, "Hamburguesa local 10", true);
        when(productoRepository.findByLocalId(localId))
                .thenReturn(List.of(p));

        List<Producto> resultado = productoService.findByLocal(localId);

        assertEquals(1, resultado.size());
        assertEquals("Hamburguesa local 10", resultado.get(0).getNombre());
        verify(productoRepository, times(1)).findByLocalId(localId);
        verifyNoMoreInteractions(productoRepository);
    }

    @Nested
    class FindDisponiblesTests {

        @Test
        void findDisponibles_debeDevolverSoloDisponibles() {
            Producto p1 = crearProductoDemo(1L, "Hamburguesa", true);
            Producto p2 = crearProductoDemo(2L, "Pizza", true);

            when(productoRepository.findByDisponibleTrue())
                    .thenReturn(List.of(p1, p2));

            List<Producto> resultado = productoService.findDisponibles();

            assertEquals(2, resultado.size());
            assertTrue(resultado.stream().allMatch(Producto::isDisponible));
            verify(productoRepository, times(1)).findByDisponibleTrue();
        }

        @Test
        void findDisponibles_debeDevolverListaVaciaSiNoHayDisponibles() {
            when(productoRepository.findByDisponibleTrue())
                    .thenReturn(Collections.emptyList());

            List<Producto> resultado = productoService.findDisponibles();

            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
            verify(productoRepository, times(1)).findByDisponibleTrue();
        }
    }

    @Nested
    class SaveTests {

        @Test
        void save_debeGuardarYDevolverProducto() {
            Producto p = crearProductoDemo(null, "Hamburguesa", true);

            when(productoRepository.save(p)).thenAnswer(invocation -> {
                Producto guardado = invocation.getArgument(0);
                guardado.setId(100L);
                return guardado;
            });

            Producto resultado = productoService.save(p);

            assertNotNull(resultado.getId());
            assertEquals(100L, resultado.getId());
            assertEquals("Hamburguesa", resultado.getNombre());
            verify(productoRepository, times(1)).save(p);
        }

        @Test
        void save_debePasarElMismoObjetoQueRecibeAlRepositorio() {
            Producto p = crearProductoDemo(null, "Pizza", false);

            productoService.save(p);

            ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
            verify(productoRepository).save(captor.capture());
            Producto enviado = captor.getValue();

            assertEquals("Pizza", enviado.getNombre());
            assertFalse(enviado.isDisponible());
        }
    }

    @Test
    void delete_debeBorrarPorId() {
        Long id = 42L;

        productoService.delete(id);

        verify(productoRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(productoRepository);
    }
}
