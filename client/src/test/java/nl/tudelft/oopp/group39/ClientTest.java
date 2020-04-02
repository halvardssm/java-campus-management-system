package nl.tudelft.oopp.group39;

import nl.tudelft.oopp.group39.server.views.UsersDisplay;
import org.junit.jupiter.api.Test;

public class ClientTest {

    @Test
    public void contextRun() {
    }

    @Test
    public void run() throws InterruptedException {
        Thread thread = new Thread(() -> UsersDisplay.main(new String[] {}));
        thread.start();
        Thread.sleep(2000);
    }
}
