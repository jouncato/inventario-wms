package com.litethinking.hexagonal.adapter.web;

import com.litethinking.hexagonal.domain.model.Producto;
import com.litethinking.hexagonal.domain.port.in.ProductoUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@Validated
public class ProductoController {

    private final ProductoUseCase productoService;

    public ProductoController(ProductoUseCase productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> getAll() {
        return ResponseEntity.ok(productoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Long id) {
        return productoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> create(@Valid @RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productoService.save(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        Optional<Producto> existingOpt = productoService.getById(id);
        if (existingOpt.isPresent()) {
            producto.setId(id); // Aseguramos que el ID sea el mismo
            return ResponseEntity.ok(productoService.save(producto));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Void> actualizarStock(
            @PathVariable Long id,
            @RequestParam @Min(value = 0, message = "El stock no puede ser negativo") Integer nuevoStock) {
        boolean actualizado = productoService.actualizarStock(id, nuevoStock);
        return actualizado ? ResponseEntity.noContent().build()
                           : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean eliminado = productoService.delete(id);
        return eliminado ? ResponseEntity.noContent().build()
                         : ResponseEntity.notFound().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.findByNombre(nombre));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> buscarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.findByCategoria(categoriaId));
    }

    @GetMapping("/precio")
    public ResponseEntity<List<Producto>> buscarPorRangoPrecio(
            @RequestParam @DecimalMin(value = "0.0", inclusive = true) BigDecimal min,
            @RequestParam @DecimalMin(value = "0.0", inclusive = true) BigDecimal max) {
        if (min.compareTo(max) > 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(productoService.findByRangoPrecio(min, max));
    }
}