package com.example.Foundation.controller;

import com.example.Foundation.dto.JwtResponse;
import com.example.Foundation.dto.LoginApiDto;
import com.example.Foundation.exception.AuthenticationException;
import com.example.Foundation.service.*;
import com.example.Foundation.util.JWTUtility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class AuthenticationController {

    private JWTUtility jwtUtil;
    private AuthenticationManager authenticationManager;
    private EmailService emailService;


    public AuthenticationController(JWTUtility jwtUtil, AuthenticationManager authenticationManager, EmailService emailService, StudentServiceImpl studentService, AdminServiceImpl adminService, TrainerServiceImpl trainerService, DonorServiceImpl donorService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }


    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody LoginApiDto loginCredentials) throws AuthenticationException {
        try {
            // Authenticate the user using Spring Security's authentication manager
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginCredentials.getEmailAddress(), loginCredentials.getPassword())
            );

            String token = jwtUtil.generateToken(loginCredentials.getEmailAddress());

            return ResponseEntity.ok(new JwtResponse(token));
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

