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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import nl.tudelft.oopp.group39.auth.services.JwtService;
import nl.tudelft.oopp.group39.config.Constants;
import nl.tudelft.oopp.group39.event.entities.Event;
import nl.tudelft.oopp.group39.event.enums.EventTypes;
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

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Event testEvent = new Event(
        EventTypes.EVENT,
        LocalDate.now(ZoneId.of("Europe/Paris")),
        LocalDate.now(ZoneId.of("Europe/Paris")).plusDays(1),
        null
    );
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


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private EventController eventController;

    @BeforeEach
    void setUp() {
        userService.createUser(testUser);
        jwt = jwtService.encrypt(testUser);

        Event event = eventService.createEvent(testEvent);
        testEvent.setId(event.getId());
    }

    @AfterEach
    void tearDown() {
        eventService.deleteEvent(testEvent.getId());
        userService.deleteUser(testUser.getUsername());
    }

    @Test
    void listEvents() throws Exception {
        mockMvc.perform(get(REST_MAPPING))
            .andExpect(jsonPath("$.body").isArray())
            .andExpect(jsonPath("$.body", hasSize(1)))
            .andExpect(jsonPath("$.body[0].type", is(EventTypes.EVENT.name())))
            .andExpect(jsonPath("$.body[0].startDate", is(testEvent.getStartDate().toString())))
            .andExpect(jsonPath("$.body[0].endDate", is(testEvent.getEndDate().toString())))
            .andExpect(jsonPath("$.body[0].rooms").isArray())
            .andExpect(jsonPath("$.body[0].rooms", hasSize(0)));
    }

    @Test
    void deleteAndCreateEvent() throws Exception {
        mockMvc.perform(delete(REST_MAPPING + "/" + testEvent.getId())
                            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").doesNotExist());

        testEvent.setId(null);

        String json = objectMapper.writeValueAsString(testEvent);

        mockMvc.perform(post(REST_MAPPING)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.body.type", is(EventTypes.EVENT.name())))
            .andExpect(jsonPath("$.body.startDate", is(testEvent.getStartDate().toString())))
            .andExpect(jsonPath("$.body.endDate", is(testEvent.getEndDate().toString())))
            .andExpect(jsonPath("$.body.rooms").isArray())
            .andExpect(jsonPath("$.body.rooms", hasSize(0)))
            .andDo((event) -> {
                String responseString = event.getResponse().getContentAsString();
                JsonNode productNode = new ObjectMapper().readTree(responseString);
                testEvent.setId(productNode.get("body").get("id").intValue());
            });
    }

    @Test
    void readEvent() throws Exception {
        mockMvc.perform(get(REST_MAPPING + "/" + testEvent.getId()))
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body.type", is(EventTypes.EVENT.name())))
            .andExpect(jsonPath("$.body.startDate", is(testEvent.getStartDate().toString())))
            .andExpect(jsonPath("$.body.endDate", is(testEvent.getEndDate().toString())))
            .andExpect(jsonPath("$.body.rooms").isArray())
            .andExpect(jsonPath("$.body.rooms", hasSize(0)));
    }

    @Test
    void updateEvent() throws Exception {
        testEvent.setType(EventTypes.HOLIDAY);
        String json = objectMapper.writeValueAsString(testEvent);

        mockMvc.perform(put(REST_MAPPING + "/" + testEvent.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body.type", is(EventTypes.HOLIDAY.name())))
            .andExpect(jsonPath("$.body.startDate", is(testEvent.getStartDate().toString())))
            .andExpect(jsonPath("$.body.endDate", is(testEvent.getEndDate().toString())))
            .andExpect(jsonPath("$.body.rooms").isArray())
            .andExpect(jsonPath("$.body.rooms", hasSize(0)));

        testEvent.setType(EventTypes.EVENT);
    }

    @Test
    void testError() {
        assertEquals(
            "Target object must not be null; nested exception is "
                + "java.lang.IllegalArgumentException: Target object must not be null",
            eventController.createEvent(null).getBody().getError()
        );

        assertEquals("Event 0 not found", eventController.readEvent(0).getBody().getError());

        assertEquals(
            "Event 0 not found",
            eventController.updateEvent(0, null).getBody().getError()
        );
    }
}
