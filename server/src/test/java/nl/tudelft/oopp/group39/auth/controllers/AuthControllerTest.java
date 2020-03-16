package nl.tudelft.oopp.group39.auth.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import java.util.HashSet;
import nl.tudelft.oopp.group39.auth.entities.AuthRequest;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import nl.tudelft.oopp.group39.user.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;

    @Test
    void createToken() throws Exception {
        userService.createUser(new User(
            "test",
            "test@tudelft.nl",
            "test",
            null,
            Role.STUDENT,
            new HashSet<>(),
            new HashSet<>()
        ));

        AuthRequest request = new AuthRequest("test", "test");
        Gson gson = new Gson();
        String json = gson.toJson(request);

        mockMvc.perform(post(AuthController.REST_MAPPING)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.body.token").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.body.token").isNotEmpty());
    }

    @Test
    void createTokenFailed() throws Exception {
        AuthRequest request = new AuthRequest("test2", "test");
        Gson gson = new Gson();
        String json = gson.toJson(request);

        mockMvc.perform(post(AuthController.REST_MAPPING)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isUnauthorized())
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.error")
                .value("Wrong username or password"));
    }
}
