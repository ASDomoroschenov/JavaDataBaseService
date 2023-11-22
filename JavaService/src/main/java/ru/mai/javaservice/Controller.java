package ru.mai.javaservice;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.mai.javaservice.app.AppConfig;
import ru.mai.javaservice.person.Person;
import ru.mai.javaservice.person.PersonDAO;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TextArea textArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        StringBuilder stringBuilder = new StringBuilder();
        PersonDAO personDAO = context.getBean(PersonDAO.class);

        for (Person p : personDAO.getAllPersons()) {
            stringBuilder.append(p).append(System.lineSeparator());
        }

        textArea.setText(stringBuilder.toString());

        context.close();
    }
}