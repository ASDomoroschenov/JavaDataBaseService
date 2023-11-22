module ru.mai.javadatabaseservice {
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.beans;
    requires spring.context;
    requires spring.core;
    requires spring.jdbc;
    requires java.sql;

    opens ru.mai.javaservice to spring.core, javafx.fxml;
    exports ru.mai.javaservice;
    exports ru.mai.javaservice.person;
    opens ru.mai.javaservice.person to javafx.fxml, spring.core;
    exports ru.mai.javaservice.app;
    opens ru.mai.javaservice.app to javafx.fxml, spring.core;
}