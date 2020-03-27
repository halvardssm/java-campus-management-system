package nl.tudelft.oopp.group39.reservation.repositories;

import nl.tudelft.oopp.group39.reservation.entities.ReservationAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ReservationAmountRepository extends JpaRepository<ReservationAmount, Integer> {
    void deleteByReservationId(Integer id);
}
