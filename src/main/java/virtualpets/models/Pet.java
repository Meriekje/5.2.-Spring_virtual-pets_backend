
package virtualpets.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "pets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "owner")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Pet name is required")
    @Size(min = 2, max = 50, message = "Pet name must be between 2 and 50 characters")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetType type;

    @Column(nullable = false)
    private String color = "#FF6B6B";

    @Column(name = "happiness_level", nullable = false)
    @Min(value = 0, message = "Happiness must be between 0 and 100")
    @Max(value = 100, message = "Happiness must be between 0 and 100")
    private Integer happinessLevel = 50;

    @Column(name = "energy_level", nullable = false)
    @Min(value = 0, message = "Energy must be between 0 and 100")
    @Max(value = 100, message = "Energy must be between 0 and 100")
    private Integer energyLevel = 50;

    @Column(name = "hunger_level", nullable = false)
    @Min(value = 0, message = "Hunger must be between 0 and 100")
    @Max(value = 100, message = "Hunger must be between 0 and 100")
    private Integer hungerLevel = 50;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;




    public void feed() {
        this.hungerLevel = Math.max(0, this.hungerLevel - 20);
        this.happinessLevel = Math.min(100, this.happinessLevel + 10);
    }

    public void play() {
        this.energyLevel = Math.max(0, this.energyLevel - 15);
        this.happinessLevel = Math.min(100, this.happinessLevel + 15);
    }

    public void rest() {
        this.energyLevel = Math.min(100, this.energyLevel + 25);
    }
}
