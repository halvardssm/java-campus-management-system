package nl.tudelft.oopp.group39.reservable.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import nl.tudelft.oopp.group39.AbstractTest;
import org.junit.jupiter.api.Test;

class FoodTest extends AbstractTest {

    @Test
    void testEquals() {
        Food food1 = new Food("Name", "Description", 5.6, null, null);
        Food food2 = new Food("Name", "Description", 5.6, null, null);

        assertEquals(food1, food2);
    }

    @Test
    void testEqualsNotInstanceOf() {
        Food food = new Food("Name", "Description", 5.6, null, null);

        assertNotEquals(food, new Object());
    }

    @Test
    void testEqualsSame() {
        Food food = new Food("Name", "Description", 5.6, null, null);

        assertEquals(food, food);
    }
}