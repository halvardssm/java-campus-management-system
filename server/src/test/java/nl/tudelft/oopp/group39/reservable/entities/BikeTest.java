package nl.tudelft.oopp.group39.reservable.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import nl.tudelft.oopp.group39.AbstractTest;
import nl.tudelft.oopp.group39.reservable.enums.BikeType;
import org.junit.jupiter.api.Test;

class BikeTest extends AbstractTest {

    @Test
    void testEquals() {
        Bike bike1 = new Bike(BikeType.CITY, 5.6, null, null);
        Bike bike2 = new Bike(BikeType.CITY, 5.6, null, null);

        assertEquals(bike1, bike2);
    }

    @Test
    void testEqualsNotInstanceOf() {
        Bike bike = new Bike(BikeType.CITY, 5.6, null, null);

        assertNotEquals(bike, new Object());
    }

    @Test
    void testEqualsSame() {
        Bike bike = new Bike(BikeType.CITY, 5.6, null, null);

        assertEquals(bike, bike);
    }
}