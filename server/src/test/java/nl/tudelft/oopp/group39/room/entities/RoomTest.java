package nl.tudelft.oopp.group39.room.entities;

import static nl.tudelft.oopp.group39.config.Utils.initSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import nl.tudelft.oopp.group39.AbstractTest;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomTest extends AbstractTest {
    private String name;
    private Building building;
    private int capacity;
    private boolean onlyStaff;
    private String description;
    private Set<Facility> facilities = new HashSet<>();
    private Set<Booking> bookings = new HashSet<>();
    private Set<Room> rooms = new HashSet<>();
    private Room room1;
    private Room room2;
    private Room room3;

    @BeforeEach
    void testRoom() {
        bookingService.createBooking(new Booking(
            null, LocalDate.now(),
            LocalTime.now(), LocalTime.now(),
            null, null
        ));
        facilityService.createFacility(new Facility(
            "smartboard", rooms
        ));
        facilityService.createFacility(new Facility(
            "whiteboard", rooms
        ));
        bookings.add(bookingService.readBooking(1L));
        facilities.add(facilityService.readFacility(1L));

        this.building = new Building(
            null,
            "EEMCS",
            "Mekelweg 4",
            "Faculty of Electrical Engineering, Maths and Computer Science",
            LocalTime.of(7, 0),
            LocalTime.of(18, 0),
            null,
            null
        );
        this.name = "Lecture Hall Ampere";
        this.capacity = 200;
        this.onlyStaff = false;
        this.description = "This is a room for testing purposes";
        this.facilities.addAll(initSet(facilities));
        this.bookings.addAll(initSet(bookings));
        this.room1 = new Room(null, building, name,
            capacity, onlyStaff, description,
            null, facilities, bookings
        );
        this.room2 = new Room(null, building, name,
            capacity, onlyStaff, description,
            null, facilities, bookings
        );
        this.room3 = new Room(null,
            new Building(null, "Drebbelweg",
                "Drebbelweg 5",
                "Drebbelweg",
                LocalTime.of(6, 0),
                LocalTime.of(17, 30),
                null,
                null),
            "Projectroom 1",
            8,
            true,
            "This is another room for testing purposes",
            null,
            facilities,
            bookings
        );
    }

    @Test
    void getBuildingTest() {
        assertEquals(building, room1.getBuilding());
        assertEquals(room1.getBuilding(), room2.getBuilding());
        assertNotEquals(room1.getBuilding(), room3.getBuilding());
    }

    @Test
    void getCapacityTest() {
        assertEquals(200, room1.getCapacity());
        assertEquals(8, room3.getCapacity());
        assertEquals(room1.getCapacity(), room2.getCapacity());
        assertNotEquals(room1.getCapacity(), room3.getCapacity());
    }

    @Test
    void getOnlyStaffTest() {
        assertFalse(room1.getOnlyStaff());
        assertNotEquals(false, room3.getOnlyStaff());
        assertEquals(room1.getOnlyStaff(), room2.getOnlyStaff());
    }

    @Test
    void getDescriptionTest() {
        assertEquals("This is a room for testing purposes", room1.getDescription());
        assertEquals(room1.getDescription(), room2.getDescription());
        assertNotEquals(room2.getDescription(), room3.getDescription());
    }

    @Test
    void getFacilitiesTest() {
        Set<Facility> facilities2 = new HashSet<>();
        facilities2.add(facilityService.readFacility(2L));
        Room room4 = new Room(null, building, name,
            capacity, onlyStaff, description,
            null, facilities2, bookings);

        assertEquals(room1.getFacilities(), room2.getFacilities());
        assertNotEquals(room1.getFacilities(), room4.getFacilities());
    }

    @Test
    void getBookingsTest() {
        Set<Booking> bookings2 = new HashSet<>();
        Room room4 = new Room(null, building, name,
            capacity, onlyStaff, description,
            null, facilities, bookings2);

        assertEquals(room1.getBookings(), room2.getBookings());
        assertNotEquals(room1.getBookings(), room4.getBookings());
    }

    @Test
    void getReservationsTest() {
        assertEquals(room1.getReservations(), room2.getReservations());
    }

    @Test
    void equalsTest() {
        assertEquals(room1, room2);
        assertNotEquals(room1, room3);
    }
}

