package nl.tudelft.oopp.group39.reservable.controllers;

import static nl.tudelft.oopp.group39.reservable.controllers.FoodController.REST_MAPPING;
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
import nl.tudelft.oopp.group39.reservable.dto.FoodDto;
import nl.tudelft.oopp.group39.reservable.entities.Food;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;
import nl.tudelft.oopp.group39.user.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class FoodControllerTest extends AbstractControllerTest {
    private final Food testFood = new Food(null, "Food", "Piece of yummy food", 5.6, null, null);

    @BeforeEach
    void setUp() {
        User user = userService.createUser(testUser, true);
        jwt = jwtService.encrypt(testUser);

        Food food = foodService.createFood(testFood);
        testFood.setId(food.getId());
    }

    @AfterEach
    void tearDown() {
        foodService.deleteFood(testFood.getId());
        userService.deleteUser(testUser.getUsername());
    }

    @Test
    void listFoods() throws Exception {
        mockMvc.perform(get(REST_MAPPING))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isArray())
            .andExpect(jsonPath("$.body", hasSize(1)))
            .andExpect(jsonPath("$.body[0]." + Food.COL_ID, is(testFood.getId().intValue())))
            .andExpect(jsonPath("$.body[0]." + Food.COL_NAME, is(testFood.getName())))
            .andExpect(jsonPath("$.body[0]." + Food.COL_DESCRIPTION, is(testFood.getDescription())))
            .andExpect(jsonPath("$.body[0]." + Food.COL_PRICE, is(testFood.getPrice())));
    }

    @Test
    void deleteAndCreateFood() throws Exception {
        mockMvc.perform(delete(REST_MAPPING + "/" + testFood.getId())
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").doesNotExist());

        String json = objectMapper.writeValueAsString(testFood.toDto());

        mockMvc.perform(post(REST_MAPPING)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body." + Food.COL_ID).isNumber())
            .andExpect(jsonPath("$.body." + Food.COL_NAME, is(testFood.getName())))
            .andExpect(jsonPath("$.body." + Food.COL_DESCRIPTION, is(testFood.getDescription())))
            .andExpect(jsonPath("$.body." + Food.COL_PRICE, is(testFood.getPrice())))
            .andDo((food) -> {
                String responseString = food.getResponse().getContentAsString();
                JsonNode productNode = new ObjectMapper().readTree(responseString);
                testFood.setId(productNode.get("body").get("id").longValue());
            });
    }

    @Test
    void readFood() throws Exception {
        mockMvc.perform(get(REST_MAPPING + "/" + testFood.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body." + Reservation.COL_ID, is(testFood.getId().intValue())))
            .andExpect(jsonPath("$.body." + Food.COL_NAME, is(testFood.getName())))
            .andExpect(jsonPath("$.body." + Food.COL_DESCRIPTION, is(testFood.getDescription())))
            .andExpect(jsonPath("$.body." + Food.COL_PRICE, is(testFood.getPrice())));
    }

    @Test
    void updateFood() throws Exception {
        testFood.setPrice(6.7);
        testFood.setName("Other name");
        testFood.setDescription("Other description");

        String json = objectMapper.writeValueAsString(testFood.toDto());

        mockMvc.perform(put(REST_MAPPING + "/" + testFood.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath("$.body." + Food.COL_NAME, is(testFood.getName())))
            .andExpect(jsonPath("$.body." + Food.COL_DESCRIPTION, is(testFood.getDescription())))
            .andExpect(jsonPath("$.body." + Food.COL_PRICE, is(testFood.getPrice())));
    }

    @Test
    void testError() {
        assertEquals(
            "java.lang.NullPointerException",
            foodController.create(null).getBody().getError()
        );

        assertEquals(
            "Food with id '0' wasn't found.",
            foodController.read(0L).getBody().getError()
        );

        assertEquals(
            "Food with id '0' wasn't found.",
            foodController.update(0L, new FoodDto()).getBody().getError()
        );
    }
}