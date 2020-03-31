package nl.tudelft.oopp.group39.room.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import nl.tudelft.oopp.group39.AbstractTest;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.room.entities.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoomServiceTest extends AbstractTest {
    Building b1 = new Building(
        "EEMCS",
        "Mekelweg 4",
        "Faculty of Electrical Engineering, Maths and Computer Science",
        LocalTime.of(7, 0),
        LocalTime.of(18, 0),
        null,
        null);

    private final Room testRoom = new Room(b1,
        "Projectroom 1",
        8,
        true,
        "This is another room for testing purposes",
        null,
        null
    );

    @BeforeEach
    void setUp() {
        Room room = roomService.createRoom(testRoom);
        testRoom.setId(room.getId());
    }

    @AfterEach
    void tearDown() {
        System.out.println((int) testRoom.getId());
        roomService.deleteRoom((int) testRoom.getId());
    }

    @Test
    void listRoomsTest() {
        List<Room> rooms = roomService.listRooms();
        int id = Math.toIntExact(testRoom.getId());

        assertEquals(1, rooms.size());
        assertEquals(testRoom, rooms.get(id));
    }

    @Test
    void deleteAndCreateRoomTest() {
        roomService.deleteRoom((int) testRoom.getId());

        assertEquals(new ArrayList<>(), roomService.listRooms());

        Room room = roomService.createRoom(testRoom);

        testRoom.setId(room.getId());

        assertEquals(testRoom, room);
    }

    @Test
    void readRoomTest() {
        Room room2 = roomService.readRoom(testRoom.getId());

        assertEquals(testRoom, room2);
    }

    @Test
    void updateRoomTest() {
        testRoom.setCapacity(50);

        Room room = roomService.updateRoom(testRoom, (int) testRoom.getId());

        assertEquals(testRoom, room);

        testRoom.setCapacity(8); //because 8 was the original value of capacity
    }
}
