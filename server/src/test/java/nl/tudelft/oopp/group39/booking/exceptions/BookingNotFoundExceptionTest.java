package nl.tudelft.oopp.group39.booking.exceptions;

import nl.tudelft.oopp.group39.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BookingNotFoundExceptionTest extends AbstractTest {
    @Test
    public void notFoundTest() {
        Assertions.assertThrows(BookingNotFoundException.class, () -> {
            throw new BookingNotFoundException(1);
        });
    }
}
