package ru.mai.javaservice.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class ControllerStudentPerformanceView {
    private final ControllerProfessorView controllerProfessorView;
    @FXML
    private TextField textFieldSubject;
    @FXML
    private TextField textFieldSemester;

    public ControllerStudentPerformanceView(ControllerProfessorView controllerProfessorView) {
        this.controllerProfessorView = controllerProfessorView;
    }

    public void generate() {
        String subject = textFieldSubject.getText();
        int semester = Integer.parseInt(textFieldSemester.getText());
        controllerProfessorView.generateStudentPerformance(subject, semester);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Отчет успешно сгенерирован!");
        alert.showAndWait();
    }
}
