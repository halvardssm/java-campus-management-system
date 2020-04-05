package nl.tudelft.oopp.group39.facilities.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import nl.tudelft.oopp.group39.AbstractTest;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FacilityTest extends AbstractTest {
    private String description;
    private Facility facility1;
    private Facility facility2;
    private Facility facility3;

    @BeforeEach
    void testFacility() {
        this.description = "Facility";
        this.facility1 = new Facility(
            description,
            null
        );
        this.facility2 = new Facility(
            description,
            null
        );
        this.facility3 = new Facility(
            "Test",
            null
        );
    }

    @Test
    void getDescriptionTest() {
        assertEquals(description, facility1.getDescription());
        assertEquals(facility1.getDescription(), facility2.getDescription());
        assertNotEquals(facility1.getDescription(), facility3.getDescription());
    }

    @Test
    void equalsTest() {
        assertEquals(facility1, facility2);
        assertNotEquals(facility1, facility3);
    }
}
