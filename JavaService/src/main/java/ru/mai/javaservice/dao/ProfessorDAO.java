package ru.mai.javaservice.dao;

import org.apache.commons.lang3.tuple.Triple;
import ru.mai.javaservice.controllers.ControllerProfessorView;
import ru.mai.javaservice.objects_database.Person;
import ru.mai.javaservice.objects_database.Professor;
import ru.mai.javaservice.objects_database.Student;

import java.util.List;

public interface ProfessorDAO {
    Professor getProfessor(Person person);

    List<ControllerProfessorView.RowTableStudents> getListStudents();

    int getGroupId(String nameGroup);

    int addPerson(Person person);

    void addStudent(Student student);

    void deleteStudent(Student student);

    List<ControllerProfessorView.RowTableAllGrades> getAllGrades();

    boolean existGrade(int semester, String subject, Student student);

    void setGrade(int semester, String subject, int grade, Student student);

    List<ControllerProfessorView.RowTableStudentPerformance> getStudentPerformance(String subject, int semester);

    List<ControllerProfessorView.RowTableStudents> getPassedSession(int semester);

    List<ControllerProfessorView.RowTableGroupPerformance> getGroupPerformance();

    void addGrade(int semester, String subject, int grade, Student student, int professorId);
}
