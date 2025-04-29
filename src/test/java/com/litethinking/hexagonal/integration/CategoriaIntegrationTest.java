package com.litethinking.hexagonal.integration;

import com.litethinking.hexagonal.domain.model.Categoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoriaIntegrationTest {

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate;
    private String baseUrl;

    @BeforeEach
    void setup() {
        restTemplate = new RestTemplate();
        baseUrl = "http://localhost:" + port + "/api/categorias";
    }

    @Test
    @DisplayName("Ciclo completo: crear, consultar, actualizar y eliminar una categoría")
    void fullCategoriaLifecycle() {
        Categoria nueva = new Categoria(null, "Herramientas", "Uso general");
        ResponseEntity<Categoria> postResp = restTemplate.postForEntity(baseUrl, nueva, Categoria.class);
        assertThat(postResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Categoria saved = postResp.getBody();
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();

        Categoria encontrada = restTemplate.getForObject(baseUrl + "/" + saved.getId(), Categoria.class);
        assertThat(encontrada.getNombre()).isEqualTo("Herramientas");

        encontrada.setNombre("Ferretería");
        HttpEntity<Categoria> updateEntity = new HttpEntity<>(encontrada);
        ResponseEntity<Categoria> updateResp = restTemplate.exchange(baseUrl + "/" + encontrada.getId(), HttpMethod.PUT, updateEntity, Categoria.class);
        assertThat(updateResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResp.getBody().getNombre()).isEqualTo("Ferretería");

        ResponseEntity<Categoria[]> allResp = restTemplate.getForEntity(baseUrl, Categoria[].class);
        assertThat(allResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(List.of(allResp.getBody())).anyMatch(c -> "Ferretería".equals(c.getNombre()));

        restTemplate.delete(baseUrl + "/" + saved.getId());

        ResponseEntity<String> notFound = restTemplate.getForEntity(baseUrl + "/" + saved.getId(), String.class);
        assertThat(notFound.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}