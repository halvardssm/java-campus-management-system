package nl.tudelft.oopp.group39.config.exceptions;

import nl.tudelft.oopp.group39.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NotFoundExceptionTest extends AbstractTest {
    @Test
    public void exceptionTest() {
        Assertions.assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException("test", 1L);
        });
    }
}
