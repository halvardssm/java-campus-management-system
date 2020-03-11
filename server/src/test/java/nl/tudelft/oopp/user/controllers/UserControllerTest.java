package nl.tudelft.oopp.user.controllers;

import static nl.tudelft.oopp.user.controllers.UserController.REST_MAPPING;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import nl.tudelft.oopp.auth.services.JwtService;
import nl.tudelft.oopp.role.entities.Role;
import nl.tudelft.oopp.user.entities.User;
import nl.tudelft.oopp.user.repositories.UserRepository;
import nl.tudelft.oopp.user.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    private final User testUser = new User(
        "test",
        "test@tudelft.nl",
        "test",
        null,
        Role.ADMIN
    );
    private final Gson gson = new Gson();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userService.createUser(testUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void createUser() throws Exception {
        User user = testUser;
        user.setUsername("test2");
        String json = gson.toJson(user);

        mockMvc.perform(post(REST_MAPPING)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.body.username", is(user.getUsername())))
            .andExpect(jsonPath("$.body.email", is(user.getEmail())))
            .andExpect(jsonPath("$.body.password").exists());
    }

    @Test
    void listUsers() throws Exception {
        String jwt = jwtService.encrypt(testUser);

        ResultActions resultActions = mockMvc.perform(get(REST_MAPPING)
            .header(HttpHeaders.AUTHORIZATION, JwtService.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isArray())
            .andExpect(jsonPath("$.body", hasSize(1)))
            .andExpect(jsonPath("$.body[0].username", is(testUser.getUsername())))
            .andExpect(jsonPath("$.body[0].email", is(testUser.getEmail())));
    }

    @Test
    void readUser() throws Exception {
        String jwt = jwtService.encrypt(testUser);

        mockMvc.perform(get(REST_MAPPING + "/"
            + testUser.getUsername())
            .header(HttpHeaders.AUTHORIZATION, JwtService.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body.username", is(testUser.getUsername())))
            .andExpect(jsonPath("$.body.email", is(testUser.getEmail())))
            .andExpect(jsonPath("$.body.password").exists());
    }

    @Test
    void updateUser() throws Exception {
        String jwt = jwtService.encrypt(testUser);

        User user = testUser;
        user.setEmail("test@student.tudelft.nl");
        String json = gson.toJson(user);

        mockMvc.perform(put(REST_MAPPING + "/"
            + testUser.getUsername())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, JwtService.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body.username", is(user.getUsername())))
            .andExpect(jsonPath("$.body.email", is(user.getEmail())));
    }

    @Test
    void deleteUser() throws Exception {
        String jwt = jwtService.encrypt(testUser);

        mockMvc.perform(delete(REST_MAPPING + "/"
            + testUser.getUsername())
            .header(HttpHeaders.AUTHORIZATION, JwtService.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").doesNotExist());
    }
}