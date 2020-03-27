package nl.tudelft.oopp.group39.room.repositories;

import java.util.List;

import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.room.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * This repository gets all data from the rooms table, and also queries to it.
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    /**
     * Returns a list with the rooms which a selected user has reserved.
     */
    @Query("SELECT u FROM Room u where u.id = :id")
    List<Room> findById(@Param("id") int id);
}
