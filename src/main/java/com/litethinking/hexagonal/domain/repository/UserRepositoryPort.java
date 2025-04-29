
package com.litethinking.hexagonal.domain.repository;

import com.litethinking.hexagonal.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    List<User> findAll();
    Optional<User> findById(Long id);
    void delete(Long id);
}
