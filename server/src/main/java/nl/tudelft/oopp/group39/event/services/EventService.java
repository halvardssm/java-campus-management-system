package nl.tudelft.oopp.group39.event.services;

import java.util.List;
import java.util.Map;
import javassist.NotFoundException;
import nl.tudelft.oopp.group39.event.dao.EventDao;
import nl.tudelft.oopp.group39.event.entities.Event;
import nl.tudelft.oopp.group39.event.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    public static final String EXCEPTION_EVENT_NOT_FOUND = "Event %d not found";

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventDao eventDao;

    /**
     * List all events.
     *
     * @return a list of events {@link Event}.
     */
    public List<Event> listEvents(Map<String, String> params) {
        return eventDao.filter(params);
    }

    /**
     * Get an event.
     *
     * @return event by id {@link Event}.
     */
    public Event readEvent(Long id) throws NotFoundException {
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
    public Event updateEvent(Long id, Event newEvent) throws NotFoundException {
        return eventRepository.findById(id)
            .map(event -> {
                event.setTitle(newEvent.getTitle());
                event.setStartsAt(newEvent.getStartsAt());
                event.setEndsAt(newEvent.getEndsAt());
                event.setUser(newEvent.getUser());
                event.setRooms(newEvent.getRooms());
                return eventRepository.save(event);
            })
            .orElseThrow(() -> new NotFoundException(String.format(EXCEPTION_EVENT_NOT_FOUND, id)));
    }

    /**
     * Delete an event {@link Event}.
     */
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
