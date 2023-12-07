package ru.mai.javaservice.dao_impl;

import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.mai.javaservice.controllers.ControllerProfessorView;
import ru.mai.javaservice.dao.ProfessorDAO;
import ru.mai.javaservice.database_connection.DataBaseConnection;
import ru.mai.javaservice.objects_database.Person;
import ru.mai.javaservice.objects_database.Professor;
import ru.mai.javaservice.objects_database.Student;

import java.util.List;

@Repository
public class ProfessorDAOImpl implements ProfessorDAO {
    private final JdbcTemplate jdbcTemplate;
    private static final String GET_LIST_STUDENTS = """
                SELECT
                    application_schema.group.course AS course,
                    application_schema.group.group_name AS group_name,
                    application_schema.person.first_name AS first_name,
                    application_schema.person.last_name AS last_name
                FROM application_schema.person
                JOIN application_schema.student ON application_schema.person.person_id = application_schema.student.person_id
                JOIN application_schema.group ON application_schema.student.group_id = application_schema.group.group_id;
            """;
    private static final String GET_GROUP_ID = """
                SELECT application_schema.group.group_id
                FROM application_schema.group
                WHERE application_schema.group.group_name = ?;
            """;
    private static final String GET_STUDENT_ID = """
                SELECT application_schema.student.student_id
                FROM application_schema.student
                WHERE group_id = ?
                AND is_master_group = ?
                AND person_id = ?;
            """;
    private static final String GET_PERSON_ID = """
                SELECT application_schema.person.person_id
                FROM application_schema.person
                WHERE first_name = ?
                  AND last_name = ?
                  AND birthday = ?
                  AND mail = ?
                  AND gender = ?;
            """;
    private static final String ADD_PERSON = """
            INSERT INTO application_schema.person(first_name, last_name, birthday, mail, gender)
                   VALUES(?, ?, ?, ?, ?);
            """;
    private static final String ADD_STUDENT = """
                INSERT INTO application_schema.student(group_id, is_master_group, person_id)
                       VALUES(?, ?, ?);
            """;
    private static final String DELETE_FROM_GRADE = """
                DELETE FROM application_schema.grade
                WHERE application_schema.grade.student_id = ?;
            """;
    private static final String DELETE_STUDENT = """
                DELETE FROM application_schema.student
                WHERE application_schema.student.student_id = ?;
            """;
    private static final String DELETE_PERSON = """
                DELETE FROM application_schema.person
                WHERE application_schema.person.person_id = ?;
            """;
    private static final String ALL_GRADES = """
                SELECT
                    application_schema.group.group_name,
                    application_schema.person.first_name,
                    application_schema.person.last_name,
                    application_schema.subject.subject_name,
                    CEIL((application_schema.session.date_end - application_schema.group.start_date_studying) / (30.5 * 6)) AS semester,
                    application_schema.grade.grade
                FROM application_schema.grade
                JOIN application_schema.subject ON application_schema.grade.subject_id = application_schema.subject.subject_id
                JOIN application_schema.session ON application_schema.grade.session_id = application_schema.session.session_id
                JOIN application_schema.student ON application_schema.grade.student_id = application_schema.student.student_id
                JOIN application_schema.group ON application_schema.student.group_id = application_schema.group.group_id
                JOIN application_schema.person ON application_schema.student.person_id = application_schema.person.person_id
                ORDER BY application_schema.group.group_name DESC, semester;
            """;
    private static final String GET_SUBJECT_ID = """
                SELECT application_schema.subject.subject_id
                FROM application_schema.subject
                WHERE application_schema.subject.subject_name = ?;
            """;
            private static final String EXIST_GRADE = """
                SELECT EXISTS(SELECT *
                              FROM application_schema.grade
                              WHERE student_id = ?
                                AND subject_id = ?
                                AND CEIL(
                                                (
                                                        (SELECT application_schema.session.date_end
                                                         FROM application_schema.session
                                                         WHERE application_schema.session.session_id =
                                                               application_schema.grade.session_id)
                                                        -
                                                        (SELECT application_schema.group.start_date_studying
                                                         FROM application_schema.group
                                                         WHERE application_schema.group.group_id =
                                                               (SELECT application_schema.student.group_id
                                                                FROM application_schema.student
                                                                WHERE application_schema.student.student_id =
                                                                      application_schema.grade.student_id))
                                                    )
                                                / (30.5 * 6)) = ?
                                        );
            """;
    private static final String SET_GRADE = """
                UPDATE application_schema.grade
                SET grade = ?
                WHERE student_id = ?
                AND subject_id = ?
                AND CEIL(
                    (
                        (
                            SELECT application_schema.session.date_end
                            FROM application_schema.session
                            WHERE application_schema.session.session_id = application_schema.grade.session_id
                        )
                        -
                        (
                            SELECT application_schema.group.start_date_studying
                            FROM application_schema.group
                            WHERE application_schema.group.group_id = (
                                SELECT application_schema.student.group_id
                                FROM application_schema.student
                                WHERE application_schema.student.student_id = application_schema.grade.student_id
                            )
                        )
                    )
                    / (30.5 * 6)) = ?
            """;
    private static final String GET_STUDENT_PERFORMANCE = """
                SELECT application_schema.group.course,
                       application_schema.group.group_name,
                       application_schema.person.first_name,
                       application_schema.person.last_name,
                       application_schema.grade.grade
                FROM application_schema.grade
                JOIN application_schema.student ON application_schema.grade.student_id = application_schema.student.student_id
                JOIN application_schema.person ON application_schema.student.person_id = application_schema.person.person_id
                JOIN application_schema.subject ON application_schema.grade.subject_id = application_schema.subject.subject_id
                JOIN application_schema.group ON application_schema.student.group_id = application_schema.group.group_id
                JOIN application_schema.session ON application_schema.grade.session_id = application_schema.session.session_id
                WHERE application_schema.subject.subject_id = ?
                AND   CEIL((application_schema.session.date_end - application_schema.group.start_date_studying) / (30.5 * 6)) = ?;
            """;
    private static final String PASSED_SESSION = """
                SELECT application_schema.group.course,
                       application_schema.group.group_name,
                       application_schema.person.first_name,
                       application_schema.person.last_name
                FROM application_schema.grade
                         JOIN application_schema.student ON application_schema.grade.student_id = application_schema.student.student_id
                         JOIN application_schema.person ON application_schema.student.person_id = application_schema.person.person_id
                         JOIN application_schema.subject ON application_schema.grade.subject_id = application_schema.subject.subject_id
                         JOIN application_schema.group ON application_schema.student.group_id = application_schema.group.group_id
                         JOIN application_schema.session ON application_schema.grade.session_id = application_schema.session.session_id
                WHERE application_schema.grade.grade > 2
                AND CEIL((application_schema.session.date_end - application_schema.group.start_date_studying) / (30.5 * 6)) = ?
                GROUP BY application_schema.group.course,
                         application_schema.group.group_name,
                         application_schema.person.first_name,
                         application_schema.person.last_name;
            """;
    private static final String GROUP_PERFORMANCE = """
                SELECT application_schema.group.group_name AS group_name,
                       application_schema.subject.subject_name AS subject_name,
                       AVG(application_schema.grade.grade) AS average_grade
                FROM application_schema.grade
                JOIN application_schema.subject_and_professor ON application_schema.grade.subject_id = application_schema.subject_and_professor.subject_id
                JOIN application_schema.subject ON application_schema.grade.subject_id = application_schema.subject.subject_id
                JOIN application_schema.professor ON application_schema.subject_and_professor.professor_id = application_schema.professor.professor_id
                JOIN application_schema.person ON application_schema.professor.person_id = application_schema.person.person_id
                JOIN application_schema.student ON application_schema.grade.student_id = application_schema.student.student_id
                JOIN application_schema.group ON application_schema.student.group_id = application_schema.group.group_id
                GROUP BY application_schema.group.group_name,
                         application_schema.subject.subject_name
                ORDER BY group_name DESC;
            """;
    private static final String ADD_GRADE = """
            INSERT INTO application_schema.grade (professor_id, student_id, subject_id, session_id, reporting_from, grade)
            VALUES (?, ?, ?, ?, ?, ?);
            """;
    private static final String GET_PROFESSOR = """
                SELECT *
                FROM application_schema.professor
                WHERE application_schema.professor.person_id = ?;
            """;

