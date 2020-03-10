package nl.tudelft.oopp.user.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.oopp.role.entities.Role;
import nl.tudelft.oopp.role.enums.Roles;
import nl.tudelft.oopp.role.repositories.RoleRepository;
import nl.tudelft.oopp.role.services.RoleService;
import nl.tudelft.oopp.user.entities.User;
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
        new Role(Roles.STUDENT)
    );

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    RoleService roleService;
    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
        for (User user : userService.listUsers()) {
            userService.deleteUser(user.getUsername());
        }

        roleRepository.deleteAll();

        roleService.createRole(new Role(Roles.STUDENT));

        userService.createUser(testUser);
    }

    @Test
    void listUsers() {
        List<User> users = userService.listUsers();

        assertEquals(1, users.size());
        assertEquals(testUser, users.get(0));
    }

    @Test
    void createUser() {
        User user = testUser;
        user.setUsername("user2");

        User user2 = userService.createUser(user);

        assertEquals(user, user2);
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
    void deleteUser() {
        List<User> testUsers = new ArrayList<>();
        userService.deleteUser("test");
        assertEquals(testUsers, userService.listUsers());
    }
}
