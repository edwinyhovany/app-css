// Adaptador de salida que implementa UserRepository y delega el acceso a datos en UserJpaRepository, desacoplando
// el dominio de la capa de persistencia
package com.example.hexagonal_users_api.infraestructure.adapter.outbound.persistence;

import com.example.hexagonal_users_api.domain.model.User;
import com.example.hexagonal_users_api.domain.port.out.UserRepository;
import com.example.hexagonal_users_api.infraestructure.adapter.outbound.persistence.entity.UserEntity;
import com.example.hexagonal_users_api.infraestructure.adapter.outbound.persistence.mapper.UserEntityMapper;
import com.example.hexagonal_users_api.infraestructure.adapter.outbound.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public List<User> findAll() {
        List<UserEntity> entities = userJpaRepository.findAll();
        return UserEntityMapper.toDomainList(entities);
    }
}
