package nl.tudelft.oopp.group39.event.services;

import java.util.List;
import javassist.NotFoundException;
import nl.tudelft.oopp.group39.event.entities.Event;
import nl.tudelft.oopp.group39.event.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    public static final String EXCEPTION_EVENT_NOT_FOUND = "Event %d not found";

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
    public Event readEvent(Integer id) throws NotFoundException {
        return eventRepository.findById(id).orElseThrow(()
            -> new NotFoundException(String.format(EXCEPTION_EVENT_NOT_FOUND, id)));
    }

    /**
     * Create an event.
     *
     * @return the created event {@link Event}.
     */
    public Event createEvent(Event event) throws IllegalArgumentException {
        return eventRepository.save(event);
    }

    /**
     * Update an event.
     *
     * @return the updated event {@link Event}.
     */
    public Event updateEvent(Integer id, Event newEvent) throws NotFoundException {
        return eventRepository.findById(id)
            .map(event -> {
                newEvent.setId(id);
                event = newEvent;

                return eventRepository.save(event);
            })
            .orElseThrow(() -> new NotFoundException(String.format(EXCEPTION_EVENT_NOT_FOUND, id)));
    }

    /**
     * Delete an event {@link Event}.
     */
    public void deleteEvent(Integer id) {
        eventRepository.deleteById(id);
    }
}
