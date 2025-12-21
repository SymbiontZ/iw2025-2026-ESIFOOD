package es.uca.esifoodteam.cart;

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

    private CarroSnapshot cargar_carro(){
        String carroJson = WebStorage.getItem(Storage.LOCAL_STORAGE, CARRITO_KEY).join();
        try {
            return mapper.readValue( carroJson, CarroSnapshot.class);
        } catch (Exception e) {
            System.err.println("Error cargando el carro: " + e.getMessage());
            return new CarroSnapshot();
        }
    }

    private void guardar_carro(CarroSnapshot carro){
        try {
            String carroJson = mapper.writeValueAsString(carro);
            WebStorage.setItem(Storage.LOCAL_STORAGE, CARRITO_KEY, carroJson);
        } catch (Exception e) {
            WebStorage.removeItem(Storage.LOCAL_STORAGE, CARRITO_KEY);
            System.err.println("Error guardando el carro: " + e.getMessage());
        }
    }
}
