package ru.mai.javaservice.dao_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.mai.javaservice.dao.PersonDAO;
import ru.mai.javaservice.database_connection.DataBaseConnection;
import ru.mai.javaservice.mappers.PersonMapper;
import ru.mai.javaservice.objects_database.Person;

import java.sql.Date;

@Component
public class PersonDAOImpl implements PersonDAO {
    JdbcTemplate jdbcTemplate;
    private static final String GET_PERSON = """
                SELECT *
                FROM application_schema.person
                WHERE first_name = ?
                  AND last_name = ?
                  AND birthday = ?
                  AND gender = ?
            """;
    private static final String PERSON_EXIST = """
                SELECT EXISTS (SELECT 1
                               FROM application_schema.person
                               WHERE first_name = ?
                                 AND last_name = ?
                                 AND birthday = ?
                                 AND gender = ?);
            """;
    private static final String PERSON_ROLE = """
                SELECT CASE
                           WHEN application_schema.student.student_id IS NOT NULL THEN 'Студент'
                           WHEN application_schema.professor.professor_id IS NOT NULL THEN 'Преподаватель'
                           END AS role
                FROM
                    application_schema.person
                    LEFT JOIN application_schema.student ON application_schema.person.person_id = application_schema.student.person_id
                    LEFT JOIN application_schema.professor ON application_schema.person.person_id = application_schema.professor.person_id
                WHERE application_schema.person.first_name = ?
                  AND application_schema.person.last_name = ?
                  AND application_schema.person.birthday = ?
                  AND application_schema.person.gender = ?;
            """;

    @Autowired
    public PersonDAOImpl(DataBaseConnection dataBaseConnection) {
        jdbcTemplate = dataBaseConnection.getJdbcTemplate();
    }

    @Override
    public Person getPerson(String firstName, String lastName, Date birthday, String gender) {
        return jdbcTemplate.queryForObject(
                GET_PERSON,
                new PersonMapper(),
                firstName,
                lastName,
                birthday,
                gender);
    }

    @Override
    public boolean exist(String firstName, String lastName, Date birthday, String gender) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                PERSON_EXIST,
                Boolean.class,
                firstName,
                lastName,
                birthday,
                gender));
    }

    @Override
    public String personRole(Person person) {
        return jdbcTemplate.queryForObject(
                PERSON_ROLE,
                (resultSet, rowNum) -> resultSet.getString("role"),
                person.getFirstName(),
                person.getLastName(),
                person.getBirthday(),
                person.getGender());
    }
}
