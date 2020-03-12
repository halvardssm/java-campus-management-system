package nl.tudelft.oopp.group39.event.controllers;

import static nl.tudelft.oopp.group39.event.controllers.EventController.REST_MAPPING;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import nl.tudelft.oopp.group39.auth.controllers.AuthController;
import nl.tudelft.oopp.group39.auth.services.JwtService;
import nl.tudelft.oopp.group39.event.entities.Event;
import nl.tudelft.oopp.group39.event.enums.EventTypes;
import nl.tudelft.oopp.group39.event.repositories.EventRepository;
import nl.tudelft.oopp.group39.event.services.EventService;
import nl.tudelft.oopp.group39.role.entities.Role;
import nl.tudelft.oopp.group39.role.enums.Roles;
import nl.tudelft.oopp.group39.user.entities.User;
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
    private static final User testUser = new User(
        "test",
        "test@tudelft.nl",
        "test",
        null,
        List.of(new Role(Roles.ADMIN))
    );
    private static final Event testEvent = new Event(
        EventTypes.EVENT,
        LocalDate.now(ZoneId.of("Europe/Paris")),
        LocalDate.now(ZoneId.of("Europe/Paris")).plusDays(1),
        null
    );
    private static final Gson gson = new Gson();
    private static String jwt;


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        eventService.createEvent(testEvent);

        userService.createUser(testUser);
        jwt = jwtService.encrypt(testUser);
    }

    @AfterEach
    void tearDown() {
        eventRepository.deleteAll();
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

        mockMvc.perform(post(EventController.REST_MAPPING)
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
        mockMvc.perform(get(REST_MAPPING + '/'))
            .andExpect(jsonPath("$.body").isArray())
            .andExpect(jsonPath("$.body", hasSize(1)))
            .andExpect(jsonPath("$.body[0].type", is(testEvent.getType().name())))
            .andExpect(jsonPath("$.body[0].startDate", is(testEvent.getStartDate().toString())))
            .andExpect(jsonPath("$.body[0].endDate", is(testEvent.getEndDate().toString())))
            .andExpect(jsonPath("$.body[0].rooms").isArray())
            .andExpect(jsonPath("$.body[0].rooms", hasSize(0)));
    }

    @Test
    void updateEvent() {
    }

    @Test
    void deleteEmployee() {
    }
}