package com.litethinking.hexagonal.infrastructure.persistence.repository;

import com.litethinking.hexagonal.infrastructure.persistence.entity.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepositoryJpa extends JpaRepository<CategoriaEntity, Long> {
    Optional<CategoriaEntity> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}
