package nl.tudelft.oopp.group39.config;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.oopp.group39.role.entities.Role;
import nl.tudelft.oopp.group39.role.enums.Roles;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.repositories.UserRepository;
import nl.tudelft.oopp.group39.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Seeds the database on application load.
 */
@Component
public class DbSeeder {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    /**
     * Initiates the db with all the roles.
     */
    public void seedDatabase() {
        System.out.println("[SEED] Seeding started");
        initUsers();
    }

    /**
     * Initiates the database with an admin user with all authorities.
     */
    private void initUsers() {
        List<GrantedAuthority> roles = new ArrayList<>();

        for (Roles role : Roles.values()) {
            roles.add(new Role(role));
        }

        User user = new User(
            "admin",
            "admin@tudelft.nl",
            userService.encryptPassword("pwd"),
            null,
            roles
        );

        userRepository.saveAndFlush(user);
        System.out.println("[SEED] Admin user created");
    }
}
