package es.uca.esifoodteam.establecimientos;


import java.util.ArrayList;
import java.util.List;

import es.uca.esifoodteam.productos.models.Producto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "establecimiento")
public class Establecimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 255)
    private String direccion;

    @Column(nullable = true)
    private Boolean estaDisponible = true;

    public Establecimiento() {}

    public Establecimiento(String nombre, String direccion){
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Boolean getEstaDisponible() { return estaDisponible; }
    public void setEstaDisponible(Boolean estaDisponible) { this.estaDisponible = estaDisponible; }
}


