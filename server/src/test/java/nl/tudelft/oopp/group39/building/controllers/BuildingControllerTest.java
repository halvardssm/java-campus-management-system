package nl.tudelft.oopp.group39.building.controllers;

import static nl.tudelft.oopp.group39.building.controllers.BuildingController.REST_MAPPING;
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
import nl.tudelft.oopp.group39.user.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class BuildingControllerTest extends AbstractControllerTest {
    private final Building testBuilding = new Building(
        null,
        "EEMCS",
        "Mekelweg 4",
        "Faculty of Electrical Engineering, Maths and Computer Science",
        LocalTime.of(7, 0, 1),
        LocalTime.of(17, 59, 59),
        null,
        null
    );

    @BeforeEach
    void setUp() {
        User user = userService.createUser(testUser, true);
        jwt = jwtService.encrypt(testUser);

        Building building = buildingService.createBuilding(testBuilding);
        testBuilding.setId(building.getId());
    }

    @AfterEach
    void tearDown() {
        buildingService.deleteBuilding(testBuilding.getId());
        userService.deleteUser(testUser.getUsername());
    }

    @Test
    void listBuildingsTest() throws Exception {
        mockMvc.perform(get(REST_MAPPING))
            .andExpect(jsonPath("$.body").isArray())
            .andExpect(jsonPath("$.body", hasSize(1)))
            .andExpect(jsonPath("$.body[0].name", is(testBuilding.getName())))
            .andExpect(jsonPath("$.body[0].location", is(testBuilding.getLocation())))
            .andExpect(jsonPath("$.body[0].description", is(testBuilding.getDescription())))
            .andExpect(jsonPath("$.body[0].open", is(testBuilding.getOpen().toString())))
            .andExpect(jsonPath("$.body[0].closed", is(testBuilding.getClosed().toString())));
    }

    @Test
    void deleteAndCreateBuildingTest() throws Exception {
        mockMvc.perform(delete(REST_MAPPING + "/" + testBuilding.getId())
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").doesNotExist());

        testBuilding.setId(null);

        String json = objectMapper.writeValueAsString(testBuilding);

        mockMvc.perform(post(REST_MAPPING)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.body.name", is(testBuilding.getName())))
            .andExpect(jsonPath("$.body.location", is(testBuilding.getLocation())))
            .andExpect(jsonPath("$.body.description", is(testBuilding.getDescription())))
            .andExpect(jsonPath("$.body.open", is(testBuilding.getOpen().toString())))
            .andExpect(jsonPath("$.body.closed", is(testBuilding.getClosed().toString())))
            .andDo((building) -> {
                String responseString = building.getResponse().getContentAsString();
                JsonNode productNode = new ObjectMapper().readTree(responseString);
                testBuilding.setId(productNode.get("body").get("id").longValue());
            });
    }

    @Test
    void readBuildingTest() throws Exception {
        mockMvc.perform(get(REST_MAPPING + "/" + testBuilding.getId()))
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body.name", is(testBuilding.getName())))
            .andExpect(jsonPath("$.body.location", is(testBuilding.getLocation())))
            .andExpect(jsonPath("$.body.description", is(testBuilding.getDescription())))
            .andExpect(jsonPath("$.body.open", is(testBuilding.getOpen().toString())))
            .andExpect(jsonPath("$.body.closed", is(testBuilding.getClosed().toString())));
    }

    @Test
    void updateBuildingTest() throws Exception {
        testBuilding.setName("asdasd");
        String json = objectMapper.writeValueAsString(testBuilding);

        mockMvc.perform(put(REST_MAPPING + "/" + testBuilding.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body.name", is(testBuilding.getName())))
            .andExpect(jsonPath("$.body.location", is(testBuilding.getLocation())))
            .andExpect(jsonPath("$.body.description", is(testBuilding.getDescription())))
            .andExpect(jsonPath("$.body.open", is(testBuilding.getOpen().toString())))
            .andExpect(jsonPath("$.body.closed", is(testBuilding.getClosed().toString())));

        testBuilding.setName("EEMCS");
    }

    @Test
    void errorTest() {
        assertEquals(
            "java.lang.NullPointerException",
            buildingController.create(null).getBody().getError()
        );

        assertEquals(
            "Building with id '0' wasn't found.",
            buildingController.read(0L).getBody().getError()
        );

        assertEquals(
            "The given id must not be null!; nested exception is "
                + "java.lang.IllegalArgumentException: "
                + "The given id must not be null!",
            buildingController.update(testBuilding.toDto(), null).getBody().getError()
        );
    }
}
