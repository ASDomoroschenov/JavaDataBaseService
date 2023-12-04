package ru.mai.javaservice.dao_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.mai.javaservice.dao.PersonDAO;
import ru.mai.javaservice.person.Person;
import ru.mai.javaservice.mappers.PersonMapper;

import javax.sql.DataSource;
import java.util.List;

@Component
public class PersonDAOImpl implements PersonDAO {
    JdbcTemplate jdbcTemplate;
    private final String SQL_FIND_PERSON = "SELECT * FROM application_schema.person WHERE person_id = ?";
    private final String SQL_DELETE_PERSON = "DELETE FROM application_schema.person WHERE person_id = ?";
    private final String SQL_UPDATE_PERSON = "UPDATE application_schema.person SET first_name = ?, last_name = ? WHERE person_id = ?";
    private final String SQL_GET_ALL = "SELECT * FROM application_schema.person";
    private final String SQL_INSERT_PERSON = "INSERT INTO application_schema.person(first_name, last_name, birthday, mail, gender) VALUES(?,?,?,?,?)";

    @Autowired
    public PersonDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Person getPersonById(Long id) {
        return (Person) jdbcTemplate.query(SQL_FIND_PERSON, new PersonMapper());
    }

    @Override
    public List<Person> getAllPersons() {
        return jdbcTemplate.query(SQL_GET_ALL, new PersonMapper());
    }

    @Override
    public boolean deletePerson(Person person) {
        return jdbcTemplate.update(SQL_DELETE_PERSON, person.getId()) > 0;
    }

    @Override
    public boolean updatePerson(Person person) {
        return jdbcTemplate.update(SQL_UPDATE_PERSON, person.getFirstName(), person.getLastName(), person.getId()) > 0;
    }

    @Override
    public boolean createPerson(Person person) {
        return jdbcTemplate.update(SQL_INSERT_PERSON, person.getId(), person.getFirstName(), person.getLastName(), person.getBirthday(), person.getMail(), person.getGender()) > 0;
    }
}
