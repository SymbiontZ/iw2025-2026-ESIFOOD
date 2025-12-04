package es.uca.esifoodteam.productos;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TipoProductoServiceTest {

    @Mock
    private TipoProductoRepository tipoProductoRepository;

    @InjectMocks
    private TipoProductoService tipoProductoService;

    private TipoProducto crearTipoDemo(Long id, String nombre) {
        TipoProducto t = new TipoProducto();
        t.setId(id);
        t.setNombre(nombre);
        return t;
    }

    @Test
    void findAll_debeDevolverTodosLosTipos() {
        TipoProducto t1 = crearTipoDemo(1L, "Hamburguesa");
        TipoProducto t2 = crearTipoDemo(2L, "Bebida");

        when(tipoProductoRepository.findAll()).thenReturn(List.of(t1, t2));

        List<TipoProducto> resultado = tipoProductoService.findAll();

        assertEquals(2, resultado.size());
        assertEquals("Hamburguesa", resultado.get(0).getNombre());
        assertEquals("Bebida", resultado.get(1).getNombre());
        verify(tipoProductoRepository, times(1)).findAll();
    }

    @Nested
    class FindByIdTests {

        @Test
        void findById_debeDevolverTipoCuandoExiste() {
            TipoProducto t = crearTipoDemo(1L, "Hamburguesa");
            when(tipoProductoRepository.findById(1L)).thenReturn(Optional.of(t));

            Optional<TipoProducto> resultado = tipoProductoService.findById(1L);

            assertTrue(resultado.isPresent());
            assertEquals("Hamburguesa", resultado.get().getNombre());
            verify(tipoProductoRepository, times(1)).findById(1L);
        }

        @Test
        void findById_debeDevolverOptionalVacioCuandoNoExiste() {
            when(tipoProductoRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<TipoProducto> resultado = tipoProductoService.findById(99L);

            assertTrue(resultado.isEmpty());
            verify(tipoProductoRepository, times(1)).findById(99L);
        }
    }

    @Nested
    class FindByNombreTests {

        @Test
        void findByNombre_debeBuscarPorNombreExacto() {
            String nombre = "Hamburguesa";
            TipoProducto t = crearTipoDemo(1L, nombre);

            when(tipoProductoRepository.findByNombre(nombre))
                    .thenReturn(Optional.of(t));

            Optional<TipoProducto> resultado = tipoProductoService.findByNombre(nombre);

            assertTrue(resultado.isPresent());
            assertEquals(nombre, resultado.get().getNombre());
            verify(tipoProductoRepository, times(1)).findByNombre(nombre);
        }

        @Test
        void findByNombre_debeDevolverVacioSiNoExiste() {
            when(tipoProductoRepository.findByNombre("Inexistente"))
                    .thenReturn(Optional.empty());

            Optional<TipoProducto> resultado = tipoProductoService.findByNombre("Inexistente");

            assertTrue(resultado.isEmpty());
            verify(tipoProductoRepository, times(1)).findByNombre("Inexistente");
        }
    }

    @Nested
    class SaveTests {

        @Test
        void save_debeGuardarTipoProducto() {
            TipoProducto t = crearTipoDemo(null, "Hamburguesa");

            when(tipoProductoRepository.save(t)).thenReturn(t);

            TipoProducto resultado = tipoProductoService.save(t);

            assertEquals("Hamburguesa", resultado.getNombre());
            verify(tipoProductoRepository, times(1)).save(t);
        }

        @Test
        void save_debePermitirNombresCortos() {
            TipoProducto t = crearTipoDemo(null, "A");

            when(tipoProductoRepository.save(t)).thenReturn(t);

            TipoProducto resultado = tipoProductoService.save(t);

            assertEquals("A", resultado.getNombre());
            verify(tipoProductoRepository, times(1)).save(t);
        }
    }

    @Test
    void delete_debeBorrarPorId() {
        Long id = 3L;

        tipoProductoService.delete(id);

        verify(tipoProductoRepository, times(1)).deleteById(id);
    }
}
