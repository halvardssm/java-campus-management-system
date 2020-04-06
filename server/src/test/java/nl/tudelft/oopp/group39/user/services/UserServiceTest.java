package nl.tudelft.oopp.group39.user.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import nl.tudelft.oopp.group39.AbstractTest;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest extends AbstractTest {

    @BeforeEach
    void setUp() {
        userService.createUser(testUserStudent, true);
    }

    @AfterEach
    void tearDown() {
        userService.deleteUser(testUserStudent.getUsername());
        testUserStudent.setPassword("test");
    }

    @Test
    void listUsers() {
        List<User> users = userService.listUsers(new HashMap<>());

        assertEquals(1, users.size());
        assertEquals(testUserStudent, users.get(0));
    }

    @Test
    void deleteAndCreateUser() {
        userService.deleteUser(testUserStudent.getUsername());

        assertEquals(new ArrayList<>(), userService.listUsers(new HashMap<>()));

        User user = userService.createUser(testUserStudent, true);

        assertEquals(testUserStudent, user);
    }

    @Test
    void readUser() {
        User user = userService.readUser("test");

        assertEquals(testUserStudent, user);
    }

    @Test
    void updateUser() {
        testUserStudent.setEmail("test@tudelft.nl");
        User user = userService.updateUser("test", testUserStudent, true);

        assertEquals(testUserStudent, user);
    }

    @Test
    void mapRoleForUser() {
        User user = testUserStudent;
        user.setRole(null);
        userService.mapRoleForUser(user, false);

        assertEquals(testUserStudent, user);
        assertEquals(user.getRole(), Role.STUDENT);
    }
}
