package com.litethinking.hexagonal.adapter.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.litethinking.hexagonal.domain.model.User;
import com.litethinking.hexagonal.domain.port.in.UserUseCase;
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

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserUseCase userUseCase;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("GET /api/users - lista usuarios")
    void shouldListAllUsers() throws Exception {
        List<User> users = List.of(
                new User(1L, "Ana", "ana@example.com"),
                new User(2L, "Luis", "luis@example.com")
        );

        when(userUseCase.findAll()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].email", is("ana@example.com")));
    }

    @Test
    @DisplayName("GET /api/users/{id} - encontrado")
    void shouldReturnUserById() throws Exception {
        User user = new User(1L, "Ana", "ana@example.com");

        when(userUseCase.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Ana")));
    }

    @Test
    @DisplayName("GET /api/users/{id} - no encontrado")
    void shouldReturn404IfUserNotFound() throws Exception {
        when(userUseCase.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/users - crear usuario")
    void shouldCreateUser() throws Exception {
        User newUser = new User(null, "Marta", "marta@example.com");
        User saved = new User(3L, "Marta", "marta@example.com");

        when(userUseCase.save(any(User.class))).thenReturn(saved);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)));
    }

    @Test
    @DisplayName("DELETE /api/users/{id} - éxito")
    void shouldDeleteUser() throws Exception {
        doNothing().when(userUseCase).delete(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userUseCase).delete(1L);
    }
}