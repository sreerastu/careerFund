package com.example.Foundation.service;

import com.example.Foundation.exception.InvalidStudentIdException;
import com.example.Foundation.exception.StudentNotFoundException;
import com.example.Foundation.modal.Student;

import java.util.List;

public interface StudentService {


    Student createStudent(Student student);

    Student updateStudent(int studentId, Student student) throws InvalidStudentIdException;

    List<Student> getAllStudents();

    Student getStudentById(int studentId) throws InvalidStudentIdException;

    String deleteStudentById(int studentId) throws StudentNotFoundException;

    Student login(String emailAddress, String password);
    Student getStdByEmail(String emailAddress);
}