package ru.mai.javaservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mai.javaservice.dao.ProfessorDAO;
import ru.mai.javaservice.objects_database.Person;
import ru.mai.javaservice.objects_database.Professor;

@Component
public class ControllerProfessorView {
    private final ProfessorDAO professorDAO;
    private Professor professor;
    private Person person;

    @Autowired
    public ControllerProfessorView(ProfessorDAO professorDAO) {
        this.professorDAO = professorDAO;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setProfessor() {
        this.professor = professorDAO.getProfessor(person);
    }
}
