package com.example.Foundation.controller;

import com.example.Foundation.Enum.UserType;
import com.example.Foundation.dto.JwtResponse;
import com.example.Foundation.dto.LoginApiDto;
import com.example.Foundation.exception.AuthenticationException;
import com.example.Foundation.service.*;
import com.example.Foundation.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class AuthenticationController {

    private JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;
    private EmailService emailService;

    private final StudentServiceImpl studentService;
    private final AdminServiceImpl adminService;
    private final TrainerServiceImpl trainerService;
    private final DonorServiceImpl donorService;

    public AuthenticationController(JwtUtil jwtUtil, AuthenticationManager authenticationManager, EmailService emailService, StudentServiceImpl studentService, AdminServiceImpl adminService, TrainerServiceImpl trainerService, DonorServiceImpl donorService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
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
            UserType userType = studentService.determineUserType(loginCredentials.getEmailAddress());

            // If userType is still null, authentication failed
            if (userType == null) {
                throw new AuthenticationException("Invalid Credentials");
            }

            // Generate a JWT token with the determined user type
            String token = jwtUtil.generateToken(loginCredentials.getEmailAddress(), userType);

            return ResponseEntity.ok(new JwtResponse(token));
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            throw ex; // Rethrow the AuthenticationException
        } catch (BadCredentialsException ex) {
            // Handle invalid credentials separately
            throw new AuthenticationException("Invalid Credentials");
        }
    }


    @PostMapping("/reset/{emailAddress}")
    public ResponseEntity<?> resetPassword(@PathVariable String emailAddress) throws Exception {

        emailService.sendEmail(emailAddress);

        return ResponseEntity.status(HttpStatus.OK).body("Mail Sent Successfully......!");

    }

    @PostMapping("/verification/{emailAddress}/{code}")
    public ResponseEntity<?> verification(@PathVariable String emailAddress, @PathVariable String code) throws Exception {

        emailService.verifyEmail(emailAddress, code);

        return ResponseEntity.status(HttpStatus.OK).body("Mail Sent Successfully......!");

    }


}

