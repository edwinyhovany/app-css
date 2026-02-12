// El Mapper que convierte entre User (dominio) y UserEntity (persistencia)
package com.example.hexagonal_users_api.infraestructure.adapter.outbound.persistence.mapper;

import com.example.hexagonal_users_api.domain.model.User;
import com.example.hexagonal_users_api.infraestructure.adapter.outbound.persistence.entity.UserEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class UserEntityMapper {
    private UserEntityMapper() { }

    public static User toDomain(UserEntity userEntity) {
        if (userEntity == null) return null;
        return new User(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail()
        );
    }

    public static UserEntity toEntity(User user) {
        if (user == null) return null;
        return new UserEntity(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static List<User> toDomainList(List<UserEntity> entities) {
        if (entities == null || entities.isEmpty()) return Collections.emptyList();
        return entities.stream()
                .map(UserEntityMapper::toDomain)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static List<UserEntity> toEntityList(List<User> users) {
        if (users == null || users.isEmpty()) return Collections.emptyList();
        return users.stream()
                .map(UserEntityMapper::toEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
