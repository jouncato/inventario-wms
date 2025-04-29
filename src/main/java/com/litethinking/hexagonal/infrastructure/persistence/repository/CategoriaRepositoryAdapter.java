package com.litethinking.hexagonal.infrastructure.persistence.repository;

import com.litethinking.hexagonal.domain.model.Categoria;
import com.litethinking.hexagonal.domain.port.out.CategoriaRepositoryPort;
import com.litethinking.hexagonal.infrastructure.persistence.entity.CategoriaEntity;
import com.litethinking.hexagonal.infrastructure.persistence.mapper.CategoriaMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CategoriaRepositoryAdapter implements CategoriaRepositoryPort {

    private final CategoriaRepositoryJpa jpa;
    private final CategoriaMapper mapper;

    public CategoriaRepositoryAdapter(CategoriaRepositoryJpa jpa, CategoriaMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Categoria save(Categoria categoria) {
        CategoriaEntity entity = mapper.toEntity(categoria);
        CategoriaEntity saved = jpa.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public Optional<Categoria> findById(Long id) {
        return jpa.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<Categoria> findByNombre(String nombre) {
        return jpa.findByNombre(nombre).map(mapper::toModel);
    }

    @Override
    public List<Categoria> findAll() {
        return jpa.findAll().stream()
                  .map(mapper::toModel)
                  .collect(Collectors.toList());
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return jpa.existsByNombre(nombre);
    }

    @Override
    public boolean deleteById(Long id) {
        if (!jpa.existsById(id)) return false;
        jpa.deleteById(id);
        return true;
    }
}