package com.example.Foundation.service;

import com.example.Foundation.Enum.UserType;
import com.example.Foundation.modal.Admin;
import com.example.Foundation.modal.Donor;
import com.example.Foundation.modal.Student;
import com.example.Foundation.modal.Trainer;
import com.example.Foundation.repositories.AdminRepository;
import com.example.Foundation.repositories.DonorRepository;
import com.example.Foundation.repositories.StudentRepository;
import com.example.Foundation.repositories.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetails implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmailAddress(username);
        Donor donor = donorRepository.findByEmailAddress(username);
        Student student = studentRepository.findByEmailAddress(username);
        Trainer trainer = trainerRepository.findByEmailAddress(username);

        // Determine the user type and return the appropriate user object
        if (admin != null) {
            return admin;
        } else if (donor != null) {
            return donor;
        } else if (student != null) {
            return student;
        } else if (trainer != null) {
            return trainer;
        } else {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

    }




    // Method to determine the userType based on the UserDetails object
    public UserType determineUserType(UserDetails userDetails) {
        if (userDetails instanceof Admin) {
            return UserType.ADMIN;
        } else if (userDetails instanceof Donor) {
            return UserType.DONOR;
        } else if (userDetails instanceof Student) {
            return UserType.STUDENT;
        } else if (userDetails instanceof Trainer) {
            return UserType.TRAINER;
        } else {
            throw new UsernameNotFoundException("userType is not defined");
        }
    }

}
