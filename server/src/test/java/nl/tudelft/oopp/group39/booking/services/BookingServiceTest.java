package nl.tudelft.oopp.group39.booking.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookingServiceTest {
    private final LocalDate date = LocalDate.now();
    private final LocalTime start = LocalTime.of(4, 20, 42);
    private final LocalTime end = LocalTime.of(6, 9, 20);
    private final Booking testBooking = new Booking(
        date,
        start,
        end,
        null,
        null
    );

    @Autowired
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        Booking booking = bookingService.createBooking(testBooking);
        testBooking.setId(booking.getId());
    }

    @AfterEach
    void tearDown() {
        bookingService.deleteBooking(testBooking.getId());
    }

    @Test
    void listBookings() {
        List<Booking> bookings = bookingService.listBookings(new HashMap<>());

        assertEquals(1, bookings.size());
        assertEquals(testBooking, bookings.get(0));
    }

    @Test
    void deleteAndCreateBooking() {
        bookingService.deleteBooking(testBooking.getId());

        assertEquals(new ArrayList<>(), bookingService.listBookings(new HashMap<>()));

        Booking booking = bookingService.createBooking(testBooking);

        testBooking.setId(booking.getId());

        assertEquals(testBooking, booking);
    }

    @Test
    void readBooking() {
        Booking booking2 = bookingService.readBooking(testBooking.getId());

        assertEquals(testBooking, booking2);
    }

    @Test
    void updateBooking() {
        testBooking.setDate(date.plusDays(1));

        Booking booking = bookingService.updateBooking(testBooking, testBooking.getId());

        assertEquals(testBooking, booking);

        testBooking.setDate(date);
    }
}
