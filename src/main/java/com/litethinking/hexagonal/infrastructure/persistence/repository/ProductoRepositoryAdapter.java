package com.litethinking.hexagonal.infrastructure.persistence.repository;

import com.litethinking.hexagonal.domain.model.Producto;
import com.litethinking.hexagonal.domain.port.out.ProductoRepositoryPort;
import com.litethinking.hexagonal.infrastructure.persistence.mapper.ProductoMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProductoRepositoryAdapter implements ProductoRepositoryPort {

    private final ProductoRepositoryJpa jpa;
    private final ProductoMapper mapper;

    public ProductoRepositoryAdapter(ProductoRepositoryJpa jpa, ProductoMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Producto save(Producto producto) {
        return mapper.toModel(jpa.save(mapper.toEntity(producto)));
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return jpa.findById(id).map(mapper::toModel);
    }

    @Override
    public List<Producto> findAll() {
        return jpa.findAll().stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> findByCategoriaId(Long categoriaId) {
        return jpa.findByCategoria_Id(categoriaId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> findByNombre(String nombre) {
        return jpa.findByNombreContainingIgnoreCase(nombre).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> findByPrecioBetween(BigDecimal min, BigDecimal max) {
        return jpa.findByPrecioBetween(min, max).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Producto> findByCodigoSku(String codigoSku) {
        return jpa.findByCodigoSku(codigoSku).map(mapper::toModel);
    }

    @Override
    public boolean deleteById(Long id) {
        if (!jpa.existsById(id)) return false;
        jpa.deleteById(id);
        return true;
    }
}