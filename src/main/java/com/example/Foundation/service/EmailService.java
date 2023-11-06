package com.example.Foundation.service;

import com.example.Foundation.exception.AuthenticationException;
import com.example.Foundation.modal.Admin;
import com.example.Foundation.modal.Donor;
import com.example.Foundation.modal.Student;
import com.example.Foundation.modal.Trainer;
import com.example.Foundation.repositories.AdminRepository;
import com.example.Foundation.repositories.DonorRepository;
import com.example.Foundation.repositories.StudentRepository;
import com.example.Foundation.repositories.TrainerRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    final static Logger log = LoggerFactory.getLogger(EmailService.class);


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


    public String sendEmail(String emailAddress) throws MessagingException, AuthenticationException, javax.mail.MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Password Reset");
        String randomPwd = sendTempPassword();
        helper.setText("Your temporary new password is " + randomPwd + ",Please change your password at profile");
        helper.setFrom(sender);
        helper.setTo(emailAddress);


        Student stdByEmail = studentRepo.findByEmailAddress(emailAddress);
        Admin adByEmail = adminRepo.findByEmailAddress(emailAddress);
        Trainer trainerByEmail = trainerRepo.findByEmailAddress(emailAddress);
        Donor donorByEmail = donorRepo.findByEmailAddress(emailAddress);
        if (stdByEmail != null) {
            stdByEmail.setPassword(randomPwd);
            studentRepo.save(stdByEmail);
        } else if (adByEmail != null) {
            adByEmail.setPassword(randomPwd);
            adminRepo.save(adByEmail);
        } else if (trainerByEmail != null) {
            trainerByEmail.setPassword(randomPwd);
            trainerRepo.save(trainerByEmail);
        } else if (donorByEmail != null) {
            donorByEmail.setEmailAddress(randomPwd);
            donorRepo.save(donorByEmail);
        } else {
            throw new AuthenticationException("Invalid EmailAddress");
        }

        javaMailSender.send(message);
        log.info("Mail Sent Successfully......");
        return "Mail Sent Successfully......!";
    }
    public String verifyEmail(String emailAddress,String code) throws javax.mail.MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Email Verification");
        String randomPwd = sendTempPassword();
        helper.setText("Your Verification Code is " + randomPwd + ",Please use the code  for mail verification");
        helper.setFrom(sender);
        helper.setTo(emailAddress);
        javaMailSender.send(message);

        if(randomPwd == code) {

            log.info("Mail Sent Successfully......");
            return "Mail Sent Successfully......!";
        }
        return "";

    }

    public String sendTempPassword() {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String pwd = RandomStringUtils.random(10, characters);
        log.info(pwd);
        return pwd;
    }

}
