package ru.mai.javaservice.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mai.javaservice.dao.StudentDAO;
import ru.mai.javaservice.objects_database.Person;
import ru.mai.javaservice.objects_database.Student;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class ControllerStudentView implements Initializable {
    private final StudentDAO studentDAO;
    @FXML
    private TabPane tabPane;
    @FXML
    private GridPane gridPaneInfo;
    @FXML
    private TableView<RowTableSubjects> tableViewSubjects;
    @FXML
    private TableView<RowTableAvgGrades> tableViewAvgGrades;
    @FXML
    private TableView<RowTableProfessors> tableViewProfessors;
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

        fillTableViewSubjects();
        fillTableViewAvgGrades();
        fillTableViewProfessors();
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

    private void fillTableViewSubjects() {
        TableColumn<RowTableSubjects, String> numberCourseColumn = new TableColumn<>("Курс");
        TableColumn<RowTableSubjects, String> numberSemesterColumn = new TableColumn<>("Семестр");
        TableColumn<RowTableSubjects, String> subjectNameColumn = new TableColumn<>("Дисциплина");
        TableColumn<RowTableSubjects, String> reportFormColumn = new TableColumn<>("Форма сдачи");
        TableColumn<RowTableSubjects, String> gradeSemesterColumn = new TableColumn<>("Оценка");
        int semesters = studentDAO.getCurrentSemester(student);

        setCellFactoryCentered(numberCourseColumn);
        setCellFactoryCentered(numberSemesterColumn);
        setCellFactoryCentered(subjectNameColumn);
        setCellFactoryCentered(reportFormColumn);
        setCellFactoryCentered(gradeSemesterColumn);
        numberCourseColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().course()));
        numberSemesterColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().semester()));
        subjectNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().nameSubject()));
        reportFormColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().reportForm()));
        gradeSemesterColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().grade()));

        ObservableList<RowTableSubjects> rowsTable = FXCollections.observableArrayList();

        for (int i = 1; i <= semesters; i++) {
            List<Triple<String, String, String>> listSubjectsSemester = studentDAO.getSubjectListSemester(student, i);

            for (Triple<String, String, String> subject : listSubjectsSemester) {
                rowsTable.add(new RowTableSubjects(String.valueOf(i / 2 + 1), String.valueOf(i), subject.getLeft(), subject.getMiddle(), subject.getRight()));
            }
        }

        numberCourseColumn.setMaxWidth(60);
        numberSemesterColumn.setMaxWidth(70);
        reportFormColumn.setMaxWidth(150);
        gradeSemesterColumn.setMaxWidth(70);
        tableViewSubjects.getColumns().add(numberCourseColumn);
        tableViewSubjects.getColumns().add(numberSemesterColumn);
        tableViewSubjects.getColumns().add(subjectNameColumn);
        tableViewSubjects.getColumns().add(reportFormColumn);
        tableViewSubjects.getColumns().add(gradeSemesterColumn);
        tableViewSubjects.setItems(rowsTable);
    }

    private void fillTableViewAvgGrades() {
        TableColumn<RowTableAvgGrades, String> nameSubjectColumn = new TableColumn<>("Дисциплина");
        TableColumn<RowTableAvgGrades, String> avgGradeColumn = new TableColumn<>("Средняя оценка");
        TableColumn<RowTableAvgGrades, String> fullAvgGradeColumn = new TableColumn<>("Общая средняя оценка");
        List<Pair<String, String>> listAvgGrades = studentDAO.getAverageGrades(student);
        double sumGrades = 0.0;
        int countGrades = 0;

        setCellFactoryCentered(nameSubjectColumn);
        setCellFactoryCentered(avgGradeColumn);
        setCellFactoryCentered(fullAvgGradeColumn);
        nameSubjectColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().nameSubject()));
        avgGradeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().avgGrade()));
        fullAvgGradeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().fullAvgGrade()));

        ObservableList<RowTableAvgGrades> rowsTable = FXCollections.observableArrayList();

        for (Pair<String, String> avgGrade : listAvgGrades) {
            sumGrades += Double.parseDouble(avgGrade.getRight());
            countGrades++;
            rowsTable.add(new RowTableAvgGrades(avgGrade.getLeft(), avgGrade.getRight(), ""));
        }

        RowTableAvgGrades firstRow = new RowTableAvgGrades(rowsTable.get(0).nameSubject, rowsTable.get(0).avgGrade, String.valueOf(Math.round((sumGrades / countGrades) * 100) / 100.0));
        rowsTable.set(0, firstRow);
        tableViewAvgGrades.getColumns().add(nameSubjectColumn);
        tableViewAvgGrades.getColumns().add(avgGradeColumn);
        tableViewAvgGrades.getColumns().add(fullAvgGradeColumn);
        tableViewAvgGrades.setItems(rowsTable);
    }

    private void fillTableViewProfessors() {
        TableColumn<RowTableProfessors, String> numberCourseColumn = new TableColumn<>("Курс");
        TableColumn<RowTableProfessors, String> numberSemesterColumn = new TableColumn<>("Семестр");
        TableColumn<RowTableProfessors, String> subjectNameColumn = new TableColumn<>("Дисциплина");
        TableColumn<RowTableProfessors, String> professorNameColumn = new TableColumn<>("Преподаватель");
        int semesters = studentDAO.getCurrentSemester(student);

        setCellFactoryCentered(numberCourseColumn);
        setCellFactoryCentered(numberSemesterColumn);
        setCellFactoryCentered(subjectNameColumn);
        setCellFactoryCentered(professorNameColumn);
        numberCourseColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().course()));
        numberSemesterColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().semester()));
        subjectNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().nameSubject()));
        professorNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().fullNameProfessor()));

        ObservableList<RowTableProfessors> rowsTable = FXCollections.observableArrayList();

        for (int i = 1; i <= semesters; i++) {
            List<Pair<String, String>> listSubjectAndProfessor = studentDAO.getListSubjectAndProfessor(student, i);
            for (Pair<String, String> subjectAndProfessor : listSubjectAndProfessor) {
                rowsTable.add(new RowTableProfessors(String.valueOf(i / 2 + 1), String.valueOf(i), subjectAndProfessor.getLeft(), subjectAndProfessor.getRight()));
            }
        }

        numberCourseColumn.setMaxWidth(60);
        numberSemesterColumn.setMaxWidth(70);
        tableViewProfessors.getColumns().add(numberCourseColumn);
        tableViewProfessors.getColumns().add(numberSemesterColumn);
        tableViewProfessors.getColumns().add(subjectNameColumn);
        tableViewProfessors.getColumns().add(professorNameColumn);
        tableViewProfessors.setItems(rowsTable);
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

    public record RowTableSubjects(String course,
                                   String semester,
                                   String nameSubject,
                                   String reportForm,
                                   String grade) {
    }

    public record RowTableAvgGrades(String nameSubject,
                                    String avgGrade,
                                    String fullAvgGrade) {
    }

    public record RowTableProfessors(String course,
                                     String semester,
                                     String nameSubject,
                                     String fullNameProfessor) {
    }
}
