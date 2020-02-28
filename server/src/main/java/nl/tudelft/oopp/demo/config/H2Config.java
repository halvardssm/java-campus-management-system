package nl.tudelft.oopp.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;


//@Configuration
//@EnableJpaRepositories
//@PropertySource("application-dev.properties")
//@EnableTransactionManagement
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
//        return dataSource;
//    }
}
