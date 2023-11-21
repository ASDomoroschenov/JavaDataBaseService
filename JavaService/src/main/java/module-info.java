module ru.mai.javadatabaseservice {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.mai.javaservice to javafx.fxml;
    exports ru.mai.javaservice;
}