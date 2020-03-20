package nl.tudelft.oopp.group39.booking.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class BookingNotFoundExceptionTest {
    @Test
    public void notFoundTest() {
        Assertions.assertThrows(BookingNotFoundException.class, () -> {
            throw new BookingNotFoundException(1);
        });
    }
}
