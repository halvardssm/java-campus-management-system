package nl.tudelft.oopp.role.entities;

import nl.tudelft.oopp.role.enums.Roles;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoleTest {

    @Test
    void constructWithIllegalRole() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Role("blah");
        });
    }

    @Test
    void constructorWithString() {
        Role testRole = new Role(Roles.ADMIN.name());
        Assertions.assertNotNull(testRole);
    }

    @Test
    void constructorWithRole() {
        Role testRole = new Role(Roles.ADMIN);
        Assertions.assertNotNull(testRole);
    }

    @Test
    void setAndGetId() {
        Role testRole = new Role(Roles.ADMIN);

        Assertions.assertNull(testRole.getId());
        testRole.setId((long) 1);
        Assertions.assertEquals(1, testRole.getId());
    }

    @Test
    void setAndGetAuthority() {
        Role testRole = new Role(Roles.ADMIN);
        Assertions.assertEquals(Roles.ADMIN.name(), testRole.getAuthority());

        testRole.setAuthority(Roles.STUDENT);
        Assertions.assertEquals(Roles.STUDENT.name(), testRole.getAuthority());
    }

    @Test
    void getAuthorityAsRoles() {
        Role testRole = new Role(Roles.ADMIN);
        Assertions.assertEquals(Roles.ADMIN, testRole.getAuthorityAsRoles());
    }

    @Test
    void equalsTrue() {
        Role testRole = new Role(Roles.ADMIN);
        testRole.setId((long) 1);
        Role testRole2 = new Role(Roles.ADMIN);
        testRole2.setId((long) 1);

        Assertions.assertEquals(testRole, testRole2);
    }

    @Test
    void equalsTrueClone() {
        Role testRole = new Role(Roles.ADMIN);
        testRole.setId((long) 1);

        Assertions.assertEquals(testRole, testRole);
    }

    @Test
    void equalsFalse() {
        Role testRole = new Role(Roles.ADMIN);
        testRole.setId((long) 1);

        Assertions.assertNotEquals(testRole, testRole.getAuthority());
    }
}