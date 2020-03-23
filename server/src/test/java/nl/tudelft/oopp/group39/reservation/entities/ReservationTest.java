package nl.tudelft.oopp.group39.reservation.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;
import nl.tudelft.oopp.group39.AbstractTest;
import org.junit.jupiter.api.Test;

class ReservationTest extends AbstractTest {

    @Test
    void testEquals() {
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation1 = new Reservation(
            now,
            now.plusHours(2),
            null,
            null,
            null
        );
        Reservation reservation2 = new Reservation(
            now,
            now.plusHours(2),
            null,
            null,
            null
        );

        assertEquals(reservation1, reservation2);
    }

    @Test
    void testEqualsNotInstanceOf() {
        Reservation reservation1 = new Reservation(
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(2),
            null,
            null,
            null
        );

        assertNotEquals(reservation1, new Object());
    }

    @Test
    void testEqualsSame() {
        Reservation reservation = new Reservation(
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(2),
            null,
            null,
            null
        );

        assertEquals(reservation, reservation);
    }
}