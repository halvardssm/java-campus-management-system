package nl.tudelft.oopp.group39.reservable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.oopp.group39.reservable.model.Reservable;
import org.junit.jupiter.api.Test;

class ReservableTest {

    private static ObjectMapper mapper = new ObjectMapper();

    private static Reservable testReservable = new Reservable(1L, 6.9, 1L);

    @Test
    void getId() {
        assertEquals(testReservable.getId(), 1L);
    }

    @Test
    void getPrice() {
        assertEquals(testReservable.getPrice(), 6.9);
    }

    @Test
    void getBuilding() {
        assertEquals(testReservable.getBuilding(), 1L);
    }

    @Test
    void getNullId() {
        assertNull(new Reservable().getId());
    }
}