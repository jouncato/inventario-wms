package com.litethinking.hexagonal.domain.port.in;

import com.litethinking.hexagonal.domain.model.Producto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductoUseCase {
    Producto save(Producto producto);
    Optional<Producto> getById(Long id);
    List<Producto> getAll();
    boolean delete(Long id);
    List<Producto> findByCategoria(Long categoriaId);
    List<Producto> findByNombre(String nombre);
    List<Producto> findByRangoPrecio(BigDecimal min, BigDecimal max);
    boolean actualizarStock(Long id, Integer nuevoStock);
}
