package nl.tudelft.oopp.group39.auth.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BadAuthExceptionTest {

    @Test
    public void exceptionTest() {
        Assertions.assertThrows(BadAuthException.class, () -> {
            throw new BadAuthException();
        });
    }
}