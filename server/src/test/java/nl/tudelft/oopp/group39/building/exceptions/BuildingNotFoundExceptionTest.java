package nl.tudelft.oopp.group39.building.exceptions;

import nl.tudelft.oopp.group39.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BuildingNotFoundExceptionTest extends AbstractTest {
    @Test
    public void exceptionTest() {
        Assertions.assertThrows(BuildingNotFoundException.class, () -> {
            throw new BuildingNotFoundException(1L);
        });
    }
}
