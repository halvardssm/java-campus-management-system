package nl.tudelft.oopp.group39.event;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import nl.tudelft.oopp.group39.event.model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventTest {

    static Event testEvent = new Event("TestType", LocalDate.of(1881,5,19), LocalDate.of(1938,11,10));

    @Test
    void getId() {
        assertNull(testEvent.getId());
    }

    @Test
    void jsonConversionTest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String convert = mapper.writeValueAsString(testEvent);

        Event reconvEvent = mapper.readValue(convert, Event.class);

        assertEquals(reconvEvent, testEvent);
    }

    @Test
    void getType() {
        assertEquals(testEvent.getType(), "TestType");
    }

    @Test
    void getStartDate() {
        assertEquals(testEvent.getStartDate(), LocalDate.of(1881,5,19));
    }

    @Test
    void getEndDate() {
        assertEquals(testEvent.getEndDate(), LocalDate.of(1938,11,10));
    }

    @Test
    void testIncompatible() {
        assertNotEquals(testEvent, null);
        assertNotEquals(testEvent, new Object());
    }

    @Test
    void testSame() {
        assertEquals(testEvent,testEvent);
    }
}