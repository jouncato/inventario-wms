package com.litethinking.hexagonal.infrastructure.persistence.mapper;

import com.litethinking.hexagonal.domain.model.User;
import com.litethinking.hexagonal.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre UserEntity (JPA) y User (modelo de dominio).
 */
@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        if (entity == null) return null;

        return new User(
            entity.getId(),
            entity.getName(),
            entity.getEmail()
        );
    }

    public UserEntity toEntity(User domain) {
        if (domain == null) return null;

        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setEmail(domain.getEmail());
        return entity;
    }
}