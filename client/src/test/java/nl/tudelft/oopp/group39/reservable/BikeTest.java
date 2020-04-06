package nl.tudelft.oopp.group39.reservable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import nl.tudelft.oopp.group39.reservable.model.Bike;
import org.junit.jupiter.api.Test;

class BikeTest {

    Bike bike = new Bike(1L, 42.0, 1L, "CITY", "69420");

    @Test
    void getBikeType() {
        assertEquals(bike.getBikeType(), "CITY");
    }

    @Test
    void getRentalDuration() {
        assertEquals(bike.getRentalDuration(), "69420");
    }

    @Test
    void testNullGetter() {
        assertNull(new Bike().getBikeType());
    }
}