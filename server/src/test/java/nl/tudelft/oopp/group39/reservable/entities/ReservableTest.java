package nl.tudelft.oopp.group39.reservable.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import nl.tudelft.oopp.group39.AbstractTest;
import org.junit.jupiter.api.Test;

class ReservableTest extends AbstractTest {
    @Test
    void testEquals() {
        Reservable reservable1 = new Reservable(null, 5.6, null, null);
        Reservable reservable2 = new Reservable(null, 5.6, null, null);

        assertEquals(reservable1, reservable2);
    }

    @Test
    void testEqualsNotInstanceOf() {
        Reservable reservable = new Reservable(null, 5.6, null, null);

        assertNotEquals(reservable, new Object());
    }

    @Test
    void testEqualsSame() {
        Reservable reservable = new Reservable(null, 5.6, null, null);

        assertEquals(reservable, reservable);
    }
}