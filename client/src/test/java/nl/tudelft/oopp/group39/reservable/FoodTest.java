package nl.tudelft.oopp.group39.reservable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import nl.tudelft.oopp.group39.reservable.model.Food;
import org.junit.jupiter.api.Test;

class FoodTest {

    Food food = new Food(1L, "Apple", "Really? A description for an Apple?", 0.69, 1L);

    @Test
    void getName() {
        assertEquals(food.getName(), "Apple");
    }

    @Test
    void getDescription() {
        assertEquals(food.getDescription(), "Really? A description for an Apple?");
    }

    @Test
    void testNullName() {
        assertNull(new Food().getName());
    }

    @Test
    void testInheritance() {
        assertEquals(food.getId(), 1L);
        assertEquals(food.getName(), "Apple");
        assertEquals(food.getDescription(), "Really? A description for an Apple?");
        assertEquals(food.getPrice(), 0.69);
        assertEquals(food.getBuilding(), 1L);
    }
}
