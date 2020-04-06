package nl.tudelft.oopp.group39.booking.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BookingTest {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private User user;
    private Room room;
    private Booking booking1;
    private Booking booking2;
    private Booking booking3;

    @BeforeEach
    void testBooking() {
        this.date = LocalDate.of(2069, 4, 20);
        this.startTime = LocalTime.of(13, 0);
        this.endTime = LocalTime.of(15, 0);
        this.user = new User(
            "student",
            "student@student.tudelft.nl",
            "student123",
            null,
            Role.STUDENT
        );
        this.room = new Room(
            null,
            new Building(null, "Drebbelweg",
                "Drebbelweg 5",
                "Drebbelweg",
                LocalTime.of(6, 0),
                LocalTime.of(17, 30),
                null,
                null
            ),
            "Projectroom 1",
            8,
            true,
            "This is another room for testing purposes",
            null,
            null,
            null
        );
        this.booking1 = new Booking(
            null, date,
            startTime, endTime,
            user, room
        );
        this.booking2 = new Booking(
            null, date,
            startTime, endTime,
            user, room
        );
        this.booking3 = new Booking(
            null,
            LocalDate.of(2020, 3, 20),
            LocalTime.of(15, 0),
            LocalTime.of(16, 0),
            null,
            null
        );
    }

    @Test
    void getDateTest() {
        assertEquals(date, booking1.getDate());
        assertEquals(booking1.getDate(), booking2.getDate());
        assertNotEquals(booking1.getDate(), booking3.getDate());
    }

    @Test
    void getStartTimeTest() {
        assertEquals(startTime, booking1.getStartTime());
        assertEquals(booking1.getStartTime(), booking2.getStartTime());
        assertNotEquals(booking1.getStartTime(), booking3.getStartTime());
    }

    @Test
    void getEndTimeTest() {
        assertEquals(endTime, booking1.getEndTime());
        assertEquals(booking1.getEndTime(), booking2.getEndTime());
        assertNotEquals(booking1.getEndTime(), booking3.getEndTime());
    }

    @Test
    void getUserTest() {
        assertEquals(user, booking1.getUser());
        assertEquals(booking1.getUser(), booking2.getUser());
        assertNotEquals(booking1.getUser(), booking3.getUser());
    }

    @Test
    void getRoomTest() {
        assertEquals(room, booking1.getRoom());
        assertEquals(booking1.getRoom(), booking2.getRoom());
        assertNotEquals(booking1.getRoom(), booking3.getRoom());
    }

    @Test
    void equalsTest() {
        assertEquals(booking1, booking2);
        assertNotEquals(booking1, booking3);
    }
}
