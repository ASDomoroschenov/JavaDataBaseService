package ru.mai.javaservice.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.mai.javaservice.person.Person;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet resultSet, int i) throws SQLException {
        Person person = new Person(
                resultSet.getLong("person_id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("birthday"),
                resultSet.getString("mail"),
                resultSet.getString("gender"));

        return person;
    }
}
