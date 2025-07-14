package virtualpets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import virtualpets.models.Pet;
import virtualpets.models.PetType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByOwnerId(Long ownerId);

    List<Pet> findByOwnerUsername(String username);

    List<Pet> findByType(PetType type);

    @Query("SELECT p FROM Pet p WHERE p.owner.id = :ownerId AND p.id = :petId")
    Optional<Pet> findByIdAndOwnerId(@Param("petId") Long petId, @Param("ownerId") Long ownerId);

    @Query("SELECT p FROM Pet p WHERE p.lastInteraction < :cutoffTime")
    List<Pet> findPetsNeedingAttention(@Param("cutoffTime") LocalDateTime cutoffTime);

    @Query("SELECT COUNT(p) FROM Pet p WHERE p.owner.id = :ownerId")
    long countByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT p FROM Pet p WHERE p.happinessLevel < :threshold")
    List<Pet> findUnhappyPets(@Param("threshold") Integer threshold);
}
