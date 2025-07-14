package virtualpets.dtos;

import jakarta.validation.constraints.*;

import lombok.Data;
import virtualpets.models.PetType;

@Data
public class PetCreateDTO {

    @NotBlank(message = "Pet name is required")
    @Size(min = 2, max = 50)
    private String name;

    @NotNull(message = "Pet type is required")
    private PetType type;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Color must be a valid hex code")
    private String color = "#FF6B6B";
}

