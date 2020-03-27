package nl.tudelft.oopp.group39.booking.repositories;

import java.util.List;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT u FROM Booking u WHERE u.id = :id")
    List<Booking> findById(@Param("id") int id);

}
