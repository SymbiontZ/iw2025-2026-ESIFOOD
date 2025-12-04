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
class IngredienteServiceTest {

    @Mock
    private IngredienteRepository ingredienteRepository;

    @InjectMocks
    private IngredienteService ingredienteService;

    private Ingrediente crearIngredienteDemo(Long id, String nombre, int stock) {
        Ingrediente ing = new Ingrediente();
        ing.setId(id);
        ing.setNombre(nombre);
        ing.setStock(stock);
        return ing;
    }

    @Test
    void findAll_debeDevolverTodosLosIngredientes() {
        Ingrediente i1 = crearIngredienteDemo(1L, "Tomate", 10);
        Ingrediente i2 = crearIngredienteDemo(2L, "Queso", 5);

        when(ingredienteRepository.findAll()).thenReturn(List.of(i1, i2));

        List<Ingrediente> resultado = ingredienteService.findAll();

        assertEquals(2, resultado.size());
        assertEquals("Tomate", resultado.get(0).getNombre());
        assertEquals("Queso", resultado.get(1).getNombre());
        verify(ingredienteRepository, times(1)).findAll();
    }

    @Nested
    class FindByIdTests {

        @Test
        void findById_debeDevolverIngredienteCuandoExiste() {
            Ingrediente ing = crearIngredienteDemo(1L, "Tomate", 10);
            when(ingredienteRepository.findById(1L)).thenReturn(Optional.of(ing));

            Optional<Ingrediente> resultado = ingredienteService.findById(1L);

            assertTrue(resultado.isPresent());
            assertEquals("Tomate", resultado.get().getNombre());
            assertEquals(10, resultado.get().getStock());
            verify(ingredienteRepository, times(1)).findById(1L);
        }

        @Test
        void findById_debeDevolverOptionalVacioCuandoNoExiste() {
            when(ingredienteRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Ingrediente> resultado = ingredienteService.findById(99L);

            assertTrue(resultado.isEmpty());
            verify(ingredienteRepository, times(1)).findById(99L);
        }
    }

    @Nested
    class BuscarPorNombreTests {

        @Test
        void buscarPorNombre_debeBuscarPorPatronIgnoreCase() {
            String filtro = "tom";

            Ingrediente ing = crearIngredienteDemo(1L, "Tomate", 10);
            when(ingredienteRepository.findByNombreContainingIgnoreCase(filtro))
                    .thenReturn(List.of(ing));

            List<Ingrediente> resultado = ingredienteService.buscarPorNombre(filtro);

            assertEquals(1, resultado.size());
            assertEquals("Tomate", resultado.get(0).getNombre());
            verify(ingredienteRepository, times(1))
                    .findByNombreContainingIgnoreCase(filtro);
        }

        @Test
        void buscarPorNombre_conFiltroQueNoCoincideDebeDevolverListaVacia() {
            when(ingredienteRepository.findByNombreContainingIgnoreCase("xyz"))
                    .thenReturn(Collections.emptyList());

            List<Ingrediente> resultado = ingredienteService.buscarPorNombre("xyz");

            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
            verify(ingredienteRepository, times(1))
                    .findByNombreContainingIgnoreCase("xyz");
        }
    }

    @Nested
    class SaveTests {

        @Test
        void save_debeGuardarIngrediente() {
            Ingrediente ing = crearIngredienteDemo(null, "Tomate", 10);
            when(ingredienteRepository.save(ing)).thenReturn(ing);

            Ingrediente resultado = ingredienteService.save(ing);

            assertEquals("Tomate", resultado.getNombre());
            assertEquals(10, resultado.getStock());
            verify(ingredienteRepository, times(1)).save(ing);
        }

        @Test
        void save_debePermitirStockCero() {
            Ingrediente ing = crearIngredienteDemo(null, "Lechuga", 0);
            when(ingredienteRepository.save(ing)).thenReturn(ing);

            Ingrediente resultado = ingredienteService.save(ing);

            assertEquals(0, resultado.getStock());
            verify(ingredienteRepository, times(1)).save(ing);
        }
    }

    @Test
    void delete_debeBorrarPorId() {
        Long id = 5L;

        ingredienteService.delete(id);

        verify(ingredienteRepository, times(1)).deleteById(id);
    }
}
