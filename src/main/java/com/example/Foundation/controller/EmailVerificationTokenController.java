package com.example.Foundation.controller;

import com.example.Foundation.modal.EmailVerificationToken;
import com.example.Foundation.service.EmailVerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class EmailVerificationTokenController {

    @Autowired
    private EmailVerificationTokenService emailVerificationTokenService;

    @GetMapping("/token/all")
    public ResponseEntity<List<EmailVerificationToken>> getAllTokens() {
        List<EmailVerificationToken> tokens = emailVerificationTokenService.getAllTokens();
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

    @GetMapping("/token/{id}")
    public ResponseEntity<EmailVerificationToken> getTokenById(@PathVariable int id) {
        Optional<EmailVerificationToken> token = emailVerificationTokenService.getTokenById(id);
        return token.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/token")
    public ResponseEntity<EmailVerificationToken> createToken(@RequestBody EmailVerificationToken token) {
        EmailVerificationToken createdToken = emailVerificationTokenService.createToken(token);
        return new ResponseEntity<>(createdToken, HttpStatus.CREATED);
    }

    @PutMapping("/update/token/{id}")
    public ResponseEntity<EmailVerificationToken> updateToken(@PathVariable int id,
                                                              @RequestBody EmailVerificationToken token) {
        EmailVerificationToken updatedToken = emailVerificationTokenService.updateToken(id, token);
        if (updatedToken != null) {
            return new ResponseEntity<>(updatedToken, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/token/{id}")
    public ResponseEntity<Void> deleteToken(@PathVariable int id) {
        emailVerificationTokenService.deleteToken(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    @GetMapping("/tokens/{emailAddress}")
    public ResponseEntity<?> getEmailVerificationTokenByEmail(@PathVariable String emailAddress) {
       List< EmailVerificationToken> token = emailVerificationTokenService.findByAssociatedEmailAddress(emailAddress);
        if (token != null) {
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
