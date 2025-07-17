package virtualpets.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import virtualpets.models.Pet;
import virtualpets.models.User;
import virtualpets.repositories.PetRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {

    private static final Logger logger = LoggerFactory.getLogger(PetService.class);

    private final PetRepository petRepository;
    private final UserService userService;

    public PetService(PetRepository petRepository, UserService userService) {
        this.petRepository = petRepository;
        this.userService = userService;
    }

    public Pet createPet(Pet pet, Long ownerId) {
        logger.info("Creating new pet '{}' for user ID: {}", pet.getName(), ownerId);

        try {
            User owner = userService.findById(ownerId)
                    .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

            pet.setOwner(owner);
            Pet savedPet = petRepository.save(pet);

            logger.info("Pet created successfully with ID: {} for user: {}",
                    savedPet.getId(), owner.getUsername());

            return savedPet;

        } catch (Exception e) {
            logger.error("Error creating pet '{}' for user ID {}: {}",
                    pet.getName(), ownerId, e.getMessage(), e);
            throw e;
        }
    }

    @Cacheable(value = "pets", key = "#id")
    public Optional<Pet> findById(Long id) {
        logger.debug("Finding pet by ID: {}", id);
        Optional<Pet> pet = petRepository.findById(id);

        if (pet.isPresent()) {
            logger.debug("Pet found: {} (ID: {})", pet.get().getName(), id);
        } else {
            logger.debug("Pet not found with ID: {}", id);
        }

        return pet;
    }

    @Cacheable(value = "userPets", key = "#ownerId")
    public List<Pet> findByOwnerId(Long ownerId) {
        logger.debug("Finding pets for owner ID: {}", ownerId);
        List<Pet> pets = petRepository.findByOwnerId(ownerId);
        logger.debug("Found {} pets for owner ID: {}", pets.size(), ownerId);
        return pets;
    }

    @Caching(evict = {
            @CacheEvict(value = "pets", key = "#pet.id"),
            @CacheEvict(value = "userPets", key = "#pet.owner.id"),
            @CacheEvict(value = "petStats", key = "#pet.id")
    })
    public Pet updatePetDirect(Pet pet) {
        logger.info("Updating pet: {} (ID: {})", pet.getName(), pet.getId());

        try {
            Pet updatedPet = petRepository.save(pet);
            logger.info("Pet updated successfully: {} (ID: {})",
                    updatedPet.getName(), updatedPet.getId());
            return updatedPet;

        } catch (Exception e) {
            logger.error("Error updating pet ID {}: {}", pet.getId(), e.getMessage(), e);
            throw e;
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "pets", key = "#pet.id"),
            @CacheEvict(value = "userPets", key = "#pet.owner.id"),
            @CacheEvict(value = "petStats", key = "#pet.id")
    })
    public void deletePetDirect(Pet pet) {
        logger.info("Deleting pet: {} (ID: {})", pet.getName(), pet.getId());

        try {
            petRepository.delete(pet);
            logger.info("Pet deleted successfully: {} (ID: {})",
                    pet.getName(), pet.getId());

        } catch (Exception e) {
            logger.error("Error deleting pet ID {}: {}", pet.getId(), e.getMessage(), e);
            throw e;
        }
    }

    public Pet updatePet(Pet pet, Long ownerId) {
        logger.info("Updating pet with ownership validation: {} (ID: {}) for owner: {}",
                pet.getName(), pet.getId(), ownerId);

        Pet existingPet = petRepository.findByIdAndOwnerId(pet.getId(), ownerId)
                .orElseThrow(() -> {
                    logger.warn("Pet not found or not owned by user - Pet ID: {}, Owner ID: {}",
                            pet.getId(), ownerId);
                    return new IllegalArgumentException("Pet not found or not owned by user");
                });

        existingPet.setName(pet.getName());
        existingPet.setColor(pet.getColor());
        existingPet.setType(pet.getType());
        existingPet.setHappinessLevel(pet.getHappinessLevel());
        existingPet.setEnergyLevel(pet.getEnergyLevel());
        existingPet.setHungerLevel(pet.getHungerLevel());

        return updatePetDirect(existingPet);
    }

    public void deletePet(Long petId, Long ownerId) {
        logger.info("Deleting pet with ownership validation - Pet ID: {}, Owner ID: {}",
                petId, ownerId);

        Pet existingPet = petRepository.findByIdAndOwnerId(petId, ownerId)
                .orElseThrow(() -> {
                    logger.warn("Pet not found or not owned by user - Pet ID: {}, Owner ID: {}",
                            petId, ownerId);
                    return new IllegalArgumentException("Pet not found or not owned by user");
                });

        deletePetDirect(existingPet);
    }
}

