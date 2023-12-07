package ru.mai.javaservice.objects_database;

import lombok.Data;

import java.util.Date;

@Data
public class Person {
    private Long id;
    private String firstName;
    private String lastName;
    private Date birthday;
    private String mail;
    private String gender;

    public Person(Long id, String firstName, String lastName, Date birthday, String mail, String gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.mail = mail;
        this.gender = gender;
    }

    public Person(String firstName, String lastName, Date birthday, String mail, String gender) {
        this.id = 0L;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.mail = mail;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return id + " " + firstName + " " + lastName + " " + birthday + " " + mail + " " + gender;
    }
}
