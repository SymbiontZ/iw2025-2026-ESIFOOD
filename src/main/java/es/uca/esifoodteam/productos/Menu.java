package es.uca.esifoodteam.productos;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "producto_menu")
public class Menu extends Producto {

    @ManyToMany
    @JoinTable(
        name = "menu_simple",
        joinColumns = @JoinColumn(name = "menu_id"),
        inverseJoinColumns = @JoinColumn(name = "simple_id")
    )
    private Set<Simple> productos = new HashSet<>();

    public Menu() {}

    public Set<Simple> getProductos() { return productos; }
    public void setProductos(Set<Simple> productos) { this.productos = productos; }
}
