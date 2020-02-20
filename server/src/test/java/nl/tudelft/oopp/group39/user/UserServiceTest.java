package nl.tudelft.oopp.group39.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {
    private final User testUser = new User("test", "test@tudelft.nl", "test", null, null);

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
        userService.createUser(testUser);
    }

    @AfterEach
    void tearDown() {
        for (User user : userService.listUsers()) {
            userService.deleteUser(user.getId());
        }
    }

    @Test
    void listUsers() {
        List<User> users = userService.listUsers();

        List<User> testUsers = new ArrayList<>();
        testUsers.add(testUser);
        assertEquals(users, testUsers);
    }

    @Test
    void createUser() {
        User user = new User("test2", "test2@tudelft.nl", "test2", null, null);
        userService.createUser(user);

        User user2 = userService.readUser("test2");
        assertEquals(user, user2);
    }

    @Test
    void readUser() {
        User user2 = userService.readUser("test");
        assertEquals(testUser, user2);
    }

    @Test
    void updateUser() {
        User updatedUser = new User("test", "test@student.tudelft.nl", "test", null, null);
        userService.updateUser("test", updatedUser);

        User user2 = userService.readUser("test");
        assertEquals(updatedUser, user2);
    }

    @Test
    void deleteUser() {
        List<User> testUsers = new ArrayList<>();
        userService.deleteUser("test");
        assertEquals(testUsers, userService.listUsers());
    }
}