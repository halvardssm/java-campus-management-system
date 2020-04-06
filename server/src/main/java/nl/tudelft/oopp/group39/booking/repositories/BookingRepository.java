package nl.tudelft.oopp.group39.booking.repositories;

import nl.tudelft.oopp.group39.booking.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
}
