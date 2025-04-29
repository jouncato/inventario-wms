package com.litethinking.hexagonal.domain.port.in;

import com.litethinking.hexagonal.domain.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaUseCase {
    Categoria save(Categoria categoria);
    Optional<Categoria> getById(Long id);
    List<Categoria> getAll();
    boolean delete(Long id);
    boolean existsByNombre(String nombre);
}
