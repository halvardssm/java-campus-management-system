package nl.tudelft.oopp.group39.facility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.oopp.group39.facility.model.Facility;
import org.junit.jupiter.api.Test;

class FacilityTest {

    private static Facility testFacility = new Facility(1L, "Blackboard");

    private static String facilityJSON = "{\"id\":1,\"description\":\"Blackboard\"}";

    @Test
    void getId() {
        assertEquals(testFacility.getId(), 1L);
    }

    @Test
    void getDescription() {
        assertEquals(testFacility.getDescription(), "Blackboard");
    }

    @Test
    void testEquals() throws JsonProcessingException {
        assertEquals(testFacility, new ObjectMapper().readValue(facilityJSON, Facility.class));
        assertNotEquals(testFacility, null);
        assertNotEquals(testFacility, new Object());
        assertEquals(testFacility, testFacility);
    }
}