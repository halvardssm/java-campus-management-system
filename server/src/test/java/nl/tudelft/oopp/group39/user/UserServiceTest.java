package nl.tudelft.oopp.group39.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void listUsers() {
        User user = new User("test", "test@tudelft.nl", "test", null, null);
        userService.createUser(user);

        User user2 = userService.readUser("test");
        assertEquals(user, user2);
    }

    @Test
    void createAndReadUser() {
        User user = new User("test", "test@tudelft.nl", "test", null, null);
        userService.createUser(user);

        User user2 = userService.readUser("test");
        assertEquals(user, user2);
    }

    @Test
    void updateUser() {
        User user = new User("test", "test@tudelft.nl", "test", null, null);
        userService.createUser(user);

        User user2 = userService.readUser("test");
        assertEquals(user, user2);
    }

    @Test
    void deleteUser() {
    }
}