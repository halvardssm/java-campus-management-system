package nl.tudelft.oopp.group39.room.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nl.tudelft.oopp.group39.AbstractTest;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.room.exceptions.RoomNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoomServiceTest extends AbstractTest {
    Set<Facility> facilities = new HashSet<>();
    private final Room testRoom = new Room(
        null,
        null,
        "Projectroom 1",
        8,
        true,
        "This is another room for testing purposes",
        null,
        facilities,
        null
    );

    @BeforeEach
    void setUp() {
        Room room = roomService.createRoom(testRoom);
        testRoom.setId(room.getId());
    }

    @AfterEach
    void tearDown() {
        roomService.deleteRoom(testRoom.getId());
    }

    @Test
    void listRoomsTest() {
        List<Room> rooms = roomService.listRooms();

        assertEquals(1, rooms.size());
        assertEquals(testRoom.getId(), rooms.get(0).getId());
        assertEquals(testRoom.getName(), rooms.get(0).getName());
    }

    @Test
    void deleteAndCreateRoomTest() {
        roomService.deleteRoom(testRoom.getId());

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

        Room room = roomService.updateRoom(testRoom, testRoom.getId());

        assertEquals(testRoom, room);

        testRoom.setCapacity(8);
    }

    @Test
    void mapFacilitiesForRoomsTest() {
        Room room = testRoom;
        room.setFacilities(null);
        roomService.mapFacilitiesForRooms(room);

        assertEquals(testRoom, room);
        assertEquals(room.getFacilities(), facilities);
    }

    @Test
    void errorTest() {
        assertThrows(RoomNotFoundException.class, () -> {
            roomService.deleteRoom(0L);
        });
    }
}
