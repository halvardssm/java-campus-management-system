package nl.tudelft.oopp.demo.objects.building;

import nl.tudelft.oopp.demo.objects.building.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
    @Query("SELECT u.id FROM Building u WHERE u.name LIKE CONCAT('%',:name,'%')")
    int[] filterBuildingsOnName(@Param("name") String name);

    @Query("SELECT u.id FROM Building u WHERE u.location LIKE CONCAT('%',:location,'%')")
    int[] filterBuildingsOnLocation(@Param("location") String location);

    @Query("SELECT u.id FROM Building u WHERE u.location LIKE CONCAT('%',:location,'%') and u.name LIKE CONCAT('%',:name,'%')")
    int[] filterBuildingsOnLocationAndName(@Param("location") String location, @Param("name") String name);

    @Query("SELECT u.id FROM Building u WHERE u.location LIKE CONCAT('%',:location,'%') and u.name LIKE CONCAT('%',:name,'%') and u.open <= :open and u.closed >= :closed and u.closed>u.open")
    int[] filterBuildingsOnLocationAndNameAndTime(@Param("location") String location, @Param("name") String name, @Param("open") LocalTime open, @Param("closed") LocalTime closed);

    @Query("SELECT id FROM Building")
    int[] getAllBuildingIds();

    @Query("SELECT MAX(u.id) FROM Building u")
    int getMaxId();

    @Query("SELECT u FROM Building u WHERE u.id IN :ids")
    List<Building> getAllBuildingsByIds(@Param("ids") List<Long> ids);

    @Query("SELECT u FROM Building u WHERE u.id = :id")
    List<Building> findById(@Param("id") int id);

}
