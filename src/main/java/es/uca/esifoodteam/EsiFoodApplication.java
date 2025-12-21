package es.uca.esifoodteam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "es.uca.esifoodteam.**")
@EntityScan(basePackages = "es.uca.esifoodteam.**")
public class EsiFoodApplication {
    public static void main(String[] args) {
        SpringApplication.run(EsiFoodApplication.class, args);
    }
}