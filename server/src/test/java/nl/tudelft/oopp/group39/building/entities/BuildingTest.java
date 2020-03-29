package nl.tudelft.oopp.group39.building.entities;

import static nl.tudelft.oopp.group39.config.Utils.initSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import nl.tudelft.oopp.group39.AbstractTest;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import nl.tudelft.oopp.group39.room.entities.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BuildingTest extends AbstractTest {
    private long id;
    private String name;
    private String location;
    private String description;
    private LocalTime open;
    private LocalTime closed;
    private Set<Room> rooms = new HashSet<>();
    private Set<Reservable> reservables = new HashSet<>();
    private Building building1;
    private Building building2;
    private Building building3;

    @BeforeEach
    void testBuilding() {
        roomService.createRoom(new Room(
            building1,
            "Lecture Hall Ampere",
            50,
            false,
            "Lecture hall in EEMCS",
            null,
            null));

        rooms.add(roomService.readRoom(1));

        this.name = "EEMCS";
        this.location = "Mekelweg 4";
        this.description = "Faculty of Electrical Engineering, Maths and Computer Science";
        this.open = LocalTime.of(7, 0);
        this.closed = LocalTime.of(18, 0);
        this.rooms.addAll(initSet(rooms));
        this.reservables.addAll(initSet(reservables));
        this.building1 = new Building(
            name, location, description,
            open, closed, rooms, reservables
        );
        this.building2 = new Building(
            name, location, description,
            open, closed, rooms, reservables
        );
        this.building3 = new Building(
            "Drebbelweg",
            "Drebbelweg 5",
            "Drebbelweg",
            LocalTime.of(6, 0),
            LocalTime.of(17, 30),
            null,
            null);
    }
    /*
    @Test
    void getIdTest() {
        assertEquals(0, building1.getId());
        assertEquals(building1.getId(), building2.getId());
        assertNotEquals(building1.getId(), building3.getId());
    } */

    @Test
    void getNameTest() {
        assertEquals("EEMCS", building1.getName());
        assertEquals(building1.getName(), building2.getName());
        assertNotEquals(building1.getName(), building3.getName());
    }

    @Test
    void getLocationTest() {
        assertEquals("Mekelweg 4", building1.getLocation());
        assertEquals(building1.getLocation(), building2.getLocation());
        assertNotEquals(building1.getLocation(), building3.getLocation());
    }

    @Test
    void getDescriptionTest() {
        assertEquals("Drebbelweg", building3.getDescription());
        assertEquals(building1.getDescription(), building2.getDescription());
        assertNotEquals(building1.getDescription(), building3.getDescription());
    }

    @Test
    void getOpenTest() {
        assertEquals(LocalTime.of(7, 0), building1.getOpen());
        assertEquals(building1.getOpen(), building2.getOpen());
        assertNotEquals(building1.getOpen(), building3.getOpen());
    }

    @Test
    void getClosedTest() {
        assertEquals(LocalTime.of(18, 0), building1.getClosed());
        assertEquals(building1.getClosed(), building2.getClosed());
        assertNotEquals(building1.getClosed(), building3.getClosed());
    }

    @Test
    void getRoomsTest() {
        assertEquals(building1.getRooms(), building2.getRooms());
        assertNotEquals(building1.getRooms(), building3.getRooms());
    }

    @Test
    void getReservablesTest() {
        assertEquals(building1.getReservables(), building2.getReservables());
    }

    @Test
    void equalsTest() {
        assertEquals(building1, building2);
        assertNotEquals(building1, building3);
    }
}
