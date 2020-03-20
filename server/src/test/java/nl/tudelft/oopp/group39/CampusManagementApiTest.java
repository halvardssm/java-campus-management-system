package nl.tudelft.oopp.group39;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class CampusManagementApiTest {

    @Test
    public void contextLoads() {
    }

    @Test
    public void main() {
        CampusManagementApi.main(new String[] {});
    }
}
