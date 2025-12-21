package es.uca.esifoodteam.cart.models;
import java.util.List;

public class Carro {
    private List<ItemCarro> items;

    public List<ItemCarro> getItems() {
        return items;
    }

    public void setItems(List<ItemCarro> items) {
        this.items = items;
    }
}
