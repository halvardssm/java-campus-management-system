package nl.tudelft.oopp.group39.facilities.exceptions;

import nl.tudelft.oopp.group39.AbstractTest;
import nl.tudelft.oopp.group39.facility.exceptions.FacilityExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FacilityExistsExceptionTest extends AbstractTest {
    @Test
    public void exceptionTest() {
        Assertions.assertThrows(FacilityExistsException.class, () -> {
            throw new FacilityExistsException(1L);
        });
    }
}
