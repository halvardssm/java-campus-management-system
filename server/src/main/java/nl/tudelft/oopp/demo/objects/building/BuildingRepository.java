package nl.tudelft.oopp.demo.objects.building;

import nl.tudelft.oopp.demo.objects.building.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    @Query("SELECT u.id FROM Building u WHERE u.name LIKE CONCAT('%',:name,'%')")
    int[] filterBuildingsOnName(@Param("name") String name);

    @Query("SELECT u.id FROM Building u WHERE u.location LIKE CONCAT('%',:location,'%')")
    int[] filterBuildingsOnLocation(@Param("location") String location);

    @Query("SELECT u.id FROM Building u WHERE u.location LIKE CONCAT('%',:location,'%') and u.name LIKE CONCAT('%',:name,'%')")
    int[] filterBuildingsOnLocationAndName(@Param("location") String location, @Param("name") String name);

    @Query("SELECT id FROM Building")
    int[] getAllBuildingIds();

    @Query("SELECT MAX(u.id) FROM Building u")
    int getMaxId();

    @Query("SELECT u FROM Building u WHERE u.id IN :ids")
    List<Building> getAllBuildingsByIds(@Param("ids") List<Integer> ids);

}
