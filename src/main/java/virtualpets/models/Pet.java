
package virtualpets.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "virtualpets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    private PetType type;

    private String color = "#FF6B6B";

    @Column(name = "happiness_level")
    private Integer happinessLevel = 50;

    @Column(name = "energy_level")
    private Integer energyLevel = 50;

    @Column(name = "hunger_level")
    private Integer hungerLevel = 50;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    // Mètodes per interactuar amb la mascota
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
