package nl.tudelft.oopp.group39;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.oopp.group39.user.User;
import nl.tudelft.oopp.group39.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    @Autowired
    private UserService userService;

    @Test
    public void saveAndRetrieveUser() {
        User user = new User("test", "test@tudelft.nl", "test");
        userService.createUser(user);

        User user2 = userService.readUser("test");
        assertEquals(user, user2);
    }
}