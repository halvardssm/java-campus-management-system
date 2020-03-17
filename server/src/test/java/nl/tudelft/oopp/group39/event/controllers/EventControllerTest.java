package nl.tudelft.oopp.group39.event.controllers;

import static nl.tudelft.oopp.group39.event.controllers.EventController.REST_MAPPING;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import nl.tudelft.oopp.group39.auth.controllers.AuthController;
import nl.tudelft.oopp.group39.auth.services.JwtService;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.event.entities.Event;
import nl.tudelft.oopp.group39.event.enums.EventTypes;
import nl.tudelft.oopp.group39.event.repositories.EventRepository;
import nl.tudelft.oopp.group39.event.services.EventService;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import nl.tudelft.oopp.group39.user.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class,
        (JsonSerializer<LocalDate>) (date, typeOfT, context)
            -> new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE))).create();
    private static Event testEvent = new Event(
        EventTypes.EVENT,
        LocalDate.now(ZoneId.of("Europe/Paris")),
        LocalDate.now(ZoneId.of("Europe/Paris")).plusDays(1),
        null
    );
    Set<Booking> bookings = new HashSet<>();
    private final User testUser = new User(
        "test",
        "test@tudelft.nl",
        "test",
        null,
        Role.ADMIN,
        bookings
    );
    private String jwt;


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private EventController eventController;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        Event event = eventService.createEvent(testEvent);
        testEvent.setId(event.getId());
        userService.createUser(testUser);
        jwt = jwtService.encrypt(testUser);
    }

    @AfterEach
    void tearDown() {
        eventRepository.deleteAll();
        testEvent.setId(null);
        userService.deleteUser(testUser.getUsername());
    }

    @Test
    void listEvents() throws Exception {
        mockMvc.perform(get(REST_MAPPING))
            .andExpect(jsonPath("$.body").isArray())
            .andExpect(jsonPath("$.body", hasSize(1)))
            .andExpect(jsonPath("$.body[0].type", is(testEvent.getType().name())))
            .andExpect(jsonPath("$.body[0].startDate", is(testEvent.getStartDate().toString())))
            .andExpect(jsonPath("$.body[0].endDate", is(testEvent.getEndDate().toString())))
            .andExpect(jsonPath("$.body[0].rooms").isArray())
            .andExpect(jsonPath("$.body[0].rooms", hasSize(0)));
    }

    @Test
    void createEvent() throws Exception {
        Event event = testEvent;
        event.setType(EventTypes.HOLIDAY);
        String json = gson.toJson(event);

        mockMvc.perform(post(REST_MAPPING)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, AuthController.HEADER_BEARER + jwt))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.body.type", is(event.getType().name())))
            .andExpect(jsonPath("$.body.startDate", is(event.getStartDate().toString())))
            .andExpect(jsonPath("$.body.endDate", is(event.getEndDate().toString())))
            .andExpect(jsonPath("$.body.rooms").isArray())
            .andExpect(jsonPath("$.body.rooms", hasSize(0)));
    }

    @Test
    void readEvent() throws Exception {
        mockMvc.perform(get(REST_MAPPING + "/" + testEvent.getId()))
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body.type", is(testEvent.getType().name())))
            .andExpect(jsonPath("$.body.startDate", is(testEvent.getStartDate().toString())))
            .andExpect(jsonPath("$.body.endDate", is(testEvent.getEndDate().toString())))
            .andExpect(jsonPath("$.body.rooms").isArray())
            .andExpect(jsonPath("$.body.rooms", hasSize(0)));
    }

    @Test
    void updateEvent() throws Exception {
        Event event = testEvent;
        event.setType(EventTypes.HOLIDAY);
        event.setStartDate(LocalDate.now(ZoneId.of("Europe/Paris")).plusDays(3));
        event.setEndDate(LocalDate.now(ZoneId.of("Europe/Paris")).plusDays(5));
        String json = gson.toJson(event);

        mockMvc.perform(put(REST_MAPPING + "/" + testEvent.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, AuthController.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body.type", is(EventTypes.HOLIDAY.name())))
            .andExpect(jsonPath("$.body.startDate", is(event.getStartDate().toString())))
            .andExpect(jsonPath("$.body.endDate", is(event.getEndDate().toString())))
            .andExpect(jsonPath("$.body.rooms").isArray())
            .andExpect(jsonPath("$.body.rooms", hasSize(0)));
    }

    @Test
    void deleteEvent() throws Exception {
        mockMvc.perform(delete(REST_MAPPING + "/" + testEvent.getId())
            .header(HttpHeaders.AUTHORIZATION, AuthController.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").doesNotExist());
    }

    @Test
    void testError() {
        assertEquals("Target object must not be null; nested exception is "
                + "java.lang.IllegalArgumentException: Target object must not be null",
            eventController.createEvent(null).getBody().getError());

        assertEquals("Event 0 not found", eventController.readEvent(0).getBody().getError());

        assertEquals(
            "Event 0 not found",
            eventController.updateEvent(0, null).getBody().getError()
        );
    }
}
