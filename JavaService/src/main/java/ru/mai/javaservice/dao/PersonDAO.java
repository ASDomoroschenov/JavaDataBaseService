package ru.mai.javaservice.dao;

import ru.mai.javaservice.objects_database.Person;

import java.sql.Date;
import java.util.List;

public interface PersonDAO {
    List<Person> getPersonByName(String firstName);
    List<Person> getPersonsMin(String firstName, String lastName);
    List<Person> getPersonsPart(String firstName, String lastName, Date birthday);
    List<Person> getPersonsFull(String firstName, String lastName, Date birthday, String gender);

    Person getPerson(String firstName, String lastName, Date birthday, String gender);

    boolean exist(String firstName, String lastName, Date birthday, String gender);

    String personRole(Person person);
}
