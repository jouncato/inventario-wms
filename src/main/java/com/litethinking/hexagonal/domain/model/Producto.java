package com.litethinking.hexagonal.domain.model;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Modelo de dominio para representar un producto en el sistema de inventario.
 */
public class Producto {

    private Long id;
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    private String nombre;
    
    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    private String descripcion;
    
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;
    
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
    
    @NotNull(message = "La categoría es obligatoria")
    private Categoria categoria;
    
    @NotBlank(message = "El código SKU es obligatorio")
    @Pattern(regexp = "^[A-Z0-9]{3,50}$", message = "El SKU debe contener entre 3 y 50 caracteres alfanuméricos en mayúsculas")
    private String codigoSku;

    public Producto() {}

    public Producto(Long id, String nombre, String descripcion, BigDecimal precio,
                    Integer stock, Categoria categoria, String codigoSku) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.codigoSku = codigoSku;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public Integer getStock() {
        return stock;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getCodigoSku() {
        return codigoSku;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setCodigoSku(String codigoSku) {
        this.codigoSku = codigoSku;
    }

    // Equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto producto = (Producto) o;
        return Objects.equals(id, producto.id) &&
               Objects.equals(nombre, producto.nombre) &&
               Objects.equals(descripcion, producto.descripcion) &&
               Objects.equals(precio, producto.precio) &&
               Objects.equals(stock, producto.stock) &&
               Objects.equals(categoria, producto.categoria) &&
               Objects.equals(codigoSku, producto.codigoSku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, descripcion, precio, stock, categoria, codigoSku);
    }

    // toString
    @Override
    public String toString() {
        return "Producto{" +
               "id=" + id +
               ", nombre='" + nombre + '\'' +
               ", descripcion='" + descripcion + '\'' +
               ", precio=" + precio +
               ", stock=" + stock +
               ", categoria=" + categoria +
               ", codigoSku='" + codigoSku + '\'' +
               '}';
    }
}