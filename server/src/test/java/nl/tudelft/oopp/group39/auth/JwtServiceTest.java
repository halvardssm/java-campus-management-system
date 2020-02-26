package nl.tudelft.oopp.group39.auth;

import nl.tudelft.oopp.group39.auth.service.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void decryptUsername() {
    }

    @Test
    void decryptExpiration() {
    }

    @Test
    void validate() {
    }

    @Test
    void encrypt() {
    }

    @Test
    void decrypt() {
    }
}