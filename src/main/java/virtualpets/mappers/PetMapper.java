package virtualpets.mappers;

import virtualpets.dtos.PetDTO;
import virtualpets.dtos.PetCreateDTO;
import virtualpets.models.Pet;
import virtualpets.models.User;

public class PetMapper {

    public static PetDTO toDTO(Pet pet) {
        PetDTO dto = new PetDTO();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setType(pet.getType());
        dto.setColor(pet.getColor());
        dto.setHappinessLevel(pet.getHappinessLevel());
        dto.setEnergyLevel(pet.getEnergyLevel());
        dto.setHungerLevel(pet.getHungerLevel());

        if (pet.getOwner() != null) {
            dto.setOwnerId(pet.getOwner().getId());
            dto.setOwnerUsername(pet.getOwner().getUsername());
        }

        return dto;
    }

    public static Pet fromCreateDTO(PetCreateDTO dto, User owner) {
        Pet pet = new Pet();
        pet.setName(dto.getName());
        pet.setType(dto.getType());
        pet.setColor(dto.getColor());
        pet.setOwner(owner);
        return pet;
    }
}
