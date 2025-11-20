package es.uca.esifoodteam.pedidos;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import es.uca.esifoodteam.usuarios.Usuario;

@Entity
@Table(name = "pedido")
public class Pedido {
	@Id
    @GeneratedValue
    private Long id;

    @Column(name="price", nullable = false)
    private Double precio;
    
    @Column(name="created_datetime", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name="updated_datetime", nullable = false)
    private LocalDateTime fechaActualizacion;

    @ManyToOne
    @JoinColumn(name="created_by", nullable = false)
    private Usuario creadoPor;

    @ManyToOne
    @JoinColumn(name="updated_by", nullable = false)
    private Usuario actualizadoPor;

    @ManyToOne
    private EstadoPedido estado;

    @OneToMany(mappedBy = "pedido")
    private List<LineaPedido> lineasPedido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Usuario getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(Usuario creadoPor) {
        this.creadoPor = creadoPor;
    }

    public Usuario getActualizadoPor() {
        return actualizadoPor;
    }

    public void setActualizadoPor(Usuario actualizadoPor) {
        this.actualizadoPor = actualizadoPor;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

}
