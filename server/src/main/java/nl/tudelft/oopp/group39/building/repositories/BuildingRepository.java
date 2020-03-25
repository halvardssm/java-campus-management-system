package nl.tudelft.oopp.group39.building.repositories;

import java.time.LocalTime;
import java.util.List;
import nl.tudelft.oopp.group39.building.entities.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
    @Query("SELECT u.id FROM Building u WHERE u.name LIKE CONCAT('%',:name,'%')")
    int[] filterBuildingsOnName(@Param("name") String name);

    @Query("SELECT u.id FROM Building u WHERE u.location LIKE CONCAT('%',:location,'%')")
    int[] filterBuildingsOnLocation(@Param("location") String location);

    @Query("SELECT u.id FROM Building u WHERE u.location LIKE CONCAT('%',:location,'%') "
        + "and u.name LIKE CONCAT('%',:name,'%')")
    int[] filterBuildingsOnLocationAndName(
        @Param("location") String location,
        @Param("name") String name
    );

    @Query("SELECT u.id FROM Building u WHERE u.location LIKE CONCAT('%',:location,'%') "
        + "and u.name LIKE CONCAT('%',:name,'%') and u.open <= :open and u.closed >= :closed "
        + "and u.closed>=u.open")
    int[] filterBuildingsOnLocationAndNameAndTime(
        @Param("location") String location,
        @Param("name") String name,
        @Param("open") LocalTime open,
        @Param("closed") LocalTime closed
    );

    @Query("SELECT u FROM Building u WHERE u.location LIKE CONCAT('%',:location,'%') "
        + "and u.name LIKE CONCAT('%',:name,'%') and u.open <= :open "
        + "and u.closed >= :closed and u.closed>=u.open")
    List<Long> filterBuildingsOnLocationAndNameAndTimeList(
        @Param("location") String location,
        @Param("name") String name,
        @Param("open") LocalTime open,
        @Param("closed") LocalTime closed
    );

    @Query("SELECT u FROM Building u WHERE u.location LIKE CONCAT('%',:location,'%') "
            + "and u.name LIKE CONCAT('%',:name,'%') and u.open <= :open "
            + "and u.closed >= :closed and u.closed>=u.open")
    List<Building> filterBuildingsOnLocationAndNameAndTimeBuildingList(
            @Param("location") String location,
            @Param("name") String name,
            @Param("open") LocalTime open,
            @Param("closed") LocalTime closed
    );

    //Returns an array with all the buildings in it
    @Query("SELECT id FROM Building")
    int[] getAllBuildingIds();

    //Returns the maximum capacity of a selected building
    @Query("SELECT MAX(u.id) FROM Building u")
    int getMaxId();

    @Query("SELECT u FROM Building u WHERE u.id IN :ids")
    List<Building> getAllBuildingsByIds(@Param("ids") List<Long> ids);

    @Query("SELECT u FROM Building u WHERE u.id = :id")
    List<Building> findById(@Param("id") int id);

}
