package nl.tudelft.oopp.group39.auth.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.oopp.group39.AbstractControllerTest;
import nl.tudelft.oopp.group39.auth.dto.AuthRequestDto;
import nl.tudelft.oopp.group39.auth.exceptions.UnauthorizedException;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class AuthControllerTest extends AbstractControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final User testUser = new User(
        "test",
        "test@tudelft.nl",
        "test",
        null,
        Role.ADMIN
    );

    @BeforeEach
    void setUp() {
        userService.createUser(testUser);
    }

    @AfterEach
    void tearDown() {
        userService.deleteUser(testUser.getUsername());
    }

    @Test
    void createToken() throws Exception {
        AuthRequestDto request = new AuthRequestDto("test", "test");
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(AuthController.REST_MAPPING)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.body.token").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.body.token").isNotEmpty());
    }

    @Test
    void createTokenFailed() throws Exception {
        AuthRequestDto request = new AuthRequestDto("test2", "test");
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(AuthController.REST_MAPPING)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
            .andExpect(status().isUnauthorized())
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.error")
                           .value(UnauthorizedException.UNAUTHORIZED));
    }
}
