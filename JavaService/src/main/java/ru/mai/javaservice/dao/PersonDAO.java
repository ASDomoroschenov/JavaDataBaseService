package ru.mai.javaservice.dao;

import ru.mai.javaservice.objects_database.Person;

import java.sql.Date;

public interface PersonDAO {
    Person getPerson(String firstName, String lastName, Date birthday, String gender);

    boolean exist(String firstName, String lastName, Date birthday, String gender);

    String personRole(Person person);
}
