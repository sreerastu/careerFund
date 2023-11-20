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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService, UserDetailsService {

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
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(int studentId, Student student) throws InvalidStudentIdException {
        Student existingStudent = studentRepository.findById(student.getStudentId()).orElseThrow(() -> new InvalidStudentIdException("please enter a valid studentId"));
        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setGender(student.getGender());
        existingStudent.setEmailAddress(student.getEmailAddress());
        existingStudent.setPassword(this.bCryptPasswordEncoder.encode(student.getPassword()));
        existingStudent.setContactNumber(student.getContactNumber());

        return existingStudent;
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


   /* public UserType determineUserType(String emailAddress) {
        Admin admin = adminRepository.findByEmailAddress(emailAddress);
        Trainer trainer = trainerRepository.findByEmailAddress(emailAddress);
        Donor donor = donorRepository.findByEmailAddress(emailAddress);
        Student student = studentRepository.findByEmailAddress(emailAddress);

        if (admin != null) {
            return admin.getUserType(); // Get the userType field from the Admin entity
        } else if (trainer != null) {
            return trainer.getUserType(); // Get the userType field from the Trainer entity
        } else if (donor != null) {
            return donor.getUserType(); // Get the userType field from the Donor entity
        } else {
            return student.getUserType();
        }
    }*/

  /*  public UserType determineUserTypeBasedOnToken(String token) {
        if (token != null) {
            // Decode the token and extract relevant claims
            Claims claims = jwtUtil.getAllClaimsFromToken(token);

            if (claims != null) {
                // Check a claim in the JWT to determine the user type
                String userTypeClaim = claims.get("userType", String.class);

                if (userTypeClaim != null) {
                    // Convert the claim value to a UserType enum
                    try {
                        return UserType.valueOf(userTypeClaim.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        // Handle invalid or unknown user types
                    }
                }
            }
        }
        // Return a default user type or handle cases where the user type can't be determined
        return UserType.UNKNOWN;
    }
*/
    public List<Student> getAllPlacedStudents() {
        List<Student> placedStudents = studentRepository.findAll();
        List<Student> collect = placedStudents.stream().filter(x -> x.getPlaced() == true).collect(Collectors.toList());
        return collect;
    }
}
