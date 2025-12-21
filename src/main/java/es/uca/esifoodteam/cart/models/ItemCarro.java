package es.uca.esifoodteam.cart.models;

import es.uca.esifoodteam.productos.models.*;
import java.util.*;

public class ItemCarro {
    private Producto producto;
    private Integer cantidad;

    private Map<Ingrediente, Integer> base;
    private Map<Ingrediente, Integer> extra;

    public Producto getProducto() {
        return producto;
    }
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    public Integer getCantidad() {
        return cantidad;
    }
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    public Map<Ingrediente, Integer> getBase() {
        return base;
    }
    public void setBase(Map<Ingrediente, Integer> base) {
        this.base = base;
    }
    public Map<Ingrediente, Integer> getExtra() {
        return extra;
    }
    public void setExtra(Map<Ingrediente, Integer> extra) {
        this.extra = extra;
    }

}
