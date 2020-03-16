package nl.tudelft.oopp.group39.booking.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.booking.repositories.BookingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookingServiceTest {
    private LocalDate date = LocalDate.now();
    private LocalTime start = LocalTime.of(4, 20, 42);
    private LocalTime end = LocalTime.of(6, 9, 20);
    private final Booking testBooking = new Booking(
        date,
        start,
        end,
        null,
        null
    );

    @Autowired
    BookingService bookingService;
    @Autowired
    BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        Booking booking = bookingService.createBooking(testBooking);
        testBooking.setId(booking.getId());
    }

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll();
    }

    @Test
    void listBookings() {
        List<Booking> bookings = bookingService.listBookings(new HashMap<>());

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

        assertEquals(testBookings, bookingService.listBookings(new HashMap<>()));
    }
}
