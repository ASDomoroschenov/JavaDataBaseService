package ru.mai.javaservice.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class PersonDAOImpl implements PersonDAO {
    JdbcTemplate jdbcTemplate;
    private final String SQL_FIND_PERSON = "select * from application_schema.person where person_id = ?";
    private final String SQL_DELETE_PERSON = "delete from application_schema.person where person_id = ?";
    private final String SQL_UPDATE_PERSON = "update application_schema.person set first_name = ?, last_name = ? where person_id = ?";
    private final String SQL_GET_ALL = "select * from application_schema.person";
    private final String SQL_INSERT_PERSON = "insert into application_schema.person(first_name, last_name, birthday, mail, gender) values(?,?,?,?,?)";

    @Autowired
    public PersonDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Person getPersonById(Long id) {
        return jdbcTemplate.queryForObject(SQL_FIND_PERSON, new Object[]{id}, new PersonMapper());
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
