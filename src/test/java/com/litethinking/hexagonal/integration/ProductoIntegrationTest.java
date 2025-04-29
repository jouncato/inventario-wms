package com.litethinking.hexagonal.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.litethinking.hexagonal.domain.model.Categoria;
import com.litethinking.hexagonal.domain.model.Producto;
import com.litethinking.hexagonal.domain.port.in.CategoriaUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProductoIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CategoriaUseCase categoriaUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;
    private String categoriasUrl;
    private Categoria savedCategoria;

    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + port + "/api/productos";
        categoriasUrl = "http://localhost:" + port + "/api/categorias";
        
        // Crear una categoría real para las pruebas
        Categoria categoria = new Categoria(null, "TestCat", "Usada en integración");
        savedCategoria = restTemplate.postForObject(categoriasUrl, categoria, Categoria.class);
    }

    @Test
    @DisplayName("Ciclo completo de producto: crear, consultar, actualizar y eliminar")
    void fullProductoLifecycle() {
        // Crear un producto usando la categoría guardada
        Producto producto = new Producto(
            null, 
            "TestProduct", 
            "Descripción de prueba", 
            new BigDecimal("123.45"), 
            10, 
            savedCategoria, 
            "SKU999"
        );

        // 1. Crear producto
        ResponseEntity<Producto> postResp = restTemplate.postForEntity(baseUrl, producto, Producto.class);
        assertThat(postResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Producto savedProducto = postResp.getBody();
        assertThat(savedProducto).isNotNull();
        assertThat(savedProducto.getId()).isNotNull();
        assertThat(savedProducto.getNombre()).isEqualTo("TestProduct");

        // 2. Obtener por ID
        Producto fetchedProducto = restTemplate.getForObject(
            baseUrl + "/" + savedProducto.getId(), 
            Producto.class
        );
        assertThat(fetchedProducto).isNotNull();
        assertThat(fetchedProducto.getNombre()).isEqualTo("TestProduct");
        assertThat(fetchedProducto.getPrecio()).isEqualByComparingTo(new BigDecimal("123.45"));

        // 3. Actualizar producto
        fetchedProducto.setNombre("TestProduct Actualizado");
        fetchedProducto.setPrecio(new BigDecimal("150.00"));
        
        HttpEntity<Producto> requestEntity = new HttpEntity<>(fetchedProducto);
        ResponseEntity<Producto> putResp = restTemplate.exchange(
            baseUrl + "/" + fetchedProducto.getId(),
            HttpMethod.PUT,
            requestEntity,
            Producto.class
        );
        
        assertThat(putResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(putResp.getBody().getNombre()).isEqualTo("TestProduct Actualizado");
        assertThat(putResp.getBody().getPrecio()).isEqualByComparingTo(new BigDecimal("150.00"));

        // 4. Buscar por nombre
        ResponseEntity<Producto[]> searchResp = restTemplate.getForEntity(
            baseUrl + "/buscar?nombre=Actualizado",
            Producto[].class
        );
        
        assertThat(searchResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(searchResp.getBody()).isNotNull();
        assertThat(searchResp.getBody().length).isGreaterThan(0);
        assertThat(searchResp.getBody()[0].getNombre()).contains("Actualizado");

        // 5. Actualizar stock
        restTemplate.put(
            baseUrl + "/" + savedProducto.getId() + "/stock?nuevoStock=42",
            null
        );
        
        Producto updatedStockProducto = restTemplate.getForObject(
            baseUrl + "/" + savedProducto.getId(), 
            Producto.class
        );
        
        assertThat(updatedStockProducto.getStock()).isEqualTo(42);

        // 6. Eliminar producto
        restTemplate.delete(baseUrl + "/" + savedProducto.getId());
        
        // Verificar que se ha eliminado
        ResponseEntity<String> getDeletedResp = restTemplate.getForEntity(
            baseUrl + "/" + savedProducto.getId(),
            String.class
        );
        
        assertThat(getDeletedResp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        
        // 7. Limpiar - eliminar la categoría creada
        restTemplate.delete(categoriasUrl + "/" + savedCategoria.getId());
    }
}