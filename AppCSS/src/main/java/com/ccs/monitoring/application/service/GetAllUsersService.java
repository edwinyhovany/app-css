// Implementación del caso de uso GetAllUsersUseCase , que orquesta la obtención
// de usuarios desde el repositorio.
package com.example.hexagonal_users_api.application.service;

import com.example.hexagonal_users_api.domain.model.User;
import com.example.hexagonal_users_api.domain.port.in.GetAllUsersUseCase;
import com.example.hexagonal_users_api.domain.port.out.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllUsersService implements GetAllUsersUseCase {
    private final UserRepository userRepository;

    public GetAllUsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
