package nl.tudelft.oopp.role.services;

import java.util.List;
import nl.tudelft.oopp.role.entities.Role;
import nl.tudelft.oopp.role.enums.Roles;
import nl.tudelft.oopp.role.repositories.RoleRepository;
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
        Role role = roleService.readRole(Roles.ADMIN);

        Assertions.assertEquals(Roles.ADMIN.name(), role.getAuthority());
    }

    @Test
    void readRoleString() {
        Role role = roleService.readRole(Roles.ADMIN.name());

        Assertions.assertEquals(Roles.ADMIN.name(), role.getAuthority());
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
