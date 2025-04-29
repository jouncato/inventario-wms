package com.litethinking.hexagonal.domain.port.out;

import com.litethinking.hexagonal.domain.model.Producto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductoRepositoryPort {
    Producto save(Producto producto);
    Optional<Producto> findById(Long id);
    List<Producto> findAll();
    List<Producto> findByCategoriaId(Long categoriaId);
    List<Producto> findByNombre(String nombre);
    List<Producto> findByPrecioBetween(BigDecimal min, BigDecimal max);
    Optional<Producto> findByCodigoSku(String codigoSku);
    boolean deleteById(Long id);
}
