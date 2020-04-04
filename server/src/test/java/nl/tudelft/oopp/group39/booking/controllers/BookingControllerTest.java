package nl.tudelft.oopp.group39.booking.controllers;

import static nl.tudelft.oopp.group39.booking.controllers.BookingController.REST_MAPPING;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import nl.tudelft.oopp.group39.AbstractControllerTest;
import nl.tudelft.oopp.group39.booking.dto.BookingDto;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.config.Constants;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.user.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

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
        User user = userService.createUser(testUser);
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
        mockMvc.perform(get(REST_MAPPING))
            .andExpect(jsonPath("$.body").isArray())
            .andExpect(jsonPath("$.body", hasSize(1)))
            .andExpect(jsonPath("$.body[0].date", is(testBooking.getDate().toString())))
            .andExpect(jsonPath("$.body[0].startTime", is(testBooking.getStartTime().toString())))
            .andExpect(jsonPath("$.body[0].endTime", is(testBooking.getEndTime().toString())));
    }

    @Test
    void deleteAndCreateBookingTest() throws Exception {
        mockMvc.perform(delete(REST_MAPPING + "/" + testBooking.getId())
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").doesNotExist());

        testBooking.setId(null);

        String json = objectMapper.writeValueAsString(testBooking);

        mockMvc.perform(post(REST_MAPPING)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.body.date", is(testBooking.getDate().toString())))
            .andExpect(jsonPath("$.body.startTime", is(testBooking.getStartTime().toString())))
            .andExpect(jsonPath("$.body.endTime", is(testBooking.getEndTime().toString())))
            .andDo((booking) -> {
                String responseString = booking.getResponse().getContentAsString();
                JsonNode productNode = new ObjectMapper().readTree(responseString);
                testBooking.setId(productNode.get("body").get("id").longValue());
            });
    }

    @Test
    void readBookingTest() throws Exception {
        mockMvc.perform(get(REST_MAPPING + "/" + testBooking.getId()))
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body.date", is(testBooking.getDate().toString())))
            .andExpect(jsonPath("$.body.startTime", is(testBooking.getStartTime().toString())))
            .andExpect(jsonPath("$.body.endTime", is(testBooking.getEndTime().toString())));
    }

    @Test
    void updateBookingTest() throws Exception {
        testBooking.setDate(date.plusDays(2));
        String json = objectMapper.writeValueAsString(testBooking);

        mockMvc.perform(put(REST_MAPPING + "/" + testBooking.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body.date", is(testBooking.getDate().toString())))
            .andExpect(jsonPath("$.body.startTime", is(testBooking.getStartTime().toString())))
            .andExpect(jsonPath("$.body.endTime", is(testBooking.getEndTime().toString())));

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
