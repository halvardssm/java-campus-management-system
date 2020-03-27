package nl.tudelft.oopp.group39.reservation.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import nl.tudelft.oopp.group39.AbstractTest;
import org.junit.jupiter.api.Test;

class ReservationAmountTest extends AbstractTest {

    @Test
    void testEquals() {
        ReservationAmount reservationAmount1 = new ReservationAmount(1, null, null);
        ReservationAmount reservationAmount2 = new ReservationAmount(1, null, null);

        assertEquals(reservationAmount1, reservationAmount2);
    }

    @Test
    void testEqualsNotInstanceOf() {
        ReservationAmount reservationAmount1 = new ReservationAmount(1, null, null);

        assertNotEquals(reservationAmount1, new Object());
    }

    @Test
    void testEqualsSame() {
        ReservationAmount reservationAmount = new ReservationAmount(1, null, null);

        assertEquals(reservationAmount, reservationAmount);
    }
}