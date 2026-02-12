// Repositorio JPA para la persistencia de UserEntity, proporciona operaciones CRUD básicas mediante Spring Data JPA
package com.example.hexagonal_users_api.infraestructure.adapter.outbound.persistence.repository;

import com.example.hexagonal_users_api.infraestructure.adapter.outbound.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
}
