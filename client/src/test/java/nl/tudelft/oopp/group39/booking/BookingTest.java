package nl.tudelft.oopp.group39.booking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.oopp.group39.booking.model.Booking;
import org.junit.jupiter.api.Test;

class BookingTest {

    public static Booking testBooking = new Booking(
        1,
        "10-11-1938",
        "00:00:00",
        "09:05:00",
        "f.oft.1938",
        1L);

    @Test
    void getId() {
        assertEquals(testBooking.getId(),1);
    }

    @Test
    void getDate() {
        assertEquals(testBooking.getDate(), "10-11-1938");
    }

    @Test
    void getStartTime() {
        assertEquals(testBooking.getStartTime(), "00:00:00");
    }

    @Test
    void getEndTime() {
        assertEquals(testBooking.getEndTime(), "09:05:00");
    }

    @Test
    void getUser() {
        assertEquals(testBooking.getUser(), "f.oft.1938");
    }

    @Test
    void getRoom() {
        assertEquals(testBooking.getRoom(), 1L);
    }

    @Test
    void testEquals() {
        assertEquals(testBooking, testBooking);
        assertNotEquals(testBooking, null);
        assertNotEquals(testBooking, new Object());
    }

    @Test
    void testJsonConversion() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Booking reconvEvent = mapper.readValue(
            mapper.writeValueAsString(testBooking), Booking.class);

        assertEquals(reconvEvent, testBooking);
    }
}