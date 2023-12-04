package ru.mai.javaservice.person;

import lombok.Data;

@Data
public class Person {
    private Long id;
    private String firstName;
    private String lastName;
    private String birthday;
    private String mail;
    private String gender;

    public Person(Long id, String firstName, String lastName, String birthday, String mail, String gender) {
        this.id = id;
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
