package nl.tudelft.oopp.group39.user.exceptions;

import nl.tudelft.oopp.group39.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserExistsExceptionTest extends AbstractTest {

    @Test
    public void exceptionTest() {
        Assertions.assertThrows(UserExistsException.class, () -> {
            throw new UserExistsException("1");
        });
    }
}
