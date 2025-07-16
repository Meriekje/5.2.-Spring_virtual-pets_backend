package virtualpets.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import virtualpets.models.Role;
import virtualpets.models.User;
import virtualpets.repositories.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setRole(Role.ROLE_USER);
    }

    @Test
    void createUser_Success() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.createUser(testUser);

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals(Role.ROLE_USER, result.getRole());
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).save(testUser);
    }

    @Test
    void createUser_UsernameAlreadyExists_ThrowsException() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(testUser)
        );

        assertEquals("Username already taken", exception.getMessage());
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findById_UserExists_ReturnsUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userService.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        verify(userRepository).findById(1L);
    }

    @Test
    void findById_UserNotExists_ReturnsEmpty() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.findById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(userRepository).findById(1L);
    }

    @Test
    void findByUsername_UserExists_ReturnsUser() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userService.findByUsername("testuser");

        // Then
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void updateUser_UserExists_UpdatesUser() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.updateUser(testUser);

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).existsById(1L);
        verify(userRepository).save(testUser);
    }

    @Test
    void updateUser_UserNotExists_ThrowsException() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUser(testUser)
        );

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).existsById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_UserExists_DeletesUser() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_UserNotExists_ThrowsException() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteUser(1L)
        );

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).existsById(1L);
        verify(userRepository, never()).deleteById(any());
    }
}