package com.example.Foundation.service;

import com.example.Foundation.exception.InvalidStudentIdException;
import com.example.Foundation.exception.StudentNotFoundException;
import com.example.Foundation.exception.TrainerNotFoundException;
import com.example.Foundation.modal.Student;
import com.example.Foundation.modal.Trainer;
import com.example.Foundation.repositories.AdminRepository;
import com.example.Foundation.repositories.DonorRepository;
import com.example.Foundation.repositories.StudentRepository;
import com.example.Foundation.repositories.TrainerRepository;
import com.example.Foundation.util.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService, UserDetailsService {

    private static String UPLOADS_DIR = "./src/main/resources/static/uploads/";

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private JWTUtility jwtUtil;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Student createStudent(Student student, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            // Save image file to uploads directory
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            Path path = Paths.get(UPLOADS_DIR + fileName);
            Files.write(path, file.getBytes());
            student.setImage(fileName);
        }
        return studentRepository.save(student);
    }

    public Student updateStudent(int studentId, Student student) throws InvalidStudentIdException {
        Student existingStudent = studentRepository.findById(student.getStudentId())
                .orElseThrow(() -> new InvalidStudentIdException("Please enter a valid studentId"));

        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setGender(student.getGender());
        existingStudent.setCourse(student.getCourse());
        existingStudent.setEmailAddress(student.getEmailAddress());
        existingStudent.setPassword(this.bCryptPasswordEncoder.encode(student.getPassword()));
        existingStudent.setContactNumber(student.getContactNumber());
        if (student.getImage() != null) {
            existingStudent.setImage(student.getImage());
        }
        return studentRepository.save(existingStudent);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(int studentId) throws InvalidStudentIdException {
        return studentRepository.findById(studentId).orElseThrow(() -> new InvalidStudentIdException("Invalid StudentId"));
    }

    @Override
    public String deleteStudentById(int studentId) throws StudentNotFoundException {
        try {
            studentRepository.deleteById(studentId);

        } catch (Exception ex) {
            throw new StudentNotFoundException("invalid vendorId passed");
        }
        return "Student Successfully deleted" + studentId;
    }

    @Override
    public Student login(String emailAddress, String password) {
        return studentRepository.findByEmailAddressAndPassword(emailAddress, password);
    }

    @Override
    public Student getStdByEmail(String emailAddress) {
        return studentRepository.findByEmailAddress(emailAddress);
    }

    public void assignStudentToTrainer(int trainerId, int studentId) throws TrainerNotFoundException {
        Trainer trainer = trainerRepository.findById(trainerId).orElseThrow(() -> new TrainerNotFoundException("trainer not found with Id" + ":" + trainerId));
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new TrainerNotFoundException("student not found with Id" + ":" + studentId));

        if (trainer != null && student != null) {
            trainer.setStudent(student);
            trainerRepository.save(trainer);
        }
    }

    public void unAssignStudentFromTrainer(int trainerId, int studentId) {
        Trainer trainer = trainerRepository.findById(trainerId).orElse(null);
        Student student = studentRepository.findById(studentId).orElse(null);

        if (trainer != null && student != null) {
            // Set the trainer reference to null to unAssign
            trainer.setStudent(null);
            studentRepository.save(student);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        Student student = studentRepository.findByEmailAddress(emailAddress);
        if (student == null) {
            throw new UsernameNotFoundException("Student not found with email address: " + emailAddress);
        }
        return new org.springframework.security.core.userdetails.User(student.getEmailAddress(), student.getPassword(), student.getAuthorities());
    }

    public List<Student> getAllPlacedStudents() {
        List<Student> placedStudents = studentRepository.findAll();
        List<Student> collect = placedStudents.stream().filter(x -> x.getPlaced() == true).collect(Collectors.toList());
        return collect;
    }
}
