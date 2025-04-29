package com.litethinking.hexagonal.infrastructure.persistence.repository;

import com.litethinking.hexagonal.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryJpa extends JpaRepository<UserEntity, Long> {
}