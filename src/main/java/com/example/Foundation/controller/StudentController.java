package com.example.Foundation.controller;

import com.example.Foundation.exception.InvalidStudentIdException;
import com.example.Foundation.exception.StudentNotFoundException;
import com.example.Foundation.exception.TrainerNotFoundException;
import com.example.Foundation.modal.Student;
import com.example.Foundation.modal.Trainer;
import com.example.Foundation.service.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api")
public class StudentController {


    @Autowired
    private StudentServiceImpl studentService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @PostMapping("/register/student")
    public ResponseEntity<?> createStudent(@Valid @ModelAttribute Student student, @RequestParam(required = false) MultipartFile file) throws IOException {
        student.setPassword(this.bCryptPasswordEncoder.encode(student.getPassword()));
        Student createdStudent = studentService.createStudent(student,file);
        return ResponseEntity.status(HttpStatus.OK).body(createdStudent);
    }

    @PatchMapping("/student/{studentId}")
    public ResponseEntity<?> updateStudent(@PathVariable int studentId, @ModelAttribute Student studentX, @RequestParam(required = false) MultipartFile file) throws InvalidStudentIdException, IOException {
        Student student = studentService.updateStudent(studentId, studentX, file);
        return ResponseEntity.status(HttpStatus.OK).body(student);
    }

    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.status(HttpStatus.OK).body(students);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getStudentById(@PathVariable int studentId) throws InvalidStudentIdException {
        Student student = studentService.getStudentById(studentId);
        return ResponseEntity.status(HttpStatus.OK).body(student);
    }

    @DeleteMapping("/student/{studentId}")
    public ResponseEntity<?> deleteStudentById(@PathVariable int studentId) throws StudentNotFoundException {
        studentService.deleteStudentById(studentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/student/{studentId}/assignStudent/{trainerId}")
    public ResponseEntity<?> assignStudentToTrainer(
            @PathVariable int trainerId,
            @PathVariable int studentId
    ) throws TrainerNotFoundException {
        Trainer trainer = studentService.assignStudentToTrainer(trainerId, studentId);
        return ResponseEntity.status(HttpStatus.OK).body(trainer);
    }

    @DeleteMapping("/trainer/{trainerId}/unAssignStudent/{studentId}")
    public ResponseEntity<String> unAssignStudentFromTrainer(
            @PathVariable int trainerId,
            @PathVariable int studentId
    ) {
        studentService.unAssignStudentFromTrainer(trainerId, studentId);
        return ResponseEntity.ok("Student unassigned from trainer successfully");
    }

    @GetMapping("/placedStudents")
    public ResponseEntity<?> getPlacedStudents() {
        List<Student> allPlacedStudents = studentService.getAllPlacedStudents();
        return ResponseEntity.status(HttpStatus.OK).body(allPlacedStudents);
    }
}
