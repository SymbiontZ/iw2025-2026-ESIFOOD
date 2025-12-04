package es.uca.esifoodteam.pedidos.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import es.uca.esifoodteam.establecimientos.Establecimiento;
import es.uca.esifoodteam.usuarios.Usuario;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id", nullable = false)
    private EstadoPedido estado;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActualizacionPedido> actualizaciones = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineaPedido> lineas = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "establecimiento_id", nullable = false)
    private Establecimiento establecimiento;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private LocalDateTime fechaHora;  

    @Column(length = 500)
    private String observaciones;

    // Constructor vacío requerido por JPA
    public Pedido() {}

    // Constructor con parámetros
    public Pedido(EstadoPedido estado, Usuario usuario, BigDecimal precio, LocalDateTime fechaHora, String observaciones) {
        this.estado = estado;
        this.usuario = usuario;
        this.precio = precio;
        this.fechaHora = fechaHora;
        this.observaciones = observaciones;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public List<ActualizacionPedido> getActualizaciones() { return actualizaciones; }
    public void setActualizaciones(List<ActualizacionPedido> actualizaciones) { this.actualizaciones = actualizaciones; }

    public List<LineaPedido> getLineas() { return lineas; }
    public void setLineas(List<LineaPedido> lineas) { this.lineas = lineas; }

    public Establecimiento getLocal() { return establecimiento; }
    public void setLocal(Establecimiento establecimiento) { this.establecimiento = establecimiento; }
}
