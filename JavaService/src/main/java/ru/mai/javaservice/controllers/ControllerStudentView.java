package ru.mai.javaservice.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mai.javaservice.dao.StudentDAO;
import ru.mai.javaservice.objects_database.Person;
import ru.mai.javaservice.objects_database.Student;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class ControllerStudentView implements Initializable {
    @FXML
    private TabPane tabPane;
    @FXML
    private GridPane gridPaneInfo;
    @FXML
    private TableView<RowTable> tableViewSubjects;
    private final StudentDAO studentDAO;
    private Student student;
    private Person person;

    @Autowired
    public ControllerStudentView(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getLabelFromGridPane(0, 1).setText(person.getFirstName());
        getLabelFromGridPane(1, 1).setText(person.getLastName());
        getLabelFromGridPane(2, 1).setText(String.valueOf(person.getBirthday()));
        getLabelFromGridPane(3, 1).setText(person.getMail());
        getLabelFromGridPane(4, 1).setText(person.getGender().equals("М") ? "Мужской" : "Женский");
        getLabelFromGridPane(5, 1).setText(studentDAO.getGroup(student));

        setSubjectNameColumn();
        setSubjectSemestersColumn();
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setStudent() {
        this.student = studentDAO.getStudent(person);
    }

    private Label getLabelFromGridPane(int rowIndex, int colIndex) {
        for (Node node : gridPaneInfo.getChildren()) {
            Integer nodeRowIndex = GridPane.getRowIndex(node);
            Integer nodeColIndex = GridPane.getColumnIndex(node);

            nodeRowIndex = nodeRowIndex == null ? 0 : nodeRowIndex;
            nodeColIndex = nodeColIndex == null ? 0 : nodeColIndex;

            if ((node instanceof Label label) && (nodeRowIndex == rowIndex && nodeColIndex == colIndex)) {
                return label;
            }
        }

        return null;
    }

    private void setSubjectNameColumn() {
//        List<String> listSubject = studentDAO.getSubjectList(student);
//        TableColumn<String, String> subjectNameColumn = new TableColumn<>("Дисциплина");
//        subjectNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
//        tableViewSubjects.getColumns().add(subjectNameColumn);
//        ObservableList<String> data = FXCollections.observableArrayList(listSubject);
//        tableViewSubjects.setItems(data);
    }

    private void setSubjectSemestersColumn() {
        TableColumn<RowTable, String> numberCourseColumn = new TableColumn<>("Курс");
        TableColumn<RowTable, String> numberSemesterColumn = new TableColumn<>("Семестр");
        TableColumn<RowTable, String> subjectNameColumn = new TableColumn<>("Дисциплина");
        TableColumn<RowTable, String> reportFormColumn = new TableColumn<>("Форма сдачи");
        TableColumn<RowTable, String> gradeSemesterColumn = new TableColumn<>("Оценка");
        int semesters = studentDAO.getCurrentSemester(student);

        numberCourseColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCourse()));
        numberSemesterColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSemester()));
        subjectNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameSubject()));
        reportFormColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReportForm()));
        gradeSemesterColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGrade()));

        ObservableList<RowTable> rowsTable = FXCollections.observableArrayList();

        for (int i = 1; i <= semesters; i++) {
            List<Triple<String, String, String>> listSubjectsSemester = studentDAO.getSubjectListSemester(student, i);

            for (Triple<String, String, String> subject : listSubjectsSemester) {
                rowsTable.add(new RowTable(String.valueOf(i / 2 + 1), String.valueOf(i), subject.getLeft(), subject.getMiddle(), subject.getRight()));
            }
        }

        tableViewSubjects.getColumns().add(numberCourseColumn);
        tableViewSubjects.getColumns().add(numberSemesterColumn);
        tableViewSubjects.getColumns().add(subjectNameColumn);
        tableViewSubjects.getColumns().add(reportFormColumn);
        tableViewSubjects.getColumns().add(gradeSemesterColumn);
        tableViewSubjects.setItems(rowsTable);
    }

    @Getter
    public class RowTable {
        private final String course;
        private final String semester;
        private final String nameSubject;
        private final String reportForm;
        private final String grade;

        public RowTable(String course, String semester, String nameSubject, String reportForm, String grade) {
            this.course = course;
            this.semester = semester;
            this.nameSubject = nameSubject;
            this.reportForm = reportForm;
            this.grade = grade;
        }
    }
}
