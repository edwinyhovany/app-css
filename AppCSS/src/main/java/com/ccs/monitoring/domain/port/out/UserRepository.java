// Puerto de salida del dominio para persistir y recupeara usuarios, independientemente de
// la tecnología (JPA, JDC, etc...)
package com.example.hexagonal_users_api.domain.port.out;

import com.example.hexagonal_users_api.domain.model.User;
import java.util.List;

public interface UserRepository {
    List<User> findAll();
}
