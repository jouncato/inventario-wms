package com.litethinking.hexagonal.application.usecase;

import com.litethinking.hexagonal.domain.model.Categoria;
import com.litethinking.hexagonal.domain.port.in.CategoriaUseCase;
import com.litethinking.hexagonal.domain.port.out.CategoriaRepositoryPort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServiceImpl implements CategoriaUseCase {

    private final CategoriaRepositoryPort repo;

    public CategoriaServiceImpl(CategoriaRepositoryPort repo) {
        this.repo = repo;
    }

    public Categoria save(Categoria categoria) {
        return repo.save(categoria);
    }

    public Optional<Categoria> getById(Long id) {
        return repo.findById(id);
    }

    public List<Categoria> getAll() {
        return repo.findAll();
    }

    public boolean delete(Long id) {
        return repo.deleteById(id);
    }

    public boolean existsByNombre(String nombre) {
        return repo.existsByNombre(nombre);
    }
}
