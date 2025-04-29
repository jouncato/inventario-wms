package com.litethinking.hexagonal.infrastructure.persistence.repository;

import com.litethinking.hexagonal.infrastructure.persistence.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductoRepositoryJpa extends JpaRepository<ProductoEntity, Long> {
    List<ProductoEntity> findByCategoria_Id(Long categoriaId);
    List<ProductoEntity> findByNombreContainingIgnoreCase(String nombre);
    List<ProductoEntity> findByPrecioBetween(BigDecimal min, BigDecimal max);
    Optional<ProductoEntity> findByCodigoSku(String sku);
}
