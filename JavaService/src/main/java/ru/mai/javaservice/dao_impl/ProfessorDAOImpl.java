package ru.mai.javaservice.dao_impl;

import org.springframework.stereotype.Component;
import ru.mai.javaservice.dao.ProfessorDAO;
import ru.mai.javaservice.objects_database.Person;
import ru.mai.javaservice.objects_database.Professor;

@Component
public class ProfessorDAOImpl implements ProfessorDAO {
    @Override
    public Professor getProfessor(Person person) {
        return null;
    }
}
