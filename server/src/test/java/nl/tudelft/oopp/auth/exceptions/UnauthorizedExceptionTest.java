package nl.tudelft.oopp.auth.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UnauthorizedExceptionTest {

    @Test
    public void exceptionTest() {
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            throw new UnauthorizedException();
        });
    }
}
