package nl.tudelft.oopp.group39.booking.services;

import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BookingServiceTest {
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

    @Autowired
    BookingService bookingService;

    @Test
    void listBookings() {
        List<Booking> bookings = bookingService.listBookings();

        assertEquals(1, bookings.size());
        assertEquals(testBooking, bookings.get(0));
    }

    @Test
    void createBooking() {
        Booking booking = testBooking;
        booking.setId(testBooking.getId());
        Booking booking2 = bookingService.createBooking(booking);

        assertEquals(booking, booking2);
    }

    @Test
    void readBooking() {
        Booking booking2 = bookingService.readBooking(testBooking.getId());

        assertEquals(testBooking, booking2);
    }

    @Test
    void updateBooking() {
        Booking booking = testBooking;
        booking.setDate(booking.getDate());
        booking.setStartTime(booking.getStartTime());
        booking.setEndTime(booking.getEndTime());
        booking.setUser(booking.getUser());
        booking.setRoom(booking.getRoom());
        Booking booking2 = bookingService.updateBooking(booking, testBooking.getId());

        assertEquals(booking, booking2);
    }

    @Test
    void deleteBooking() {
        List<Booking> testBookings = new ArrayList<>();
        bookingService.deleteBooking(testBooking.getId());

        assertEquals(testBookings, bookingService.listBookings());
    }
}
