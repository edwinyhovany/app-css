// Mapper entre el modelo de dominio User y el DTO UserResponse
package com.example.hexagonal_users_api.application.mapper;

import com.example.hexagonal_users_api.application.dto.UserResponse;
import com.example.hexagonal_users_api.domain.model.User;

import java.util.List;
import java.util.Objects;

public final class UserResponseMapper {

    private UserResponseMapper() { }

    public static UserResponse toResponse(User user) {
        if (user == null) return null;
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toDomain(UserResponse userResponse) {
        if (userResponse == null) return null;
        return new User(
                userResponse.getId(),
                userResponse.getName(),
                userResponse.getEmail()
        );
    }

    public static List<UserResponse> toResponseList(List<User> users) {
        if (users == null || users.isEmpty()) return List.of();
        return users.stream()
                .map(UserResponseMapper::toResponse)
                .filter(Objects::nonNull)
                .toList();
    }

    public static List<User> toDomainList(List<UserResponse> userResponses) {
        if (userResponses == null || userResponses.isEmpty()) return List.of();
        return userResponses.stream()
                .map(UserResponseMapper::toDomain)
                .filter(Objects::nonNull)
                .toList();
    }
}
