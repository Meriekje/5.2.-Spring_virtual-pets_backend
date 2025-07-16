package virtualpets.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import virtualpets.config.SecurityConfig;
import virtualpets.dtos.UserCreateDTO;
import virtualpets.models.Role;
import virtualpets.models.User;
import virtualpets.security.CustomUserDetailsService;
import virtualpets.security.JwtAuthenticationFilter;
import virtualpets.security.JwtTokenProvider;
import virtualpets.services.UserService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private UserCreateDTO userCreateDTO;
    private User testUser;

    @BeforeEach
    void setUp() {
        userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername("testuser");
        userCreateDTO.setPassword("password123");

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setRole(Role.ROLE_USER);
    }

    @Test
    void register_Success() throws Exception {
        // Given
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userService.createUser(any(User.class))).thenReturn(testUser);

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));

        verify(passwordEncoder).encode("password123");
        verify(userService).createUser(any(User.class));
    }

    @Test
    void register_UsernameAlreadyExists_ReturnsBadRequest() throws Exception {
        // Given
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userService.createUser(any(User.class)))
                .thenThrow(new IllegalArgumentException("Username already taken"));

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username already taken"));

        verify(userService).createUser(any(User.class));
    }

    @Test
    void register_InvalidInput_ReturnsBadRequest() throws Exception {
        // Given
        UserCreateDTO invalidUser = new UserCreateDTO();
        invalidUser.setUsername("ab"); // Too short
        invalidUser.setPassword("123"); // Too short

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors.username").exists())
                .andExpect(jsonPath("$.fieldErrors.password").exists());
    }

    @Test
    void login_Success() throws Exception {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("jwt-token");
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.username").value("testuser"))
                .andExpect(jsonPath("$.user.role").value("ROLE_USER"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateToken(authentication);
        verify(userService).findByUsername("testuser");
    }

    @Test
    void login_InvalidCredentials_ReturnsBadRequest() throws Exception {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid credentials"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}