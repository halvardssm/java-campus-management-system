package nl.tudelft.oopp.group39;

import nl.tudelft.oopp.group39.config.DbSeeder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CampusManagementApi {

    /**
     * The entry point for the CampusManagementApi.
     *
     * @param args Array of args
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext context;

        context = SpringApplication.run(CampusManagementApi.class, args);

        context.getBean(DbSeeder.class).seedDatabase();
    }
}
