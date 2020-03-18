package nl.tudelft.oopp.group39.reservation.repositories;

import nl.tudelft.oopp.group39.reservation.entities.ReservationAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationAmountRepository extends JpaRepository<ReservationAmount, Integer> {
}
