package ru.mai.javaservice.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.mai.javaservice.app_config.AppConfig;
import ru.mai.javaservice.dao.PersonDAO;
import ru.mai.javaservice.dao.ProfessorDAO;
import ru.mai.javaservice.dao.StudentDAO;
import ru.mai.javaservice.objects_database.Person;
import ru.mai.javaservice.objects_database.Student;

import java.sql.Date;

public class ControllerChooseStudentView {
    private PersonDAO personDAO;
    private StudentDAO studentDAO;
    private ProfessorDAO professorDAO;
    private ControllerEditGradeView controllerEditGradeView;
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private TextField textFiledBirthday;
    @FXML
    private RadioButton buttonMale;
    @FXML
    private RadioButton buttonFemale;

    public ControllerChooseStudentView(ControllerEditGradeView controllerEditGradeView) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.professorDAO = context.getBean(ProfessorDAO.class);
        this.studentDAO = context.getBean(StudentDAO.class);
        this.personDAO = context.getBean(PersonDAO.class);
        this.controllerEditGradeView = controllerEditGradeView;
        context.close();
    }

    public void choose() {
        try {
            if (textFieldFirstName.getText().isEmpty() ||
                    textFieldLastName.getText().isEmpty() ||
                    textFiledBirthday.getText().isEmpty() ||
                    !(buttonFemale.isSelected() || buttonMale.isSelected())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Все данные должны быть заполнены!");
                alert.showAndWait();
            } else {
                String firstName = textFieldFirstName.getText();
                String lastName = textFieldLastName.getText();
                Date birthday = Date.valueOf(textFiledBirthday.getText());
                String gender = buttonMale.isSelected() ? "М" : "Ж";

                if (personDAO.exist(firstName, lastName, birthday, gender)) {
                    Person person = personDAO.getPerson(firstName, lastName, birthday, gender);
                    Student student = studentDAO.getStudent(person);
                    controllerEditGradeView.setStudent(student);
                    controllerEditGradeView.updateTableViewGrades();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Такой студент не существует!");
                    alert.showAndWait();
                }
            }
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Неверный формат даты (yyyy-mm-dd)");
            alert.showAndWait();
        }
    }
}
