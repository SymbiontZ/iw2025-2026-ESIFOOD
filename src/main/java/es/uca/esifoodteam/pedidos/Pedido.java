package es.uca.esifoodteam.pedidos;

import jakarta.persistence.*;

import java.sql.Time;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido {
	@Id
    @GeneratedValue
    private Long id;
    
    private String descripcion;
    private Double precio;
    private Date fecha;
    private Time hora;

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

}
