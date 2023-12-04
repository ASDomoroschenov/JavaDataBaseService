package ru.mai.javaservice.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ru.mai.javaservice.Application;
import ru.mai.javaservice.app_config.AppConfig;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller {
    public void authorizationStudent() throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        Stage stageStudent = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("student-service-view.fxml"));
        fxmlLoader.setController(context.getBean("studentController"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 500);
        stageStudent.setResizable(false);
        stageStudent.setTitle("Студент");
        stageStudent.setScene(scene);
        stageStudent.show();

        context.close();
    }

    public void authorizationProfessor() throws IOException {
        Stage stageProfessor = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("professor-service-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 500);
        stageProfessor.setResizable(false);
        stageProfessor.setTitle("Преподаватель");
        stageProfessor.setScene(scene);
        stageProfessor.show();
    }
}