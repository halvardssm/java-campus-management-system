package nl.tudelft.oopp.group39.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.time.LocalDateTime;
import java.util.Arrays;
import nl.tudelft.oopp.group39.reservation.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationTest {

    private ObjectMapper mapper = new ObjectMapper();

    private String reservationJson = "[\n"
        + "{\"id\": 2,\"amount\": 1,\"reservable\": 1}"
        + ","
        + "{\"id\": 1,\"amount\": 5,\"reservable\": 4}]";

    private Reservation testReservation = new Reservation(
        1,
        "2020-04-01T16:09:00",
        "2020-04-01T22:00:00",
        1L,
        null,
        "admin"
    );

    @BeforeEach
    void setUp() throws JsonProcessingException {
        testReservation.setReservationAmounts((ArrayNode) mapper.readTree(reservationJson));
    }

    @Test
    void getId() {
        assertEquals(testReservation.getId(), 1);
        assertNull(new Reservation().getId());
    }

    @Test
    void getTimeOfDelivery() {
        assertEquals(testReservation.getTimeOfDelivery(), "2020-04-01T16:09:00");
    }

    @Test
    void getTimeOfPickup() {
        assertEquals(testReservation.getTimeOfPickup(), "2020-04-01T22:00:00");
    }

    @Test
    void getRoom() {
        assertEquals(testReservation.getRoom(), 1L);
    }

    @Test
    void getReservationAmounts() throws JsonProcessingException {
        assertEquals(testReservation.getReservationAmounts(), mapper.readTree(reservationJson));
    }

    @Test
    void getReservable() {
        assertEquals(testReservation.getReservables(), Arrays.asList(1L, 4L));
    }

    @Test
    void getPickupTime() {
        assertEquals(LocalDateTime.parse("2020-04-01T22:00"), testReservation.getPickupTime());
    }

    @Test
    void getDeliveryTime() {
        assertEquals(LocalDateTime.parse("2020-04-01T16:09:00"), testReservation.getDeliveryTime());
    }

    @Test
    void getUser() {
        assertEquals("admin", testReservation.getUser());
    }
}