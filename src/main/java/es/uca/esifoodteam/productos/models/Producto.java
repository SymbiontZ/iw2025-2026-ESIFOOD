package es.uca.esifoodteam.productos.models;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import es.uca.esifoodteam.establecimientos.Establecimiento;
import es.uca.esifoodteam.pedidos.models.LineaPedido;

@Entity
@Table(name = "producto")
@Inheritance(strategy = InheritanceType.JOINED)
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToMany
    @JoinTable(
        name = "producto_ingrediente",
        joinColumns = @JoinColumn(name = "producto_id"),
        inverseJoinColumns = @JoinColumn(name = "ingrediente_id")
    )
    private Set<Ingrediente> ingredientes = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "producto_tipo_producto",
        joinColumns = @JoinColumn(name = "producto_id"),
        inverseJoinColumns = @JoinColumn(name = "tipo_producto_id")
    )
    private Set<TipoProducto> tipos = new HashSet<>();

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String nombre;

    @Size(max = 500)
    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private boolean disponible = true;

    @Column(nullable = true)
    private String imagen_url;

    

    public Producto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public String getImagenUrl() { return imagen_url; }
    public void setImagenUrl(String imagen_url) { this.imagen_url = imagen_url; }

    public Set<TipoProducto> getTipos() { return tipos; }
    public void setTipos(Set<TipoProducto> tipos) { this.tipos = tipos; }

    public Set<Ingrediente> getIngredientes() { return ingredientes; }
    public void setIngredientes(Set<Ingrediente> ingredientes) { this.ingredientes = ingredientes; }
}