package sba.sms.dao;

import sba.sms.models.Course;
import sba.sms.models.Student;

import java.util.List;
import java.util.Set;

public interface StudentI {
    List<Student> getAllStudents();
    void createStudent(Student student);

    Student getStudentByEmail(String email);

    boolean validateStudent(String email, String password);

    void registerStudentToCourse(String email, int courseId);

    Set<Course> getStudentCourses(String email);
}