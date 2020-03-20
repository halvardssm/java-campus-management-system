package nl.tudelft.oopp.group39.auth.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class UnauthorizedExceptionTest {

    @Test
    public void exceptionTest() {
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            throw new UnauthorizedException();
        });
    }
}
