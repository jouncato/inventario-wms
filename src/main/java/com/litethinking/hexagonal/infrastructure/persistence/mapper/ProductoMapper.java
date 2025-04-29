package com.litethinking.hexagonal.infrastructure.persistence.mapper;

import com.litethinking.hexagonal.domain.model.Producto;
import com.litethinking.hexagonal.infrastructure.persistence.entity.ProductoEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    private final CategoriaMapper categoriaMapper;

    public ProductoMapper(CategoriaMapper categoriaMapper) {
        this.categoriaMapper = categoriaMapper;
    }

    public ProductoEntity toEntity(Producto model) {
        if (model == null) return null;

        ProductoEntity entity = new ProductoEntity();
        entity.setId(model.getId());
        entity.setNombre(model.getNombre());
        entity.setDescripcion(model.getDescripcion());
        entity.setPrecio(model.getPrecio());
        entity.setStock(model.getStock());
        entity.setCodigoSku(model.getCodigoSku());
        
        if (model.getCategoria() != null) {
            entity.setCategoria(categoriaMapper.toEntity(model.getCategoria()));
        }
        
        return entity;
    }

    public Producto toModel(ProductoEntity entity) {
        if (entity == null) return null;

        return new Producto(
            entity.getId(),
            entity.getNombre(),
            entity.getDescripcion(),
            entity.getPrecio(),
            entity.getStock(),
            categoriaMapper.toModel(entity.getCategoria()),
            entity.getCodigoSku()
        );
    }
}