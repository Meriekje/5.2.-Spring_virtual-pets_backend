package virtualpets.dtos;

import lombok.Data;
import virtualpets.models.PetType;

@Data
public class PetDTO {

    private Long id;
    private String name;
    private PetType type;
    private String color;

    private Integer happinessLevel;
    private Integer energyLevel;
    private Integer hungerLevel;

    private Long ownerId;
    private String ownerUsername;
}
