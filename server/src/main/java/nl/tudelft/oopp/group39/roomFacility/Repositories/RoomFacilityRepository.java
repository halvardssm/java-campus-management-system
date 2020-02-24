package nl.tudelft.oopp.group39.roomFacility.Repositories;

import nl.tudelft.oopp.group39.roomFacility.Entities.RoomFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//This repository represents a table that relates rooms to their facilities
public interface RoomFacilityRepository extends JpaRepository<RoomFacility, Long> {

    //Returns a list of all RoomFacilities (room id paired to a single facility id) if the room facility
    //id and the room id match the inputted room facility id param and the room id param
    @Query("SELECT u FROM RoomFacility u WHERE u.roomId = :roomId and u.facilityId = :facilityId")
    List<RoomFacility> filterRooms(@Param("roomId") long roomId, @Param("facilityId") long facilityId);

    @Query("SELECT MAX(u.id) FROM RoomFacility u")
    int getMaxId();

    @Query("SELECT u.id FROM RoomFacility u WHERE u.roomId = :roomId")
    int[] getRoomFacilityIdsByRoomId(@Param("roomId") long roomId);

    @Query("SELECT u.id FROM RoomFacility u WHERE u.facilityId = :facilityId")
    int[] getRoomFacilityIdsByFacilityId(@Param("facilityId") long facilityId);

}
