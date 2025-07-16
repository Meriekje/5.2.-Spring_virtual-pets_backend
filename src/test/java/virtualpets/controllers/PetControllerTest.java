package virtualpets.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import virtualpets.config.SecurityConfig;
import virtualpets.dtos.PetCreateDTO;
import virtualpets.models.Pet;
import virtualpets.models.PetType;
import virtualpets.models.Role;
import virtualpets.models.User;
import virtualpets.security.CustomUserDetailsService;
import virtualpets.security.JwtAuthenticationFilter;
import virtualpets.security.JwtTokenProvider;
import virtualpets.services.PetService;
import virtualpets.services.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
@Import({SecurityConfig.class})
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PetService petService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private Pet testPet;
    private PetCreateDTO petCreateDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setRole(Role.ROLE_USER);

        testPet = new Pet();
        testPet.setId(1L);
        testPet.setName("TestPet");
        testPet.setType(PetType.MOLE);
        testPet.setColor("#FF6B6B");
        testPet.setOwner(testUser);
        testPet.setHappinessLevel(50);
        testPet.setEnergyLevel(50);
        testPet.setHungerLevel(50);

        petCreateDTO = new PetCreateDTO();
        petCreateDTO.setName("TestPet");
        petCreateDTO.setType(PetType.MOLE);
        petCreateDTO.setColor("#FF6B6B");
    }

    @Test
    @WithMockUser(username = "testuser")
    void getUserPets_Success() throws Exception {
        // Given
        List<Pet> pets = Arrays.asList(testPet);
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(petService.findByOwnerId(1L)).thenReturn(pets);

        // When & Then
        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("TestPet"))
                .andExpect(jsonPath("$[0].type").value("MOLE"));

        verify(userService).findByUsername("testuser");
        verify(petService).findByOwnerId(1L);
    }

    @Test
    @WithMockUser(username = "testuser")
    void getPetById_Success() throws Exception {
        // Given
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(petService.findById(1L)).thenReturn(Optional.of(testPet));

        // When & Then
        mockMvc.perform(get("/api/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestPet"))
                .andExpect(jsonPath("$.type").value("MOLE"));

        verify(petService).findById(1L);
    }

    @Test
    @WithMockUser(username = "testuser")
    void getPetById_NotFound() throws Exception {
        // Given
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(petService.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/pets/1"))
                .andExpect(status().isNotFound());

        verify(petService).findById(1L);
    }

    @Test
    @WithMockUser(username = "testuser")
    void createPet_Success() throws Exception {
        // Given
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(petService.createPet(any(Pet.class), anyLong())).thenReturn(testPet);

        // When & Then
        mockMvc.perform(post("/api/pets")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestPet"))
                .andExpect(jsonPath("$.type").value("MOLE"));

        verify(petService).createPet(any(Pet.class), eq(1L));
    }

    @Test
    @WithMockUser(username = "testuser")
    void createPet_InvalidInput() throws Exception {
        // Given
        PetCreateDTO invalidPet = new PetCreateDTO();
        invalidPet.setName(""); // Invalid name
        invalidPet.setType(PetType.MOLE);

        // When & Then
        mockMvc.perform(post("/api/pets")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser")
    void feedPet_Success() throws Exception {
        // Given
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(petService.findById(1L)).thenReturn(Optional.of(testPet));
        when(petService.updatePet(any(Pet.class), anyLong())).thenReturn(testPet);

        // When & Then
        mockMvc.perform(post("/api/pets/1/feed")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestPet"));

        verify(petService).findById(1L);
        verify(petService).updatePet(any(Pet.class), eq(1L));
    }

    @Test
    @WithMockUser(username = "testuser")
    void playWithPet_Success() throws Exception {
        // Given
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(petService.findById(1L)).thenReturn(Optional.of(testPet));
        when(petService.updatePet(any(Pet.class), anyLong())).thenReturn(testPet);

        // When & Then
        mockMvc.perform(post("/api/pets/1/play")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestPet"));

        verify(petService).findById(1L);
        verify(petService).updatePet(any(Pet.class), eq(1L));
    }

    @Test
    @WithMockUser(username = "testuser")
    void deletePet_Success() throws Exception {
        // Given
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(petService.findById(1L)).thenReturn(Optional.of(testPet));

        // When & Then
        mockMvc.perform(delete("/api/pets/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Pet deleted successfully"));

        verify(petService).findById(1L);
        verify(petService).deletePet(1L, 1L);
    }

    @Test
    void getUserPets_Unauthorized() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isUnauthorized());
    }
}