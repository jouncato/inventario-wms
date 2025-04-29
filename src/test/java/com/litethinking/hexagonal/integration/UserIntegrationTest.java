package com.litethinking.hexagonal.integration;

import com.litethinking.hexagonal.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private User testUser;

    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + port + "/api/users";
        // Crear un usuario con nombre único para evitar conflictos entre pruebas
        String uniqueName = "User_" + UUID.randomUUID().toString().substring(0, 8);
        testUser = new User(null, uniqueName, uniqueName.toLowerCase() + "@example.com");
    }

    @Test
    @DisplayName("Debería realizar el ciclo completo de un usuario: crear, consultar, actualizar y eliminar")
    void fullUserFlow() {
        // 1. Crear usuario
        ResponseEntity<User> postResp = restTemplate.postForEntity(baseUrl, testUser, User.class);
        assertThat(postResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        User saved = postResp.getBody();
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo(testUser.getName());
        assertThat(saved.getEmail()).isEqualTo(testUser.getEmail());

        // 2. Consultar por ID
        User fetched = restTemplate.getForObject(baseUrl + "/" + saved.getId(), User.class);
        assertThat(fetched).isNotNull();
        assertThat(fetched.getId()).isEqualTo(saved.getId());
        assertThat(fetched.getName()).isEqualTo(testUser.getName());
        assertThat(fetched.getEmail()).isEqualTo(testUser.getEmail());

        // 3. Consultar todos los usuarios
        ResponseEntity<User[]> allResp = restTemplate.getForEntity(baseUrl, User[].class);
        assertThat(allResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<User> users = Arrays.asList(allResp.getBody());
        assertThat(users).isNotEmpty();
        assertThat(users).anyMatch(u -> u.getId().equals(saved.getId()));

        // 4. Actualizar usuario
        saved.setName(saved.getName() + " Updated");
        saved.setEmail("updated." + saved.getEmail());
        
        HttpEntity<User> requestEntity = new HttpEntity<>(saved);
        ResponseEntity<User> putResp = restTemplate.exchange(
            baseUrl + "/" + saved.getId(),
            HttpMethod.PUT,
            requestEntity,
            User.class
        );
        
        assertThat(putResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        User updated = putResp.getBody();
        assertThat(updated).isNotNull();
        assertThat(updated.getName()).contains("Updated");
        assertThat(updated.getEmail()).startsWith("updated.");

        // 5. Eliminar usuario
        restTemplate.delete(baseUrl + "/" + saved.getId());

        // 6. Verificar que fue eliminado
        ResponseEntity<String> notFoundResp = restTemplate.getForEntity(
            baseUrl + "/" + saved.getId(), 
            String.class
        );
        assertThat(notFoundResp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}