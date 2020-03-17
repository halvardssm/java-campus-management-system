package nl.tudelft.oopp.group39.user.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {
    private final User testUser = new User(
        "test",
        "test@tudelft.nl",
        "test",
        null,
        Role.STUDENT,
        null
    );

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
        userService.createUser(testUser);
    }

    @AfterEach
    void tearDown() {
        userService.deleteUser(testUser.getUsername());
        testUser.setPassword("test");
    }

    @Test
    void listUsers() {
        List<User> users = userService.listUsers();

        assertEquals(1, users.size());
        assertEquals(testUser, users.get(0));
    }

    @Test
    void deleteAndCreateUser() {
        userService.deleteUser(testUser.getUsername());

        assertEquals(new ArrayList<>(), userService.listUsers());

        User user = userService.createUser(testUser);

        assertEquals(testUser, user);
    }

    @Test
    void readUser() {
        User user2 = userService.readUser("test");

        assertEquals(testUser, user2);
    }

    @Test
    void updateUser() {
        User user = testUser;
        user.setEmail("test@tudelft.nl");
        User user2 = userService.updateUser("test", user);

        assertEquals(user, user2);
    }

    @Test
    void mapRoleForUser() {
        User user = testUser;
        user.setRole(null);
        userService.mapRoleForUser(user);

        assertEquals(testUser, user);
        assertEquals(user.getRole(), Role.STUDENT);
    }
}
