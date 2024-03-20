package com.example.Foundation.service;

import com.example.Foundation.exception.AuthenticationException;
import com.example.Foundation.modal.*;
import com.example.Foundation.repositories.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;

@Service
public class EmailService {

    final static Logger log = LoggerFactory.getLogger(EmailService.class);


    @Autowired
    private EmailVerificationTokenService emailVerificationTokenService;


    private StudentRepository studentRepo;
    private TrainerRepository trainerRepo;
    private AdminRepository adminRepo;
    private DonorRepository donorRepo;

    public EmailService(StudentRepository studentRepo, TrainerRepository trainerRepo, AdminRepository adminRepo, DonorRepository donorRepo, JavaMailSender javaMailSender) {
        this.studentRepo = studentRepo;
        this.trainerRepo = trainerRepo;
        this.adminRepo = adminRepo;
        this.donorRepo = donorRepo;
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private final JavaMailSender javaMailSender;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    public String verifyEmail(String emailAddress) throws javax.mail.MessagingException, AuthenticationException {

        String randomPwd = sendTempOtpPassword();


        Student stdByEmail = studentRepo.findByEmailAddress(emailAddress);
        Admin adByEmail = adminRepo.findByEmailAddress(emailAddress);
        Trainer trainerByEmail = trainerRepo.findByEmailAddress(emailAddress);
        Donor donorByEmail = donorRepo.findByEmailAddress(emailAddress);

        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();

        if (stdByEmail != null) {
            emailVerificationToken.setOtp(randomPwd);
            emailVerificationToken.setStudent(stdByEmail);
            emailVerificationToken.setTimeStamp(LocalDateTime.now());
            emailVerificationTokenRepository.save(emailVerificationToken);

        } else if (adByEmail != null) {
            emailVerificationToken.setOtp(randomPwd);
            emailVerificationToken.setAdmin(adByEmail);
            emailVerificationToken.setTimeStamp(LocalDateTime.now());
            emailVerificationTokenRepository.save(emailVerificationToken);
        } else if (trainerByEmail != null) {
            emailVerificationToken.setOtp(randomPwd);
            emailVerificationToken.setTrainer(trainerByEmail);
            emailVerificationToken.setTimeStamp(LocalDateTime.now());
            emailVerificationTokenRepository.save(emailVerificationToken);
        } else if (donorByEmail != null) {
            emailVerificationToken.setOtp(randomPwd);
            emailVerificationToken.setDonor(donorByEmail);
            emailVerificationToken.setTimeStamp(LocalDateTime.now());
            emailVerificationTokenRepository.save(emailVerificationToken);
        } else {
            throw new AuthenticationException("Invalid EmailAddress");
        }

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Email Verification");
        helper.setText("Your Verification Code is " + randomPwd + ",Please use the code  for mail verification");
        helper.setFrom(sender);
        helper.setTo(emailAddress);
        javaMailSender.send(message);

        return "Mail Sent Successfully......!";
    }

    public String resetPassword(String emailAddress, String code, String newPassword, String confirmPassword) throws AuthenticationException {
        // Find the latest EmailVerificationToken based on timestamp
        EmailVerificationToken latestToken = emailVerificationTokenService.findByAssociatedEmailAddress(emailAddress).stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(EmailVerificationToken::getTimeStamp)))
                .findFirst()
                .orElseThrow(() -> new AuthenticationException("OTP does not match"));


        if (latestToken.getOtp().equals(code) && newPassword.equals(confirmPassword)) {

            // Encode the new password with BCrypt
            String encodedPassword = bCryptPasswordEncoder.encode(newPassword);

            Student stdByEmail = studentRepo.findByEmailAddress(emailAddress);
            Admin adByEmail = adminRepo.findByEmailAddress(emailAddress);
            Trainer trainerByEmail = trainerRepo.findByEmailAddress(emailAddress);
            Donor donorByEmail = donorRepo.findByEmailAddress(emailAddress);

            if (stdByEmail != null) {
                stdByEmail.setPassword(encodedPassword);
                studentRepo.save(stdByEmail);
            } else if (adByEmail != null) {
                adByEmail.setPassword(encodedPassword);
                adminRepo.save(adByEmail);
            } else if (trainerByEmail != null) {
                trainerByEmail.setPassword(encodedPassword);
                trainerRepo.save(trainerByEmail);
            } else if (donorByEmail != null) {
                donorByEmail.setPassword(encodedPassword);
                donorRepo.save(donorByEmail);
            } else {
                throw new AuthenticationException("Invalid EmailAddress");
            }
            return "Password reset Successfully......!";
        } else {
            throw new AuthenticationException("OTP or passwords does not match");
        }
    }


    public String sendTempPassword() {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String pwd = RandomStringUtils.random(10, characters);
        log.info(pwd);
        return pwd;
    }


    public String sendTempOtpPassword() {
        String otp = RandomStringUtils.randomNumeric(4); // Generate 4-digit OTP
        log.info(otp);
        return otp;
    }

}
