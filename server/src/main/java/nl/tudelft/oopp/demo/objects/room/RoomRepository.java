package nl.tudelft.oopp.demo.objects.room;

import nl.tudelft.oopp.demo.objects.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//This repository gets all data from the rooms table, and also queries to it
public interface RoomRepository extends JpaRepository<Room, Long> {

    //Returns a list of all rooms that match the inputted capacity param, the inputted onlyStaff param and the
    //inputted id param
    @Query("SELECT u FROM Room u WHERE u.capacity <= :capacity and u.onlyStaff = :onlyStaff and u.id IN :id")
    List<Room> filterRooms(@Param("capacity") int capacity, @Param("onlyStaff") boolean onlyStaff, @Param("id") List<Integer> id);

    //Returns an array of all room ids
    @Query("SELECT id FROM Room")
    int[] getAllRoomIds();

    //Returns the building id of the room that matches the inputted id param
    @Query("SELECT u.buildingId FROM Room u WHERE u.id = :id")
    int getAllBuildingIds(@Param("id") int id);

    //Returns the room that matches the inputted id param
    @Query("SELECT u FROM Room u WHERE u.id = :id")
    Room getRoomById(@Param("id") int id);

    @Query("SELECT MAX(u.capacity) FROM Room u WHERE u.buildingId = :id")
    int getMaxRoomCapacityByBuildingId(@Param("id") int id);

    @Query("SELECT u FROM Room u WHERE u.buildingId = :id")
    List<Room> getRoomsByBuildingId(@Param("id") int id);

    @Query("SELECT MAX(u.id) FROM Room u")
    int getMaxId();

}
