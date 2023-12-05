package ru.mai.javaservice.objects_database;

import lombok.Getter;

@Getter
public class Subject  {
    private String subjectName;

    public Subject(String subjectName) {
        this.subjectName = subjectName;
    }
}
