package virtualpets.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import virtualpets.models.Pet;
import virtualpets.models.PetType;
import virtualpets.models.Role;
import virtualpets.models.User;
import virtualpets.repositories.PetRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PetService petService;

    private User testUser;
    private Pet testPet;

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
    }

    @Test
    void createPet_Success() {
        // Given
        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        when(petRepository.save(any(Pet.class))).thenReturn(testPet);

        // When
        Pet result = petService.createPet(testPet, 1L);

        // Then
        assertNotNull(result);
        assertEquals("TestPet", result.getName());
        assertEquals(PetType.MOLE, result.getType());
        assertEquals(testUser, result.getOwner());
        verify(userService).findById(1L);
        verify(petRepository).save(testPet);
    }

    @Test
    void createPet_OwnerNotFound_ThrowsException() {
        // Given
        when(userService.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> petService.createPet(testPet, 1L)
        );

        assertEquals("Owner not found", exception.getMessage());
        verify(userService).findById(1L);
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void findById_PetExists_ReturnsPet() {
        // Given
        when(petRepository.findById(1L)).thenReturn(Optional.of(testPet));

        // When
        Optional<Pet> result = petService.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("TestPet", result.get().getName());
        verify(petRepository).findById(1L);
    }

    @Test
    void findById_PetNotExists_ReturnsEmpty() {
        // Given
        when(petRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Pet> result = petService.findById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(petRepository).findById(1L);
    }

    @Test
    void findByOwnerId_ReturnsOwnerPets() {
        // Given
        Pet pet2 = new Pet();
        pet2.setId(2L);
        pet2.setName("TestPet2");
        pet2.setOwner(testUser);

        List<Pet> pets = Arrays.asList(testPet, pet2);
        when(petRepository.findByOwnerId(1L)).thenReturn(pets);

        // When
        List<Pet> result = petService.findByOwnerId(1L);

        // Then
        assertEquals(2, result.size());
        assertEquals("TestPet", result.get(0).getName());
        assertEquals("TestPet2", result.get(1).getName());
        verify(petRepository).findByOwnerId(1L);
    }

    @Test
    void updatePet_Success() {
        // Given
        Pet updatedPet = new Pet();
        updatedPet.setId(1L);
        updatedPet.setName("UpdatedPet");
        updatedPet.setType(PetType.MAGPIE);
        updatedPet.setColor("#00FF00");
        updatedPet.setHappinessLevel(80);
        updatedPet.setEnergyLevel(70);
        updatedPet.setHungerLevel(30);

        when(petRepository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(testPet));
        when(petRepository.save(any(Pet.class))).thenReturn(testPet);

        // When
        Pet result = petService.updatePet(updatedPet, 1L);

        // Then
        assertNotNull(result);
        verify(petRepository).findByIdAndOwnerId(1L, 1L);
        verify(petRepository).save(testPet);
    }

    @Test
    void updatePet_PetNotFound_ThrowsException() {
        // Given
        when(petRepository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> petService.updatePet(testPet, 1L)
        );

        assertEquals("Pet not found or not owned by user", exception.getMessage());
        verify(petRepository).findByIdAndOwnerId(1L, 1L);
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void deletePet_Success() {
        // Given
        when(petRepository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(testPet));

        // When
        petService.deletePet(1L, 1L);

        // Then
        verify(petRepository).findByIdAndOwnerId(1L, 1L);
        verify(petRepository).delete(testPet);
    }

    @Test
    void deletePet_PetNotFound_ThrowsException() {
        // Given
        when(petRepository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> petService.deletePet(1L, 1L)
        );

        assertEquals("Pet not found or not owned by user", exception.getMessage());
        verify(petRepository).findByIdAndOwnerId(1L, 1L);
        verify(petRepository, never()).delete(any(Pet.class));
    }
}