package nl.tudelft.oopp.group39.event.services;

import java.util.List;
import javassist.NotFoundException;
import nl.tudelft.oopp.group39.event.entities.Event;
import nl.tudelft.oopp.group39.event.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    /**
     * List all events.
     *
     * @return a list of events {@link Event}.
     */
    public List<Event> listEvents() {
        return eventRepository.findAll();
    }

    /**
     * Get an event.
     *
     * @return event by id {@link Event}.
     */
    public Event readEvent(String id) throws NotFoundException {
        return eventRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    /**
     * Create an event.
     *
     * @return the created event {@link Event}.
     */
    public Event createEvent(Event newEvent) {
        try {
            Event event = readEvent(newEvent.getEventname());
            throw new EventExistsException(event.getEventname());

        } catch (EventnameNotFoundException e) {
            newEvent.setPassword(encryptPassword(newEvent.getPassword()));
            mapRolesForEvent(newEvent);

            eventRepository.save(newEvent);

            return newEvent;
        }
    }

    /**
     * Update an event.
     *
     * @return the updated event {@link Event}.
     */
    public Event updateEvent(String id, Event newEvent) throws EventnameNotFoundException {
        return eventRepository.findById(id)
            .map(event -> {
                newEvent.setEventname(id);
                newEvent.setPassword(encryptPassword(newEvent.getPassword()));
                mapRolesForEvent(newEvent);
                return eventRepository.save(newEvent);
            }).orElseThrow(() -> new EventnameNotFoundException(id));
    }

    /**
     * Delete an event {@link Event}.
     */
    public void deleteEvent(String id) throws EventnameNotFoundException {
        readEvent(id);
        eventRepository.deleteById(id);
    }
}
