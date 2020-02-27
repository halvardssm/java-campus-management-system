package nl.tudelft.oopp.group39.role.services;

import java.util.List;
import nl.tudelft.oopp.group39.role.entities.Role;
import nl.tudelft.oopp.group39.role.enums.Roles;
import nl.tudelft.oopp.group39.role.repositories.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoleServiceTest {

    @Autowired
    RoleService roleService;

    @Autowired
    RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleRepository.deleteAll();

        for (Roles role : Roles.values()) {
            roleRepository.save(new Role(role));
        }

        roleRepository.flush();
    }

    @Test
    void readRole() {
        Role role = roleService.readRole(Roles.ROLE_ADMIN);

        Assertions.assertEquals(Roles.ROLE_ADMIN.name(), role.getAuthority());
    }

    @Test
    void readRoleString() {
        Role role = roleService.readRole(Roles.ROLE_ADMIN.name());

        Assertions.assertEquals(Roles.ROLE_ADMIN.name(), role.getAuthority());
    }

    @Test
    void listRoles() {
        List<Role> roles = roleService.listRoles();

        Assertions.assertEquals(Roles.values().length, roles.size());

        for (Role role : roles) {
            Enum.valueOf(Roles.class, role.getAuthority());
        }
    }
}