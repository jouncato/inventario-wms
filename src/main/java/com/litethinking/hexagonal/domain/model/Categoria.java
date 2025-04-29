package com.litethinking.hexagonal.domain.model;

import java.util.Objects;

/**
 * Modelo de dominio para representar una categoría.
 * Clase inmutable en su construcción con mutadores explícitos.
 */
public class Categoria {

    private Long id;
    private String nombre;
    private String descripcion;

    public Categoria() {}

    public Categoria(Long id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
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

    // Equals and hashCode (importante para comparaciones en listas, sets, etc.)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria)) return false;
        Categoria that = (Categoria) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(nombre, that.nombre) &&
               Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, descripcion);
    }

    // toString
    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
