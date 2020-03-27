package nl.tudelft.oopp.group39.event.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javassist.NotFoundException;
import nl.tudelft.oopp.group39.AbstractTest;
import nl.tudelft.oopp.group39.event.entities.Event;
import nl.tudelft.oopp.group39.event.enums.EventTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventServiceTest extends AbstractTest {
    private static final Event testEvent = new Event(
        EventTypes.EVENT,
        LocalDate.now(ZoneId.of("Europe/Paris")),
        LocalDate.now(ZoneId.of("Europe/Paris")).plusDays(1),
        null
    );

    @BeforeEach
    void setUp() {
        Event event = eventRepository.save(testEvent);
        testEvent.setId(event.getId());
    }

    @AfterEach
    void tearDown() {
        eventRepository.deleteById(testEvent.getId());
        testEvent.setId(null);
    }

    @Test
    void listEvents() {
        List<Event> events = eventService.listEvents();

        assertEquals(1, events.size());
        assertEquals(testEvent, events.get(0));
    }

    @Test
    void readEvent() throws NotFoundException {
        Event event = eventService.readEvent(testEvent.getId());

        assertEquals(testEvent, event);
    }

    @Test
    void deleteAndCreateEvent() {
        eventService.deleteEvent(testEvent.getId());

        assertEquals(new ArrayList<>(), eventService.listEvents());

        Event event = eventService.createEvent(testEvent);

        testEvent.setId(event.getId());

        assertEquals(testEvent, event);
    }

    @Test
    void updateEvent() throws NotFoundException {
        Event event = testEvent;
        event.setType(EventTypes.HOLIDAY);
        Event event2 = eventService.updateEvent(testEvent.getId(), event);

        assertEquals(event, event2);
    }
}