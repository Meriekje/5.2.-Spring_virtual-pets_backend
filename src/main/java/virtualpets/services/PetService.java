package virtualpets.services;

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

    private final PetRepository petRepository;
    private final UserService userService;

    public PetService(PetRepository petRepository, UserService userService) {
        this.petRepository = petRepository;
        this.userService = userService;
    }

    public Pet createPet(Pet pet, Long ownerId) {
        User owner = userService.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        pet.setOwner(owner);
        return petRepository.save(pet);
    }

    public Pet updatePet(Pet pet, Long ownerId) {
        Pet existingPet = petRepository.findByIdAndOwnerId(pet.getId(), ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found or not owned by user"));

        existingPet.setName(pet.getName());
        existingPet.setColor(pet.getColor());
        existingPet.setType(pet.getType());
        existingPet.setHappinessLevel(pet.getHappinessLevel());
        existingPet.setEnergyLevel(pet.getEnergyLevel());
        existingPet.setHungerLevel(pet.getHungerLevel());

        return petRepository.save(existingPet);
    }

    public void deletePet(Long petId, Long ownerId) {
        Pet existingPet = petRepository.findByIdAndOwnerId(petId, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found or not owned by user"));
        petRepository.delete(existingPet);
    }

   public Pet updatePetDirect(Pet pet) {
        return petRepository.save(pet);
    }

    public void deletePetDirect(Pet pet) {
        petRepository.delete(pet);
    }


    public Optional<Pet> findById(Long id) {
        return petRepository.findById(id);
    }

    public List<Pet> findByOwnerId(Long ownerId) {
        return petRepository.findByOwnerId(ownerId);
    }
}
