package nl.tudelft.oopp.group39.booking.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class BookingExistsExceptionTest {
    @Test
    public void exceptionTest() {
        Assertions.assertThrows(BookingExistsException.class, () -> {
            throw new BookingExistsException(1);
        });
    }
}
