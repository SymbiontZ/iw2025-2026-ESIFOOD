package es.uca.esifoodteam.pedidos.models;

import jakarta.persistence.*;

@Entity
@Table(name = "estado_pedido")
public class EstadoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String nombre;

    // Constructor vacío requerido por JPA
    public EstadoPedido() {}

    // Constructor con parámetros
    public EstadoPedido(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
