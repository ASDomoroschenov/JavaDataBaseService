package ru.mai.javaservice.dao_impl;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.mai.javaservice.dao.StudentDAO;
import ru.mai.javaservice.database_connection.DataBaseConnection;
import ru.mai.javaservice.mappers.StudentMapper;
import ru.mai.javaservice.objects_database.Person;
import ru.mai.javaservice.objects_database.Student;

import java.util.List;

@Component
public class StudentDAOImpl implements StudentDAO {
    private final JdbcTemplate jdbcTemplate;
    private static final String SUBJECT_LIST = """
            SELECT subject_name
            FROM application_schema.student
            JOIN application_schema.grade ON student.student_id = grade.student_id
            JOIN application_schema.subject ON grade.subject_id = subject.subject_id
            WHERE application_schema.student.student_id = ?;
            """;
    private static final String GET_STUDENT = """
                SELECT *
                FROM application_schema.student
                WHERE application_schema.student.person_id = ?;
            """;
    private static final String GET_GROUP = """
                SELECT application_schema.group.group_name
                FROM application_schema.student
                JOIN application_schema.group ON application_schema.student.group_id = application_schema.group.group_id
                WHERE application_schema.student.student_id = ?
            """;
    private static final String GET_COURSE = """
                SELECT application_schema.group.course
                FROM application_schema.student
                JOIN application_schema.group ON application_schema.student.group_id = application_schema.group.group_id
                WHERE application_schema.student.student_id = ?
            """;
    private static final String GET_CURRENT_SEMESTER = """
                SELECT CEIL((CURRENT_DATE - application_schema.group.start_date_studying) / (30.5 * 6)) AS semester_number
                FROM application_schema.student
                JOIN application_schema.group ON application_schema.student.group_id = application_schema.group.group_id
                WHERE application_schema.student.student_id = ?;
            """;
    private static final String GET_SUBJECT_LIST_SEMESTER = """
                SELECT
                    application_schema.subject.subject_name,
                    application_schema.grade.reporting_from,
                    application_schema.grade.grade
                FROM application_schema.grade
                JOIN application_schema.subject ON application_schema.grade.subject_id = application_schema.subject.subject_id
                JOIN application_schema.session ON application_schema.grade.session_id = application_schema.session.session_id
                JOIN application_schema.student ON application_schema.grade.student_id = application_schema.student.student_id
                JOIN application_schema.group ON application_schema.student.group_id = application_schema.group.group_id
                WHERE application_schema.student.student_id = ?
                  AND CEIL((application_schema.session.date_end - application_schema.group.start_date_studying) / (30.5 * 6)) = ?;
            """;

    @Autowired
    public StudentDAOImpl(DataBaseConnection dataBaseConnection) {
        jdbcTemplate = dataBaseConnection.getJdbcTemplate();
    }

    @Override
    public Student getStudent(Person person) {
        Student student = jdbcTemplate.queryForObject(GET_STUDENT, new StudentMapper(), person.getId());

        if (student != null) {
            student.setPerson(person);
        }

        return student;
    }

    @Override
    public String getGroup(Student student) {
        return jdbcTemplate.queryForObject(GET_GROUP, (resultSet, rowNum) -> resultSet.getString("group_name"), student.getStudentId());
    }

    @Override
    public List<String> getSubjectList(Student student) {
        return jdbcTemplate.queryForList(SUBJECT_LIST, String.class, student.getStudentId());
    }

    @Override
    public Integer getCourse(Student student) {
        return jdbcTemplate.queryForObject(GET_COURSE, (resultSet, rowNum) -> resultSet.getInt("course"), student.getStudentId());
    }

    @Override
    public Integer getCurrentSemester(Student student) {
        return jdbcTemplate.queryForObject(GET_CURRENT_SEMESTER, (resultSet, rowNum) -> resultSet.getInt("semester_number"), student.getStudentId());
    }

    @Override
    public List<Triple<String, String, String>> getSubjectListSemester(Student student, int semester) {
        return jdbcTemplate.query(GET_SUBJECT_LIST_SEMESTER, (resultSet, numRow) -> {
            String subjectName = resultSet.getString("subject_name");
            String reportForm = resultSet.getString("reporting_from");
            String grade = resultSet.getString("grade");
            return Triple.of(subjectName, reportForm, grade);
        }, student.getStudentId(), semester);
    }
}
