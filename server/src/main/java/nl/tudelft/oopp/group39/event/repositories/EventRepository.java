package nl.tudelft.oopp.group39.event.repositories;

import nl.tudelft.oopp.group39.event.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
}
