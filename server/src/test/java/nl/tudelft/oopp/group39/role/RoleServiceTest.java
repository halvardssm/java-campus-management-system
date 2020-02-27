package nl.tudelft.oopp.group39.role;

import java.util.List;
import nl.tudelft.oopp.group39.role.entity.Role;
import nl.tudelft.oopp.group39.role.enums.Roles;
import nl.tudelft.oopp.group39.role.service.RoleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoleServiceTest {

    @Autowired
    RoleService roleService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void readRole() {
    }

    @Test
    void testReadRole() {
    }

    @Test
    void listRoles() {
        List<Role> roles = roleService.listRoles();

        for (Role role : roles) {
            System.out.println(role.getRole());
        }

        Assertions.assertEquals(roles.size(), Roles.values().length);
    }
}