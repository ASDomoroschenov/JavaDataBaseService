package ru.mai.javaservice.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ru.mai.javaservice.Application;
import ru.mai.javaservice.app_config.AppConfig;
import ru.mai.javaservice.dao.PersonDAO;
import ru.mai.javaservice.objects_database.Person;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

@Component
public class ControllerAuthorizationView implements Initializable {
    private final PersonDAO personDAO;
    @FXML
    private Button buttonAuthorization;
    @FXML
    private TextArea textArea;
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private TextField textFieldBirthday;
    @FXML
    private RadioButton maleGender;
    @FXML
    private RadioButton femaleGender;

    @Autowired
    public ControllerAuthorizationView(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textFieldFirstName.setText("Александр");
        textFieldLastName.setText("Доморощенов");
        textFieldBirthday.setText("1989-02-09");
        maleGender.setSelected(true);
    }

    public void authorization() {
        try {
            String firstName = textFieldFirstName.getText();
            String lastName = textFieldLastName.getText();
            Date birthday = Date.valueOf(textFieldBirthday.getText());
            String gender = maleGender.isSelected() ? "М" : "Ж";

            if (personDAO.exist(firstName, lastName, birthday, gender)) {
                Person person = personDAO.getPerson(firstName, lastName, birthday, gender);
                String role = personDAO.personRole(person);
                loadView(person, role);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Такого пользователя не существует");
                alert.showAndWait();
            }
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Неверный формат даты (yyyy-mm-dd)");
            alert.showAndWait();
        } catch (IOException e) {

        }
    }

    private void loadView(Person person, String role) throws IOException {
        Stage stage = new Stage();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        FXMLLoader fxmlLoader = null;

        if (role.equals("Студент")) {
            fxmlLoader = new FXMLLoader(Application.class.getResource("student-view.fxml"));
            ControllerStudentView controllerStudentView = context.getBean(ControllerStudentView.class);
            controllerStudentView.setPerson(person);
            controllerStudentView.setStudent();
            fxmlLoader.setController(controllerStudentView);
            stage.setTitle("Студент");
        } else {
            fxmlLoader = new FXMLLoader(Application.class.getResource("professors-view.fxml"));
            ControllerProfessorView controllerProfessorView = context.getBean(ControllerProfessorView.class);
            controllerProfessorView.setPerson(person);
            controllerProfessorView.setProfessor();
            fxmlLoader.setController(controllerProfessorView);
            stage.setTitle("Преподаватель");
        }

        Scene scene = new Scene(fxmlLoader.load(), 580, 500);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        context.close();
    }
}
