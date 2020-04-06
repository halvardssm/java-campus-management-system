package nl.tudelft.oopp.group39.room.controllers;

import static nl.tudelft.oopp.group39.room.controllers.RoomController.REST_MAPPING;
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
import java.time.LocalTime;
import nl.tudelft.oopp.group39.AbstractControllerTest;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.config.Constants;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.user.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class RoomControllerTest extends AbstractControllerTest {
    private final Building testBuilding = new Building(
        null,
        "Drebbelweg",
        "Drebbelweg 5",
        "Drebbelweg",
        LocalTime.of(6, 0),
        LocalTime.of(17, 30),
        null, null,
        null
    );
    private final Room testRoom = new Room(
        null,
        "Projectroom 1", "This is another room for testing purposes", 8, true, null, testBuilding,
        null,
        null,
        null
    );

    @BeforeEach
    void setUp() {
        User user = userService.createUser(testUser, true);
        jwt = jwtService.encrypt(testUser);

        Building building = buildingService.createBuilding(testBuilding);
        testBuilding.setId(building.getId());

        testRoom.setBuilding(testBuilding);

        Room room = roomService.createRoom(testRoom);
        testRoom.setId(room.getId());
    }

    @AfterEach
    void tearDown() {
        roomService.deleteRoom(testRoom.getId());
        buildingService.deleteBuilding(testBuilding.getId());
        userService.deleteUser(testUser.getUsername());
    }


    @Test
    void listRoomsTest() throws Exception {
        mockMvc.perform(get(REST_MAPPING))
            .andExpect(jsonPath("$.body").isArray())
            .andExpect(jsonPath("$.body", hasSize(1)))
            .andExpect(jsonPath(
                "$.body[0].building",
                is(testRoom.getBuilding().getId().intValue())
            ))
            .andExpect(jsonPath("$.body[0].name", is(testRoom.getName())))
            .andExpect(jsonPath("$.body[0].capacity", is(testRoom.getCapacity())))
            .andExpect(jsonPath("$.body[0].onlyStaff", is(testRoom.getOnlyStaff())))
            .andExpect(jsonPath("$.body[0].description", is(testRoom.getDescription())));
    }

    @Test
    void deleteAndCreateRoomTest() throws Exception {
        mockMvc.perform(delete(REST_MAPPING + "/" + testRoom.getId())
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").doesNotExist());

        testRoom.setId(null);

        String json = objectMapper.writeValueAsString(testRoom.toDto());

        mockMvc.perform(post(REST_MAPPING)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isCreated())
            .andExpect(jsonPath(
                "$.body.building",
                is(testRoom.getBuilding().getId().intValue())
            ))
            .andExpect(jsonPath("$.body.name", is(testRoom.getName())))
            .andExpect(jsonPath("$.body.capacity", is(testRoom.getCapacity())))
            .andExpect(jsonPath("$.body.onlyStaff", is(testRoom.getOnlyStaff())))
            .andExpect(jsonPath("$.body.description", is(testRoom.getDescription())))
            .andDo((room) -> {
                String responseString = room.getResponse().getContentAsString();
                JsonNode productNode = new ObjectMapper().readTree(responseString);
                testRoom.setId(productNode.get("body").get("id").longValue());
            });
    }

    @Test
    void readRoomTest() throws Exception {
        mockMvc.perform(get(REST_MAPPING + "/" + testRoom.getId()))
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath(
                "$.body.building",
                is(testRoom.getBuilding().getId().intValue())
            ))
            .andExpect(jsonPath("$.body.name", is(testRoom.getName())))
            .andExpect(jsonPath("$.body.capacity", is(testRoom.getCapacity())))
            .andExpect(jsonPath("$.body.onlyStaff", is(testRoom.getOnlyStaff())))
            .andExpect(jsonPath("$.body.description", is(testRoom.getDescription())));
    }


    @Test
    void updateRoomTest() throws Exception {
        testRoom.setName("asdasd");
        String json = objectMapper.writeValueAsString(testRoom.toDto());

        mockMvc.perform(put(REST_MAPPING + "/" + testRoom.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath(
                "$.body.building",
                is(testRoom.getBuilding().getId().intValue())
            ))
            .andExpect(jsonPath("$.body.name", is(testRoom.getName())))
            .andExpect(jsonPath("$.body.capacity", is(testRoom.getCapacity())))
            .andExpect(jsonPath("$.body.onlyStaff", is(testRoom.getOnlyStaff())))
            .andExpect(jsonPath("$.body.description", is(testRoom.getDescription())));

        testRoom.setName("Projectroom 1");
    }

    @Test
    void errorTest() {
        assertEquals(
            "java.lang.NullPointerException",
            roomController.create(null).getBody().getError()
        );

        assertEquals(
            "Room with id '0' wasn't found.",
            roomController.read(0L).getBody().getError()
        );

        assertEquals(
            "The given id must not be null!; nested exception is "
                + "java.lang.IllegalArgumentException: "
                + "The given id must not be null!",
            roomController.update(testRoom.toDto(), null).getBody().getError()
        );
    }
}
