package ru.mai.javaservice.objects_database;

import lombok.Data;

@Data
public class Student {
    private int studentId;
    private int groupId;
    private boolean isMaster;
    private Person person;

    public Student(int studentId, int groupId, boolean isMaster, Person person) {
        this.studentId = studentId;
        this.groupId = groupId;
        this.isMaster = isMaster;
        this.person = person;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", groupId=" + groupId +
                ", isMaster=" + isMaster +
                ", person=" + person +
                '}';
    }
}
