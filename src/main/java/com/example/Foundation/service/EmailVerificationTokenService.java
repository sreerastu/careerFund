package com.example.Foundation.service;

import com.example.Foundation.modal.EmailVerificationToken;
import com.example.Foundation.repositories.EmailVerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmailVerificationTokenService {

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    public List<EmailVerificationToken> getAllTokens() {
        return emailVerificationTokenRepository.findAll();
    }

    public Optional<EmailVerificationToken> getTokenById(int id) {
        return emailVerificationTokenRepository.findById(id);
    }

    public EmailVerificationToken createToken(EmailVerificationToken token) {
        return emailVerificationTokenRepository.save(token);
    }

    public EmailVerificationToken updateToken(int id, EmailVerificationToken token) {
        if (emailVerificationTokenRepository.existsById(id)) {
            token.setId(id);
            return emailVerificationTokenRepository.save(token);
        }
        return null;
    }

    public void deleteToken(int id) {
        emailVerificationTokenRepository.deleteById(id);
    }


    public List<EmailVerificationToken> findByAssociatedEmailAddress(String emailAddress) {
        return emailVerificationTokenRepository.findByAssociatedEmailAddress(emailAddress);
    }
}
