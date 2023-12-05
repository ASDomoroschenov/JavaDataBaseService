package ru.mai.javaservice.database_connection;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Getter
@Component
public class DataBaseConnection {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DataBaseConnection(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