    @Autowired
    public ProfessorDAOImpl(DataBaseConnection dataBaseConnection) {
        jdbcTemplate = dataBaseConnection.getJdbcTemplate();
    }

    @Override
    public Professor getProfessor(Person person) {
        return jdbcTemplate.queryForObject(GET_PROFESSOR, (resulSet, rowNum) -> new Professor(
                resulSet.getInt("professor_id"),
                resulSet.getInt("person_id")
        ), person.getId());
    }

    @Override
    public List<ControllerProfessorView.RowTableStudents> getListStudents() {
        return jdbcTemplate.query(GET_LIST_STUDENTS, (resultSet, rowNum) -> new ControllerProfessorView.RowTableStudents(
                resultSet.getString("course"),
                resultSet.getString("group_name"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name")
        ));
    }

    @Override
    public int getGroupId(String nameGroup) {
        return jdbcTemplate.queryForObject(GET_GROUP_ID, Integer.class, nameGroup);
    }

    @Override
    public int addPerson(Person person) {
        jdbcTemplate.update(ADD_PERSON, person.getFirstName(), person.getLastName(), person.getBirthday(), person.getMail(), person.getGender());
        return jdbcTemplate.queryForObject(GET_PERSON_ID, Integer.class, person.getFirstName(), person.getLastName(), person.getBirthday(), person.getMail(), person.getGender());
    }

    @Override
    public void addStudent(Student student) {
        int personId = addPerson(student.getPerson());
        jdbcTemplate.update(ADD_STUDENT, student.getGroupId(), student.isMaster(), personId);
    }

    @Override
    public void deleteStudent(Student student) {
        Person person = student.getPerson();
        int personId = jdbcTemplate.queryForObject(GET_PERSON_ID, Integer.class, person.getFirstName(), person.getLastName(), person.getBirthday(), person.getMail(), person.getGender());
        int studentId = jdbcTemplate.queryForObject(GET_STUDENT_ID, Integer.class, student.getGroupId(), student.isMaster(), personId);
        jdbcTemplate.update(DELETE_FROM_GRADE, studentId);
        jdbcTemplate.update(DELETE_STUDENT, studentId);
        jdbcTemplate.update(DELETE_PERSON, personId);
    }

    @Override
    public List<ControllerProfessorView.RowTableAllGrades> getAllGrades() {
        return jdbcTemplate.query(ALL_GRADES, (resultSet, rowNum) -> new ControllerProfessorView.RowTableAllGrades(
                resultSet.getString("group_name"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("subject_name"),
                resultSet.getString("semester"),
                resultSet.getString("grade")
        ));
    }

    @Override
    public boolean existGrade(int semester, String subject, Student student) {
        int subjectId = jdbcTemplate.queryForObject(GET_SUBJECT_ID, Integer.class, subject);
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(EXIST_GRADE, Boolean.class, student.getStudentId(), subjectId, semester));
    }

    @Override
    public void setGrade(int semester, String subject, int grade, Student student) {
        int subjectId = jdbcTemplate.queryForObject(GET_SUBJECT_ID, Integer.class, subject);
        jdbcTemplate.update(SET_GRADE, grade, student.getStudentId(), subjectId, semester);
    }

    @Override
    public List<ControllerProfessorView.RowTableStudentPerformance> getStudentPerformance(String subject, int semester) {
        int subjectId = jdbcTemplate.queryForObject(GET_SUBJECT_ID, Integer.class, subject);
        return jdbcTemplate.query(GET_STUDENT_PERFORMANCE, (resultSet, rowNum) -> new ControllerProfessorView.RowTableStudentPerformance(
                resultSet.getString("course"),
                resultSet.getString("group_name"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("grade")
        ), subjectId, semester);
    }

    @Override
    public List<ControllerProfessorView.RowTableStudents> getPassedSession(int semester) {
        return jdbcTemplate.query(PASSED_SESSION, (resultSet, rowNum) -> new ControllerProfessorView.RowTableStudents(
                resultSet.getString("course"),
                resultSet.getString("group_name"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name")
        ), semester);
    }

    @Override
    public List<ControllerProfessorView.RowTableGroupPerformance> getGroupPerformance() {
        return jdbcTemplate.query(GROUP_PERFORMANCE, (resultSet, rowNum) -> new ControllerProfessorView.RowTableGroupPerformance(
                resultSet.getString("group_name"),
                resultSet.getString("subject_name"),
                String.valueOf(Math.round(Double.parseDouble(resultSet.getString("average_grade")) * 100) / 100.0)
        ));
    }

    @Override
    public void addGrade(int semester, String subject, int grade, Student student, int professorId) {
        int subjectId = jdbcTemplate.queryForObject(GET_SUBJECT_ID, Integer.class, subject);
        jdbcTemplate.update(ADD_GRADE, professorId, student.getStudentId(), subjectId, semester, "Экзамен", grade);
    }
}
