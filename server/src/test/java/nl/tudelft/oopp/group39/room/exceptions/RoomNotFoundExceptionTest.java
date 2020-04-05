package nl.tudelft.oopp.group39.room.exceptions;

import nl.tudelft.oopp.group39.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RoomNotFoundExceptionTest extends AbstractTest {
    @Test
    public void notFoundTest() {
        Assertions.assertThrows(RoomNotFoundException.class, () -> {
            throw new RoomNotFoundException(1L);
        });
    }
}
