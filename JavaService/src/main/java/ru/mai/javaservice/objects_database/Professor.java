package ru.mai.javaservice.objects_database;

import lombok.Data;

@Data
public class Professor {
    private int professorId;
    private Person person;

    public Professor(int professorId, Person person) {
        this.professorId = professorId;
        this.person = person;
    }
}
