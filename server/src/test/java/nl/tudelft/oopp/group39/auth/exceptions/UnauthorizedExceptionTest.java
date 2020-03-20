package nl.tudelft.oopp.group39.auth.exceptions;

import nl.tudelft.oopp.group39.CoreTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UnauthorizedExceptionTest extends CoreTest {

    @Test
    public void exceptionTest() {
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            throw new UnauthorizedException();
        });
    }
}
