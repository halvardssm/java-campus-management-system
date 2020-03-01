package nl.tudelft.oopp.group39.user.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserExistsExceptionTest {

    @Test
    public void exceptionTest() {
        Assertions.assertThrows(UserExistsException.class, () -> {
            throw new UserExistsException("1");
        });
    }
}
