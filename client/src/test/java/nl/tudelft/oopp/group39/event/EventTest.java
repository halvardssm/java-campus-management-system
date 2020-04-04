package nl.tudelft.oopp.group39.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import nl.tudelft.oopp.group39.event.model.Event;
import org.junit.jupiter.api.Test;

class EventTest {

    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static Event testEvent = new Event(
        "Christmas",
        LocalDateTime.of(
            LocalDate.of(1881, 5, 19),
            LocalTime.of(0, 0, 0))
            .format(dateTimeFormatter),
        LocalDateTime.of(
            LocalDate.of(1938, 11, 10),
            LocalTime.of(23, 59, 59))
            .format(dateTimeFormatter),
        false,
        "admin",
        List.of(5L, 7L)
    );

    @Test
    void getId() {
        assertNull(testEvent.getId());
    }

    @Test
    void jsonConversionTest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(testEvent));
        Event reconvEvent = mapper.readValue(
            mapper.writeValueAsString(testEvent), Event.class);

        System.out.println(mapper.writeValueAsString(reconvEvent));
        assertEquals(reconvEvent, testEvent);
    }

    @Test
    void getTitle() {
        assertEquals(testEvent.getTitle(), "Christmas");
    }

    @Test
    void getStartsAt() {
        assertEquals(
            testEvent.getStartsAt(),
            LocalDateTime.of(
                LocalDate.of(1881, 5, 19),
                LocalTime.of(0, 0, 0))
                .format(dateTimeFormatter)
        );
    }

    @Test
    void getEndsAt() {
        assertEquals(
            testEvent.getEndsAt(),
            LocalDateTime.of(
                LocalDate.of(1938, 11, 10),
                LocalTime.of(23, 59, 59))
                .format(dateTimeFormatter)
        );
    }

    @Test
    void isGlobal() {
        assertFalse(testEvent.isGlobal());
    }

    @Test
    void getUser() {
        assertEquals(testEvent.getUser(), "admin");
    }

    @Test
    void getRooms() {
        assertEquals(testEvent.getRooms(), List.of(5L, 7L));
    }

    @Test
    void testIncompatible() {
        assertNotEquals(testEvent, null);
        assertNotEquals(testEvent, new Object());
    }

    @Test
    void testSame() {
        assertEquals(testEvent, testEvent);
    }
}