package nl.tudelft.oopp.group39.user.controllers;

import static nl.tudelft.oopp.group39.user.controllers.UserController.REST_MAPPING;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.tudelft.oopp.group39.CoreTest;
import nl.tudelft.oopp.group39.config.Constants;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@AutoConfigureMockMvc
class UserControllerTest extends CoreTest {
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

    @BeforeEach
    void setUp() {
        userService.createUser(testUser);
        jwt = jwtService.encrypt(testUser);
        testUser.setPassword("test");
    }

    @AfterEach
    void tearDown() {
        userService.deleteUser(testUser.getUsername());
        testUser.setPassword("test");
    }

    @Test
    void deleteAndCreateUser() throws Exception {
        mockMvc.perform(delete(REST_MAPPING + "/"
                                   + testUser.getUsername())
                            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").doesNotExist());

        String json = "{\"username\":\"test\",\"email\":\"test@tudelft.nl\",\"password\":\"test\"}";

        mockMvc.perform(post(REST_MAPPING)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.body.username", is(testUser.getUsername())))
            .andExpect(jsonPath("$.body.email", is(testUser.getEmail())));
    }

    @Test
    void listUsers() throws Exception {
        mockMvc.perform(get(REST_MAPPING)
                            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isArray())
            .andExpect(jsonPath("$.body", hasSize(1)))
            .andExpect(jsonPath("$.body[0].username", is(testUser.getUsername())))
            .andExpect(jsonPath("$.body[0].email", is(testUser.getEmail())));
    }

    @Test
    void readUser() throws Exception {
        mockMvc.perform(get(REST_MAPPING + "/"
                                + testUser.getUsername())
                            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body.username", is(testUser.getUsername())))
            .andExpect(jsonPath("$.body.email", is(testUser.getEmail())));
    }

    @Test
    void updateUser() throws Exception {
        User user = testUser;
        user.setEmail("test@student.tudelft.nl");
        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(put(REST_MAPPING + "/"
                                + testUser.getUsername())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body.username", is(user.getUsername())))
            .andExpect(jsonPath("$.body.email", is(user.getEmail())));
    }

    @Test
    void testError() {
        assertEquals("User can not be null", userController.createUser(null).getBody().getError());

        assertEquals("User asdf not found", userController.readUser("asdf").getBody().getError());

        assertEquals("User asdf not found", userController.updateUser("asdf", null)
            .getBody().getError());
    }
}