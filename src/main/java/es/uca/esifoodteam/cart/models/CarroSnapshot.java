package es.uca.esifoodteam.cart.models;

import java.util.*;

public class CarroSnapshot {
    private List<ItemCarroSnapshot> items;

    public CarroSnapshot() {
        this.items = new ArrayList<>();
    }

    public List<ItemCarroSnapshot> getItems() {
        return items;
    }
    public void setItems(List<ItemCarroSnapshot> items) {
        this.items = items;
    }    
}
