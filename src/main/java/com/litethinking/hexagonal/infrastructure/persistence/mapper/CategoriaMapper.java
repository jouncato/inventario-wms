package com.litethinking.hexagonal.infrastructure.persistence.mapper;

import com.litethinking.hexagonal.domain.model.Categoria;
import com.litethinking.hexagonal.infrastructure.persistence.entity.CategoriaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper que convierte entre Categoria del dominio y CategoriaEntity de JPA.
 */
@Component
public class CategoriaMapper {

    public CategoriaEntity toEntity(Categoria model) {
        if (model == null) return null;

        CategoriaEntity entity = new CategoriaEntity();
        entity.setId(model.getId());
        entity.setNombre(model.getNombre());
        entity.setDescripcion(model.getDescripcion());
        return entity;
    }

    public Categoria toModel(CategoriaEntity entity) {
        if (entity == null) return null;

        return new Categoria(
            entity.getId(),
            entity.getNombre(),
            entity.getDescripcion()
        );
    }
}