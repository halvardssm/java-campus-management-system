package nl.tudelft.oopp.group39.booking.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import nl.tudelft.oopp.group39.auth.services.JwtService;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.booking.repositories.BookingRepository;
import nl.tudelft.oopp.group39.booking.services.BookingService;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.room.services.RoomService;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import nl.tudelft.oopp.group39.user.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import static nl.tudelft.oopp.group39.booking.controllers.BookingController.REST_MAPPING;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTest {
    Set<Facility> facilities = new HashSet<>();
    Set<Booking> bookings = new HashSet<>();
    private final User testUser = new User(
        "test",
        "test@tudelft.nl",
        "test",
        null,
        Role.STUDENT,
        null
    );
    private final Room testRoom = new Room(
        1,
        10,
        false,
        "This is a test description",
        facilities,
        null
    );
    LocalDate date = LocalDate.now();
    LocalTime start = LocalTime.now();
    LocalTime end = LocalTime.now();
    private final Booking testBooking = new Booking(
        date,
        start,
        end,
        null,
        testRoom
    );

    private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class,
        (JsonSerializer<LocalDate>) (date, typeOfT, context)
            -> new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)))
        .registerTypeAdapter(LocalTime.class, (JsonSerializer<LocalTime>) (time, typeOfT, context)
            -> new JsonPrimitive(time.format(DateTimeFormatter.ISO_LOCAL_TIME))).create();

    private String jwt;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingController bookingController;
    @Autowired
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        Room room = roomService.createRoom(testRoom);
        Booking booking = bookingService.createBooking(testBooking);
        testBooking.setId(booking.getId());
        testBooking.setRoom(room);
        userService.createUser(testUser);
        jwt = jwtService.encrypt(testUser);
    }

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll();
        testBooking.setId(null);
        userService.deleteUser(testUser.getUsername());
    }

    @Test
    void listBookings() throws Exception {
        mockMvc.perform(get(REST_MAPPING))
            .andExpect(jsonPath("$.body").isArray())
            .andExpect(jsonPath("$.body", hasSize(1)))
            .andExpect(jsonPath("$.body[0].date", is(testBooking.getDate().toString())))
            .andExpect(jsonPath("$.body[0].startTime", is(testBooking.getStartTime().toString())))
            .andExpect(jsonPath("$.body[0].endTime", is(testBooking.getEndTime().toString())))
            .andExpect(jsonPath("$.body[0].room", is(testBooking.getRoom())));
    }

/*    @Test
    void createBooking() throws Exception {
        Booking booking = testBooking;
        String json = gson.toJson(booking);

        mockMvc.perform(post(REST_MAPPING)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, AuthController.HEADER_BEARER + jwt))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.body[0].date", is(booking.getDate().toString())))
            .andExpect(jsonPath("$.body[0].startTime", is(booking.getStartTime().toString())))
            .andExpect(jsonPath("$.body[0].endTime", is(booking.getEndTime().toString())))
            .andExpect(jsonPath("$.body[0].room", is(booking.getRoom())));
    }

    @Test
    void readBooking() throws Exception {
        mockMvc.perform(get(REST_MAPPING + "/" + testBooking.getId()))
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body[0].date", is(testBooking.getDate().toString())))
            .andExpect(jsonPath("$.body[0].startTime", is(testBooking.getStartTime().toString())))
            .andExpect(jsonPath("$.body[0].endTime", is(testBooking.getEndTime().toString())))
            .andExpect(jsonPath("$.body[0].room", is(testBooking.getRoom())));
    }

    @Test
    void updateBooking() throws Exception {
        Booking booking = testBooking;
        booking.setStartTime(LocalTime.now(ZoneId.of("Europe/Paris")));
        booking.setEndTime(LocalTime.now(ZoneId.of("Europe/Paris")).plusHours(2));
        String json = gson.toJson(booking);

        mockMvc.perform(put(REST_MAPPING + "/" + testBooking.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, AuthController.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body[0].date", is(booking.getDate().toString())))
            .andExpect(jsonPath("$.body[0].startTime", is(booking.getStartTime().toString())))
            .andExpect(jsonPath("$.body[0].endTime", is(booking.getEndTime().toString())))
            .andExpect(jsonPath("$.body[0].room", is(booking.getRoom())));
    }

    @Test
    void deleteBooking() throws Exception {
        mockMvc.perform(delete(REST_MAPPING + "/" + testBooking.getId())
            .header(HttpHeaders.AUTHORIZATION, AuthController.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").doesNotExist());
    }

    @Test
    void testError() {
        assertEquals("Target object must not be null; nested exception is "
                + "java.lang.IllegalArgumentException: Target object must not be null",
            bookingController.createBooking(null).getBody().getError());

        assertEquals("Booking 0 not found", bookingController.readBooking(0).getBody().getError());

        assertEquals("Booking 0 not found", bookingController.updateBooking(null, 0).getBody().getError());
    }
*/
}
