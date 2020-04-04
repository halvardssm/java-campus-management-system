package nl.tudelft.oopp.group39.config.exceptions;

import nl.tudelft.oopp.group39.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UnauthorizedExceptionTest extends AbstractTest {

    @Test
    public void exceptionTest() {
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            throw new UnauthorizedException();
        });
    }
}
