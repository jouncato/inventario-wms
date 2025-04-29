package com.litethinking.hexagonal.domain.service;

import com.litethinking.hexagonal.domain.model.User;
import com.litethinking.hexagonal.domain.port.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // <--- ¡AÑADE ESTO!
public class UserDomainService {

    private final UserRepositoryPort repository;

    public UserDomainService(UserRepositoryPort repository) {
        this.repository = repository;
    }

    public User save(User user) {
        return repository.save(user);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}
