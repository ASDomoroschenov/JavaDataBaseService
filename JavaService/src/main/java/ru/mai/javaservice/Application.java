package ru.mai.javaservice;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ru.mai.javaservice.app_config.AppConfig;
import ru.mai.javaservice.controllers.ControllerAuthorizationView;

import javax.sql.DataSource;
import java.io.IOException;

@Component
public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("authorization-view.fxml"));
        fxmlLoader.setController(context.getBean(ControllerAuthorizationView.class));
        Scene scene = new Scene(fxmlLoader.load(), 450, 370);
        stage.setResizable(false);
        stage.setTitle("Авторизация");
        stage.setScene(scene);
        stage.show();
        context.close();
    }

    public static void main(String[] args) {
        launch();
    }
}