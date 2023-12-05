package ru.mai.javaservice.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.mai.javaservice.objects_database.Student;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentMapper implements RowMapper<Student> {
    @Override
    public Student mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Student student = new Student(
                resultSet.getInt("student_id"),
                resultSet.getInt("group_id"),
                resultSet.getBoolean("is_master_group"),
                null
        );

        return student;
    }
}
