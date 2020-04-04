package nl.tudelft.oopp.group39.user;

import static org.junit.jupiter.api.Assertions.*;

import nl.tudelft.oopp.group39.user.model.User;
import org.junit.jupiter.api.Test;

class UserTest {

    public static User testUser = new User("kittyamazing", "j.lig@ma.stud.nl", "secret", null, "STUDENT");

    @Test
    void getUsername() {
        assertEquals(testUser.getUsername(),"kittyamazing");
    }

    @Test
    void getEmail() {
        assertEquals(testUser.getEmail(), "j.lig@ma.stud.nl");
    }

    @Test
    void getPassword() {
        assertEquals(testUser.getPassword(), "secret");
    }

    @Test
    void getImage() {
        assertNull(testUser.getImage());
    }

    @Test
    void getRole() {
        assertEquals(testUser.getRole(), "STUDENT");
    }
}