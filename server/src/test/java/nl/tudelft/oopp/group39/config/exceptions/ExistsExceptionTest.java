package nl.tudelft.oopp.group39.config.exceptions;

import nl.tudelft.oopp.group39.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExistsExceptionTest extends AbstractTest {
    @Test
    public void exceptionTest() {
        Assertions.assertThrows(ExistsException.class, () -> {
            throw new ExistsException("test", 1L);
        });
    }
}
