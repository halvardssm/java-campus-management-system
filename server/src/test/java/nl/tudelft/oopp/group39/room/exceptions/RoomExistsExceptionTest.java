package nl.tudelft.oopp.group39.room.exceptions;

import nl.tudelft.oopp.group39.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RoomExistsExceptionTest extends AbstractTest {
    @Test
    public void exceptionTest() {
        Assertions.assertThrows(RoomExistsException.class, () -> {
            throw new RoomExistsException(1L);
        });
    }
}
