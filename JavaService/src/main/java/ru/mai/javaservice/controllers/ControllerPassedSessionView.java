package ru.mai.javaservice.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class ControllerPassedSessionView {
    private final ControllerProfessorView controllerProfessorView;
    @FXML
    private TextField textFieldSemester;

    public ControllerPassedSessionView(ControllerProfessorView controllerProfessorView) {
        this.controllerProfessorView = controllerProfessorView;
    }

    public void generate() {
        int semester = Integer.parseInt(textFieldSemester.getText());
        controllerProfessorView.generatePassedSession(semester);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Отчет успешно сгенерирован!");
        alert.showAndWait();
    }
}
