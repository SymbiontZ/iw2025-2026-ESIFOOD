package es.uca.esifoodteam.cart.models;

import java.util.*;

public class ItemCarroSnapshot {
    private String productoId;
    private int cantidad;
    private Map<String, Integer> base;
    private Map<String, Integer> extra;

    public ItemCarroSnapshot() {}

    public String getProductoId() {
        return productoId;
    }
    public void setProductoId(String productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Map<String, Integer> getBase() {
        return base;
    }
    public void setBase(Map<String, Integer> base) {
        this.base = base;
    }

    public Map<String, Integer> getExtra() {
        return extra;
    }
    public void setExtra(Map<String, Integer> extra) {
        this.extra = extra;
    }
}
