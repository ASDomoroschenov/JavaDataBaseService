package ru.mai.javaservice.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.mai.javaservice.Application;
import ru.mai.javaservice.app_config.AppConfig;
import ru.mai.javaservice.dao.PersonDAO;
import ru.mai.javaservice.dao.ProfessorDAO;
import ru.mai.javaservice.dao.StudentDAO;
import ru.mai.javaservice.objects_database.Person;
import ru.mai.javaservice.objects_database.Student;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ControllerCRUDStudentView {
    private PersonDAO personDAO;
    private StudentDAO studentDAO;
    private ProfessorDAO professorDAO;
    private ControllerProfessorView controllerProfessorView;
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private TextField textFiledBirthday;
    @FXML
    private TextField textFieldMail;
    @FXML
    private TextField textFieldGroup;
    @FXML
    private RadioButton buttonMale;
    @FXML
    private RadioButton buttonFemale;

    public ControllerCRUDStudentView(ControllerProfessorView controllerProfessorView) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.professorDAO = context.getBean(ProfessorDAO.class);
        this.studentDAO = context.getBean(StudentDAO.class);
        this.personDAO = context.getBean(PersonDAO.class);
        this.controllerProfessorView = controllerProfessorView;
        context.close();
    }

    public void addStudent() {
        try {
            if (textFieldFirstName.getText().isEmpty() ||
                    textFieldLastName.getText().isEmpty() ||
                    textFiledBirthday.getText().isEmpty() ||
                    textFieldMail.getText().isEmpty() ||
                    textFieldGroup.getText().isEmpty() ||
                    !(buttonFemale.isSelected() || buttonMale.isSelected())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Все данные должны быть заполнены!");
                alert.showAndWait();
            } else {
                String firstName = textFieldFirstName.getText();
                String lastName = textFieldLastName.getText();
                Date birthday = Date.valueOf(textFiledBirthday.getText());
                String mail = textFieldMail.getText();
                String group = textFieldGroup.getText();
                String gender = buttonMale.isSelected() ? "М" : "Ж";

                if (!personDAO.exist(firstName, lastName, birthday, gender)) {
                    Person person = new Person(firstName, lastName, birthday, mail, gender);
                    Student student = new Student(professorDAO.getGroupId(group), false, person);
                    professorDAO.addStudent(student);
                    controllerProfessorView.updateTableViewStudents();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Студент добавлен!");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Такой студент уже существует!");
                    alert.showAndWait();
                }
            }
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Неверный формат даты (yyyy-mm-dd)");
            alert.showAndWait();
        }
    }

    public void deleteStudent() {
        try {
            if (textFieldFirstName.getText().isEmpty() ||
                    textFieldLastName.getText().isEmpty() ||
                    textFiledBirthday.getText().isEmpty() ||
                    textFieldMail.getText().isEmpty() ||
                    textFieldGroup.getText().isEmpty() ||
                    !(buttonFemale.isSelected() || buttonMale.isSelected())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Все данные должны быть заполнены!");
                alert.showAndWait();
            } else {
                String firstName = textFieldFirstName.getText();
                String lastName = textFieldLastName.getText();
                Date birthday = Date.valueOf(textFiledBirthday.getText());
                String mail = textFieldMail.getText();
                String group = textFieldGroup.getText();
                String gender = buttonMale.isSelected() ? "М" : "Ж";

                if (personDAO.exist(firstName, lastName, birthday, gender)) {
                    Person person = personDAO.getPerson(firstName, lastName, birthday, gender);

                    if (studentDAO.exist(professorDAO.getGroupId(group), person)) {
                        Student student = studentDAO.getStudent(person);
                        professorDAO.deleteStudent(student);
                        controllerProfessorView.updateTableViewStudents();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Студент удален!");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Такого студента не существует!");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Такого студента не существует!");
                    alert.showAndWait();
                }
            }
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Неверный формат даты (yyyy-mm-dd)");
            alert.showAndWait();
        }
    }

    public void findStudent() {
        try {
            String firstName = null;
            String lastName = null;
            Date birthday = null;
            String mail = null;
            String group = null;
            String gender = null;
            List<Person> personList = null;
            List<Student> studentsList = new ArrayList<>();

            System.out.println("BEGIN");

            if (!textFieldFirstName.getText().isEmpty() && textFieldLastName.getText().isEmpty() && textFiledBirthday.getText().isEmpty() && !(buttonMale.isSelected() || buttonFemale.isSelected())) {
                firstName = textFieldFirstName.getText();
                personList = personDAO.getPersonByName(firstName);
            } else {
                if (!textFieldFirstName.getText().isEmpty() && !textFieldLastName.getText().isEmpty() && textFiledBirthday.getText().isEmpty() && !(buttonMale.isSelected() || buttonFemale.isSelected())) {
                    firstName = textFieldFirstName.getText();
                    lastName = textFieldLastName.getText();
                    personList = personDAO.getPersonsMin(firstName, lastName);
                } else {
                    if (!textFieldFirstName.getText().isEmpty() && !textFieldLastName.getText().isEmpty() && !textFiledBirthday.getText().isEmpty() && !(buttonMale.isSelected() || buttonFemale.isSelected())) {
                        firstName = textFieldFirstName.getText();
                        lastName = textFieldLastName.getText();
                        birthday = Date.valueOf(textFiledBirthday.getText());
                        personList = personDAO.getPersonsPart(firstName, lastName, birthday);
                    } else {
                        firstName = textFieldFirstName.getText();
                        lastName = textFieldLastName.getText();
                        birthday = Date.valueOf(textFiledBirthday.getText());
                        gender = buttonMale.isSelected() ? "М" : "Ж";
                        personList = personDAO.getPersonsFull(firstName, lastName, birthday, gender);
                    }
                }
            }

            List<Person> students = new ArrayList<>();

            for (Person person : personList) {
                if (personDAO.personRole(person).equals("Студент")) {
                    students.add(person);
                }
            }

            if (!students.isEmpty()) {
                for (Person person : students) {
                    studentsList.add(studentDAO.getStudent(person));
                }
                loadFoundStudentsStage(studentsList);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Такого студента(ов) не существует!");
                alert.showAndWait();
            }
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Неверный формат даты (yyyy-mm-dd)");
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка при загрузке окна найденных студентов!");
            alert.showAndWait();
        }
    }

    private void loadFoundStudentsStage(List<Student> studentsList) throws IOException {
        Stage stageFoundStudents = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("found-students-view.fxml"));
        fxmlLoader.setController(new ControllerFoundStudentsView(studentsList));
        Scene scene = new Scene(fxmlLoader.load(), 580, 500);
        stageFoundStudents.setTitle("Найденные студенты");
        stageFoundStudents.setResizable(false);
        stageFoundStudents.setScene(scene);
        stageFoundStudents.show();
    }
}
