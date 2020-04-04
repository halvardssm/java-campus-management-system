package nl.tudelft.oopp.group39.facilities.controllers;

import static nl.tudelft.oopp.group39.facility.controllers.FacilityController.REST_MAPPING;
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
import nl.tudelft.oopp.group39.AbstractControllerTest;
import nl.tudelft.oopp.group39.config.Constants;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

public class FacilityControllerTest extends AbstractControllerTest {
    private final User testUser = new User(
        "test",
        "test@tudelft.nl",
        "test",
        null,
        Role.ADMIN
    );
    private final Facility testFacility = new Facility(
        "test",
        null
    );
    private String jwt;

    @BeforeEach
    void setUp() {
        User user = userService.createUser(testUser);
        jwt = jwtService.encrypt(testUser);

        Facility facility = facilityService.createFacility(testFacility);
        testFacility.setId(facility.getId());
    }

    @AfterEach
    void tearDown() {
        facilityService.deleteFacility(testFacility.getId());
        userService.deleteUser(testUser.getUsername());
    }

    @Test
    void listFacilitiesTest() throws Exception {
        mockMvc.perform(get(REST_MAPPING))
            .andExpect(jsonPath("$.body").isArray())
            .andExpect(jsonPath("$.body", hasSize(1)))
            .andExpect(jsonPath("$.body[0].description",
                is(testFacility.getDescription())));
    }

    @Test
    void deleteAndCreateFacilityTest() throws Exception {
        mockMvc.perform(delete(REST_MAPPING + "/" + testFacility.getId())
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").doesNotExist());

        testFacility.setId(null);

        String json = objectMapper.writeValueAsString(testFacility);

        mockMvc.perform(post(REST_MAPPING)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.body.description",
                is(testFacility.getDescription())))
            .andDo((facility) -> {
                String responseString = facility.getResponse().getContentAsString();
                JsonNode productNode = new ObjectMapper().readTree(responseString);
                testFacility.setId(productNode.get("body").get("id").longValue());
            });
    }

    @Test
    void readFacilityTest() throws Exception {
        mockMvc.perform(get(REST_MAPPING + "/" + testFacility.getId()))
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body.description",
                is(testFacility.getDescription())));
    }

    @Test
    void updateFacilityTest() throws Exception {
        testFacility.setDescription("asdasd");
        String json = objectMapper.writeValueAsString(testFacility);

        mockMvc.perform(put(REST_MAPPING + "/" + testFacility.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body.description",
                is(testFacility.getDescription())));


        testFacility.setDescription("test");
    }

    @Test
    void errorTest() {
        assertEquals(
            "Target object must not be null; "
                + "nested exception is java.lang.IllegalArgumentException: "
                + "Target object must not be null",
            facilityController.createFacility(null).getBody().getError()
        );

        assertEquals("Facility with id 0 wasn't found.",
            facilityController.readFacility(0L).getBody().getError());

        assertEquals(
            "The given id must not be null!; nested exception is "
                + "java.lang.IllegalArgumentException: "
                + "The given id must not be null!",
            facilityController.updateFacility(testFacility, null).getBody().getError()
        );
    }
}
