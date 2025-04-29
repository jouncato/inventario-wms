package com.litethinking.hexagonal.application.usecase;

import com.litethinking.hexagonal.domain.model.Categoria;
import com.litethinking.hexagonal.domain.port.out.CategoriaRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepositoryPort repository;

    @InjectMocks
    private CategoriaServiceImpl service;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria(1L, "Electrónica", "Equipos y gadgets");
    }

    @Test
    @DisplayName("Debería guardar una categoría correctamente")
    void shouldSaveCategoria() {
        // Arrange
        Categoria nuevaCategoria = new Categoria(null, "Electrónica", "Equipos y gadgets");
        when(repository.save(nuevaCategoria)).thenReturn(categoria);

        // Act
        Categoria result = service.save(nuevaCategoria);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        verify(repository).save(nuevaCategoria);
    }

    @Test
    @DisplayName("Debería encontrar una categoría por ID cuando existe")
    void shouldReturnCategoriaByIdWhenExists() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(categoria));

        // Act
        Optional<Categoria> result = service.getById(1L);

        // Assert
        assertThat(result).isPresent().contains(categoria);
    }

    @Test
    @DisplayName("Debería retornar vacío cuando la categoría no existe")
    void shouldReturnEmptyWhenCategoriaNotExists() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Categoria> result = service.getById(99L);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Debería retornar todas las categorías")
    void shouldReturnAllCategorias() {
        // Arrange
        List<Categoria> categorias = List.of(
            new Categoria(1L, "Ropa", "Textiles"),
            new Categoria(2L, "Alimentos", "Comida envasada")
        );
        when(repository.findAll()).thenReturn(categorias);

        // Act
        List<Categoria> result = service.getAll();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getNombre()).isEqualTo("Ropa");
        assertThat(result.get(1).getNombre()).isEqualTo("Alimentos");
    }

    @Test
    @DisplayName("Debería verificar si una categoría existe por nombre")
    void shouldReturnTrueIfCategoriaExistsByName() {
        // Arrange
        when(repository.existsByNombre("Ropa")).thenReturn(true);

        // Act
        boolean exists = service.existsByNombre("Ropa");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Debería eliminar una categoría si existe")
    void shouldDeleteCategoriaIfExists() {
        // Arrange
        when(repository.deleteById(1L)).thenReturn(true);

        // Act
        boolean result = service.delete(1L);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("No debería eliminar si la categoría no existe")
    void shouldNotDeleteCategoriaIfNotExists() {
        // Arrange
        when(repository.deleteById(999L)).thenReturn(false);

        // Act
        boolean result = service.delete(999L);

        // Assert
        assertThat(result).isFalse();
    }
}