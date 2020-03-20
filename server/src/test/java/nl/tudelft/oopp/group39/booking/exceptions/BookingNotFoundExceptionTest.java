package nl.tudelft.oopp.group39.booking.exceptions;

import nl.tudelft.oopp.group39.CoreTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BookingNotFoundExceptionTest extends CoreTest {
    @Test
    public void notFoundTest() {
        Assertions.assertThrows(BookingNotFoundException.class, () -> {
            throw new BookingNotFoundException(1);
        });
    }
}
