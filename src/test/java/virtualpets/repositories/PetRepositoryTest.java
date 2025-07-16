package virtualpets.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import virtualpets.models.Pet;
import virtualpets.models.PetType;
import virtualpets.models.Role;
import virtualpets.models.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PetRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PetRepository petRepository;

    private User testUser;
    private User otherUser;
    private Pet testPet;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRole(Role.ROLE_USER);
        testUser = entityManager.persistAndFlush(testUser);

        otherUser = new User();
        otherUser.setUsername("otheruser");
        otherUser.setPassword("password");
        otherUser.setRole(Role.ROLE_USER);
        otherUser = entityManager.persistAndFlush(otherUser);

        testPet = new Pet();
        testPet.setName("TestPet");
        testPet.setType(PetType.MOLE);
        testPet.setColor("#FF6B6B");
        testPet.setOwner(testUser);
        testPet = entityManager.persistAndFlush(testPet);
    }

    @Test
    void findByOwnerId_ReturnsOwnerPets() {
        // When
        List<Pet> pets = petRepository.findByOwnerId(testUser.getId());

        // Then
        assertEquals(1, pets.size());
        assertEquals("TestPet", pets.get(0).getName());
        assertEquals(testUser.getId(), pets.get(0).getOwner().getId());
    }

    @Test
    void findByOwnerId_EmptyListForUserWithNoPets() {
        // When
        List<Pet> pets = petRepository.findByOwnerId(otherUser.getId());

        // Then
        assertTrue(pets.isEmpty());
    }

    @Test
    void findByOwnerUsername_ReturnsOwnerPets() {
        // When
        List<Pet> pets = petRepository.findByOwnerUsername("testuser");

        // Then
        assertEquals(1, pets.size());
        assertEquals("TestPet", pets.get(0).getName());
    }

    @Test
    void findByType_ReturnsPetsOfSpecificType() {
        // Given
        Pet magpiePet = new Pet();
        magpiePet.setName("MagpiePet");
        magpiePet.setType(PetType.MAGPIE);
        magpiePet.setColor("#00FF00");
        magpiePet.setOwner(testUser);
        entityManager.persistAndFlush(magpiePet);

        // When
        List<Pet> molePets = petRepository.findByType(PetType.MOLE);
        List<Pet> magpiePets = petRepository.findByType(PetType.MAGPIE);

        // Then
        assertEquals(1, molePets.size());
        assertEquals("TestPet", molePets.get(0).getName());
        assertEquals(1, magpiePets.size());
        assertEquals("MagpiePet", magpiePets.get(0).getName());
    }

    @Test
    void findByIdAndOwnerId_ReturnsPetWhenOwnerMatches() {
        // When
        Optional<Pet> result = petRepository.findByIdAndOwnerId(testPet.getId(), testUser.getId());

        // Then
        assertTrue(result.isPresent());
        assertEquals("TestPet", result.get().getName());
    }

    @Test
    void findByIdAndOwnerId_ReturnsEmptyWhenOwnerDoesNotMatch() {
        // When
        Optional<Pet> result = petRepository.findByIdAndOwnerId(testPet.getId(), otherUser.getId());

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void countByOwnerId_ReturnsCorrectCount() {
        // Given
        Pet secondPet = new Pet();
        secondPet.setName("SecondPet");
        secondPet.setType(PetType.TOAD);
        secondPet.setColor("#0000FF");
        secondPet.setOwner(testUser);
        entityManager.persistAndFlush(secondPet);

        // When
        long count = petRepository.countByOwnerId(testUser.getId());
        long otherCount = petRepository.countByOwnerId(otherUser.getId());

        // Then
        assertEquals(2, count);
        assertEquals(0, otherCount);
    }
}