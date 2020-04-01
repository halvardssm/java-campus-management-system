package nl.tudelft.oopp.group39;

import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public abstract class AbstractControllerTest extends AbstractTest {
    protected final User testUser = new User(
        "test",
        "test@tudelft.nl",
        "test",
        null,
        Role.ADMIN,
        null,
        null
    );

    @Autowired
    protected MockMvc mockMvc;
}
