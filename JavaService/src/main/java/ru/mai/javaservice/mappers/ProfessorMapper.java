package ru.mai.javaservice.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.mai.javaservice.objects_database.Professor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfessorMapper implements RowMapper<Professor> {
    @Override
    public Professor mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return null;
    }
}
