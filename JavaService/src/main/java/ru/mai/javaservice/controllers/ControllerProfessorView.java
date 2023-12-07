package ru.mai.javaservice.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mai.javaservice.Application;
import ru.mai.javaservice.dao.ProfessorDAO;
import ru.mai.javaservice.objects_database.Person;
import ru.mai.javaservice.objects_database.Professor;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class ControllerProfessorView implements Initializable {
    @FXML
    private TableView<RowTableStudents> tableViewStudents;
    @FXML
    private TableView<RowTableAllGrades> tableViewAllGrades;
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
        System.out.println(person);
        System.out.println(professor);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillTableViewStudents();
        fillTableViewAllGrades();
    }

    public void actionStudent() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("crud-student-view.fxml"));
        fxmlLoader.setController(new ControllerCRUDStudentView(this));
        Scene scene = new Scene(fxmlLoader.load(), 580, 500);
        stage.setTitle("Действие со студентом");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void editGrades() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("edit-grade-view.fxml"));
        fxmlLoader.setController(new ControllerEditGradeView(this));
        Scene scene = new Scene(fxmlLoader.load(), 700, 400);
        stage.setTitle("Редактирование оценок");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void generateReportStudentPerformance() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("student-performance-view.fxml"));
        fxmlLoader.setController(new ControllerStudentPerformanceView(this));
        Scene scene = new Scene(fxmlLoader.load(), 350, 200);
        stage.setTitle("Генерация отчета");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void generateReportPassedSession() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("passed-session-view.fxml"));
        fxmlLoader.setController(new ControllerPassedSessionView(this));
        Scene scene = new Scene(fxmlLoader.load(), 400, 200);
        stage.setTitle("Генерация отчета");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void generateStudentPerformance(String subject, int semester) {
        List<RowTableStudentPerformance> studentPerformances = professorDAO.getStudentPerformance(subject, semester);
        String filePath = "/home/alexandr/JavaDataBaseService/JavaService/src/main/resources/reports/student-performance.csv";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("Курс,Группа,Имя,Фамилия,Оценка").append(System.lineSeparator());

            for (RowTableStudentPerformance row : studentPerformances) {
                writer.append(row.course())
                      .append(",")
                      .append(row.group())
                      .append(",")
                      .append(row.firstName())
                      .append(",")
                      .append(row.lastName())
                      .append(",")
                      .append(row.grade())
                      .append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Professor getProfessor() {
        return professor;
    }

    public void generatePassedSession(int semester) {
        List<RowTableStudents> students = professorDAO.getPassedSession(semester);
        String filePath = "/home/alexandr/JavaDataBaseService/JavaService/src/main/resources/reports/passed-session.csv";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("Курс,Группа,Имя,Фамилия").append(System.lineSeparator());

            for (RowTableStudents row : students) {
                writer.append(row.course())
                        .append(",")
                        .append(row.group())
                        .append(",")
                        .append(row.firstName())
                        .append(",")
                        .append(row.lastName())
                        .append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateGroupPerformance() {
        List<RowTableGroupPerformance> groupPerformanceList = professorDAO.getGroupPerformance();
        String filePath = "/home/alexandr/JavaDataBaseService/JavaService/src/main/resources/reports/group-performance.csv";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("Группа,Дисциплина,Средняя оценка").append(System.lineSeparator());

            for (RowTableGroupPerformance row : groupPerformanceList) {
                writer.append(row.group())
                        .append(",")
                        .append(row.subject())
                        .append(",")
                        .append(row.avgGrade())
                        .append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Отчет успешно сгенерирован!");
        alert.showAndWait();
    }

    public void fillTableViewStudents() {
        TableColumn<RowTableStudents, String> courseColumn = new TableColumn<>("Курс");
        TableColumn<RowTableStudents, String> groupColumn = new TableColumn<>("Группа");
        TableColumn<RowTableStudents, String> firstNameColumn = new TableColumn<>("Имя");
        TableColumn<RowTableStudents, String> lastNameColumn = new TableColumn<>("Фамилия");
        List<RowTableStudents> students = professorDAO.getListStudents();

        setCellFactoryCentered(courseColumn);
        setCellFactoryCentered(groupColumn);
        setCellFactoryCentered(firstNameColumn);
        setCellFactoryCentered(lastNameColumn);
        courseColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().course()));
        groupColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().group()));
        firstNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().firstName()));
        lastNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().lastName()));

        ObservableList<RowTableStudents> rowsTable = FXCollections.observableArrayList();
        rowsTable.addAll(students);

        tableViewStudents.getColumns().add(courseColumn);
        tableViewStudents.getColumns().add(groupColumn);
        tableViewStudents.getColumns().add(firstNameColumn);
        tableViewStudents.getColumns().add(lastNameColumn);
        tableViewStudents.setItems(rowsTable);
    }

    public void fillTableViewAllGrades() {
        TableColumn<RowTableAllGrades, String> groupColumn = new TableColumn<>("Группа");
        TableColumn<RowTableAllGrades, String> firstNameColumn = new TableColumn<>("Имя");
        TableColumn<RowTableAllGrades, String> lastNameColumn = new TableColumn<>("Фамилия");
        TableColumn<RowTableAllGrades, String> subjectColumn = new TableColumn<>("Дисциплина");
        TableColumn<RowTableAllGrades, String> semesterColumn = new TableColumn<>("Семестр");
        TableColumn<RowTableAllGrades, String> gradeColumn = new TableColumn<>("Оценка");

        setCellFactoryCentered(groupColumn);
        setCellFactoryCentered(firstNameColumn);
        setCellFactoryCentered(lastNameColumn);
        setCellFactoryCentered(subjectColumn);
        setCellFactoryCentered(semesterColumn);
        setCellFactoryCentered(gradeColumn);
        groupColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().group()));
        firstNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().firstName()));
        lastNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().lastName()));
        subjectColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().subjectName()));
        semesterColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().semester()));
        gradeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().grade()));

        ObservableList<RowTableAllGrades> rowsTable = FXCollections.observableArrayList();
        rowsTable.addAll(professorDAO.getAllGrades());

        subjectColumn.setMinWidth(150);
        semesterColumn.setMaxWidth(80);
        gradeColumn.setMaxWidth(80);
        tableViewAllGrades.getColumns().add(groupColumn);
        tableViewAllGrades.getColumns().add(firstNameColumn);
        tableViewAllGrades.getColumns().add(lastNameColumn);
        tableViewAllGrades.getColumns().add(subjectColumn);
        tableViewAllGrades.getColumns().add(semesterColumn);
        tableViewAllGrades.getColumns().add(gradeColumn);
        tableViewAllGrades.setItems(rowsTable);
    }

    public void updateTableViewStudents() {
        tableViewStudents.getItems().clear();
        List<RowTableStudents> students = professorDAO.getListStudents();
        ObservableList<RowTableStudents> rowsTable = FXCollections.observableArrayList();
        rowsTable.addAll(students);
        tableViewStudents.setItems(rowsTable);
    }

    public void updateTableViewAllGrades() {
        tableViewAllGrades.getItems().clear();
        List<RowTableAllGrades> students = professorDAO.getAllGrades();
        ObservableList<RowTableAllGrades> rowsTable = FXCollections.observableArrayList();
        rowsTable.addAll(students);
        tableViewAllGrades.setItems(rowsTable);
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

    public record RowTableStudents(String course,
                                   String group,
                                   String firstName,
                                   String lastName) {

    }

    public record RowTableAllGrades(String group,
                                    String firstName,
                                    String lastName,
                                    String subjectName,
                                    String semester,
                                    String grade) {

    }

    public record RowTableStudentPerformance(String course,
                                             String group,
                                             String firstName,
                                             String lastName,
                                             String grade) {

    }

    public record RowTableGroupPerformance(String group,
                                           String subject,
                                           String avgGrade) {

    }
}
