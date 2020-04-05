package nl.tudelft.oopp.group39.booking.controllers;

import static nl.tudelft.oopp.group39.booking.controllers.BookingController.REST_MAPPING;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import nl.tudelft.oopp.group39.AbstractControllerTest;
import nl.tudelft.oopp.group39.booking.dto.BookingDto;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.user.entities.User;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BookingControllerTest extends AbstractControllerTest {
    private final LocalDate date = LocalDate.now();
    private final LocalTime start = LocalTime.of(4, 20, 42);
    private final LocalTime end = LocalTime.of(6, 9, 20);
    private final Room testRoom = new Room(
        null,
        null,
        "Projectroom 1",
        8,
        true,
        "This is another room for testing purposes",
        null,
        null,
        null
    );
    private final BookingDto testBooking = new BookingDto(
        null,
        date,
        start,
        end,
        testUser.getUsername(),
        testRoom.getId()
    );

    @BeforeEach
    void setUp() {
        User user = userService.createUser(testUser, true);
        jwt = jwtService.encrypt(testUser);

        Room room = roomService.createRoom(testRoom);
        testBooking.setRoom(room.getId());

        Booking booking = bookingService.createBooking(testBooking);
        testBooking.setId(booking.getId());
    }

    @AfterEach
    void tearDown() {
        bookingService.deleteBooking(testBooking.getId());
        roomService.deleteRoom(testRoom.getId());
        userService.deleteUser(testUser.getUsername());
    }

    @Test
    void listBookingsTest() throws Exception {
        Map<String, Matcher<?>> map = new HashMap<>();

        map.put(Booking.COL_DATE, is(testBooking.getDate().toString()));
        map.put(Booking.COL_START_TIME, is(testBooking.getStartTime().toString()));
        map.put(Booking.COL_END_TIME, is(testBooking.getEndTime().toString()));

        listTest(REST_MAPPING, map);
    }

    @Test
    void listBookingsFilterTest() throws Exception {
        Map<String, Matcher<?>> map = new HashMap<>();

        map.put(Booking.COL_DATE, is(testBooking.getDate().toString()));
        map.put(Booking.COL_START_TIME, is(testBooking.getStartTime().toString()));
        map.put(Booking.COL_END_TIME, is(testBooking.getEndTime().toString()));

        listTest(REST_MAPPING, map);
    }

    @Test
    void deleteAndCreateBookingTest() throws Exception {
        deleteTest(REST_MAPPING, testBooking);

        Map<String, Matcher<?>> map = new HashMap<>();

        map.put(Booking.COL_DATE, is(testBooking.getDate().toString()));
        map.put(Booking.COL_START_TIME, is(testBooking.getStartTime().toString()));
        map.put(Booking.COL_END_TIME, is(testBooking.getEndTime().toString()));

        createTest(REST_MAPPING, testBooking, map);
    }

    @Test
    void readBookingTest() throws Exception {
        Map<String, Matcher<?>> map = new HashMap<>();

        map.put(Booking.COL_DATE, is(testBooking.getDate().toString()));
        map.put(Booking.COL_START_TIME, is(testBooking.getStartTime().toString()));
        map.put(Booking.COL_END_TIME, is(testBooking.getEndTime().toString()));

        readTest(REST_MAPPING, testBooking, map);
    }

    @Test
    void updateBookingTest() throws Exception {
        testBooking.setDate(date.plusDays(2));

        Map<String, Matcher<?>> map = new HashMap<>();

        map.put(Booking.COL_DATE, is(testBooking.getDate().toString()));
        map.put(Booking.COL_START_TIME, is(testBooking.getStartTime().toString()));
        map.put(Booking.COL_END_TIME, is(testBooking.getEndTime().toString()));

        updateTest(REST_MAPPING, testBooking, map);

        testBooking.setDate(date);
    }

    @Test
    void errorTest() {
        assertEquals(
            "The given id must not be null!; nested exception is java.lang"
                + ".IllegalArgumentException: The given id must not be null!",
            bookingController.create(null, new BookingDto()).getBody().getError()
        );

        assertEquals(
            "Booking with id '0' wasn't found.",
            bookingController.read(0L).getBody().getError()
        );

        assertEquals(
            "The given id must not be null!; nested exception is "
                + "java.lang.IllegalArgumentException: The given id must not be null!",
            bookingController.update(null, testBooking, null).getBody().getError()
        );
    }

}
