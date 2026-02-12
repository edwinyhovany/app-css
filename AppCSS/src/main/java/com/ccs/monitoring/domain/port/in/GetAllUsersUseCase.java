// Puerto de entrada del dominio que expone el caso de uso para obtener todos los usuarios
// independientemente de la capa de presentación.
package com.example.hexagonal_users_api.domain.port.in;

import com.example.hexagonal_users_api.domain.model.User;
import java.util.List;

public interface GetAllUsersUseCase {
    List<User> getAll();
}
