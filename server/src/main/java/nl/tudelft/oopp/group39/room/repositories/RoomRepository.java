package nl.tudelft.oopp.group39.room.repositories;

import java.util.List;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.room.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
//This repository gets all data from the rooms table, and also queries to it
public interface RoomRepository extends JpaRepository<Room, Long> {

    //Returns a list of all rooms that match the inputted capacity param, the inputted onlyStaff param and the
    //inputted id param
    @Query("SELECT u FROM Room u WHERE u.capacity >= :capacity and u.onlyStaff = :onlyStaff and u.buildingId IN :buildingId and u.facilities IN :facilities")
    List<Room> filterRooms(@Param("capacity") int capacity, @Param("onlyStaff") boolean onlyStaff, @Param("buildingId") List<Long> buildingId, @Param("facilities") List<Facility> facilities);

    //Returns an array of all room ids
    @Query("SELECT id FROM Room")
    int[] getAllRoomIds();

    //Returns the building id of the room that matches the inputted id param
    @Query("SELECT u.buildingId FROM Room u WHERE u.id = :id")
    int getAllBuildingIds(@Param("id") int id);

    //Returns the room that matches the inputted id param
    @Query("SELECT u FROM Room u WHERE u.id = :id")
    Room getRoomById(@Param("id") long id);

    //Returns the maximum capacity of a room in a chosen building
    @Query("SELECT MAX(u.capacity) FROM Room u WHERE u.buildingId = :id")
    int getMaxRoomCapacityByBuildingId(@Param("id") long id);

    //Returns a list with the rooms that match the chosen building id
    @Query("SELECT u FROM Room u WHERE u.buildingId = :id")
    List<Room> getRoomsByBuildingId(@Param("id") long id);

    //Returns the maximum amount of users (capacity) in a room
    @Query("SELECT MAX(u.id) FROM Room u")
    int getMaxId();

    //Returns a list with the rooms which a selected user has reserved
    @Query("SELECT u FROM Room u where u.id = :id")
    List<Room> findById(@Param("id") int id);

}
