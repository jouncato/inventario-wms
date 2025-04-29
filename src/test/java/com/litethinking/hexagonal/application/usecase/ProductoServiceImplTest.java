package com.litethinking.hexagonal.application.usecase;

import com.litethinking.hexagonal.domain.model.Categoria;
import com.litethinking.hexagonal.domain.model.Producto;
import com.litethinking.hexagonal.domain.port.out.ProductoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductoServiceImplTest {

    private ProductoRepositoryPort repository;
    private ProductoServiceImpl service;

    private final Categoria categoria = new Categoria(1L, "Electrónica", "Tecnología");

    @BeforeEach
    void setUp() {
        repository = mock(ProductoRepositoryPort.class);
        service = new ProductoServiceImpl(repository);
    }

    @Test
    void shouldSaveProducto() {
        Producto nuevo = new Producto(null, "Mouse", "Mouse gamer", new BigDecimal("25.00"), 100, categoria, "SKU001");
        Producto saved = new Producto(1L, "Mouse", "Mouse gamer", new BigDecimal("25.00"), 100, categoria, "SKU001");

        when(repository.save(nuevo)).thenReturn(saved);

        Producto result = service.save(nuevo);

        assertThat(result.getId()).isEqualTo(1L);
        verify(repository).save(nuevo);
    }

    @Test
    void shouldReturnProductoById() {
        Producto p = new Producto(1L, "Teclado", "Teclado mecánico", new BigDecimal("50.00"), 50, categoria, "SKU002");

        when(repository.findById(1L)).thenReturn(Optional.of(p));

        Optional<Producto> result = service.getById(1L);

        assertThat(result).isPresent().contains(p);
    }

    @Test
    void shouldReturnEmptyIfProductoNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<Producto> result = service.getById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnAllProductos() {
        List<Producto> productos = List.of(
                new Producto(1L, "TV", "", new BigDecimal("1000"), 10, categoria, "SKU003"),
                new Producto(2L, "Laptop", "", new BigDecimal("1500"), 5, categoria, "SKU004")
        );

        when(repository.findAll()).thenReturn(productos);

        List<Producto> result = service.getAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldDeleteProductoIfExists() {
        when(repository.deleteById(1L)).thenReturn(true);

        boolean result = service.delete(1L);

        assertThat(result).isTrue();
    }

    @Test
    void shouldNotDeleteProductoIfNotExists() {
        when(repository.deleteById(999L)).thenReturn(false);

        boolean result = service.delete(999L);

        assertThat(result).isFalse();
    }

    @Test
    void shouldFindByNombre() {
        List<Producto> resultList = List.of(new Producto(1L, "Mouse", "", new BigDecimal("25"), 10, categoria, "SKU001"));
        when(repository.findByNombre("Mouse")).thenReturn(resultList);

        List<Producto> result = service.findByNombre("Mouse");

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldFindByCategoriaId() {
        when(repository.findByCategoriaId(1L)).thenReturn(Collections.singletonList(
                new Producto(2L, "Monitor", "", new BigDecimal("200"), 15, categoria, "SKU005")));

        List<Producto> result = service.findByCategoria(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldFindByRangoPrecio() {
        when(repository.findByPrecioBetween(new BigDecimal("10"), new BigDecimal("100"))).thenReturn(List.of(
                new Producto(3L, "Cámara", "", new BigDecimal("99.99"), 3, categoria, "SKU006")
        ));

        List<Producto> result = service.findByRangoPrecio(new BigDecimal("10"), new BigDecimal("100"));

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldUpdateStockIfProductoExists() {
        Producto p = new Producto(1L, "Teclado", "", new BigDecimal("50"), 5, categoria, "SKU007");

        when(repository.findById(1L)).thenReturn(Optional.of(p));
        when(repository.save(any())).thenReturn(p);

        boolean result = service.actualizarStock(1L, 99);

        assertThat(result).isTrue();
        verify(repository).save(argThat(prod -> prod.getStock() == 99));
    }

    @Test
    void shouldNotUpdateStockIfProductoDoesNotExist() {
        when(repository.findById(100L)).thenReturn(Optional.empty());

        boolean result = service.actualizarStock(100L, 10);

        assertThat(result).isFalse();
        verify(repository, never()).save(any());
    }
}
