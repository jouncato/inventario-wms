package com.litethinking.hexagonal.adapter.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.litethinking.hexagonal.domain.model.Categoria;
import com.litethinking.hexagonal.domain.model.Producto;
import com.litethinking.hexagonal.domain.port.in.ProductoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoUseCase productoUseCase;

    @InjectMocks
    private ProductoController productoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Categoria categoria;
    private Producto producto;

    @BeforeEach
    void setup() {
        // Configuración del MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(productoController).build();
        
        // Datos de prueba
        categoria = new Categoria(1L, "Electrónica", "Tecnología");
        producto = new Producto(1L, "Mouse", "Inalámbrico", new BigDecimal("25"), 100, categoria, "MOU001");
    }

    @Test
    @DisplayName("GET /api/productos - listar todos")
    void shouldListAllProducts() throws Exception {
        when(productoUseCase.getAll()).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Mouse"));
    }

    @Test
    @DisplayName("GET /api/productos/{id} - encontrar por id")
    void shouldGetProductById() throws Exception {
        when(productoUseCase.getById(1L)).thenReturn(Optional.of(producto));

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Mouse"));
    }

    @Test
    @DisplayName("POST /api/productos - crear producto")
    void shouldCreateProducto() throws Exception {
        Producto newProducto = new Producto(null, "Mouse", "Inalámbrico", new BigDecimal("25"), 100, categoria, "MOU001");
        
        when(productoUseCase.save(any())).thenReturn(producto);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProducto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /api/productos/buscar?nombre=mou")
    void shouldFindByNombre() throws Exception {
        when(productoUseCase.findByNombre("mou")).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos/buscar?nombre=mou"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Mouse"));
    }

    @Test
    @DisplayName("GET /api/productos/categoria/1")
    void shouldFindByCategoria() throws Exception {
        when(productoUseCase.findByCategoria(1L)).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos/categoria/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoria.id").value(1));
    }

    @Test
    @DisplayName("GET /api/productos/precio?min=10&max=50")
    void shouldFindByRangoPrecio() throws Exception {
        when(productoUseCase.findByRangoPrecio(new BigDecimal("10"), new BigDecimal("50")))
            .thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos/precio?min=10&max=50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].precio").value(25));
    }

    @Test
    @DisplayName("PUT /api/productos/{id}/stock - actualizar stock")
    void shouldUpdateStock() throws Exception {
        when(productoUseCase.actualizarStock(1L, 42)).thenReturn(true);

        mockMvc.perform(put("/api/productos/1/stock?nuevoStock=42"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PUT /api/productos/{id}/stock - producto no encontrado")
    void shouldReturnNotFoundIfStockUpdateFails() throws Exception {
        when(productoUseCase.actualizarStock(99L, 10)).thenReturn(false);

        mockMvc.perform(put("/api/productos/99/stock?nuevoStock=10"))
                .andExpect(status().isNotFound());
    }
}