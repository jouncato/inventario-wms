package com.litethinking.hexagonal.infrastructure.persistence.repository;

import com.litethinking.hexagonal.domain.model.User;
import com.litethinking.hexagonal.domain.port.out.UserRepositoryPort;
import com.litethinking.hexagonal.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserRepositoryJpa jpa;
    private final UserMapper mapper;

    public UserRepositoryAdapter(UserRepositoryJpa jpa, UserMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        return mapper.toDomain(jpa.save(mapper.toEntity(user)));
    }

    @Override
    public List<User> findAll() {
        return jpa.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public void delete(Long id) {
        jpa.deleteById(id);
    }
}