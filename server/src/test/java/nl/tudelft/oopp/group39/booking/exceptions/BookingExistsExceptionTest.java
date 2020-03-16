package nl.tudelft.oopp.group39.booking.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BookingExistsExceptionTest {
    @Test
    public void exceptionTest() {
        Assertions.assertThrows(BookingExistsException.class, () -> {
            throw new BookingExistsException(1);
        });
    }
}
