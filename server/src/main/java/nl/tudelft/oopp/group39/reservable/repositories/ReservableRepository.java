package nl.tudelft.oopp.group39.reservable.repositories;

import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservableRepository extends JpaRepository<Reservable, Integer> {
}
