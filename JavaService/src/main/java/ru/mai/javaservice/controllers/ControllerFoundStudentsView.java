package ru.mai.javaservice.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.mai.javaservice.app_config.AppConfig;
import ru.mai.javaservice.dao.StudentDAO;
import ru.mai.javaservice.objects_database.Student;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerFoundStudentsView implements Initializable {
    private StudentDAO studentDAO;
    @FXML
    private TableView<RowTableStudents> foundStudentsTableView;
    private List<Student> studentsList;

    public ControllerFoundStudentsView(List<Student> studentsList) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.studentDAO = context.getBean(StudentDAO.class);
        this.studentsList = studentsList;
        context.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillTableViewStudents();
    }

    public void fillTableViewStudents() {
        TableColumn<RowTableStudents, String> courseColumn = new TableColumn<>("Курс");
        TableColumn<RowTableStudents, String> groupColumn = new TableColumn<>("Группа");
        TableColumn<RowTableStudents, String> firstNameColumn = new TableColumn<>("Имя");
        TableColumn<RowTableStudents, String> lastNameColumn = new TableColumn<>("Фамилия");

        setCellFactoryCentered(courseColumn);
        setCellFactoryCentered(groupColumn);
        setCellFactoryCentered(firstNameColumn);
        setCellFactoryCentered(lastNameColumn);
        courseColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().course()));
        groupColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().group()));
        firstNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().firstName()));
        lastNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().lastName()));

        ObservableList<RowTableStudents> rowsTable = FXCollections.observableArrayList();

        for (Student student : studentsList) {
            rowsTable.add(new RowTableStudents(
                    String.valueOf(studentDAO.getCourse(student)),
                    studentDAO.getGroup(student),
                    student.getPerson().getFirstName(),
                    student.getPerson().getLastName()));
        }

        foundStudentsTableView.getColumns().add(courseColumn);
        foundStudentsTableView.getColumns().add(groupColumn);
        foundStudentsTableView.getColumns().add(firstNameColumn);
        foundStudentsTableView.getColumns().add(lastNameColumn);
        foundStudentsTableView.setItems(rowsTable);
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
}
