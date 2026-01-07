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

    private CompletableFuture<Void> guardar_carro(CarroSnapshot carro){
        try {
            String carroJson = mapper.writeValueAsString(carro);
            System.out.println("Guardando carro: " + carroJson);
            WebStorage.setItem(Storage.LOCAL_STORAGE, CARRITO_KEY, carroJson);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            System.err.println("Error guardando el carro: " + e.getMessage());
            WebStorage.removeItem(Storage.LOCAL_STORAGE, CARRITO_KEY);
            return CompletableFuture.completedFuture(null);
        }
    }

    public CompletableFuture<Carro> getCarro() {
        return WebStorage.getItem(Storage.LOCAL_STORAGE, CARRITO_KEY)
            .thenApply(carroJson -> {
                Carro carro = new Carro();
                if(carroJson == null || carroJson.isEmpty()) {
                    return carro;
                }
                try {
                    CarroSnapshot snapshot = mapper.readValue(carroJson, CarroSnapshot.class);
                    if(snapshot.getItems() == null) {
                        return carro;
                    }
                    for (ItemCarroSnapshot itemSnap : snapshot.getItems()) {
                        ItemCarro item = new ItemCarro();
                        Optional<Producto> producto = productoService.findById(itemSnap.getProductoId());
                        if (producto.isEmpty()) {
                            continue;
                        }
                        item.setProducto(producto.get());
                        item.setCantidad(itemSnap.getCantidad());
                        carro.getItems().add(item);
                    }
                } catch (Exception e) {
                    System.err.println("Error parseando carrito: " + e.getMessage());
                }
                return carro;
            });
    }

    public CompletableFuture<Void> agregarCarrito(Producto producto, Integer cantidad) {
        return WebStorage.getItem(Storage.LOCAL_STORAGE, CARRITO_KEY)
            .thenCompose(carroJson -> {
                CarroSnapshot carro;
                if(carroJson == null || carroJson.isEmpty()) {
                    carro = new CarroSnapshot();
                } else {
                    try {
                        carro = mapper.readValue(carroJson, CarroSnapshot.class);
                    } catch (Exception e) {
                        System.err.println("Error cargando el carro: " + e.getMessage());
                        carro = new CarroSnapshot();
                    }
                }
                
                System.out.println("Agregando al carro: " + producto.getNombre() + " Cantidad: " + cantidad);
                ItemCarroSnapshot nuevoItem = new ItemCarroSnapshot();
                nuevoItem.setProductoId(producto.getId());
                nuevoItem.setCantidad(cantidad);
                
                carro.getItems().add(nuevoItem);
                System.out.println("Carro actualizado con " + carro.getItems().size() + " items.");
                
                return guardar_carro(carro);
            });
    }

    public CompletableFuture<Void> vaciarCarrito() {
        WebStorage.removeItem(Storage.LOCAL_STORAGE, CARRITO_KEY);
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<Void> eliminarDelCarrito(Long productoId) {
        return WebStorage.getItem(Storage.LOCAL_STORAGE, CARRITO_KEY)
            .thenCompose(carroJson -> {
                CarroSnapshot carro;
                if (carroJson == null || carroJson.isEmpty()) {
                    carro = new CarroSnapshot();
                } else {
                    try {
                        carro = mapper.readValue(carroJson, CarroSnapshot.class);
                    } catch (Exception e) {
                        System.err.println("Error cargando el carro para eliminar: " + e.getMessage());
                        carro = new CarroSnapshot();
                    }
                }

                if (carro.getItems() != null && productoId != null) {
                    boolean eliminado = false;
                    for (int i = 0; i < carro.getItems().size(); i++) {
                        ItemCarroSnapshot it = carro.getItems().get(i);
                        if (productoId.equals(it.getProductoId())) {
                            carro.getItems().remove(i);
                            eliminado = true;
                            System.out.println("Eliminado 1 item del carro (prodId=" + productoId + ")");
                            break; // solo una ocurrencia
                        }
                    }
                    if (!eliminado) {
                        System.out.println("No se encontró item a eliminar (prodId=" + productoId + ")");
                    }
                }

                return guardar_carro(carro);
            });
    }
}
