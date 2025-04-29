package com.litethinking.hexagonal.adapter.web;

import com.litethinking.hexagonal.domain.model.Categoria;
import com.litethinking.hexagonal.domain.port.in.CategoriaUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaUseCase categoriaService;

    public CategoriaController(CategoriaUseCase categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> getAll() {
        return ResponseEntity.ok(categoriaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getById(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaService.getById(id);
        return categoria.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Categoria> create(@RequestBody Categoria categoria) {
        if (categoriaService.existsByNombre(categoria.getNombre())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Categoria created = categoriaService.save(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> update(@PathVariable Long id, @RequestBody Categoria categoria) {
        Optional<Categoria> existing = categoriaService.getById(id);
        if (existing.isPresent()) {
            Categoria existingCat = existing.get();

            // Si cambia el nombre y ya existe otro con ese nombre
            if (!existingCat.getNombre().equals(categoria.getNombre())
                && categoriaService.existsByNombre(categoria.getNombre())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            existingCat.setNombre(categoria.getNombre());
            existingCat.setDescripcion(categoria.getDescripcion());
            return ResponseEntity.ok(categoriaService.save(existingCat));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = categoriaService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
