package nl.tudelft.oopp.group39.reservation.repositories;

import nl.tudelft.oopp.group39.reservation.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
}
