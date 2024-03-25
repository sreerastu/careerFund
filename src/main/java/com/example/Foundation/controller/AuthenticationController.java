package com.example.Foundation.controller;

import com.example.Foundation.dto.JwtResponse;
import com.example.Foundation.dto.LoginApiDto;
import com.example.Foundation.exception.AuthenticationException;
import com.example.Foundation.service.*;
import com.example.Foundation.util.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    public AuthenticationController(JWTUtility jwtUtil, AuthenticationManager authenticationManager, EmailService emailService, StudentServiceImpl studentService, AdminServiceImpl adminService, TrainerServiceImpl trainerService, DonorServiceImpl donorService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }


    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody LoginApiDto loginCredentials) throws AuthenticationException {

        try {
            // Convert email address to lowercase
            String lowercaseEmail = loginCredentials.getEmailAddress().toLowerCase();

            // Authenticate the user using Spring Security's authentication manager
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(lowercaseEmail, loginCredentials.getPassword())
            );

            String token = jwtUtil.generateToken(loginCredentials.getEmailAddress());

            return ResponseEntity.ok(new JwtResponse(token));
        } catch (BadCredentialsException ex) {
            // Handle invalid credentials separately
            throw new AuthenticationException("Invalid Credentials");
        }
    }


    @PostMapping("/verification/{emailAddress}")
    public ResponseEntity<?> verification(@PathVariable String emailAddress) throws Exception {

        emailService.verifyEmail(emailAddress);

        return ResponseEntity.status(HttpStatus.OK).body("Mail Sent Successfully......!");

    }

    @PostMapping("/resetPassword/{emailAddress}")
    public ResponseEntity<?> resetPassword(@PathVariable String emailAddress, @RequestParam String code, @RequestParam String newPassword, @RequestParam String confirmPassword) throws Exception {

        String response = emailService.resetPassword(emailAddress, code, newPassword, confirmPassword);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
    @PostMapping("/logout")
    public ResponseEntity<String> addToBlacklist(@RequestHeader("Authorization") String token) {
        tokenBlacklistService.addToBlacklist(token.substring(7)); // Remove "Bearer " prefix
        return ResponseEntity.ok("log out successfully");
    }


    @PostMapping("/verify/authenticate")
    public ResponseEntity<?> authenticate(@RequestHeader("Authorization") String token) {
        // Check if the token is blacklisted
        if (tokenBlacklistService.isBlacklisted(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is blacklisted");
        }
        // Token is valid, continue processing
        return ResponseEntity.ok().build();
    }


}

