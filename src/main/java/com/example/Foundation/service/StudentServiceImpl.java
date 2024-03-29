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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService, UserDetailsService {

    @Autowired
    private S3Service s3Service; // Injecting the S3Service

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

    @Value("${aws.s3.StudentFolder}")
    private String folderName;

    @Override
    public Student createStudent(Student student, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            student.setImage(fileName);
            // Upload the image to S3
            s3Service.uploadImageToS3(folderName,fileName, file);
        }
        return studentRepository.save(student);
    }

    public Student updateStudent(int studentId, Student student, MultipartFile file) throws InvalidStudentIdException, IOException {
        Student existingStudent = studentRepository.findById(student.getStudentId())
                .orElseThrow(() -> new InvalidStudentIdException("Please enter a valid studentId"));

        if (student.getFirstName() != null) {
            existingStudent.setFirstName(student.getFirstName());
        }
        if (student.getLastName() != null) {
            existingStudent.setLastName(student.getLastName());
        }
        if (student.getGender() != null) {
            existingStudent.setGender(student.getGender());
        }
        if (student.getCourse() != null) {
            existingStudent.setCourse(student.getCourse());
        }
        if (student.getEmailAddress() != null) {
            existingStudent.setEmailAddress(student.getEmailAddress());
        }
        if (student.getPassword() != null) {
            existingStudent.setPassword(this.bCryptPasswordEncoder.encode(student.getPassword()));
        }
        if (student.getContactNumber() != null) {
            existingStudent.setContactNumber(student.getContactNumber());
        }
        if(student.getUserType() !=null){
            existingStudent.setUserType(student.getUserType());
        }
        if (file != null && !file.isEmpty()) {
            String oldImageName = student.getImage();
            String newImageName = file.getOriginalFilename();
            existingStudent.setImage(newImageName);

            // Upload new image to S3
            s3Service.uploadImageToS3(folderName,newImageName, file);

            // Delete the old image file from S3
            if (oldImageName != null) {
                s3Service.deleteImageFromS3(oldImageName);
            }
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

    public Trainer assignStudentToTrainer(int trainerId, int studentId) throws TrainerNotFoundException {
        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found with ID: " + trainerId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new TrainerNotFoundException("Student not found with ID: " + studentId));

        // Check if the student is already assigned to this trainer
        if (trainer.getStudents().contains(student)) {
            // If the student is already assigned, return with a message
            // You can modify the response format as per your requirement
            throw new RuntimeException("student already assigned!.. ");
        }

        // If the student is not already assigned, proceed with the assignment
        student.setTrainer(trainer); // Set the trainer for the student
        trainer.getStudents().add(student); // Add student to the set of students
        trainerRepository.save(trainer);
        return trainer;
    }



    public void unAssignStudentFromTrainer(int trainerId, int studentId) {
        Trainer trainer = trainerRepository.findById(trainerId).orElse(null);
        Student student = studentRepository.findById(studentId).orElse(null);

        if (trainer != null && student != null) {
            // Remove the student from the set of students associated with the trainer
            trainer.getStudents().remove(student);
            trainerRepository.save(trainer);

            // Update the trainer field in the student entity to null
            student.setTrainer(null);
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
