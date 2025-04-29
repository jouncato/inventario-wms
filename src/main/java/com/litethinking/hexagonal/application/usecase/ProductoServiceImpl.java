package com.litethinking.hexagonal.application.usecase;

import com.litethinking.hexagonal.domain.model.Producto;
import com.litethinking.hexagonal.domain.port.in.ProductoUseCase;
import com.litethinking.hexagonal.domain.port.out.ProductoRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoUseCase {

    private final ProductoRepositoryPort repository;

    public ProductoServiceImpl(ProductoRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Producto save(Producto producto) {
        return repository.save(producto);
    }

    @Override
    public Optional<Producto> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Producto> getAll() {
        return repository.findAll();
    }

    @Override
    public boolean delete(Long id) {
        return repository.deleteById(id);
    }

    @Override
    public List<Producto> findByCategoria(Long categoriaId) {
        return repository.findByCategoriaId(categoriaId);
    }

    @Override
    public List<Producto> findByNombre(String nombre) {
        return repository.findByNombre(nombre);
    }

    @Override
    public List<Producto> findByRangoPrecio(BigDecimal min, BigDecimal max) {
        return repository.findByPrecioBetween(min, max);
    }

    @Override
    public boolean actualizarStock(Long id, Integer nuevoStock) {
        Optional<Producto> opt = repository.findById(id);
        if (opt.isPresent()) {
            Producto producto = opt.get();
            producto.setStock(nuevoStock);
            repository.save(producto);
            return true;
        }
        return false;
    }
}
