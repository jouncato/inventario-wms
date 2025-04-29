package com.litethinking.hexagonal.infrastructure.persistence.repository;

import com.litethinking.hexagonal.domain.model.Categoria;
import com.litethinking.hexagonal.infrastructure.persistence.entity.CategoriaEntity;
import com.litethinking.hexagonal.infrastructure.persistence.mapper.CategoriaMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({CategoriaRepositoryAdapter.class, CategoriaMapper.class})
class CategoriaRepositoryAdapterTest {

    @Autowired
    private CategoriaRepositoryJpa jpa;

    @Autowired
    private CategoriaRepositoryAdapter adapter;

    @Test
    @DisplayName("Debería guardar una categoría")
    void shouldSaveCategoria() {
        // Arrange
        Categoria categoria = new Categoria(null, "Test", "Descripción de prueba");
        
        // Act
        Categoria saved = adapter.save(categoria);
        
        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getNombre()).isEqualTo("Test");
        assertThat(saved.getDescripcion()).isEqualTo("Descripción de prueba");
        
        // Verify in repository
        Optional<CategoriaEntity> foundEntity = jpa.findById(saved.getId());
        assertThat(foundEntity).isPresent();
        assertThat(foundEntity.get().getNombre()).isEqualTo("Test");
    }

    @Test
    @DisplayName("Debería encontrar todas las categorías")
    void shouldFindAllCategorias() {
        // Arrange
        CategoriaEntity entity1 = new CategoriaEntity(null, "Cat1", "Desc1");
        CategoriaEntity entity2 = new CategoriaEntity(null, "Cat2", "Desc2");
        jpa.save(entity1);
        jpa.save(entity2);
        
        // Act
        List<Categoria> categorias = adapter.findAll();
        
        // Assert
        assertThat(categorias).hasSize(2);
        assertThat(categorias).extracting(Categoria::getNombre).containsExactlyInAnyOrder("Cat1", "Cat2");
    }

    @Test
    @DisplayName("Debería encontrar categoría por ID")
    void shouldFindById() {
        // Arrange
        CategoriaEntity entity = new CategoriaEntity(null, "FindById", "Test");
        entity = jpa.save(entity);
        
        // Act
        Optional<Categoria> found = adapter.findById(entity.getId());
        
        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("FindById");
    }

    @Test
    @DisplayName("Debería encontrar categoría por nombre")
    void shouldFindByNombre() {
        // Arrange
        CategoriaEntity entity = new CategoriaEntity(null, "FindByNombre", "Test");
        jpa.save(entity);
        
        // Act
        Optional<Categoria> found = adapter.findByNombre("FindByNombre");
        
        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("FindByNombre");
    }

    @Test
    @DisplayName("Debería verificar si existe por nombre")
    void shouldCheckIfExistsByNombre() {
        // Arrange
        CategoriaEntity entity = new CategoriaEntity(null, "Exists", "Test");
        jpa.save(entity);
        
        // Act
        boolean exists = adapter.existsByNombre("Exists");
        boolean notExists = adapter.existsByNombre("NotExists");
        
        // Assert
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("Debería eliminar por ID")
    void shouldDeleteById() {
        // Arrange
        CategoriaEntity entity = new CategoriaEntity(null, "ToDelete", "Test");
        entity = jpa.save(entity);
        Long id = entity.getId();
        
        // Act
        boolean deleted = adapter.deleteById(id);
        
        // Assert
        assertThat(deleted).isTrue();
        assertThat(jpa.existsById(id)).isFalse();
    }

    @Test
    @DisplayName("No debería eliminar si no existe")
    void shouldNotDeleteIfNotExists() {
        // Act
        boolean deleted = adapter.deleteById(9999L);
        
        // Assert
        assertThat(deleted).isFalse();
    }
}