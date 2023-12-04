package ru.mai.javaservice.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import ru.mai.javaservice.app_config.AppConfig;
import ru.mai.javaservice.person.Person;
import ru.mai.javaservice.dao.PersonDAO;

import java.util.List;

@Component
public class StudentController {
    private final PersonDAO personDAO;
    @FXML
    private Button buttonLoad;
    @FXML
    private TextArea textArea;

    @Autowired
    public StudentController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    public void load() {
        StringBuilder persons = new StringBuilder();
        List<Person> personList = personDAO.getAllPersons();

        for (Person person: personList) {
            persons.append(person).append(System.lineSeparator());
        }

        textArea.setText(persons.toString());
    }
}