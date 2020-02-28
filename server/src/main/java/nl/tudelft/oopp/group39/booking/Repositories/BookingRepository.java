package nl.tudelft.oopp.group39.booking.Repositories;

import nl.tudelft.oopp.group39.booking.Entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT u.id FROM Booking u WHERE u.buildingId = :buildingId")
    int[] filterBookingsOnLocation(@Param("buildingId") long buildingId);

    @Query("SELECT u.id FROM Booking u WHERE u.buildingId = :buildingId and u.roomId = :roomId")
    int[] filterBookingsOnLocationAndRoomId(@Param("buildingId") long buildingId, @Param("roomId") long roomId);

    @Query("SELECT u.id FROM Booking u WHERE u.userId = :userId")
    int[] filterBookingsOnUserId(@Param("userId") long userId);

    @Query("SELECT u.id FROM Booking u WHERE u.startTime <= :startTime and u.endTime >= :endTime and u.endTime>=u.startTime")
    int[] filterBookingsOnTime(@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    @Query("SELECT u.id FROM Booking u WHERE u.buildingId = :buildingId and u.startTime <= :startTime and u.endTime >= :endTime and u.endTime>=u.startTime")
    int[] filterBookingsOnLocationAndTime(@Param("buildingId") long buildingId, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    @Query("SELECT u FROM Booking u WHERE u.buildingId = :buildingId and u.startTime <= :startTime and u.endTime >= :endTime and u.endTime>=u.startTime")
    List<Long> filterBookingsOnLocationAndTimeList(@Param("buildingId") long buildingId, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    //Returns an array with all the bookings in it
    @Query("SELECT id FROM Booking")
    int[] getAllBookingIds();

    @Query("SELECT u FROM Booking u WHERE u.id IN :ids")
    List<Booking> getAllBookingsByIds(@Param("ids") List<Long> ids);

    @Query("SELECT u FROM Booking u WHERE u.id = :id")
    List<Booking> findById(@Param("id") int id);

}
