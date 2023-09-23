package com.example.Foundation.controller;

import com.example.Foundation.dto.LoginApiDto;
import com.example.Foundation.exception.AuthenticationException;
import com.example.Foundation.service.*;
import com.example.Foundation.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class AuthenticationController {

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private EmailService emailService;

    private final StudentServiceImpl studentService;
    private final AdminServiceImpl adminService;
    private final TrainerServiceImpl trainerService;
    private final DonorServiceImpl donorService;

    @Autowired
    public AuthenticationController(StudentServiceImpl studentService, AdminServiceImpl adminService, TrainerServiceImpl trainerService, DonorServiceImpl donorService) {
        this.studentService = studentService;
        this.adminService = adminService;
        this.trainerService = trainerService;
        this.donorService = donorService;
    }


    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody LoginApiDto loginCredentials) throws AuthenticationException {
        try {
            // Authenticate the user using Spring Security's authentication manager
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginCredentials.getEmailAddress(), loginCredentials.getPassword())
            );

            // Determine the user type based on your authentication logic
            String userType = String.valueOf(studentService.determineUserType(loginCredentials.getEmailAddress()));

            // If userType is still null, authentication failed
            if (userType == null) {
                throw new AuthenticationException("Invalid Credentials");
            }

            // Generate a JWT token with the determined user type
            String token = jwtUtil.generateToken(loginCredentials.getEmailAddress(), userType);

            return ResponseEntity.ok(token);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AuthenticationException("Invalid details provided");
        }
    }
/*    @PostMapping("/login")
    public String login(@RequestBody LoginApiDto loginRequest) throws AuthenticationException {

        Student student = studentService.login(loginRequest.getEmailAddress(), loginRequest.getPassword());
        Admin admin = adminService.login(loginRequest.getEmailAddress(), loginRequest.getPassword());
        Trainer trainer = trainerService.login(loginRequest.getEmailAddress(), loginRequest.getPassword());
        Donor donor = donorService.login(loginRequest.getEmailAddress(), loginRequest.getPassword());

        if (student != null) {
            return "student login successfully";
        } else if (admin != null) {
            return "admin login successfully";
        } else if (trainer != null) {
            return "trainer login successfully";
        } else if (donor != null) {
            return "donor login successfully";
        } else {
            throw new AuthenticationException("Invalid Credentials");
        }

    }*/

    @PostMapping("/reset/{emailAddress}")
    public ResponseEntity<?> resetPassword(@PathVariable String emailAddress) throws Exception {

        emailService.sendEmail(emailAddress);

        return ResponseEntity.status(HttpStatus.OK).body("Mail Sent Successfully......!");

    }


}

