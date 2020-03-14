package nl.tudelft.oopp.group39.booking.repositories;

import nl.tudelft.oopp.group39.booking.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    //Returns an array with bookings from a chosen building
    @Query("SELECT u.id FROM Booking u WHERE u.buildingId = :buildingId")
    int[] filterBookingsOnLocation(@Param("buildingId") int buildingId);

    //Returns an array with bookings from a chosen room in a chosen building
    @Query("SELECT u.id FROM Booking u WHERE u.buildingId = :buildingId and u.roomId = :roomId")
    int[] filterBookingsOnLocationAndRoomId(@Param("buildingId") int buildingId, @Param("roomId") int roomId);

    //Returns an array with bookings from a chosen user
    @Query("SELECT u.id FROM Booking u WHERE u.userId = :userId")
    int[] filterBookingsOnUserId(@Param("userId") int userId);

    //Returns an array with bookings with a certain start/end-times
    @Query("SELECT u.id FROM Booking u WHERE u.startTime <= :startTime and u.endTime >= :endTime and u.endTime>=u.startTime")
    int[] filterBookingsOnTime(@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    //Returns an array with bookings from a certain building with certain start/end-times
    @Query("SELECT u.id FROM Booking u WHERE u.buildingId = :buildingId and u.startTime <= :startTime and u.endTime >= :endTime and u.endTime>=u.startTime")
    int[] filterBookingsOnLocationAndTime(@Param("buildingId") int buildingId, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    //Returns a list with bookings from a certain building with certain start/end-times
    @Query("SELECT u FROM Booking u WHERE u.buildingId = :buildingId and u.startTime <= :startTime and u.endTime >= :endTime and u.endTime>=u.startTime")
    List<Long> filterBookingsOnLocationAndTimeList(@Param("buildingId") int buildingId, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    //Returns an array with all the bookings in it
    @Query("SELECT id FROM Booking")
    int[] getAllBookingIds();

    @Query("SELECT u FROM Booking u WHERE u.id IN :ids")
    List<Booking> getAllBookingsByIds(@Param("ids") List<Long> ids);

    @Query("SELECT u FROM Booking u WHERE u.id = :id")
    List<Booking> findById(@Param("id") int id);

}
