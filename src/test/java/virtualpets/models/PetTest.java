package virtualpets.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PetTest {

    private Pet pet;

    @BeforeEach
    void setUp() {
        pet = new Pet();
        pet.setName("TestPet");
        pet.setType(PetType.MOLE);
        pet.setColor("#FF6B6B");
        pet.setHappinessLevel(50);
        pet.setEnergyLevel(50);
        pet.setHungerLevel(50);
    }

    @Test
    void feed_DecreasesHungerAndIncreasesHappiness() {
        // Given
        int initialHunger = pet.getHungerLevel();
        int initialHappiness = pet.getHappinessLevel();

        // When
        pet.feed();

        // Then
        assertEquals(Math.max(0, initialHunger - 20), pet.getHungerLevel());
        assertEquals(Math.min(100, initialHappiness + 10), pet.getHappinessLevel());
    }

    @Test
    void feed_HungerCannotGoBelowZero() {
        // Given
        pet.setHungerLevel(10);

        // When
        pet.feed();

        // Then
        assertEquals(0, pet.getHungerLevel());
    }

    @Test
    void feed_HappinessCannotGoAbove100() {
        // Given
        pet.setHappinessLevel(95);

        // When
        pet.feed();

        // Then
        assertEquals(100, pet.getHappinessLevel());
    }

    @Test
    void play_DecreasesEnergyAndIncreasesHappiness() {
        // Given
        int initialEnergy = pet.getEnergyLevel();
        int initialHappiness = pet.getHappinessLevel();

        // When
        pet.play();

        // Then
        assertEquals(Math.max(0, initialEnergy - 15), pet.getEnergyLevel());
        assertEquals(Math.min(100, initialHappiness + 15), pet.getHappinessLevel());
    }

    @Test
    void play_EnergyCannotGoBelowZero() {
        // Given
        pet.setEnergyLevel(10);

        // When
        pet.play();

        // Then
        assertEquals(0, pet.getEnergyLevel());
    }

    @Test
    void play_HappinessCannotGoAbove100() {
        // Given
        pet.setHappinessLevel(90);

        // When
        pet.play();

        // Then
        assertEquals(100, pet.getHappinessLevel());
    }

    @Test
    void rest_IncreasesEnergy() {
        // Given
        int initialEnergy = pet.getEnergyLevel();

        // When
        pet.rest();

        // Then
        assertEquals(Math.min(100, initialEnergy + 25), pet.getEnergyLevel());
    }

    @Test
    void rest_EnergyCannotGoAbove100() {
        // Given
        pet.setEnergyLevel(80);

        // When
        pet.rest();

        // Then
        assertEquals(100, pet.getEnergyLevel());
    }

    @Test
    void petType_HasCorrectDisplayName() {
        // Given & When & Then
        assertEquals("Mole", PetType.MOLE.getDisplayName());
        assertEquals("Magpie", PetType.MAGPIE.getDisplayName());
        assertEquals("Toad", PetType.TOAD.getDisplayName());
    }

    @Test
    void petType_HasCorrectSvgFileName() {
        // Given & When & Then
        assertEquals("mole.svg", PetType.MOLE.getSvgFileName());
        assertEquals("magpie.svg", PetType.MAGPIE.getSvgFileName());
        assertEquals("toad.svg", PetType.TOAD.getSvgFileName());
    }
}