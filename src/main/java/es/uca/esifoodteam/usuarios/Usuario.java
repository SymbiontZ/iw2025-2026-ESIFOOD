package es.uca.esifoodteam.usuarios;


import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {
    @GeneratedValue
    @Id
    private Long id;

    


}
