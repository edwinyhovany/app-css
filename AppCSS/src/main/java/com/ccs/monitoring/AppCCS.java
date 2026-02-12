// Implementación sin mapstruct
// opción 1: crear ficherro data.sql

package com.example.hexagonal_users_api;

import com.example.hexagonal_users_api.infraestructure.adapter.outbound.persistence.entity.UserEntity;
import com.example.hexagonal_users_api.infraestructure.adapter.outbound.persistence.repository.UserJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HexagonalUsersApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(HexagonalUsersApiApplication.class, args);
	}

	// opción 2: CommadnLineRunner para cargar datos de prueba al iniciar la aplicación, útil para desarrollo y pruebas
	@Bean
	CommandLineRunner seedData(UserJpaRepository repo) {
		return args -> {
			if (repo.count() == 0) {
				repo.save(new UserEntity(null, "Alice", "alice@example.com"));
				repo.save(new UserEntity(null, "Bob", "bob@example.com"));
				repo.save(new UserEntity(null, "Charlie", "charlie@example.com"));
			}
		};
	}

}
