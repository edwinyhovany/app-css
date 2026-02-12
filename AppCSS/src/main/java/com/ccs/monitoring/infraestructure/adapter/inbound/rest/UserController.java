// adaptador REST de entrada para exponer casos de uso de usuarios
package com.example.hexagonal_users_api.infraestructure.adapter.inbound.rest;

import com.example.hexagonal_users_api.application.dto.UserResponse;
import com.example.hexagonal_users_api.application.mapper.UserResponseMapper;
import com.example.hexagonal_users_api.domain.model.User;
import com.example.hexagonal_users_api.domain.port.in.GetAllUsersUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private final GetAllUsersUseCase getAllUsersUseCase;

    public UserController(GetAllUsersUseCase getAllUsersUseCase) {
        this.getAllUsersUseCase = getAllUsersUseCase;
    }

    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        List<User> users = getAllUsersUseCase.getAll(); // Baja hasta abajo y sube con el resultado
        return UserResponseMapper.toResponseList(users);
    }
}
