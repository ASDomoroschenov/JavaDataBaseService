package ru.mai.javaservice.dao;

import ru.mai.javaservice.objects_database.Person;
import ru.mai.javaservice.objects_database.Professor;

public interface ProfessorDAO {
    Professor getProfessor(Person person);
}
