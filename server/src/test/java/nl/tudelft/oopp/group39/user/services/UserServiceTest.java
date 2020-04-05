package nl.tudelft.oopp.group39.user.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.oopp.group39.AbstractTest;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest extends AbstractTest {
    private final User testUser = new User(
        "test",
        "test@tudelft.nl",
        "test",
        null,
        Role.STUDENT
    );

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
        User user = userService.readUser("test");

        assertEquals(testUser, user);
    }

    @Test
    void updateUser() {
        testUser.setEmail("test@tudelft.nl");
        User user = userService.updateUser("test", testUser);

        assertEquals(testUser, user);
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
