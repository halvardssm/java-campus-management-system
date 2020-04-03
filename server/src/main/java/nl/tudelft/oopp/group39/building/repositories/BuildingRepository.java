package nl.tudelft.oopp.group39.building.repositories;

import nl.tudelft.oopp.group39.building.entities.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
}
