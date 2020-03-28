package nl.tudelft.oopp.group39.room.repositories;

import nl.tudelft.oopp.group39.room.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This repository gets all data from the rooms table, and also queries to it.
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {}
