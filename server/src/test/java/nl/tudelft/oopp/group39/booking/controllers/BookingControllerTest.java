package nl.tudelft.oopp.group39.booking.controllers;

import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
class BookingControllerTest {
    Set<Facility> facilities = new HashSet<>();
    Set<Booking> bookings = new HashSet<>();
    private final User testUser = new User(
        "test",
        "test@tudelft.nl",
        "test",
        null,
        Role.STUDENT,
        bookings
    );
    private final Room testRoom = new Room(
        1,
        10,
        false,
        "This is a test description",
        facilities,
        bookings
    );
    LocalDate date = LocalDate.now();
    LocalTime start = LocalTime.now();
    LocalTime end = LocalTime.now();
    private final Booking testBooking = new Booking(
        date,
        start,
        end,
        testUser,
        testRoom
    );


}
