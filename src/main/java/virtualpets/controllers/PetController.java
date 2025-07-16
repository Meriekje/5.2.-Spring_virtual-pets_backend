package virtualpets.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import virtualpets.dtos.PetCreateDTO;
import virtualpets.dtos.PetDTO;
import virtualpets.exceptions.ResourceNotFoundException;
import virtualpets.exceptions.UnauthorizedException;
import virtualpets.models.Pet;
import virtualpets.models.User;
import virtualpets.services.PetService;
import virtualpets.services.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "http://localhost:3000")
public class PetController {

    private final PetService petService;
    private final UserService userService;
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String PET_NOT_FOUND = "Pet not found";

    public PetController(PetService petService, UserService userService) {
        this.petService = petService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<PetDTO>> getUserPets() {
        User currentUser = getCurrentUser();
        List<Pet> pets = petService.findByOwnerId(currentUser.getId());
        List<PetDTO> petDTOs = pets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(petDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDTO> getPetById(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        Pet pet = petService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PET_NOT_FOUND));

        // Check if user owns the pet or is admin
        if (!pet.getOwner().getId().equals(currentUser.getId()) &&
                !currentUser.getRole().name().equals(ROLE_ADMIN)) {
            throw new UnauthorizedException("You don't have permission to access this pet");
        }

        return ResponseEntity.ok(convertToDTO(pet));
    }

    @PostMapping
    public ResponseEntity<PetDTO> createPet(@Valid @RequestBody PetCreateDTO petCreateDTO) {
        User currentUser = getCurrentUser();

        Pet pet = new Pet();
        pet.setName(petCreateDTO.getName());
        pet.setType(petCreateDTO.getType());
        pet.setColor(petCreateDTO.getColor());

        Pet savedPet = petService.createPet(pet, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedPet));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDTO> updatePet(@PathVariable Long id, @Valid @RequestBody PetCreateDTO petUpdateDTO) {
        User currentUser = getCurrentUser();

        Pet existingPet = petService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PET_NOT_FOUND));

        // Check if user owns the pet or is admin
        if (!existingPet.getOwner().getId().equals(currentUser.getId()) &&
                !currentUser.getRole().name().equals(ROLE_ADMIN)) {
            throw new UnauthorizedException("You don't have permission to update this pet");
        }

        existingPet.setName(petUpdateDTO.getName());
        existingPet.setType(petUpdateDTO.getType());
        existingPet.setColor(petUpdateDTO.getColor());

        Pet updatedPet = petService.updatePet(existingPet, currentUser.getId());
        return ResponseEntity.ok(convertToDTO(updatedPet));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePet(@PathVariable Long id) {
        User currentUser = getCurrentUser();

        Pet existingPet = petService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PET_NOT_FOUND));

        // Check if user owns the pet or is admin
        if (!existingPet.getOwner().getId().equals(currentUser.getId()) &&
                !currentUser.getRole().name().equals(ROLE_ADMIN)) {
            throw new UnauthorizedException("You don't have permission to delete this pet");
        }

        petService.deletePet(id, currentUser.getId());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Pet deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/feed")
    public ResponseEntity<PetDTO> feedPet(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        Pet pet = getPetWithPermissionCheck(id, currentUser);

        pet.feed();
        Pet updatedPet = petService.updatePet(pet, currentUser.getId());

        return ResponseEntity.ok(convertToDTO(updatedPet));
    }

    @PostMapping("/{id}/play")
    public ResponseEntity<PetDTO> playWithPet(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        Pet pet = getPetWithPermissionCheck(id, currentUser);

        pet.play();
        Pet updatedPet = petService.updatePet(pet, currentUser.getId());

        return ResponseEntity.ok(convertToDTO(updatedPet));
    }

    private Pet getPetWithPermissionCheck(Long petId, User currentUser) {
        Pet pet = petService.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException(PET_NOT_FOUND));

        // Check if user owns the pet or is admin
        if (!pet.getOwner().getId().equals(currentUser.getId()) &&
                !currentUser.getRole().name().equals(ROLE_ADMIN)) {
            throw new UnauthorizedException("You don't have permission to interact with this pet");
        }

        return pet;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }

    private PetDTO convertToDTO(Pet pet) {
        PetDTO dto = new PetDTO();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setType(pet.getType());
        dto.setColor(pet.getColor());
        dto.setHappinessLevel(pet.getHappinessLevel());
        dto.setEnergyLevel(pet.getEnergyLevel());
        dto.setHungerLevel(pet.getHungerLevel());
        dto.setOwnerId(pet.getOwner().getId());
        dto.setOwnerUsername(pet.getOwner().getUsername());
        return dto;
    }
}