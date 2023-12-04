package ru.mai.javaservice;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.mai.javaservice.controllers.Controller;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("java-service-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 300);
        stage.setResizable(false);
        stage.setTitle("Система базы данных");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}