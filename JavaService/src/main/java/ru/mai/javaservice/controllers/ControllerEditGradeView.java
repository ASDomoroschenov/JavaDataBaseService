package ru.mai.javaservice.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.mai.javaservice.Application;
import ru.mai.javaservice.app_config.AppConfig;
import ru.mai.javaservice.dao.PersonDAO;
import ru.mai.javaservice.dao.ProfessorDAO;
import ru.mai.javaservice.dao.StudentDAO;
import ru.mai.javaservice.objects_database.Student;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerEditGradeView implements Initializable {
    private PersonDAO personDAO;
    private StudentDAO studentDAO;
    private ProfessorDAO professorDAO;
    private ControllerProfessorView controllerProfessorView;
    @FXML
    private TableView<RowTableGrades> tableViewGrades;
    @FXML
    private TextField textFieldSemester;
    @FXML
    private TextField textFieldSubject;
    @FXML
    private TextField textFieldGrade;
    private Student student;

    public ControllerEditGradeView(ControllerProfessorView controllerProfessorView) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.controllerProfessorView = controllerProfessorView;
        this.personDAO = context.getBean(PersonDAO.class);
        this.studentDAO = context.getBean(StudentDAO.class);
        this.professorDAO = context.getBean(ProfessorDAO.class);
        context.close();
    }

    public void chooseStudent() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("choose-student-view.fxml"));
        fxmlLoader.setController(new ControllerChooseStudentView(this));
        Scene scene = new Scene(fxmlLoader.load(), 450, 350);
        stage.setTitle("Выбор студента");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillTableGrades();
    }

    public void setGrade() {
        try {
            int semester = Integer.parseInt(textFieldSemester.getText());
            String subject = textFieldSubject.getText();
            int grade = Integer.parseInt(textFieldGrade.getText());

            int currentSemester = studentDAO.getCurrentSemester(student);
            List<String> subjectList = studentDAO.getSubjectList(student);

            if (professorDAO.existGrade(semester, subject, student)) {
                professorDAO.setGrade(semester, subject, grade, student);
            } else {
                int professorId = controllerProfessorView.getProfessor().getProfessorId();
                professorDAO.addGrade(semester, subject, grade, student, professorId);
            }

            controllerProfessorView.updateTableViewAllGrades();
            updateTableViewGrades();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Неверный формат данных!");
            alert.showAndWait();
        }
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void fillTableGrades() {
        TableColumn<RowTableGrades, String> semesterColumn = new TableColumn<>("Семестр");
        TableColumn<RowTableGrades, String> subjectColumn = new TableColumn<>("Дисциплина");
        TableColumn<RowTableGrades, String> gradeColumn = new TableColumn<>("Оценка");

        setCellFactoryCentered(semesterColumn);
        setCellFactoryCentered(subjectColumn);
        setCellFactoryCentered(gradeColumn);

        semesterColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().semester()));
        subjectColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().subject()));
        gradeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().grade()));

        semesterColumn.setMaxWidth(80);
        gradeColumn.setMaxWidth(80);
        tableViewGrades.getColumns().add(semesterColumn);
        tableViewGrades.getColumns().add(subjectColumn);
        tableViewGrades.getColumns().add(gradeColumn);
    }

    private <T> void setCellFactoryCentered(TableColumn<T, String> column) {
        column.setCellFactory(cell -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item);
                    setAlignment(javafx.geometry.Pos.CENTER);
                }
            }
        });
    }

    public void updateTableViewGrades() {
        List<RowTableGrades> grades = studentDAO.getGrades(student);
        tableViewGrades.getItems().clear();
        ObservableList<RowTableGrades> rowsTable = FXCollections.observableArrayList();
        rowsTable.addAll(grades);
        tableViewGrades.setItems(rowsTable);
    }

    public record RowTableGrades(String semester,
                                 String subject,
                                 String grade) {

    }
}
