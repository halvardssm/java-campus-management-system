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

    //Returns an array with all the buildings in it
    @Query("SELECT id FROM Building")
    Integer[] getAllBuildingIds();

    @Query("SELECT u FROM Building u WHERE u.id IN :ids")
    List<Building> getAllBuildingsByIds(@Param("ids") List<Long> ids);

    @Query("SELECT u FROM Building u WHERE u.id = :id")
    Optional<Building> findById(@Param("id") int id);

}
