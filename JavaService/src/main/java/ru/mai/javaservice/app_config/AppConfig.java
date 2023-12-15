package ru.mai.javaservice.app_config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.mai.javaservice.controllers.ControllerAuthorizationView;
import ru.mai.javaservice.dao.PersonDAO;
import ru.mai.javaservice.dao_impl.PersonDAOImpl;

import javax.sql.DataSource;

@Configuration
@ComponentScan("ru.mai")
@PropertySource(value = "classpath:application.properties")
public class AppConfig {
    @Autowired
    Environment environment;
    private final String URL = "database.url.localhost";
    private final String USER = "database.user";
    private final String DRIVER = "database.driver";
    private final String PASSWORD = "database.password";

    @Bean
    DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(environment.getProperty(URL));
        driverManagerDataSource.setUsername(environment.getProperty(USER));
        driverManagerDataSource.setPassword(environment.getProperty(PASSWORD));
        driverManagerDataSource.setDriverClassName(environment.getProperty(DRIVER));
        return driverManagerDataSource;
    }
}
