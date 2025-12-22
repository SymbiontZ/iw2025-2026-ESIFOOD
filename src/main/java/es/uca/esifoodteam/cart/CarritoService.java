package es.uca.esifoodteam.cart;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.component.page.WebStorage.Storage;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.uca.esifoodteam.productos.models.*;
import es.uca.esifoodteam.cart.models.*;
import es.uca.esifoodteam.productos.services.*;


@Service
public class CarritoService {
    private static final String CARRITO_KEY = "esifood_cart";
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProductoService productoService;

    @Autowired
    public CarritoService(ProductoService productoService) {
        this.productoService = productoService;
    }

    private CompletableFuture<CarroSnapshot> cargar_carro(){
        return WebStorage.getItem(Storage.LOCAL_STORAGE, CARRITO_KEY)
            .thenApply(carroJson -> {
                if(carroJson == null || carroJson.isEmpty())
                {
                    return new CarroSnapshot();
                }
                try {
                    return mapper.readValue(carroJson, CarroSnapshot.class);
                } catch (Exception e) {
                    WebStorage.removeItem(Storage.LOCAL_STORAGE, CARRITO_KEY);
                    System.err.println("Error cargando el carro: " + e.getMessage());
                    return new CarroSnapshot();
                }
            });
    }

    private void guardar_carro(CarroSnapshot carro){
        try {
            String carroJson = mapper.writeValueAsString(carro);
            System.out.println("Guardando carro: " + carroJson);
            WebStorage.setItem(Storage.LOCAL_STORAGE, CARRITO_KEY, carroJson);
        } catch (Exception e) {
            WebStorage.removeItem(Storage.LOCAL_STORAGE, CARRITO_KEY);
            System.err.println("Error guardando el carro: " + e.getMessage());
        }
    }

    public CompletableFuture<Carro> getCarro() {
        return cargar_carro().thenApply(snapshot -> {
            Carro carro = new Carro();
            if(snapshot.getItems() == null) {
                return carro;
            }
            for (ItemCarroSnapshot itemSnap : snapshot.getItems()) {
                ItemCarro item = new ItemCarro();
                Optional<Producto> producto = productoService.findById(itemSnap.getProductoId());
                if (producto.isEmpty()) {
                    continue; // O manejar el error según sea necesario
                }
                item.setProducto(producto.get());
                item.setCantidad(itemSnap.getCantidad());
                // Aquí se podrían mapear las bases y extras si es necesario
                carro.getItems().add(item);
            }
            return carro;
        });
    }

    public CompletableFuture<Void> agregarCarrito(Producto producto, Integer cantidad) {
        return cargar_carro().thenAccept(carro -> {
            System.out.println("Agregando al carro: " + producto.getNombre() + " Cantidad: " + cantidad);
            ItemCarroSnapshot nuevoItem = new ItemCarroSnapshot();
            nuevoItem.setProductoId(producto.getId());
            nuevoItem.setCantidad(cantidad);

            carro.getItems().add(nuevoItem);
            System.out.println("Carro actualizado con " + carro.getItems().size() + " items.");
            guardar_carro(carro);
        });
    }
}
