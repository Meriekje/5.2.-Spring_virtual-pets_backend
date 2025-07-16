package virtualpets.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import virtualpets.dtos.PetDTO;
import virtualpets.dtos.UserDTO;
import virtualpets.models.Pet;
import virtualpets.models.User;
import virtualpets.repositories.PetRepository;
import virtualpets.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public AdminController(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/pets")
    public ResponseEntity<List<PetDTO>> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        List<PetDTO> petDTOs = pets.stream()
                .map(this::convertPetToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(petDTOs);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = users.stream()
                .map(this::convertUserToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    private PetDTO convertPetToDTO(Pet pet) {
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

    private UserDTO convertUserToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole().name());
        return dto;
    }
}