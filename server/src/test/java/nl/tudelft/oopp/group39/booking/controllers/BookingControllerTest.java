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
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.config.Constants;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class BookingControllerTest extends AbstractControllerTest {
    private final LocalDate date = LocalDate.now();
    private final LocalTime start = LocalTime.of(4, 20, 42);
    private final LocalTime end = LocalTime.of(6, 9, 20);
    private final User testUser = new User(
        "test",
        "test@tudelft.nl",
        "test",
        null,
        Role.ADMIN,
        null,
        null
    );
    private String jwt;
    private final Booking testBooking = new Booking(
        null,
        date,
        start,
        end,
        testUser,
        null
    );

    @BeforeEach
    void setUp() {
        //userService.createUser(testUser);
        jwt = jwtService.encrypt(testUser);

        Booking booking = bookingService.createBooking(testBooking);
        testBooking.setId(booking.getId());
    }

    @AfterEach
    void tearDown() {
        bookingService.deleteBooking(testBooking.getId());
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

        mockMvc.perform(post(BookingController.REST_MAPPING)
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
        mockMvc.perform(get(BookingController.REST_MAPPING + "/" + testBooking.getId()))
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body.date", is(testBooking.getDate().toString())))
            .andExpect(jsonPath("$.body.startTime", is(testBooking.getStartTime().toString())))
            .andExpect(jsonPath("$.body.endTime", is(testBooking.getEndTime().toString())));
    }

    @Test
    void updateBookingTest() throws Exception {
        testBooking.setDate(date.plusDays(2));
        String json = objectMapper.writeValueAsString(testBooking);

        mockMvc.perform(put(BookingController.REST_MAPPING + "/" + testBooking.getId())
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
            "Target object must not be null; nested exception is "
                + "java.lang.IllegalArgumentException: Target object must not be null",
            bookingController.createBooking(null).getBody().getError()
        );

        assertEquals("Booking 0 not found", bookingController.readBooking(0L).getBody().getError());

        /*
        assertEquals(
            "Booking 0 not found",
            bookingController.updateBooking(0L, null).getBody().getError()
        ); */
    }

}
