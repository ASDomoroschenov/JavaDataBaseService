package ru.mai.javaservice.dao;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import ru.mai.javaservice.controllers.ControllerEditGradeView;
import ru.mai.javaservice.objects_database.Person;
import ru.mai.javaservice.objects_database.Student;

public interface StudentDAO {
    boolean exist(int groupId, Person person);

    Student getStudent(Person person);

    List<String> getSubjectList(Student student);

    String getGroup(Student student);

    Integer getCourse(Student student);

    Integer getCurrentSemester(Student student);

    List<Triple<String, String, String>> getSubjectListSemester(Student student, int semester);

    List<Pair<String, String>> getAverageGrades(Student student);

    List<Pair<String, String>> getListSubjectAndProfessor(Student student, int semester);

    List<ControllerEditGradeView.RowTableGrades> getGrades(Student student);
}
