package nl.tudelft.oopp.auth.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import nl.tudelft.oopp.auth.entities.AuthRequest;
import nl.tudelft.oopp.role.entities.Role;
import nl.tudelft.oopp.role.enums.Roles;
import nl.tudelft.oopp.role.services.RoleService;
import nl.tudelft.oopp.user.entities.User;
import nl.tudelft.oopp.user.services.UserService;
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
    @Autowired
    private RoleService roleService;

    @Test
    void createToken() throws Exception {
        roleService.createRole(new Role(Roles.STUDENT));
        userService.createUser(new User(
            "test",
            "test@tudelft.nl",
            "test",
            null,
            new Role(Roles.STUDENT)
        ));

        AuthRequest request = new AuthRequest("test", "test");
        Gson gson = new Gson();
        String json = gson.toJson(request);

        mockMvc.perform(post(AuthController.REST_MAPPING)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.body.jwt").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.body.jwt").isNotEmpty());
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
