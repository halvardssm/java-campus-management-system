package nl.tudelft.oopp.group39.building.exceptions;

import nl.tudelft.oopp.group39.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BuildingExistsExceptionTest extends AbstractTest {
    @Test
    public void exceptionTest() {
        Assertions.assertThrows(BuildingExistsException.class, () -> {
            throw new BuildingExistsException(1L);
        });
    }
}
