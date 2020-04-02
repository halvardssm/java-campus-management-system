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
import java.time.LocalDateTime;
import java.time.ZoneId;
import nl.tudelft.oopp.group39.AbstractControllerTest;
import nl.tudelft.oopp.group39.config.Constants;
import nl.tudelft.oopp.group39.event.dto.EventDto;
import nl.tudelft.oopp.group39.event.entities.Event;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class EventControllerTest extends AbstractControllerTest {
    private final Event testEvent = new Event(
        null, "test",
        LocalDateTime.now(ZoneId.of(Constants.DEFAULT_TIMEZONE)),
        LocalDateTime.now(ZoneId.of(Constants.DEFAULT_TIMEZONE)).plusDays(1),
        false,
        null,
        null
    );
    private final User testUser = new User(
        "test",
        "test@tudelft.nl",
        "test",
        null,
        Role.ADMIN
    );
    private String jwt;

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
            .andExpect(jsonPath("$.body[0].startDate", is(testEvent.getStartsAt().toString())))
            .andExpect(jsonPath("$.body[0].endDate", is(testEvent.getEndsAt().toString())));
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
            .andExpect(jsonPath("$.body.startDate", is(testEvent.getStartsAt().toString())))
            .andExpect(jsonPath("$.body.endDate", is(testEvent.getEndsAt().toString())))
            .andDo((event) -> {
                String responseString = event.getResponse().getContentAsString();
                JsonNode productNode = new ObjectMapper().readTree(responseString);
                testEvent.setId(productNode.get("body").get("id").longValue());
            });
    }

    @Test
    void readEvent() throws Exception {
        mockMvc.perform(get(REST_MAPPING + "/" + testEvent.getId()))
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body." + Event.COL_TITLE, is(testEvent.getTitle())))
            .andExpect(jsonPath(
                "$.body." + Event.COL_START_DATE,
                is(testEvent.getStartsAt().toString())
            ))
            .andExpect(jsonPath(
                "$.body." + Event.COL_END_DATE,
                is(testEvent.getEndsAt().toString())
            ))
            .andExpect(jsonPath("$.body." + Event.COL_USER, is(testEvent.getUser().getUsername())))
            .andExpect(jsonPath("$.body." + Event.COL_IS_GLOBAL, is(testEvent.getGlobal())));
    }

    @Test
    void updateEvent() throws Exception {
        testEvent.setTitle("test2");
        String json = objectMapper.writeValueAsString(testEvent);

        mockMvc.perform(put(REST_MAPPING + "/" + testEvent.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body.startDate", is(testEvent.getStartsAt().toString())))
            .andExpect(jsonPath("$.body.endDate", is(testEvent.getEndsAt().toString())));

        testEvent.setTitle("test");
    }

    @Test
    void testError() {
        assertEquals(
            "java.lang.NullPointerException",
            eventController.createEvent(null).getBody().getError()
        );

        assertEquals("Event 0 not found", eventController.readEvent(0L).getBody().getError());

        assertEquals(
            "Event 0 not found",
            eventController.updateEvent(0L, new EventDto()).getBody().getError()
        );
    }
}
