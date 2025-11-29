package es.uca.esifoodteam.locales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalRepository extends JpaRepository<Local, Long> {

    List<Local> findByNombreContainingIgnoreCase(String nombre);

    List<Local> findByEstaAbiertoTrue();
}
