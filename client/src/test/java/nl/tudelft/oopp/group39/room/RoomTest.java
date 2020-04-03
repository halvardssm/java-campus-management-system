package nl.tudelft.oopp.group39.room;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.time.LocalTime;
import java.util.HashSet;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.room.model.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomTest {

    private static ObjectMapper mapper = new ObjectMapper();

    private static Building testBuilding = new Building(
        1L, "Test", "Test Location", "Test Description",
        LocalTime.of(0,6, 9),
        LocalTime.of(16,20, 0),
        new HashSet<>());

    private static Room testRoom = new Room(1L,
        30,
        "Test",
        false,
        "Test Description",
        1L,
        null,
        null);

    private static String facString = "[{\"id\": 1,\"description\": \"smartboard\"}]";
    private static String bookString = "[{\"id\":4,\"date\":\"2020-04-01\",\"startTime\":\"13:00:00\",\"endTime\":\"15:00:00\",\"user\":\"admin\",\"room\":2}]";


    @BeforeEach
    void setUp() throws JsonProcessingException {
        testBuilding.getRooms().add(testRoom);
        testRoom.setFacilities(
            (ArrayNode) mapper.readTree(facString)
        );
        testRoom.setBookings(
            (ArrayNode) mapper.readTree(bookString)
        );
    }

    @AfterEach
    void tearDown() {
        testBuilding.setRooms(new HashSet<>());
    }

    @Test
    void getId() {
        assertEquals(testRoom.getId(), 1L);
    }

    @Test
    void getBuilding() {
        assertEquals(testRoom.getBuilding(), testBuilding.getId());
    }

    @Test
    void getName() {
        assertEquals(testRoom.getName(), "Test");
    }

    @Test
    void getCapacity() {
        assertEquals(testRoom.getCapacity(), 30);
    }

    @Test
    void isOnlyStaff() {
        assertEquals(testRoom.isOnlyStaff(), false);
    }

    @Test
    void getDescription() {
        assertEquals(testRoom.getDescription(), "Test Description");
    }

    @Test
    void getFacilities() throws JsonProcessingException {
        assertEquals(testRoom.getFacilities(), mapper.readTree(facString));
    }

    @Test
    void getBookings() throws JsonProcessingException {
        assertEquals(testRoom.getBookings(), mapper.readTree(bookString));
    }

    @Test
    void testEqualsNull() {
        assertNotEquals(testRoom,null);
    }

    @Test
    void testEqualsDiffType() {
        assertNotEquals(testRoom,testBuilding);
    }

    @Test
    void testEquals() throws JsonProcessingException {

        Room testRoom2 = new Room(1L,
            30,
            "Test",
            false,
            "Test Description",
            1L,
            (ArrayNode) mapper.readTree(facString),
            (ArrayNode) mapper.readTree(bookString));

        assertEquals(testRoom, testRoom2);
    }

    @Test
    void testHashCode() throws JsonProcessingException {

        Room testRoom2 = new Room(1L,
            30,
            "Test",
            false,
            "Test Description",
            1L,
            (ArrayNode) mapper.readTree(facString),
            (ArrayNode) mapper.readTree(bookString));


        assertNotEquals(testRoom.hashCode(), testRoom2);
    }

    @Test
    void testHashNull() {
        assertEquals(31, (new Room()).hashCode());
    }
}