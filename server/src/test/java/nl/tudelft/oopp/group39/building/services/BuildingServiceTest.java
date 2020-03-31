package nl.tudelft.oopp.group39.building.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import nl.tudelft.oopp.group39.AbstractTest;
import nl.tudelft.oopp.group39.building.entities.Building;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BuildingServiceTest extends AbstractTest {
    private final Building testBuilding = new Building(
        "EEMCS",
        "Mekelweg 4",
        "Faculty of Electrical Engineering, Maths and Computer Science",
        LocalTime.of(7, 0),
        LocalTime.of(18, 0),
        null,
        null);

    @BeforeEach
    void setUp() {
        Building building = buildingService.createBuilding(testBuilding);
        testBuilding.setId(building.getId());
    }

    @AfterEach
    void tearDown() {
        buildingService.deleteBuilding((int) testBuilding.getId());
    }

    @Test
    void listBuildingsTest() {
        List<Building> buildings = buildingService.listBuildings(new HashMap<>());
        assertEquals(1, buildings.size());
        assertEquals(testBuilding, buildings.get(0));
    }

    @Test
    void deleteAndCreateBuildingTest() {
        buildingService.deleteBuilding((int) testBuilding.getId());

        assertEquals(new ArrayList<>(), buildingService.listBuildings(new HashMap<>()));

        Building building = buildingService.createBuilding(testBuilding);

        testBuilding.setId(building.getId());

        assertEquals(testBuilding, building);
    }

    @Test
    void readBuildingTest() {
        Building building2 = buildingService.readBuilding(testBuilding.getId());

        assertEquals(testBuilding, building2);
    }
