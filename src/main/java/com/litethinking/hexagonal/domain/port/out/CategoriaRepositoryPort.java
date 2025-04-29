package com.litethinking.hexagonal.domain.port.out;

import com.litethinking.hexagonal.domain.model.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaRepositoryPort {
    Categoria save(Categoria categoria);
    Optional<Categoria> findById(Long id);
    Optional<Categoria> findByNombre(String nombre);
    List<Categoria> findAll();
    boolean existsByNombre(String nombre);
    boolean deleteById(Long id);
}