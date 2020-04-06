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
import java.util.HashSet;
import java.util.List;
import nl.tudelft.oopp.group39.AbstractControllerTest;
import nl.tudelft.oopp.group39.config.Constants;
import nl.tudelft.oopp.group39.event.dto.EventDto;
import nl.tudelft.oopp.group39.event.entities.Event;
import nl.tudelft.oopp.group39.room.entities.Room;
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
        testUser,
        null
    );

    @BeforeEach
    void setUp() {
        userService.createUser(testUser, true);
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
            .andExpect(jsonPath("$.body[0]." + Event.COL_TITLE, is(testEvent.getTitle())));
    }

    @Test
    void deleteAndCreateEvent() throws Exception {
        mockMvc.perform(delete(REST_MAPPING + "/" + testEvent.getId())
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").doesNotExist());

        testEvent.setId(null);

        String json = objectMapper.writeValueAsString(testEvent.toDto());

        mockMvc.perform(post(REST_MAPPING)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.body." + Event.COL_TITLE, is(testEvent.getTitle())))
            .andDo((event) -> {
                String responseString = event.getResponse().getContentAsString();
                JsonNode productNode = new ObjectMapper().readTree(responseString);
                testEvent.setId(productNode.get("body").get(Event.COL_ID).longValue());
            });
    }

    @Test
    void readEvent() throws Exception {
        mockMvc.perform(get(REST_MAPPING + "/" + testEvent.getId()))
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body." + Event.COL_TITLE, is(testEvent.getTitle())))
            .andExpect(jsonPath(
                "$.body." + Event.COL_STARTS_AT,
                is(testEvent.getStartsAt().format(Constants.FORMATTER_DATE_TIME))
            ))
            .andExpect(jsonPath(
                "$.body." + Event.COL_ENDS_AT,
                is(testEvent.getEndsAt().format(Constants.FORMATTER_DATE_TIME))
            ))
            .andExpect(jsonPath("$.body." + Event.COL_USER, is(testEvent.getUser().getUsername())))
            .andExpect(jsonPath("$.body." + Event.COL_IS_GLOBAL, is(testEvent.getIsGlobal())));
    }

    @Test
    void updateEvent() throws Exception {
        testEvent.setTitle("test2");
        String json = objectMapper.writeValueAsString(testEvent.toDto());

        mockMvc.perform(put(REST_MAPPING + "/" + testEvent.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body." + Event.COL_TITLE, is(testEvent.getTitle())));

        testEvent.setTitle("test");
    }

    @Test
    void updateEventWithRooms() throws Exception {
        Room room1 = new Room(
            null,
            null, null, null, false, null, null,
            null,
            null,
            null
        );

        Room room = roomService.createRoom(room1);
        room1.setId(room.getId());

        testEvent.setRooms(new HashSet<>(List.of(room1)));
        testEvent.setTitle("test2");
        String json = objectMapper.writeValueAsString(testEvent.toDto());

        mockMvc.perform(put(REST_MAPPING + "/" + testEvent.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body." + Event.COL_TITLE, is(testEvent.getTitle())));

        testEvent.setTitle("test");

        roomService.deleteRoom(room1.getId());
    }

    @Test
    void testError() {
        assertEquals(
            "java.lang.NullPointerException",
            eventController.create(null).getBody().getError()
        );

        assertEquals(
            "Event with id '0' wasn't found.",
            eventController.read(0L).getBody().getError()
        );

        assertEquals(
            "Event with id '0' wasn't found.",
            eventController.update(0L, new EventDto()).getBody().getError()
        );
    }
}
