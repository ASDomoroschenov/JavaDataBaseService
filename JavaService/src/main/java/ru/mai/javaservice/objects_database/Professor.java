package ru.mai.javaservice.objects_database;

import lombok.Data;

@Data
public class Professor {
    private int professorId;
    private int person_id;

    public Professor(int professorId, int person_id) {
        this.professorId = professorId;
        this.person_id = person_id;
    }
}
