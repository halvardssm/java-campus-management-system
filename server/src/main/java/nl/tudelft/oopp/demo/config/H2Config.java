package nl.tudelft.oopp.demo.config;

import javax.sql.DataSource;

import java.sql.Connection;
import nl.tudelft.oopp.demo.entities.Quote;
import nl.tudelft.oopp.demo.repositories.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;


//jdbc:h2:mem:myDb  -> url
//localhost:8080/h2-console
//curl localhost:8080/room

@Configuration
@EnableJpaRepositories(basePackages = {
        "nl.tudelft.oopp.demo.repositories",
        "nl.tudelft.oopp.demo.objects.building",
        "nl.tudelft.oopp.demo.objects.room",
        "nl.tudelft.oopp.demo.objects.roomFacility",
        "nl.tudelft.oopp.demo.objects.facility"
})
//@PropertySource("application.properties")
@EnableTransactionManagement
public class H2Config {

    @Autowired
    private Environment environment;

    /**
     * Set up the connection to the database.
     */
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(environment.getProperty("jdbc.driverClassName"));
//        dataSource.setUrl(environment.getProperty("jdbc.url"));
//        dataSource.setUsername(environment.getProperty("jdbc.user"));
//        dataSource.setPassword(environment.getProperty("jdbc.pass"));
//
////        dataSource.setDriverClassName(environment.getProperty("mysql.driverClassName"));
////        dataSource.setUrl(environment.getProperty("mysql.url"));
////        dataSource.setUsername(environment.getProperty("mysql.user"));
////        dataSource.setPassword(environment.getProperty("mysql.pass"));
//
//        return dataSource;
//    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("springuser");
        dataSource.setPassword("12345678");
        dataSource.setUrl("jdbc:mysql://localhost:3306/db_example?createDatabaseIfNotExist=true&useSSL=false");

        return dataSource;
    }

//    @Bean
//    public QuoteRepository quoteRepository() {
//        QuoteRepository repos = new JpaRepository<Quote, Long>;
//        return repos;
//    }
}
