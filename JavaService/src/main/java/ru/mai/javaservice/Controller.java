package ru.mai.javaservice;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class Controller implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void authorizationStudent() throws IOException {
        Stage stageStudent = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("professor-service-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 500);
        stageStudent.setResizable(false);
        stageStudent.setTitle("Студент");
        stageStudent.setScene(scene);
        stageStudent.show();
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