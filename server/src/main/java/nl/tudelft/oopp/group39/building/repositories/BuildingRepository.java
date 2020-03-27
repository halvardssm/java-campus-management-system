package nl.tudelft.oopp.group39.building.repositories;

import java.util.List;
import java.util.Optional;
import nl.tudelft.oopp.group39.building.entities.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {

    @Query("SELECT u FROM Building u WHERE u.id = :id")
    Building findById(@Param("id") Integer id);

}
