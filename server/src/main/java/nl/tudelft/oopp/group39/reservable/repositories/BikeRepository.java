package nl.tudelft.oopp.group39.reservable.repositories;

import nl.tudelft.oopp.group39.reservable.entities.Bike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BikeRepository extends JpaRepository<Bike, Long> {
}
