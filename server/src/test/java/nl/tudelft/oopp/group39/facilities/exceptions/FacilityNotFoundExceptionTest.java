package nl.tudelft.oopp.group39.facilities.exceptions;

import nl.tudelft.oopp.group39.AbstractTest;
import nl.tudelft.oopp.group39.facility.exceptions.FacilityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FacilityNotFoundExceptionTest extends AbstractTest {
    @Test
    public void exceptionTest() {
        Assertions.assertThrows(FacilityNotFoundException.class, () -> {
            throw new FacilityNotFoundException(1L);
        });
    }
}
