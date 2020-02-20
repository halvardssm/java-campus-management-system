//package nl.tudelft.oopp.demo.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.env.Environment;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableJpaRepositories(basePackages = {
//        "nl.tudelft.oopp.demo.repositories",
//        "nl.tudelft.oopp.demo.objects.building",
//        "nl.tudelft.oopp.demo.objects.room.repositories",
//        "nl.tudelft.oopp.demo.objects.roomFacility"
//})
//@PropertySource("application.properties")
//@EnableTransactionManagement
//public class MysqlConfig {
//
//    @Autowired
//    private Environment environment;
//
//    /**
//     * Set up the connection to the database.
//     */
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
//
////    @Bean
////    public QuoteRepository quoteRepository() {
////        QuoteRepository repos = new JpaRepository<Quote, Long>;
////        return repos;
////    }
//}