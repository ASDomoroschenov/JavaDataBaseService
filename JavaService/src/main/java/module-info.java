module ru.mai.javadatabaseservice {
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.beans;
    requires spring.context;
    requires spring.core;
    requires spring.jdbc;
    requires java.sql;
    requires static lombok;

    opens ru.mai.javaservice to spring.core, javafx.fxml;
    exports ru.mai.javaservice;
    exports ru.mai.javaservice.person;
    opens ru.mai.javaservice.person to javafx.fxml, spring.core;
    exports ru.mai.javaservice.app_config;
    opens ru.mai.javaservice.app_config to javafx.fxml, spring.core;
    exports ru.mai.javaservice.dao;
    opens ru.mai.javaservice.dao to javafx.fxml, spring.core;
    exports ru.mai.javaservice.dao_impl;
    opens ru.mai.javaservice.dao_impl to javafx.fxml, spring.core;
    exports ru.mai.javaservice.mappers;
    opens ru.mai.javaservice.mappers to javafx.fxml, spring.core;
    exports ru.mai.javaservice.controllers;
    opens ru.mai.javaservice.controllers to javafx.fxml, spring.core;
    exports ru.mai.javaservice.app;
    opens ru.mai.javaservice.app to javafx.fxml, spring.core;
}