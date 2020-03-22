package nl.tudelft.oopp.group39;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public abstract class AbstractControllerTest extends AbstractTest {
    @Autowired
    protected MockMvc mockMvc;
}
