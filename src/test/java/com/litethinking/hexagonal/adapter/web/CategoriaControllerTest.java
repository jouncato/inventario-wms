package com.litethinking.hexagonal.adapter.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.litethinking.hexagonal.domain.model.Categoria;
import com.litethinking.hexagonal.domain.port.in.CategoriaUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoriaControllerTest {

    @Mock
    private CategoriaUseCase categoriaUseCase;

    @InjectMocks
    private CategoriaController categoriaController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/categorias - lista todas")
    void shouldReturnAllCategorias() throws Exception {
        // Configuración del MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(categoriaController).build();
        
        // Datos de prueba
        List<Categoria> categorias = List.of(
                new Categoria(1L, "Ropa", "Prendas"),
                new Categoria(2L, "Tecnología", "Gadgets"));

        when(categoriaUseCase.getAll()).thenReturn(categorias);

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].nombre", is("Ropa")));
    }

    @Test
    @DisplayName("GET /api/categorias/{id} - encontrada")
    void shouldReturnCategoriaById() throws Exception {
        // Configuración del MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(categoriaController).build();
        
        Categoria categoria = new Categoria(1L, "Hogar", "Casa");

        when(categoriaUseCase.getById(1L)).thenReturn(Optional.of(categoria));

        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Hogar")));
    }

    @Test
    @DisplayName("GET /api/categorias/{id} - no encontrada")
    void shouldReturn404IfCategoriaNotFound() throws Exception {
        // Configuración del MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(categoriaController).build();
        
        when(categoriaUseCase.getById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categorias/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/categorias - crear")
    void shouldCreateCategoria() throws Exception {
        // Configuración del MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(categoriaController).build();
        
        Categoria categoria = new Categoria(null, "Libros", "Lectura");
        Categoria saved = new Categoria(3L, "Libros", "Lectura");

        when(categoriaUseCase.existsByNombre("Libros")).thenReturn(false);
        when(categoriaUseCase.save(any())).thenReturn(saved);

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)));
    }

    @Test
    @DisplayName("POST /api/categorias - conflicto por nombre duplicado")
    void shouldReturnConflictIfNombreExists() throws Exception {
        // Configuración del MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(categoriaController).build();
        
        Categoria categoria = new Categoria(null, "Electrónica", "Duplicada");

        when(categoriaUseCase.existsByNombre("Electrónica")).thenReturn(true);

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("PUT /api/categorias/{id} - actualizar")
    void shouldUpdateCategoria() throws Exception {
        // Configuración del MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(categoriaController).build();
        
        Categoria existente = new Categoria(1L, "Hogar", "Casa");
        Categoria actualizada = new Categoria(1L, "Casa", "Mejorada");

        when(categoriaUseCase.getById(1L)).thenReturn(Optional.of(existente));
        when(categoriaUseCase.existsByNombre("Casa")).thenReturn(false);
        when(categoriaUseCase.save(any())).thenReturn(actualizada);

        mockMvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Casa")));
    }

    @Test
    @DisplayName("DELETE /api/categorias/{id} - éxito")
    void shouldDeleteCategoria() throws Exception {
        // Configuración del MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(categoriaController).build();
        
        when(categoriaUseCase.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/categorias/{id} - no encontrada")
    void shouldReturnNotFoundOnDeleteIfMissing() throws Exception {
        // Configuración del MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(categoriaController).build();
        
        when(categoriaUseCase.delete(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/categorias/99"))
                .andExpect(status().isNotFound());
    }
}