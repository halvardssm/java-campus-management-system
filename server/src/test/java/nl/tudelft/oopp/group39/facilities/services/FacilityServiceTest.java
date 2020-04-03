package nl.tudelft.oopp.group39.facilities.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import nl.tudelft.oopp.group39.AbstractTest;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FacilityServiceTest extends AbstractTest {
    private final Facility testFacility = new Facility(
        "Facility",
        null
    );

    @BeforeEach
    void setUp() {
        Facility facility = facilityService.createFacility(testFacility);
        testFacility.setId(facility.getId());
    }

    @AfterEach
    void tearDown() {
        facilityService.deleteFacility(testFacility.getId());
    }

    @Test
    void listFacilitiesTest() {
        List<Facility> facilities = facilityService.listFacilities();

        assertEquals(1, facilities.size());
        assertEquals(testFacility, facilities.get(0));
    }

    @Test
    void deleteAndCreateFacilityTest() {
        facilityService.deleteFacility(testFacility.getId());

        assertEquals(new ArrayList<>(), facilityService.listFacilities());

        Facility facility = facilityService.createFacility(testFacility);

        testFacility.setId(facility.getId());

        assertEquals(testFacility, facility);
    }

    @Test
    void readFacilityTest() {
        Facility facility2 = facilityService.readFacility(testFacility.getId());

        assertEquals(testFacility, facility2);
    }

    @Test
    void updateFacilityTest() {
        testFacility.setDescription("updated");

        Facility facility = facilityService.updateFacility(testFacility, testFacility.getId());

        assertEquals(testFacility, facility);

        testFacility.setDescription("Facility");
    }
}
