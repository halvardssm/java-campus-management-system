package nl.tudelft.oopp.group39.reservation.exceptions;

import nl.tudelft.oopp.group39.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CreationExceptionTest extends AbstractTest {

    @Test
    public void exceptionTest() {
        Assertions.assertThrows(CreationException.class, () -> {
            throw new CreationException();
        });
    }
}