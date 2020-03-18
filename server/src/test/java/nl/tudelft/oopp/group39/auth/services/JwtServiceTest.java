package nl.tudelft.oopp.group39.auth.services;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtServiceTest {
    private final User testUser = new User(
        "test",
        "test@tudelft.nl",
        "test",
        null,
        Role.STUDENT,
        null,
        null
    );

    @Autowired
    JwtService jwtService;

    @Test
    void decryptUsername() {
        String jwt = jwtService.encrypt(testUser);

        String username = jwtService.decryptUsername(jwt);

        Assertions.assertEquals(testUser.getUsername(), username);
    }

    @Test
    void decryptExpiration() {
        String jwt = jwtService.encrypt(testUser);

        Date date = jwtService.decryptExpiration(jwt);

        // Gives a 10 seconds buffer due to stupid test time not being able to be mocked
        long dateMin = new Date(System.currentTimeMillis() - 5000).getTime();
        Date dateMinExp = new Date(dateMin + JwtService.TOKEN_EXPIRATION_TIME);
        Date dateMaxExp = new Date(dateMin + JwtService.TOKEN_EXPIRATION_TIME + 10000);

        // Should return -1
        int comparedDateMin = dateMinExp.compareTo(date);
        // Should return 1
        int comparedDateMax = dateMaxExp.compareTo(date);

        Assertions.assertTrue(comparedDateMin < 0 && comparedDateMax > 0);
    }

    @Test
    void encryptAndValidate() {
        Assertions.assertTrue(jwtService.validate(jwtService.encrypt(testUser), testUser));
    }

    @Test
    void validateFalse() {
        Set<Booking> bookings = new HashSet<>();
        User user = new User(
            "test2",
            "test@tudelft.nl",
            "test",
            null,
            Role.STUDENT,
            bookings
        );
        Assertions.assertFalse(jwtService.validate(jwtService.encrypt(testUser), user));
    }
}
