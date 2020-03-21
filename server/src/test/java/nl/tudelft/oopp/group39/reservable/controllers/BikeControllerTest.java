package nl.tudelft.oopp.group39.reservable.controllers;

import static nl.tudelft.oopp.group39.reservable.controllers.BikeController.REST_MAPPING;
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
import nl.tudelft.oopp.group39.CoreControllerTest;
import nl.tudelft.oopp.group39.config.Constants;
import nl.tudelft.oopp.group39.reservable.entities.Bike;
import nl.tudelft.oopp.group39.reservable.enums.BikeType;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class BikeControllerTest extends CoreControllerTest {
    private final User testUser = new User(
        "test",
        "test@tudelft.nl",
        "test",
        null,
        Role.ADMIN,
        null,
        null
    );
    private final Bike testBike = new Bike(BikeType.CITY, 5.6, null, null);
    private String jwt;

    @BeforeEach
    void setUp() {
        User user = userService.createUser(testUser);
        jwt = jwtService.encrypt(testUser);

        Bike bike = bikeService.createBike(testBike);
        testBike.setId(bike.getId());
    }

    @AfterEach
    void tearDown() {
        bikeService.deleteBike(testBike.getId());
        userService.deleteUser(testUser.getUsername());
    }

    @Test
    void listBikes() throws Exception {
        mockMvc.perform(get(REST_MAPPING))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isArray())
            .andExpect(jsonPath("$.body", hasSize(1)))
            .andExpect(jsonPath("$.body[0]." + Bike.COL_ID, is(testBike.getId())))
            .andExpect(jsonPath(
                "$.body[0]." + Bike.COL_BIKE_TYPE,
                is(testBike.getBikeType().name())
            ))
            .andExpect(jsonPath("$.body[0]." + Bike.COL_PRICE, is(testBike.getPrice())));
    }

    @Test
    void deleteAndCreateBike() throws Exception {
        mockMvc.perform(delete(REST_MAPPING + "/" + testBike.getId())
                            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").doesNotExist());

        String json = objectMapper.writeValueAsString(testBike);

        mockMvc.perform(post(REST_MAPPING)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body." + Bike.COL_ID).isNumber())
            .andExpect(jsonPath("$.body." + Bike.COL_BIKE_TYPE, is(testBike.getBikeType().name())))
            .andExpect(jsonPath("$.body." + Bike.COL_PRICE, is(testBike.getPrice())))
            .andDo((bike) -> {
                String responseString = bike.getResponse().getContentAsString();
                JsonNode productNode = new ObjectMapper().readTree(responseString);
                testBike.setId(productNode.get("body").get("id").intValue());
            });
    }

    @Test
    void readBike() throws Exception {
        mockMvc.perform(get(REST_MAPPING + "/" + testBike.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body." + Reservation.COL_ID, is(testBike.getId())))
            .andExpect(jsonPath("$.body." + Bike.COL_BIKE_TYPE, is(testBike.getBikeType().name())))
            .andExpect(jsonPath("$.body." + Bike.COL_PRICE, is(testBike.getPrice())));
    }

    @Test
    void updateBike() throws Exception {
        testBike.setBikeType(BikeType.ELECTRIC);
        testBike.setPrice(6.7);

        String json = objectMapper.writeValueAsString(testBike);

        mockMvc.perform(put(REST_MAPPING + "/" + testBike.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body." + Bike.COL_BIKE_TYPE, is(testBike.getBikeType().name())))
            .andExpect(jsonPath("$.body." + Bike.COL_PRICE, is(testBike.getPrice())));
    }

    @Test
    void testError() {
        assertEquals(
            "Target object must not be null; nested exception is java.lang"
                + ".IllegalArgumentException: Target object must not be null",
            bikeController.createBike(null).getBody().getError()
        );

        assertEquals(
            "Bike 0 not found",
            bikeController.readBike(0).getBody().getError()
        );

        assertEquals(
            "Bike 0 not found",
            bikeController.updateBike(0, null).getBody().getError()
        );
    }
}