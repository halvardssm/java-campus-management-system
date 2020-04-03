package nl.tudelft.oopp.group39.building;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.building.model.BuildingCapacityComparator;
import nl.tudelft.oopp.group39.room.model.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BuildingTest {

    private static ObjectMapper mapper = new ObjectMapper();

    public static Building testBuilding = new Building(
        1L, "Test", "Test Location", "Test Description",
        LocalTime.of(0,6, 9),
        LocalTime.of(16,20, 0),
        new HashSet<>());

    public static Room testRoom = new Room(1L,30, "Test", false, "Test Description", 1L, null, null);
    public static Room testRoom2 = new Room(2L, 101, "Largest", false, "Room with largest capacity", 1L, null, null);
    public static Room testRoom3 = new Room(3L, 10, "Test3", true, "Test Room 3", 1L, null, null);

    public static final String json = "{\"id\":1,\"name\":\"Test\",\"location\":\"Test Location\",\"description\":\"Test Description\",\"open\":\"00:06:09\",\"closed\":\"16:20:00\",\"rooms\":" +
        "[" +
        "{\"id\":1,\"building\":1,\"name\":\"Test\",\"description\":\"Test Description\",\"onlyStaff\":false,\"capacity\":30,\"facilities\":null,\"bookings\":null}," +
        "{\"id\":2,\"building\":1,\"name\":\"Largest\",\"description\":\"Room with largest capacity\",\"onlyStaff\":false,\"capacity\":101,\"facilities\":null,\"bookings\":null}," +
        "{\"id\":3,\"building\":1,\"name\":\"Test3\",\"description\":\"Test Room 3\",\"onlyStaff\":true,\"capacity\":10,\"facilities\":null,\"bookings\":null}" +
        "]}";


    @BeforeEach
    public void setup() {
        testBuilding.getRooms().add(testRoom);
        testBuilding.getRooms().add(testRoom2);
        testBuilding.getRooms().add(testRoom3);
    }

    @AfterEach
    public void tearDown() {
        testBuilding.getRooms().clear();
    }

    @Test
    public void assertSame() {
        assertEquals(testBuilding,testBuilding);
    }

    @Test
    public void testNull() {
        assertNotEquals(null, testBuilding);
    }

    @Test
    public void checkDiffClasses() {
        assertNotEquals(testBuilding, testRoom);
    }

    @Test
    public void jsonParseTest() throws JsonProcessingException {
        Building building = mapper.readValue(json, Building.class);
        assertEquals(building, testBuilding);
    }

    @Test
    public void jsonSerializeTest() throws JsonProcessingException {
        String parsedJson = mapper.writeValueAsString(testBuilding);
        assertEquals(json, parsedJson);
    }

    @Test
    public void testGetters() {
        assertEquals(testBuilding.getId(), 1L);
        assertEquals(testBuilding.getName(), "Test");
        assertEquals(testBuilding.getDescription(), "Test Description");
        assertEquals(testBuilding.getOpen(), LocalTime.of(0,6,9));
        assertEquals(testBuilding.getClosed(), LocalTime.of(16,20,0));
        assertTrue(testBuilding.getRooms().containsAll(Arrays.asList(testRoom, testRoom2, testRoom3)));
    }

    @Test
    public void testSetters() {
        Building newBuilding = new Building(
            1L, "TestChange", "TestChangeLoc", "DescChange",
            LocalTime.of(13,37, 9),
            LocalTime.of(16,20, 0),
            new HashSet<>());

        Building newBuilding2 = new Building(
            1L, "Test", "Test Location", "Test Description",
            LocalTime.of(0,6, 9),
            LocalTime.of(16,20, 0),
            new HashSet<>());

        newBuilding2.setName("TestChange");
        newBuilding2.setLocation("TestChangeLoc");
        newBuilding2.setDescription("DescChange");
        newBuilding2.setOpen(LocalTime.of(13,37, 9));
        newBuilding2.setClosed(LocalTime.of(16,20, 0));

        assertEquals(newBuilding, newBuilding2);
    }

    @Test
    public void getMaxCapacity() {
        assertEquals(testBuilding.getMaxCapacity(),101);
    }

    @Test
    public void testNoRoomsCapacity() {
        assertEquals((new Building()).getMaxCapacity(), 0);
    }

    @Test
    void getId() {
        assertEquals(testBuilding.getId(), 1L);
    }

    @Test
    void getNullId() {
        assertNull(new Building().getId());
    }

    @Test
    void getName() {
        assertEquals(testBuilding.getName(), "Test");
    }

    @Test
    void getLocation() {
        assertEquals(testBuilding.getLocation(), "Test Location");
    }


    @Test
    void getDescription() {
        assertEquals(testBuilding.getDescription(),"Test Description");
    }

    @Test
    void getOpen() {
        assertEquals(testBuilding.getOpen(),LocalTime.of(0,6, 9));
    }

    @Test
    void getClosed() {
        assertEquals(testBuilding.getClosed(),LocalTime.of(16,20, 0));
    }

    @Test
    void getRooms() {
        Set<Room> roomSet = new HashSet<>(Arrays.asList(testRoom, testRoom2, testRoom3));
        assertEquals(testBuilding.getRooms(),roomSet);
    }

    @Test
    void setId() {
        Building building = new Building();
        building.setId(2L);
        assertEquals(building.getId(),2L);
    }

    @Test
    void setName() {
        Building building = new Building();
        building.setName("Name");
        assertEquals(building.getName(),"Name");
    }

    @Test
    void setLocation() {
        Building building = new Building();
        building.setLocation("Loc");
        assertEquals(building.getLocation(),"Loc");
    }

    @Test
    void setDescription() {
        Building building = new Building();
        building.setDescription("Desc");
        assertEquals(building.getDescription(),"Desc");
    }

    @Test
    void setOpen() {
        Building building = new Building();
        building.setOpen(LocalTime.of(0, 0, 0));
        assertEquals(building.getOpen(),LocalTime.of(0, 0, 0));
    }

    @Test
    void setClosed() {
        Building building = new Building();
        building.setClosed(LocalTime.of(0, 0, 0));
        assertEquals(building.getClosed(),LocalTime.of(0,0,0));
    }

    @Test
    void setRooms() {
        Building building = new Building();
        building.setRooms(new HashSet<>(Arrays.asList(testRoom,testRoom2)));
        assertEquals(building.getRooms(),new HashSet<>(Arrays.asList(testRoom,testRoom2)));
    }

    @Test
    void testEquals() {
        Building building = new Building(
            1L, "Test", "Test Location", "Test Description",
            LocalTime.of(0,6, 9),
            LocalTime.of(16,20, 0),
            new HashSet<>(Arrays.asList(testRoom,testRoom2,testRoom3)));
        assertEquals(building,testBuilding);
    }

    @Test
    void testHashCode() {
        Building building = new Building();
        assertEquals(building.hashCode(), 31);
    }


    @Test
    void compare() {
        Building building = new Building(
            1L, "Test", "Test Location", "Test Description",
            LocalTime.of(0,6, 9),
            LocalTime.of(16,20, 0),
            new HashSet<>(Arrays.asList(testRoom,testRoom3)));
        assertEquals(new BuildingCapacityComparator().compare(building,testBuilding),-1);
    }
    @Test
    void compareEmpty() {
        Building building = new Building();
        assertEquals(new BuildingCapacityComparator().compare(building,testBuilding),-1);
        assertEquals(new BuildingCapacityComparator().compare(testBuilding,building),1);
        assertEquals(new BuildingCapacityComparator().compare(building,building),0);
    }
}
