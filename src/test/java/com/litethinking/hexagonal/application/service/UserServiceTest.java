package com.litethinking.hexagonal.application.service;

import com.litethinking.hexagonal.domain.model.User;
import com.litethinking.hexagonal.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "Carlos", "carlos@example.com");
    }

    @Test
    @DisplayName("Debería guardar un usuario correctamente")
    void shouldSaveUser() {
        // Arrange
        User newUser = new User(null, "Carlos", "carlos@example.com");
        when(userRepository.save(newUser)).thenReturn(testUser);

        // Act
        User result = userService.save(newUser);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Carlos");
        assertThat(result.getEmail()).isEqualTo("carlos@example.com");
        verify(userRepository).save(newUser);
    }

    @Test
    @DisplayName("Debería encontrar un usuario por ID")
    void shouldFindUserById() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = userService.findById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Carlos");
        assertThat(result.get().getEmail()).isEqualTo("carlos@example.com");
    }

    @Test
    @DisplayName("Debería retornar Optional vacío si el usuario no existe")
    void shouldReturnEmptyIfUserNotFound() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findById(99L);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Debería retornar todos los usuarios")
    void shouldReturnAllUsers() {
        // Arrange
        List<User> users = List.of(
                testUser,
                new User(2L, "Laura", "laura@mail.com")
        );
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.findAll();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Carlos");
        assertThat(result.get(1).getName()).isEqualTo("Laura");
    }

    @Test
    @DisplayName("Debería eliminar un usuario correctamente")
    void shouldDeleteUser() {
        // Arrange
        doNothing().when(userRepository).delete(1L);

        // Act
        userService.delete(1L);

        // Assert
        verify(userRepository).delete(1L);
    }
}